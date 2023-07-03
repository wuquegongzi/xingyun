package cn.cloudcharts.metadata.driver;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.common.utils.DateUtils;
import cn.cloudcharts.common.utils.GsonUtils;
import cn.cloudcharts.common.utils.StringUtils;
import cn.cloudcharts.common.utils.sql.SqlUtil;
import cn.cloudcharts.metadata.convert.ITypeConvert;
import cn.cloudcharts.metadata.convert.StarRocksTypeConvert;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import cn.cloudcharts.metadata.model.result.SqlExplainResult;
import cn.cloudcharts.metadata.sql.IDbSqlGen;
import cn.cloudcharts.metadata.sql.StarRocksDbSqlGen;
import cn.cloudcharts.metadata.task.ITaskOpertion;
import cn.cloudcharts.metadata.task.StarrocksTaskOpertion;
import cn.cloudcharts.metadata.task.SyncTaskGenInfo;
import cn.cloudcharts.metadata.task.schema.SchemaSync;
import cn.cloudcharts.metadata.task.schema.StarrocksSchemaSync;
import cn.cloudcharts.sql.parser.CalciteSqlParser;
import cn.cloudcharts.sql.parser.model.Table;
import cn.cloudcharts.sql.parser.starrocks.StarrocksCalciteParser;
import cn.hutool.core.util.StrUtil;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuque
 * @title: StarRocksDriver
 * @projectName xingyun
 * @description:
 * @date 2023/5/2219:19
 */
public class StarRocksDriver extends AbstractDriver{

    @Override
    public IDbSqlGen getDbSqlGenHelper() {
        return new StarRocksDbSqlGen();
    }

    @Override
    public CalciteSqlParser getCalciteSqlParserHelper() {
        return new StarrocksCalciteParser();
    }

    @Override
    public ITaskOpertion getTaskOpertionHelper() {
        return new StarrocksTaskOpertion();
    }

    @Override
    String getDriverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public ITypeConvert getTypeConvert() {
        return new StarRocksTypeConvert();
    }

    @Override
    public String getType() {
        return "StarRocks";
    }

    @Override
    public boolean execute(String sql) throws Exception {
        AssertUtil.checkNullString(sql, "Sql 语句为空");
        try (Statement statement = conn.get().createStatement()) {
            logger.info("执行sql：{}" + sql);
            statement.execute(sql);
        }
        return true;
    }

    @Override
    public JdbcSelectResult executeSql(String sql, Integer limit) throws Exception {

        String[] statements = SqlUtil.getStatements(SqlUtil.removeNote(sql));
        List<Object> resList = new ArrayList<>();
        JdbcSelectResult result = JdbcSelectResult.buildResult();
        for (String item : statements) {
            String type = item.toUpperCase();
            if (type.startsWith("SELECT") || type.startsWith("SHOW") || type.startsWith("DESC")) {
                if(type.startsWith("SELECT")){
                    item = getDbSqlGenHelper().getExecQuery(item,limit);
                }
                result = query(item, limit);
            } else if (type.startsWith("INSERT")
                    || type.startsWith("UPDATE")
                    || type.startsWith("DELETE")) {
                try {
                    resList.add(executeUpdate(item));
                    result.setStatusList(resList);
                } catch (Exception e) {
                    resList.add(0);
                    result.setStatusList(resList);
                    result.error(e.getMessage());
                    return result;
                }
            } else {
                try {
                    execute(item);
                    resList.add(1);
                    result.setStatusList(resList);
                } catch (Exception e) {
                    resList.add(0);
                    result.setStatusList(resList);
                    result.error(e.getMessage());
                    return result;
                }
            }
        }
        result.success();
        return result;
    }


    @Override
    public List<SqlExplainResult> explain(String sql) {
        return null;
    }

    @Override
    public boolean syncTblMeta(String statement, String schemaFromCatalogName, String schemaFromCatalogDsType) throws SqlParseException, SQLException {

        String dbName;
        try {
            dbName = conn.get().getCatalog();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //解析sql
        List<Table> tblList = getCalciteSqlParserHelper().extractTableList(statement);
        for ( Table tbl : tblList ) {
            if(StringUtils.isEmpty(tbl.getDb())){
                tbl.setDb(dbName);
            }
            if(StringUtils.isEmpty(tbl.getDb())){
                throw new SQLException("Please specify the schema to query the data table , the table name is "+tbl.getTblName());
            }
        }

        //TODO if table 不存在，则直接走catalog查询

        //校验schema tbl 是否存在
        tblList.forEach(tbl ->{

            exsitSchema(tbl.getCatalog(),tbl.getDb(),true);
            if(!exsitTbl(tbl.getCatalog(),tbl.getDb(),tbl.getTblName())){

                // create tbl like catalog.tbl
                List<Map<String,Object>> srcTbl = queryAllColumns(schemaFromCatalogName,tbl.getDb(),tbl.getTblName());

                SchemaSync<CreateTableDTO> schemaSync = new StarrocksSchemaSync();
                CreateTableDTO createTableDTO = schemaSync.transformTbl(tbl.getDb(),tbl.getTblName(),srcTbl,schemaFromCatalogDsType,getType());
                try {
                    createTbl(createTableDTO);

                    //sync data 异步
                    String name = schemaFromCatalogDsType.concat("_").concat(schemaFromCatalogName).concat("_2_").concat(tbl.getDb()).concat("_").concat(tbl.getTblName());
                    String dt = DateUtils.dateTimeNow();
                    String cols = createTableDTO.getCols().stream().map(columnDTO -> {
                        return columnDTO.getColName();
                    }).collect(Collectors.joining(",", "", ""));

                    SyncTaskGenInfo taskGenInfo = new SyncTaskGenInfo();
                    taskGenInfo.setTaskName("async_"+name+"_"+ dt);
                    taskGenInfo.setLabel("insert_load_"+name+"_"+ dt);
                    taskGenInfo.setAsync(true);
                    taskGenInfo.setSrcCatalog(schemaFromCatalogName);
                    taskGenInfo.setSrcDb(tbl.getDb());
                    taskGenInfo.setSrcTbl(tbl.getTblName());
                    taskGenInfo.setSrcCols(cols);
//                    taskGenInfo.setSinkCatalog();
                    taskGenInfo.setSinkDb(tbl.getDb());
                    taskGenInfo.setSinkTbl(tbl.getTblName());
                    taskGenInfo.setSinkCols(cols);
                    taskGenInfo.setInsertType("OVERWRITE");

                    submitSyncTask(taskGenInfo);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return true;
    }

    @Override
    public List<String> getSchemaList(String catalogName) {

        String sql = getDbSqlGenHelper().getSchemaList(catalogName);

        try {
            if (StrUtil.isNotEmpty(sql)) {
                QueryRunner qr = new QueryRunner();
                return  qr.query(conn.get(),sql, new ColumnListHandler<>("Database"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    @Override
    public boolean submitSyncTask(SyncTaskGenInfo taskGenInfo) {
        logger.info("submit task {}", GsonUtils.gsonString(taskGenInfo));
        return  getTaskOpertionHelper().submitSyncTask(conn.get(),taskGenInfo);
    }

    @Override
    public List<Map<String, Object>> getPartitionsList(String schema, String tbl) {

        String sql = getDbSqlGenHelper().getPartitionsList(schema,tbl);

        List<Map<String,Object>> mapList = null;
        try {
            if (StrUtil.isNotEmpty(sql)) {
                QueryRunner qr = new QueryRunner();
                mapList = qr.query(conn.get(),sql, new MapListHandler());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mapList;
    }


}

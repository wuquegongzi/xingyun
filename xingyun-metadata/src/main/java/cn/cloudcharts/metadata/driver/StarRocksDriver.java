package cn.cloudcharts.metadata.driver;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.common.utils.DateUtils;
import cn.cloudcharts.common.utils.GsonUtils;
import cn.cloudcharts.common.utils.sql.SqlUtil;
import cn.cloudcharts.metadata.convert.ITypeConvert;
import cn.cloudcharts.metadata.convert.StarRocksTypeConvert;
import cn.cloudcharts.metadata.lock.ReentrantSpinLock;
import cn.cloudcharts.metadata.model.dto.AlertColumnDTO;
import cn.cloudcharts.metadata.model.dto.ColumnDTO;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import cn.cloudcharts.metadata.model.result.SqlExplainResult;
import cn.cloudcharts.metadata.sql.IDbSqlGen;
import cn.cloudcharts.metadata.sql.StarRocksDbSqlGen;
import cn.cloudcharts.metadata.task.ITaskOpertion;
import cn.cloudcharts.metadata.task.StarrocksTaskOpertion;
import cn.cloudcharts.metadata.task.SyncTaskGenInfo;
import cn.cloudcharts.metadata.task.SyncTblMetaResult;
import cn.cloudcharts.metadata.task.schema.SchemaSync;
import cn.cloudcharts.metadata.task.schema.StarrocksCloumn;
import cn.cloudcharts.metadata.task.schema.StarrocksSchemaSync;
import cn.cloudcharts.sql.parser.CalciteSqlParser;
import cn.cloudcharts.sql.parser.model.Table;
import cn.cloudcharts.sql.parser.starrocks.StarrocksCalciteParser;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuque
 * @title: StarRocksDriver
 * @projectName xingyun
 * @description:
 * @date 2023/5/2219:19
 */
public class StarRocksDriver extends AbstractDriver{

    private ReentrantSpinLock slock = new ReentrantSpinLock();

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
    public boolean syncTbl(List<Table> tblList, String srcCatalog, String srcCatalogType,boolean isAutoCreate, boolean syncDataByMetaChange,Map parmMap){
        tblList.forEach(tbl ->{
            boolean exsitSchema = exsitSchema(tbl.getCatalog(),tbl.getDb(),isAutoCreate);
            if(!exsitSchema){
                throw new ServiceException("不存在该数据库:"+tbl.getCatalog().concat(".").concat(tbl.getDb()));
            }
            SyncTblMetaResult syncTblMetaResult = syncTblMeta(srcCatalogType,srcCatalog,tbl.getDb(),tbl.getTblName(),"default_catalog",tbl.getDb(),tbl.getTblName(),isAutoCreate);
            if((syncDataByMetaChange && syncTblMetaResult.isMetaChange()) || !syncDataByMetaChange){
                syncTblData(srcCatalogType,srcCatalog,tbl.getDb(),tbl.getTblName(),"default_catalog",tbl.getDb(),tbl.getTblName());
            }
        });

        return true;
    }

    private void syncTblData(String srcCatalogType,String srcCatalog,String srcDb,String srcTbl,String sinkCatalog,String sinkDb,String sinkTbl) {

        //sync data 异步
        String name = srcCatalogType.concat("_").concat(srcCatalog).concat("_").concat(srcDb).concat("_").concat(srcTbl).concat("_2_").concat(sinkCatalog).concat("_").concat(sinkDb).concat("_").concat(sinkTbl);
        String dt = DateUtils.dateTimeNow();
        String cols = extractCols(srcCatalog,srcDb,srcTbl);
        SyncTaskGenInfo taskGenInfo = new SyncTaskGenInfo();
        taskGenInfo.setTaskName("async_"+name+"_"+ dt);
        taskGenInfo.setLabel("insert_load_"+name+"_"+ dt);
        taskGenInfo.setAsync(true);
        taskGenInfo.setSrcCatalog(srcCatalog);
        taskGenInfo.setSrcDb(srcDb);
        taskGenInfo.setSrcTbl(srcTbl);
        taskGenInfo.setSrcCols(cols);
        if(!"default_catalog".equals(sinkCatalog)){
            taskGenInfo.setSinkCatalog(sinkCatalog);
        }
        taskGenInfo.setSinkDb(sinkDb);
        taskGenInfo.setSinkTbl(sinkTbl);
        taskGenInfo.setSinkCols(cols);
        taskGenInfo.setInsertType("OVERWRITE");

        submitSyncTask(taskGenInfo);
    }

    private SyncTblMetaResult syncTblMeta(String srcCatalogType, String srcCatalog, String srcDb, String srcTbl, String sinkCatalog, String sinkDb, String sinkTbl, boolean isAutoCreate) {

        List<Map<String,Object>> srcColumnMapList = queryAllColumns(srcCatalog,srcDb,srcTbl);

        SyncTblMetaResult syncTblMetaResult = new SyncTblMetaResult();
        syncTblMetaResult.setSrcColumnMapList(srcColumnMapList);

        // create tbl like catalog.tbl
        if(!exsitTbl(sinkCatalog,srcDb,srcTbl)){
            if(!isAutoCreate){
               throw  new RuntimeException("tbl is not found:"+sinkCatalog.concat(".").concat(srcDb).concat(".").concat(srcTbl));
            }

            SchemaSync<CreateTableDTO> schemaSync = new StarrocksSchemaSync();
            CreateTableDTO createTableDTO = schemaSync.transformTbl(srcDb,srcTbl,srcColumnMapList,srcCatalogType,getType());

            boolean res = false;
            try {
                res = createTbl(createTableDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            syncTblMetaResult.setResult(res);
            if(res){
                syncTblMetaResult.setSinkColumnMapList(queryAllColumns(sinkCatalog,sinkDb,sinkTbl));
            }

            return  syncTblMetaResult;
        }

        //匹配表结构,同步最新结构
        List<Map<String,Object>>  sinkColumnMapList =  queryAllColumns(sinkCatalog,sinkDb,sinkTbl);

        List<StarrocksCloumn> srcColumnList = srcColumnMapList.stream().map(m ->{
            StarrocksCloumn starrocksCloumn = BeanUtil.fillBeanWithMap(m,new StarrocksCloumn(),false);
            return starrocksCloumn;
        }).collect(Collectors.toList());

        List<StarrocksCloumn> sinkColumnList = sinkColumnMapList.stream().map(m ->{
            StarrocksCloumn starrocksCloumn = BeanUtil.fillBeanWithMap(m,new StarrocksCloumn(),false);
            return starrocksCloumn;
        }).collect(Collectors.toList());

        List<StarrocksCloumn> addCloumns =  srcColumnList.stream().filter(srcCloumn -> {
            long exsitCloumn  = sinkColumnList.stream().filter(sinkCloumn -> {
                return sinkCloumn.getField().equals(srcCloumn.getField());
            }).count();

            //sinkCloumn不存在，则添加新字段
            if(exsitCloumn < 1){
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        if( addCloumns.size() <1 ) {
            syncTblMetaResult.setResult(true);
            syncTblMetaResult.setSinkColumnMapList(sinkColumnMapList);
            return syncTblMetaResult;
        }

        List<ColumnDTO> cols = addCloumns.stream().map(starrocksCloumn -> {
            ColumnDTO columnDTO = new ColumnDTO();
            columnDTO.setColName(starrocksCloumn.getField());
            if ("UNKNOWN_TYPE".equals(starrocksCloumn.getType().toUpperCase())) {
                starrocksCloumn.setType("STRING");
            }
            columnDTO.setColType(starrocksCloumn.getType());
            columnDTO.setComment(starrocksCloumn.getComment());
            columnDTO.setDefaultVal(starrocksCloumn.getDefault());
            if (!"YES".equals(starrocksCloumn.getNull())) {
                columnDTO.setNotNull("NOT NULL");
            }
            return columnDTO;
        }).collect(Collectors.toList());

        AlertColumnDTO alertColumnDTO = new AlertColumnDTO();
        alertColumnDTO.setSchemaName(sinkDb);
        alertColumnDTO.setTblName(sinkTbl);
        alertColumnDTO.setCols(cols);

        boolean isAdd = false;
        try {
            isAdd = addColumns(alertColumnDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            //SR alert 是异步操作，需要校验alert操作是否完成
            slock.lock();  // 加锁
            int num = 0; //设置最多半小时
            while (slock.isLock() && num < 360) {
                // 自旋,校验 alert是否结束
                isAdd = tblNormal(sinkDb, sinkTbl, "ALTER");
                if (isAdd) {
                    //alert结束
                    slock.unlock();
                    break;
                }
                try {
                    Thread.sleep(5000);
                    num++;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            slock.unlock();
        }

        syncTblMetaResult.setResult(true);
        syncTblMetaResult.setMetaChange(true);
        if (!isAdd) {
            logger.error("schema job fail,{}", GsonUtils.gsonString(alertColumnDTO));
        }

        syncTblMetaResult.setSinkColumnMapList(queryAllColumns(sinkCatalog,sinkDb,sinkTbl));
        return syncTblMetaResult;
    }


    private String extractCols(String catalog,String db, String tbl) {

        List<Map<String,Object>> srcTblColumns = queryAllColumns(catalog,db,tbl);

        Set<String> keySet = new LinkedHashSet();
        List<Map<String,Object>> partitions = getPartitionsList(db,tbl);
        for (Map<String, Object> map : partitions) {
            keySet.add(String.valueOf(map.getOrDefault("PartitionKey","")));
            keySet.add(String.valueOf(map.getOrDefault("DistributionKey","")));
        }
        return new StarRocksTypeConvert().convertCols(srcTblColumns,"",keySet);

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

    @Override
    public boolean tblNormal(String schema, String tbl, String operType) {

        String sql = getDbSqlGenHelper().tblNormal(schema,tbl,operType);

        List<Map<String,Object>> mapList = null;
        try {
            if (StrUtil.isNotEmpty(sql)) {
                QueryRunner qr = new QueryRunner();
                mapList = qr.query(conn.get(),sql, new MapListHandler());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null != mapList && mapList.size() < 1;
    }


}

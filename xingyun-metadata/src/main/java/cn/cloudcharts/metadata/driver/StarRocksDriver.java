package cn.cloudcharts.metadata.driver;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.common.utils.sql.SqlUtil;
import cn.cloudcharts.metadata.convert.ITypeConvert;
import cn.cloudcharts.metadata.convert.StarRocksTypeConvert;
import cn.cloudcharts.metadata.opertion.IDbOpertion;
import cn.cloudcharts.metadata.opertion.StarRocksDbOpertion;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import cn.cloudcharts.metadata.model.result.SqlExplainResult;
import cn.cloudcharts.sql.parser.CalciteSqlParser;
import cn.cloudcharts.sql.parser.starrocks.StarrocksCalciteParserHelper;
import org.apache.calcite.sql.parser.SqlParseException;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuque
 * @title: StarRocksDriver
 * @projectName xingyun
 * @description:
 * @date 2023/5/2219:19
 */
public class StarRocksDriver extends AbstractDriver{
    @Override
    public IDbOpertion getDbOpertion() {
        return new StarRocksDbOpertion();
    }

    @Override
    public CalciteSqlParser getCalciteSqlParser() {
        return new StarrocksCalciteParserHelper();
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
                    item = getDbOpertion().getExecQuery(item,limit);
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
    public boolean syncTblMeta(String statement, String schemaFromCatalogName, String schemaFromCatalogDsType) throws SqlParseException {

        try {
            String dbName = conn.get().getCatalog();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //解析sql
        List<String> tblList = getCalciteSqlParser().extractTableNameList(statement);

        //校验tbl 是否存在
        tblList.forEach(tbl ->{
//            getDbOpertion().exsitTbl()

        });

        // create tbl like catalog.tbl


        return false;
    }


}

package cn.cloudcharts.starrocks.metadata.driver;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.common.utils.sql.SqlUtil;
import cn.cloudcharts.starrocks.model.result.JdbcSelectResult;
import cn.cloudcharts.starrocks.model.result.SqlExplainResult;

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
    String getDriverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    public boolean execute(String sql) throws Exception {
        AssertUtil.checkNullString(sql, "Sql 语句为空");
        try (Statement statement = conn.get().createStatement()) {
            // logger.info("执行sql的连接id：" + ((DruidPooledConnection)
            // conn).getTransactionInfo().getId());
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
}

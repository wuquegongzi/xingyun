package cn.cloudcharts.sql.parser;

import cn.cloudcharts.sql.parser.model.Table;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;

import java.util.List;

/**
 * @author wuque
 * @title: SqlPaser
 * @projectName xingyun
 * @description:
 * @date 2023/5/1115:28
 */
public interface CalciteSqlParser {

    /**
     * 解析SQL
     * @param sql
     * @return
     */
    List<SqlNode> parser(String sql);

    /**
     * 切割SQL
     * @param sql
     * @return
     */
    List<String> splitSql(String sql);

    /**
     * 解析表名
     * @param sql
     * @return
     */
    List<String> extractTableNameList(String sql) throws SqlParseException;

    List<Table> extractTableList(String sql);



}

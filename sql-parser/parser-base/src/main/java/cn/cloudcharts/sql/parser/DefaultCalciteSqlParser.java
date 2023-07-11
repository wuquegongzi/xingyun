package cn.cloudcharts.sql.parser;

import cn.cloudcharts.sql.parser.model.Table;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParseException;

import java.util.List;

/**
 * @author wuque
 * @title: DefaultCalciteSqlParser
 * @projectName xingyun
 * @description:
 * @date 2023/7/1012:00
 */
public class DefaultCalciteSqlParser implements CalciteSqlParser {
    @Override
    public List<SqlNode> parser(String sql) {
        return null;
    }

    @Override
    public SqlKind getSingleSqlKind(String sql) throws SqlParseException {
        return null;
    }

    @Override
    public List<String> splitSql(String sql) {
        return null;
    }

    @Override
    public List<String> extractTableNameList(String sql) throws SqlParseException {
        return null;
    }

    @Override
    public List<Table> extractTableList(String sql) {
        return null;
    }
}

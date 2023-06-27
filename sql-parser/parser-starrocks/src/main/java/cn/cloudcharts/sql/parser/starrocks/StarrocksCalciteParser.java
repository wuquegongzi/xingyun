package cn.cloudcharts.sql.parser.starrocks;

import cn.cloudcharts.sql.parser.CalciteSqlParser;
import cn.cloudcharts.sql.parser.common.Constant;
import cn.cloudcharts.sql.parser.model.Table;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.dialect.MysqlSqlDialect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuque
 * @title: StarrocksParser
 * @projectName xingyun
 * @description:  starrocks sql解析器
 * @date 2023/5/1115:34
 */
@Slf4j
public class StarrocksCalciteParser implements CalciteSqlParser {

    @Override
    public List<SqlNode> parser(String sql) {

        List<SqlNode> res = Lists.newArrayList();
        SqlParser.Config config = SqlParser
                .configBuilder()
                .setParserFactory(SqlDdlParserImpl.FACTORY)
                .setLex(Lex.MYSQL)
                .build();

        Validate.isTrue(StringUtils.isNoneBlank(sql), "sql 不能能为空");
        Validate.isTrue(!sql.contains(Constant.CHINESE_SEMICOLON), "sql 存在非法字符 =>；");
        Validate.isTrue(!sql.contains(Constant.ChineseComma), "sql 存在非法字符 => ，");

        for (String s : sql.split(Constant.SPLIT)) {
            SqlParser sqlParser = SqlParser.create(s, config);
            try {
                res.add(sqlParser.parseStmt());
            } catch (SqlParseException e) {
               log.error(e.getMessage());
               res.add(null);
            }
        }
        return res;
    }

    @Override
    public SqlKind getSingleSqlKind(String sql) throws SqlParseException {
        // 解析配置
        SqlParser.Config mysqlConfig = SqlParser.configBuilder().setLex(Lex.MYSQL).build();
        // 创建解析器
        SqlParser parser = SqlParser.create(sql, mysqlConfig);
        // 解析sql
        SqlNode sqlNode = parser.parseQuery();

        return sqlNode.getKind();
    }

    @Override
    public List<String> splitSql(String sql) {
        return Arrays.asList(sql.split(Constant.SPLIT));
    }

    @Override
    public List<String> extractTableNameList(String sql) throws SqlParseException {

        SqlParser parser = SqlParser.create(sql);
        SqlNode parsed = parser.parseQuery();

        List<String> tables = new ArrayList<>();
        parseSqlNode(parsed, tables);

        return tables;
    }

    @Override
    public List<Table> extractTableList(String sql) {
        List<String> tbls = null;
        try {
            tbls = extractTableNameList(sql);
        } catch (SqlParseException e) {
            throw new RuntimeException(e);
        }

        if(null == tbls || tbls.size() < 1){
            return Lists.newArrayList();
        }

        List<Table> tableList = tbls.parallelStream().distinct().map(tbl ->{
            String tblArr[] =  tbl.split("\\.");
            Table table = new Table();
            if(tblArr.length == 1){
                table.setTblName(tblArr[0]);
            }else if(tblArr.length == 2){
                table.setDb(tblArr[0]);
                table.setTblName(tblArr[1]);
            }else if(tblArr.length == 3){
                table.setCatalog(tblArr[0]);
                table.setDb(tblArr[1]);
                table.setTblName(tblArr[2]);
            }
            return table;
        }).collect(Collectors.toList());

        return tableList;
    }

    private static void parseFromNode(SqlNode from, List<String> tableNameList) {

        SqlKind kind = from.getKind();
        switch (kind) {
            case IDENTIFIER:
                //最终的表名
                SqlIdentifier sqlIdentifier = (SqlIdentifier) from;
                tableNameList.add(sqlIdentifier.toString());
                break;
            case AS:
                SqlBasicCall sqlBasicCall = (SqlBasicCall) from;
                SqlNode selectNode = sqlBasicCall.getOperandList().get(0);
                parseSqlNode(selectNode, tableNameList);
                break;
            case JOIN:
                SqlJoin sqlJoin = (SqlJoin) from;
                SqlNode left = sqlJoin.getLeft();
                parseFromNode(left, tableNameList);
                SqlNode right = sqlJoin.getRight();
                parseFromNode(right, tableNameList);
                break;
            case SELECT:
                parseSqlNode(from, tableNameList);
                break;
        }

    }


    private static void parseSqlNode(SqlNode sqlNode, List<String> tableNameList) {
        SqlKind kind = sqlNode.getKind();
        switch (kind) {
            case IDENTIFIER:
                parseFromNode(sqlNode, tableNameList);
                break;
            case SELECT:
                SqlSelect select = (SqlSelect) sqlNode;
                parseFromNode(select.getFrom(), tableNameList);
                break;
            case UNION:
                ((SqlBasicCall) sqlNode).getOperandList().forEach(node -> {
                    parseSqlNode(node, tableNameList);
                });

                break;
            case ORDER_BY:
                handlerOrderBy(sqlNode, tableNameList);
                break;
        }
    }

    private static void handlerOrderBy(SqlNode node, List<String> tableNameList) {
        SqlOrderBy sqlOrderBy = (SqlOrderBy) node;
        SqlNode query = sqlOrderBy.query;
        parseSqlNode(query, tableNameList);
    }


    public static void main(String[] args) throws SqlParseException {
        String sql = "select * from stg.test1 t1 " +
                "left join ads.test2 t2 " +
                "on t1.id= t2.id " +
                "where t1.name='pioneeryi';"
                +
                "delete from stg.test1 t1 where t1.id = 1;"
                +
                "update stg.test1 set id = 1  where id >10;"
                +
                "drop table stg.test1";

        CalciteSqlParser sqlParser = new StarrocksCalciteParser();
        List<SqlNode> sqlNodes = sqlParser.parser(sql);

        for (int i = 0; i < sqlNodes.size(); i++) {
            System.out.println(sqlNodes.get(i).getKind().toString());
        }

        String sql2 = "select * from stg.test1 t1 " +
                "left join ads.test2 t2 " +
                "on t1.id= t2.id " +
                "where t1.name='pioneeryi'";

        List<String> s = sqlParser.extractTableNameList(sql2);
        System.out.println(s.toString());


        // Sql语句
       sql = "select * from emps where id = 1";
        // 解析配置
        SqlParser.Config mysqlConfig = SqlParser.configBuilder().setLex(Lex.MYSQL).build();
        // 创建解析器
        SqlParser parser = SqlParser.create(sql, mysqlConfig);
        // 解析sql
        SqlNode sqlNode = parser.parseQuery();
        // 还原某个方言的SQL
        System.out.println(sqlNode.toSqlString(MysqlSqlDialect.DEFAULT));
        System.out.println(sqlNode.getKind().toString());

    }
}

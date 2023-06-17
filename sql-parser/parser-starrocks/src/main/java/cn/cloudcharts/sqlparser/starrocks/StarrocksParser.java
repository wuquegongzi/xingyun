package cn.cloudcharts.sqlparser.starrocks;

import cn.cloudcharts.sqlparser.SqlPaser;
import cn.cloudcharts.sqlparser.common.Constant;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.config.Lex;
import org.apache.calcite.sql.SqlJoin;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlSelect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuque
 * @title: StarrocksParser
 * @projectName xingyun
 * @description:  starrocks sql解析器
 * @date 2023/5/1115:34
 */
@Slf4j
public class StarrocksParser implements SqlPaser {

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
    public List<String> splitSql(String sql) {
        return Arrays.asList(sql.split(Constant.SPLIT));
    }

    @Override
    public List<String> extractTableNameList(String sql) {

        List<String> tables = new ArrayList<>();

        List<SqlNode> sqlNodes = this.parser(sql);
        for (int i = 0; i < sqlNodes.size(); i++) {
            SqlNode node = sqlNodes.get(i);
            node = ((SqlSelect) node).getFrom();
            if (node == null) {
                return tables;
            }

            if (node.getKind() == SqlKind.IDENTIFIER){
                tables.add(node.toString());
            }

            if (node.getKind() == SqlKind.JOIN){
                SqlNode left = ((SqlJoin)node).getLeft();
                if (left.getKind() == SqlKind.IDENTIFIER){
                    tables.add(left.toString());
                }
                SqlNode right = ((SqlJoin)node).getRight();
                if (right.getKind() == SqlKind.IDENTIFIER){
                    tables.add(right.toString());
                }
            }
        }

        return tables;
    }


    public static void main(String[] args) {
        String sql = "select * from stg.test1 t1 " +
                "left join ads.test2 t2 " +
                "on t1.id= t2.id " +
                "where t1.name='pioneeryi';"
                +
                "delete from stg.test1 t1 where t1.id = 1;"
                +
                "update stg.test1 set id = 1  where id >10;"
                +
                "drop table stg.test1;";

        SqlPaser sqlPaser = new StarrocksParser();
        List<SqlNode> sqlNodes = sqlPaser.parser(sql);

        for (int i = 0; i < sqlNodes.size(); i++) {
            System.out.println(sqlNodes.get(i).getKind().toString());
        }

        String sql2 = "select * from stg.test1 t1 " +
                "left join ads.test2 t2 " +
                "on t1.id= t2.id " +
                "where t1.name='pioneeryi';";

        List<String> s = sqlPaser.extractTableNameList(sql2);
        System.out.println(s.toString());


    }
}

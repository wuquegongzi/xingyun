//package cn.cloudcharts.sql.parser.starrocks;
//
//import cn.cloudcharts.sql.parser.starrocks.parser.SelectSingleListener;
//import cn.cloudcharts.sql.parser.starrocks.parser.StarRocksLexer;
//import cn.cloudcharts.sql.parser.starrocks.parser.antlr4.StarRocksParser;
//import org.antlr.v4.runtime.CharStreams;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.tree.ParseTreeWalker;
//
//import java.util.Set;
//
///**
// * @author wuque
// * @title: SqlTest
// * @projectName xingyun
// * @description:
// * @date 2023/6/2112:50
// */
//public class StarRocksParserDdlTest {
//
//    public static void main(String[] args) {
//
//        String sql = "SELECT t1.column1,t1.column2,t1.column3,t2.xy " +
//                "from stg.tableC t1 " +
//                "left join tableA t2 " +
//                "on t1.id=t2.oid " +
//                "where t1.column1 = 1 and t2.yy=6";
////        System.out.println(sql);
//        StarRocksLexer lexer = new StarRocksLexer(CharStreams.fromString(sql.toUpperCase()));
//        StarRocksParser parser = new StarRocksParser(new CommonTokenStream(lexer));
//
//        //定义Listener
//        SelectSingleListener listener = new SelectSingleListener();
//        ParseTreeWalker.DEFAULT.walk(listener, parser.sqlStatements());
//        Set<String> tableNameSet = listener.getTableNameSet();
//        for (String tableName : tableNameSet) {
//            System.out.println(tableName);
//        }
//
//    }
//}

//package cn.cloudcharts.sql.parser.starrocks;
//
//import cn.cloudcharts.sql.parser.starrocks.parser.StarRocksBaseListener;
//import cn.cloudcharts.sql.parser.starrocks.parser.StarRocksLexer;
//import cn.cloudcharts.sql.parser.starrocks.parser.antlr4.StarRocksParser;
//import org.antlr.v4.runtime.CharStreams;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.tree.ParseTreeWalker;
//
///**
// * @author wuque
// * @title: Test
// * @projectName xingyun
// * @description:
// * @date 2023/6/1916:28
// */
//public class StarRocksParserDmlTest {
//
//    public static void main(String[] args) {
//        String sql = "select * from users";
//        StarRocksLexer lexer = new StarRocksLexer(CharStreams.fromString(sql));
//        StarRocksParser parser = new StarRocksParser(new CommonTokenStream(lexer));
//
//        StarRocksBaseListener listener = new StarRocksBaseListener();
//        ParseTreeWalker.DEFAULT.walk(listener, parser.sqlStatements());
////        listener.exitSelectAll();
//
//
//
//    }
//
//
//
//}

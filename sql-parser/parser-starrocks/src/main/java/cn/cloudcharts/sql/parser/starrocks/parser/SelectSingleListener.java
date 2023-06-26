//package cn.cloudcharts.sql.parser.starrocks.parser;
//
//
//import org.antlr.v4.runtime.ParserRuleContext;
//import org.antlr.v4.runtime.tree.ParseTree;
//import org.antlr.v4.runtime.tree.TerminalNode;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static java.util.stream.Collectors.toList;
//
///**
// * @author wuque
// * @title: SelectSingleListener
// * @projectName xingyun
// * @description:
// * @date 2023/6/2112:01
// */
//public class SelectSingleListener extends StarRocksBaseListener {
//
//    private final Set<String> tableNameSet = new HashSet<String>();
//
//
//    @Override
//    public void enterQueryStatement(StarRocksParser.QueryStatementContext ctx) {
//        System.out.println("enterQueryStatement");
//        List<StarRocksParser.QueryStatementContext> contexts = ctx.getRuleContexts(StarRocksParser.QueryStatementContext.class);
//
//        for (StarRocksParser.QueryStatementContext queryStatementContext : contexts){
//            System.out.println("enterQueryStatement:"+queryStatementContext.getText());
//            tableNameSet.add(queryStatementContext.getText());
//        }
//    }
//
//    @Override
//    public void enterTabletList(StarRocksParser.TabletListContext ctx) {
//        System.out.println("enterTabletList");
//
//        List<StarRocksParser.TabletListContext> contexts = ctx.getRuleContexts(StarRocksParser.TabletListContext.class);
//        for (StarRocksParser.TabletListContext tabletListContext : contexts){
//            System.out.println("enterTabletList:"+tabletListContext.getText());
//            tableNameSet.add(tabletListContext.getText());
//        }
//    }
//
//    @Override
//    public void enterSelectSingle(StarRocksParser.SelectSingleContext ctx) {
//
//        System.out.println("enterSelectSingle");
//        List<StarRocksParser.SelectSingleContext> contexts = ctx.getRuleContexts(StarRocksParser.SelectSingleContext.class);
//        for (StarRocksParser.SelectSingleContext selectSingleContext : contexts){
//            System.out.println("enterSelectSingle:"+selectSingleContext.getText());
//            tableNameSet.add(selectSingleContext.getText());
//        }
//    }
//
//    @Override
//    public void enterSqlStatements(StarRocksParser.SqlStatementsContext ctx) {
//        System.out.println("enterSqlStatements");
//        super.enterSqlStatements(ctx);
//    }
//
//    @Override
//    public void enterSingleStatement(StarRocksParser.SingleStatementContext ctx) {
//        System.out.println("enterSingleStatement");
//        super.enterSingleStatement(ctx);
//    }
//
//    @Override
//    public void enterStatement(StarRocksParser.StatementContext ctx) {
//        System.out.println("enterStatement");
//        super.enterStatement(ctx);
//    }
//
//    @Override
//    public void enterCreateTableAsSelectStatement(StarRocksParser.CreateTableAsSelectStatementContext ctx) {
//
//        System.out.println("enterCreateTableAsSelectStatement");
//        super.enterCreateTableAsSelectStatement(ctx);
//    }
//
//    @Override
//    public void enterQueryRelation(StarRocksParser.QueryRelationContext ctx) {
//        System.out.println("enterQueryRelation");
//        super.enterQueryRelation(ctx);
//    }
//
//    @Override
//    public void enterSubquery(StarRocksParser.SubqueryContext ctx) {
//
//        System.out.println("enterSubquery");
//        List<StarRocksParser.SubqueryContext> contexts = ctx.getRuleContexts(StarRocksParser.SubqueryContext.class);
//        System.out.println("----enterSubquery----"+contexts);
//        for (StarRocksParser.SubqueryContext context : contexts){
//            System.out.println("-enterSubquery-"+context.getText());
//            tableNameSet.add(context.getText());
//        }
//    }
//
//    @Override
//    public void enterSetCatalogStatement(StarRocksParser.SetCatalogStatementContext ctx) {
//
//        System.out.println("enterSetCatalogStatement");
//        List<StarRocksParser.SetCatalogStatementContext> contexts = ctx.getRuleContexts(StarRocksParser.SetCatalogStatementContext.class);
//        System.out.println("----enterSetCatalogStatement----"+contexts);
//        for (StarRocksParser.SetCatalogStatementContext context : contexts){
//            System.out.println("-enterSetCatalogStatement-"+context.getText());
//            tableNameSet.add(context.getText());
//        }
//    }
//
//
//    @Override
//    public void enterAnalyzeStatement(StarRocksParser.AnalyzeStatementContext ctx) {
//        System.out.println("enterAnalyzeStatement");
//        QualifiedName qualifiedName = getQualifiedName(ctx.qualifiedName());
//        System.out.println("qualifiedName:"+qualifiedName);
//        TableName tableName =qualifiedNameToTableName(qualifiedName);
//        System.out.println("tableName:"+tableName);
//
//    }
//
//    @Override
//    public void enterTableAtom(StarRocksParser.TableAtomContext ctx) {
//        System.out.println("enterTableAtom");
//        QualifiedName qualifiedName = getQualifiedName(ctx.qualifiedName());
//        System.out.println("qualifiedName:"+qualifiedName);
//        TableName tableName =qualifiedNameToTableName(qualifiedName);
//        System.out.println("tableName:"+tableName);
//        super.enterTableAtom(ctx);
//    }
//
//    private TableName qualifiedNameToTableName(QualifiedName qualifiedName) {
//        // Hierarchy: catalog.database.table
//        List<String> parts = qualifiedName.getParts();
//        if (parts.size() == 3) {
//            return new TableName(parts.get(0), parts.get(1), parts.get(2));
//        } else if (parts.size() == 2) {
//            return new TableName(null, qualifiedName.getParts().get(0), qualifiedName.getParts().get(1));
//        } else if (parts.size() == 1) {
//            return new TableName(null, null, qualifiedName.getParts().get(0));
//        } else {
//            throw new RuntimeException("error table name ");
//        }
//    }
//
//    private QualifiedName getQualifiedName(StarRocksParser.QualifiedNameContext context) {
//        List<String> parts = new ArrayList<>();
//        for (ParseTree c : context.children) {
//            if (c instanceof TerminalNode) {
//                TerminalNode t = (TerminalNode) c;
//                if (t.getSymbol().getType() == StarRocksParser.DOT_IDENTIFIER) {
//                    parts.add(t.getText().substring(1));
//                }
//            } else if (c instanceof StarRocksParser.IdentifierContext) {
//                StarRocksParser.IdentifierContext identifierContext = (StarRocksParser.IdentifierContext) c;
//                Identifier identifier = (Identifier) visit(identifierContext);
//                parts.add(identifier.getValue());
//            }
//        }
//
//        return QualifiedName.of(parts);
//    }
//
//
//
//
//        @Override
//    public void enterSelectAll(StarRocksParser.SelectAllContext ctx) {
//        List<StarRocksParser.SelectAllContext> contexts = ctx.getRuleContexts(StarRocksParser.SelectAllContext.class);
//        System.out.println("----4----"+contexts);
//        for (StarRocksParser.SelectAllContext selectAllContext : contexts){
//            System.out.println("2222-"+selectAllContext.getText());
//            tableNameSet.add(selectAllContext.getText());
//        }
//    }
//
//    public Set<String> getTableNameSet() {
//        return tableNameSet;
//    }
//
//
//
//    private <T> List<T> visit(List<? extends ParserRuleContext> contexts, Class<T> clazz) {
//        return contexts.stream()
//                .map(this::visit)
//                .map(clazz::cast)
//                .collect(toList());
//    }
//
//    private Object visit(ParserRuleContext parserRuleContext) {
//        return parserRuleContext.getText();
//    }
//
//}

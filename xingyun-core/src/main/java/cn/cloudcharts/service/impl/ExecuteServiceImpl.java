package cn.cloudcharts.service.impl;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.common.utils.GsonUtils;
import cn.cloudcharts.common.utils.StringUtils;
import cn.cloudcharts.common.utils.sql.SqlUtil;
import cn.cloudcharts.metadata.driver.Driver;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import cn.cloudcharts.model.entity.Database;
import cn.cloudcharts.model.request.QueryTblColumnRequest;
import cn.cloudcharts.model.request.QueryTblRequest;
import cn.cloudcharts.model.request.SqlExecRequest;
import cn.cloudcharts.model.request.SqlQueryRequest;
import cn.cloudcharts.service.ExecuteService;
import cn.cloudcharts.service.IDatabaseService;
import cn.cloudcharts.sql.parser.model.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wuque
 * @title: ExecuteServiceImpl
 * @projectName xingyun
 * @description: 任务执行
 * @date 2023/5/2320:24
 */
@Slf4j
@Service
public class ExecuteServiceImpl implements ExecuteService {

    @Autowired
    private IDatabaseService databaseService;


    @Override
    public JdbcSelectResult executeCommonSql(SqlExecRequest sqlRequest) {

        Database database = databaseService.getById(sqlRequest.getDatabaseId());
        AssertUtil.isNull(database,"该数据源不存在，请检查！");
        JdbcSelectResult selectResult;

        try {
            Driver driver = Driver.build(database.getDriverConfig());
            selectResult = driver.executeSql(sqlRequest.getStatement(), sqlRequest.getMaxRowNum());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        return selectResult;
    }

    @Override
    public List<Map<String,Object>> queryAllColumns(QueryTblColumnRequest request) {
        Database database = databaseService.getById(request.getDatabaseId());
        AssertUtil.isNull(database,"该数据源不存在，请检查！");

        List<Map<String,Object>> tableColumnResults;
        try {
            Driver driver = Driver.build(database.getDriverConfig());
            tableColumnResults = driver.queryAllColumns(
                    request.getCatalogName(),
                    request.getDbName(),
                    request.getTableName()
            );
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        return tableColumnResults;
    }

    @Override
    public List<String> getTableList(QueryTblRequest request) {
        Database database = databaseService.getById(request.getDatabaseId());
        AssertUtil.isNull(database,"该数据源不存在，请检查！");

        List<String> tables;
        try {
            Driver driver = Driver.build(database.getDriverConfig());
            tables = driver.getTableList(request.getCatalogName(),request.getDbName());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        return tables;
    }

    @Override
    public JdbcSelectResult query(SqlQueryRequest sqlRequest) {

        Database database = databaseService.getById(sqlRequest.getDatabaseId());
        AssertUtil.isNull(database,"该数据源不存在，请检查！");
        JdbcSelectResult selectResult;

        try {
            Driver driver = Driver.build(database.getDriverConfig());

            String[] statements = SqlUtil.getStatements(SqlUtil.removeNote(sqlRequest.getStatement()));
            if(statements.length < 1){
                throw new ServiceException("请输入有效查询SQL!");
            }
            String sql = statements[statements.length - 1];
            if (!sql.toUpperCase().startsWith("SELECT") && !sql.toUpperCase().startsWith("SHOW") && !sql.toUpperCase().startsWith("DESC")) {
                throw new ServiceException("non-standard query statement!");
            }

            if(sqlRequest.isCheckTblExist()){

                String dbName = driver.getConnDbName();
                List<Table> tblList = driver.getCalciteSqlParserHelper().extractTableList(sql);
                for ( Table tbl : tblList ) {
                    if(StringUtils.isEmpty(tbl.getDb())){
                        tbl.setDb(dbName);
                    }
                    if(StringUtils.isEmpty(tbl.getDb())){
                        throw new SQLException("Please specify the schema to query the data table , the table name is "+tbl.getTblName());
                    }
                }

                //校验表是否存在，如果不存在，走catalog
                OptimizeResult optimizeResult = optimizeSql(driver,sql,tblList,sqlRequest.getSchemaFromCatalogName());
                sql = optimizeResult.getSql();

                if(optimizeResult.getTblNotExsitList().size() > 0){
                    driver.syncTbl(optimizeResult.getTblNotExsitList(),sqlRequest.getSchemaFromCatalogName(),sqlRequest.getSchemaFromCatalogDsType(),true,true,null);
                }
            }

            selectResult = driver.query(sql, sqlRequest.getMaxRowNum());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        return selectResult;
    }

    @Data
    public class OptimizeResult {
        private List<Table> tblNotExsitList;
        private String sql;
    }
    private OptimizeResult optimizeSql(Driver driver, String sql, List<Table> tblList, String srcCatalog) {

        Set<String> schemaNotExsitSet = new HashSet();
        Set<Table> tblNotExsitSet = new HashSet();
        for (Table tbl : tblList) {
            if(schemaNotExsitSet.contains(tbl.getDb())){
                tblNotExsitSet.add(tbl);
                continue;
            }
            boolean exsitSchema = driver.exsitSchema(tbl.getCatalog(),tbl.getDb(),false);
            if(!exsitSchema){
                schemaNotExsitSet.add(tbl.getDb());
                tblNotExsitSet.add(tbl);
                continue;
            }

            boolean exsitTbl = driver.exsitTbl(tbl.getCatalog(),tbl.getDb(),tbl.getTblName());
            if(!exsitTbl){
                tblNotExsitSet.add(tbl);
            }
        }

        if( tblNotExsitSet.size() > 0 ){
            for (Table table : tblList) {

                //如果存在unkown_type的字段类型，且SQL查询了该字段或者作为查询条件，则抛出异常[SR的缺陷]
                List<Map<String,Object>> cloumns = driver.queryAllColumns(srcCatalog,table.getDb(),table.getTblName());
                String finalSql = sql;
                long cnt = cloumns.stream().filter(map -> {
                    if("UNKNOWN_TYPE".equals(String.valueOf(map.getOrDefault("Type","")))){
                        String fieldName = String.valueOf(map.get("Field"));
                       /* String nullName = "'' as "+fieldName;
                        finalSql.replaceAll(fieldName,nullName);
                        finalSql.replaceAll(fieldName.toLowerCase(),nullName);
                        finalSql.replaceAll(fieldName.toUpperCase(),nullName);*/
                        if(finalSql.contains(fieldName) || finalSql.contains(fieldName.toLowerCase()) || finalSql.contains(fieldName.toUpperCase())){
                           return true;
                        }
                    }
                    return false;
                }).count();

                if(cnt > 0){
                   throw new ServiceException("OLAP引擎正在同步以下表元数据，请稍后再试:"+ GsonUtils.gsonString(tblList));
                }

                String olds = (StringUtils.isEmpty(table.getCatalog())?"":table.getCatalog().concat(".")).concat(table.getDb()).concat(".").concat(table.getTblName());
                String news = srcCatalog.concat(".").concat(table.getDb()).concat(".").concat(table.getTblName());
                sql = sql.replaceAll(olds,news);
            }

            log.info("转换的SQL：{}",sql);
        }

//        driver.n  判断同步任务 todo

        List<Table>  tbls = Lists.newArrayList();
        tbls.addAll(tblNotExsitSet);

        OptimizeResult optimizeResult = new OptimizeResult();
        optimizeResult.setSql(sql);
        optimizeResult.setTblNotExsitList(tbls);

        return  optimizeResult;
    }


}

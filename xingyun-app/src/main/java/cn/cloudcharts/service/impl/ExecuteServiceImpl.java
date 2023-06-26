package cn.cloudcharts.service.impl;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.model.request.QueryTblColumnRequest;
import cn.cloudcharts.model.request.QueryTblRequest;
import cn.cloudcharts.model.request.SqlExecRequest;
import cn.cloudcharts.model.entity.Database;
import cn.cloudcharts.model.request.SqlQueryRequest;
import cn.cloudcharts.service.ExecuteService;
import cn.cloudcharts.service.IDatabaseService;
import cn.cloudcharts.metadata.driver.Driver;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: ExecuteServiceImpl
 * @projectName xingyun
 * @description: 任务执行
 * @date 2023/5/2320:24
 */
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

            if(sqlRequest.isCheckTblExist()){
                driver.syncTblMeta(sqlRequest.getStatement(),sqlRequest.getSchemaFromCatalogName(),sqlRequest.getSchemaFromCatalogDsType());
            }

            selectResult = driver.query(sqlRequest.getStatement(), sqlRequest.getMaxRowNum());
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

        return selectResult;
    }


}

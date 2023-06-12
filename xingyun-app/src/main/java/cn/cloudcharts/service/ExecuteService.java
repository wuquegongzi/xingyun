package cn.cloudcharts.service;

import cn.cloudcharts.model.request.QueryTblColumnRequest;
import cn.cloudcharts.model.request.QueryTblRequest;
import cn.cloudcharts.model.request.SqlExecRequest;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: ExecuteService
 * @projectName xingyun
 * @description:
 * @date 2023/5/2320:15
 */
public interface ExecuteService {

    JdbcSelectResult executeCommonSql(SqlExecRequest sqlRequest);

    List<Map<String,Object>> queryAllColumns(QueryTblColumnRequest queryTblRequest);

    List<String> getTableList(QueryTblRequest queryTblRequest);
}

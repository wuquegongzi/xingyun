package cn.cloudcharts.service;

import cn.cloudcharts.model.request.SqlRequest;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;

/**
 * @author wuque
 * @title: ExecuteService
 * @projectName xingyun
 * @description:
 * @date 2023/5/2320:15
 */
public interface ExecuteService {

    JdbcSelectResult executeCommonSql(SqlRequest sqlRequest);

}

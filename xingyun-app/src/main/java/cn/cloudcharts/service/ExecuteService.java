package cn.cloudcharts.service;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.model.dto.SqlDTO;
import cn.cloudcharts.starrocks.model.result.JdbcSelectResult;

/**
 * @author wuque
 * @title: ExecuteService
 * @projectName xingyun
 * @description:
 * @date 2023/5/2320:15
 */
public interface ExecuteService {

    JdbcSelectResult executeCommonSql(SqlDTO sqlDTO);

    boolean cancel(String jobId);
}

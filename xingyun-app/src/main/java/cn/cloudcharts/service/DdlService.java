package cn.cloudcharts.service;

import cn.cloudcharts.metadata.model.dto.CreateTableDTO;

/**
 * @author wuque
 * @title: DdlService
 * @projectName xingyun
 * @description:
 * @date 2023/5/2919:39
 */
public interface DdlService {

    boolean createTbl(CreateTableDTO dto);

    boolean createTbl(String sql);
}

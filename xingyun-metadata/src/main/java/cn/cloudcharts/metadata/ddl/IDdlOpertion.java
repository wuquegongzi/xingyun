package cn.cloudcharts.metadata.ddl;

import cn.cloudcharts.metadata.model.dto.CreateTableDTO;

/**
 * @author wuque
 * @title: IDdlOpertion
 * @projectName xingyun
 * @description: DDL操作接口
 * @date 2023/5/2919:53
 */
public interface IDdlOpertion {

    String getCreateTableSqlFromTemplate(CreateTableDTO tableDTO);
}

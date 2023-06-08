package cn.cloudcharts.metadata.ddl;

import cn.cloudcharts.metadata.model.dto.CreateTableDTO;

/**
 * @author wuque
 * @title: AbstractDdlOpertion
 * @projectName xingyun
 * @description:
 * @date 2023/5/2919:56
 */
public  abstract class AbstractDdlOpertion implements IDdlOpertion{


    @Override
    public String getCreateTableSqlFromTemplate(CreateTableDTO tableDTO) {
        return null;
    }


}

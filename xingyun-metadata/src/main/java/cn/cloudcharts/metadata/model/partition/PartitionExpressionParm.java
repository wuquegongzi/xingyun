package cn.cloudcharts.metadata.model.partition;

import cn.cloudcharts.metadata.enums.PartitionFuncTypeEnums;
import cn.cloudcharts.metadata.enums.PartitionTimeUnitEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: PartitionExpressionParm
 * @projectName xingyun
 * @description: 隐式分区表达式参数
 * @date 2023/5/913:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartitionExpressionParm {

    /**
     * 分区函数
     */
    private PartitionFuncTypeEnums partitionFuncType;

    /**
     * 分区粒度
     */
    private int partitionGranularity;

    /**
     * 分区粒度单位
     */
    private PartitionTimeUnitEnums partitionTimeUnit;

}

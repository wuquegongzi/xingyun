package cn.cloudcharts.olap.model;

import cn.cloudcharts.olap.enums.PartitionModeEnums;
import cn.cloudcharts.olap.enums.PartitionTimeUnitEnums;
import cn.cloudcharts.olap.enums.PartitionFuncTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wuque
 * @title: Partition
 * @projectName xingyun
 * @description: 分区
 * @date 2023/5/615:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Partition {

    /**
     * 分区模式
     */
    private PartitionModeEnums partitionMode;

    /**
     * 分区类型 Round-Robin、Range、List、Hash
     * 目前starrocks 3.0仅支持 Range
     */
    private String partitionType;

    /**
     * 分区函数
     */
    private PartitionFuncTypeEnums partitionFuncType;

    /**
     * 分区粒度
     */
    private PartitionTimeUnitEnums partitionTimeUnit;

    /**
     * 分区列
     */
    private String partitionColumnName;

    /**
     * 分区表达式
     */
    private List<PartitionExpression> partitionExpressions;






}

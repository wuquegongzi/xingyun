package cn.cloudcharts.olap.model.partition;

import cn.cloudcharts.olap.enums.PartitionDescEnums;
import cn.cloudcharts.olap.enums.PartitionModeEnums;
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
     * 分区模式，默认显性
     */
    private PartitionModeEnums partitionMode;
    /**
     * 显性分区方式
     */
    private PartitionDescEnums partitionDesc;

    /**
     * 分区类型 Round-Robin、Range、List、Hash
     * 目前starrocks 3.0仅支持 Range
     */
    private String partitionType;

    /**
     * 分区列,多个以逗号分割
     */
    private String partitionCols;

    /**
     * 显性分区-lessthan 参数
     */
    private List<PartitionLessThanParm> partitionLessThanParms;
    /**
     * 显性分区-fixed range 参数
     */
    private List<PartitionFixedRangeParm> partitionFixedRangeParms;
    /**
     * 显性分区-批量创建分区
     */
    private PartitionBatchParm partitionBatchParm;
    /**
     * 隐式分区-动态函数表达式方式
     */
    private PartitionExpressionParm partitionExpressionParm;


}

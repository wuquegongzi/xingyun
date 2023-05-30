package cn.cloudcharts.metadata.enums;

/**
 * @author wuque
 * @title: PartitionModeEnums
 * @projectName xingyun
 * @description: 分区模式
 * @date 2023/5/616:16
 */
public enum PartitionModeEnums {

    /**
     * 显性
     * 参见：cn.cloudcharts.olap.enums.PartitionDescEnums
     */
    dominant,
    /**
     * 隐性
     * 使用表达式
     */
    recessive

}

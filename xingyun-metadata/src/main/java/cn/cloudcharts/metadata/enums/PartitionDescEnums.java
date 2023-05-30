package cn.cloudcharts.metadata.enums;

/**
 * @author wuque
 * @title: PartitionModeEnums
 * @projectName xingyun
 * @description: 显性分区方式
 * @date 2023/5/616:16
 */
public enum PartitionDescEnums {

    /**
     * 使用指定的 key 列和指定的数值范围进行分区
     */
    LESS_THAN,
    /**
     * Fixed Range 比 LESS THAN 相对灵活些，左右区间完全由用户自己确定
     */
    Fixed_Range,
    /**
     * 批量创建分区
     * 用户可以通过给出一个 START 值、一个 END 值以及一个定义分区增量值的 EVERY 子句批量产生分区
     */
    BATCH

}

package cn.cloudcharts.metadata.enums;

/**
 * @author wuque
 * @title: PartitionFuncTypeEnums
 * @projectName xingyun
 * @description: 分区函数
 * @date 2023/5/615:38
 */
public enum PartitionFuncTypeEnums {
    /**
     * 根据指定的精度级别，将一个日期时间截断。
     */
    date_trunc,
    /**
     * 根据指定的时间粒度周期，将给定的时间转化为其所在的时间粒度周期的起始或结束时刻。
     */
    time_slice
}

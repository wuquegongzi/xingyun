package cn.cloudcharts.olap.enums;

/**
 * @author wuque
 * @title: PartitionTypeEnums
 * @projectName xingyun
 * @description:
 * @date 2023/5/710:09
 */
public enum PartitionTypeEnums {
    /**
     * 暂不支持
     * 以轮询的方式把数据逐个放置在相邻节点上
     */
            RoundRobin,
    /**
     * 按区间进行数据分布。如上图所示，区间 [1-3]、[4-6] 分别对应不同的范围 (Range)
     * StarRocks Range+Hash 数据分布方式：一张表拆分成多个分区，每个分区按照分桶键和分桶数量进一步进行数据划分
     */
            Range,
    /**
     * 暂不支持
     * 直接基于离散的各个取值做数据分布，性别、省份等数据就满足这种离散的特性。每个离散值会映射到一个节点上，多个不同的取值可能也会映射到相同节点上
     */
            List,
    /**
     * 通过哈希函数把数据映射到不同节点上
     * StarRocks  Hash 数据分布方式：一张表为一个分区，分区按照分桶键和分桶数量进一步进行数据划分
     */
            Hash

}

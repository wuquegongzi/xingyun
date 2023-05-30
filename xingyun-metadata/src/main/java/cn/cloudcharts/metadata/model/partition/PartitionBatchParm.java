package cn.cloudcharts.metadata.model.partition;

import cn.cloudcharts.metadata.enums.PartitionIntervalNTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: PartitionRangeEnums
 * @projectName xingyun
 * @description: 动态分区设置
 * @date 2023/5/616:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartitionBatchParm {


    private String start;

    private String end;

    /**
     * 分区粒度
     */
    private Integer interval;

    /**
     * 时间粒度周期
     */
    private PartitionIntervalNTypeEnums intervalNType;


}

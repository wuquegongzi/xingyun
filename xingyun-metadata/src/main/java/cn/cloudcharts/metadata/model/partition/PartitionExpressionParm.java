package cn.cloudcharts.metadata.model.partition;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "隐式分区表达式参数")
public class PartitionExpressionParm {

    /**
     * @cn.cloudcharts.metadata.enums.PartitionFuncTypeEnums
     * 分区函数
     */
    @Schema(description = "分区函数，取值：[date_trunc：根据指定的精度级别，将一个日期时间截断； time_slice：根据指定的时间粒度周期，将给定的时间转化为其所在的时间粒度周期的起始或结束时刻]")
    private String partitionFuncType;

    /**
     * 分区粒度
     */
    @Schema(description = "分区粒度")
    private int partitionGranularity;

    /**
     * @cn.cloudcharts.metadata.enums.PartitionTimeUnitEnums
     * 分区粒度单位
     */
    @Schema(description = "分区粒度单位,取值：[ year,\n" +
            "    month,\n" +
            "    day,\n" +
            "    hour]")
    private String partitionTimeUnit;

}

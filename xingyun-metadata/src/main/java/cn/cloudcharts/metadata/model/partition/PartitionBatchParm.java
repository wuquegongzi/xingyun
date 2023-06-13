package cn.cloudcharts.metadata.model.partition;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "动态分区设置")
public class PartitionBatchParm {


    private String start;

    private String end;

    /**
     * 分区粒度
     */
    @Schema(description = "分区粒度")
    private Integer interval;

    /**
     * @cn.cloudcharts.metadata.enums.PartitionIntervalNTypeEnums
     * 时间粒度周期
     */
    @Schema(description = "时间粒度周期,取值[ YEAR,\n" +
            "    QUARTER,\n" +
            "    MONTH,\n" +
            "    WEEK,\n" +
            "    DAY,\n" +
            "    HOUR,\n" +
            "    MINUTE,\n" +
            "    SECOND]")
    private String intervalNType;


}

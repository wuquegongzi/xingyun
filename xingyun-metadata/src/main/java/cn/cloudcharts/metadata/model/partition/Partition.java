package cn.cloudcharts.metadata.model.partition;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "分区")
public class Partition {


    /**
     * @cn.cloudcharts.metadata.enums.PartitionModeEnums
     * 分区模式，默认显性
     */
    @Schema(description = "分区模式，取值：[dominant、recessive]。默认dominant[显性 参见：@cn.cloudcharts.olap.enums.PartitionDescEnums]，recessive[隐性 使用表达式]")
    private String partitionMode;
    /**
     * @cn.cloudcharts.metadata.enums.PartitionDescEnums
     * 显性分区方式
     */
    @Schema(description = "显性分区方式,取值[LESS_THAN、Fixed_Range、BATCH]")
    private String partitionDesc;

    /**
     * 分区类型 Round-Robin、Range、List、Hash
     * 目前starrocks 3.0仅支持 Range
     */
    @Schema(description = "分区类型 Round-Robin、Range、List、Hash 目前starrocks 3.0仅支持 Range")
    private String partitionType;

    /**
     * 分区列,多个以逗号分割
     */
    @Schema(description = "分区列,多个以逗号分割")
    private String partitionCols;

    /**
     * 显性分区-lessthan 参数
     */
    @Schema(description = "显性分区-lessthan 参数")
    private List<PartitionLessThanParm> partitionLessThanParms;
    /**
     * 显性分区-fixed range 参数
     */
    @Schema(description = "显性分区-fixed range 参数")
    private List<PartitionFixedRangeParm> partitionFixedRangeParms;
    /**
     * 显性分区-批量创建分区
     */
    @Schema(description = "显性分区-批量创建分区")
    private PartitionBatchParm partitionBatchParm;
    /**
     * 隐式分区-动态函数表达式方式
     */
    @Schema(description = "隐式分区-动态函数表达式方式")
    private PartitionExpressionParm partitionExpressionParm;


}

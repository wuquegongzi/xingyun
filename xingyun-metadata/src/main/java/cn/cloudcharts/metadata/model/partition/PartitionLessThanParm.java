package cn.cloudcharts.metadata.model.partition;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: PartitionLessThanParm
 * @projectName xingyun
 * @description: 显性分区方式Lessthan参数对象
 * @date 2023/5/818:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "显性分区方式Lessthan参数对象")
public class PartitionLessThanParm {


    private String partitionName;
    /**
     * MAXVALUE|("value1", "value2", ...)
     */
    @Schema(description = "MAXVALUE|(\"value1\", \"value2\", ...)")
    private String vals;

}

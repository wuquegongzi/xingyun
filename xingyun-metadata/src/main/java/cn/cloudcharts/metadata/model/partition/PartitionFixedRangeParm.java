package cn.cloudcharts.metadata.model.partition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: PartitionLessThanParm
 * @projectName xingyun
 * @description: 显性分区方式FixedRange参数对象
 * @date 2023/5/818:16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartitionFixedRangeParm {

    /**
     *
     */
    private String partitionName;

    private String lowerVals;

    private String upperVals;

}

package cn.cloudcharts.olap.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: CreateTableDTO
 * @projectName xingyun
 * @description: 建表对象
 * @date 2023/5/614:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTableDTO {

    @ApiModelProperty(value = "数据库名称，一般根据默认数据源指定")
    private String dbName;

    @ApiModelProperty(value = "表数据模型  0-明细 1-聚合 2-更新 3-主键")
    private String tblDataType;
    @ApiModelProperty(value = "表类型 默认0-olap  1-外表")
    private String tblType;
    @ApiModelProperty(value = "表引擎 默认olap[表示创建的是 StarRocks 内部表]  " +
            "外表根据源表类型[可选值：mysql，elasticsearch，hive，jdbc(2.3 及以后)，iceberg，hudi（2.2 及以后）]")
    private String tblEngine;
    @ApiModelProperty(value = "表名")
    private String tblName;
    @ApiModelProperty(value = "表注释")
    private String comment;
    @ApiModelProperty(value = "表字段信息")
    private List<Column> cols;
    @ApiModelProperty(value =
            "明细模型排序键-duplicateKeys;" +
            "聚合模型聚合维度列-aggregateKeys;" +
            "更新模型主键列-uniqueKeys;" +
            "主键模型专用主键列-primaryKeys;" +
            "多个字段逗号分割")
    private String keyDesc;
    @ApiModelProperty(value = "分区配置")
    private Partition partition;
    @ApiModelProperty(value = "分桶字段，多个字段逗号分割")
    private String distributedCols;
    @ApiModelProperty(value = "分桶数量")
    private String buckets;
    @ApiModelProperty(value = "排序键")
    private List<String> orderKeys;

    /**
     * https://docs.starrocks.io/zh-cn/3.0/table_design/dynamic_partitioning
     */
    @ApiModelProperty(value = "额外配置项,譬如 副本个数replication_num；动态分区相关属性 ")
    private Map properties;



}

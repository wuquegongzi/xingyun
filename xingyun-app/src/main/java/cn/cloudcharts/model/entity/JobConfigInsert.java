package cn.cloudcharts.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * insert方式导入数据任务配置表
 * </p>
 *
 * @author wuque
 * @since 2023-06-15
 */
@Getter
@Setter
@TableName("xingyun_job_config_insert")
@ApiModel(value = "JobConfigInsert对象", description = "insert方式导入数据任务配置表")
public class JobConfigInsert implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("任务基础表ID")
    private Long jobId;

    private String sourceCatalog;

    private String sourceDb;

    private String sourceTbl;

    @ApiModelProperty("指定写入列，按照顺序，多个以,分割")
    private String sourceFields;

    @ApiModelProperty("设置信息")
    private String settings;

    @ApiModelProperty("配置的任务名")
    private String configName;

    @ApiModelProperty("0-insert 1-overwrite")
    private Integer overwriteFlag;

    @ApiModelProperty("设置的label")
    private String labelName;

    private String sinkCatalog;

    private String sinkDb;

    private String sinkTbl;

    @ApiModelProperty("附加配置条件")
    private String extra;

    @ApiModelProperty("条件")
    private String condition;

    @ApiModelProperty("0-可视化配置  1-SQL")
    private Integer conditionType;


}

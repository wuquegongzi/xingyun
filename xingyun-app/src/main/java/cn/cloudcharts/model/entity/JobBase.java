package cn.cloudcharts.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 作业基础信息
 * </p>
 *
 * @author wuque
 * @since 2023-06-15
 */
@Getter
@Setter
@TableName("xingyun_job_base")
@ApiModel(value = "JobBase对象", description = "作业基础信息")
public class JobBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "job_id", type = IdType.AUTO)
    private Long jobId;

    @ApiModelProperty("引擎返回的ID")
    private String bizId;

    @ApiModelProperty("作业名称")
    private String jobName;

    @ApiModelProperty("作业类型  0-数据导入  1-数据导出")
    private Integer jobType;

    @ApiModelProperty("作业执行引擎")
    private String jobEngine;

    @ApiModelProperty("业务类型  eg: delta2starrocks[catalog.db.table->catalog.db.table]")
    private String businessType;

    @ApiModelProperty("配置")
    private String configJson;

    @ApiModelProperty("提交时间")
    private LocalDateTime submitTime;

    @ApiModelProperty("异常信息")
    private String exceptionInfo;

    @ApiModelProperty("是否是实时任务")
    private Integer realtimeFlag;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建者")
    private String creator;

    @ApiModelProperty("修改者")
    private String modifier;

    @ApiModelProperty("资源组")
    private Integer resGroupId;

    @ApiModelProperty("资源配置")
    private String resConfig;

    @ApiModelProperty("执行结果")
    private String execResult;

    @ApiModelProperty("任务的明细")
    private String jobDetails;

    @ApiModelProperty("优先级")
    private Integer priority;

    @ApiModelProperty("执行开始时间")
    private LocalDateTime execStartTime;

    @ApiModelProperty("执行结束时间")
    private LocalDateTime execEndTime;

    @ApiModelProperty("删除标识")
    private Integer delFlag;

    @ApiModelProperty("0-脚本模式  1-引导模式")
    private Integer guideType;


}

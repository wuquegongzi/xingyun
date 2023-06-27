package cn.cloudcharts.metadata.task;

import lombok.Data;

/**
 * @author wuque
 * @title: TaskResult
 * @projectName xingyun
 * @description:
 * @date 2023/6/2717:46
 */
@Data
public class TaskRunsResult {

    private String queryId;
    private String taskName;
    private String createTime;
    private String finishTime;
    private String state;
    private String database;
    private String definition;
    private String expireTime;
    private String errorCode;
    private String errorMessage;
    private String progress;
    private String extraMessage;
}

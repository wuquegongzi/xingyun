package cn.cloudcharts.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: TaskInfoRequest
 * @projectName xingyun
 * @description: 任务请求体
 * @date 2023/6/1417:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "任务执行请求体")
public class JobInfoRequest {

    private String id;



}

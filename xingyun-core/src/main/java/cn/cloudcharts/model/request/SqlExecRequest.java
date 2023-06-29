package cn.cloudcharts.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: SqlDTO
 * @projectName xingyun
 * @description:
 * @date 2023/5/2320:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "SQL执行参数请求体")
public class SqlExecRequest {

    @Schema(description = "SQL脚本文本")
    private String statement;
    @Schema(description = "数据源ID")
    private Integer databaseId = 1;
    @Schema(description = "请求最大个数，默认0")
    private Integer maxRowNum;

}

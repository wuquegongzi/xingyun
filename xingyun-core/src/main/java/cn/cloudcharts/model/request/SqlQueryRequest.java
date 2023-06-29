package cn.cloudcharts.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: SqlQueryRequest
 * @projectName xingyun
 * @description:
 * @date 2023/6/1715:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "SQL查询参数请求体")
public class SqlQueryRequest extends SqlExecRequest{

    @Schema(description = "是否校验olap已存在")
    private boolean checkTblExist;
    @Schema(description = "如果表不存在，需要创建表，表结构的来源Catalog")
    private String schemaFromCatalogName;
    @Schema(description = "表来源catalog的类型，eg: delta、hive")
    private String schemaFromCatalogDsType;

}

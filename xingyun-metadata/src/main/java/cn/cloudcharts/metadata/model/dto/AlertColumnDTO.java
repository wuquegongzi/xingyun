package cn.cloudcharts.metadata.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wuque
 * @title: AlertColumnDTO
 * @projectName xingyun
 * @description: 修改字段
 * @date 2023/6/1716:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description ="修改字段信息-对象")
public class AlertColumnDTO {

    @Schema(description = "数据源ID，一般根据默认数据源指定")
    private Integer databaseId;

    @Schema(description = "数据库名称，如果数据源指定了数据库，可不传")
    private String schemaName;

    @Schema(description = "表名")
    private String tblName;

    @Schema(description =  "表字段信息")
    private List<ColumnDTO> cols;

}

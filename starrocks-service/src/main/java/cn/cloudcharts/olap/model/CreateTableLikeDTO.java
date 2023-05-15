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
public class CreateTableLikeDTO {

    @ApiModelProperty(value = "表类型 默认0-olap  1-外表")
    private String tblType;
    @ApiModelProperty(value = "源数据库名称")
    private String srcDbName;
    @ApiModelProperty(value = "源表名")
    private String srcTblName;
    @ApiModelProperty(value = "要创建的数据库名称")
    private String sinkDbName;
    @ApiModelProperty(value = "要创建的表名")
    private String sinkTblName;


}

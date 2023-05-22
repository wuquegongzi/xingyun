package cn.cloudcharts.starrocks.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: CreateTableDTO
 * @projectName xingyun
 * @description: 设置数据库属性
 * @date 2023/5/614:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertDbDTO {

    @ApiModelProperty(value = "数据库名称")
    private String dbName;

    @ApiModelProperty(value = "0- SET DATA[设置数据库数据量配额]；" +
            "1-RENAME[重命名]；" +
            "2-SET REPLICA QUOTA[设置数据库的副本数量配额]; ")
    private String operType;
    @ApiModelProperty(value = "要创建的数据库名称")
    private String sinkDbName;
    @ApiModelProperty(value = "要创建的表名")
    private String sinkTblName;


}

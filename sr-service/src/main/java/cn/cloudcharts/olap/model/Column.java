package cn.cloudcharts.olap.model;

import cn.cloudcharts.olap.enums.ColumnTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: Column
 * @projectName xingyun
 * @description:
 * @date 2023/5/614:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Column {

    private String name;

    /**
     * 字段类型
     */
    private ColumnTypeEnums type;

    /**
     * 字段长度，默认100
     */
    private Integer len;

    /**
     * 字段默认值 ''
     */
    private String defaultVal;

    /**
     * 字段注释
     */
    private String comment;

    /**
     * 聚合类型 key | agg_type
     */
    private String keyType;

    /**
     * NOT NULL
     */
    private String nullType;
}

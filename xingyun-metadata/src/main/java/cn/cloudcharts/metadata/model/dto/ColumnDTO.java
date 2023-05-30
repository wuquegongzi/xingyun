package cn.cloudcharts.metadata.model.dto;

import cn.cloudcharts.metadata.enums.ColumnTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
public class ColumnDTO implements Serializable {

    private String colName;

    /**
     * 字段类型
     */
    private ColumnTypeEnums colType;

    /**
     * 取值范围
     * DECIMAL类型输入[precision,scale],以逗号分割
     */
    private String len;

    /**
     * 字段默认值 ''
     */
    private String defaultVal;

    /**
     * 字段注释
     */
    private String comment;

    /**
     * 聚合类型，如果不指定，则该列为 key 列。否则，该列为 value 列。
     * key | agg_type
     */
    private String keyType;

    /**
     * NOT NULL
     * 列数据是否允许为 NULL
     */
    private String notNull;
}

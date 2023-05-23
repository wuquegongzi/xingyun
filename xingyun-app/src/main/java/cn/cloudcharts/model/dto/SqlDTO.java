package cn.cloudcharts.model.dto;

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
public class SqlDTO {

    private String statement;
    private Integer databaseId;
    private Integer maxRowNum;

}

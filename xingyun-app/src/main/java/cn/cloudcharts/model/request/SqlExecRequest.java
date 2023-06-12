package cn.cloudcharts.model.request;

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
public class SqlExecRequest {

    private String statement;
    private Integer databaseId;
    private Integer maxRowNum;

}

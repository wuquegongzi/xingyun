package cn.cloudcharts.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: QueryTblRequest
 * @projectName xingyun
 * @description:
 * @date 2023/6/1216:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryTblRequest {

    private Integer databaseId;
    private String catalogName;
    private String dbName;
}

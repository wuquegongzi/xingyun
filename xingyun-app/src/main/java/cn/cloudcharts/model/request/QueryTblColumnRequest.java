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
public class QueryTblColumnRequest extends QueryTblRequest{

    private String tableName;

}

package cn.cloudcharts.model.request.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName FormulaFilterGroup
 * @Description: 数据源SQL-公式 过滤条件组
 * @Author wuque
 * @Date 2022/1/24
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionGroup {

    private String groupId;
    private List<ConditionNode> nodes;
    private String nextRelation;

}

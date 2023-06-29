package cn.cloudcharts.model.request.condition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ConditionTest
 * @Description: 条件测试器
 * @Author wuque
 * @Date 2023/6/15
 * @Version V1.0
 **/
public class ConditionTest {

    public static void main(String[] args) throws JsonProcessingException {

        String groupId = "10001";
        ConditionGroup conditionGroup = new ConditionGroup();
        conditionGroup.setGroupId(groupId);
        conditionGroup.setNextRelation("and");

        List<ConditionNode> conditionNodes = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            ConditionNode conditionNode = new ConditionNode();
            conditionNode.setOrder(i);
            conditionNode.setFieldLabel("name");
            conditionNode.setFieldName("名称");
            conditionNode.setGroupId(groupId);
            conditionNode.setComparator("==");
            conditionNode.setComparatorRep("等于");
            conditionNode.setComparType("datetime");
            conditionNode.setComparValue("${yyyy-mm-dd,1d}");
            conditionNodes.add(conditionNode);
            conditionNode.setNextRelation("or");
        }
        conditionGroup.setNodes(conditionNodes);

        System.out.println(new ObjectMapper().writeValueAsString(conditionGroup));
    }
}

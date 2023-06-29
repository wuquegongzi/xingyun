package cn.cloudcharts.model.request.condition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ConditionNode
 * @Description: 条件：SQL/公式
 * @Author wuque
 * @Date 2023/6/15
 * @Version V1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConditionNode {

    /**
     * 所属组
     */
    private String groupId;

    /**
     * 排序 从小到大
     */
    private int order;

    /**
     * 字段属性名
     */
    private String fieldLabel;

    /**
     * 字段类型 0-普通字段 1-分区字段
     */
    private String partType;

    /**
     * 字段属性 中文名
     */
    private String fieldName;

    /**
     * 比较符号 >,<,>=,<=,!=,==,like,left_like,right_like
     */
    private String comparator;

    /**
     * 比较符号 描述语言 大于，小于，大于且等于，小于且等于，不等于，等于，模糊匹配，左侧开头模糊匹配，右侧结尾模糊匹配
     */
    private String comparatorRep;

    /**
     * 比较类型 constant-常量
     * [string: 字符串 yyyy
     * number: 数字
     * datetime:时间
     * field: 字段]
     *
     * variable-变量
     */
    private String comparType;

    /**
     * 比较值
     */
    private String  comparValue;

    /**
     * 与下一个节点的关系 or 、 and
     */
    private String nextRelation;

    /**
     * 变量的偏移类型
     */
    private String fieldValueOffsetType;

}

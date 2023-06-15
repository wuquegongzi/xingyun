package cn.cloudcharts.common.enums;

/**
 * @author wuque
 * @title: OffsetDateUnitTypeEnums
 * @projectName xingyun
 * @description: 时间表达式 偏移量
 *  https://dolphinscheduler.apache.org/zh-cn/docs/latest/user_doc/guide/parameter/built-in.html
 * @date 2023/6/15
 */
public enum OffsetDateUnitTypeEnums {

    y("年","$[add_months(yyyyMMdd,%s)]"),
    M("月","$[add_months(yyyyMMdd,%s)]"),
    d("日","$[yyyyMMdd%s]"),
    w("周","$[yyyyMMdd%s]"),
    H("小时","$[HHmmss%s]"),
    m("分钟","$[HHmmss%s]"),
    s("秒","$[HHmmss%s]");

    private String desc;
    private String expression;

    OffsetDateUnitTypeEnums(String desc, String expression) {
        this.desc = desc;
        this.expression = expression;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}

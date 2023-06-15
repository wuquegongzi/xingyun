package cn.cloudcharts.common.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ComparTypeEnums
 * @Description: 比较类型枚举
 * @Author wuque
 * @Date 2023/6/15
 * @Version V1.0
 **/
public enum ComparTypeEnums {

    STRING("string","字符串"),
    DATETIME("datetime","时间戳"),
    DATE("date","日期"),
    NUMBER("number","数字"),
    EXPRESSION("expression","动态表达式"),
    VARIABLE("variable","变量"),
    OTHER("other","其他"),

    ;

    private String type;
    private String typeRep;

    ComparTypeEnums(String type, String typeRep) {
        this.type = type;
        this.typeRep = typeRep;
    }

    ComparTypeEnums() {
    }

    public static List toList() {
        List list = new ArrayList();
        for (ComparTypeEnums comparatorEnums : ComparTypeEnums.values()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", comparatorEnums.getType());
            map.put("typeRep", comparatorEnums.getTypeRep());
            list.add(map);
        }
        return list;
    }

    public static Map<String,String> toMap() {

        Map<String,String> map = new HashMap();
        for (ComparTypeEnums comparatorEnums : ComparTypeEnums.values()) {
            map.put(comparatorEnums.getType(), comparatorEnums.getTypeRep());
        }
        return map;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeRep() {
        return typeRep;
    }

    public void setTypeRep(String typeRep) {
        this.typeRep = typeRep;
    }



}

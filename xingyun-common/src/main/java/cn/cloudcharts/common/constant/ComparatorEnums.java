package cn.cloudcharts.common.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ComparatorEnums
 * @Description: 比较符枚举
 * @Author wuque
 * @Date 2023/6/15
 * @Version V1.0
 **/
public enum ComparatorEnums {

    MT(">","大于"),
    LT("<","小于"),
    GE(">=","大于且等于"),
    LE("<=","小于且等于"),
    NE("!=","不等于"),
    EQ("=","等于"),
    LIKE("like","模糊匹配"),
    LEFT_LIKE("left_like","左侧开头模糊匹配"),
    RIGHT_LIKE("right_like","右侧结尾模糊匹配"),
    ;

    private String type;
    private String typeRep;

    ComparatorEnums(String type, String typeRep) {
        this.type = type;
        this.typeRep = typeRep;
    }

    ComparatorEnums() {
    }

    public static List toList() {
        List list = new ArrayList();
        for (ComparatorEnums comparatorEnums : ComparatorEnums.values()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", comparatorEnums.getType());
            map.put("typeRep", comparatorEnums.getTypeRep());
            list.add(map);
        }
        return list;
    }

    public static Map<String,String> toMap() {

        Map<String,String> map = new HashMap();
        for (ComparatorEnums comparatorEnums : ComparatorEnums.values()) {
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

package cn.cloudcharts.metadata.enums;

import cn.hutool.core.util.EnumUtil;

/**
 * @author wuque
 * @title: TblDataModelEnums
 * @projectName xingyun
 * @description: 表数据模型
 * @date 2023/6/1413:47
 */
public enum TblDataModelEnums {

    DUPLICATE("0","明细"),
    AGGREGATE("1","聚合"),
    UNIQUE("2","更新"),
    PRIMARY("3","主键");


    private String code;
    private String name;

    TblDataModelEnums(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static boolean contains(String code){
        for (TblDataModelEnums tdme : TblDataModelEnums.values()) {
            if (tdme.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package cn.cloudcharts.common.enums;

/**
 * 删除标识
 *
 * @author wuque
 */
public enum DeleteEnum
{
    OK("0", "正常"),  DELETED("2", "删除");

    private final String code;
    private final String info;

    DeleteEnum(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}

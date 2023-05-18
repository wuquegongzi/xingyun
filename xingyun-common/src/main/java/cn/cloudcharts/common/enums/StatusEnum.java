package cn.cloudcharts.common.enums;

/**
 * 启用状态（0正常 1停用）
 *
 * @author wuque
 */
public enum StatusEnum
{
    OK("0", "正常"),  STOP("1", "停用");

    private final String code;
    private final String info;

    StatusEnum(String code, String info)
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

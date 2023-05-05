package cn.cloudchats.common.enums;

/**
 * @Description: SmsTemplateEnum
 * @author: hug
 * @since: 2022/9/6 10:29
 */
public enum SmsTemplateEnum {
    TEMPLATE_NAME_REPEAT("模版名称重复"),
    TEMPLATE_NULL("模版不存在"),
    NAME_CODE_REPEAT("名称或代码已存在"),
    ACCOUNT_NAME_REPEAT("账号名称已存在"),
    CHANNEL_NULL("渠道不存在"),
    ACCOUNT_NULL("账号不存在"),
    ATTRIBUTE_NULL("用户属性不存在"),
    ATTRIBUTE_NAME_REPEAT("用户属性已存在"),
    ID_MAPPING_NULL("用户属性不存在"),
    ID_MAPPING_NAME_REPEAT("用户属性已存在"),
    STATUS("启用状态不正确"),
    STATUS_FAIL("更改状态失败"),
    ;


    private final String info;

    SmsTemplateEnum(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return info;
    }
}

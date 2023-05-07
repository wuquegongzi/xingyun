package cn.cloudcharts.common.enums;

/**
 * @Description: 短信模版审核状态（0:待审核、1:审核通过、2:审核拒绝）SmsTemplateStatusEnum
 * @author: hug
 * @since: 2022/9/6 16:38
 */
public enum SmsTemplateStatusEnum {
    TO_BE_APPROVED("0", "待审批"),
    PASSED("1", "已通过"),
    REFUSE("2", "未通过");
    private final String code;
    private final String info;

    SmsTemplateStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}

package cn.cloudcharts.common.constant;

/**
 * @ClassName BizConstants
 * @Description: 业务常量
 * @Author wuque
 * @Date 2023/6/15
 * @Version V1.0
 **/
public class BizConstants {


    /**
     * 过滤条件 [0-可视化配置 1-SQL脚本]
     */
    public static final String FILTER_FORMULA = "0";
    /**
     * 过滤条件 [0-可视化配置 1-SQL脚本]
     */
    public static final String FILTER_SQL = "1";

    /**
     * 清理规则 [0-全量覆盖 1-增量]
     */
    public static final  String OVERWRITE_TYPE_FULL = "0";
    /**
     * 清理规则 [0-全量覆盖 1-增量]
     */
    public static final  String OVERWRITE_TYPE_INCR = "1";


    /**
     * 普通字段
     */
    public static final String FIELD_TYPE_DEFALT = "0";
    /**
     * 分区字段
     */
    public static final String FIELD_TYPE_PART = "1";

    /**
     * 动态表单的配置的key和value
     */
    public static final String DYNAMIC_KEY = "key";
    public static final String DYNAMIC_value = "value";
}



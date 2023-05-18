package cn.cloudcharts.core.ds;

/**
 * @author wuque
 * @title: DBIdentifier
 * @projectName xingyun
 * @description: 数据库标识管理类。用于区分数据源连接的不同数据库
 * @date 2023/5/1810:16
 */
public class DBIdentifier {

    /**
     * 用不同的工程编码来区分数据库
     */
    private static ThreadLocal<String> dbCode = ThreadLocal.withInitial(() -> "defaultDataSource");

    public static String getDbCode() {
        return dbCode.get();
    }

    public static void setDbCode(String code) {
        dbCode.set(code);
    }
}

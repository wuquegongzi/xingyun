package cn.cloudcharts.common.enums;

import cn.cloudcharts.common.utils.EnumUtils;
import cn.hutool.core.util.StrUtil;

/**
 * 数据源类型
 * @author wuque
 * @date 2023/6/15
 */
public enum DataSourceTypeEnum {

    STARROCKS("-1", "starrocks","Starrocks"),
    MYSQL("0", "mysql","Mysql"),
    HIVE("1", "hive","Hive"),
    SPARK_THRIFT("2", "spark","Spark"),
    ORACLE("3", "oracle","Oracle"),
    POSTGRESQL("4", "postgresql","PgSQL"),
    CLICKHOUSE("5", "clickhouse","clickhouse"),
    SQLSERVER("6", "sqlserver","sqlserver"),
    DB2("7", "db2","db2"),
    PRESTO("8", "presto","presto"),
    KAFKA("9", "kafka","kafka"),
    HANA("10", "hana","hana"),
    ELASTICSEARCH("11", "elasticsearch","elasticsearch"),
    TIDB("12", "tidb","tidb"),
    IMPALA("13", "impala","impala"),
    ES6("14", "elasticsearch6","elasticsearch6"),
    ES7("15", "elasticsearch7","elasticsearch7"),
    FTP("16", "ftp","ftp"),
    OSS("17", "oss","oss"),
    SFTP("18", "sftp","sftp"),
    LOCAL_FILE("19", "本地文件","本地文件"),
    HUDI("20", "hudi","Hudi"),
    HIVE_META_DATA("21", "metadata","hiveMetaData"),
    DEFAULT("999","defalut","");

    private final String code;
    private final String desc;
    /** 引擎名，为空意味着计算引擎暂不支持 */
    private final String egName;

    public static final String JDBC_STARROCKS = "jdbc:mysql://";
    public static final String JDBC_MYSQL = "jdbc:mysql://";
    public static final String JDBC_POSTGRESQL = "jdbc:postgresql://";
    public static final String JDBC_HIVE_2 = "jdbc:hive2://";
    public static final String JDBC_CLICKHOUSE = "jdbc:clickhouse://";
    public static final String JDBC_ORACLE_SID = "jdbc:oracle:thin:@";
    public static final String JDBC_ORACLE_SERVICE_NAME = "jdbc:oracle:thin:@//";
    public static final String JDBC_SQLSERVER = "jdbc:sqlserver://";
    public static final String JDBC_DB2 = "jdbc:db2://";
    public static final String JDBC_PRESTO = "jdbc:presto://";
    public static final String JDBC_HANA = "jdbc:sap://";
    public static final String JDBC_IMPALA = "jdbc:impala://";
    public static final String JDBC_ELASTICSEARCH7 = "http://";
    public static final String JDBC_ELASTICSEARCH6 = "http://";
    public static final String JDBC_TIDB = "jdbc:mysql://";

    public static final String ORG_POSTGRESQL_DRIVER = "org.postgresql.Driver";
    public static final String COM_MYSQL_CJ_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String COM_MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static final String ORG_APACHE_HIVE_JDBC_HIVE_DRIVER = "org.apache.hive.jdbc.HiveDriver";
    public static final String COM_CLICKHOUSE_JDBC_DRIVER = "ru.yandex.clickhouse.ClickHouseDriver";
    public static final String COM_ORACLE_JDBC_DRIVER = "oracle.jdbc.OracleDriver";
    public static final String COM_SQLSERVER_JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String COM_DB2_JDBC_DRIVER = "com.ibm.db2.jcc.DB2Driver";
    public static final String COM_PRESTO_JDBC_DRIVER = "com.facebook.presto.jdbc.PrestoDriver";
    public static final String COM_SAP_DB_JBDC_DRIVER = "com.sap.db.jdbc.Driver";
    public static final String COM_CLOUDERA_IMPALA_JDBC41_DRIVER = "com.cloudera.impala.jdbc41.Driver";

    private DataSourceTypeEnum(String code, String desc, String egName) {
        this.code = code;
        this.desc = desc;
        this.egName = egName;
    }

    public String code() {
        return this.code;
    }

    public String desc() {
        return this.desc;
    }


    public static DataSourceTypeEnum valueOf(int code) {
        return EnumUtils.getEnumByCode(DataSourceTypeEnum.class, code);
    }

    public static DataSourceTypeEnum getByDesc(String desc) {
        if (desc == null) {
            return null;
        }
        for (DataSourceTypeEnum e : values()) {
            if (e.desc.equals(desc)) {
                return e;
            }
        }
        return null;
    }

    public static DataSourceTypeEnum valueOfV2(Object code) {
        if(code instanceof Integer){
            return valueOf(((Integer) code).intValue());
        } else if(code instanceof String){
            return EnumUtils.getEnumByCode(DataSourceTypeEnum.class, StrUtil.toString(code));
        }

        return DEFAULT;
    }

}

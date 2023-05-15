package cn.cloudcharts.olap.config.connect;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Properties;

/**
 * @author wuque
 * @title: SrConnect
 * @projectName xingyun
 * @description: Starrocks JDBC连接器
 * @date 2023/5/517:46
 */
@Deprecated
@Slf4j
//@Component
public class SrConnect {

    @Value("${olap.type}")
    private String olapType;
    @Value("${olap.driverClassName}")
    private String driverClassName;
    @Value("${olap.user}")
    private String user;
    @Value("${olap.password}")
    private String password;
    @Value("${olap.databaseName}")
    private String databaseName;
    @Value("${olap.portNumber}")
    private String portNumber;
    @Value("${olap.serverName}")
    private String serverName;
    @Value("${olap.dataSource.cachePrepStmts}")
    private String dataSourceCachePrepStmts;
    @Value("${olap.dataSource.prepStmtCacheSize}")
    private String dataSourcePrepStmtCacheSize;
    @Value("${olap.dataSource.prepStmtCacheSqlLimit}")
    private String dataSourcePrepStmtCacheSqlLimit;
    @Value("${olap.dataSource.useServerPrepStmts}")
    private String dataSourceUseServerPrepStmts;
    @Value("${olap.dataSource.useLocalSessionState}")
    private String dataSourceUseLocalSessionState;
    @Value("${olap.dataSource.rewriteBatchedStatements}")
    private String dataSourceRewriteBatchedStatements;
    @Value("${olap.dataSource.cacheResultSetMetadata}")
    private String dataSourceCacheResultSetMetadata;
    @Value("${olap.dataSource.cacheServerConfiguration}")
    private String dataSourceCacheServerConfiguration;
    @Value("${olap.dataSource.elideSetAutoCommits}")
    private String dataSourceElideSetAutoCommits;
    @Value("${olap.dataSource.maintainTimeStats}")
    private String dataSourceMaintainTimeStats;


    private final static SrConnect HIKARI_CP_UTI = new SrConnect();

    private static Properties properties = null;
    private static HikariDataSource dataSource = null;

    private SrConnect() {

    }

    /**
     * 1.配置和获取数据库连接配置信息
     * 2.扩展HikariCP功能,进行配置
     * 3.获取数据库连接,提供对外获取数据库资源的方法
     */
    private void initConfig() throws IOException {

        properties = new Properties();
        properties.setProperty("driverClassName", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("jdbcUrl", "jdbc:mysql://192.168.217.232:9030/tpch?useSSL=false");
        properties.setProperty("username", "root");
        properties.setProperty("password", "");
        properties.setProperty("dataSource.cachePrepStmts", "true");
        properties.setProperty("dataSource.prepStmtCacheSize", "250");
        properties.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");
        properties.setProperty("dataSource.useServerPrepStmts", "true");
        properties.setProperty("dataSource.useLocalSessionState", "true");
        properties.setProperty("dataSource.rewriteBatchedStatements", "true");
        properties.setProperty("dataSource.cacheResultSetMetadata", "true");
        properties.setProperty("dataSource.cacheServerConfiguration", "true");
        properties.setProperty("dataSource.elideSetAutoCommits", "true");
        properties.setProperty("dataSource.maintainTimeStats", "false");

        properties.put("dataSource.logWriter", new PrintWriter(System.out));

        properties.forEach((k, v) -> {
            log.debug(String.format("key:%s value:%S", k, v));
        });

        log.info(olapType + "初始化配置文件成功.....");
    }

    private void registerHikariCP() {
        if (null != dataSource) {
            return;
        }
        HikariConfig config = new HikariConfig(properties);
        dataSource = new HikariDataSource(config);
    }

    /**
     * 提供对外 获取 HikariCPDatasource 的方法
     * @return
     */
    public static DataSource getHikariCPDataSource() {
        if (null != dataSource) {
            return dataSource;
        }
        try {
            HIKARI_CP_UTI.initConfig();
            HIKARI_CP_UTI.registerHikariCP();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                DataSource dataSource = SrConnect.getHikariCPDataSource();
                System.out.println(Thread.currentThread().getName() + " dataSource = " + dataSource);
                //Thread-5 dataSource = HikariDataSource (HikariPool-6)
                // 可以明显的看出来 是默认使用了数据库连接池 HikariPool....
            }).start();
        }
        /**
         * 测试和验证 datasource 的准确性
         */
        String sql = "SELECT * from orders limit 10;";
        // 获取数据库资源
        DataSource dataSource = SrConnect.getHikariCPDataSource();

        // 使用 try-resource-catch 方式,自动关闭资源
        try (
                //获取数据库连接
                Connection connection = dataSource.getConnection();
                //预编译
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                //获取结果
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            ResultSetMetaData metaData = preparedStatement.getMetaData();
            while (resultSet.next()) {
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println((metaData.getColumnName(i) + " : " + resultSet.getObject(i)));
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }


}

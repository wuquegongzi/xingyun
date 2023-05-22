package cn.cloudcharts.starrocks.metadata.driver;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.starrocks.model.Column;
import cn.cloudcharts.starrocks.model.result.JdbcSelectResult;
import cn.hutool.core.util.ObjectUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

/**
 * @author wuque
 * @title: AbstractDriver
 * @projectName xingyun
 * @description:
 * @date 2023/5/2219:20
 */
public abstract class AbstractDriver implements Driver {

    protected static Logger logger = LoggerFactory.getLogger(AbstractDriver.class);

    protected DriverConfigPO config;

    protected ThreadLocal<Connection> conn = new ThreadLocal<>();

    private HikariDataSource dataSource;


    abstract String getDriverClass();

    @Override
    public boolean isHealth() {
        try {
            if (ObjectUtil.isEmpty(conn.get())) {
                return !conn.get().isClosed();
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public String test() {
        AssertUtil.checkNotNull(config, "无效的数据源配置");
        try {
            Class.forName(getDriverClass());
            DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword())
                    .close();
        } catch (Exception e) {
            logger.error("Jdbc链接测试失败！错误信息为：" + e.getMessage(), e);
            return e.getMessage();
        }
        return "1";
    }

    @Override
    public Driver setDriverConfig(DriverConfigPO config) {
        this.config = config;
        try {
            this.dataSource = createDataSource();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public HikariDataSource createDataSource() throws SQLException {
        if (null == dataSource) {
            synchronized (this.getClass()) {
                if (null == dataSource) {
                    HikariDataSource ds = new HikariDataSource();
                    createDataSource(ds, config);
                    this.dataSource = ds;
                }
            }
        }
        return dataSource;
    }

    protected void createDataSource(HikariDataSource ds, DriverConfigPO config) {

        Properties properties = new Properties();
        properties.setProperty("driverClassName", getDriverClass());
        //"jdbc:mysql://192.168.217.232:9030/tpch?useSSL=false"
        properties.setProperty("jdbcUrl", config.getUrl());
        properties.setProperty("username", config.getUsername());
        properties.setProperty("password", config.getPassword());
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
            logger.debug(String.format("key:%s value:%S", k, v));
        });

        logger.info( "{},初始化配置文件成功.....",config.toString());

        HikariConfig hikariConfig = new HikariConfig(properties);
        ds = new HikariDataSource(hikariConfig);

    }

    @Override
    public Driver connect() {
        if (AssertUtil.isNull(conn.get())) {
            try {
                Class.forName(getDriverClass());
                Connection connection = createDataSource().getConnection();
                conn.set(connection);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    @Override
    public void close() {
        try {
            if (ObjectUtil.isEmpty(conn.get())) {
                conn.get().close();
                conn.remove();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JdbcSelectResult query(String sql, Integer limit) {
        if (AssertUtil.isNull(limit)) {
            limit = 100;
        }

        return null;
    }


    @Override
    public int executeUpdate(String sql) throws Exception {
        AssertUtil.checkNullString(sql, "Sql 语句为空");
        int res = 0;
        try (Statement statement = conn.get().createStatement()) {
            res = statement.executeUpdate(sql);
        }
        return res;
    }

}

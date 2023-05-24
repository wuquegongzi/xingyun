package cn.cloudcharts.starrocks.metadata.driver;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.starrocks.metadata.convert.ITypeConvert;
import cn.cloudcharts.starrocks.metadata.pojo.Column;
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

    public boolean canHandle(String type) {
        return AssertUtil.isEqualsIgnoreCase(getType(), type);
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

                    this.dataSource = createDataSource(config);
                }
            }
        }
        return dataSource;
    }

    protected HikariDataSource createDataSource(DriverConfigPO config) {

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

        return new HikariDataSource(hikariConfig);
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

        JdbcSelectResult result = new JdbcSelectResult();
        List<LinkedHashMap<String, Object>> datas = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<String> columnNameList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet results = null;
        int count = 0;
        try {
            preparedStatement = conn.get().prepareStatement(sql);
            results = preparedStatement.executeQuery();
            if (AssertUtil.isNull(results)) {
                close(preparedStatement, results);
                return result;
            }
            ResultSetMetaData metaData = results.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                columnNameList.add(metaData.getColumnLabel(i));
                Column column = new Column();
                column.setName(metaData.getColumnLabel(i));
                column.setType(metaData.getColumnTypeName(i));
                column.setAutoIncrement(metaData.isAutoIncrement(i));
                column.setNullable(metaData.isNullable(i) == 0 ? false : true);
                columns.add(column);
            }
            result.setColumns(columnNameList);

            while (results.next()) {
                LinkedHashMap<String, Object> data = new LinkedHashMap<>();
                for (int i = 0; i < columns.size(); i++) {
                    data.put(
                            columns.get(i).getName(),
                            getTypeConvert()
                                    .convertValue(
                                            results,
                                            columns.get(i).getName(),
                                            columns.get(i).getType())
                    );
                }
                datas.add(data);
                count++;
                if (count >= limit) {
                    break;
                }
            }
        } catch (Exception e) {
            result.error(e.getMessage());
        } finally {
            close(preparedStatement, results);
            result.setRowData(datas);
            return result;
        }
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

    public abstract ITypeConvert getTypeConvert();

    public void close(PreparedStatement preparedStatement, ResultSet results) {
        try {
            if (null != results) {
                results.close();
            }
            if (null != preparedStatement) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

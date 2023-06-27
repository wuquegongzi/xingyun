package cn.cloudcharts.metadata.driver;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.metadata.convert.ITypeConvert;
import cn.cloudcharts.metadata.model.dto.AlertColumnDTO;
import cn.cloudcharts.metadata.opertion.IDbOpertion;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.cloudcharts.metadata.model.result.ResultColumn;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import cn.cloudcharts.sql.parser.CalciteSqlParser;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zaxxer.hikari.HikariDataSource;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.sql.*;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuque
 * @title: AbstractDriver
 * @projectName xingyun
 * @description:
 * @date 2023/5/2219:20
 */
@Slf4j
public abstract class AbstractDriver implements cn.cloudcharts.metadata.driver.Driver {

    protected static Logger logger = LoggerFactory.getLogger(AbstractDriver.class);

    protected DriverConfigPO config;

    protected volatile ThreadLocal<Connection> conn = new ThreadLocal<>();

    private HikariDataSource dataSource;

    public abstract IDbOpertion getDbOpertion();
    public abstract CalciteSqlParser getCalciteSqlParser();

    abstract String getDriverClass();

    @Override
    public boolean isHealth() {
        try {
            if (ObjectUtil.isNotEmpty(conn.get())) {
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
    public cn.cloudcharts.metadata.driver.Driver setDriverConfig(DriverConfigPO config) {
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
                    createDataSource(ds,config);
                    this.dataSource = ds;
                }
            }
        }
        return dataSource;
    }

    /**
     * https://github.com/brettwooldridge/HikariCP
     * @param config
     * @return
     */
    protected void createDataSource(HikariDataSource ds, DriverConfigPO config) {

        //"jdbc:mysql://192.168.217.232:9030/tpch?useSSL=false"
        ds.setJdbcUrl(config.getUrl());
        ds.setUsername(config.getUsername());
        ds.setPassword(config.getPassword());
        ds.setDriverClassName(getDriverClass());
        ds.setMinimumIdle(0);
        ds.setMaximumPoolSize(50);
        ds.setConnectionTestQuery("select 1");
        ds.setConnectionTimeout(60000);
        ds.setIdleTimeout(30000);
        ds.setKeepaliveTime(60000);
        ds.setMaxLifetime(300000);
        ds.setValidationTimeout(10000);
        ds.setAllowPoolSuspension(true);
        ds.setPoolName("xingyun-dspool-"+config.getName());
        ds.addDataSourceProperty("logWriter",new PrintWriter(System.out));
        ds.addDataSourceProperty("cachePrepStmts",true);
        ds.addDataSourceProperty("prepStmtCacheSize",250);
        ds.addDataSourceProperty("prepStmtCacheSqlLimit",2048);
        ds.addDataSourceProperty("useServerPrepStmts",true);
        ds.addDataSourceProperty("useLocalSessionState",true);
        ds.addDataSourceProperty("rewriteBatchedStatements",true);
        ds.addDataSourceProperty("cacheResultSetMetadata",true);
        ds.addDataSourceProperty("cacheServerConfiguration",true);
        ds.addDataSourceProperty("elideSetAutoCommits",true);
        ds.addDataSourceProperty("maintainTimeStats",false);

        //https://github.com/brettwooldridge/HikariCP/wiki/Dropwizard-HealthChecks
        ds.addHealthCheckProperty("connectivityCheckTimeoutMs","1000");
        ds.addHealthCheckProperty("expected99thPercentileMs", "10");

        logger.info( "{},初始化配置文件成功.....",ds.toString());

        // 设置metric注册器 每10秒打印一次
        // com.zaxxer.hikari.metrics.PoolStats
        LoggingMeterRegistry loggingMeterRegistry = new LoggingMeterRegistry(new LoggingRegistryConfig() {
            @Override
            public String get(String key) {
                return null;
            }
            @Override
            public Duration step() {
                return Duration.ofMinutes(3);
            }
        }, Clock.SYSTEM);
        ds.setMetricRegistry(loggingMeterRegistry);
    }

    @Override
    public Driver connect() {
        try {
            if (ObjectUtil.isNull(conn.get())) {
                Class.forName(getDriverClass());
                Connection connection = createDataSource().getConnection();
                conn.set(connection);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public Driver startTransaction() {
        if (AssertUtil.isNull(conn.get())) {
            try {
                Class.forName(getDriverClass());
                Connection connection = createDataSource().getConnection();
                // 关闭自动提交
                connection.setAutoCommit(false);
                conn.set(connection);
            } catch (ClassNotFoundException | SQLException e) {
                log.error("开启事务异常", e);
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    @Override
    public void commit() {
        try {
            Connection connection = conn.get();
            if(null != connection) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("提交事务异常", e);
        }
    }

    @Override
    public void rollback() {
        try {
            Connection connection = conn.get();
            if(connection != null) {
                //回滚事务
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("回滚事务异常", e);
        }
    }

    @Override
    public void close() {
        try {
            log.info("资源关闭：{}",conn.get());
            if (ObjectUtil.isNotEmpty(conn.get())) {
                conn.get().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            conn.remove();
        }
    }

    @Override
    public JdbcSelectResult query(String sql, Integer limit) {

        if (AssertUtil.isNull(limit)) {
            limit = 100;
        }

        JdbcSelectResult result = new JdbcSelectResult();
        List<LinkedHashMap<String, Object>> datas = new ArrayList<>();
        List<ResultColumn> columns = new ArrayList<>();
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
                ResultColumn column = new ResultColumn();
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

    @Override
    public boolean createTbl(CreateTableDTO dto) throws Exception {
        String sql = buildCreateTableSql(dto).replaceAll("\r\n", " ");
        if (StrUtil.isNotEmpty(sql)) {
            return execute(sql);
        } else {
            return false;
        }
    }

    private String buildCreateTableSql(CreateTableDTO tableDTO) {
        return getDbOpertion().buildCreateTableSql(tableDTO);
    }

    @Override
    public boolean addColumns(AlertColumnDTO dto) throws Exception {
        String sql = buildAddColumnsSql(dto).replaceAll("\r\n", " ");
        if (StrUtil.isNotEmpty(sql)) {
            return execute(sql);
        } else {
            return false;
        }
    }

    private String buildAddColumnsSql(AlertColumnDTO dto) {
        return getDbOpertion().buildAddColumnsSql(dto);
    }

    @Override
    public List<Map<String,Object>> queryAllColumns(String catalogName, String dbName, String tableName) {

        String sql = getDbOpertion().queryAllColumns(catalogName,dbName,tableName);

        List<Map<String,Object>> mapList = null;
        try {
            if (StrUtil.isNotEmpty(sql)) {
                QueryRunner qr = new QueryRunner();
                mapList = qr.query(conn.get(),sql, new MapListHandler());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return mapList;
    }

    @Override
    public List<String> getTableList(String catalogName, String dbName) {
        String sql = getDbOpertion().getTableList(catalogName,dbName);

        List<String> lists = null;
        try {
            if (StrUtil.isNotEmpty(sql)) {
                QueryRunner qr = new QueryRunner();

                List<Map<String, Object>> lists2 = qr.query(conn.get(),sql, new MapListHandler());
                lists = lists2.parallelStream().map(m ->{
                    return ObjectUtil.toString(m.values().parallelStream().findFirst().get());
                }).collect(Collectors.toList());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lists;
    }

    @Override
    public boolean exsitTbl(String catalogName, String dbName, String tblName) {

        String sql = getDbOpertion().exsitTbl(catalogName,dbName,tblName);

        try {
            if (StrUtil.isNotEmpty(sql)) {
                QueryRunner qr = new QueryRunner();
                List<Map<String, Object>> lists = qr.query(conn.get(),sql, new MapListHandler());
                return  lists.size() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean exsitSchema(String catalogName, String dbName, boolean autoCreate) {

        String sql = getDbOpertion().exsitSchema(catalogName,dbName);

        try {
            if (StrUtil.isNotEmpty(sql)) {
                QueryRunner qr = new QueryRunner();
                List<Map<String, Object>> lists = qr.query(conn.get(),sql, new MapListHandler());

                int res = lists.size();
                if(res < 1 && autoCreate){
                    sql = getDbOpertion().createSchema(dbName);
                    qr.update(sql);
                }

                return  res > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

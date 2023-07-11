package cn.cloudcharts.metadata.driver;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.metadata.model.dto.AlertColumnDTO;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import cn.cloudcharts.metadata.model.result.SqlExplainResult;
import cn.cloudcharts.metadata.task.SyncTaskGenInfo;
import cn.cloudcharts.sql.parser.CalciteSqlParser;
import cn.cloudcharts.sql.parser.model.Table;
import org.apache.calcite.sql.parser.SqlParseException;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

/**
 * @author wuque
 * @title: Driver
 * @projectName xingyun
 * @description: 动态数据源接口
 * @date 2023/5/2219:11
 */
public interface Driver extends AutoCloseable{

    static Optional<Driver> get(DriverConfigPO config) {
        AssertUtil.checkNotNull(config, "数据源配置不能为空");
        ServiceLoader<Driver> drivers = ServiceLoader.load(Driver.class);
        for (Driver driver : drivers) {
            if (driver.canHandle(config.getType())) {
                return Optional.of(driver.setDriverConfig(config));
            }
        }
        return Optional.empty();
    }


    static Driver build(DriverConfigPO config) {
        String key = config.getName();
        if (DriverPool.exist(key)) {
            return getHealthDriver(key);
        }
        synchronized (Driver.class) {
            Optional<Driver> optionalDriver = Driver.get(config);
            if (!optionalDriver.isPresent()) {
                throw new RuntimeException(
                        "缺少数据源类型【" + config.getType() + "】的依赖，请在 lib 下添加对应的扩展依赖");
            }
            Driver driver = optionalDriver.get().connect();
            DriverPool.push(key, driver);
            return driver;
        }
    }

    static Driver getHealthDriver(String key) {
        Driver driver = DriverPool.get(key);
        if (driver.isHealth()) {
            return driver;
        } else {
//            DriverPool.remove(key);
            return driver.connect();
        }
    }

    static Driver buildUnconnected(DriverConfigPO config) {
        synchronized (Driver.class) {
            Optional<Driver> optionalDriver = Driver.get(config);
            if (!optionalDriver.isPresent()) {
                throw new ServiceException(
                        "缺少数据源类型【" + config.getType() + "】的依赖，请在 lib 下添加对应的扩展依赖");
            }
            return optionalDriver.get();
        }
    }

    CalciteSqlParser getCalciteSqlParserHelper();

    boolean canHandle(String type);

    String getType();

    Driver setDriverConfig(DriverConfigPO config);

    String test();

    boolean isHealth();

    Driver connect();

    String getConnDbName();

    /**
     * 开启事务
     * @return
     */
    Driver startTransaction();
    void commit();
    void rollback();

    @Override
    void close();

    boolean execute(String sql) throws Exception;

    JdbcSelectResult executeSql(String sql, Integer limit) throws Exception;

    int executeUpdate(String sql) throws Exception;

    JdbcSelectResult query(String sql, Integer limit);

    List<SqlExplainResult> explain(String sql);


    boolean createTbl(CreateTableDTO dto) throws Exception;

    boolean addColumns(AlertColumnDTO dto) throws Exception;

    List<Map<String,Object>> queryAllColumns(String catalogName, String dbName, String tableName);

    List<String> getTableList(String catalogName, String dbName);

    boolean exsitTbl(String catalogName, String dbName, String tblName);

    boolean exsitSchema(String catalogName, String dbName,boolean autoCreate);

    boolean syncTbl(List<Table> tbls, String srcCatalog, String srcCatalogType,boolean isAutoCreate,boolean syncDataByMetaChange,Map parmMap) throws SqlParseException, SQLException;

    List<String> getSchemaList(String defaultCatalog);

    boolean submitSyncTask(SyncTaskGenInfo taskGenInfo);

    List<Map<String,Object>> getPartitionsList(String schema, String tbl);

    boolean tblNormal(String schema, String tbl, String operType);

}

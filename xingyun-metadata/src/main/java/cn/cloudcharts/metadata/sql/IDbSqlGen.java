package cn.cloudcharts.metadata.sql;

import cn.cloudcharts.metadata.model.dto.AlertColumnDTO;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;

/**
 * @author wuque
 * @title: IDdlOpertion
 * @projectName xingyun
 * @description: DDL操作接口
 * @date 2023/5/2919:53
 */
public interface IDbSqlGen {

    String buildCreateTableSql(CreateTableDTO tableDTO);

    String queryAllColumns(String catalogName, String dbName, String tableName);

    String getTableList(String catalogName, String dbName);

    String getExecQuery(String sql, Integer limit);

    String exsitTbl(String catalogName, String dbName, String tblName);

    String buildAddColumnsSql(AlertColumnDTO dto);

    String exsitSchema(String catalogName, String dbName);

    String createSchema(String dbName);
}

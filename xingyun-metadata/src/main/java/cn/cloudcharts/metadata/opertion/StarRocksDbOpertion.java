package cn.cloudcharts.metadata.opertion;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.common.support.CustomSQL;
import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author wuque
 * @title: StarRocksDdlOpertion
 * @projectName xingyun
 * @description:
 * @date 2023/5/2919:57
 */
public class StarRocksDbOpertion extends AbstractDbOpertion {


    @Override
    public String buildCreateTableSql(CreateTableDTO dto) {

            if("1".equals(dto.getTblType())){ //外表

            }

            switch (dto.getTblDataType()){ // 0-明细 1-聚合 2-更新 3-主键
                case "0":

                case "1":

                case "2":

                case "3":

                default:
                    throw new ServiceException("Please select a data model! " +
                            "StarRocks provides four table types: [0]Duplicate Key table, [1]Aggregate table, [2]Unique Key table, and [3]Primary Key table.");
            }
    }

    @Override
    public String queryAllColumns(String catalogName, String dbName, String tableName) {

        AssertUtil.checkNullString(dbName, "数据库名不可空");
        AssertUtil.checkNullString(tableName, "表名不可空");

        Map map = Maps.newHashMap();
        map.put("catalogName", StrUtil.isEmpty(catalogName)?"default_catalog":catalogName);
        map.put("dbName",dbName);
        map.put("tableName",tableName);

        return CustomSQL.getInstance().get("ops.starrocks.queryAllColumns",map);
    }


}

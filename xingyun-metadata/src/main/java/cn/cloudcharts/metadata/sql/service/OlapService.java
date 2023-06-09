package cn.cloudcharts.metadata.sql.service;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.metadata.sql.mapper.DdlMapper;
import cn.cloudcharts.metadata.sql.mapper.DmlMapper;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import cn.cloudcharts.metadata.model.result.TableColumnResult;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: OlapService
 * @projectName xingyun
 * @description:
 * @date 2023/5/1210:52
 */
//@Service
@Deprecated
public class OlapService {

//    @Autowired
    DdlMapper ddlMapper;
//    @Autowired
    DmlMapper dmlMapper;

    @Value("${spring.datasource.olap.query.limit:100}")
    private int maxVal;

    public boolean createTbl(CreateTableDTO dto) {

        if("1".equals(dto.getTblType())){ //外表
          return ddlMapper.createExternalTbl(dto) > 0;
        }

        switch (dto.getTblDataType()){ // 0-明细 1-聚合 2-更新 3-主键
            case "0":
                return ddlMapper.createDetailTbl(dto) > 0;
            case "1":
                return ddlMapper.createAggregateTbl(dto) > 0;
            case "2":
                return ddlMapper.createUniqueTbl(dto) > 0;
            case "3":
                return ddlMapper.createPrimaryTbl(dto) > 0;
            default:
                throw new ServiceException("Please select a data model! " +
                        "StarRocks provides four table types: [0]Duplicate Key table, [1]Aggregate table, [2]Unique Key table, and [3]Primary Key table.");
        }
    }

    public Boolean createTbl(String sql) {
        return ddlMapper.createTableBySql(sql) > 0;
    }

    public  List<String>  getTableList() {

        return dmlMapper.getTableList();
    }

    public List<TableColumnResult> queryAllColumns(String table) {
        return ddlMapper.queryAllColumns(table);
    }

    public List<Map> query(String sql) {
        Map map = Maps.newHashMap();
        map.put("sql",sql);
        map.put("maxVal",maxVal);

        try {
            return dmlMapper.execQuery(map);
        } catch (Exception e) {
            throw new ServiceException(e.getLocalizedMessage());
        }
    }

}

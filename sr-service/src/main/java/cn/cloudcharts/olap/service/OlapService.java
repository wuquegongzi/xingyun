package cn.cloudcharts.olap.service;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.olap.mapper.DdlMapper;
import cn.cloudcharts.olap.mapper.DmlMapper;
import cn.cloudcharts.olap.mapper.ExecuteMapper;
import cn.cloudcharts.olap.model.CreateTableDTO;
import cn.cloudcharts.olap.model.ddl.TableColumn;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: OlapService
 * @projectName xingyun
 * @description:
 * @date 2023/5/1210:52
 */
@Service
public class OlapService {

    @Autowired
    DdlMapper ddlMapper;
    @Autowired
    DmlMapper dmlMapper;
    @Autowired
    ExecuteMapper executeMapper;

    @Value("${spring.datasource.query.limit:1000}")
    private int maxVal;

    public boolean createTbl(CreateTableDTO dto) {

        if("1".equals(dto.getTblType())){ //外表
          return ddlMapper.createExternalTbl(dto) > 0;
        }

        switch (dto.getTblDataType()){ // 0-明细 1-聚合 2-更新 3-主键
            case "0" ->{
                return ddlMapper.createDetailTbl(dto) > 0;
            }
            case "1" ->{
                return ddlMapper.createAggregateTbl(dto) > 0;
            }
            case "2" ->{
                return ddlMapper.createUniqueTbl(dto) > 0;
            }
            case "3" ->{
                return ddlMapper.createPrimaryTbl(dto) > 0;
            }
            default -> {
                throw new ServiceException("Please select a data model! " +
                        "StarRocks provides four table types: [0]Duplicate Key table, [1]Aggregate table, [2]Unique Key table, and [3]Primary Key table.");
            }
        }
    }

    public Boolean createTbl(String sql) {
        return ddlMapper.createTableBySql(sql) > 0;
    }

    public  List<String>  getTableList() {
        return dmlMapper.getTableList();
    }

    public List<TableColumn> queryAllColumns(String table) {
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


    public List<String> execute(String sql) {
       return executeMapper.execute(sql);
    }
}

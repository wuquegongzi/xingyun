package cn.cloudcharts.starrocks.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: DmlMapper
 * @projectName xingyun
 * @description: DML操作
 * @date 2023/5/818:58
 */
@Mapper
public interface DmlMapper {

    List<String> getTableList();

    List<Map> execQuery(Map map);

}

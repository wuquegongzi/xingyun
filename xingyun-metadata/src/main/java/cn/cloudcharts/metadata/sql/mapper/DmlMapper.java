package cn.cloudcharts.metadata.sql.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: DmlMapper
 * @projectName xingyun
 * @description: DML操作
 * @date 2023/5/818:58
 */
@Deprecated
public interface DmlMapper {

    List<String> getTableList();

    List<Map> execQuery(Map map);

}

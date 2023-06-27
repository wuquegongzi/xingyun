package cn.cloudcharts.metadata.task.schema;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: SchemaSync
 * @projectName xingyun
 * @description:
 * @date 2023/6/2714:35
 */
public interface SchemaSync<T> {

   T transformTbl(String schemaName,String tblName, List<Map<String,Object>> src, String srcType,String sinkType);

}

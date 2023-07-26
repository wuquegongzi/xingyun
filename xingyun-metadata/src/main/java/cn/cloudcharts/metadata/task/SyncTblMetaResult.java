package cn.cloudcharts.metadata.task;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author wuque
 * @title: SyncTblMetaResult
 * @projectName xingyun
 * @description:
 * @date 2023/7/1019:00
 */
@Data
public class SyncTblMetaResult {

    boolean result;
    //是否同步了结构
    boolean metaChange;
    List<Map<String,Object>> srcColumnMapList;
    List<Map<String,Object>> sinkColumnMapList;

}

package cn.cloudcharts.metadata.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuque
 * @title: TaskGenInfo
 * @projectName xingyun
 * @description:
 * @date 2023/6/2717:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SyncTaskGenInfo {

    private boolean async;
    private String label;
    private String taskName;
    private String taskType;
    /**
     * OVERWRITE / INTO
     */
    private String insertType;

    private String srcCatalog;
    private String srcDb;
    private String srcTbl;
    private String srcCols;

    private String sinkCatalog;
    private String sinkDb;
    private String sinkTbl;
    private String sinkCols;




}

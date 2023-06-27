package cn.cloudcharts.metadata.task;

import java.sql.Connection;

/**
 * @author wuque
 * @title: ITask
 * @projectName xingyun
 * @description: 任务接口
 * @date 2023/6/2717:02
 */
public interface ITaskOpertion {

    boolean submitSyncTask(Connection connection, SyncTaskGenInfo taskGenInfo);

}

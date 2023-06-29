package cn.cloudcharts.scheduler.sync;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;

/**
 * @author wuque
 * @title: CatalogSchemaAndDataBatchSyncTask
 * @projectName xingyun
 * @description:
 * @date 2023/6/2819:38
 */
@Configuration
public class CatalogSchemaAndDataBatchSyncTask {

    @Async
    @Scheduled(cron = "0/5 * * * * ?")
    public void ScheduledM1() {
        System.err.println("【静态】执行定时任务:" + LocalTime.now().toString());
    }

}

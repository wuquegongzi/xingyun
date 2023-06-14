package cn.cloudcharts.controller.open;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.model.request.JobInfoRequest;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuque
 * @title: SyncController
 * @projectName xingyun
 * @description: 数据同步[导入-导出]
 * @date 2023/6/1320:47
 */
@Tag(name = "JobApi", description = "作业接口")
@RestController("/job")
public class JobController {

    @ApiOperationSupport(author = "安山")
    @Operation(summary = "作业提交",
            description = ""
    )
    @PostMapping("/submit")
    public R<?> submit(@RequestBody JobInfoRequest taskInfoRequest){

        return R.ok();
    }

    @ApiOperationSupport(author = "安山")
    @Operation(summary = "作业暂停",
            description = ""
    )
    @PostMapping("/pause")
    public R<?> pause(@RequestBody JobInfoRequest taskInfoRequest){
        return R.ok();
    }


    @ApiOperationSupport(author = "安山")
    @Operation(summary = "作业恢复",
            description = ""
    )
    @PostMapping("/resume")
    public R<?> resume(@RequestBody JobInfoRequest taskInfoRequest){
        return R.ok();
    }

    @ApiOperationSupport(author = "安山")
    @Operation(summary = "作业修改",
            description = ""
    )
    @PostMapping("/update")
    public R<?> update(@RequestBody JobInfoRequest taskInfoRequest){

        return R.ok();
    }

    @ApiOperationSupport(author = "安山")
    @Operation(summary = "作业停止",
            description = ""
    )
    @PostMapping("/stop")
    public R<?> stop(@RequestBody JobInfoRequest taskInfoRequest){

        return R.ok();
    }


}

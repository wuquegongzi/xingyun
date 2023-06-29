package cn.cloudcharts.controller.open;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.model.request.QueryTblColumnRequest;
import cn.cloudcharts.model.request.QueryTblRequest;
import cn.cloudcharts.model.request.SqlExecRequest;
import cn.cloudcharts.model.request.SqlQueryRequest;
import cn.cloudcharts.service.ExecuteService;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author wuque
 * @title: ExecuteController
 * @projectName xingyun
 * @description:  OLAP 操作接口
 * @date 2023/5/417:45
 */
@Tag(name = "ExecuteApi", description = "OLAP 操作接口")
@RestController
@RequestMapping(value = "/exec")
public class ExecuteController {

    @Autowired
    ExecuteService executeService;


    /**
     * 执行SQL脚本
     * @param sqlRequest
     * @return
     */
    @ApiOperationSupport(author = "安山")
    @Operation(summary = "执行SQL脚本",
            description = "多个SQL以通用分号[;]分割,仅返回最后一个查询结果"
    )
    @ApiResponse(description = "执行结果", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JdbcSelectResult.class)))
    @PostMapping("/executeSql")
    public R<JdbcSelectResult> executeSql(@RequestBody SqlExecRequest sqlRequest){
        return R.ok(executeService.executeCommonSql(sqlRequest));
    }

    @ApiOperationSupport(author = "安山")
    @Operation(summary = "执行单SQL QUERY"
    )
    @ApiResponse(description = "执行结果", content = @Content(mediaType = "application/json", schema = @Schema(implementation = JdbcSelectResult.class)))
    @PostMapping("/query")
    public R<JdbcSelectResult> query(@RequestBody SqlQueryRequest sqlRequest){
        return R.ok(executeService.query(sqlRequest));
    }

    @ApiOperationSupport(author = "安山")
    @Operation(summary = "根据数据源获取某表的字段集合")
    @PostMapping("/queryAllColumns")
    public R queryAllColumns(@RequestBody QueryTblColumnRequest queryTblRequest){
        return R.ok(executeService.queryAllColumns(queryTblRequest));
    }

    @ApiOperationSupport(author = "安山")
    @Operation(summary = "根据数据源获取表集合")
    @PostMapping("/getTableList")
    public R getTableList(@RequestBody QueryTblRequest queryTblRequest){
        return R.ok(executeService.getTableList(queryTblRequest));
    }



}

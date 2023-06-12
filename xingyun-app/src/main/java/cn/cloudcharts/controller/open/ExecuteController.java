package cn.cloudcharts.controller.open;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.model.request.QueryTblColumnRequest;
import cn.cloudcharts.model.request.QueryTblRequest;
import cn.cloudcharts.model.request.SqlExecRequest;
import cn.cloudcharts.service.ExecuteService;
import cn.cloudcharts.metadata.model.result.JdbcSelectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author wuque
 * @title: ExecuteController
 * @projectName xingyun
 * @description:  OLAP 操作接口
 * @date 2023/5/417:45
 */
@RestController("/exec")
public class ExecuteController {

    @Autowired
    ExecuteService executeService;


    /**
     * 执行SQL脚本
     * @param sqlDTO
     * @return
     */
    @PostMapping("/executeSql")
    public R<JdbcSelectResult> executeSql(@RequestBody SqlExecRequest sqlRequest){
        return R.ok(executeService.executeCommonSql(sqlRequest));
    }

    @PostMapping("/queryAllColumns")
    public R queryAllColumns(@RequestBody QueryTblColumnRequest queryTblRequest){
        return R.ok(executeService.queryAllColumns(queryTblRequest));
    }

    @PostMapping("/getTableList")
    public R getTableList(@RequestBody QueryTblRequest queryTblRequest){
        return R.ok(executeService.getTableList(queryTblRequest));
    }


}

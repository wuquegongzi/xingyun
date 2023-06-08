package cn.cloudcharts.controller.open;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.model.request.SqlRequest;
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
    public R<JdbcSelectResult> executeSql(@RequestBody SqlRequest sqlRequest){
        return R.ok(executeService.executeCommonSql(sqlRequest));
    }

//    @GetMapping("/queryAllColumns")
//    public R queryAllColumns(@RequestParam(name="table") String table){
//        return R.ok(olapService.queryAllColumns(table));
//    }
//
//    @GetMapping("/getTableList")
//    public R getTableList(){
//        return R.ok(olapService.getTableList());
//    }
//

}

package cn.cloudcharts.api;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.model.dto.SqlDTO;
import cn.cloudcharts.service.ExecuteService;
import cn.cloudcharts.starrocks.model.result.JdbcSelectResult;
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
    public R<JdbcSelectResult> executeSql(@RequestBody SqlDTO sqlDTO){
        return R.ok(executeService.executeCommonSql(sqlDTO));
    }

    @GetMapping("/cancel")
    public R<Boolean> cancel(@RequestParam String jobId) {
        return R.ok(executeService.cancel(jobId), "停止成功");
    }

//    /**
//     *  向导建表
//     * @return
//     */
//    @PostMapping("/createTbl")
//    public R createTbl(@RequestBody CreateTableDTO dto){
//       return R.ok(olapService.createTbl(dto));
//    }
//
//    /**
//     *  脚本建表
//     * @return
//     */
//    @PostMapping("/createTblBySql")
//    public R createTbl(@RequestParam(name="sql") String sql){
//        return R.ok(olapService.createTbl(sql));
//    }
//
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

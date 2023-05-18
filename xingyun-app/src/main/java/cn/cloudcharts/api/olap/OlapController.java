package cn.cloudcharts.api.olap;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.olap.model.CreateTableDTO;
import cn.cloudcharts.olap.service.ExecuteService;
import cn.cloudcharts.olap.service.OlapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author wuque
 * @title: HelloController
 * @projectName xingyun
 * @description:  OLAP 操作接口
 * @date 2023/5/417:45
 */
@RestController("/olap")
public class OlapController {

    @Autowired
    OlapService olapService;
    @Autowired
    ExecuteService executeService;

    /**
     *  向导建表
     * @return
     */
    @PostMapping("/createTbl")
    public R createTbl(@RequestBody CreateTableDTO dto){
       return R.ok(olapService.createTbl(dto));
    }

    /**
     *  脚本建表
     * @return
     */
    @PostMapping("/createTblBySql")
    public R createTbl(@RequestParam(name="sql") String sql){
        return R.ok(olapService.createTbl(sql));
    }

    @GetMapping("/queryAllColumns")
    public R queryAllColumns(@RequestParam(name="table") String table){
        return R.ok(olapService.queryAllColumns(table));
    }

    @GetMapping("/getTableList")
    public R getTableList(){
        return R.ok(olapService.getTableList());
    }

    @GetMapping("/queryBySql")
    public R query(@RequestParam(name="sql") String sql){
        return R.ok(olapService.query(sql));
    }

    /**
     *
     * @param sql
     * @return
     */
    @GetMapping("/execute")
    public R execute(@RequestParam(name="sql") String sql){
        return R.ok(executeService.execute(sql));
    }



}

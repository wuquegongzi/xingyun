package cn.cloudcharts.controller.open;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.service.DdlService;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuque
 * @title: DdlController
 * @projectName xingyun
 * @description: DDL操作
 * @date 2023/5/2919:37
 */
@RestController("/ddl")
public class DdlController {

    @Autowired
    DdlService ddlService;

    /**
     *  向导建表
     * @return
     */
    @PostMapping("/createTbl")
    public R createTbl(@RequestBody CreateTableDTO dto){
       return R.ok(ddlService.createTbl(dto));
    }

    /**
     *  脚本建表
     * @return
     */
    @PostMapping("/createTblBySql")
    public R createTbl(@RequestParam(name="sql") String sql){
        return R.ok(ddlService.createTbl(sql));
    }

}

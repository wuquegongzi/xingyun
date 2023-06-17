package cn.cloudcharts.controller.open;

import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.metadata.model.dto.AlertColumnDTO;
import cn.cloudcharts.service.DdlService;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "DdlApi", description = "OLAP 建模接口")
@RestController("/ddl")
public class DdlController {

    @Autowired
    DdlService ddlService;

    /**
     *  向导建表
     * @return
     */
    @ApiOperationSupport(author = "安山")
    @Operation(summary = "向导建表")
    @PostMapping("/createTbl")
    public R createTbl(@RequestBody CreateTableDTO dto){

        return R.ok(ddlService.createTbl(dto));
    }

    /**
     *  脚本建表
     * @return
     */
    @ApiOperationSupport(author = "安山")
    @Operation(summary = "脚本建表",
            parameters = {
                    @Parameter(name = "databaseId", description = "数据源ID"),
                    @Parameter(name = "sql", description = "建表语句")
            })
    @PostMapping("/createTblBySql")
    public R createTbl(@RequestParam(name="databaseId") Integer databaseId,
                       @RequestParam(name="sql") String sql){

        return R.ok(ddlService.createTbl(databaseId,sql));
    }


    @ApiOperationSupport(author = "安山")
    @Operation(summary = "添加字段")
    @PostMapping("/addColumns")
    public R addColumns(@RequestBody AlertColumnDTO dto){

        return R.ok(ddlService.addColumns(dto));
    }
}

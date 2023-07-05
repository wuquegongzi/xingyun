package cn.cloudcharts.controller.admin;


import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.model.entity.Database;
import cn.cloudcharts.service.IDatabaseService;
import cn.cloudcharts.model.request.DataBaseRequest;
import cn.cloudcharts.core.domain.R;
import cn.cloudcharts.metadata.driver.DriverPool;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * database management
 * </p>
 *
 * @author wuque
 * @since 2023-05-17
 */
@RestController
@RequestMapping(value = "/database")
@Tag(name = "DsApi", description = "多数据源管理接口")
public class DatabaseController {

    @Autowired
    IDatabaseService databaseService;

    /**
     * 列表查询
     * @param dataBaseRequest
     * @return
     */
    @Operation(summary = "列表查询")
    @PostMapping(value ="/list")
    public R<PageInfo> list(@RequestBody DataBaseRequest dataBaseRequest){

        PageHelper.startPage(dataBaseRequest.getPageNum(), dataBaseRequest.getPageSize());
        List<Database> databaseList = databaseService.list();
        PageInfo<Database> info = new PageInfo<>(databaseList, dataBaseRequest.getPageSize());
        return R.ok(info);
    }

    /**
     * 添加数据源
     * @param database
     * @return
     */
    @Operation(summary = "添加数据源")
    @PostMapping(value ="/add")
    public R add(@RequestBody Database database){

        return R.ok(databaseService.save(database));
    }

    @Operation(summary = "添加或者更新数据源")
    @PutMapping
    public R<Void> saveOrUpdate(@RequestBody Database database) {
        if (databaseService.saveOrUpdateDataBase(database)) {
            DriverPool.remove(database.getName());
            return R.ok();
        } else {
            return R.fail("更新失败");
        }
    }

    @Operation(summary = "根据ID获取明细")
    @PostMapping("/getOneById")
    public R<Database> getOneById(@RequestParam(name = "id") String id) {
        Database database = databaseService.getById(id);
        return R.ok(database, "获取成功");
    }

    /**
     * 测试连通性
     * @param database
     * @return
     */
    @Operation(summary = "测试连通性")
    @PostMapping("/testConnect")
    public R testConnect(@RequestBody Database database) {
        String msg = databaseService.testConnect(database);
        boolean isHealthy = "1".equals(msg);
        if (isHealthy) {
            return R.ok("数据源连接测试成功!");
        } else {
            return R.fail(msg);
        }
    }

    @Operation(summary = "清除所有已连接")
    @PostMapping("/cleanAll")
    public R cleanAll() {
        DriverPool.cleanAll();
        return R.ok();
    }

    @Operation(summary = "根据ID清除连接")
    @PostMapping("/cleanOneById")
    public R cleanOneById(@RequestParam(name = "id") String id) {
        Database database = databaseService.getById(id);
        if(ObjectUtil.isNotEmpty(database)){
            DriverPool.remove(database.getName());
            return R.ok();
        }
        throw new ServiceException("不存在该数据源，请检查！");
    }

}

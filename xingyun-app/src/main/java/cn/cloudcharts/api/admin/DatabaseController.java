package cn.cloudcharts.api.admin;


import cn.cloudcharts.admin.entity.Database;
import cn.cloudcharts.admin.service.IDatabaseService;
import cn.cloudcharts.api.admin.request.DataBaseRequest;
import cn.cloudcharts.core.domain.R;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
@RequestMapping("/database")
public class DatabaseController {

    @Autowired
    IDatabaseService databaseService;

    @GetMapping(value ="/list")
    public R<PageInfo> list(@RequestBody DataBaseRequest dataBaseRequest){

        PageHelper.startPage(dataBaseRequest.getPageNum(), dataBaseRequest.getPageSize());
        List<Database> databaseList = databaseService.list();
        PageInfo<Database> info = new PageInfo<>(databaseList, dataBaseRequest.getPageSize());
        return R.ok(info);
    }

    @PostMapping(value ="/add")
    public R add(@RequestBody Database database){

        return R.ok(databaseService.save(database));
    }

}

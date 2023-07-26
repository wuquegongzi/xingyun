package cn.cloudcharts.service.impl;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.mapper.DatabaseMapper;
import cn.cloudcharts.metadata.driver.Driver;
import cn.cloudcharts.model.entity.Database;
import cn.cloudcharts.service.IDatabaseService;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * database management 服务实现类
 * </p>
 *
 * @author wuque
 * @since 2023-05-18
 */
@Service
public class DatabaseServiceImpl extends ServiceImpl<DatabaseMapper, Database> implements IDatabaseService {

    @Override
    public String testConnect(Database database) {
        return Driver.buildUnconnected(database.getDriverConfig()).test();
    }

    @Override
    public boolean saveOrUpdateDataBase(Database database) {
        if (AssertUtil.isNull(database)) {
            return false;
        }
        if (AssertUtil.isNull(database.getId())) {
            LambdaQueryWrapper<Database> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Database::getName,database.getName());
            Database exsit = getOne(queryWrapper);
            if(ObjectUtil.isNotEmpty(exsit)){
                throw new RuntimeException("["+database.getName()+"]:A data source with this name already exists!");
            }

            return save(database);
        } else {
            Database dataBaseInfo = getById(database.getId());
            if (AssertUtil.isNull(database.getUrl())) {
                database.setUrl(dataBaseInfo.getUrl());
            }
            if (AssertUtil.isNull(database.getUsername())) {
                database.setUsername(dataBaseInfo.getUsername());
            }
            if (AssertUtil.isNull(database.getPassword())) {
                database.setPassword(dataBaseInfo.getPassword());
            }
            return updateById(database);
        }
    }

}

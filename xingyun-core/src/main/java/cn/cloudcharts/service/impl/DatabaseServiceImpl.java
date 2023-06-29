package cn.cloudcharts.service.impl;

import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.model.entity.Database;
import cn.cloudcharts.mapper.DatabaseMapper;
import cn.cloudcharts.service.IDatabaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import cn.cloudcharts.metadata.driver.Driver;

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

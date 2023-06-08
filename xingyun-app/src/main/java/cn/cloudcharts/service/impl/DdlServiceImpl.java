package cn.cloudcharts.service.impl;

import cn.cloudcharts.common.exception.ServiceException;
import cn.cloudcharts.common.utils.AssertUtil;
import cn.cloudcharts.model.entity.Database;
import cn.cloudcharts.service.DdlService;
import cn.cloudcharts.service.IDatabaseService;
import cn.cloudcharts.metadata.driver.Driver;
import cn.cloudcharts.metadata.model.dto.CreateTableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wuque
 * @title: DdlServiceImpl
 * @projectName xingyun
 * @description: 建表DDL操作接口
 * @date 2023/5/2919:39
 */
@Service
public class DdlServiceImpl implements DdlService {

    @Autowired
    private IDatabaseService databaseService;

    @Override
    public boolean createTbl(CreateTableDTO dto) {
        Database database = databaseService.getById(dto.getDatabaseId());
        AssertUtil.isNull(database,"该数据源不存在，请检查！");

        boolean is = false;
        try {
            Driver driver = Driver.build(database.getDriverConfig());
            is = driver.createTbl(dto);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return is;
    }

    @Override
    public boolean createTbl(Integer databaseId, String sql) {
        Database database = databaseService.getById(databaseId);
        AssertUtil.isNull(database,"该数据源不存在，请检查！");

        boolean is = false;
        try {
            Driver driver = Driver.build(database.getDriverConfig());
            is = driver.execute(sql);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
        return is;
    }
}

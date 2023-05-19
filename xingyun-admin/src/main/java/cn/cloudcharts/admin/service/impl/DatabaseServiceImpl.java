package cn.cloudcharts.admin.service.impl;

import cn.cloudcharts.admin.model.entity.Database;
import cn.cloudcharts.admin.mapper.DatabaseMapper;
import cn.cloudcharts.admin.service.IDatabaseService;
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

}

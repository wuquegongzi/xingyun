package cn.cloudcharts.service.impl;

import cn.cloudcharts.model.entity.Database;
import cn.cloudcharts.mapper.DatabaseMapper;
import cn.cloudcharts.service.IDatabaseService;
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

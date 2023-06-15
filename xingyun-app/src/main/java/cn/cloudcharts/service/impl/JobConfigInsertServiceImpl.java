package cn.cloudcharts.service.impl;

import cn.cloudcharts.mapper.JobConfigInsertMapper;
import cn.cloudcharts.model.entity.JobConfigInsert;
import cn.cloudcharts.service.IJobConfigInsertService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * insert方式导入数据任务配置表 服务实现类
 * </p>
 *
 * @author wuque
 * @since 2023-06-15
 */
@Service
public class JobConfigInsertServiceImpl extends ServiceImpl<JobConfigInsertMapper, JobConfigInsert> implements IJobConfigInsertService {

}

package cn.cloudcharts.service.impl;

import cn.cloudcharts.mapper.JobBaseMapper;
import cn.cloudcharts.model.entity.JobBase;
import cn.cloudcharts.service.IJobBaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 作业基础信息 服务实现类
 * </p>
 *
 * @author wuque
 * @since 2023-06-15
 */
@Service
public class JobBaseServiceImpl extends ServiceImpl<JobBaseMapper, JobBase> implements IJobBaseService {

}

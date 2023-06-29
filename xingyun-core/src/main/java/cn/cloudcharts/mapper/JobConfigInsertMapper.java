package cn.cloudcharts.mapper;

import cn.cloudcharts.model.entity.JobConfigInsert;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * insert方式导入数据任务配置表 Mapper 接口
 * </p>
 *
 * @author wuque
 * @since 2023-06-15
 */
@Mapper
public interface JobConfigInsertMapper extends BaseMapper<JobConfigInsert> {

}

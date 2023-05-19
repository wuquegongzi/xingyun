package cn.cloudcharts.admin.mapper;

import cn.cloudcharts.admin.model.entity.Database;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * database management Mapper 接口
 * </p>
 *
 * @author wuque
 * @since 2023-05-18
 */
@Mapper
public interface DatabaseMapper extends BaseMapper<Database> {

}

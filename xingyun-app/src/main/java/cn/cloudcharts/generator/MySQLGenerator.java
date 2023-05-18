package cn.cloudcharts.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import org.junit.jupiter.api.Test;

/**
 * @author wuque
 * @title: MySQLGenerator
 * @projectName xingyun
 * @description:
 * @date 2023/5/1717:44
 */
public class MySQLGenerator extends BaseGenerator{
    /**
     * 数据源配置
     */
    private static final DataSourceConfig DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://192.168.216.154:31984/xingyun?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true", "root", "1qaz2wsx")
            .schema("xingyun")
            .build();

    @Test
    public void testSimple() {
        AutoGenerator generator = new AutoGenerator(DATA_SOURCE_CONFIG);
        generator.strategy(strategyConfig().build());
        generator.global(globalConfig().build());
        generator.execute();
    }
}

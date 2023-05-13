package cn.cloudcharts.ds;

import cn.cloudcharts.common.enums.DataSourceType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wuque
 * @title: DynamicDataSourceConfig
 * @projectName xingyun
 * @description:
 * @date 2023/5/1310:32
 */
@Configuration
public class DynamicDataSourceConfig {

        @Bean(name="olap")
        @ConfigurationProperties("spring.datasource.olap")
        public DataSource olapDs(){
            return DataSourceBuilder.create().build();
        }

        @Bean(name ="rdbms")
        @ConfigurationProperties("spring.datasource.rdbms")
        public DataSource rdbmsDs(){
            return DataSourceBuilder.create().build();
        }

        @Bean(name="logdb")
        @ConfigurationProperties("spring.datasource.logdb")
        public DataSource logdbDs(){
            return DataSourceBuilder.create().build();
        }

        @Bean(name="dynamicDataSource")
        @Primary
        public DynamicDataSource dataSource() {
            Map<Object, Object> targetDataSources = new HashMap<>(3);
            targetDataSources.put(DataSourceType.OLAP.name().toLowerCase(), olapDs());
            targetDataSources.put(DataSourceType.RDBMS.name().toLowerCase(), rdbmsDs());
            targetDataSources.put(DataSourceType.LOGDB.name().toLowerCase(), logdbDs());
            return new DynamicDataSource(olapDs(), targetDataSources);
        }
}

package cn.cloudcharts.dfs.config;

import cn.cloudcharts.dfs.utils.HdfsUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * @author wuque
 * @title: DfsConfig
 * @projectName xingyun
 * @description:
 * @date 2023/5/1511:54
 */
@Configuration
@ComponentScan(value= "cn.cloudcharts.dfs")
@PropertySource("classpath:datasource_default.properties")
public class DfsConfig {

    @Value("${dfs.namenode.url}")
    private String hdfsUri;

    @Bean
    public HdfsUtils hdfsUtils() throws IOException {
        return new HdfsUtils(hdfsUri);
    }

}

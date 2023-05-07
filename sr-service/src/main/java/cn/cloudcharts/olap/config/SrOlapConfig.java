package cn.cloudcharts.olap.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author wuque
 * @title: t1
 * @projectName xingyun
 * @description:
 * @date 2023/5/517:45
 */
@PropertySource("classpath:datasource_default.properties")
@MapperScan("cn.cloudcharts.olap.mapper")
public class SrOlapConfig {

}

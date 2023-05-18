package cn.cloudcharts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wuque
 * @title: OpenApiConfig
 * @projectName xingyun
 * @description: swagger3
 * @date 2023/5/1110:26
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI().info(new Info() //
                .title("星云 SpringDoc API") //
                .description("星云 SpringDoc Application") //
                .version("1.0.0"));
    }
}

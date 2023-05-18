package cn.cloudcharts.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wuque
 * @title: FastAutoGenerator
 * @projectName xingyun
 * @description: 快速生成
 * @date 2023/5/1717:49
 */
public class FastAutoGeneratorTest extends BaseGenerator {

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://192.168.216.154:31984/xingyun?useSSL=false&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&allowMultiQueries=true", "root", "1qaz2wsx");

    /**
     * 执行 run
     */
    public static void main(String[] args) {

        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig(
                        (scanner, builder) -> builder.author(scanner.apply("请输入作者名称？"))
                        .enableSwagger() // 开启 swagger 模式
                        .fileOverride()  // 覆盖已生成文件
//                        .outputDir("D://") // 指定输出目录  默认生成到tmp
                )
                // 包配置
                .packageConfig(
                        (scanner, builder) -> builder.parent(scanner.apply("请输入包名？"))
//                                .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://"))
                )
                // 策略配置
                .strategyConfig(
                        (scanner, builder) -> builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                        .addTablePrefix("xingyun_","t_") //设置过滤表前缀
                        .controllerBuilder().enableRestStyle().enableHyphenStyle()
                        .entityBuilder().enableLombok().build()
                )
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())  */
                   .templateEngine(new FreemarkerTemplateEngine())

                .execute();
    }

    /**
     * 处理 all 情况
     */
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }

}

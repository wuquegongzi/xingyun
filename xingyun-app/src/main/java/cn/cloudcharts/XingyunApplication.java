package cn.cloudcharts;

import cn.hutool.system.SystemUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class XingyunApplication {

    private static final Logger log = LoggerFactory.getLogger(XingyunApplication.class);

    public static void main(String[] args) {

        ConfigurableApplicationContext context =  SpringApplication.run(XingyunApplication.class, args);
        Environment environment = context.getBean(Environment.class);

        if(!"dev".equals(environment.getProperty("spring.profiles.active"))){
            System.setProperty("spring.devtools.restart.enabled", "false");
        }

        log.info("(♥◠‿◠)ﾉﾞ  启动成功   ლ(´ڡ`ლ)ﾞ ");
        log.info("JAVA:\n {}", SystemUtil.getJavaInfo());
        log.info("系统信息:\n {}",SystemUtil.getOsInfo());
        log.info("网络地址信息:\n {}",SystemUtil.getHostInfo());
        log.info("运行内存信息：\n {}",SystemUtil.getRuntimeInfo());
        log.info("访问链接：http://localhost:" + environment.getProperty("local.server.port"));
        log.info("            #                     \n" +
                "  ############             #      \n" +
                "  #         #       #########     \n" +
                "  ###########                     \n" +
                "  #         #                     \n" +
                "  ###########                 #   \n" +
                "       #         ###############  \n" +
                "  #    #    #          #          \n" +
                "  ############         #          \n" +
                "  #    #              #           \n" +
                " #     #   #          #           \n" +
                "# ###########        #            \n" +
                "       #             #     #      \n" +
                "       #     #      #       #     \n" +
                "###############    ###########    \n" +
                "                             #    \n");

    }

}

package cn.cloudcharts.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wuque
 * @title: HelloController
 * @projectName xingyun
 * @description:
 * @date 2023/5/417:45
 */
@RestController
public class HelloController {

    @RequestMapping("/")
    String home() {
        return "你好，星云!";
    }

}

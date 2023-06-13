package cn.cloudcharts.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wuque
 * @title: HelloController
 * @projectName xingyun
 * @description:
 * @date 2023/5/417:45
 */
@Tag(name = "Hi", description = "Hello XINGYUN")
@RestController
public class HelloController {

    @GetMapping("/")
    String home() {
        return "你好，星云!";
    }

}

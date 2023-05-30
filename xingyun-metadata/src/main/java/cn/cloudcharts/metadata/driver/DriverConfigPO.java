package cn.cloudcharts.metadata.driver;

import cn.cloudcharts.common.utils.AssertUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author wuque
 * @title: DriverConfig
 * @projectName xingyun
 * @description: 数据源配置对象
 * @date 2023/5/2219:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverConfigPO {

    private String name;
    private String type;
    private String ip;
    private Integer port;
    private String url;
    private String username;
    private String password;


    public DriverConfigPO(String name, String type, String url, String username, String password) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DriverConfigPO build(Map<String, String> confMap) {
        AssertUtil.isNull(confMap, "数据源配置不能为空");
        return new DriverConfigPO(
                confMap.get("name"),
                confMap.get("type"),
                confMap.get("url"),
                confMap.get("username"),
                confMap.get("password"));
    }
}

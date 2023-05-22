package cn.cloudcharts.starrocks.metadata.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}

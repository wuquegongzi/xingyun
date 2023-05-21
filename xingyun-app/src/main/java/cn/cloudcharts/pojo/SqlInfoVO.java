package cn.cloudcharts.pojo;

import lombok.Data;

@Data
public class SqlInfoVO {
    private String username;
    private String password;
    private String url;
    private String driverClassName;
    private String projectId;
}

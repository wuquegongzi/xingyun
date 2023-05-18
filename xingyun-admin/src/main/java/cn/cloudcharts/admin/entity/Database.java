package cn.cloudcharts.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * database management
 * </p>
 *
 * @author wuque
 * @since 2023-05-18
 */
@Getter
@Setter
@TableName("xingyun_database")
@ApiModel(value = "Database对象", description = "database management")
public class Database implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("tenant id")
    private Integer tenantId;

    @ApiModelProperty("database name")
    private String name;

    @ApiModelProperty("database belong group name")
    private String groupName;

    @ApiModelProperty("database type")
    private String type;

    @ApiModelProperty("database ip")
    private String ip;

    @ApiModelProperty("database port")
    private Integer port;

    @ApiModelProperty("database url")
    private String url;

    @ApiModelProperty("username")
    private String username;

    @ApiModelProperty("password")
    private String password;

    @ApiModelProperty("note")
    private String note;

    @ApiModelProperty("Flink configuration")
    private String flinkConfig;

    @ApiModelProperty("Flink template")
    private String flinkTemplate;

    @ApiModelProperty("version，such as: 11g of oracle ，2.2.3 of hbase")
    private String dbVersion;

    @ApiModelProperty("heartbeat status")
    private Boolean status;

    @ApiModelProperty("last heartbeat time of trigger")
    private LocalDateTime healthTime;

    @ApiModelProperty("last heartbeat time")
    private LocalDateTime heartbeatTime;

    @ApiModelProperty("is enable")
    private Boolean enabled;

    @ApiModelProperty("create time")
    private LocalDateTime createTime;

    @ApiModelProperty("update time")
    private LocalDateTime updateTime;

    @ApiModelProperty("db code")
    private String code;


}

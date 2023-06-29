package cn.cloudcharts.model.request;

import cn.cloudcharts.core.domain.BasePage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wuque
 * @title: XingyunDataBaseRequest
 * @projectName xingyun
 * @description:
 * @date 2023/5/1719:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBaseRequest extends BasePage implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * ID
         */
        private Integer id;

        /**
         * tenant id
         */
        private Integer tenantId;

        /**
         * database name
         */
        private String name;

        /**
         * database belong group name
         */
        private String groupName;

        /**
         * database type
         */
        private String type;

        /**
         * database ip
         */
        private String ip;

        /**
         * database port
         */
        private Integer port;

        /**
         * database url
         */
        private String url;

        /**
         * username
         */
        private String username;

        /**
         * password
         */
        private String password;

        /**
         * note
         */
        private String note;

        /**
         * version，such as: 11g of oracle ，2.2.3 of hbase
         */
        private String dbVersion;

        /**
         * heartbeat status
         */
        private Boolean status;

        /**
         * last heartbeat time of trigger
         */
        private String healthTime;

        /**
         * last heartbeat time
         */
        private String heartbeatTime;

        /**
         * is enable
         */
        private Boolean enabled;

        /**
         * create time
         */
        private String createTime;

        /**
         * update time
         */
        private String updateTime;


}

DROP TABLE IF EXISTS `xingyun_database`;
CREATE TABLE `xingyun_database`  (
                                     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                     `tenant_id` int(11) NOT NULL DEFAULT 1 COMMENT 'tenant id',
                                     `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'database name',
                                     `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'Default' COMMENT 'database belong group name',
                                     `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'database type',
                                     `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'database ip',
                                     `port` int(11) NULL DEFAULT NULL COMMENT 'database port',
                                     `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'database url',
                                     `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'username',
                                     `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'password',
                                     `note` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'note',
                                     `flink_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Flink configuration',
                                     `flink_template` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT 'Flink template',
                                     `db_version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'version，such as: 11g of oracle ，2.2.3 of hbase',
                                     `status` tinyint(1) NULL DEFAULT NULL COMMENT 'heartbeat status',
                                     `health_time` datetime(0) NULL DEFAULT NULL COMMENT 'last heartbeat time of trigger',
                                     `heartbeat_time` datetime(0) NULL DEFAULT NULL COMMENT 'last heartbeat time',
                                     `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT 'is enable',
                                     `create_time` datetime(0) NULL DEFAULT NULL COMMENT 'create time',
                                     `update_time` datetime(0) NULL DEFAULT NULL COMMENT 'update time',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE INDEX `database_un_idx1`(`name`, `tenant_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'database management' ROW_FORMAT = Dynamic;

-- xingyun.xingyun_job_base definition
CREATE TABLE `xingyun_job_base` (
                                    `job_id` bigint(20) NOT NULL AUTO_INCREMENT,
                                    `biz_id` varchar(100) DEFAULT NULL COMMENT '引擎返回的ID',
                                    `job_name` varchar(100) DEFAULT NULL COMMENT '作业名称',
                                    `job_type` int(11) DEFAULT NULL COMMENT '作业类型  0-数据导入  1-数据导出',
                                    `job_engine` varchar(100) DEFAULT NULL COMMENT '作业执行引擎',
                                    `business_type` varchar(500) DEFAULT NULL COMMENT '业务类型  eg: delta2starrocks[catalog.db.table->catalog.db.table]',
                                    `config_json` text COMMENT '配置',
                                    `submit _time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
                                    `exception_info` text COMMENT '异常信息',
                                    `realtime_flag` int(11) DEFAULT NULL COMMENT '是否是实时任务',
                                    `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                    `creator` varchar(100) DEFAULT NULL COMMENT '创建者',
                                    `modifier` varchar(100) DEFAULT NULL COMMENT '修改者',
                                    `res_group_id` int(11) DEFAULT NULL COMMENT '资源组',
                                    `res_config` text COMMENT '资源配置',
                                    `exec_result` text COMMENT '执行结果',
                                    `job_details` text COMMENT '任务的明细',
                                    `priority` int(11) DEFAULT '0' COMMENT '优先级',
                                    `exec_start_time` datetime DEFAULT NULL COMMENT '执行开始时间',
                                    `exec_end_time` datetime DEFAULT NULL COMMENT '执行结束时间',
                                    `del_flag` int(11) DEFAULT '0' COMMENT '删除标识',
                                    `guide_type` int(11) DEFAULT NULL COMMENT '0-脚本模式  1-引导模式',
                                    PRIMARY KEY (`job_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='作业基础信息';

-- xingyun.xingyun_job_config_insert definition
CREATE TABLE `xingyun_job_config_insert` (
                                             `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                             `job_id` bigint(20) NOT NULL COMMENT '任务基础表ID',
                                             `source_catalog` varchar(100) DEFAULT NULL,
                                             `source_db` varchar(100) DEFAULT NULL,
                                             `source_tbl` varchar(100) DEFAULT NULL,
                                             `source_fields` varchar(2000) DEFAULT NULL COMMENT '指定写入列，按照顺序，多个以,分割',
                                             `settings` text COMMENT '设置信息',
                                             `config_name` varchar(100) DEFAULT NULL COMMENT '配置的任务名',
                                             `overwrite_flag` int(11) DEFAULT NULL COMMENT '0-insert 1-overwrite',
                                             `label_name` varchar(200) DEFAULT NULL COMMENT '设置的label',
                                             `sink_catalog` varchar(100) DEFAULT NULL,
                                             `sink_db` varchar(100) DEFAULT NULL,
                                             `sink_tbl` varchar(100) DEFAULT NULL,
                                             `extra` text COMMENT '附加配置条件',
                                             `condition` text COMMENT '条件',
                                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='insert方式导入数据任务配置表';

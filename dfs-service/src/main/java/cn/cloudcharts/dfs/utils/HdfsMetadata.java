package cn.cloudcharts.dfs.utils;

import lombok.Data;

/**
 * @author wuque
 * @title: HdfsMetadata
 * @projectName xingyun
 * @description:
 * @date 2023/5/1514:44
 */
@Data
public class HdfsMetadata {
    private String path;
    private long modificationTime;
    private short blockReplication;
    private long blockSize;
    private String permission;
    private String owner;
    private String group;
}

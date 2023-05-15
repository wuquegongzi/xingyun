package cn.cloudcharts.dfs.impl;

import cn.cloudcharts.dfs.DfsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author wuque
 * @title: LocalServiceImpl
 * @projectName xingyun
 * @description: 本地共享磁盘  TODO
 * @date 2023/5/1519:06
 */
@ConditionalOnProperty(name = "dfs.type",havingValue = "local")
@Service("local")
public class LocalServiceImpl implements DfsService {

    @Override
    public boolean mkdirDir(String path) {
        return false;
    }

    @Override
    public boolean existFile(String path) {
        return false;
    }

    @Override
    public void createFile(String path, InputStream inputStream) {

    }

    @Override
    public void write(String filePath, String content, Charset charset) {

    }

    @Override
    public void append(String filePath, String content, Charset charset, boolean absentOrCreate) {

    }

    @Override
    public String read(String path) {
        return null;
    }

    @Override
    public byte[] readToBytes(String path) {
        return new byte[0];
    }

    @Override
    public List<String> listFile(String path) {
        return null;
    }

    @Override
    public boolean renameFile(String oldName, String newName) {
        return false;
    }

    @Override
    public boolean deleteFile(String path) {
        return false;
    }

    @Override
    public void uploadFile(String path, String uploadPath) {

    }

    @Override
    public void downloadFile(String path, String downloadPath) {

    }

    @Override
    public void copyFile(String sourcePath, String targetPath) {

    }
}

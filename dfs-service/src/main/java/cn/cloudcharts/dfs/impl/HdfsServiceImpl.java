package cn.cloudcharts.dfs.impl;

import cn.cloudcharts.dfs.DfsService;
import cn.cloudcharts.dfs.utils.HdfsUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.BlockLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author wuque
 * @title: HdfsServiceImpl
 * @projectName xingyun
 * @description:
 * @date 2023/5/1511:46
 */
@Slf4j
@ConditionalOnProperty(name = "dfs.type",havingValue = "hdfs")
@Service("hdfs")
public class HdfsServiceImpl implements DfsService {

    @Autowired
    private HdfsUtils hdfsUtils;


    @Override
    public boolean mkdirDir(String path) {
        try {
            hdfsUtils.createDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean existFile(String path) {
        try {
            return hdfsUtils.exists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFile(String path, InputStream inputStream) {
        try {
            hdfsUtils.createFile(path,inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建文件并写入内容
     * @param filePath
     * @param content
     * @param charset StandardCharsets.UTF_8
     * @throws IOException
     */
    public void write(String filePath, String content, Charset charset){

        if(null == charset){
            charset = StandardCharsets.UTF_8;
        }
        InputStream inputStream = new ByteArrayInputStream(content.getBytes(charset));
        try {
            hdfsUtils.createFile(filePath, inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void append(String filePath, String content, Charset charset, boolean absentOrCreate) {
        hdfsUtils.append(filePath,content,charset,absentOrCreate);
    }

    @Override
    public String read(String path) {

        try {
            return hdfsUtils.read(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public byte[] readToBytes(String path) {
        try {
            return hdfsUtils.readToBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> listFile(String path) {
        try {
            return hdfsUtils.listFiles(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean renameFile(String oldName, String newName) {
        return hdfsUtils.renameFile(oldName,newName);
    }

    @Override
    public boolean deleteFile(String path) {
        try {
            hdfsUtils.delete(path,true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public void uploadFile(String path, String uploadPath) {
        try {
            hdfsUtils.copyFromLocal(path,uploadPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void downloadFile(String path, String downloadPath) {
        try {
            hdfsUtils.copyToLocal(path,downloadPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void copyFile(String sourcePath, String targetPath) {
        hdfsUtils.copyFile(sourcePath,targetPath);
    }


    @Override
    public BlockLocation[] getFileBlockLocations(String path) {

        try {
            return hdfsUtils.getFileBlockLocations(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package cn.cloudcharts.dfs;

import org.apache.hadoop.fs.BlockLocation;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author wuque
 * @title: OssService
 * @projectName xingyun
 * @description: 存储统一接口
 * @date 2023/5/1115:36
 */
public interface DfsService {


    /**
     * 文件夹创建
     * @param path
     * @return
     */
    public boolean mkdirDir(String path);

    /**
     * 文件是否存在
     * @param path
     * @return
     */
    public boolean existFile(String path);


    /**
     * 创建文件
     * @param path
     * @param inputStream
     */
    public void createFile(String path, InputStream inputStream);

    /**
     *
     * 创建文件并写入内容
     * @param filePath
     * @param content
     * @param charset 默认UTF-8
     */
    public void write(String filePath, String content, Charset charset);

    /**
     * 对已有文件追加内容
     * @param filePath
     * @param content
     * @param charset
     * @param absentOrCreate  不存在默认抛出异常，或者 创建文件
     */
    public void append(String filePath, String content, Charset charset, boolean absentOrCreate);

    /**
     * 读取文件内容
     * @param path
     * @return
     */
    public String read(String path);

    /**
     * 读取指定文件 返回字节数组
     * @param path
     * @return
     */
    public byte[] readToBytes(String path);

    /**
     * HDFS 读完文件列表
     * @param path
     * @return
     */
    public List<String> listFile(String path);

    /**
     * 文件重命名
     * @param oldName
     * @param newName
     * @return
     */
    public boolean renameFile(String oldName, String newName);

    /**
     * 文件删除
     * @param path
     * @return
     */
    public boolean deleteFile(String path);

    /**
     * 文件上传
     * @param path
     * @param uploadPath
     */
    public void uploadFile(String path, String uploadPath);

    /**
     * 文件下载
     * @param path
     * @param downloadPath
     */
    public void downloadFile(String path, String downloadPath);

    /**
     * 文件复制
     * @param sourcePath
     * @param targetPath
     */
    public void copyFile(String sourcePath, String targetPath);


    /**
     * 获取指定文件 BlockLocation信息
     * @param path
     * @return
     * @throws Exception
     */
    public default BlockLocation[] getFileBlockLocations(String path){
        System.out.println("默认HDFS实现");
        return new BlockLocation[0];
    }

}

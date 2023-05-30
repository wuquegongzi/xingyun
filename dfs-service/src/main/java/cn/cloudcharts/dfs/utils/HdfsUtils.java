package cn.cloudcharts.dfs.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author wuque
 * @title: HdfsUtils
 * @projectName xingyun
 * @description:
 * @date 2023/5/1513:55
 */
@Slf4j
public class HdfsUtils {

    private static final Log logger = LogFactory.getLog(HdfsUtils.class);
    private static final int bufferSize = 1024 * 1024 * 64;
    private Configuration config = new Configuration();
    private FileSystem fs;

    public HdfsUtils(String hdfsUri) throws IOException {
        if(StringUtils.isEmpty(hdfsUri)){
            throw new ServerException("请检查fs.defaultFS配置");
        }
        config.set("fs.defaultFS", hdfsUri);
        fs = FileSystem.get(config);
    }

    public void copyFromLocal(String sourcePath, String destPath) throws IOException {
        fs.copyFromLocalFile(new Path(sourcePath), new Path(destPath));
        logger.info("Successfully copied file from local file system to HDFS");
    }

    public void copyToLocal(String sourcePath, String destPath) throws IOException {
        fs.copyToLocalFile(new Path(sourcePath), new Path(destPath));
        logger.info("Successfully copied file from HDFS to local file system");
    }

    public void createDirectory(String directoryPath) throws IOException {
        Path path = new Path(directoryPath);
        if (!fs.exists(path)) {
            fs.mkdirs(path);
        }
    }

    public void createFile(String filePath, InputStream inputStream) throws IOException {
        FSDataOutputStream outputStream = fs.create(new Path(filePath));
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream,outputStream);
        logger.info("Successfully created file on HDFS");
    }

    public void delete(String path, boolean recursive) throws IOException {
        Path hdfsPath = new Path(path);
        fs.delete(hdfsPath, recursive);
    }
    public List<String> listFiles(String directoryPath) throws IOException {
        Path path = new Path(directoryPath);
        RemoteIterator<LocatedFileStatus> filesIterator = fs.listFiles(path, true);
        List<String> filesList = new ArrayList<>();
        while (filesIterator.hasNext()) {
            String filePath = filesIterator.next().getPath().toString();
            filesList.add(filePath);
        }
        return filesList;
    }
    public boolean exists(String path) throws IOException {
        return fs.exists(new Path(path));
    }


    public BlockLocation[] getFileBlockLocations(String path) throws IOException {

        BlockLocation[] blocks = null;
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!exists(path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        try{
            FileStatus fileStatus = fs.getFileStatus(srcPath);
            blocks = fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
        }catch(Exception e){
            logger.error(e.getMessage());
        }
        return blocks;
    }

    public void copyFile(String sourcePath, String targetPath) {

        if (StringUtils.isEmpty(sourcePath) || StringUtils.isEmpty(targetPath)) {
            return;
        }
        // 原始文件路径
        Path oldPath = new Path(sourcePath);
        // 目标路径
        Path newPath = new Path(targetPath);

        FSDataInputStream inputStream = null;
        FSDataOutputStream outputStream = null;
        try {
            try{
                inputStream = fs.open(oldPath);
                outputStream = fs.create(newPath);

                IOUtils.copy(inputStream, outputStream, bufferSize);
            }catch(Exception e){
                logger.error(e.getMessage());
            }
        } finally {
            IOUtils.closeQuietly(inputStream,outputStream);
        }
    }

    public boolean renameFile(String oldName, String newName) {

        boolean target = false;
        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
            return false;
        }
        // 原文件目标路径
        Path oldPath = new Path(oldName);
        // 重命名目标路径
        Path newPath = new Path(newName);
        try{
            target = fs.rename(oldPath, newPath);
        }catch(Exception e){
            e.printStackTrace();
        }

        return target;
    }

    public String read(String path) throws IOException {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!exists(path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(srcPath);
            // 防止中文乱码
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = "";
            while ((lineTxt = reader.readLine()) != null) {
                sb.append(lineTxt);
            }
        }catch(Exception e){
            log.error(e.getMessage());
        } finally {
           IOUtils.closeQuietly(inputStream);
        }
        return sb.toString();
    }


    public byte[] readToBytes(String path) throws IOException {
        byte[] bytes= null;
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        if (!exists(path)) {
            return null;
        }
        // 目标路径
        Path srcPath = new Path(path);
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(srcPath);
            bytes = IOUtils.readFully(inputStream,64);
        }catch(Exception e){
            logger.error(e.getMessage());
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
        return bytes;
    }

    public void append(String filePath, String content, Charset charset, boolean absentOrCreate){

        if(null == charset){
            charset = StandardCharsets.UTF_8;
        }

        FSDataOutputStream out = null;
        Path f = new Path(filePath);

        try {
            if(!fs.exists(f)){
               if(absentOrCreate){
                   out = fs.create(f);
               }else{
                   throw new RuntimeException(filePath+ " is not exists");
               }
            }else {
                out = fs.append(f);
            }

            out.write(content.getBytes(charset));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
        }

    }
}

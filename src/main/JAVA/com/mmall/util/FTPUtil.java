package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author kenan
 * @description FTP文件上传工具类
 * @date 2018/9/25
 */
public class FTPUtil {
    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public static String getFtpIp() {
        return ftpIp;
    }

    public static void setFtpIp(String ftpIp) {
        FTPUtil.ftpIp = ftpIp;
    }

    public static String getFtpUser() {
        return ftpUser;
    }

    public static void setFtpUser(String ftpUser) {
        FTPUtil.ftpUser = ftpUser;
    }

    public static String getFtpPass() {
        return ftpPass;
    }

    public static void setFtpPass(String ftpPass) {
        FTPUtil.ftpPass = ftpPass;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    /**
    * 注意这个构造器里 ftpClient属性是不存在的
    * @author kenan
    * @date 2018/9/25
    * @param
    * @return
    */
    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    /**
     * 静态方法
     * @author kenan
     * @date 2018/9/25
     * @param
     * @return
     */
    public static boolean uploadFile(List<File> fileList){
        FTPUtil ftpUtil = new FTPUtil(ftpIp, 21, ftpUser, ftpPass);
        return false;
    }

    /**
    * remotePath:参数解释：remotePath是在ftp服务器文件夹以下更深的路径
    * @author kenan
    * @date 2018/9/25
    * @param
    * @return boolean
    */
    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        // 连接ftp 服务器
       if ( this.connectServer(this.ip, this.port, this.user, this.pwd)) {
           // 是否切换文件夹
           try {
               ftpClient.changeWorkingDirectory(remotePath);
               // 设置文件缓冲区
               ftpClient.setBufferSize(1024);
               ftpClient.setControlEncoding("UTF-8");
               // 文件类型  是一个二进制的文件类型。这样可以更通用。和防止乱码问题
               ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
               // 因为我们前段时间这只的linux vsftpd 是一个被动模式、且有端口范围。
               // 打开(本地的？？)被动模式
               ftpClient.enterLocalPassiveMode();

               // 遍历fileList
               for (File fileItem : fileList) {
                   // 将文件转换成流对象
                    fis = new FileInputStream(fileItem);
                    //  调用storeFile() 方法来存储文件
                    ftpClient.storeFile(fileItem.getName(), fis);
               }
           } catch (IOException e) {
               logger.error("上传文件异常", e);
               uploaded = false;
           } finally {
               // 关闭流
               fis.close();
               // 释放连接
               ftpClient.disconnect();
           }
       }
        return uploaded;
    }

    private boolean connectServer (String ip, int port, String user, String pwd){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            // 指明地址
            ftpClient.connect(ip);
            //登录
            isSuccess = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("ftp服务器连接异常：", e);
        }
        return isSuccess;
    }
}

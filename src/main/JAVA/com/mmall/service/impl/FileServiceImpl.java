package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.mmall.common.ServerResponse;
import com.mmall.service.IFileService;
import com.mmall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @author kenan
 * @description 文件上传的实现类
 * @date 2018/9/24
 */
@Service("iFileService")
public class FileServiceImpl implements IFileService {
    private static Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String upload(MultipartFile file, String path) {
        String fileName = file.getOriginalFilename();
        // 扩展名
        String fileExtendName = fileName.substring(fileName.indexOf(".")+1);
        // 保存到数据库的文件名
        String uploadFileName = UUID.randomUUID().toString().concat(".").concat(fileExtendName);
        // 记录一下日志 logBack 支持占位符
        logger.info("文件开始上传，文件名是:{}， 文件路径是:{}, 新文件名是：{}", fileName, path, fileExtendName);
        // 创建文件所在目录和创建文件
        File fileDir = new File(path);
        // 判断该tomcat下该目录是否存在
        if (!fileDir.exists()) {
            // 如果不存在，我就需要创建这个目录， 首先要设置权限为可写（针对 linux系统）
            fileDir.setWritable(true);
            // 创建文件夹
            /**
            * mkdirs()可以建立多级文件夹， mkdir()只会建立一级的文件夹， 如下：
            * new File("/tmp/one/two/three").mkdirs();
            * 执行后， 会建立tmp/one/two/three四级目录
            * new File("/tmp/one/two/three").mkdir();
            * 则不会建立任何目录， 因为找不到/tmp/one/two目录， 结果返回false
            * 本文来自 cnnumen 的CSDN 博客 ，全文地址请点击：https://blog.csdn.net/cnnumen/article/details/8463736?utm_source=copy
            * @author kenan
            * @date 2018/9/24
            * @param [file, path]
            * @return com.mmall.common.ServerResponse
            */
            fileDir.mkdirs();
        }
        // 创建目标文件
        File targetFile = new File(path, uploadFileName);
        try {
            // file.transferTo 是springMVC 封装的一个方法
            // 文件上传到tomcat
            file.transferTo(targetFile);// 该方法执行完以后targetFile变成了已经填充了文件数据的 文件对象
            // 文件上传到FTP服务器
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            // 已经上传到ftp 服务器上
            //上传到tomcat后还要将targetFile 上传到FTP 服务器上

            // TODO 上传完之后删除 upload文件夹下的文件
            // 删除tomcat下 该文件，文件夹可以不删除
            targetFile.delete();
        } catch (IOException e) {
            logger.error("文件上传发生异常", e);
            return null;
        }
        return targetFile.getName();
    }
}

package com.mmall.service;

import com.mmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author kenan
 * @description 文件上传接口类
 * @date 2018/9/24
 */
public interface IFileService {
    public String upload(MultipartFile file, String path);
}

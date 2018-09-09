package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author kenan
 * @description 用户接口实现类
 * @date 2018/9/8
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.createBySuccessMessage("用户名不存在");
        }

        // 密码登录MD5
        String MD5Pwd = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, MD5Pwd);
        if (user == null) {
            return ServerResponse.createBySuccessMessage("密码错误");
        }
        //将密码置空后再返回user对象，防止在网络中用户信息被截获
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccessMessage("登录成功",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        ServerResponse validServerResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validServerResponse.isSuccess()) {
            return validServerResponse;
        }

        validServerResponse =this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validServerResponse.isSuccess()) {
            return validServerResponse;
        }
        // 检验通过后默认给用户设置为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);

        // 将用户的明文密码进行MD5 加密
        String pwd = MD5Util.MD5EncodeUtf8(user.getPassword());
        user.setPassword(pwd);
        int resultCount =  userMapper.insert(user);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("注册成功！");
        } else {
            return ServerResponse.createByErrorMessage("注册失败");
        }
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isNotBlank(type)) {//isNotBlank 如果值是“   ” 也会返回 false
            // 开始校验
            if (Const.USERNAME.equals(type)) {
                int resultCount = this.userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("用户名已存在");
                }
            } else if (Const.EMAIL.equals(type)) {
                int resultCount = this.userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("邮箱已存在");
                }
            }
        } else {
            return ServerResponse.createBySuccessMessage("参数错误！");
        }
        return ServerResponse.createBySuccessMessage("校验成功！");
    }
}

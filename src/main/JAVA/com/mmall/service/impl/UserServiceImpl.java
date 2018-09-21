package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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
        return ServerResponse.createBySuccess("登录成功", user);
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
        //isNotBlank 如果值是“   ” 也会返回 false
        if (StringUtils.isNotBlank(type)) {
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

    @Override
    public ServerResponse selectQuestion(String username){

        ServerResponse validResponse = this.checkValid(username,Const.USERNAME);
        if(validResponse.isSuccess()){
            //用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    // 使用了缓存
    @Override
    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明问题及问题答案是这个用户的,并且是正确的,回答正确后生成Token. 这里生成token 是为了记录鉴权安全（有效期12小时）。
            // 生成Token,并将Token放入guava缓存
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (forgetToken == null || StringUtils.isBlank(forgetToken)) {
            ServerResponse.createByErrorMessage("token无效！");
        }
        // 我们继续校验一下username.
        ServerResponse validServerResponse = this.checkValid(username, Const.USERNAME);
        if (validServerResponse.isSuccess()) {
            // 这里的isSuccess 表示用户不存在
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        // 根据用户名和“token_”作为key从guava缓存中获取token
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);

        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("Token 无效或者已过期");
        }
        if (StringUtils.equals(token, forgetToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userMapper.updatePwdByUsername(username, md5Password);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("密码修改成功！");
            } else {
                // 如果传来的Token 和后端Cache 里的值不一样，返回错误提示
                return ServerResponse.createByErrorMessage("Token错误，请重新获取重置密码的token");
            }
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }
    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        // 防止纵向越权，一定要校验一下这个用户的旧密码，一定要指定是这个用户，因为我们会查询一个count(1) 出来,而且要指定id count 出来的的值也一定是 要 大于0 的
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (resultCount == 0) {
            return ServerResponse.createByErrorMessage("旧密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.createBySuccessMessage("密码更新成功!");
        }
        return ServerResponse.createByErrorMessage("密码更新失败！");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        // username 不能被更新
        // email 也要加一个校验，因为如要更新的email 是被别人占用的email 那么就要告诉用户该邮箱别人占用，你无权更新。
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0 ) {
            // 说明这个邮箱已经被占用
            return ServerResponse.createByErrorMessage("邮箱已被占用，请换一个邮箱进行更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setQuestion(user.getQuestion());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.createBySuccess("更新成功！", updateUser);
        }
        return  ServerResponse.createByErrorMessage("更新个人信息失败！");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user==null) {
            return ServerResponse.createByErrorMessage("找不到该用户!");
        }
        //返回user时将密码置空，
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.createBySuccess();
        }
            return ServerResponse.createByError();
    }


    public static void main(String[] args) {
        System.out.printf(MD5Util.MD5EncodeUtf8("123456"));
    }


}

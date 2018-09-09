package com.mmall.controller;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
* 用户控制器
* @author kenan
* @date 2018/9/8
* @param
* @return
*/
@Controller
@RequestMapping(value = "/user")
public class UserController {
    // iUserService 要和 IUserService 类上面的 @Service("iUserService") 名称对应就会注入进来？？？
    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "/login.do", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){
        ServerResponse response = iUserService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return  response;
    }

    /**
    * 退出登录
    * @author kenan
    * @date 2018/9/9
    * @param
    * @return
    */
    @RequestMapping(value = "/logout.do", method = {RequestMethod.GET})
    @ResponseBody
    public ServerResponse<String> logout (HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
    * 注册接口
    * @author kenan
    * @date 2018/9/9
    * @param
    * @return
    */
    @RequestMapping(value = "/register.do", method = {RequestMethod.GET})
    @ResponseBody
    public ServerResponse<String> register (User user) {
        return iUserService.register(user);
    }

    /**
    * 注册时的表单校验
    * @author kenan
    * @date 2018/9/9
    * @param str：表单 某项的值  type：用户名，邮箱
    * @return
    */
    public ServerResponse<String> checkValid (String str, String type) {
        return iUserService.checkValid(str, type);
    }
}

package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mysql.fabric.Server;
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
    @RequestMapping(value = "/logout.do", method = {RequestMethod.POST})
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
    @RequestMapping(value = "/register.do", method = {RequestMethod.POST})
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
    @RequestMapping(value = "/checkValid.do", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse<String> checkValid (String str, String type) {
        return iUserService.checkValid(str, type);
    }

    /**
    * 获取当前用户信息
    * @author kenan
    * @date 2018/9/10
    * @param
    * @return
    */
    @RequestMapping(value = "/get_user_info.do", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if ( user != null ) {
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前用户信息");
    }

    /**
    * 忘记密码
    * @author kenan
    * @date 2018/9/10
    * @param
    * @return
    */
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }

    /**
    * 检查密保答案接口
    * 使用本地缓存存放检验通过后生成的token，
    * 这里的token生成后,返回给前端之后，前端要带着这个token来调用下一个接口（忘记密码中的重置密码接口）
    * @author kenan
    * @date 2018/9/10
    * @param username, question, answer
    * @return com.mmall.common.ServerResponse<java.lang.String>
    */
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question,String answer){
        return iUserService.checkAnswer(username,question,answer);
    }

    /**
    * 忘记密码时的重置密码
    * @author kenan
    * @date 2018/9/11
    * @param username：用户名, passwordNew 新密码, forgetToken： token
    * @return com.mmall.common.ServerResponse<java.lang.String>
    */
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
    * 用户登录情况下的重置密码
    * @author kenan
    * @date 2018/9/14
    * @param
    * @return
    */
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword (HttpSession session, String passwordOld, String passworldNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录！");
        }
        return iUserService.resetPassword(passwordOld, passworldNew, user);
    }
    /**修改用户信息
     * 我们需要将新的用户信息 返回给前端，同时更新session里的用户信息
    * @author kenan
    * @date 2018/9/14
    * @param
    * @return
    */
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null ) {
             return ServerResponse.createByErrorMessage("用户未登录！");
        }
        // 因为我们前端传过来 的user 是 没有userid 的，所以我们要手动将userid 赋值成当前登录用户的id
        // 从session的当前用户里获取比从页面上直接传过来要安全。防止越权问题
        // 另外 用户名也是不能被更新的
        user.setUsername(user.getUsername());
        user.setId(currentUser.getId());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
    *获取用户信息
    * @author kenan
    * @date 2018/9/16
    * @param session
    * @return com.mmall.common.ServerResponse<com.mmall.pojo.User>
    */
    @ResponseBody
    @RequestMapping(value = "/get_information.do")
    public ServerResponse<User> getInformation(HttpSession session) {
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            ServerResponse.createByErrorMessage("还未登录！");
        }
        return iUserService.getInformation(currentUser.getId());
    }
}

package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author kenan
 * @description 分类管理模块Controller
 * @date 2018/9/18
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;
    /**
    * 添加分类，如前端，没有传递parentid 我们给parentId 赋默认值
     *  0 是 分类的根节点
    * @author kenan
    * @date 2018/9/18
    * @param session, categoryName, parentId
    * @return com.mmall.common.ServerResponse<java.lang.String>
    */
    @RequestMapping("/addCategory.do")
    @ResponseBody
    public ServerResponse<String> addCategory(HttpSession session, String categoryName,@RequestParam(value = "parentId", defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null) {
            ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录！请登录");
        }

        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByErrorMessage("非管理员，无权添加分类！");
        }
    }

    /**
    * 设置分类名称
    * @author kenan
    * @date 2018/9/18
    * @param session, categoryId, categoryName
    * @return com.mmall.common.ServerResponse
    */
    @RequestMapping("set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(HttpSession session,Integer categoryId,String categoryName){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            //更新categoryName
            return iCategoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作,需要管理员权限");
        }
    }

    /**
    * 获取子节点，但不递归
    * @author kenan
    * @date 2018/9/18
    * @param session, categoryId
    * @return com.mmall.common.ServerResponse
    */
    @RequestMapping("get_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCtegory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null) {
            ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录！请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无法查看分类！");
        }
    }

    /**
    * 使用递归算法获取无限层级的产品分类树
    * @author kenan
    * @date 2018/9/19
    * @param session, categoryId
    * @return com.mmall.common.ServerResponse
    */
    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user==null) {
            ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录！请登录");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            // 是管理员
            // 查询当前节点的 id 和递归子节点的id
            // 0-->10--100-->1000
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.createByErrorMessage("无法查看分类！");
        }
    }
}

package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author kenan
 * @description 订单controller，包含支付的示例代码
 * 创建订单 和 支付订单
 * @date 2018/10/28
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService iOrderService;


    /**
    * 支付方法
    * @author kenan
    * @date 2018/10/30
    * @param session, orderNo, request
    * @return com.mmall.common.ServerResponse
    */
    @RequestMapping("/pay.do")
    @ResponseBody
    public ServerResponse pay (HttpSession session, Long orderNo,  HttpServletRequest request) {
        // request  我们会把传过来的参数变成要扫描的二维码，然后把二维码存到服务器端，并将图片地址返回给前端，前端展示图片
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }

        String path = request.getSession().getServletContext().getRealPath("upload");
        return iOrderService.pay(orderNo,user.getId(),path);
    }

    // 支付宝的回调函数
}

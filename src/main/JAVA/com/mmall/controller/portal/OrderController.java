package com.mmall.controller.portal;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;

/**
 * @author kenan
 * @description 订单controller，包含支付的示例代码
 * 创建订单 和 支付订单
 * @date 2018/10/28
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
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
    public Object alipayCallback(HttpServletRequest request) {
        Map<String, String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values =   (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i<values.length; i++) {
                valueStr = (i == values.length - 1)? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        logger.info("支付宝回调，sign:{},trade_status:{},参数：{}",params.get("sign"),params.get("trade_status"), params.toString());
        // 非常重要。验证回调的正确性，是不是支付宝发的，并且还要回避重复通知

    }
}

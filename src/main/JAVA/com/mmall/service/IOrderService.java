package com.mmall.service;

import com.mmall.common.ServerResponse;

import java.util.Map;

/**
 * @author kenan
 * @description 订单接口
 * @date 2018/10/28
 */
public interface IOrderService {
     ServerResponse pay (Long orderNo, Integer userId, String path);

     ServerResponse aliCallback(Map<String, String> params);

     ServerResponse queryOrderPayStatus (Integer userId, long orderNo)
}

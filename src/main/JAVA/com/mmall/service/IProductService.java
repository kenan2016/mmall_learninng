package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;

/**
 * Created by kenan
 */
public interface IProductService {
    /**
    * 功能描述
    * @author kenan
    * @date 2018/9/21
    * @param
    * @return com.mmall.common.ServerResponse
    */
    ServerResponse saveOrUpdateProduct(Product product);
    /**
    * 功能描述
    * @author kenan
    * @date 2018/9/21
    * @param
    * @return com.mmall.common.ServerResponse<java.lang.String>
    */
    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> gtList(Integer pageNum, Integer pageSize);
}

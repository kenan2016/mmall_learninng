package com.mmall.service;

import com.mmall.common.ServerResponse;

/**
 * @author kenan
 * @description 商品分类业务接口层
 * @date 2018/9/18
 */
public interface ICategoryService {

    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
}

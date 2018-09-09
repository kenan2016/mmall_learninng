package com.mmall.common;

/**
 * @author kenan
 * @description 常量类
 * @date 2018/9/9
 */
public class Const {
    public static final String CURRENT_USER = "currentUser";

    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    // 使用这种写法实现，类似于枚举的作用和 简易数据分组的作用
    public interface Role {
        int ROLE_CUSTOMER = 0; // 普通用户
        int ROLE_ADMIN = 1; // 管理员
    }
}

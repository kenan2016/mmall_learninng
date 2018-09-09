package com.mmall.common;

/**
 * @author kenan
 * @description 请求响应的status数字对应字典信息 这是一个响应编码 的枚举类
 * @date 2018/9/8
 */
public enum ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROR(1, "ERROR"),
    NEED_LOGIN(10, "NEED_LOGIN"),
    ILLEGAL_ARGUMENT(2,"ILLEGAl_ARGUMENT");

    private final int code;
    private final String desc;

    /**
    * 枚举类的构造器只能是私有的
    * @author kenan
    * @date 2018/9/8
    * @param
    * @return
    **/
    private ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    //把 desc 和 code 开放出去
    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}

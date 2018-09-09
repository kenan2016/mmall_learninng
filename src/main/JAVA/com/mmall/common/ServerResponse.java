package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * @author kenan
 * @description 项目中通用的数据响应封装类
 * @date 2018/9/8
 * json串忽略对象中null属性key value 不出现
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable{
    private int status;
    private String msg;
    private T data;

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public int getStatus() {
        return status;
    }

    /**
    * 私有化构造器，这 也就意味着，我们的这个构造方法只能内部使用。
     * 这样我 们要提供一个共有的构造方法来供外部使用
    * @author kenan
    * @date 2018/9/8
    * @param
    * @return
    */
    private ServerResponse(int status) {
        this.status = status;
    }
    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    public static void main(String[] args) {
        ServerResponse sr1 = new ServerResponse(1,new Object());
        ServerResponse sr2 = new ServerResponse(1,"abc");
        System.out.printf("2333");
//        System.out.printf("console");
    }
    /**
    *  判断是不是请求成功。
    * @author kenan
    * @date 2018/9/8
    * @param
    * @return boolean
     * JsonIgnore 有这个注解的字段不会被序列化到json串中
    */
    @JsonIgnore
    public boolean isSuccess(){
        //这段代码的意思是当返回的是0 我们返回 true; 如果返回不是0 则返回false
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public static <T> ServerResponse<T> createBySuccess() {
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> ServerResponse<T> createBySuccessMessage(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg, T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(), msg,data);
    }

    public static <T> ServerResponse<T> createByError () {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage) {
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode, String errorMessage) {
        return new ServerResponse<T>(errorCode, errorMessage);
    }
}

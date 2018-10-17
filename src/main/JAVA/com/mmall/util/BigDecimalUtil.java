package com.mmall.util;

import java.math.BigDecimal;

/**
 * @author kenan
 * @description BigDecimal工具类
 * @date 2018/10/16
 */
public class BigDecimalUtil {
    // 让这个类不能再外部访问：即，将构造器私有化
    private BigDecimalUtil () {

    }

    //加法
    public static BigDecimal add (double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    //减法
    public static BigDecimal sub (double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    //乘法，保留两位小数，进行四舍五入
    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2);// 四舍五入，保留两位小数
    }

    /**
    * 除法
     * 对于除不尽的情况，要保留指定位数的小数
    * @author kenan
    * @date 2018/10/16
    * @param
    * @return
    */
    public static BigDecimal div (double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);
    }

}


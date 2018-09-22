package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @author kenan
 * @description 利用jodaTime类库封装属于我们自己的时间工具类
 * jodaTime
 * String --》 datetime
 * datetime--》String
 * @date 2018/9/22
 */
public class DateTimeUtil {
    // 默认的时间格式字符串
    public static final String STAND_FORMATE = "yyyy-MM-dd HH:mm:ss";
    /**
    * str--> date
    * @author kenan
    * @date 2018/9/22
    * @param
    * @return
    */
    public static Date strToDate(String dateTimeStr, String formatStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date, String formatStr) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        // 这里要传一个模式字符串
        return dateTime.toString(formatStr);
    }

    public static void main(String[] args) {
        System.out.printf(DateTimeUtil.dateToStr(new Date(),"yyyy-MM-dd HH:mm:ss"));
        System.out.print(DateTimeUtil.strToDate("2018-09-22 10:22:11", "yyyy-MM-dd HH:mm:ss"));
    }

    /**
    * 加两个带有默认格式的时间格式化重载方法
    * @author kenan
    * @date 2018/9/22
    * @param
    * @return
    */
    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STAND_FORMATE);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date) {
        if (date == null) {
            return StringUtils.EMPTY;
        }
        DateTime dateTime = new DateTime(date);
        // 这里要传一个模式字符串
        return dateTime.toString(STAND_FORMATE);
    }


}

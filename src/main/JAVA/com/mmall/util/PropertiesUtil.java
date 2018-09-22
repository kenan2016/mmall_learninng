package com.mmall.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author kenan
 * @description 读取配置文件的工具类
 * @date 2018/9/22
 */
public class PropertiesUtil {
    // 日志
    private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    // 申明静态变量
    public static Properties props;

    // 我们要在tomcat 启动的时候就要读取到配置文件。所以我们要在这里使用静态代码块。

    // java 知识点：静态代码块执行顺序优于普通代码块优于构造代码块
    /**
     * 静态代码块 static {}  // 只执行一次：讲台代码块会在类被（JVM）加载将在时被执行，且只会被执行一次
     * 常被用来做初始化静态变量
     *
     * 普通代码块{ xx }
     *
     * 构造代码块：每次构造对象时都会被调用：PropertisUtil () {}
     *
     */
    /**
     * 当这个类被jvm 的 classLoader 加载时 会先执行 static 代码块
     * 我们可以申明依一些静态变量在静态代码块中
     * @author kenan
     * @date 2018/9/22
     * @param
     * @return
     */
    static {
        String fileName = "mmall.properties";
        // 以流的形式读取配置文件
        // 实例化 properties 对象
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (IOException e) {
            logger.error("读取mmall.propertis文件发生异常", e );
        }
    }

    public static String getProperty (String key) {
        String value = props.getProperty(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    /**
    * 加一个带有默认值的重载方法
    * @author kenan
    * @date 2018/9/22
    * @param
    * @return
    */
    public static String getProperty(String key, String defaultValue) {
        String value = props.getProperty(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value.trim();
    }
}

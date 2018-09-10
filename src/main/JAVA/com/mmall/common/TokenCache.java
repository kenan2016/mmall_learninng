package com.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * guava本地缓存
 * guava缓存工具类
 * https://www.jianshu.com/p/b3c10fcdbf0f
 * @author kenan
 * @description 本地缓存
 * @date 2018/9/10
 */
public class TokenCache {
    private static final Logger logger = LoggerFactory.getLogger(TokenCache.class);
    public static final String TOKEN_PREFIX = "token_";


    // LRU 算法Least recently used，最近最少使用
    //算法讲解http://flychao88.iteye.com/blog/1977653
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000)
            // 12 小时缓存过期（Token 失效）
            .maximumSize(1000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";//结合下面的代码可发现这里这么写避免了 null.xx 异常！
                }
            });


    public static void setKey(String key,String value) {
        localCache.put(key, value);
    }

    /**
    * 根据key获取缓存中的值
    * @author kenan
    * @date 2018/9/10
    * @param [key]
    * @return java.lang.String
    */
    public static String getKey(String key){
        String value = null;
        try {
            value = localCache.get(key);
            if ("null".equals(value)) {
                return null;
            }
        } catch (Exception e) {
            logger.error("localCache get error", e);
        }
        return null;
    }
}

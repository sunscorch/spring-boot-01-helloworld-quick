package com.newIntel.adapter.utils;



import java.util.*;

/**
 * 字符串工具类
 * 
 * @author jeethink  官方网址：www.jeethink.vip
 */
public class StringUtils
{
    /**
     * * 判断一个对象是否为空
     *
     * @param object Object
     * @return true：为空 false：非空
     */
    public static boolean isNull(Object object)
    {
        return object == null;
    }

    /**
     * * 判断一个对象是否非空
     *
     * @param object Object
     * @return true：非空 false：空
     */
    public static boolean isNotNull(Object object)
    {
        return !isNull(object);
    }
}
package com.qdk.util;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by lenovo on 2017/6/18.
 */
public final class ReflectionUtil {
    private static final Logger LOGGER = Logger.getLogger(ReflectionUtil.class);

    /**
     * 将每一个Class的到一个Object对象
     * @param cls
     * @return
     */
    public static Object newInstance(Class<?> cls){
        Object instance;
        try {
            instance=cls.newInstance();//创建动态代理
        }catch (Exception e){
            LOGGER.error("new instance failure",e);
            throw new RuntimeException(e);
        }
        return instance;
    }

    public static Object invokeMethod(Object obj, Method method,Object... args){
        Object result;
        try{
            method.setAccessible(true);
            result=method.invoke(obj,args);
        }catch (Exception e){
            LOGGER.error("invoke method failure",e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 设置成员变量
     * @param obj
     * @param field
     * @param value
     */
    public static void setField(Object obj, Field field,Object value){
        try {
            field.setAccessible(true);
            field.set(obj,value);
        }catch (Exception e){
            LOGGER.error("set field failure",e);
            throw new RuntimeException(e);
        }
    }

}

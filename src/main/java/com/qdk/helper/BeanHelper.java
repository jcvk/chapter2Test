package com.qdk.helper;

import com.qdk.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lenovo on 2017/6/18.
 */
public final class BeanHelper {
    private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<Class<?>, Object>();

    static {
        //得到Service和controller文件
        Set<Class<?>> beanClassSet=ClassHelper.getBeamClassSet();
        //循环每一个文件，为每一个文件创建一个动态代理并让如BEAN_AMP中
        for(Class<?> beanClass:beanClassSet){
            Object obj= ReflectionUtil.newInstance(beanClass);
            BEAN_MAP.put(beanClass,obj);
        }
    }

    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class："+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
}

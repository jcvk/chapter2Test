package com.qdk.helper;

import com.qdk.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lenovo on 2017/6/18.
 */
public final class BeanHelper {
    //一个Class对应一个Object
    private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<Class<?>, Object>();

    static {
        //得到Service和controller文件,即所有的类，让如到Class类型的beanClassSet集合中
        Set<Class<?>> beanClassSet=ClassHelper.getBeamClassSet();
        //循环每一个文件，为每一个文件创建一个动态代理并让如BEAN_AMP中
        for(Class<?> beanClass:beanClassSet){
            Object obj= ReflectionUtil.newInstance(beanClass);
            //将得到的Object和对应的Class放入到一个图中。
            BEAN_MAP.put(beanClass,obj);
        }
    }

    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    /**
     * cls:类名
     * 返回输入类名的对象
     */
    public static <T> T getBean(Class<T> cls){
        if(!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class："+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }
}

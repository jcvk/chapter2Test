package com.qdk.helper;

import com.qdk.annotation.Inject;
import com.qdk.util.CollectionUtil;
import com.qdk.util.ReflectionUtil;
import com.sun.deploy.util.ArrayUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by lenovo on 2017/6/19.
 */
public final class IocHelper {

    //只需要在locHelper的静态快中实现相关逻辑，就能完成IoC容器的初始化
    //工作。
    static {
        //这里的Map<Class<?>,Object>表示的是一个类，和一个类对象之间的一个
        //映射关系，此时IoC框架中所管理的对象都是单例的。
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
        if(CollectionUtil.isNotEmpty(beanMap)){
            //遍历BeanMap
            //beanMap.entrySet产生一个Map.Entry对象集
            for(Map.Entry<Class<?>,Object> beanEntry:beanMap.entrySet()){
                //获取BeanMap中的Bean类和Bean实例
                Class<?> beanClass=beanEntry.getKey();
                Object beanInstance=beanEntry.getValue();
                //获取Bean类定义的所有成员变量
                Field[] beanFields=beanClass.getDeclaredFields();
                if(ArrayUtils.isNotEmpty(beanFields)){
                    //遍历beanFields
                    for(Field beanField:beanFields){
                        //判断当前BeanField是否带有Inject注解,循环成员变量
                        //然后判断成员变量是否有Inject注解
                        if(beanField.isAnnotationPresent(Inject.class)){
                            //如果有注解，得到这个成员变量的类，然后通过
                            //beanMap得到这个类的对象
                            Class<?> beanFieldClass=beanField.getType();
                            Object beanFieldInstance=beanMap.get(beanFieldClass);
                            if(beanFieldInstance!=null){
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }
                        }
                    }
                }


            }
        }
    }
}

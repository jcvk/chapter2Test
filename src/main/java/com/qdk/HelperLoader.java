package com.qdk;

import com.qdk.helper.BeanHelper;
import com.qdk.helper.ClassHelper;
import com.qdk.helper.ControllerHelper;
import com.qdk.helper.IocHelper;
import com.qdk.util.ClassUtil;

/**
 * Created by lenovo on 2017/6/19.
 */
public final class HelperLoader {

    public static void init(){
        Class<?>[] classList={
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls:classList){
            ClassUtil.loadClass(cls.getName(),false);
        }
    }
}

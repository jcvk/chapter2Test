package com.qdk.helper;

import com.qdk.annotation.Action;
import com.qdk.bean.Handler;
import com.qdk.bean.Request;
import com.qdk.util.CollectionUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lenovo on 2017/6/19.
 */
public final class ControllerHelper {

    /**
     * 用于存放请求与处理器的映射关系,在ControllerHelper中封装了一个
     * Action Map，通过它来存放Request与Handler，然后通过ClassHelper来获取
     * 带有Controller注解的类，遍历Controller类，从Action注解中提取URL，
     * 最后初始化Request与Handler之间的关系
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        //获取所有的Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            for (Class<?> controllerClass : controllerClassSet) {
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtils.isNotEmpty(methods)) {
                    //遍历这些Controller类中的方法
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(Action.class)) {
                            //从Action注解中获取URL映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            //正则表达式判断mapping的格式
                            if (mapping.matches("\\w+:/\\w*")) {
                                //将mapping通过":"分开放入array数组中
                                String[] array = mapping.split(":");
                                if (ArrayUtils.isNotEmpty(array) && array.length == 2) {
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    //创建request变量，并初始化
                                    Request request = new Request(requestMethod, requestPath);
                                    //类和方法
                                    Handler handler = new Handler(controllerClass, method);
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}

package com.qdk.bean;

import java.lang.reflect.Method;

/**
 * Created by lenovo on 2017/6/19.
 */
public class Handler {

    /**
     * Controller 类
     */
    private Class<?> controllerClass;

    private Method actionMethod;

    public Handler(Class<?> controllerClass,Method actionMethod){
        this.controllerClass=controllerClass;
        this.actionMethod=actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}

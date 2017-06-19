package com.qdk.bean;

/**
 * Created by lenovo on 2017/6/19.
 */
public class Data {
    /**
     * 数据模型
     */
    private Object model;

    public Data(Object model) {
        this.model = model;
    }

    /**
     * 返回的Data类型数据封装了一个Object类型的数据模型，
     * 框架会将该对象写入HttpServletResponse对象中。
     * @return
     */
    public Object getModel() {
        return model;
    }
}

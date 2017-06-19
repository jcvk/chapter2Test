package com.qdk.bean;

import com.qdk.util.CastUtil;

import java.util.Map;

/**
 * Created by lenovo on 2017/6/19.
 */
public class Param {

    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    public Map<String,Object> getParamMap(){
        return paramMap;
    }
}

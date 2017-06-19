package com.qdk.helper;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

/**
 * Created by lenovo on 2017/6/19.
 */
public class JsonUtil {
    private static final Logger LOGGER=Logger.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER=new ObjectMapper();

    public static <T> String toJson(T obj){
        String json;
        try{
            json=OBJECT_MAPPER.writeValueAsString(obj);
        }catch (Exception e){
            LOGGER.error("convert POJO to JSON failure",e);
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T fromJsom(String json,Class<T> type){
        T pojo;
        try{
            pojo=OBJECT_MAPPER.readValue(json,type);
        }catch (Exception e){
            LOGGER.error("convert JSON to POJO failure",e);
            throw new RuntimeException(e);
        }
        return pojo;
    }
}

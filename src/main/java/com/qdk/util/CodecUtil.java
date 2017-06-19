package com.qdk.util;


import org.apache.log4j.Logger;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by lenovo on 2017/6/19.
 */
public final class CodecUtil {
    private static final Logger LOGGER=Logger.getLogger(CodecUtil.class);


    public static String encodeURL(String source){
        String target;
        try {
            target= URLEncoder.encode(source,"UTF-8");
        }catch (Exception e){
            LOGGER.error("encode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }

    public static String decodeURL(String source){
        String target;
        try {
            target= URLDecoder.decode(source,"UTF-8");
        }catch (Exception e){
            LOGGER.error("decode url failure",e);
            throw new RuntimeException(e);
        }
        return target;
    }
}

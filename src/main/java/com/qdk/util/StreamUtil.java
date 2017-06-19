package com.qdk.util;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lenovo on 2017/6/19.
 */
public final class StreamUtil {
    private static final Logger LOGGER=Logger.getLogger(StreamUtil.class);

    public static String getString(InputStream is){
        StringBuilder sb=new StringBuilder();
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line=reader.readLine())!=null){
                sb.append(line);
            }
        }catch (Exception e){
            LOGGER.error("get string failure",e);
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}

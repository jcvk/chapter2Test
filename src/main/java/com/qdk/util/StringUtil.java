package com.qdk.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lenovo on 2017/6/13.
 */
public class StringUtil {
    /**
     * str.trim()
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(str!=null){
            str=str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}

package com.qdk.util;

import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by lenovo on 2017/6/13.
 */
public final class CollectionUtil {
    public static boolean isEmpty(Collection<?> collection){
        return CollectionUtil.isEmpty(collection);
    }

    public static boolean isNotEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?,?> map){
        return MapUtils.isEmpty(map);
    }

    public static boolean isNotEmpty(Map<?,?> map){
        return !isEmpty(map);
    }
}

package com.qdk.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by lenovo on 2017/6/13.
 */
public final class CastUtil {
    /**
     * 转为String型
     * @param obj
     * @return
     */
    public static String castString(Object obj){
        return CastUtil.castString(obj,"");
    }

    public static String castString(Object obj,String defaultValue){
        return obj!=null?String.valueOf(obj):defaultValue;
    }

    public static double castDouble(Object obj){
        return CastUtil.castDouble(obj,0);
    }

    public static double castDouble(Object obj,double defaultValue){
        double doubleValue=defaultValue;
        if(obj!=null){
            String strValue=castString(obj);
            if(StringUtils.isEmpty(strValue)){
                try{
                    doubleValue=Double.parseDouble(strValue);

                }catch (NumberFormatException e){
                    doubleValue=defaultValue;
                }
            }
        }
        return doubleValue;
    }

    public static double castLong(Object obj){
        return CastUtil.castLong(obj,0);
    }

    public static double castLong(Object obj,long defaultValue){
        long longValue=defaultValue;
        if(obj!=null){
            String strValue=castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    longValue=Long.parseLong(strValue);

                }catch (NumberFormatException e){
                    longValue=defaultValue;
                }
            }
        }
        return longValue;
    }

    public static int castInt(Object obj){
        return CastUtil.castInt(obj,0);
    }

    public static int castInt(Object obj,int defaultValue){
        int intValue=defaultValue;
        if(obj!=null){
            String strValue=castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    intValue=Integer.parseInt(strValue);

                }catch (NumberFormatException e){
                    intValue=defaultValue;
                }
            }
        }
        return intValue;
    }

    public static Boolean castBoolean(Object obj){
        return CastUtil.castBoolean(obj,false);
    }

    public static Boolean castBoolean(Object obj,Boolean defaultValue){
        Boolean BooleanValue=defaultValue;
        if(obj!=null){
            String strValue=castString(obj);
            if(StringUtils.isNotEmpty(strValue)){
                try{
                    BooleanValue=Boolean.parseBoolean(strValue);

                }catch (NumberFormatException e){
                    BooleanValue=defaultValue;
                }
            }
        }
        return BooleanValue;
    }
}

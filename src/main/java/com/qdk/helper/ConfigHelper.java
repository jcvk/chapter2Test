package com.qdk.helper;

import com.qdk.util.PropsUtil;

import java.util.Properties;

/**
 * Created by lenovo on 2017/6/18.
 * CONFIG_PROPS的到的就是smart.properties,然后传入键值，即使smart.properties文件中等号前面的部分
 */
public final class ConfigHelper {
    private static final Properties CONFIG_PROPS= PropsUtil.loadProps
            (ConfigConstant.CONFIG_FILE);

    /**
     * 获取JDBC驱动
     * @return
     */
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL);
    }

    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);
    }

    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);
    }

    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);
    }
}

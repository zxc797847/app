package com.avic.demo.sebeiglzx.utils.app;

import com.avic.demo.sebeiglzx.utils.properties.PropertiesUtils;

public class AppHelper {
    /**
     * 获取APP名称
     */
    public static String getAppName() {
        String appName = "";
        PropertiesUtils pro = new PropertiesUtils("/assets/appconfig.properties");
        appName = pro.getConfig("appName", "");
        return appName;
    }
}

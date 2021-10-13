package com.avic.demo.sebeiglzx.utils.properties;

import android.util.Log;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    /*	*//**
     * 得到netconfig.properties配置文件中的所有配置属性
     *
     * @return Properties对象
     */

    String filepath = ""; // assets目录下的文件如： assets/payconfig.txt
    Properties prop = null;

    /** 创建AssetProperty */
    public PropertiesUtils(String filepath) {
        this.filepath = filepath;
        if (prop == null)
            prop = getAssetsProperty(filepath);
    }

    /** 读取AssetProperty中的配置信息 */
    public String getConfig(String name, String defval) {
        if (prop == null)
            return defval;
        else
            return prop.getProperty(name, defval);
    }

    /** 读取Assest文件夹下资源，返回Properties */
    public static Properties getAssetsProperty(String filepath) {
        try {
            Properties prop = new Properties();
            InputStream reader = PropertiesUtils.class.getResourceAsStream(filepath);// "/assets/netconfig.properties"
            prop.load(reader);
            reader.close();
            return prop;
        } catch (Exception e) {
            Log.e("AssetProperty", e.toString());
        }
        return null;
    }
}

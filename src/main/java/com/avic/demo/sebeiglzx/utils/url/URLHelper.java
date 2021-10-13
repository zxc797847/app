package com.avic.demo.sebeiglzx.utils.url;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class URLHelper {

    /**
     * 获取URL路径
     */
    public static String getBaseUrl(Context context) {
        SharedPreferences sp;
        String url = "http://";
        sp = context.getSharedPreferences("networkConfigInfo", Context.MODE_PRIVATE);
        String ip = sp.getString("NETWORK_CONFIG_IP", "");
        String port = sp.getString("NETWORK_CONFIG_PORT", "");
        String proName = sp.getString("NETWORK_CONFIG_PRONAME", "");
        url += ip;


        if (!TextUtils.isEmpty(port)) {
            url += ":" + port;
        }

        if (!TextUtils.isEmpty(proName)) {
            url += "/" + proName;
        }

        return url;
    }
}

package com.avic.demo.sebeiglzx.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;



public class GetHttp {
    private static final String CHARSET = "utf-8";
    private static final int TIME_OUT = 10 * 1000;//超时时间
    private static final String TAG = "uploadFile";

    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";


    public static String RequstGetHttp(String strUrl) throws IOException {
        URL url = null;
        String result = "";
        url = new URL(strUrl);
        HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
        // 设置请求方法为 GET 请求
        urlconn.setRequestMethod("GET");
        urlconn.setRequestProperty("Content-type", "text/html");
        urlconn.setRequestProperty("Accept-Charset", "utf-8");
        urlconn.setRequestProperty("contentType", "utf-8");
        urlconn.setDoInput(true);
        // GET 方式，不需要使用输出流
        urlconn.setDoOutput(false);
        urlconn.setConnectTimeout(10000);
        urlconn.setReadTimeout(10000);

        urlconn.connect();// 链接服务器并发送消息

        // 开始接收返回的数据
        InputStreamReader is = new InputStreamReader(urlconn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(is);
        String readLine = "";
        while ((readLine = bufferedReader.readLine()) != null) {
            result += readLine;
        }

        is.close();
        urlconn.disconnect();

        return result;
        /*URL url = null;
        String result = "";
        url = new URL(strUrl);
        HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
        // 设置请求方法为 GET 请求
        urlconn.setRequestMethod("GET");
        urlconn.setRequestProperty("Content-type", "text/html");
        urlconn.setRequestProperty("Accept-Charset", "utf-8");
        urlconn.setRequestProperty("contentType", "utf-8");
        urlconn.setDoInput(true);
        // GET 方式，不需要使用输出流
        urlconn.setDoOutput(false);
        urlconn.setConnectTimeout(5000);
        urlconn.setReadTimeout(5000);

        urlconn.connect();// 链接服务器并发送消息

        // 开始接收返回的数据
        InputStreamReader is = new InputStreamReader(urlconn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(is);
        String readLine = "";
        while ((readLine = bufferedReader.readLine()) != null) {
            result += readLine;
        }
        is.close();
        urlconn.disconnect();
        return result;*/
    }
}

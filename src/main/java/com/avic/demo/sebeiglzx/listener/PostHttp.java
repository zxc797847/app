package com.avic.demo.sebeiglzx.listener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.avic.demo.sebeiglzx.utils.tools.LogToFile;
import com.avic.demo.sebeiglzx.utils.url.ExceptionUtil;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
public class PostHttp {

    public static String HttpRequest(String request, String RequestMethod, String output) throws IOException {
        try {
            output = output.replace("+", "%2B");// 解决+号传过去为空格的问题
        } catch (Exception e) {
        }

        StringBuffer buffer = new StringBuffer();
        HttpURLConnection connection = null;
        OutputStream out = null;
        InputStream input = null;
        String result = "";
        try {
            // 建立连接
            URL url = new URL(request);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod(RequestMethod);
            connection.setConnectTimeout(5000);  //设置连接超时
            connection.setReadTimeout(5000);     //设置读取超时
            if (output != null) {
                out = connection.getOutputStream();
                out.write(output.getBytes("UTF-8"));
                out.flush();
                out.close();
            }
            // 流处理
            input = connection.getInputStream();
            InputStreamReader inputReader = new InputStreamReader(input, "UTF-8");
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            connection.disconnect();
            String bufStr = buffer.toString();
            result = bufStr;
        } catch (MalformedURLException e) {
            System.out.println("debug:" + e);
        } catch (IOException e) {

        }
        return result;
    }

    public static String RequstPostHttp(String strUrl, Map<String, String> params) {
        URL url = null;
        String result = "";
        try {
            url = new URL(strUrl);
            HttpURLConnection urlconn = (HttpURLConnection) url.openConnection();
            urlconn.setDoInput(true);// 设置输入流采用字节流模式
            urlconn.setDoOutput(true);
            urlconn.setRequestMethod("POST");
            urlconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlconn.setRequestProperty("Charset", "UTF-8");
            urlconn.setConnectTimeout(650000);
            urlconn.setReadTimeout(650000);
            urlconn.setChunkedStreamingMode(0);

            urlconn.connect();// 链接服务器并发送消息

            byte[] data = getRequestData(params, "utf-8").toString().getBytes();// 获得请求体
            // 设置请求体的长度
            urlconn.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream outStream = urlconn.getOutputStream();
            // 获得输出流，向服务器写入数据
            outStream.write(data);
            outStream.flush();// 发送，清除缓存
            outStream.close();// 关闭

            // 开始接收返回的数据
            InputStream is = urlconn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String readLine = "";
            while ((readLine = bufferedReader.readLine()) != null) {
                result += readLine;
            }

            bufferedReader.close();
            urlconn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String RequstPostHttpNew(Context context, String url, Map<String, String> parameters) {
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        StringBuffer sb = new StringBuffer();// 处理请求参数
        String params = "";// 编码之后的参数
        try {
            // 编码请求参数
            if (parameters.size() == 1) {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), "UTF-8"));
                }
                params = sb.toString();
            } else {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=").append(java.net.URLEncoder.encode(parameters.get(name), "UTF-8")).append("&");
                }
                String temp_params = sb.toString();
                params = temp_params.substring(0, temp_params.length() - 1);
            }
            // 创建URL对象
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL.openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static Object getRequestData(Map<String, String> params, String encode) {
        StringBuffer buffer = new StringBuffer();
        if (params != null && !params.isEmpty()) {
            // 迭代器
            for (Map.Entry<String, String> entry : params.entrySet()) {
                try {
                    buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        // System.out.println(buffer.toString());
        // 删除最后一个字符&，多了一个;主体设置完毕
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.deleteCharAt(buffer.length() - 1);
    }

    public static String RequstPostHttp(Context context, String strUrl, List<Map<String, Object>> params) {
        String output = "params=" + getRequestData(params, "utf-8");
        try {
            output = output.replace("+", "%2B");// 解决+号传过去为空格的问题
        } catch (Exception e) {
        }

        StringBuffer buffer = new StringBuffer();
        HttpURLConnection connection = null;
        OutputStream out = null;
        InputStream input = null;
        String result = "";
        try {
            // 建立连接
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            if (output != null) {
                out = connection.getOutputStream();
                out.write(output.getBytes("UTF-8"));
                out.flush();
                out.close();
            }
            // 流处理
            input = connection.getInputStream();
            InputStreamReader inputReader = new InputStreamReader(input, "UTF-8");
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            connection.disconnect();
            String bufStr = buffer.toString();
            result = bufStr;
        } catch (MalformedURLException e) {
            Log.e("上传数据失败1", ExceptionUtil.getExceptionMessage(e));
            //Toast.makeText(context, "上传数据失败1" + ExceptionUtil.getExceptionMessage(e), 20000).show();
        } catch (IOException e) {
            Log.e("上传数据失败2", ExceptionUtil.getExceptionMessage(e));
            //Toast.makeText(context, "上传数据失败2" + ExceptionUtil.getExceptionMessage(e), 20000).show();

        }
        return result;
    }

    private static String getRequestData(List<Map<String, Object>> params, String encode) {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < params.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            Map<String, Object> map = params.get(i);
            // 迭代器
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    jsonObject.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    private static String getRequestDatal(Map<String, Object>params, String encode) {

        JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();
            Map<String, Object> map = params;
            // 迭代器
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                try {
                    jsonObject.put(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            jsonArray.put(jsonObject);
        return jsonArray.toString();
    }

    public static String RequstPostHttpl(Context context, String strUrl, Map<String, Object>params) {
        String output = "params=" + getRequestDatal(params, "utf-8");
        try {
            output = output.replace("+", "%2B");// 解决+号传过去为空格的问题
        } catch (Exception e) {
        }

        StringBuffer buffer = new StringBuffer();
        HttpURLConnection connection = null;
        OutputStream out = null;
        InputStream input = null;
        String result = "";
        try {
            // 建立连接
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            if (output != null) {
                out = connection.getOutputStream();
                out.write(output.getBytes("UTF-8"));
                out.flush();
                out.close();
            }
            // 流处理
            input = connection.getInputStream();
            InputStreamReader inputReader = new InputStreamReader(input, "UTF-8");
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            if (connection.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
            }
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            connection.disconnect();
            String bufStr = buffer.toString();
            result = bufStr;
        } catch (MalformedURLException e) {
            Log.e("上传数据失败1", ExceptionUtil.getExceptionMessage(e));
            //Toast.makeText(context, "上传数据失败1" + ExceptionUtil.getExceptionMessage(e), 20000).show();
        } catch (IOException e) {
            Log.e("上传数据失败2", ExceptionUtil.getExceptionMessage(e));
            //Toast.makeText(context, "上传数据失败2" + ExceptionUtil.getExceptionMessage(e), 20000).show();

        }
        return result;
    }
}

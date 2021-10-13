package com.avic.demo.sebeiglzx.utils.function.SebeTzxzActivity;

import android.content.Context;
import android.util.Log;

import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新增界面，自动补全查询设置
 */
public class SebeAdd {

    //查询设备编码
    public static ArrayList searchCpbmAdd(Context context,String fieldl,String valuel){
        ArrayList<String> list=new ArrayList<>();
        String url = URLHelper.getBaseUrl(context) + "/appController.do?getCpbmbByField&field="+fieldl+"&value="+valuel ;
        String dataResult = "";
        try {
            dataResult = GetHttp.RequstGetHttp(url);
        } catch (IOException e) {
            Log.e("error:", e.getMessage());
        }
        try {
            JSONObject jsonObj = new JSONObject(dataResult);
            Boolean success = jsonObj.getBoolean("success");
            final String msg = jsonObj.getString("msg");
            if (success) {
                JSONObject attributes = jsonObj.getJSONObject("attributes");
                JSONArray dataArr = attributes.getJSONArray("data");
                if (dataArr == null||dataArr.length()<=0) {
                    Log.w("WARMING:", "没有数据！");
                } else {
                    for (int j = 0; j < dataArr.length(); j++) {
                        JSONObject data = (JSONObject) dataArr.get(j);
                        String cpbm = data.isNull("cpbm") ? "" : data.getString("cpbm");
                        list.add(cpbm);
                    }
                }
            } else {
                Log.d("error",msg);
            }
        } catch (JSONException e) {
            Log.e("error:", e.getMessage());
        }
        return  list;
    }

    //使用部门
    public static ArrayList searchSybmAdd(Context context,String str){
        ArrayList<String> list=new ArrayList<>();
        String url = URLHelper.getBaseUrl(context) + "/appController.do?getDepart&departName="+str;
        String dataResult = "";
        try {
            dataResult = GetHttp.RequstGetHttp(url);
        } catch (IOException e) {
            Log.e("error:", e.getMessage());
        }
        try {
            JSONObject jsonObj = new JSONObject(dataResult);
            Boolean success = jsonObj.getBoolean("success");
            final String msg = jsonObj.getString("msg");
            if (success) {
                JSONObject attributes = jsonObj.getJSONObject("attributes");
                JSONArray dataArr = attributes.getJSONArray("data");
                if (dataArr == null||dataArr.length()<=0) {
                    Log.w("WARMING:", "没有数据！");
                } else {
                    for (int j = 0; j < dataArr.length(); j++) {
                        JSONObject data = (JSONObject) dataArr.get(j);
                        String departname = data.isNull("departname") ? "" : data.getString("departname");
                        list.add(departname);
                    }
                }
            } else {
                Log.d("error",msg);
            }
        } catch (JSONException e) {
            Log.e("error:", e.getMessage());
        }
        return  list;
    }

    //负责人/经办人
    public static ArrayList searchSbfzrAdd(Context context,String str){
        ArrayList<String> list=new ArrayList<>();
        String url = URLHelper.getBaseUrl(context) + "/appController.do?getUser&userName="+str;
        String dataResult = "";
        try {
            dataResult = GetHttp.RequstGetHttp(url);
        } catch (IOException e) {
            Log.e("error:", e.getMessage());
        }
        try {
            JSONObject jsonObj = new JSONObject(dataResult);
            Boolean success = jsonObj.getBoolean("success");
            final String msg = jsonObj.getString("msg");
            if (success) {
                JSONObject attributes = jsonObj.getJSONObject("attributes");
                JSONArray dataArr = attributes.getJSONArray("data");
                if (dataArr == null||dataArr.length()<=0) {
                    Log.w("WARMING:", "没有数据！");
                } else {
                    for (int j = 0; j < dataArr.length(); j++) {
                        JSONObject data = (JSONObject) dataArr.get(j);
                        String realname = data.isNull("realname") ? "" : data.getString("realname");
                        list.add(realname);
                    }
                }
            } else {
                Log.d("error",msg);
            }
        } catch (JSONException e) {
            Log.e("error:", e.getMessage());
        }
        return  list;
    }
    //负责人/经办人
    public static ArrayList searchSbjbrAdd(Context context,String str){
        ArrayList<String> list=new ArrayList<>();
        String url = URLHelper.getBaseUrl(context) + "/appController.do?getUser&userName="+str;
        String dataResult = "";
        try {
            dataResult = GetHttp.RequstGetHttp(url);
        } catch (IOException e) {
            Log.e("error:", e.getMessage());
        }
        try {
            JSONObject jsonObj = new JSONObject(dataResult);
            Boolean success = jsonObj.getBoolean("success");
            final String msg = jsonObj.getString("msg");
            if (success) {
                JSONObject attributes = jsonObj.getJSONObject("attributes");
                JSONArray dataArr = attributes.getJSONArray("data");
                if (dataArr == null||dataArr.length()<=0) {
                    Log.w("WARMING:", "没有数据！");
                } else {
                    for (int j = 0; j < dataArr.length(); j++) {
                        JSONObject data = (JSONObject) dataArr.get(j);
                        String realname = data.isNull("realname") ? "" : data.getString("realname");
                        list.add(realname);
                    }
                }
            } else {
                Log.d("error",msg);
            }
        } catch (JSONException e) {
            Log.e("error:", e.getMessage());
        }
        return  list;
    }

    //查询设备编码two
    public static List<Map<String, Object>> searchCpbmAddl(Context context, String fieldl, String valuel){
        List<Map<String,Object>> listMap=new ArrayList<>();
        Map<String, Object> mapAdd= new HashMap<String, Object>();
        String url = URLHelper.getBaseUrl(context) + "/appController.do?getCpbmbByField&field="+fieldl+"&value="+valuel ;
        String dataResult = "";
        try {
            dataResult = GetHttp.RequstGetHttp(url);
        } catch (IOException e) {
            Log.e("error:", e.getMessage());
        }
        try {
            JSONObject jsonObj = new JSONObject(dataResult);
            Boolean success = jsonObj.getBoolean("success");
            final String msg = jsonObj.getString("msg");
            if (success) {
                JSONObject attributes = jsonObj.getJSONObject("attributes");
                JSONArray dataArr = attributes.getJSONArray("data");
                if (dataArr == null) {
                    Log.w("WARMING:", "没有数据！");
                } else {
                    for (int j = 0; j < dataArr.length(); j++) {
                        JSONObject data = (JSONObject) dataArr.get(j);
                        Integer id=data.getInt("id");
                        String cpbm = data.isNull("cpbm") ? "" : data.getString("cpbm");
                         String cpgg = data.isNull("cpgg") ? "" : data.getString("cpgg");
                        String cpmc = data.isNull("cpmc") ? "" : data.getString("cpmc");
                        String cpxh = data.isNull("cpxh") ? "" : data.getString("cpxh");

                        mapAdd.put("id",id);
                        mapAdd.put("cpbm",cpbm);
                        mapAdd.put("cpgg",cpgg);
                        mapAdd.put("cpmc",cpmc);
                        mapAdd.put("cpxh",cpxh);
                        listMap.add(mapAdd);
                    }
                }
            } else {
                Log.d("error",msg);
            }
        } catch (JSONException e) {
            Log.e("error:", e.getMessage());
        }
        return  listMap;
    }

    //查询生产厂家
    public static ArrayList searchZdsccj(Context context,String str){
        ArrayList<String> list=new ArrayList<>();
        String url = URLHelper.getBaseUrl(context) + "/appController.do?getSccj&gysMc=" + str;
        String dataResult = "";
        try {
            dataResult = GetHttp.RequstGetHttp(url);
        } catch (IOException e) {
            Log.e("error:", e.getMessage());
        }
        try {
            JSONObject jsonObj = new JSONObject(dataResult);
            Boolean success = jsonObj.getBoolean("success");
            final String msg = jsonObj.getString("msg");
            if (success) {
                JSONObject attributes = jsonObj.getJSONObject("attributes");
                JSONArray dataArr = attributes.getJSONArray("data");
                if (dataArr == null||dataArr.length()<=0) {
                    Log.w("WARMING:", "没有数据！");
                } else {
                    for (int j = 0; j < dataArr.length(); j++) {
                        JSONObject data = (JSONObject) dataArr.get(j);
                        final String sccj = data.isNull("gys_mc") ? "" : data.getString("gys_mc");
                        list.add(sccj);
                    }
                }
            } else {
                Log.d("error",msg);
            }
        } catch (JSONException e) {
            Log.e("error:", e.getMessage());
        }
        return  list;
    }
}

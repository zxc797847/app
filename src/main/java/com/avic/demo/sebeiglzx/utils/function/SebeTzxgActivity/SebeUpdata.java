package com.avic.demo.sebeiglzx.utils.function.SebeTzxgActivity;

import android.content.Context;
import android.util.Log;

import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SebeUpdata {


    public static ArrayList searchSybmll(Context context,String str){
        ArrayList<String> list=new ArrayList<>();
        String url = URLHelper.getBaseUrl(context) + "/appController.do?getDepart&departName="+str;
        String dataResult = "";
        try {
            dataResult = GetHttp.RequstGetHttp(url);
        } catch (IOException e) {
            e.printStackTrace();
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

    //查询负债人
public static ArrayList searchSbfzr(Context context,String str){
    ArrayList<String> list=new ArrayList<>();
    String url = URLHelper.getBaseUrl(context) + "/appController.do?getUser&userName="+str;
    String dataResult = "";
    try {
        dataResult = GetHttp.RequstGetHttp(url);
    } catch (IOException e) {
        e.printStackTrace();
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

}

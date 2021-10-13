package com.avic.demo.sebeiglzx.utils.function.SebeTzxxBdActivity;

import android.content.Context;
import android.text.TextUtils;
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

public class SebeBinding {

    //查询设备编号
  public static ArrayList searchCpbm(Context context,String field,String str){
        ArrayList<String> list=new ArrayList<>();
      String url = URLHelper.getBaseUrl(context) + "/appController.do?getSbtzbByField&field="+field+"&value="+str;
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
                      Integer id = (Integer) data.get("id");
                      String rfid_tid= data.isNull("rfid_tid") ? "" : data.getString("rfid_tid");
                      String rfid_epc=data.isNull("rfid_epc") ? "" : data.getString("rfid_epc");
                      if(TextUtils.isEmpty(rfid_tid)&&TextUtils.isEmpty(rfid_epc)){
                          String sbbh = data.isNull("sbbh") ? "" : data.getString("sbbh");
                          list.add(sbbh);
                      }
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

    //查询编号方法二
    public static List<Map<String, Object>> searchCpbml(Context context, String field, String str){
        List<Map<String,Object>> listMap=new ArrayList<>();
        Map<String, Object> mapBd= new HashMap<String, Object>();
        String url = URLHelper.getBaseUrl(context) + "/appController.do?getSbtzbByField&field="+field+"&value="+str;
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
                        Integer id = (Integer) data.get("id");
                        String cpbm = data.isNull("cpbm") ? "" : data.getString("cpbm");
                        String sbbh = data.isNull("sbbh") ? "" : data.getString("sbbh");
                         String ccbh = data.isNull("ccbh") ? "" : data.getString("ccbh");
                         String cpgg = data.isNull("cpgg") ? "" : data.getString("cpgg");
                         String cpmc = data.isNull("cpmc") ? "" : data.getString("cpmc");
                        String cpxh = data.isNull("cpxh") ? "" : data.getString("cpxh");
                         String cpfl_mc = data.isNull("cpfl_mc") ? "" : data.getString("cpfl_mc");
                         String sccj = data.isNull("sccj") ? "" : data.getString("sccj");
                         String sbzt = data.isNull("sbzt") ? "" : data.getString("sbzt");
                         String jldw = data.isNull("jldw") ? "" : data.getString("jldw");
                         String sybm = data.isNull("sybm") ? "" : data.getString("sybm");
                         String sbdj = data.isNull("sbdj") ? "0" : data.getString("sbdj");
                         String sl = data.isNull("sl") ? "0" : data.getString("sl");
                         String szwz = data.isNull("szwz") ? "" : data.getString("szwz");
                         String sbtp = data.isNull("sbtp") ? "" : data.getString("sbtp");
                         String sbzs =data.isNull("sbzs") ? "" : data.getString("sbzs");
                         String jbr=data.isNull("jbr") ? "" : data.getString("jbr");
                        String sbfzr = data.isNull("sbfzr") ? "" : data.getString("sbfzr");
                         String bz=data.isNull("bz") ? "" : data.getString("bz");

                        mapBd.put("id",id);
                        mapBd.put("cpbm",cpbm);
                        mapBd.put("sbbh",sbbh);
                        mapBd.put("ccbh",ccbh);
                        mapBd.put("cpgg",cpgg);
                        mapBd.put("cpmc",cpmc);
                        mapBd.put("cpxh",cpxh);
                        mapBd.put("cpfl_mc",cpfl_mc);
                        mapBd.put("sccj",sccj);
                        mapBd.put("sbzt",sbzt);
                        mapBd.put("jldw",jldw);
                        mapBd.put("sybm",sybm);
                        mapBd.put("sbdj",sbdj);
                        mapBd.put("sl",sl);
                        mapBd.put("szwz",szwz);
                        mapBd.put("sbtp",sbtp);
                        mapBd.put("sbzs",sbzs);
                        mapBd.put("jbr",jbr);
                        mapBd.put("sbfzr",sbfzr);
                        mapBd.put("bz",bz);

                        listMap.add(mapBd);
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

}

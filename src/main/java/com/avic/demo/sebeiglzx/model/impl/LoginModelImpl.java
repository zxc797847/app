package com.avic.demo.sebeiglzx.model.impl;

import android.content.Context;
import com.avic.demo.sebeiglzx.listener.OnLoginListener;
import com.avic.demo.sebeiglzx.model.ILoginModel;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;
import com.avic.demo.sebeiglzx.http.GetHttp;

import org.json.JSONObject;

public class LoginModelImpl implements ILoginModel {

    @Override
    public void login(final Context context, final String username, final String password, final OnLoginListener onLoginListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = URLHelper.getBaseUrl(context) + "/appController.do?aPPLogin&userName=" + username + "&passWord=" + password + "";
                    String result = GetHttp.RequstGetHttp(url);
                    JSONObject jsonObj = new JSONObject(result);
                    Boolean isSuccess = (Boolean) jsonObj.get("success");
                    if (isSuccess) {
                        //onLoginListener.onSuccess();// 登录成功
                        JSONObject attributes = jsonObj.getJSONObject("attributes");
                        String realName = attributes.getString("realname");
                        String emp_no = attributes.getString("emp_no");
                        onLoginListener.onSuccess(realName,emp_no);// 登录成功
                    } else {
                        onLoginListener.onFailure();// 登录失败
                    }
                } catch (Exception e) {
                    System.out.println("错误：" + e.getMessage());
                    onLoginListener.onFailure();// 登录失败
                }
            }
        }).start();
    }
}

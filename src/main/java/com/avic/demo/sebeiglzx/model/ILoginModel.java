package com.avic.demo.sebeiglzx.model;

import android.content.Context;
import com.avic.demo.sebeiglzx.listener.OnLoginListener;
public interface ILoginModel {

    /**
     * 登录
     * @Param username
     * password
     * onLoginListener
     *
     */
    void login(final Context context, String username, String password, OnLoginListener onLoginListener);
}

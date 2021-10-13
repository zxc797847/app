package com.avic.demo.sebeiglzx.listener;

public interface OnLoginListener {

    //登录成功
    public void onSuccess(String realname,String emp_no);
    //登录失败
    public void onFailure();

}

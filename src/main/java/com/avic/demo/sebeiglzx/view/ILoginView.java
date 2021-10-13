package com.avic.demo.sebeiglzx.view;

public interface ILoginView {
    public String getUserName();
    public String getPassword();
    public void onSuccess(String realname,String emp_no);
    public void onFailure(String msg);
}

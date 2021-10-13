package com.avic.demo.sebeiglzx.view;

import com.avic.demo.sebeiglzx.bean.ProRfidFkbBean;

public interface IProRfidMgrSearchView {

    public String getBqhValue();
    public void onSuccess(ProRfidFkbBean bean);
    public void onFailure(String msg);
}

package com.avic.demo.sebeiglzx.listener;

import java.util.Map;
import com.avic.demo.sebeiglzx.bean.ProRfidFkbBean;
public interface OnSuccessListener {

    /**
     * 执行成功
     *
     * @param msg
     */
    public void onSuccess(String msg);

    /**
     * 执行失败
     *
     * @param msg
     */
    public void onFailure(String msg);

}

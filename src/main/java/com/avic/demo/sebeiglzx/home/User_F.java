package com.avic.demo.sebeiglzx.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.activity.LoginActivity;
import com.avic.demo.sebeiglzx.bean.CkBean;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;

import java.util.ArrayList;
import java.util.List;

/*
* 个人中心
* */
public class User_F extends Fragment {

    private SharedPreferences sp;
    private TextView tv_username;
    private TextView tv_realname;
    private TextView tv_emp_no;
    private TextView tv_departname;
    private TextView tv_companyname;
    private TextView tv_user_logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.user_f, null);
        initView(view);
        return view;
    }

    private void initView(final View v) {
        sp = v.getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        tv_username = (TextView) v.findViewById(R.id.tv_username);
        tv_realname = (TextView) v.findViewById(R.id.tv_realname);
        tv_emp_no = (TextView) v.findViewById(R.id.tv_emp_no);
        tv_departname = (TextView) v.findViewById(R.id.tv_departname);
        tv_companyname = (TextView) v.findViewById(R.id.tv_companyname);
        tv_user_logout = (TextView) v.findViewById(R.id.tv_user_logout);
        // 退出
        tv_user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sp.edit().remove("USER_NAME").commit();
                sp.edit().remove("PASSWORD").commit();
                Intent intent = new Intent();
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        final String username = sp.getString("USER_NAME", "");
        if (TextUtils.isEmpty(username)) {
           /* Intent intent = new Intent();
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);*/
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<CkBean> ckBeanList = new ArrayList<CkBean>();
                    String url = URLHelper.getBaseUrl(v.getContext()) + "/appController.do?getUserByAccount&account=" + username;
                    String result = "";
                    try {
                        result = GetHttp.RequstGetHttp(url);
                    } catch (Exception e1) {
                        // 切换主线程更新ui
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(v.getContext(), "网络异常！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    if (result == null) {
                        // 切换主线程更新ui
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(v.getContext(), "查询无结果！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        try {
                            JSONObject jsonObj = new JSONObject(result);
                            boolean success = (Boolean) jsonObj.get("success");
                            final String msg = (String) jsonObj.get("msg");
                            if (!success) {
                                // 切换主线程更新ui
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(v.getContext(), "获取用户信息失败，原因：“" + msg + "”！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                JSONObject attributes = jsonObj.getJSONObject("attributes");
                                JSONObject data = attributes.getJSONObject("data");
                                final String realname = (String) data.get("realname");
                                final String emp_no = (String) data.get("emp_no");
                                final String departname = (String) data.get("departname");
                                final String companyname = (String) data.get("companyname");
                                // 切换主线程更新ui
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_username.setText(username);
                                        tv_realname.setText(realname);
                                        tv_emp_no.setText(emp_no);
                                        tv_departname.setText(departname);
                                        tv_companyname.setText(companyname);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            // 切换主线程更新ui
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(v.getContext(), "获取数据格式处在问题！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

            }).start();

        }

    }


}

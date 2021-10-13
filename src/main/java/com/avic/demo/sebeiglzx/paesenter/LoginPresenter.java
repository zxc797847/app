package com.avic.demo.sebeiglzx.paesenter;

import android.content.Context;

import com.avic.demo.sebeiglzx.listener.OnLoginListener;
import com.avic.demo.sebeiglzx.model.ILoginModel;
import com.avic.demo.sebeiglzx.model.impl.LoginModelImpl;
import com.avic.demo.sebeiglzx.view.ILoginView;

public class LoginPresenter implements OnLoginListener {

    private ILoginView loginView;//登录view
    private ILoginModel loginModel;//登录接口
    //实例化登录接口和登录view
    public LoginPresenter(ILoginView loginView){
        this.loginView = loginView;
        loginModel = new LoginModelImpl();
    }

    /**
     * 将从view层中获取的用户名和密码传送给Model层 然后让activity中的登录按钮调用此方法
     */
    public void login(Context context) {
        String username = loginView.getUserName();
        String password = loginView.getPassword();
        loginModel.login(context,username, password, this);
    }


    @Override
    public void onSuccess(String realname,String emp_no) {
        loginView.onSuccess(realname,emp_no);
    }

    @Override
    public void onFailure() {

    }


}

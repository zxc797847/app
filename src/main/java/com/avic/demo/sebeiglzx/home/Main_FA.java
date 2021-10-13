package com.avic.demo.sebeiglzx.home;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.utils.redcarutil.ReadRfidCardUtil;
import com.avic.demo.sebeiglzx.utils.tools.IBtnCallListener;

/**
 *
 * 整个程序最底层的框架Activity，所有的Fragment都是依赖于此Activity而存在的
 *
 */

public class Main_FA extends FragmentActivity implements View.OnClickListener,IBtnCallListener {

    // 界面底部的菜单按钮
    private ImageView[] bt_menu = new ImageView[3];
    // 界面底部的菜单按钮id
    private int[] bt_menu_id = { R.id.iv_menu_0, R.id.iv_menu_2,R.id.iv_menu_3 };
    // 界面底部的选中菜单按钮资源
    private int[] select_on = { R.drawable.guide_home_on, R.drawable.guide_discover_on, R.drawable.guide_account_on };
    // 界面底部的未选中菜单按钮资源
    private int[] select_off = { R.drawable.bt_menu_0_select, R.drawable.bt_menu_2_select, R.drawable.bt_menu_3_select };
    /** 主界面 */
    private Home_F home_F;
    private NetworkConfig_F networkConfig_F;
    /** 用户页面 */
    private User_F user_F;
    private long exitTime = 0;
    ReadRfidCardUtil readRfidCardUtil=new ReadRfidCardUtil();
   public   MyApplication myapp;
   public MyApplication rmyapp;
  public Handler rHandler;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main__fa);
        initView();
    }

    // 初始化组件
    private void initView() {
        
        // 找到底部菜单的按钮并设置监听
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i] = (ImageView) findViewById(bt_menu_id[i]);
            bt_menu[i].setOnClickListener(this);
        }
        // 初始化默认显示的界面
        if (home_F == null) {
            home_F = new Home_F();
            addFragment(home_F);
            showFragment(home_F);
        } else {
            showFragment(home_F);
        }
        // 设置默认首页为点击时的图片
        bt_menu[0].setImageResource(select_on[0]);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_menu_0:
                // 主界面
                if (home_F == null) {
                    home_F = new Home_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(home_F);
                    showFragment(home_F);
                } else {
                    // if (home_F.isHidden()) {
                    showFragment(home_F);
                    // }
                }
                break;
            case R.id.iv_menu_2:
                // 网络配置界面
                if (networkConfig_F == null) {
                    networkConfig_F = new NetworkConfig_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(networkConfig_F);
                    showFragment(networkConfig_F);
                } else {
                    if (networkConfig_F.isHidden()) {
                        showFragment(networkConfig_F);
                    }
                }
                break;

            case R.id.iv_menu_3:
                // 用户界面
                if (user_F == null) {
                    user_F = new User_F();
                    // 判断当前界面是否隐藏，如果隐藏就进行添加显示，false表示显示，true表示当前界面隐藏
                    addFragment(user_F);
                    showFragment(user_F);
                } else {
                    if (user_F.isHidden()) {
                        showFragment(user_F);
                    }
                }
                break;
                default:
                    break;
        }
        // 设置按钮的选中和未选中资源
        for (int i = 0; i < bt_menu.length; i++) {
            bt_menu[i].setImageResource(select_off[i]);
            if (v.getId() == bt_menu_id[i]) {
                bt_menu[i].setImageResource(select_on[i]);
            }
        }
    }

    /** 添加Fragment **/
    public void addFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.show_layout, fragment);
        ft.commit();
    }

    /** 删除Fragment **/
    public void removeFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    /** 显示Fragment **/
    public void showFragment(Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        // 设置Fragment的切换动画
        ft.setCustomAnimations(R.anim.cu_push_right_in, R.anim.cu_push_left_out);

        // 判断页面是否已经创建，如果已经创建，那么就隐藏掉
        if (home_F != null) {
            ft.hide(home_F);
        }
        if (networkConfig_F != null) {
            ft.hide(networkConfig_F);
        }
        if (user_F != null) {
            ft.hide(user_F);
        }
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }

    /** 返回按钮的监听 */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, "点击返回按钮", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    /** Fragment的回调函数 */
    @SuppressWarnings("unused")
    private IBtnCallListener btnCallListener;

    @Override
    public void onAttachFragment(Fragment fragment) {
        try {
            btnCallListener = (IBtnCallListener) fragment;
        } catch (Exception e) {
        }
        super.onAttachFragment(fragment);
    }

    /**
     * 响应从Fragment中传过来的消息
     */
    @Override
    public void transferMsg() {
        if (home_F == null) {
            home_F = new Home_F();
            addFragment(home_F);
            showFragment(home_F);
        } else {
            showFragment(home_F);
        }
        bt_menu[3].setImageResource(select_off[3]);
        bt_menu[0].setImageResource(select_on[0]);

        System.out.println("由Fragment中传送来的消息");
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            onDestroy();
        }
    }

  //读写器掉电
     @Override
    protected void onDestroy() {
         //释放内存串
         Intent intent = new Intent(Intent.ACTION_MAIN);
         intent.addCategory(Intent.CATEGORY_HOME);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         this.startActivity(intent);
         System.exit(0);
         super.onDestroy();
    }
}

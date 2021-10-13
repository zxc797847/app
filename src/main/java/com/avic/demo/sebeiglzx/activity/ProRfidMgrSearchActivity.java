package com.avic.demo.sebeiglzx.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.home.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.utils.bean_implement.SbjldwImplement;
import com.avic.demo.sebeiglzx.utils.bean_implement.SbztImplement;

import com.avic.demo.sebeiglzx.utils.redcarutil.ReadRfidCardUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;
import com.uhf.api.cls.Reader;
import com.avic.demo.sebeiglzx.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * 设备台账查询
 */


public class ProRfidMgrSearchActivity extends Activity {

    private TextView tv_pro_rfid_mgr_sbcx_search;
    private TextView tv_pro_rfid_mgr_sbcx_add;
    private TextView tv_pro_rfid_mgr_sbcx_delet;
    private ImageView iv_back;
    private EditText et_bqh;
    private TextView tv_bqh;
    private TextView tv_sbbh;
    private TextView tv_ccbh;
    private TextView tv_cpbm;
    private TextView tv_cpgg;
    private TextView tv_cpmc;
    private TextView tv_cpxh;
    private TextView tv_cpfl_mc;
    private TextView tv_sccj;
    private TextView tv_sbzt;
    private TextView tv_jldw;
    private TextView tv_cgsj;
    private TextView tv_sybm;
    private TextView tv_sbdj;
    private TextView tv_sl;
    private TextView tv_szwz;
    private TextView tv_sbtp;
    private TextView tv_sbzs;
    private TextView tv_jbr;
    private TextView tv_sbfzr;
    private TextView tv_bz;

    private Handler rHandler;
    private SoundPool soundPool, soundPoolerr;
    public ProgressDialog myDialog = null;
    private ProgressDialog myyDialog = null;
    public ProgressDialog deleteDialog = null;
    ReadRfidCardUtil readRfidCardUtil = new ReadRfidCardUtil();
    MyApplication myapp;
    MyApplication rmyapp;
    Reader.TagFilter_ST g2tf = null;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_rfid_mgr_search2);
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, R.raw.beep333, 1);
        initView();
        giveLimit();
    }

    /**
     * 初始页面
     */
    private void initView() {

        //初始Application开子线程
        rHandler = new Handler() { //主线程调用
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                myapp = (MyApplication) msg.obj;
            }
        };
        new Thread(    //开启子线程
                new Runnable() {
                    @Override
                    public void run() {
                        Application app = getApplication();
                        rmyapp = readRfidCardUtil.ReadRfidapp(app);
                        Message message = new Message();
                        message.obj = rmyapp;
                        rHandler.sendMessage(message);
                    }
                }).start();

        //返回上级页面
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_bqh = (TextView) findViewById(R.id.tv_sbcx_bqh);
        tv_sbbh = (TextView) findViewById(R.id.tv_sbcx_sbbh);
        tv_ccbh = (TextView) findViewById(R.id.tv_sbcx_ccbh);
        tv_cpbm = (TextView) findViewById(R.id.tv_sbcx_cpbm);
        tv_cpgg = (TextView) findViewById(R.id.tv_sbcx_cpgg);
        tv_cpmc = (TextView) findViewById(R.id.tv_sbcx_cpmc);
        tv_cpxh = (TextView) findViewById(R.id.tv_sbcx_cpxh);
        tv_cpfl_mc = (TextView) findViewById(R.id.tv_sbcx_cpfl_mc);
        tv_sccj = (TextView) findViewById(R.id.tv_sbcx_sccj);
        tv_sbzt = (TextView) findViewById(R.id.tv_sbcx_sbzt);
        tv_jldw = (TextView) findViewById(R.id.tv_sbcx_jldw);
        tv_cgsj = (TextView) findViewById(R.id.tv_sbcx_cgsj);
        tv_sybm = (TextView) findViewById(R.id.tv_sbcx_sybm);
        tv_sbdj = (TextView) findViewById(R.id.tv_sbcx_sbdj);
        tv_sl = (TextView) findViewById(R.id.tv_sbcx_sl);
        tv_szwz = (TextView) findViewById(R.id.tv_sbcx_szwz);
        tv_sbtp = (TextView) findViewById(R.id.tv_sbcx_sbtp);
        tv_sbzs = (TextView) findViewById(R.id.tv_sbcx_sbzs);
        tv_jbr = (TextView) findViewById(R.id.tv_sbcx_jbr);
        tv_sbfzr = (TextView) findViewById(R.id.tv_sbcx_sbfzr);
        tv_bz = (TextView) findViewById(R.id.tv_sbcx_bz);
        et_bqh = (EditText) findViewById(R.id.et_find_bqh);

        //读卡查询
        tv_pro_rfid_mgr_sbcx_add = (TextView) findViewById(R.id.tv_pro_rfid_mgr_sbcx_add);
        tv_pro_rfid_mgr_sbcx_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readRfid();
            }
        });


        //根据标签号查询设备台账信息
        tv_pro_rfid_mgr_sbcx_search = (TextView) findViewById(R.id.tv_pro_rfid_mgr_sbcx_search);
        tv_pro_rfid_mgr_sbcx_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bqhValue = et_bqh.getText().toString().trim();
                if (TextUtils.isEmpty(bqhValue)) {
                    Toast.makeText(ProRfidMgrSearchActivity.this, "标签号不能为空,请填写标签号!", Toast.LENGTH_SHORT).show();
                    et_bqh.setText("");
                    tv_bqh.setText("");
                    tv_sbbh.setText("");
                    tv_ccbh.setText("");
                    tv_cpbm.setText("");
                    tv_cpgg.setText("");
                    tv_cpmc.setText("");
                    tv_cpxh.setText("");
                    tv_cpfl_mc.setText("");
                    tv_sccj.setText("");
                    tv_sbzt.setText("");
                    tv_jldw.setText("");
                    tv_cgsj.setText("");
                    tv_sybm.setText("");
                    tv_sbdj.setText("");
                    tv_sl.setText("");
                    tv_szwz.setText("");
                    tv_sbtp.setText("");
                    tv_sbzs.setText("");
                    tv_jbr.setText("");
                    tv_sbfzr.setText("");
                    tv_bz.setText("");
                } else if (bqhValue.length() != 8) {
                    Toast.makeText(ProRfidMgrSearchActivity.this, "标签号长度不为8,请填写正确的标签号!", Toast.LENGTH_SHORT).show();
                    tv_bqh.setText("");
                    tv_sbbh.setText("");
                    tv_ccbh.setText("");
                    tv_cpbm.setText("");
                    tv_cpgg.setText("");
                    tv_cpmc.setText("");
                    tv_cpxh.setText("");
                    tv_cpfl_mc.setText("");
                    tv_sccj.setText("");
                    tv_sbzt.setText("");
                    tv_jldw.setText("");
                    tv_cgsj.setText("");
                    tv_sybm.setText("");
                    tv_sbdj.setText("");
                    tv_sl.setText("");
                    tv_szwz.setText("");
                    tv_sbtp.setText("");
                    tv_sbzs.setText("");
                    tv_jbr.setText("");
                    tv_sbfzr.setText("");
                    tv_bz.setText("");
                } else {
                    myyDialog = ProgressDialog.show(ProRfidMgrSearchActivity.this, "", "正在查询....请稍等", true);
                    String field = "bqh";
                    String value = bqhValue;
                    proRfidSearch(field, value);
                }
            }
        });

        //删除
        tv_pro_rfid_mgr_sbcx_delet = (TextView) findViewById(R.id.tv_pro_rfid_mgr_sbcx_delet);
        tv_pro_rfid_mgr_sbcx_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tvbqhValue = tv_bqh.getText().toString().trim();
                if (TextUtils.isEmpty(tvbqhValue)) {
                    Toast.makeText(ProRfidMgrSearchActivity.this, "标签号为空,无数据可删!", Toast.LENGTH_SHORT).show();
                } else {
                    final String tvbqh = tvbqhValue.substring(0, 4) + tvbqhValue.substring(5, 9);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProRfidMgrSearchActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("你确定要删除标签号为:" + tvbqhValue + "的数据吗？");
                    builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteDialog = ProgressDialog.show(ProRfidMgrSearchActivity.this, "", "正在删除....请稍等", true);
                            String field = "bqh";
                            proReadRfidMgrDelet(field, tvbqh);
                        }
                    });
                    builder.setNegativeButton("取  消", null);
                    builder.show();
                }
            }
        });

        //查看图片
        tv_sbtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ProRfidMgrSearchActivity.this, Image_album_showActivity.class);
                String sbtp = tv_sbtp.getText().toString().trim();
                if (sbtp.length() > 0) {
                    intent.putExtra("sbtp", sbtp);
                    intent.putExtra("falg", 1);
                    ProRfidMgrSearchActivity.this.startActivity(intent);
                }
            }
        });
    }

    //给存储权限
    private void giveLimit() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }
        }

    }

    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("由于手持机需要获取存储空间，为您创建存储过程；\n否则，您将无法正常使用手持机")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }


    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else
                        finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许手持机使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 按下事件
     */
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                exit();
            }
            break;
            case KeyEvent.KEYCODE_MENU:
                break;
            case 422: {
                readRfid();
            }
            break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * 读卡实现
     * */
    private void readRfid() {
        String val_TID = "";
        String val_EPC = readRfidCardUtil.ReadRfidCard_EPC();
        if (readRfidCardUtil.ReadRfidCard_TID1() == "") {
            val_TID = readRfidCardUtil.ReadRfidCard_TID();
        } else {
            val_TID = readRfidCardUtil.ReadRfidCard_TID1();
        }
        if (TextUtils.isEmpty(val_TID)) {
            Toast.makeText(ProRfidMgrSearchActivity.this, "读卡失败,请把RFID卡离手持机在10cm内!", Toast.LENGTH_SHORT).show();
            et_bqh.setText("");
            tv_bqh.setText("");
            tv_sbbh.setText("");
            tv_ccbh.setText("");
            tv_cpbm.setText("");
            tv_cpgg.setText("");
            tv_cpmc.setText("");
            tv_cpxh.setText("");
            tv_cpfl_mc.setText("");
            tv_sccj.setText("");
            tv_sbzt.setText("");
            tv_jldw.setText("");
            tv_cgsj.setText("");
            tv_sybm.setText("");
            tv_sbdj.setText("");
            tv_sl.setText("");
            tv_szwz.setText("");
            tv_sbtp.setText("");
            tv_sbzs.setText("");
            tv_jbr.setText("");
            tv_sbfzr.setText("");
            tv_bz.setText("");
        } else {
            soundPool.play(1, 1, 1, 0, 0, 1);
            myyDialog = ProgressDialog.show(ProRfidMgrSearchActivity.this, "", "正在查询....请稍等", true);
            String fieldl = "rfid_tid";
            String valuel = val_TID;
            proRfidSearch(fieldl, valuel);
        }
    }


    /*
     *查询方法
     * */
    public void proRfidSearch(final String field, final String str) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URLHelper.getBaseUrl(ProRfidMgrSearchActivity.this) + "/appController.do?getSbtzbByField&field=" + field + "&value=" + str;
                String dataResult = "";
                try {
                    dataResult = GetHttp.RequstGetHttp(url);
                } catch (IOException e1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myyDialog.dismiss();
                            Toast.makeText(ProRfidMgrSearchActivity.this, "网络已断开或者服务器停止,请联系管理员!", Toast.LENGTH_SHORT).show();
                            if(field.equals("rfid_tid")){
                                et_bqh.setText("");
                            }
                            tv_bqh.setText("");
                            tv_sbbh.setText("");
                            tv_ccbh.setText("");
                            tv_cpbm.setText("");
                            tv_cpgg.setText("");
                            tv_cpmc.setText("");
                            tv_cpxh.setText("");
                            tv_cpfl_mc.setText("");
                            tv_sccj.setText("");
                            tv_sbzt.setText("");
                            tv_jldw.setText("");
                            tv_cgsj.setText("");
                            tv_sybm.setText("");
                            tv_sbdj.setText("");
                            tv_sl.setText("");
                            tv_szwz.setText("");
                            tv_sbtp.setText("");
                            tv_sbzs.setText("");
                            tv_jbr.setText("");
                            tv_sbfzr.setText("");
                            tv_bz.setText("");
                        }
                    });
                }
                try {
                    JSONObject jsonObj = new JSONObject(dataResult);
                    Boolean success = jsonObj.getBoolean("success");
                    final String msg = jsonObj.getString("msg");
                    if (success) {
                        JSONObject attributes = jsonObj.getJSONObject("attributes");
                        JSONArray dataArr = attributes.getJSONArray("data");
                        if (dataArr == null||dataArr.length() <= 0) {
                            //切换主线程更新ui
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myyDialog.dismiss();
                                    Toast.makeText(ProRfidMgrSearchActivity.this, "查询失败:设备台账表里没有该设备信息!", Toast.LENGTH_SHORT).show();
                                   if(field.equals("rfid_tid")){
                                       et_bqh.setText("");
                                   }
                                    tv_bqh.setText("");
                                    tv_sbbh.setText("");
                                    tv_ccbh.setText("");
                                    tv_cpbm.setText("");
                                    tv_cpgg.setText("");
                                    tv_cpmc.setText("");
                                    tv_cpxh.setText("");
                                    tv_cpfl_mc.setText("");
                                    tv_sccj.setText("");
                                    tv_sbzt.setText("");
                                    tv_jldw.setText("");
                                    tv_cgsj.setText("");
                                    tv_sybm.setText("");
                                    tv_sbdj.setText("");
                                    tv_sl.setText("");
                                    tv_szwz.setText("");
                                    tv_sbtp.setText("");
                                    tv_sbzs.setText("");
                                    tv_jbr.setText("");
                                    tv_sbfzr.setText("");
                                    tv_bz.setText("");
                                }
                            });
                        } else {
                            for (int j = 0; j < dataArr.length(); j++) {
                                JSONObject data = (JSONObject) dataArr.get(j);
                                Integer id = (Integer) data.get("id");
                                final String sbbh = data.isNull("sbbh") ? "" : data.getString("sbbh");
                                final String ccbh = data.isNull("ccbh") ? "" : data.getString("ccbh");
                                final String bqh = data.isNull("bqh") ? "" : data.getString("bqh");
                                final String cpbm = data.isNull("cpbm") ? "" : data.getString("cpbm");
                                final String cpgg = data.isNull("cpgg") ? "" : data.getString("cpgg");
                                final String cpmc = data.isNull("cpmc") ? "" : data.getString("cpmc");
                                final String cpxh = data.isNull("cpxh") ? "" : data.getString("cpxh");
                                final String cpfl_mc = data.isNull("cpfl_mc") ? "" : data.getString("cpfl_mc");
                                final String sccj = data.isNull("sccj") ? "" : data.getString("sccj");
                                String sbzt = data.isNull("sbzt") ? "" : data.getString("sbzt");
                                final String sbztname = SbztImplement.sbztText(sbzt);
                                String jldw = data.isNull("jldw") ? "" : data.getString("jldw");
                                final String jldwname = SbjldwImplement.jildwText(jldw);
                                final String cgsj = data.isNull("cgsj") ? "" : data.getString("cgsj");
                                final String sybm = data.isNull("sybm") ? "" : data.getString("sybm");
                                final String sbdj = data.isNull("sbdj") ? "0" : data.getString("sbdj");
                                final String sl = data.isNull("sl") ? "0" : data.getString("sl");
                                final String szwz = data.isNull("szwz") ? "" : data.getString("szwz");
                                final String sbtp = data.isNull("sbtp") ? "" : data.getString("sbtp");
                                final String sbzs = data.isNull("sbzs") ? "0" : data.getString("sbzs");
                                final String jbr = data.isNull("jbr") ? "" : data.getString("jbr");
                                final String sbfzr = data.isNull("sbfzr") ? "" : data.getString("sbfzr");
                                final String bz = data.isNull("bz") ? "" : data.getString("bz");

                                //切换主线程更新ui
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        myyDialog.dismiss();
                                        Toast.makeText(ProRfidMgrSearchActivity.this, "查询成功！", Toast.LENGTH_SHORT).show();
                                        et_bqh.setText(bqh);
                                        tv_bqh.setText(bqh.substring(0, 4) + "-" + bqh.substring(4, 8));
                                        tv_sbbh.setText(sbbh);
                                        tv_ccbh.setText(ccbh);
                                        tv_cpbm.setText(cpbm);
                                        tv_cpgg.setText(cpgg);
                                        tv_cpmc.setText(cpmc);
                                        tv_cpxh.setText(cpxh);
                                        tv_cpfl_mc.setText(cpfl_mc);
                                        tv_sccj.setText(sccj);
                                        tv_sbzt.setText(sbztname);
                                        tv_jldw.setText(jldwname);
                                        tv_cgsj.setText(cgsj);
                                        tv_sybm.setText(sybm);
                                        tv_sbdj.setText(sbdj);
                                        tv_sl.setText(sl);
                                        tv_szwz.setText(szwz);
                                        tv_sbtp.setText(sbtp);
                                        tv_sbzs.setText(sbzs);
                                        tv_jbr.setText(jbr);
                                        tv_sbfzr.setText(sbfzr);
                                        tv_bz.setText(bz);
                                    }
                                });
                            }
                        }
                    } else {
                        //切换主线程更新ui
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myyDialog.dismiss();
                                Toast.makeText(ProRfidMgrSearchActivity.this, "查询失败原因:" + msg, Toast.LENGTH_SHORT).show();
                                if(field.equals("rfid_tid")){
                                    et_bqh.setText("");
                                }
                                tv_bqh.setText("");
                                tv_sbbh.setText("");
                                tv_ccbh.setText("");
                                tv_cpbm.setText("");
                                tv_cpgg.setText("");
                                tv_cpmc.setText("");
                                tv_cpxh.setText("");
                                tv_cpfl_mc.setText("");
                                tv_sccj.setText("");
                                tv_sbzt.setText("");
                                tv_jldw.setText("");
                                tv_cgsj.setText("");
                                tv_sybm.setText("");
                                tv_sbdj.setText("");
                                tv_sl.setText("");
                                tv_szwz.setText("");
                                tv_sbtp.setText("");
                                tv_sbzs.setText("");
                                tv_jbr.setText("");
                                tv_sbfzr.setText("");
                                tv_bz.setText("");
                            }
                        });
                    }
                } catch (JSONException e) {
                    //切换主线程更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myyDialog.dismiss();
                            Toast.makeText(ProRfidMgrSearchActivity.this, "查询失败:返回数据格式有问题或者服务器停止,请联系管理员!", Toast.LENGTH_SHORT).show();
                            if(field.equals("rfid_tid")){
                                et_bqh.setText("");
                            }
                            tv_bqh.setText("");
                            tv_sbbh.setText("");
                            tv_ccbh.setText("");
                            tv_cpbm.setText("");
                            tv_cpgg.setText("");
                            tv_cpmc.setText("");
                            tv_cpxh.setText("");
                            tv_cpfl_mc.setText("");
                            tv_sccj.setText("");
                            tv_sbzt.setText("");
                            tv_jldw.setText("");
                            tv_cgsj.setText("");
                            tv_sybm.setText("");
                            tv_sbdj.setText("");
                            tv_sl.setText("");
                            tv_szwz.setText("");
                            tv_sbtp.setText("");
                            tv_sbzs.setText("");
                            tv_jbr.setText("");
                            tv_sbfzr.setText("");
                            tv_bz.setText("");
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 删除方法
     */
    public void proReadRfidMgrDelet(final String str, final String value) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URLHelper.getBaseUrl(ProRfidMgrSearchActivity.this) + "/appController.do?deleteSbtzbByField&field=" + str + "&value=" + value;
                String dataResult = "";
                try {
                    dataResult = GetHttp.RequstGetHttp(url);
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deleteDialog.dismiss();
                            Toast.makeText(ProRfidMgrSearchActivity.this, "网络已断开或者服务器停止,请联系管理员！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                try {
                    JSONObject jsonObj = new JSONObject(dataResult);
                    Boolean success = jsonObj.getBoolean("success");
                    final String msg = jsonObj.getString("msg");
                    if (success) {
                        //切换主线程更新ui
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                et_bqh.setText("");
                                tv_bqh.setText("");
                                tv_sbbh.setText("");
                                tv_ccbh.setText("");
                                tv_cpbm.setText("");
                                tv_cpgg.setText("");
                                tv_cpmc.setText("");
                                tv_cpxh.setText("");
                                tv_cpfl_mc.setText("");
                                tv_sccj.setText("");
                                tv_sbzt.setText("");
                                tv_jldw.setText("");
                                tv_cgsj.setText("");
                                tv_sybm.setText("");
                                tv_sbdj.setText("");
                                tv_sl.setText("");
                                tv_szwz.setText("");
                                tv_sbtp.setText("");
                                tv_sbzs.setText("");
                                tv_jbr.setText("");
                                tv_sbfzr.setText("");
                                tv_bz.setText("");
                                deleteDialog.dismiss();
                                Toast.makeText(ProRfidMgrSearchActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        //切换主线程更新ui
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                deleteDialog.dismiss();
                                Toast.makeText(ProRfidMgrSearchActivity.this, "删除失败:" + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                   // deleteDialog.dismiss();
                    //切换主线程更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deleteDialog.dismiss();
                            Toast.makeText(ProRfidMgrSearchActivity.this, "删除失败:返回数据格式有问题或者服务器停止,请联系管理员!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    //手持机返回键
    private void exit() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (myapp != null) {
            if (myapp.Mreader != null) {
                myapp.Mreader.CloseReader();
                myapp.needlisen = true;
                myapp.haveinit = false;
                boolean blen = myapp.Rpower.PowerDown();
                if (blen) {
                    Toast.makeText(ProRfidMgrSearchActivity.this, "下电：" + blen, Toast.LENGTH_SHORT).show();
                }
                super.onDestroy();
            } else {
                super.onDestroy();
            }
        } else {
            super.onDestroy();
        }
    }
}

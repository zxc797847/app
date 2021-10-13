package com.avic.demo.sebeiglzx.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.home.Main_FA;

import com.avic.demo.sebeiglzx.home.NetworkConfig_F;
import com.avic.demo.sebeiglzx.home.OtherPower;
import com.avic.demo.sebeiglzx.paesenter.LoginPresenter;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.view.ILoginView;
import com.avic.demo.sebeiglzx.R;
import com.function.SPconfig;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements ILoginView {

    private Button btn_connectBlue;
    private Button btn_network_config;
    private Button login_button;
    private EditText userName, password;
    private CheckBox rem_pwd, auto_login;
    private String userNameValue, passwordValue;
    private SharedPreferences sp;
    private SharedPreferences networkSp;
    private LoginPresenter loginPresenter;
    MyApplication myapp;
    Reader.TagFilter_ST g2tf = null;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private AlertDialog dialog;
    boolean isotherpowerup=false;
    boolean isdefaultmaxpow_V=false;

    public void Poweron(){
        String ip = networkSp.getString("NETWORK_CONFIG_IP", "");
        try {
            if (StringUtil.isEmpty(ip)) {
                Toast.makeText(LoginActivity.this, "请先配置网络连接！", Toast.LENGTH_SHORT).show();
            } else {

                String connectionIp = "/dev/ttyMT1";//"/dev/ttyMT1";

                if (connectionIp == "") {
                    Toast.makeText(LoginActivity.this,
                            MyApplication.Constr_sub1adrno, Toast.LENGTH_SHORT)
                            .show();
                }
                RfidPower.PDATYPE PT = RfidPower.PDATYPE.HDBX6;
                myapp.Rpower = new RfidPower(PT,getApplicationContext());
                if (myapp.Rpower == null) {
                    Toast.makeText(LoginActivity.this,
                            MyApplication.Constr_sub1pdtsl, Toast.LENGTH_SHORT)
                            .show();
                }
                boolean blen =false;

                if(myapp.Rpower.GetType()!=RfidPower.PDATYPE.NONE)
                {
                    blen =myapp.Rpower.PowerUp();

                    if (!blen)
                        return;
                }
                else
                {
                    if(isotherpowerup)
                        OtherPower.oPowerUp();
                }
                int portc = 1;

                long const_v = System.currentTimeMillis();
                //Thread.sleep(500);
                Reader.READER_ERR er = myapp.Mreader.InitReader_Notype(connectionIp, portc);

                if (er == Reader.READER_ERR.MT_OK_ERR) {
                    myapp.needlisen = true;
                    myapp.needreconnect = false;
                    myapp.spf.SaveString("PDATYPE", String
                            .valueOf(""));
                    myapp.spf.SaveString("ADDRESS", connectionIp);
                    myapp.spf.SaveString("ANTPORT",
                            String.valueOf("1"));

                    Reader.Inv_Potls_ST ipst = myapp.Mreader.new Inv_Potls_ST();
                    ipst.potlcnt = 1;
                    ipst.potls = new Reader.Inv_Potl[ipst.potlcnt];
                    for (int i = 0; i < ipst.potlcnt; i++) {
                        Reader.Inv_Potl ipl = myapp.Mreader.new Inv_Potl();
                        ipl.weight = 30;
                        ipl.potl = Reader.SL_TagProtocol.SL_TAG_PROTOCOL_GEN2;
                        ipst.potls[i] = ipl;
                    }
                    er = myapp.Mreader.ParamSet(
                            Reader.Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
                    int[] av = new int[1];
                    myapp.Mreader.ParamGet(
                            Reader.Mtr_Param.MTR_PARAM_READER_AVAILABLE_ANTPORTS, av);
                    myapp.antportc = portc;

                    myapp.Rparams = myapp.spf.ReadReaderParams();

                    if(isdefaultmaxpow_V)
                    {

                        int[] mp = new int[1];
                        er = myapp.Mreader.ParamGet(
                                Reader.Mtr_Param.MTR_PARAM_RF_MAXPOWER, mp);
                        if (er == Reader.READER_ERR.MT_OK_ERR) {
                            myapp.Rparams.setdefaulpwval((short)mp[0]);

                        }

                    }

                    if (myapp.Rparams.invpro.size() < 1)
                        myapp.Rparams.invpro.add("GEN2");

                    List<Reader.SL_TagProtocol> ltp = new ArrayList<Reader.SL_TagProtocol>();
                    for (int i = 0; i < myapp.Rparams.invpro.size(); i++) {
                        if (myapp.Rparams.invpro.get(i).equals("GEN2")) {
                            ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_GEN2);

                        } else if (myapp.Rparams.invpro.get(i).equals("6B")) {
                            ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_ISO180006B);

                        } else if (myapp.Rparams.invpro.get(i).equals("IPX64")) {
                            ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_IPX64);

                        } else if (myapp.Rparams.invpro.get(i).equals("IPX256")) {
                            ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_IPX256);

                        }
                    }

                    ipst = myapp.Mreader.new Inv_Potls_ST();
                    ipst.potlcnt = ltp.size();
                    ipst.potls = new Reader.Inv_Potl[ipst.potlcnt];
                    Reader.SL_TagProtocol[] stp = ltp
                            .toArray(new Reader.SL_TagProtocol[ipst.potlcnt]);
                    for (int i = 0; i < ipst.potlcnt; i++) {
                        Reader.Inv_Potl ipl = myapp.Mreader.new Inv_Potl();
                        ipl.weight = 30;
                        ipl.potl = stp[i];
                        ipst.potls[i] = ipl;
                    }

                    er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
                    Log.d("MYINFO", "Connected set pro:" + er.toString());

                    er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_READER_IS_CHK_ANT,
                            new int[] { myapp.Rparams.checkant });
                    Log.d("MYINFO", "Connected set checkant:" + er.toString());

                    Reader.AntPowerConf apcf = myapp.Mreader.new AntPowerConf();
                    apcf.antcnt = myapp.antportc;
                    for (int i = 0; i < apcf.antcnt; i++) {
                        Reader.AntPower jaap = myapp.Mreader.new AntPower();
                        jaap.antid = i + 1;
                        jaap.readPower = (short) myapp.Rparams.rpow[i];
                        jaap.writePower = (short) myapp.Rparams.wpow[i];
                        apcf.Powers[i] = jaap;
                    }
                    //Sub4TabActivity.nowpower=myapp.Rparams.rpow[0];
                    myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);

                    Reader.Region_Conf rre;
                    switch (myapp.Rparams.region) {
                        case 0:
                            rre = Reader.Region_Conf.RG_PRC;
                            break;
                        case 1:
                            rre = Reader.Region_Conf.RG_NA;
                            break;
                        case 2:
                            rre = Reader.Region_Conf.RG_NONE;
                            break;
                        case 3:
                            rre = Reader.Region_Conf.RG_KR;
                            break;
                        case 4:
                            rre = Reader.Region_Conf.RG_EU;
                            break;
                        case 5:
                            rre = Reader.Region_Conf.RG_EU2;
                            break;
                        case 6:
                            rre = Reader.Region_Conf.RG_EU3;
                            break;
                        case 9:
                            rre=Reader.Region_Conf.RG_OPEN;
                            break;
                        case 10:
                            rre=Reader.Region_Conf.RG_PRC2;
                            break;
                        case 7:
                        case 8:
                        default:
                            rre = Reader.Region_Conf.RG_NONE;
                            break;
                    }
                    if (rre != Reader.Region_Conf.RG_NONE) {
                        er = myapp.Mreader.ParamSet(
                                Reader.Mtr_Param.MTR_PARAM_FREQUENCY_REGION, rre);
                    }

                    if (myapp.Rparams.frelen > 0) {

                        Reader.HoptableData_ST hdst = myapp.Mreader.new HoptableData_ST();
                        hdst.lenhtb = myapp.Rparams.frelen;
                        hdst.htb = myapp.Rparams.frecys;
                        er = myapp.Mreader.ParamSet(
                                Reader.Mtr_Param.MTR_PARAM_FREQUENCY_HOPTABLE, hdst);
                    }

                    er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION,
                            new int[] { myapp.Rparams.session });
                    er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_Q,
                            new int[] { myapp.Rparams.qv });
                    er = myapp.Mreader.ParamSet(
                            Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_WRITEMODE,
                            new int[] { myapp.Rparams.wmode });
                    er = myapp.Mreader.ParamSet(
                            Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_MAXEPCLEN,
                            new int[] { myapp.Rparams.maxlen });
                    er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_TARGET,
                            new int[] { myapp.Rparams.target });

                    if (myapp.Rparams.filenable == 1) {
                        Reader.TagFilter_ST tfst = myapp.Mreader.new TagFilter_ST();
                        tfst.bank = myapp.Rparams.filbank;
                        int len = myapp.Rparams.fildata.length();
                        len = len % 2 == 0 ? len : len + 1;
                        tfst.fdata = new byte[len / 2];
                        myapp.Mreader.Str2Hex(myapp.Rparams.fildata,
                                myapp.Rparams.fildata.length(), tfst.fdata);
                        tfst.flen = myapp.Rparams.fildata.length() * 4;
                        tfst.startaddr = myapp.Rparams.filadr;
                        tfst.isInvert = myapp.Rparams.filisinver;

                        myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, tfst);
                    }

                    if (myapp.Rparams.emdenable == 1) {
                        Reader.EmbededData_ST edst = myapp.Mreader.new EmbededData_ST();

                        edst.accesspwd = null;
                        edst.bank = myapp.Rparams.emdbank;
                        edst.startaddr = myapp.Rparams.emdadr;
                        edst.bytecnt = myapp.Rparams.emdbytec;

                        er = myapp.Mreader.ParamSet(
                                Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, edst);
                    }

                    er = myapp.Mreader.ParamSet(
                            Reader.Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA,
                            new int[] { myapp.Rparams.adataq });
                    er = myapp.Mreader.ParamSet(
                            Reader.Mtr_Param.MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI,
                            new int[] { myapp.Rparams.rhssi });
                    er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_SEARCH_MODE,
                            new int[] { myapp.Rparams.invw });

                    Reader.AntPortsVSWR apvr=myapp.Mreader.new AntPortsVSWR();
                    apvr.andid=1;
                    apvr.power=(short) myapp.Rparams.rpow[0];
                    apvr.region=Reader.Region_Conf.RG_NA;
                    er=myapp.Mreader.ParamGet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPORTS_VSWR, apvr);

                    //TextView tv_module = (TextView) findViewById(R.id.textView_module);

                    Reader.HardwareDetails val = myapp.Mreader.new HardwareDetails();
                    er = myapp.Mreader.GetHardwareDetails(val);

                    if (er == Reader.READER_ERR.MT_OK_ERR) {
                        myapp.myhd=val;
                    }
                    myapp.Address = connectionIp;
                    myapp.haveinit=true;

                } else {

                }


            }
        } catch (Exception e) {
            Toast.makeText(LoginActivity.this, "登陆失败，原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
      // 获得实例对象
        Application app = getApplication();
        try {
            myapp = (MyApplication) app;
        } catch (Exception e2) {
        }
        /*Application app = getApplication();
        myapp = (MyApplication) app;*/
        //myapp.Mreader = new Reader("/lib/myalib/");
        //myapp.Mreader = new Reader("/lib/myalib");
        myapp.Mreader = new Reader();
        myapp.spf = new SPconfig(this);
        giving();//动态给权限
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        networkSp = this.getSharedPreferences("networkConfigInfo", Context.MODE_PRIVATE);
        btn_connectBlue = (Button) findViewById(R.id.btn_connectBlue);
        btn_network_config = (Button) findViewById(R.id.btn_network_config);
        login_button = (Button) findViewById(R.id.login_button);
        userName = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.network_config_port);
        rem_pwd = (CheckBox) findViewById(R.id.login_rememberpassword);
        auto_login = (CheckBox) findViewById(R.id.login_autologin);

        // 判断记住密码多选框的状态
        if (sp.getBoolean("REM_PWD", false)) {
            // 设置默认是记录密码状态
            rem_pwd.setChecked(true);
            userName.setText(sp.getString("USER_NAME", ""));
            password.setText(sp.getString("PASSWORD", ""));
            // 判断自动登陆多选框状态
            if (sp.getBoolean("AUTO_LOGIN", false)) {
                // 设置默认是自动登录状态
                auto_login.setChecked(true);
                userNameValue = sp.getString("USER_NAME", "");
                passwordValue = sp.getString("PASSWORD", "");
                // 开始登录
                Poweron();
                loginPresenter = new LoginPresenter(LoginActivity.this);
                loginPresenter.login(LoginActivity.this);

            }
        }


        /**
         * 数据同步
         */
        btn_connectBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
              /*  Intent intent = new Intent(LoginActivity.this, UpDataActivity.class);
                startActivity(intent);*/
            }
        });


        /**
         * 连接网络配置
         */
        btn_network_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, NetworkConfigActivity.class);
                startActivity(intent);
            }
        });


        /**
         * 登录
         */
        login_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//				 Intent intent = new Intent(LoginActivity.this, Main_FA.class);
//				 LoginActivity.this.startActivity(intent);
//				 finish();

                userNameValue = userName.getText().toString();
                passwordValue = password.getText().toString();
                String ip = networkSp.getString("NETWORK_CONFIG_IP", "");
                if (StringUtil.isEmpty(userNameValue) || StringUtil.isEmpty(passwordValue)) {
                    Toast.makeText(LoginActivity.this, "用户名或密码不允许为空！请输入用户名和密码", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if (StringUtil.isEmpty(ip)) {
                            Toast.makeText(LoginActivity.this, "请先配置网络连接！", Toast.LENGTH_SHORT).show();
                        } else {

                            String connectionIp = "/dev/ttyMT1";//"/dev/ttyMT1";

                            if (connectionIp == "") {
                                Toast.makeText(LoginActivity.this,
                                        MyApplication.Constr_sub1adrno, Toast.LENGTH_SHORT)
                                        .show();
                            }
                            RfidPower.PDATYPE PT = RfidPower.PDATYPE.HDBX6;
                            myapp.Rpower = new RfidPower(PT,getApplicationContext());
                            if (myapp.Rpower == null) {
                                Toast.makeText(LoginActivity.this,
                                        MyApplication.Constr_sub1pdtsl, Toast.LENGTH_SHORT)
                                        .show();
                            }
                            boolean blen =false;

                            if(myapp.Rpower.GetType()!=RfidPower.PDATYPE.NONE)
                            {
                                blen =myapp.Rpower.PowerUp();

                                if (!blen)
                                    return;
                            }
                            else
                            {
                                if(isotherpowerup)
                                    OtherPower.oPowerUp();
                            }
                            int portc = 1;

                            long const_v = System.currentTimeMillis();
                            //Thread.sleep(500);
                            Reader.READER_ERR er = myapp.Mreader.InitReader_Notype(connectionIp, portc);

                            if (er == Reader.READER_ERR.MT_OK_ERR) {
                                myapp.needlisen = true;
                                myapp.needreconnect = false;
                                myapp.spf.SaveString("PDATYPE", String
                                        .valueOf(""));
                                myapp.spf.SaveString("ADDRESS", connectionIp);
                                myapp.spf.SaveString("ANTPORT",
                                        String.valueOf("1"));

                                Reader.Inv_Potls_ST ipst = myapp.Mreader.new Inv_Potls_ST();
                                ipst.potlcnt = 1;
                                ipst.potls = new Reader.Inv_Potl[ipst.potlcnt];
                                for (int i = 0; i < ipst.potlcnt; i++) {
                                    Reader.Inv_Potl ipl = myapp.Mreader.new Inv_Potl();
                                    ipl.weight = 30;
                                    ipl.potl = Reader.SL_TagProtocol.SL_TAG_PROTOCOL_GEN2;
                                    ipst.potls[i] = ipl;
                                }
                                er = myapp.Mreader.ParamSet(
                                        Reader.Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
                                int[] av = new int[1];
                                myapp.Mreader.ParamGet(
                                        Reader.Mtr_Param.MTR_PARAM_READER_AVAILABLE_ANTPORTS, av);
                                myapp.antportc = portc;

                                myapp.Rparams = myapp.spf.ReadReaderParams();

                                if(isdefaultmaxpow_V)
                                {

                                    int[] mp = new int[1];
                                    er = myapp.Mreader.ParamGet(
                                            Reader.Mtr_Param.MTR_PARAM_RF_MAXPOWER, mp);
                                    if (er == Reader.READER_ERR.MT_OK_ERR) {
                                        myapp.Rparams.setdefaulpwval((short)mp[0]);

                                    }

                                }

                                if (myapp.Rparams.invpro.size() < 1)
                                    myapp.Rparams.invpro.add("GEN2");

                                List<Reader.SL_TagProtocol> ltp = new ArrayList<Reader.SL_TagProtocol>();
                                for (int i = 0; i < myapp.Rparams.invpro.size(); i++) {
                                    if (myapp.Rparams.invpro.get(i).equals("GEN2")) {
                                        ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_GEN2);

                                    } else if (myapp.Rparams.invpro.get(i).equals("6B")) {
                                        ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_ISO180006B);

                                    } else if (myapp.Rparams.invpro.get(i).equals("IPX64")) {
                                        ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_IPX64);

                                    } else if (myapp.Rparams.invpro.get(i).equals("IPX256")) {
                                        ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_IPX256);

                                    }
                                }

                                ipst = myapp.Mreader.new Inv_Potls_ST();
                                ipst.potlcnt = ltp.size();
                                ipst.potls = new Reader.Inv_Potl[ipst.potlcnt];
                                Reader.SL_TagProtocol[] stp = ltp
                                        .toArray(new Reader.SL_TagProtocol[ipst.potlcnt]);
                                for (int i = 0; i < ipst.potlcnt; i++) {
                                    Reader.Inv_Potl ipl = myapp.Mreader.new Inv_Potl();
                                    ipl.weight = 30;
                                    ipl.potl = stp[i];
                                    ipst.potls[i] = ipl;
                                }

                                er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
                                Log.d("MYINFO", "Connected set pro:" + er.toString());

                                er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_READER_IS_CHK_ANT,
                                        new int[] { myapp.Rparams.checkant });
                                Log.d("MYINFO", "Connected set checkant:" + er.toString());

                                Reader.AntPowerConf apcf = myapp.Mreader.new AntPowerConf();
                                apcf.antcnt = myapp.antportc;
                                for (int i = 0; i < apcf.antcnt; i++) {
                                    Reader.AntPower jaap = myapp.Mreader.new AntPower();
                                    jaap.antid = i + 1;
                                    jaap.readPower = (short) myapp.Rparams.rpow[i];
                                    jaap.writePower = (short) myapp.Rparams.wpow[i];
                                    apcf.Powers[i] = jaap;
                                }
                                //Sub4TabActivity.nowpower=myapp.Rparams.rpow[0];
                                myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);

                                Reader.Region_Conf rre;
                                switch (myapp.Rparams.region) {
                                    case 0:
                                        rre = Reader.Region_Conf.RG_PRC;
                                        break;
                                    case 1:
                                        rre = Reader.Region_Conf.RG_NA;
                                        break;
                                    case 2:
                                        rre = Reader.Region_Conf.RG_NONE;
                                        break;
                                    case 3:
                                        rre = Reader.Region_Conf.RG_KR;
                                        break;
                                    case 4:
                                        rre = Reader.Region_Conf.RG_EU;
                                        break;
                                    case 5:
                                        rre = Reader.Region_Conf.RG_EU2;
                                        break;
                                    case 6:
                                        rre = Reader.Region_Conf.RG_EU3;
                                        break;
                                    case 9:
                                        rre=Reader.Region_Conf.RG_OPEN;
                                        break;
                                    case 10:
                                        rre=Reader.Region_Conf.RG_PRC2;
                                        break;
                                    case 7:
                                    case 8:
                                    default:
                                        rre = Reader.Region_Conf.RG_NONE;
                                        break;
                                }
                                if (rre != Reader.Region_Conf.RG_NONE) {
                                    er = myapp.Mreader.ParamSet(
                                            Reader.Mtr_Param.MTR_PARAM_FREQUENCY_REGION, rre);
                                }

                                if (myapp.Rparams.frelen > 0) {

                                    Reader.HoptableData_ST hdst = myapp.Mreader.new HoptableData_ST();
                                    hdst.lenhtb = myapp.Rparams.frelen;
                                    hdst.htb = myapp.Rparams.frecys;
                                    er = myapp.Mreader.ParamSet(
                                            Reader.Mtr_Param.MTR_PARAM_FREQUENCY_HOPTABLE, hdst);
                                }

                                er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION,
                                        new int[] { myapp.Rparams.session });
                                er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_Q,
                                        new int[] { myapp.Rparams.qv });
                                er = myapp.Mreader.ParamSet(
                                        Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_WRITEMODE,
                                        new int[] { myapp.Rparams.wmode });
                                er = myapp.Mreader.ParamSet(
                                        Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_MAXEPCLEN,
                                        new int[] { myapp.Rparams.maxlen });
                                er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_TARGET,
                                        new int[] { myapp.Rparams.target });

                                if (myapp.Rparams.filenable == 1) {
                                    Reader.TagFilter_ST tfst = myapp.Mreader.new TagFilter_ST();
                                    tfst.bank = myapp.Rparams.filbank;
                                    int len = myapp.Rparams.fildata.length();
                                    len = len % 2 == 0 ? len : len + 1;
                                    tfst.fdata = new byte[len / 2];
                                    myapp.Mreader.Str2Hex(myapp.Rparams.fildata,
                                            myapp.Rparams.fildata.length(), tfst.fdata);
                                    tfst.flen = myapp.Rparams.fildata.length() * 4;
                                    tfst.startaddr = myapp.Rparams.filadr;
                                    tfst.isInvert = myapp.Rparams.filisinver;

                                    myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, tfst);
                                }

                                if (myapp.Rparams.emdenable == 1) {
                                    Reader.EmbededData_ST edst = myapp.Mreader.new EmbededData_ST();

                                    edst.accesspwd = null;
                                    edst.bank = myapp.Rparams.emdbank;
                                    edst.startaddr = myapp.Rparams.emdadr;
                                    edst.bytecnt = myapp.Rparams.emdbytec;

                                    er = myapp.Mreader.ParamSet(
                                            Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, edst);
                                }

                                er = myapp.Mreader.ParamSet(
                                        Reader.Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA,
                                        new int[] { myapp.Rparams.adataq });
                                er = myapp.Mreader.ParamSet(
                                        Reader.Mtr_Param.MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI,
                                        new int[] { myapp.Rparams.rhssi });
                                er = myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_SEARCH_MODE,
                                        new int[] { myapp.Rparams.invw });

                                Reader.AntPortsVSWR apvr=myapp.Mreader.new AntPortsVSWR();
                                apvr.andid=1;
                                apvr.power=(short) myapp.Rparams.rpow[0];
                                apvr.region=Reader.Region_Conf.RG_NA;
                                er=myapp.Mreader.ParamGet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPORTS_VSWR, apvr);

                                //TextView tv_module = (TextView) findViewById(R.id.textView_module);

                                Reader.HardwareDetails val = myapp.Mreader.new HardwareDetails();
                                er = myapp.Mreader.GetHardwareDetails(val);

                                if (er == Reader.READER_ERR.MT_OK_ERR) {
                                    myapp.myhd=val;
                                }
                                myapp.Address = connectionIp;
                                myapp.haveinit=true;

                            } else {

                            }

                            loginPresenter = new LoginPresenter(LoginActivity.this);
                            loginPresenter.login(LoginActivity.this);
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "登陆失败，原因："+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                /*Intent intent = new Intent(LoginActivity.this, Main_FA.class);
                LoginActivity.this.startActivity(intent);
                finish();*/

            }
        });

        /**
         * 监听记住密码多选框按钮事件
         */
        rem_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (rem_pwd.isChecked()) {

                    System.out.println("记住密码已选中");
                    sp.edit().putBoolean("REM_PWD", true).commit();
                } else {
                    System.out.println("记住密码没有选中");
                    sp.edit().putBoolean("REM_PWD", false).commit();
                }
            }
        });


        /**
         * 监听自动登录多选框事件
         */
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自动登录已选中");

                    sp.edit().putBoolean("AUTO_LOGIN", true).commit();
                } else {
                    System.out.println("自动登录没有选中");
                    sp.edit().putBoolean("AUTO_LOGIN", false).commit();
                }
            }
        });
    }

    //@Override
    public String getUserName() {
        return userNameValue;
    }

    // @Override
    public String getPassword() {
        return passwordValue;
    }

    /**
     * 登录成功
     */
    //@Override
    public void onSuccess(final String realname,final String emp_no) {
        // 切换主线程更新ui
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 跳转界面
                /*Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                // 登录成功和记住密码框为选中状态才保存用户信息
                if (rem_pwd.isChecked()) {
                    // 记住用户名、密码
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("USER_NAME", userNameValue);
                    editor.putString("PASSWORD", passwordValue);
                    editor.commit();
                }
                Intent intent = new Intent(LoginActivity.this, Main_FA.class);
                LoginActivity.this.startActivity(intent);*/
                // 跳转界面
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                // 登录成功和记住密码框为选中状态才保存用户信息
                // 记住用户名、密码
                SharedPreferences.Editor editor = sp.edit();
                if (rem_pwd.isChecked()) {
                    // 记住用户名、密码
                    editor.putString("USER_NAME", userNameValue);
                    editor.putString("PASSWORD", passwordValue);
                }
                editor.putString("REALNAME", realname);
                editor.putString("EMP_NO", emp_no);
                //editor.putString("myapp", myapp);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, Main_FA.class);
                LoginActivity.this.startActivity(intent);


                finish();
            }
        });
    }

    /**
     * 登录失败
     */
    //@Override
    public void onFailure(final String msg) {
        // 切换主线程更新ui
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //给存储权限
    private void giving() {
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

}

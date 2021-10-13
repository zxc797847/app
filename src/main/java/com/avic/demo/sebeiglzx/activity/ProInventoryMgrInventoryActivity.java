package com.avic.demo.sebeiglzx.activity;

import android.annotation.SuppressLint;
import android.app.Activity;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.adapter.AdapterListViewInventoryMgrInventory;
import com.avic.demo.sebeiglzx.bean.PddBean;
import com.avic.demo.sebeiglzx.bean.PddMxBean;
import com.avic.demo.sebeiglzx.home.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.listener.PostHttp;
import com.avic.demo.sebeiglzx.sort.MesRfidFkbSort;
import com.avic.demo.sebeiglzx.utils.redcarutil.ReadRfidCardUtil;
import com.avic.demo.sebeiglzx.utils.tools.LogToFile;
import com.avic.demo.sebeiglzx.utils.url.ExceptionUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;
import com.uhf.api.cls.Reader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/*
 * 设备盘点单盘点
 * */
public class ProInventoryMgrInventoryActivity extends Activity {
    private ImageView iv_sbpd_back;
    private Spinner spinner_sbpd_pdd;
    private TextView tv_sbpd_pdd_search;
    private TextView tv_sbpd_zpds;
    private TextView tv_sbpd_ypds;
    private TextView tv_sbpd_pdd_start;
    private TextView tv_sbpd_pdd_save;
    private ListView list_sbpd;

    private ArrayAdapter<PddBean> pddhAdapter;
    private String pddValue = "";
    Map<String, Integer> InventoryBuffer;
    public ProgressDialog myDialog = null;
    public ProgressDialog pddDialog = null;
    public ProgressDialog saveDialog = null;
    private List<PddMxBean> mDatas;
    private AdapterListViewInventoryMgrInventory mAdapter;
    private boolean isSearchPdd = false;
    private String pdd;
    private int ypdsCounts = 0;
    private int zpdsCounts = 0;
    boolean isrun, issound = true;
    private int xtBqsl = 0;
    private int ypBqsl = 0;
    private int spSl = 0;

    ReadRfidCardUtil readRfidCardUtil = new ReadRfidCardUtil();
    private Handler rHandler;
    MyApplication myapp;
    MyApplication rmyapp;
    private SoundPool soundPool, soundPoolerr;
    private SimpleDateFormat simpleDateFormat;
    private Date date;
    private long exitTime = 0;
    private List<String> epcList = new ArrayList<String>();
    private Handler handlerpd = new Handler();

    @Override
    @SuppressLint("SimpleDateFormat")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_inventory_mgr_inventory);
        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundPool.load(this, R.raw.beep333, 1);
        initView();
        initPddh();
    }

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
        tv_sbpd_zpds = (TextView) findViewById(R.id.tv_sbpd_zpds);
        tv_sbpd_ypds = (TextView) findViewById(R.id.tv_sbpd_ypds);
        list_sbpd = (ListView) findViewById(R.id.list_sbpd);
        mDatas = new ArrayList<PddMxBean>();
        mAdapter = new AdapterListViewInventoryMgrInventory(ProInventoryMgrInventoryActivity.this, mDatas);


        spinner_sbpd_pdd = (Spinner) findViewById(R.id.spinner_sbpd_pdd);
        spinner_sbpd_pdd.setPrompt("选择盘点单号：");
        spinner_sbpd_pdd.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                pddValue = spinner_sbpd_pdd.getSelectedItem().toString();
                arg0.setVisibility(View.VISIBLE);
                tv_sbpd_ypds.setText("" + 0);
                tv_sbpd_zpds.setText("" + 0);
                mDatas.clear();
                mAdapter.notifyDataSetChanged();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                arg0.setVisibility(View.VISIBLE);
            }
        });
        /* 下拉菜单弹出的内容选项触屏事件处理 */
        spinner_sbpd_pdd.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        //盘点单查询
        tv_sbpd_pdd_search = (TextView) findViewById(R.id.tv_sbpd_pdd_search);
        tv_sbpd_pdd_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdd = getPddValue().toString().trim();
                String text=tv_sbpd_pdd_start.getText().toString().trim();
                if (TextUtils.isEmpty(pdd)) {
                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "盘点单不能为空,请选择!", Toast.LENGTH_SHORT).show();
                } else if (pdd.length() < 12) {
                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "盘点单异常,盘点单长度小于12!", Toast.LENGTH_SHORT).show();
                } else if(text.equals("停止盘点")){
                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "正在盘点不能查询,请停止盘点后再进行查询!", Toast.LENGTH_SHORT).show();
                }else if(ypdsCounts>0){
                    android.app.AlertDialog alertDialog1 = new android.app.AlertDialog.Builder(ProInventoryMgrInventoryActivity.this).setTitle("提示").setMessage("已盘点到"+ypdsCounts+"项并未提交,确定要重新查询吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {// 添加"Yes"按钮
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            InventoryBuffer = new LinkedHashMap<String, Integer>();
                            myDialog = ProgressDialog.show(ProInventoryMgrInventoryActivity.this, "", "正在查询....请稍等", true);
                            mDatas.clear();
                            mAdapter.notifyDataSetChanged();
                            list_sbpd.setAdapter(mAdapter);
                            pddtaillSeach(pdd);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 添加取消
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).create();
                    alertDialog1.show();
                }
                else {
                    InventoryBuffer = new LinkedHashMap<String, Integer>();
                    myDialog = ProgressDialog.show(ProInventoryMgrInventoryActivity.this, "", "正在查询....请稍等", true);
                    mDatas.clear();
                    mAdapter.notifyDataSetChanged();
                    list_sbpd.setAdapter(mAdapter);
                    pddtaillSeach(pdd);
                }
            }
        });

        //开始盘点
        tv_sbpd_pdd_start = (TextView) findViewById(R.id.tv_sbpd_pdd_start);
        tv_sbpd_pdd_start.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                pddCheck();
            }
        });

        //提交
        tv_sbpd_pdd_save = (TextView) findViewById(R.id.tv_sbpd_pdd_save);
        tv_sbpd_pdd_save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                String pddh = getPddValue().toString().trim();
                String txt = tv_sbpd_pdd_start.getText().toString().trim();
                String tids = "";
                StringBuffer sb = new StringBuffer();
                saveDialog = ProgressDialog.show(ProInventoryMgrInventoryActivity.this, "", "正在提交....请稍等", true);
                if (txt.equals("停止盘点")) {
                    saveDialog.dismiss();
                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "请停止盘点,才能提交!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mDatas.size() <= 0) {
                        saveDialog.dismiss();
                        Toast.makeText(ProInventoryMgrInventoryActivity.this, "没有需要盘点的信息,请先查询并盘点后再提交!", Toast.LENGTH_SHORT).show();
                    } else {
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (mDatas.get(i).getIsCheck()) {
                                sb.append(mDatas.get(i).getRfid_epc() + ",");
                            }
                        }
                        tids = sb.toString();//转换为字符串赋值给tids
                        if (TextUtils.isEmpty(tids)) {
                            saveDialog.dismiss();
                            Toast.makeText(ProInventoryMgrInventoryActivity.this, "没有盘点成功信息,请盘点成功后再提交!", Toast.LENGTH_SHORT).show();
                        } else {
                            tids = tids.substring(0, tids.length() - 1);
                            String rfid = "rfid_epc";
                            post(pddh, tids, rfid);
                        }

                    }
                }
            }
        });

        //返回
        iv_sbpd_back = (ImageView) findViewById(R.id.iv_sbpd_back);
        iv_sbpd_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        });
    }

    /**
     * 初始盘点单号
     */
    private void initPddh() {
        pddDialog = ProgressDialog.show(ProInventoryMgrInventoryActivity.this, "", "正在加载....请稍等", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URLHelper.getBaseUrl(ProInventoryMgrInventoryActivity.this) + "/appController.do?getPddh";
                String dataResult = "";
                try {
                    dataResult = GetHttp.RequstGetHttp(url);
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pddDialog.dismiss();
                            Toast.makeText(ProInventoryMgrInventoryActivity.this, "网络已断开或者服务器停止,请联系管理员", Toast.LENGTH_SHORT).show();
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
                        if (dataArr == null||dataArr.length()<=0) {
                            pddDialog.dismiss();
                            Log.w("WARMING:", "没有数据！");
                        } else {
                            List<PddBean> pddhList = new ArrayList<PddBean>();
                            for (int j = 0; j < dataArr.length(); j++) {
                                JSONObject data = (JSONObject) dataArr.get(j);
                                final String pddh = data.isNull("pddh") ? "" : data.getString("pddh");
                                pddhList.add(new PddBean(pddh));
                            }
                            pddhAdapter = new ArrayAdapter<PddBean>(ProInventoryMgrInventoryActivity.this, android.R.layout.simple_spinner_item, pddhList);
                            //切换主线程更新ui
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spinner_sbpd_pdd.setAdapter(pddhAdapter);
                                    pddDialog.dismiss();
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pddDialog.dismiss();
                                Toast.makeText(ProInventoryMgrInventoryActivity.this, "没有盘点单,原因：" + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    //切换主线程更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pddDialog.dismiss();
                            Toast.makeText(ProInventoryMgrInventoryActivity.this, "获取盘点单失败:返回数据格式有问题或者服务器停止,请联系管理员!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e("ERROR", "获取接口数据失败,请联系管理员");
                }
            }
        }).start();
    }


    public String getPddValue() {
        return pddValue;
    }

    /**
     * 盘点单查询方法
     */

    public void pddtaillSeach(final String pdd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = URLHelper.getBaseUrl(ProInventoryMgrInventoryActivity.this) + "/appController.do?getPddMxbByPddh&pddh=" + pdd;
                String dataResult = "";
                try {
                    dataResult = GetHttp.RequstGetHttp(url);
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myDialog.dismiss();
                            Toast.makeText(ProInventoryMgrInventoryActivity.this, "网络已断开或者服务器停止,请联系管理员", Toast.LENGTH_SHORT).show();
                            tv_sbpd_ypds.setText("" + 0);
                            tv_sbpd_zpds.setText("" + 0);
                            tv_sbpd_pdd_start.setText("开始盘点");
                            mDatas.clear();
                            mAdapter.notifyDataSetChanged();
                            list_sbpd.setAdapter(mAdapter);
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
                        if (dataArr == null||dataArr.length()<=0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myDialog.dismiss();
                                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "查询失败:该盘点单没有盘点明细信息！", Toast.LENGTH_SHORT).show();
                                    tv_sbpd_ypds.setText("" + 0);
                                    tv_sbpd_zpds.setText("" + 0);
                                    tv_sbpd_pdd_start.setText("开始盘点");
                                    mDatas.clear();
                                    mAdapter.notifyDataSetChanged();
                                    list_sbpd.setAdapter(mAdapter);
                                }
                            });
                            Log.w("WARMING:", "没有数据！");
                        } else {
                            List<PddMxBean> list = new ArrayList<>();
                            for (int j = 0; j < dataArr.length(); j++) {
                                JSONObject data = (JSONObject) dataArr.get(j);
                                // Integer id = (Integer) data.get("id");
                                String bqh = data.isNull("bqh") ? "" : data.getString("bqh");
                                String rfid_tid = data.isNull("rfid_tid") ? "" : data.getString("rfid_tid");
                                String rfid_epc = data.isNull("rfid_epc") ? "" : data.getString("rfid_epc");
                                String cpmc = data.isNull("cpmc") ? "" : data.getString("cpmc");
                                String sybm = data.isNull("sybm") ? "" : data.getString("sybm");
                                String szwz = data.isNull("szwz") ? "" : data.getString("szwz");
                                String sbbh = data.isNull("sbbh") ? "" : data.getString("sbbh");
                                String cpgg = data.isNull("cpgg") ? "" : data.getString("cpgg");
                                String sbfzr = data.isNull("sbfzr") ? "" : data.getString("sbfzr");


                                PddMxBean pddmx = new PddMxBean();
                                pddmx.setBqh(bqh);
                                pddmx.setRfid_tid(rfid_tid);
                                pddmx.setRfid_epc(rfid_epc);
                                pddmx.setCpmc(cpmc);
                                pddmx.setSybm(sybm);
                                pddmx.setSzwz(szwz);
                                pddmx.setSbbh(sbbh);
                                pddmx.setCpgg(cpgg);
                                pddmx.setSbfzr(sbfzr);
                                pddmx.setIsCheck(false);
                                list.add(pddmx);
                            }
                            mDatas = list;
                            zpdsCounts = mDatas.size();
                            //切换主线程更新ui
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ypdsCounts = 0;
                                    myDialog.dismiss();
                                    tv_sbpd_ypds.setText("" + ypdsCounts);
                                    tv_sbpd_zpds.setText("" + zpdsCounts);
                                    mAdapter = new AdapterListViewInventoryMgrInventory(ProInventoryMgrInventoryActivity.this, (List<PddMxBean>) mDatas);
                                    list_sbpd.setAdapter(mAdapter);
                                }
                            });
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myDialog.dismiss();
                                Toast.makeText(ProInventoryMgrInventoryActivity.this, "盘点单查询失败,原因：" + msg, Toast.LENGTH_SHORT).show();
                                tv_sbpd_ypds.setText("" + 0);
                                tv_sbpd_zpds.setText("" + 0);
                                tv_sbpd_pdd_start.setText("开始盘点");
                                mDatas.clear();
                                mAdapter.notifyDataSetChanged();
                                list_sbpd.setAdapter(mAdapter);
                            }
                        });
                    }
                } catch (JSONException e) {
                    //切换主线程更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myDialog.dismiss();
                            Toast.makeText(ProInventoryMgrInventoryActivity.this, "盘点单查询失败:返回数据格式有问题或者服务器停止,请联系管理员!", Toast.LENGTH_SHORT).show();
                            tv_sbpd_ypds.setText("" + 0);
                            tv_sbpd_zpds.setText("" + 0);
                            tv_sbpd_pdd_start.setText("开始盘点");
                            mDatas.clear();
                            mAdapter.notifyDataSetChanged();
                            list_sbpd.setAdapter(mAdapter);
                        }
                    });
                }
            }
        }).start();
    }

    //执行线程（盘点扫描）
    private Runnable runnable_MainActivity = new Runnable() {
        public void run() {
            int[] tagcnt = new int[1];
            tagcnt[0] = 0;
            String[] tag = null;
            int streadt = 0, enreadt = 0;
            synchronized (this) {
                Reader.READER_ERR er;
                streadt = (int) System.currentTimeMillis();
                if (myapp.isquicklymode) {
                    er = myapp.Mreader.AsyncGetTagCount(tagcnt);
                } else {
                    er = myapp.Mreader.TagInventory_Raw(myapp.Rparams.uants,
                            myapp.Rparams.uants.length,
                            (short) myapp.Rparams.readtime, tagcnt);
                }

                if (er == Reader.READER_ERR.MT_OK_ERR) {
                    if (tagcnt[0] > 0) {
                        tag = new String[tagcnt[0]];
                        for (int i = 0; i < tagcnt[0]; i++) {
                            Reader.TAGINFO tfs = myapp.Mreader.new TAGINFO();

                            if (myapp.isquicklymode)
                                er = myapp.Mreader.AsyncGetNextTag(tfs);
                            else
                                er = myapp.Mreader.GetNextTag(tfs);
                            if (er == Reader.READER_ERR.MT_HARDWARE_ALERT_ERR_BY_TOO_MANY_RESET) {
                                myapp.needreconnect = true;
                            }
                            if (er == Reader.READER_ERR.MT_OK_ERR) {
                                tag[i] = Reader.bytes_Hexstr(tfs.EpcId);
                                // soundPool.play(1, 1, 1, 0, 0, 1);
                            } else
                                break;
                        }
                        enreadt = (int) System.currentTimeMillis();
                    }
                }
                if (tag != null && tag.length != 0) {
                    for (int i = 0; i < tag.length; i++) {
                        epcList.add(tag[i]);
                    }
                    for (String epcValue : epcList) {
                        String Str = new String(epcValue);
                        if (Str.equals("")) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "盘点读卡失败,请继续盘点将Rfid卡再靠近一点！！！", Toast.LENGTH_SHORT).show();
                                }
                            });
                            break;
                        } else {
                            String epcStr="";
                            if(Str.length() < 24){
                                epcStr=(Str+"000000000000");
                            }else {
                                epcStr=Str;
                            }
                            Integer findIndex = InventoryBuffer.get(epcStr);
                            if (findIndex == null) {
                                for (int j = 0; j < mDatas.size(); j++) {
                                    if (epcStr.equals(mDatas.get(j).getRfid_epc())) {
                                        soundPool.play(1, 1, 1, 0, 0, 1);
                                        InventoryBuffer.put(epcStr, ypdsCounts);
                                        ypdsCounts++;
                                        mDatas.get(j).setIsCheck(true);
                                        List<PddMxBean> newDatas = new ArrayList<PddMxBean>();
                                        newDatas.addAll(mDatas);
                                        mDatas.clear();
                                        mDatas.addAll(newDatas);
                                        Collections.sort(mDatas, new MesRfidFkbSort());
                                        // 切换主线程更新ui
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tv_sbpd_ypds.setText("" + ypdsCounts);
                                                String zpds = tv_sbpd_zpds.getText().toString().trim();
                                                int zpdcount = Integer.parseInt(zpds);
                                                // 总盘点数==已盘点数，停止
                                                if (ypdsCounts == zpdcount) {
                                                    isrun = false;
                                                    tv_sbpd_pdd_start.setText("开始盘点");
                                                    handlerpd.removeCallbacks(runnable_MainActivity);
                                                    AlertDialog alertDialog2 = new AlertDialog.Builder(ProInventoryMgrInventoryActivity.this).setTitle("提示").setMessage("本次盘点已经完成！").setPositiveButton("确定", new DialogInterface.OnClickListener() {// 添加"Yes"按钮
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                        }
                                                    }).create();
                                                    alertDialog2.show();
                                                }
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        });
                                        break;
                                    }
                                }
                            }
                        }
                    }

                }
            }
            if (isrun) {
                handlerpd.postDelayed(this, myapp.Rparams.sleep);
            } else {
                if (myapp.ThreadMODE == 0) {
                    if (myapp.isquicklymode) {
                        Log.d("MYINFO", "stop---");
                        Reader.READER_ERR er = myapp.Mreader.AsyncStopReading();
                        if (er != Reader.READER_ERR.MT_OK_ERR) {
                            return;
                        }
                    }
                    handlerpd.removeCallbacks(runnable_MainActivity);
                } else if (myapp.ThreadMODE == 1) {
                    if (myapp.Mreader.StopReading() != Reader.READER_ERR.MT_OK_ERR) {
                        return;
                    }
                }
            }
        }
    };

    //盘点操作
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void pddCheck() {
        String txt = tv_sbpd_pdd_start.getText().toString().trim();
        String zpds = tv_sbpd_zpds.getText().toString().trim();
        String ypds = tv_sbpd_ypds.getText().toString().trim();
        int zpdscount = Integer.parseInt(zpds);
        int ypdscount = Integer.parseInt(ypds);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = new Date(System.currentTimeMillis());
        if (zpdscount <= 0) {
            isrun = false;
            tv_sbpd_pdd_start.setText("开始盘点");
            Toast.makeText(ProInventoryMgrInventoryActivity.this, "没有需要盘点设备信息,无法盘点！", Toast.LENGTH_SHORT).show();
        } else {
            if (ypdscount == zpdscount) {
                isrun = false;
                tv_sbpd_pdd_start.setText("开始盘点");
                Toast.makeText(ProInventoryMgrInventoryActivity.this, "《盘点已结束,不能再盘点;请提交盘点数据》", Toast.LENGTH_SHORT).show();
            } else {
                if ("开始盘点".equals(txt)) {
                    tv_sbpd_pdd_start.setText("停止盘点");
                    isrun = true;
                    //开始盘点
                    handlerpd.postDelayed(runnable_MainActivity, 0);
                  /*  Thread runThread = new Thread(sebeDataterminalRunnable);
                    runThread.start();*/
                } else {
                    tv_sbpd_pdd_start.setText("开始盘点");
                    isrun = false;
                    handlerpd.removeCallbacks(runnable_MainActivity);

                }
            }
        }

    }

    //提交数据
    public void post(final String pddh, final String tids, final String rfid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = "";
                String params = "{\"pddh\":\"" + pddh + "\",\"tidStr\":\"" + tids + "\",\"field\":\"" + rfid + "\"}";
                String url = URLHelper.getBaseUrl(ProInventoryMgrInventoryActivity.this) + "/appController.do?submitPdd";
                String paramerStr = "params=" + params;
                try {
                    result = PostHttp.HttpRequest(url, "POST", paramerStr);
                } catch (Exception e1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            saveDialog.dismiss();
                            Toast.makeText(ProInventoryMgrInventoryActivity.this, "网络已断开或者服务器停止,请联系管理员", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    Boolean success = jsonObj.getBoolean("success");
                    final String msg = jsonObj.getString("msg");
                    if (success) {
                        JSONObject attributes = jsonObj.getJSONObject("attributes");
                        String resultData = (String) attributes.get("data");
                        if (!TextUtils.isEmpty(resultData)) {
                            String[] tids = resultData.split(",");
                            for (int i = 0; i < tids.length; i++) {
                                for (int j = 0; j < mDatas.size(); j++) {
                                    if (tids[i].equals(mDatas.get(j).getRfid_epc())) {
                                        mDatas.remove(j--);
                                    }
                                }
                            }
                            final int zpds = mDatas.size();
                            // 切换主线程更新ui
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ypdsCounts = 0;
                                    saveDialog.dismiss();
                                    tv_sbpd_zpds.setText("" + zpds);
                                    tv_sbpd_ypds.setText("" + ypdsCounts);
                                    mAdapter.notifyDataSetChanged();
                                    list_sbpd.setAdapter(mAdapter);
                                    if(zpds<=0){
                                        initPddh();//更新初始盘点单
                                    }
                                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            saveDialog.dismiss();
                            Toast.makeText(ProInventoryMgrInventoryActivity.this, "没有返回数据不能刷新界面,请联系管理员！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                saveDialog.dismiss();
                                Toast.makeText(ProInventoryMgrInventoryActivity.this, "盘点数据提交失败,原因：" + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    //切换主线程更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            saveDialog.dismiss();
                            Toast.makeText(ProInventoryMgrInventoryActivity.this, "提交失败:返回数据格式有问题或者服务器停止,请联系管理员!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    //手柄按钮按下事件
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                exit();
            }
            break;
            case KeyEvent.KEYCODE_MENU:
                break;
            case 422: {
                pddCheck();
            }
            break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
    //手柄按钮松开事件
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK: {
                exit();
            }
            break;
            case KeyEvent.KEYCODE_MENU:
                break;
            case 422: {
                String txt = tv_sbpd_pdd_start.getText().toString().trim();
                if (mDatas.size() <= 0) {
                    isrun = false;
                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "没有需要盘点设备信息,无法盘点！", Toast.LENGTH_SHORT).show();
                } else {
                    if ("停止盘点".equals(txt)) {
                        tv_sbpd_pdd_start.setText("停止盘点");
                        isrun = true;
                        handlerpd.postDelayed(runnable_MainActivity,0);
                       /* Thread runThread = new Thread(sebeDataterminalRunnable);
                        runThread.start();*/
                    } else {
                        tv_sbpd_pdd_start.setText("开始盘点");
                        isrun = false;
                        handlerpd.removeCallbacks(runnable_MainActivity);
                    }
                }
            }
            break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //手持机返回按钮事件
    private void exit() {
        String exitString = tv_sbpd_pdd_start.getText().toString().trim();
        if (exitString.equals("停止盘点")) {
            Toast.makeText(ProInventoryMgrInventoryActivity.this, "请点击停止盘点,才能退出程序！", Toast.LENGTH_SHORT).show();
        } else if (exitString.equals("开始盘点")) {
            finish();
        }
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
                    Toast.makeText(ProInventoryMgrInventoryActivity.this, "下电：" + blen, Toast.LENGTH_SHORT).show();
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







































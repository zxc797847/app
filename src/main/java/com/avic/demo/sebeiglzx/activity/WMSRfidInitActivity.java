package com.avic.demo.sebeiglzx.activity;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.adapter.WMSRFIDInitTableAdapter;
import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.home.OtherPower;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.listener.PostHttp;
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;
import com.function.SPconfig;
import com.function.commfun;
import com.pow.api.cls.RfidPower;
import com.uhf.api.cls.Reader;
import com.avic.demo.sebeiglzx.R;

/**
 * ??????
 *
 */
@SuppressLint("SimpleDateFormat")
public class WMSRfidInitActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	// ????????????
	private ImageView iv_back;
	String tid = "";
	String epc = "";
	boolean isrun, issound = true;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	MyApplication myapp;
	private KeyReceiver keyReceiver;
	String hjh = "";
	private Spinner spinner;
	private Spinner spinne;
	/*private Spinner spinner_GYLXK;*/
	private TextView spinner_PCH;
	//private TextView use_length;
	private TextView fb_number;
	private TextView tv_LPH;
	private TextView tv_code;
	private TextView tv_CPBM;
	private TextView process_route_no;
	private TextView tv_CPMC_name;
	private TextView tv_SL_name;
	private TextView Lu_number;
	private TextView tv_LPH_name;
	private TextView tv_GYLXK_name;
	private TextView tv_BQH_name;
	private TextView tv_init_read_rfid;
	//private TextView tv_init_scan;
	private EditText et_BQH;
	private EditText et_LPH;
	private EditText et_SL;
	private EditText et_card_SL;
	private EditText et_cpth;
	private EditText tv_DBH;
	private TextView tv_init_save;
	private TextView tv_init_add;
	private TextView tv_init_delete;
	private TextView tv_total_num;
	private List<InitProRfidFkbBean> proRfidFkbBeanList;
	private ListView tableListView;
	private CheckBox ck_XZ;
	WMSRFIDInitTableAdapter adapter;
	String[] proDataArr;
	String[] processTempDataArr;
	String[] PCHDataArr;
	String[] MACDataArr;
	private TextView tv_fb_number;

	//ICommonDialog dialog;
	ProgressDialog progressDialog;

	// ???????????????
	//private ScanThread scanThread;
	private List<Map<String, String>> listMap;

	private boolean mIsContinuous = false;

	private Timer scanTimer = null;

	/**
	 * Whether to use GBK character encoding set for transcoding
	 */
	private boolean mGbkFlag = false;

	private boolean mRegisterFlag = false;

	// ??????????????????
	private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
	private AlertDialog dialog;
	boolean isotherpowerup=false;
	boolean isdefaultmaxpow_V=false;
    Reader.READER_ERR er;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_rfid_init);
		initView();
		initData();
	}

	private void initData() {
		initProData();
		// initProcessTemp();
	}

	private void initView() {
		//dialog = CommonDialogFactory.createDialogByType(WMSRfidInitActivity.this, DialogUtil.DIALOG_TYPE_101);
		// ??????????????????
		Application app = getApplication();
		try {
			myapp = (MyApplication) app;
		} catch (Exception e2) {
		}
		/*myapp.Mreader = new Reader();
		myapp.spf = new SPconfig(this);*/
		//myapp = (MyApplication) this.getIntent().getSerializableExtra("myapp");
		proRfidFkbBeanList = new ArrayList<InitProRfidFkbBean>();
		spinner = (Spinner) findViewById(R.id.spinner_CPMC);
		//spinne = (Spinner) findViewById(R.id.Lu_number);

		tv_CPMC_name = (TextView) findViewById(R.id.tv_CPMC_name);
		spinner_PCH = (TextView) findViewById(R.id.spinner_PCH);
		//use_length = (TextView) findViewById(R.id.use_length);
		tv_CPMC_name = (TextView) findViewById(R.id.tv_CPMC_name);
		//tv_fb_number=(TextView) findViewById(R.id.tv_fb_number);
		tv_SL_name = (TextView) findViewById(R.id.tv_SL_name);
		//	tv_LPH_name = (TextView) findViewById(R.id.tv_LPH_name);
		tv_GYLXK_name = (TextView) findViewById(R.id.tv_GYLXK_name);
		tv_BQH_name = (TextView) findViewById(R.id.tv_BQH_name);
		tv_total_num = (TextView) findViewById(R.id.tv_total_num);

		/*ck_XZ = (CheckBox) findViewById(R.id.ck_XZ);
		ck_XZ.setOnCheckedChangeListener(this);*/
		tableListView = (ListView) findViewById(R.id.list);
		adapter = new WMSRFIDInitTableAdapter(WMSRfidInitActivity.this, proRfidFkbBeanList);

		String data[] = new String[] {};
		// ????????????ArrayAdaper???????????????Spinner??????
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, data);
		// ??????item???????????????
		adapter3.setDropDownViewResource(R.layout.spinner_item);
		// ?????????Spinner??????adapter
		spinner.setAdapter(adapter3);

		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String CPBMStr = spinner.getSelectedItem().toString();
				if (!StringUtil.isEmpty(CPBMStr)) {
					PCHDataArr=null;
					//		LPHDataArr=null;
					processTempDataArr=null;
					String order_no = CPBMStr.substring(0, CPBMStr.indexOf("|"));
					initProcessTemp(order_no);
				}
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				arg0.setVisibility(View.VISIBLE);
			}
		});

		/*spinner_PCH.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String pch = spinner_PCH.getSelectedItem().toString();
				if (!StringUtil.isEmpty(pch)) {
					getLphByPch(pch);
				}
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				arg0.setVisibility(View.VISIBLE);
			}
		});*/
		// ????????????
		initDev();

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		et_BQH = (EditText) findViewById(R.id.et_BQH);

		et_SL = (EditText) findViewById(R.id.et_SL);
		et_card_SL = (EditText) findViewById(R.id.et_card_SL);
		et_cpth = (EditText) findViewById(R.id.et_cpth);
		tv_DBH = (EditText) findViewById(R.id.tv_DBH);
		tv_init_read_rfid = (TextView) findViewById(R.id.tv_init_read_rfid);
		process_route_no = (TextView) findViewById(R.id.process_route_no);
		tv_init_read_rfid.setOnClickListener(this);

		//
		//	tv_init_scan = (TextView) findViewById(R.id.tv_init_scan);
		//	tv_init_scan.setOnClickListener(this);
		tv_init_add = (TextView) findViewById(R.id.tv_init_add);
		tv_init_add.setOnClickListener(this);
		tv_init_delete = (TextView) findViewById(R.id.tv_init_delete);
		tv_init_delete.setOnClickListener(this);
		tv_init_save = (TextView) findViewById(R.id.tv_init_save);
		tv_init_save.setOnClickListener(this);

		// ???????????????
		/*try {
			scanThread = new ScanThread(mHandler);
		} catch (Exception e) {
			// ????????????
			Toast.makeText(getApplicationContext(), "serialport init fail", 0).show();
			return;
		}
		scanThread.start();*/
		// ???????????????????????????
		keyReceiver = new KeyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.rfid.FUN_KEY");
		filter.addAction("android.intent.action.FUN_KEY");
		registerReceiver(keyReceiver, filter);
		mRegisterFlag = true;
	}

	/**
	 * ?????????????????????
	 */
	private void initDev() {
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep, 1);
		soundPoolerr = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		/*soundPoolerr.load(this, R.raw.alarm, 1);
		Application app = WMSRfidInitActivity.this.getApplication();
		myapp = (MyApplication) app;
		manager = myapp.manager;
		if (manager == null) {
			try {
				manager = UHFLongerManager.getInstance();
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			myapp.manager = manager;
		}

		manager.setAntenna(1);// set antenna
		final int value = 20;
		manager.setOutPower((short) value);
		final int reg = 1;
		manager.setFreBand((short) reg);*/

		keyReceiver = new KeyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.rfid.FUN_KEY");
		registerReceiver(keyReceiver, intentFilter);
	}

	@Override
	public void onClick(final View v) {

		switch (v.getId()) {
			/**
			 * ??????
			 */
			case R.id.iv_back:

				/*if (myapp.Mreader != null) {
					myapp.Mreader.CloseReader();
					myapp.needlisen = true;
				}

				if (myapp.Rpower != null) {
					myapp.Rpower.PowerDown();
					myapp.needreconnect = true;
				}*/
				finish();
				break;

			/**
			 * ??????
			 */
			case R.id.tv_init_read_rfid:
				/*progressDialog = ProgressDialog.show(WMSRfidInitActivity.this, "?????????", "????????????RFID?????????...", true, false);
				progressDialog.show();*/

				readTidByMobileDataTerminal();

				break;

			/**
			 * ??????
			 */
	/*	case R.id.tv_init_scan:
			// et_LPH.setText("J11906501");
			// ????????????
			scanThread.scan();
			break;*/
			/**
			 * ??????
			 */
			case R.id.tv_init_add:
				String spinner_CPMC =(String) spinner.getSelectedItem() /**/;
				if (StringUtil.isEmpty(spinner_CPMC)) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				initGenerateMarking();
				String batchNo =spinner_PCH.getText().toString();
				//String useLength=use_length.getText().toString();
				String gylxh=process_route_no.getText().toString();
				String CPMCStr = spinner.getSelectedItem().toString();
				//String zj = spinne.getSelectedItem().toString();
				//String GYLXKStr = spinner_GYLXK.getSelectedItem().toString();
				//String PCHStr = spinner_PCH.getSelectedItem().toString();
				String orderNum = et_SL.getText().toString();
				String et_SL = et_card_SL.getText().toString();
				String cpth = et_cpth.getText().toString();
				String dbh = tv_DBH.getText().toString();
				String processRouteNo = process_route_no.getText().toString();
				/*String SL = tv_SL_name.getText().toString();*/



				String BQH = et_BQH.getText().toString();
				//String LPHStr = Lu_number.getText().toString();
				String CPBM = "";
				String clph = "";
				String CPMC = "";
				if (!CPMCStr.contains("|")) {
					Toast.makeText(WMSRfidInitActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
					return;
				} else {
					CPBM = CPMCStr.substring(0, CPMCStr.indexOf("|"));
					CPMC = CPMCStr.substring(CPMCStr.indexOf("|") + 1, CPMCStr.length());

				}
				if (StringUtil.isEmpty(batchNo)) {
					Toast.makeText(WMSRfidInitActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringUtil.isEmpty(cpth)) {
					Toast.makeText(WMSRfidInitActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringUtil.isEmpty(dbh)) {
					Toast.makeText(WMSRfidInitActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
					return;
				}
				if (StringUtil.isEmpty(et_SL)) {
					Toast.makeText(WMSRfidInitActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
					return;
				}

				if (StringUtil.isEmpty(BQH)) {
					Toast.makeText(WMSRfidInitActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
					return;
				}

	/*		if (StringUtil.isEmpty(LPHStr)) {
				Toast.makeText(WMSRfidInitActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
				return;
			}
			if (StringUtil.isEmpty(LPHStr)) {
				Toast.makeText(WMSRfidInitActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
				return;
			}*/

		/*	String GYLXH = "";
			if (!GYLXKStr.contains("|")) {
				Toast.makeText(WMSRfidInitActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
				return;
			} else {
				GYLXH = GYLXKStr.substring(GYLXKStr.indexOf("|") + 1, GYLXKStr.length());
			}*/

				/**
				 * ???????????????????????????
				 */
				if (proRfidFkbBeanList.size() > 0) {
					for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
						if (proRfidFkbBeanList.get(i).getBqh().equals(BQH)) {
							Toast.makeText(WMSRfidInitActivity.this, "???????????????????????????????????????" + BQH + "????????????????????????", Toast.LENGTH_SHORT).show();
							et_BQH.setText("");
							return;
						}
					}
				}

				BigDecimal OldTotalNum = new BigDecimal(tv_total_num.getText().toString());
				OldTotalNum = OldTotalNum.add(new BigDecimal(et_SL));
				if(OldTotalNum.compareTo(new BigDecimal(orderNum))==1){
					Toast.makeText(WMSRfidInitActivity.this, "?????????????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
					return;
				}


				// ?????????????????????????????????
				ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
				tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
				InitProRfidFkbBean proRfidFkbBean = new InitProRfidFkbBean();
				proRfidFkbBean.setCpbm(CPBM);
				proRfidFkbBean.setPch(batchNo);
				//proRfidFkbBean.setUseLength(useLength);
				proRfidFkbBean.setCpmc(CPMC);
				proRfidFkbBean.setSl(new BigDecimal(et_SL));
				proRfidFkbBean.setBqh(BQH);
				proRfidFkbBean.setIsCheck(false);
				//proRfidFkbBean.setLph(LPHStr);
				proRfidFkbBean.setGylxh(gylxh);
				//proRfidFkbBean.setLph(zj);
				proRfidFkbBean.setClph(clph);
				proRfidFkbBean.setCpth(cpth);
				proRfidFkbBean.setDbh(dbh);
				proRfidFkbBeanList.add(proRfidFkbBean);
				ArrayAdapter<String> dataArr=(ArrayAdapter) spinner.getAdapter();
				/*spinner.setSelection(0);
				int count=dataArr.getCount();
				String[]data=new String[count-1];
				data[0]="";
				int q=1;
				for(int i=1;i<count;i++){
					String str=dataArr.getItem(i);
					if(!"".equals(str)&&str!=null){
						str = str.substring(0, str.indexOf("|"));
					}
					if(!CPBM.equals(str)&&!str.equals("")){
						data[q]=dataArr.getItem(i);
						q++;
					}
				}
				List<String> d=new ArrayList<String>(Arrays.asList(data));
				ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(WMSRfidInitActivity.this, R.layout.spinner_item, d);
				spinner.setAdapter(adapter3);*/
				adapter = new WMSRFIDInitTableAdapter(WMSRfidInitActivity.this, proRfidFkbBeanList);
				tableListView.setAdapter(adapter);
				BigDecimal totalNum = new BigDecimal(0);
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					totalNum = totalNum.add(proRfidFkbBeanList.get(i).getSl());
				}
				tv_total_num.setText(totalNum.toString());

				et_BQH.setText("");
				break;

			/**
			 * ??????
			 */
			case R.id.tv_init_delete:
				/*ArrayAdapter<String> dataArr1=(ArrayAdapter) spinner.getAdapter();
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					InitProRfidFkbBean proRfidFkbBean1 = proRfidFkbBeanList.get(i);
					if (proRfidFkbBean1.getIsCheck()) {
						String CPbm=proRfidFkbBeanList.get(i).getCpbm();
						String Cpmc=proRfidFkbBeanList.get(i).getCpmc();
						String str=CPbm+"|"+Cpmc;
						dataArr1.add(str);
						proRfidFkbBeanList.remove(i--);
					}
				}
			//List<String> d1=new ArrayList<String>(Arrays.asList(data1));
		    ArrayAdapter<String> adapter31 = new ArrayAdapter<String>(WMSRfidInitActivity.this, R.layout.spinner_item, d1);
		    spinner.setAdapter(adapter31);
				tableListView.setAdapter(adapter);
				BigDecimal totalNum1 = new BigDecimal(0);
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					totalNum1 = totalNum1.add(proRfidFkbBeanList.get(i).getSl());
				}
				tv_total_num.setText(totalNum1.toString());
				adapter.notifyDataSetChanged();*/
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					InitProRfidFkbBean proRfidFkbBean1 = proRfidFkbBeanList.get(i);
					if (proRfidFkbBean1.getIsCheck()) {
						proRfidFkbBeanList.remove(i--);
					}
				}
				tableListView.setAdapter(adapter);
				BigDecimal totalNum1 = new BigDecimal(0);
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					totalNum1 = totalNum1.add(proRfidFkbBeanList.get(i).getSl());
				}
				tv_total_num.setText(totalNum1.toString());
				adapter.notifyDataSetChanged();
				break;

			/**
			 * ??????
			 */
			case R.id.tv_init_save:

				progressDialog = ProgressDialog.show(WMSRfidInitActivity.this, "?????????", "????????????...", true, false);
				progressDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {

						String url = URLHelper.getBaseUrl(WMSRfidInitActivity.this) + "/appscjController.do?initialPointsCardTable";
						String result = "";
						try {
							JSONArray jsonArr = new JSONArray();
							if (proRfidFkbBeanList.size() > 0) {
								InitProRfidFkbBean proRfidFkbBean = proRfidFkbBeanList.get(0);
								for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
									JSONObject jsonObj = new JSONObject();
									jsonObj.put("bqh", proRfidFkbBeanList.get(i).getBqh());
									jsonObj.put("sl", proRfidFkbBeanList.get(i).getSl());
									jsonObj.put("cpth", proRfidFkbBeanList.get(i).getCpth());
									jsonObj.put("dbh", proRfidFkbBeanList.get(i).getDbh());
									jsonArr.put(jsonObj);
								}
								// url += "&data="+jsonArr.toString()+"&cpbm=" + proRfidFkbBean.getCpbm() + "&lph" + proRfidFkbBean.getLph() + "&gylxh=" + proRfidFkbBean.getGylxh();
								// result = GetHttp.RequstGetHttp(url);
								Map<String, String> paramsMap = new HashMap<String, String>();
								paramsMap.put("data", jsonArr.toString());
								paramsMap.put("cpbm", proRfidFkbBean.getCpbm());
								paramsMap.put("pch", proRfidFkbBean.getPch());
								//paramsMap.put("clph", proRfidFkbBean.getLph());
								//paramsMap.put("zj", proRfidFkbBean.getBz());
								paramsMap.put("gylxh", proRfidFkbBean.getGylxh());
								//paramsMap.put("useLength", proRfidFkbBean.getUseLength());
								//paramsMap.put("zj", proRfidFkbBean.getLph());
								//paramsMap.put("clph", proRfidFkbBean.getClph());

								//paramsMap.put("ycl", proRfidFkbBean.getGylxh());
								// result = GetHttp.RequstGetHttp(url);
								result = PostHttp.RequstPostHttpNew(WMSRfidInitActivity.this, url, paramsMap);
							} else {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(WMSRfidInitActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
									}
								});
							}
							List<InitProRfidFkbBean> beanList = new ArrayList<InitProRfidFkbBean>();
							JSONObject jsonObj = new JSONObject(result);
							boolean success = (Boolean) jsonObj.get("success");
							final String msg = (String) jsonObj.get("msg");
							if (success) {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(WMSRfidInitActivity.this, "????????????", Toast.LENGTH_SHORT).show();
										refreshData();
									}
								});
							} else {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(WMSRfidInitActivity.this, "????????????????????????"+msg, Toast.LENGTH_SHORT).show();
									}
								});
							}
						} catch (final Exception e1) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(WMSRfidInitActivity.this, "????????????", Toast.LENGTH_SHORT).show();
								}
							});
						} finally {
							progressDialog.cancel();
						}
					}
				}).start();
				break;
			default:
				Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
				break;

		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buton, boolean isChecked) {
		switch (buton.getId()) {
			/**
			 * ??????
			 */
			case R.id.ck_XZ:
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					proRfidFkbBeanList.get(i).setIsCheck(isChecked);
				}
				adapter = new WMSRFIDInitTableAdapter(WMSRfidInitActivity.this, proRfidFkbBeanList);
				tableListView.setAdapter(adapter);
				BigDecimal totalNum = new BigDecimal(0);
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					totalNum = totalNum.add(proRfidFkbBeanList.get(i).getSl());
				}
				tv_total_num.setText(totalNum.toString());
				adapter.notifyDataSetChanged();
				break;

		}
	}

	/**
	 * ?????????????????????
	 */
	private void initProcessTemp(final String order_no) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidInitActivity.this) + "/appscjController.do?getAllroutingNoAndroutingName&order_no=" + order_no;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					boolean isSuccess = jsonObj.getBoolean("success");
					final String msg = jsonObj.getString("msg");

					if (isSuccess) {

						JSONObject attributes = jsonObj.getJSONObject("attributes");
						final String processRouteNo= attributes.getString("gylxh");
						final String pch= attributes.getString("pch");

						final String number= attributes.get("number").toString();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								et_SL.setText(number);
								spinner_PCH.setText(pch);
								process_route_no.setText(processRouteNo);
							}
						});


					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidInitActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "?????????????????????"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}


		}).start();
	}

	/**
	 * ?????????????????????
	 *
	 */
	private void initGenerateMarking() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidInitActivity.this) + "/appscjController.do?initGenerateMarking";
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					boolean isSuccess = jsonObj.getBoolean("success");
					final String msg = jsonObj.getString("msg");
					if (isSuccess) {
						JSONObject attributes = jsonObj.getJSONObject("attributes");
						final String dbNumber= attributes.getString("dbNumber");
						tv_DBH.setText(dbNumber);

					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidInitActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "?????????????????????"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}


		}).start();
	}
	/**
	 * ?????????????????????
	 */
	private void getLphByPch(final String pch) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidInitActivity.this) + "/appscjController.do?getLphByPch&pch=" + pch;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					boolean isSuccess = jsonObj.getBoolean("success");
					final String msg = jsonObj.getString("msg");
					if (isSuccess) {
						JSONObject attributes = jsonObj.getJSONObject("attributes");
						final String LPH = attributes.getString("lph");
						/*runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tv_LPH.setText("nhao");
							}
						});*/

					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidInitActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "?????????????????????"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}


		}).start();
	}

	/**
	 * ????????????????????????
	 */
	private void initProData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidInitActivity.this) + "/appscjController.do?getAllProductCodeAndProductName";
				//String url1 = URLHelper.getBaseUrl(WMSRfidInitActivity.this) + "/appscjController.do?selectRawMaterials";
				String result = "";
				String dataa = "";
				try {
					result = GetHttp.RequstGetHttp(url);
					//dataa = GetHttp.RequstGetHttp(url1);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					JSONObject attributes = jsonObj.getJSONObject("attributes");
					JSONArray dataArr = attributes.getJSONArray("data");

					//JSONObject jsonOb = new JSONObject(dataa);
					//JSONObject attribute = jsonOb.getJSONObject("attributes");
					//JSONArray dataAr = attribute.getJSONArray("data");
					// String data[] = new String[] { "?????????????????????",
					// "BBT000007|?????????",
					// "BBT000008|??????" };
					//MACDataArr = new String[dataAr.length() + 1];
					proDataArr = new String[dataArr.length() + 1];
					//MACDataArr[0]="";
					proDataArr[0] = "";
					for (int i = 1; i <= dataArr.length(); i++) {
						JSONObject data = dataArr.getJSONObject(i - 1);
						String order_no = (String) data.get("order_no");
						String product_material = (String) data.get("product_material");
						proDataArr[i] = order_no + "|" + product_material;

					}
					/*for (int i = 1; i <= dataAr.length(); i++) {
						JSONObject data = dataAr.getJSONObject(i - 1);
						String clph = (String) data.get("clph");
						String zj = (String) data.get("zj");
						MACDataArr[i] = clph + "|" + zj;

					}*/
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(WMSRfidInitActivity.this, R.layout.spinner_item, proDataArr);
							//ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(WMSRfidInitActivity.this, R.layout.spinner_item, MACDataArr);
							// ??????item???????????????
							adapter3.setDropDownViewResource(R.layout.spinner_item);
							//adapter4.setDropDownViewResource(R.layout.spinner_item);
							// ?????????Spinner??????adapter
							spinner.setAdapter(adapter3);
							//spinne.setAdapter(adapter4);
						}
					});

				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "???????????????"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}

		}).start();
	}

	/**
	 * ??????tid???????????????
	 *
	 * @param
	 * @param value
	 */
	public void getBqhByTid(final String value) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidInitActivity.this) + "/appscjController.do?getPointsCardTabletagNoByTid&tid=" + value;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "?????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					boolean success = jsonObj.getBoolean("success");
					final String msg = jsonObj.getString("msg");
					if (success) {
						JSONObject attributes = jsonObj.getJSONObject("attributes");
						JSONArray dataArr = attributes.getJSONArray("data");
						final String bqh = (String) dataArr.get(0);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								et_BQH.setText(bqh);
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidInitActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidInitActivity.this, "???????????????"+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					//progressDialog.cancel();
				}
			}
		}).start();

	}

	/**
	 * ????????????
	 */
	public void refreshData() {

		tv_total_num.setText("");
		et_BQH.setText("");
		et_SL.setText("");
		et_card_SL.setText("");
		process_route_no.setText("");
		//et_LPH.setText("");
		spinner_PCH.setText("");
		spinner.setSelection(0);
		//spinner_GYLXK.setSelection(0);
		proRfidFkbBeanList.clear();
		adapter.notifyDataSetChanged();
	}

	public class KeyReceiver extends BroadcastReceiver {
		private String TAG = "KeyReceiver";

		@Override
		public void onReceive(Context context, Intent intent) {
			int keyCode = intent.getIntExtra("keyCode", 0);
			boolean keyDown = intent.getBooleanExtra("keydown", false);
			if (keyDown) {
				switch (keyCode) {
					case KeyEvent.KEYCODE_F1:
						readTidByMobileDataTerminal();
						break;
					case KeyEvent.KEYCODE_F2:
						//scanThread.scan();
						break;
					case KeyEvent.KEYCODE_F3:
						break;
					case KeyEvent.KEYCODE_F5:
						break;
					case KeyEvent.KEYCODE_F4:
						// showToast("F4");
						break;
				}
			}

		}
	}

	/**
	 * ????????????
	 */
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				break;
			case KeyEvent.KEYCODE_MENU:
				break;
			case KeyEvent.KEYCODE_F4: {
			}
			break;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	};


	private void readTidByMobileDataTerminal() {
		try {
            String tid = RfidRead.getTidValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSRfidInitActivity.this, "?????????????????????RFID??????????????????10cm??????", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
				getBqhByTid(tid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		finally {

		}
	}


	@Override
	protected void onDestroy() {
		/*
		 * if ("wmsApp".equals(AppHelper.getAppName())) { // ????????????????????????????????????I/O???????????????????????????RFIDReaderHelper // ?????????????????????????????????//???????????????????????????????????????????????? ?????????????????????//connector.connectCom("dev/ttyS4",115200); // RFIDReaderHelper?????????????????? ModuleManager.newInstance().setUHFStatus(false);// ?????????????????????????????????????????? // ??????????????????????????????????????? ModuleManager.newInstance().release(); } else if ("MobileDataTerminal".equals(AppHelper.getAppName())) { // if (manager != null) { // manager.close(); // manager = null; // } unregisterReceiver(keyReceiver); }
		 */
		if (scanTimer != null) {
			scanTimer.cancel();
		}
		mIsContinuous = false;
		/*if (scanThread != null) {
			scanThread.stopScan();
			scanThread.interrupt();
			scanThread.close();
			scanThread = null;
		}*/

		mRegisterFlag = false;
		unregisterReceiver(keyReceiver);
		super.onDestroy();
	}
}
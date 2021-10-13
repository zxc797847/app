package com.avic.demo.sebeiglzx.activity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.adapter.AdapterListViewWMSProcess;
import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;
import com.avic.demo.sebeiglzx.bean.MesLcgdMxbEntity;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;


/**
 * 流程进度查看
 *
 */
@SuppressLint("SimpleDateFormat")
public class WMSRfidProcessActivity extends Activity implements OnClickListener {
	// 返回按钮
	private ImageView iv_back;
	String tid = "";
	String epc = "";
	boolean isrun, issound = true;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	MyApplication myapp;
	private KeyReceiver keyReceiver;
	String hjh = "";
	private List<MesLcgdMxbEntity> mesLcgdMxbEntityList;
	private TextView tv_receive_delete;

	private List<InitProRfidFkbBean> proRfidFkbBeanList;
	private ListView wms_rfid_process_list;
	private CheckBox ck_XZ;
	AdapterListViewWMSProcess adapterListViewWMSProcess;
	String[] proDataArr;
	String[] processTempDataArr;
	private TextView tv_process_CPMC;
	//	private TextView tv_process_CPGG;
	private TextView tv_process_PCH;
	private TextView tv_process_SL;
	private TextView tv_process_LCGDH;
	private TextView tv_process_ZSL;
	private TextView tv_receive_read_card;
	private LinearLayout ll_process_read_card;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_rfid_process);
		initView();
		Intent intent = getIntent();
		tid = intent.getStringExtra("tid");
		if (!StringUtil.isEmpty(tid)) {
			ll_process_read_card.setVisibility(View.GONE);
			initData();
		}
	}

	private void initView() {
		Application app = getApplication();
		try {
			myapp = (MyApplication) app;
		} catch (Exception e2) {
		}
		// 初始化读写器
		initDev();
		wms_rfid_process_list = (ListView) findViewById(R.id.wms_rfid_process_list);
		tv_process_CPMC = (TextView) findViewById(R.id.tv_process_CPMC);
		//	tv_process_CPGG = (TextView) findViewById(R.id.tv_process_CPGG);
		tv_process_PCH = (TextView) findViewById(R.id.tv_process_PCH);
		tv_process_SL = (TextView) findViewById(R.id.tv_process_SL);
		tv_process_LCGDH = (TextView) findViewById(R.id.tv_process_LCGDH);
		tv_process_ZSL = (TextView) findViewById(R.id.tv_process_ZSL);
		tv_receive_read_card = (TextView) findViewById(R.id.tv_receive_read_card);
		tv_receive_read_card.setOnClickListener(this);
		ll_process_read_card = (LinearLayout) findViewById(R.id.ll_process_read_card);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		mesLcgdMxbEntityList = new LinkedList<MesLcgdMxbEntity>();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			/**
			 * 返回
			 */
			case R.id.iv_back:
				finish();
				break;
			/**
			 * 读卡
			 */
			case R.id.tv_receive_read_card:
				readTidByMobileDataTerminal();

			default:
				break;
		}
	}

	private void initData() {
		mesLcgdMxbEntityList.clear();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidProcessActivity.this) + "/appscjController.do?getAllLcgdAndMxbInfoByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidProcessActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
						final JSONArray lcgdJsonArr = attributes.getJSONArray("data");
						final JSONArray lcgdMxbJsonArr = attributes.getJSONArray("childData");
						final JSONObject lcgdJson = lcgdJsonArr.getJSONObject(0);

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									tv_process_LCGDH.setText("流程跟单号：" + lcgdJson.getString("lcgdh"));
									tv_process_ZSL.setText("标签号：" + lcgdJson.getString("bqh"));
									tv_process_CPMC.setText("产品名称：" + lcgdJson.getString("cpmc"));
									//		tv_process_CPGG.setText("产品规格：" + lcgdJson.getString("cpgg"));
									tv_process_PCH.setText("产品图号：" + lcgdJson.getString("cpth"));
									tv_process_SL.setText("批次数量：" + lcgdJson.getString("pchsl"));
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
									for (int i = 0; i < lcgdMxbJsonArr.length(); i++) {
										JSONObject lcgdmxbJson = lcgdMxbJsonArr.getJSONObject(i);
										MesLcgdMxbEntity dataBean = new MesLcgdMxbEntity();
										dataBean.setGz(lcgdmxbJson.getString("gz"));
										dataBean.setXh(lcgdmxbJson.getString("xh"));
//										dataBean.setZt(lcgdmxbJson.getString("zt"));
										dataBean.setZt_en(lcgdmxbJson.getString("zt_en"));
										String fps = lcgdmxbJson.getString("fps");
										fps = StringUtil.isEmpty(fps) ? "0" : fps;

										String hgs = lcgdmxbJson.getString("hgs");
										hgs = StringUtil.isEmpty(hgs) ? "0" : hgs;
										dataBean.setFps(new BigDecimal(fps));
										dataBean.setHgs(new BigDecimal(hgs));
										dataBean.setJgz(lcgdmxbJson.getString("jgz"));
										dataBean.setJyy(lcgdmxbJson.getString("jyy"));
										dataBean.setJgnr(lcgdmxbJson.getString("jgnr"));
										dataBean.setJyy(lcgdmxbJson.getString("jyy"));
										try {
											dataBean.setJyrq(sdf.parse(lcgdmxbJson.getString("jyrq")));
										} catch (ParseException e) {
											dataBean.setJyrq(null);
										}
										try {
											dataBean.setJsrq(sdf.parse(lcgdmxbJson.getString("jsrq")));
										} catch (ParseException e) {
											dataBean.setJsrq(null);
										}
										mesLcgdMxbEntityList.add(dataBean);
									}
									adapterListViewWMSProcess = new AdapterListViewWMSProcess(WMSRfidProcessActivity.this, mesLcgdMxbEntityList);
									wms_rfid_process_list.setAdapter(adapterListViewWMSProcess);
									adapterListViewWMSProcess.notifyDataSetChanged();

								} catch (final JSONException e) {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(WMSRfidProcessActivity.this, "获取接口数据失败，请联系管理员" + e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									});
								}

							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidProcessActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidProcessActivity.this, "获取接口数据失败，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();

	}

	/**
	 * 初始读写器设备
	 */
	private void initDev() {
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep, 1);
		soundPoolerr = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//soundPoolerr.load(this, R.raw.alarm, 1);
		Application app = WMSRfidProcessActivity.this.getApplication();
		myapp = (MyApplication) app;
		/*manager = myapp.manager;
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
	 * 按下事件
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

	/**
	 * 读卡
	 */
	private void readTidByMobileDataTerminal() {
		try {
			tid = RfidRead.getTidValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSRfidProcessActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
				//getBqhByTid(tid);
				initData();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		finally {

		}
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(keyReceiver);
		super.onDestroy();
	}

}
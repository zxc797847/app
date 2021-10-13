package com.avic.demo.sebeiglzx.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.adapter.WMSRFIDCheckReadTableAdapter;
import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.listener.PostHttp;
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 废品审核
 *
 */
@SuppressLint("SimpleDateFormat")
public class WMSRfidWasteCheckActivity extends Activity implements OnClickListener {
	// 返回按钮
	private ImageView iv_back;
	private TextView tv_check_read_card;
	private static TextView tv_check_BQH;
	private static TextView tv_check_CPMC;
	private static TextView tv_check_SL;
	private static TextView tv_check_JGNR;
	private static TextView tv_check_CPGG;
	private static TextView tv_check_PCH;
	private static EditText et_HGSL;
	private TextView tv_init_add;
	private TextView tv_save;
	private TextView tv_init_delete;
	private static TextView tv_display_PCH;
	private static TextView tv_display_ZSL;
	private static TextView tv_display_HGSL;
	private static TextView tv_display_BHGSL;
	private static List<InitProRfidFkbBean> proRfidFkbBeanList;
	private static ListView tableListView;
	private CheckBox ck_XZ;
	static WMSRFIDCheckReadTableAdapter adapter;
	private static InitProRfidFkbBean proRfidFkbBean;
	boolean isrun, issound = true;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	MyApplication myapp;
	private KeyReceiver keyReceiver;
	String tid = "";
	String epc = "";
	private static ProgressDialog progressDialog;
	private static TextView et_DBH;
	private static LinearLayout LinearLayout_BH;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_rfid_waste_check);
		initView();
	}

	private void initView() {
		Application app = getApplication();
		try {
			myapp = (MyApplication) app;
		} catch (Exception e2) {
		}
		// 初始化读写器
		initDev();
		proRfidFkbBeanList = new ArrayList<InitProRfidFkbBean>();
		adapter = new WMSRFIDCheckReadTableAdapter(WMSRfidWasteCheckActivity.this, proRfidFkbBeanList);
		tableListView = (ListView) findViewById(R.id.wms_check_read_list);
		et_HGSL = (EditText) findViewById(R.id.et_HGSL);
		tv_check_BQH = (TextView) findViewById(R.id.tv_check_BQH);
		tv_check_CPMC = (TextView) findViewById(R.id.tv_check_CPMC);
		tv_check_SL = (TextView) findViewById(R.id.tv_check_SL);
		tv_check_JGNR=(TextView) findViewById(R.id.tv_check_JGNR);
		//tv_check_CPGG = (TextView) findViewById(R.id.tv_check_CPGG);
		tv_check_PCH = (TextView) findViewById(R.id.tv_check_PCH);
		et_HGSL = (EditText) findViewById(R.id.et_HGSL);
		tv_display_PCH = (TextView) findViewById(R.id.tv_display_PCH);
		tv_display_ZSL = (TextView) findViewById(R.id.tv_display_ZSL);
		tv_display_HGSL = (TextView) findViewById(R.id.tv_display_HGSL);
		tv_display_BHGSL = (TextView) findViewById(R.id.tv_display_BHGSL);
		// 读卡
		tv_check_read_card = (TextView) findViewById(R.id.tv_check_read_card);
		tv_check_read_card.setOnClickListener(this);
		// 返回
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		// 下一步
		tv_save = (TextView) findViewById(R.id.tv_save);
		tv_save.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();

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
			case R.id.tv_check_read_card:
				readTidByMobileDataTerminal();
				break;


			/**
			 * 提交
			 */
			case R.id.tv_save:
				if(StringUtil.isEmpty(tid)){
					Toast.makeText(WMSRfidWasteCheckActivity.this, "请读卡获取废品单信息！", Toast.LENGTH_SHORT).show();
					return;
				}
				if("标签号：".equals(tv_check_BQH.getText().toString())){
					Toast.makeText(WMSRfidWasteCheckActivity.this, "请读卡获取废品单信息！", Toast.LENGTH_SHORT).show();
					return;
				}
				wasteCheck(tid);
				break;

			default:
				break;
		}

	}

	/**
	 * 通过tid 获取产品信息
	 *
	 * @param
	 */
	private void getProDataByTid(final String tid) {
		progressDialog = ProgressDialog.show(WMSRfidWasteCheckActivity.this, "请稍等", "正在获取RFID卡信息...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidWasteCheckActivity.this) + "/appscjController.do?getWasteInfoByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidWasteCheckActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();

						}
					});
					return;
				} finally {
					progressDialog.cancel();
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					boolean success = jsonObj.getBoolean("success");
					final String msg = jsonObj.getString("msg");
					if (success) {
						JSONObject attributes = jsonObj.getJSONObject("attributes");
						final JSONObject json = attributes.getJSONObject("data");
						final JSONObject mesLcgdMxbJson = attributes.getJSONObject("mesFpglbEntity");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {

									String CPBM = json.getString("cpbm");
									String CPMC = json.getString("cpmc");
									int SL = mesLcgdMxbJson.getInt("bfs");
									String BQH = json.getString("bqh");
									//String LPH = json.getString("lph");
									String PCH = json.getString("pch");
									/*String CPXH = json.getString("cpxh");
									String CPGG = json.getString("cpgg");
									String rfidTid = json.getString("rfidTid");
									String jgz = mesLcgdMxbJson.getString("jgz");
									String jgzgh = mesLcgdMxbJson.getString("jgzgh");*/
									String dqgx=mesLcgdMxbJson.getString("gxmc");


									tv_check_BQH.setText("标签号：" + BQH);
									tv_check_CPMC.setText("废品名称：" + CPMC);
									tv_check_SL.setText("报废数：" + SL);
									//tv_check_CPGG.setText("产品规格：" + CPGG);
									tv_check_JGNR.setText("报废工序：" + dqgx);
									tv_check_PCH.setText("批次号：" + PCH);

								} catch (JSONException e) {

								}
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidWasteCheckActivity.this, msg, Toast.LENGTH_SHORT).show();

							}
						});
					}

				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

						}
					});
				} finally {
					progressDialog.cancel();
				}
			}
		}).start();

	}

	private void wasteCheck(final String tid) {
		progressDialog = ProgressDialog.show(WMSRfidWasteCheckActivity.this, "请稍等", "正在提交审核...", true, false);
		progressDialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {

				String url = URLHelper.getBaseUrl(WMSRfidWasteCheckActivity.this) + "/appscjController.do?wasteCheck";
				String result = "";
				try {
					JSONArray jsonArr = new JSONArray();
					if (StringUtil.isNotEmpty(tid)) {
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("tid", tid);
						result = PostHttp.RequstPostHttpNew(WMSRfidWasteCheckActivity.this, url, paramsMap);
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidWasteCheckActivity.this, "无数据提交", Toast.LENGTH_SHORT).show();
							}
						});
					}
					JSONObject jsonObj = new JSONObject(result);
					boolean success = (Boolean) jsonObj.get("success");
					final String msg = (String) jsonObj.get("msg");
					if (success) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidWasteCheckActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
								refreshData();
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidWasteCheckActivity.this, "提交失败！原因："+msg, Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (final Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidWasteCheckActivity.this, "提交异常", Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					progressDialog.cancel();
				}
			}
		}).start();
	}

	/**
	 * 刷新数据
	 */
	public void refreshData() {

		tv_check_BQH.setText("标签号：");
		tv_check_CPMC.setText("产品名称：");
		tv_check_SL.setText("数量：");
		//tv_check_CPGG.setText("产品规格：");
		tv_check_PCH.setText("批次号：");
		String PCH = "";
		tv_check_JGNR.setText("当前工序：");
	}

	/**
	 * 刷新数据
	 */
	public static void cleanData() {

		tv_check_BQH.setText("标签号：");
		tv_check_CPMC.setText("产品名称：");
		tv_check_SL.setText("报废数：");
		//tv_check_CPGG.setText("产品规格：");
		tv_check_PCH.setText("批次号：");

	}

	/**
	 * 初始读写器设备
	 */
	private void initDev() {
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep, 1);
		soundPoolerr = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//soundPoolerr.load(this, R.raw.alarm, 1);
		/*Application app = WMSRfidCheckReadActivity.this.getApplication();
		myapp = (MyApplication) app;*/
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
						Toast.makeText(WMSRfidWasteCheckActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
				getProDataByTid(tid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		finally {

		}
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
						// readTidByMobileDataTerminal();
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
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

	@Override
	protected void onDestroy() {
		unregisterReceiver(keyReceiver);
		super.onDestroy();
	}

}
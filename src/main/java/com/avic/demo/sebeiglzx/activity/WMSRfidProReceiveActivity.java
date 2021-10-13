package com.avic.demo.sebeiglzx.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.adapter.WMSRFIDProReceiveTableAdapter;
import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.listener.PostHttp;
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;


/**
 * 派工
 *
 */
@SuppressLint("SimpleDateFormat")
public class WMSRfidProReceiveActivity extends Activity implements OnClickListener {
	// 返回按钮
	private ImageView iv_back;
	String tid = "";
	String epc = "";
	String yggh_tid = "";
	String yggh_epc = "";
	boolean isrun, issound = true;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	MyApplication myapp;
	private KeyReceiver keyReceiver;
	String hjh = "";
	private EditText et_PGYGGH;
	private TextView tv_receive_read_PGYGGH;
	private TextView tv_receive_read_card;
	private TextView tv_receive_CPMC;
	private TextView tv_receive_BQH;
	//	private TextView tv_receive_CPGG;
	private TextView tv_receive_PCH;
	private TextView tv_display_PCH;
	private TextView tv_display_ZSL;
	private TextView tv_receive_save;
	private TextView tv_receive_delete;
	private TextView tv_receive_proccess_see;

	private ListView wms_receive_read_list;
	private List<InitProRfidFkbBean> proRfidFkbBeanList;
	private ListView tableListView;
	WMSRFIDProReceiveTableAdapter adapter;
	String[] proDataArr;
	String[] processTempDataArr;
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_rfid_pro_receive);
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
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		et_PGYGGH = (EditText) findViewById(R.id.et_PGYGGH);
		tv_receive_read_PGYGGH = (TextView) findViewById(R.id.tv_receive_read_PGYGGH);
		tv_receive_read_PGYGGH.setOnClickListener(this);
		tv_receive_read_card = (TextView) findViewById(R.id.tv_receive_read_card);
		tv_receive_read_card.setOnClickListener(this);
		tv_receive_delete = (TextView) findViewById(R.id.tv_receive_delete);
		tv_receive_delete.setOnClickListener(this);
		tv_receive_proccess_see = (TextView) findViewById(R.id.tv_receive_proccess_see);
		tv_receive_proccess_see.setOnClickListener(this);
		tv_receive_CPMC = (TextView) findViewById(R.id.tv_receive_CPMC);
		//	tv_receive_CPGG = (TextView) findViewById(R.id.tv_receive_CPGG);
		tv_receive_PCH = (TextView) findViewById(R.id.tv_receive_PCH);
		wms_receive_read_list = (ListView) findViewById(R.id.wms_receive_read_list);
		tv_display_PCH = (TextView) findViewById(R.id.tv_display_PCH);
		tv_display_ZSL = (TextView) findViewById(R.id.tv_display_ZSL);
		tv_receive_BQH = (TextView) findViewById(R.id.tv_receive_BQH);
		tv_receive_save = (TextView) findViewById(R.id.tv_receive_save);
		tv_receive_save.setOnClickListener(this);
		tableListView = (ListView) findViewById(R.id.wms_receive_read_list);

		proRfidFkbBeanList = new ArrayList<InitProRfidFkbBean>();
		adapter = new WMSRFIDProReceiveTableAdapter(WMSRfidProReceiveActivity.this, proRfidFkbBeanList);

		// 设置表格标题的背景颜色
		ViewGroup tableTitle = (ViewGroup) findViewById(R.id.wms_receive_table_title);
		tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
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
			 * 获取派工员工工号
			 */
			case R.id.tv_receive_read_PGYGGH:
				readEmpNoByMobileDataTerminal();
				break;
			/**
			 * 删除
			 */
			case R.id.tv_receive_delete:
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
				tv_display_ZSL.setText("总数量：" + totalNum1.toString());
				adapter.notifyDataSetChanged();
				break;

			/**
			 * 读取TID获取产品信息
			 */
			case R.id.tv_receive_read_card:
				String PGYGGH = et_PGYGGH.getText().toString();
				if (StringUtil.isEmpty(PGYGGH)) {
					Toast.makeText(WMSRfidProReceiveActivity.this, "请获取派工员工工号！", Toast.LENGTH_SHORT).show();
					return;
				}
				tid = "";
				readTidByMobileDataTerminal();
				break;
			/**
			 * 流程进度
			 */
			case R.id.tv_receive_proccess_see:
				if (StringUtil.isEmpty(tid)) {
					Toast.makeText(WMSRfidProReceiveActivity.this, "请读取产品信息！", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent1 = new Intent(WMSRfidProReceiveActivity.this, WMSRfidProcessActivity.class);
				intent1.putExtra("tid", tid);
				startActivity(intent1);
				break;
			/**
			 * 提交数据
			 */
			case R.id.tv_receive_save:
				submitData();

				break;

			default:
				break;
		}
	}

	/**
	 * 提交数据
	 *
	 */
	private void submitData() {
		progressDialog = ProgressDialog.show(WMSRfidProReceiveActivity.this, "请稍等", "正在提交数据...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidProReceiveActivity.this) + "/appscjController.do?dispatching";
				String result = "";
				try {

					JSONArray jsonArr = new JSONArray();
					if (proRfidFkbBeanList.size() > 0) {
						InitProRfidFkbBean proRfidFkbBean = proRfidFkbBeanList.get(0);
						for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("rfid_tid", proRfidFkbBeanList.get(i).getRfidTid());
							jsonArr.put(jsonObj);
						}
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("rfidTidList", jsonArr.toString());
						paramsMap.put("bqh", et_PGYGGH.getText().toString());
						result = PostHttp.RequstPostHttpNew(WMSRfidProReceiveActivity.this, url, paramsMap);
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {

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
								Toast.makeText(WMSRfidProReceiveActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
								clearData();

							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {

							}
						});
					}
				} catch (final Exception e1) {
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

	/**
	 * 清空数据
	 */
	public void clearData() {
		et_PGYGGH.setText("");
		proRfidFkbBeanList.clear();
		adapter.notifyDataSetChanged();
		tid = "";
		tv_receive_BQH.setText("标签号：");
		tv_receive_CPMC.setText("产品名称：");
//		tv_receive_CPGG.setText("产品规格：");
		tv_receive_PCH.setText("批次号：");
		tv_display_ZSL.setText("总数量：0");
		tv_display_PCH.setText("批次号：");

	}

	/**
	 * 初始读写器设备
	 */
	private void initDev() {
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep, 1);
		soundPoolerr = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//soundPoolerr.load(this, R.raw.alarm, 1);
		/*Application app = WMSRfidProReceiveActivity.this.getApplication();
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
	 * 读取员工工号
	 */
	private void readEmpNoByMobileDataTerminal() {

		try {
			String tid = RfidRead.getTidValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSRfidProReceiveActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
				getUserInfoByTid(tid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		finally {

		}
	}

	/**
	 * 读卡
	 */
	private void readTidByMobileDataTerminal() {

		try {
			String tid = RfidRead.getTidValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSRfidProReceiveActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
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

	/**
	 * 通过tid 获取产品信息
	 *
	 * @param
	 */
	private void getProDataByTid(final String tid) {
		progressDialog = ProgressDialog.show(WMSRfidProReceiveActivity.this, "请稍等", "正在加载数据...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidProReceiveActivity.this) + "/appscjController.do?getMesRfidFkbOnReceiveByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							soundPoolerr.play(1, 1, 1, 0, 0, 1);
							Toast.makeText(WMSRfidProReceiveActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									String CPBM = json.getString("cpbm");
									String CPMC = json.getString("cpmc");
									Double SL = json.getDouble("sl");
									String BQH = json.getString("bqh");
									String LPH = json.getString("lph");
									String PCH = json.getString("pch");
									String CPXH = json.getString("cpxh");
									String CPGG = json.getString("cpgg");
									String rfidTid = json.getString("rfidTid");
									/**
									 * 判断批次号是否一样
									 */
									if (proRfidFkbBeanList.size() > 0) {
										if (!proRfidFkbBeanList.get(0).getPch().equals(PCH)) {

											return;
										} else if (proRfidFkbBeanList.get(0).getBqh().equals(BQH)) {


											return;
										}
									}
									InitProRfidFkbBean proRfidFkbBean = new InitProRfidFkbBean();
									proRfidFkbBean.setCpbm(CPBM);
									proRfidFkbBean.setCpmc(CPMC);
									proRfidFkbBean.setSl(new BigDecimal(SL));
									proRfidFkbBean.setBqh(BQH);
									proRfidFkbBean.setIsCheck(false);
									proRfidFkbBean.setLph(LPH);
									proRfidFkbBean.setPch(PCH);
									proRfidFkbBean.setCpxh(CPXH);
									proRfidFkbBean.setCpgg(CPGG);
									proRfidFkbBean.setRfidTid(rfidTid);
									proRfidFkbBeanList.add(proRfidFkbBean);

									adapter = new WMSRFIDProReceiveTableAdapter(WMSRfidProReceiveActivity.this, proRfidFkbBeanList);
									tableListView.setAdapter(adapter);
									BigDecimal totalNum = new BigDecimal(0);
									for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
										totalNum = totalNum.add(proRfidFkbBeanList.get(i).getSl());
									}
									tv_receive_BQH.setText("标签号：" + BQH);
									tv_receive_CPMC.setText("产品名称：" + CPMC);
									//				tv_receive_CPGG.setText("产品规格：" + CPGG);
									tv_receive_PCH.setText("批次号：" + PCH);
									tv_display_ZSL.setText("总数量：" + totalNum.toString());
									tv_display_PCH.setText("批次号：" + PCH);
									soundPool.play(1, 1, 1, 0, 0, 1);
								} catch (final JSONException e) {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {

										}
									});
								}

							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {

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

	/***
	 * 通过TID获取员工信息
	 *
	 * @param
	 */
	private void getUserInfoByTid(final String tid) {
		progressDialog = ProgressDialog.show(WMSRfidProReceiveActivity.this, "请稍等", "正在加载数据...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidProReceiveActivity.this) + "/appscjController.do?getTSUserEmpNoByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							soundPoolerr.play(1, 1, 1, 0, 0, 1);
							Toast.makeText(WMSRfidProReceiveActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
						final String empNo = attributes.getString("empNo");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								soundPool.play(1, 1, 1, 0, 0, 1);
								et_PGYGGH.setText(empNo);
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								soundPoolerr.play(1, 1, 1, 0, 0, 1);

							}
						});
					}

				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							soundPoolerr.play(1, 1, 1, 0, 0, 1);

						}
					});
				} finally {
					progressDialog.cancel();
				}
			}
		}).start();

	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(keyReceiver);
		super.onDestroy();
	}

}
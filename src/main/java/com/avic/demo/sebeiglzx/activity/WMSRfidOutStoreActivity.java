package com.avic.demo.sebeiglzx.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

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
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.adapter.WMSRFIDOutStoreTableAdapter;
import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.listener.PostHttp;
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;

/**
 * 出库
 *
 */
@SuppressLint("SimpleDateFormat")
public class WMSRfidOutStoreActivity extends Activity implements OnClickListener {
	// 返回按钮
	private ImageView iv_back;
	private TextView tv_out_store_scan;
	private TextView tv_out_store_read_card_add;
	private TextView tv_out_store_read_card_delete;
	private TextView tv_out_store_BQH;
	private TextView tv_out_store_CPMC;
	private TextView tv_out_store_SL;
	private EditText et_out_store_CKDH;
	private TextView tv_out_store_CPGG;
	private TextView tv_out_store_PCH;
	private TextView tv_out_store_next;
	private TextView tv_out_store_display_CKZSL;
	private TextView tv_out_store_display_DQSL;
	private List<InitProRfidFkbBean> proRfidFkbBeanList;
	private ListView tableListView;
	private CheckBox ck_XZ;
	WMSRFIDOutStoreTableAdapter adapter;
	private InitProRfidFkbBean proRfidFkbBean;
	boolean isrun, issound = true;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	MyApplication myapp;
	private KeyReceiver keyReceiver;
	String tid = "";
	String epc = "";
	private static ProgressDialog progressDialog;
	private BigDecimal CKZSL = new BigDecimal(0);
	private String OrderNo = "";
	private String CK_ID = "";
	private String CPBM = "";
	private String PCH = "";
	private String tidsStr = "";
	private String ckdh = "";
	private List<Map<String, String>> listMap;

	private boolean mIsContinuous = false;

	private Timer scanTimer = null;
	private Timer timer;

	/**
	 * Whether to use GBK character encoding set for transcoding
	 */
	private boolean mGbkFlag = false;

	private boolean mRegisterFlag = false;
	private ScanUtil scanUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_rfid_out_store);
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
		adapter = new WMSRFIDOutStoreTableAdapter(WMSRfidOutStoreActivity.this, proRfidFkbBeanList);
		tableListView = (ListView) findViewById(R.id.wms_out_store_list);
		tv_out_store_BQH = (TextView) findViewById(R.id.tv_out_store_BQH);
		tv_out_store_CPMC = (TextView) findViewById(R.id.tv_out_store_CPMC);
		tv_out_store_SL = (TextView) findViewById(R.id.tv_out_store_SL);
		tv_out_store_CPGG = (TextView) findViewById(R.id.tv_out_store_CPGG);
		tv_out_store_PCH = (TextView) findViewById(R.id.tv_out_store_PCH);
		et_out_store_CKDH = (EditText) findViewById(R.id.et_out_store_CKDH);
		tv_out_store_display_DQSL = (TextView) findViewById(R.id.tv_out_store_display_DQSL);
		tv_out_store_display_CKZSL = (TextView) findViewById(R.id.tv_out_store_display_CKZSL);
		// 设置表格标题的背景颜色
		ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
		tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
		// 读卡获取出库单
		tv_out_store_scan = (TextView) findViewById(R.id.tv_out_store_scan);
		tv_out_store_scan.setOnClickListener(this);
		// 扫描
		tv_out_store_read_card_add = (TextView) findViewById(R.id.tv_out_store_read_card_add);
		tv_out_store_read_card_add.setOnClickListener(this);
		// 删除
		tv_out_store_read_card_delete = (TextView) findViewById(R.id.tv_out_store_read_card_delete);
		tv_out_store_read_card_delete.setOnClickListener(this);
		// 返回
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		// 提交
		tv_out_store_next = (TextView) findViewById(R.id.tv_out_store_save);
		tv_out_store_next.setOnClickListener(this);

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
			 * 扫描
			 */
			case R.id.tv_out_store_scan:
				//scanThread.scan();
				scanUtil.scan();
				break;
			/**
			 * 读卡新增
			 */
			case R.id.tv_out_store_read_card_add:
				readTidByMobileDataTerminal();
				break;
			/**
			 * 删除
			 */
			case R.id.tv_out_store_read_card_delete:
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
				tv_out_store_display_DQSL.setText("当前数量：" + totalNum1.toString());
				adapter.notifyDataSetChanged();
				break;

			/**
			 * 提交
			 */
			case R.id.tv_out_store_save:

				if (proRfidFkbBeanList.size() <= 0) {
					Toast.makeText(WMSRfidOutStoreActivity.this, "无数据出库，请添加数据后，在点击【提交】！", Toast.LENGTH_SHORT).show();
					return;
				} else {
					BigDecimal totalNum = new BigDecimal(0);
					tidsStr = "";
					for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
						InitProRfidFkbBean proRfidFkbBean = proRfidFkbBeanList.get(i);
						totalNum = totalNum.add(proRfidFkbBean.getSl());
						tidsStr += "'" + proRfidFkbBean.getRfidTid() + "',";
					}
					tidsStr = StringUtil.isEmpty(tidsStr) ? "" : tidsStr.substring(0, tidsStr.length() - 1);
				/*	if (totalNum.compareTo(CKZSL) != 0) {
						Toast.makeText(WMSRfidOutStoreActivity.this, "发货总数量与当前数量不一致！", Toast.LENGTH_SHORT).show();
						//return;
					} else {*/
						new Thread(new Runnable() {
							@Override
							public void run() {
								String url = URLHelper.getBaseUrl(WMSRfidOutStoreActivity.this) + "/appscjController.do?fhCheck";
								String result = "";
								try {
									String wlNo = et_out_store_CKDH.getText().toString();
									if(StringUtil.isEmpty(wlNo)){
										Toast.makeText(WMSRfidOutStoreActivity.this, "请填写物流单号！", Toast.LENGTH_SHORT).show();
										return;
									}
									Map<String, String> paramsMap = new HashMap<String, String>();
									paramsMap.put("wlNo", wlNo);
									paramsMap.put("orderNo", OrderNo);
									paramsMap.put("tids", tidsStr);
									result = PostHttp.RequstPostHttpNew(WMSRfidOutStoreActivity.this, url, paramsMap);
									JSONObject jsonObj = new JSONObject(result);
									boolean success = (Boolean) jsonObj.get("success");
									final String msg = (String) jsonObj.get("msg");
									if (success) {
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(WMSRfidOutStoreActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
												cleanData();
											}
										});
									} else {
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Toast.makeText(WMSRfidOutStoreActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
											}
										});
									}
								} catch (final Exception e1) {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											// Toast.makeText(WMSRfidOutStoreActivity.this, "" + e1.getMessage(), Toast.LENGTH_SHORT).show();

										}
									});
								}
							}
						}).start();
					//}
				}
				break;

			default:
				break;
		}

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			byte[] data = intent.getByteArrayExtra("data");
			if (data != null) {
//                String barcode = Tools.Bytes2HexString(data, data.length);
				String barcode = new String(data);
                String [] bbarcode = barcode.split("[=]");
				et_out_store_CKDH.setText(bbarcode[1]);
			}

		}
	};

	/***
	 * 扫描获取出库单信息
	 */
	private void getMesCkdInfoByscanCKDH(final String ckdh) {
		progressDialog = ProgressDialog.show(WMSRfidOutStoreActivity.this, "请稍等", "正在获取出库单信息...", true, false);
		cleanData();
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidOutStoreActivity.this) + "/appscjController.do?getMesCkdInfo&ckdh=" + ckdh;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

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
						final JSONObject data = attributes.getJSONObject("data");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									tv_out_store_BQH.setText("出库单号：" + data.getString("ckdh"));
									tv_out_store_CPMC.setText("产品名称：" + data.getString("cpmc"));
									tv_out_store_SL.setText("数量：" + data.getString("cksl"));
									tv_out_store_CPGG.setText("产品规格：" + data.getString("cpgg"));
									tv_out_store_PCH.setText("批次号：" + data.getString("pch"));
									CKZSL = new BigDecimal(data.getString("cksl"));

									OrderNo = data.getString("ckdh");
									CPBM = data.getString("cpbm");
									PCH = data.getString("pch");

									tv_out_store_display_CKZSL.setText("出库总数量：" + data.getString("cksl"));
									tv_out_store_display_DQSL.setText("当前数量：0");

								} catch (JSONException e) {
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

	/**
	 * 通过tid 获取产品信息
	 *
	 * @param
	 */
	private void getProDataByTid(final String tid) {
		progressDialog = ProgressDialog.show(WMSRfidOutStoreActivity.this, "请稍等", "正在获取RFID卡信息...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidOutStoreActivity.this) + "/appscjController.do?getMesRfidFkbByTid&key=rfidTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							soundPoolerr.play(1, 1, 1, 0, 0, 1);
							Toast.makeText(WMSRfidOutStoreActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
						final JSONObject mesRfidFkbJson = attributes.getJSONObject("data");
						final JSONObject orderJson = attributes.getJSONObject("orderData");
						String cpmc = mesRfidFkbJson.getString("cpmc");
						String bqh = mesRfidFkbJson.getString("bqh");
						String sl = mesRfidFkbJson.getString("sl");
						String cpbm = mesRfidFkbJson.getString("cpbm");
						String pch = mesRfidFkbJson.getString("pch");
						/*if (proRfidFkbBeanList.size() > 0) {

							for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
								if (!proRfidFkbBeanList.get(i).getBqh().equals(bqh)) {
									soundPoolerr.play(1, 1, 1, 0, 0, 1);
								}
							}
						}else{

							tv_out_store_BQH.setText("订单号：" + orderJson.getString("orderNo"));
							tv_out_store_CPMC.setText("产品名称：" + orderJson.getString("productName"));
							tv_out_store_SL.setText("数量：" + orderJson.getString("number"));
							tv_out_store_CPGG.setText("产品规格：" + orderJson.getString("product_material_gg"));
							tv_out_store_PCH.setText("批次号：" + orderJson.getString("batch_number"));
							CKZSL = new BigDecimal(orderJson.getString("number"));

							OrderNo = orderJson.getString("orderNo");
							CPBM = orderJson.getString("productCode");
							PCH = pch;

							tv_out_store_display_CKZSL.setText("发货总数量：" + orderJson.getString("number"));
						}

						*//**
						 * RFID卡的产品编码和批次号必须与出库单的产品编码和批次号一致。
						 *//*

						if (CPBM.equals(cpbm) && PCH.equals(pch)) {
							InitProRfidFkbBean proRfidFkbBean = new InitProRfidFkbBean();
							proRfidFkbBean.setCpbm(cpbm);
							proRfidFkbBean.setCpmc(cpmc);
							proRfidFkbBean.setSl(new BigDecimal(sl));
							proRfidFkbBean.setBqh(bqh);
							proRfidFkbBean.setRfidTid(tid);
							proRfidFkbBean.setIsCheck(false);
							proRfidFkbBean.setOrderNo(orderJson.getString("orderNo"));
							proRfidFkbBeanList.add(proRfidFkbBean);

							adapter = new WMSRFIDOutStoreTableAdapter(WMSRfidOutStoreActivity.this, proRfidFkbBeanList);
							tableListView.setAdapter(adapter);
							soundPool.play(1, 1, 1, 0, 0, 1);

							BigDecimal totalNum1 = new BigDecimal(0);
							for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
								totalNum1 = totalNum1.add(proRfidFkbBeanList.get(i).getSl());
							}
							tv_out_store_display_DQSL.setText("当前数量：" + totalNum1.toString());

						} else {
							soundPoolerr.play(1, 1, 1, 0, 0, 1);
							Toast.makeText(WMSRfidOutStoreActivity.this, "该卡", Toast.LENGTH_SHORT).show();
						}*/



						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {


									/**
									 * 判断批次号是否一样
									 */
									if (proRfidFkbBeanList.size() > 0) {
										String bqh = mesRfidFkbJson.getString("bqh");
										for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
											if (!proRfidFkbBeanList.get(i).getBqh().equals(bqh)) {
												soundPoolerr.play(1, 1, 1, 0, 0, 1);
											}
										}
									}else{
										String cpmc = mesRfidFkbJson.getString("cpmc");
										String bqh = mesRfidFkbJson.getString("bqh");
										String sl = mesRfidFkbJson.getString("sl");
										String cpbm = mesRfidFkbJson.getString("cpbm");
										String pch = mesRfidFkbJson.getString("pch");
										tv_out_store_BQH.setText("订单号：" + orderJson.getString("orderNo"));
										tv_out_store_CPMC.setText("产品名称：" + orderJson.getString("productName"));
										tv_out_store_SL.setText("数量：" + orderJson.getString("number"));
										tv_out_store_CPGG.setText("产品规格：" + orderJson.getString("product_material_gg"));
										tv_out_store_PCH.setText("批次号：" + orderJson.getString("batch_number"));
										CKZSL = new BigDecimal(orderJson.getString("number"));

										OrderNo = orderJson.getString("orderNo");
										CPBM = orderJson.getString("productCode");
										PCH = pch;

										tv_out_store_display_CKZSL.setText("发货总数量：" + orderJson.getString("number"));
									}

									/**
									 * RFID卡的产品编码和批次号必须与出库单的产品编码和批次号一致。
									 */
									String cpmc = mesRfidFkbJson.getString("cpmc");
									String bqh = mesRfidFkbJson.getString("bqh");
									String sl = mesRfidFkbJson.getString("sl");
									String cpbm = mesRfidFkbJson.getString("cpbm");
									String pch = mesRfidFkbJson.getString("pch");
									if (CPBM.equals(cpbm) && PCH.equals(pch)) {
										InitProRfidFkbBean proRfidFkbBean = new InitProRfidFkbBean();
										proRfidFkbBean.setCpbm(cpbm);
										proRfidFkbBean.setCpmc(cpmc);
										proRfidFkbBean.setSl(new BigDecimal(sl));
										proRfidFkbBean.setBqh(bqh);
										proRfidFkbBean.setRfidTid(tid);
										proRfidFkbBean.setIsCheck(false);
										proRfidFkbBean.setOrderNo(orderJson.getString("orderNo"));
										proRfidFkbBeanList.add(proRfidFkbBean);

										adapter = new WMSRFIDOutStoreTableAdapter(WMSRfidOutStoreActivity.this, proRfidFkbBeanList);
										tableListView.setAdapter(adapter);
										soundPool.play(1, 1, 1, 0, 0, 1);

										BigDecimal totalNum1 = new BigDecimal(0);
										for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
											totalNum1 = totalNum1.add(proRfidFkbBeanList.get(i).getSl());
										}
										tv_out_store_display_DQSL.setText("当前数量：" + totalNum1.toString());

									} else {
										soundPoolerr.play(1, 1, 1, 0, 0, 1);
										Toast.makeText(WMSRfidOutStoreActivity.this, "该卡", Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									soundPoolerr.play(1, 1, 1, 0, 0, 1);

								}

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

				} catch (JSONException e) {
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
	public void cleanData() {

		tv_out_store_BQH.setText("出库单号：");
		tv_out_store_CPMC.setText("产品名称：");
		tv_out_store_SL.setText("数量：");
		tv_out_store_CPGG.setText("产品规格：");
		tv_out_store_PCH.setText("批次号：");
		tv_out_store_display_CKZSL.setText("出库总数量：");
		tv_out_store_display_DQSL.setText("当前数量：");
		proRfidFkbBean = null;

		adapter.notifyDataSetChanged();
	}

	/**
	 * 刷新数据
	 */
	public void refreshData() {

		BigDecimal totalNum = new BigDecimal(0);

		if (proRfidFkbBeanList.size() > 0) {
			for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
				InitProRfidFkbBean proRfidFkbBean = proRfidFkbBeanList.get(i);
				if (proRfidFkbBean.getIsCheck()) {
					totalNum = totalNum.add(proRfidFkbBean.getSl());
				}
			}
			tv_out_store_display_DQSL.setText("当前数量：" + totalNum);
		}
		proRfidFkbBean = null;

		adapter.notifyDataSetChanged();
	}

	/**
	 * 初始读写器设备
	 */
	private void initDev() {
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep, 1);
		soundPoolerr = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		//soundPoolerr.load(this, R.raw.alarm, 1);
		/*Application app = WMSRfidOutStoreActivity.this.getApplication();
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

		//keyReceiver = new KeyReceiver();




		IntentFilter filter = new IntentFilter();
		filter.addAction("com.rfid.SCAN");
		registerReceiver(receiver, filter);
		IntentFilter batteryfilter = new IntentFilter();
		batteryfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryReceiver, batteryfilter);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.rfid.FUN_KEY");
		registerReceiver(keyReceiver, intentFilter);

		// 初始二维码
		/*try {
			scanThread = new ScanThread(mHandler);
		} catch (Exception e) {
			// 出现异常
			Toast.makeText(getApplicationContext(), "serialport init fail", 0).show();
			return;
		}
		scanThread.start();*/
		// 注册按键广播接收者
		/*keyReceiver = new KeyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.rfid.FUN_KEY");
		filter.addAction("android.intent.action.FUN_KEY");
		registerReceiver(keyReceiver, filter);*/
		mRegisterFlag = true;

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
						Toast.makeText(WMSRfidOutStoreActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
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

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			/*if (msg.what == ScanThread.SCAN) {
				// String data = msg.getData().getString("data");
				byte[] dataBytes = msg.getData().getByteArray("dataBytes");
				if (dataBytes == null || dataBytes.length == 0) {
					if (mIsContinuous) {
						scanThread.scan();
					}
					return;
				}
				String data = "";
				if (mGbkFlag) {
					try {
						data = new String(dataBytes, 0, dataBytes.length, "GBK");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					data = new String(dataBytes, 0, dataBytes.length);
					et_out_store_CKDH.setText(data);
					getMesCkdInfoByscanCKDH(data);
				}
				if (data == null || data.equals("")) {
					if (mIsContinuous) {
						scanThread.scan();
					}
					return;
				}
				// Toast.makeText(getApplicationContext(), data, 0).show();

			}*/
		};
	};

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

	@Override
	protected void onDestroy() {
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
		/*unregisterReceiver(keyReceiver);*/
		super.onDestroy();
	/*	unregisterReceiver(receiver);
		unregisterReceiver(batteryReceiver);*/
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (scanUtil == null) {
			scanUtil = new ScanUtil(this);
			//we must set mode to 0 : BroadcastReceiver mode
			scanUtil.setScanMode(0);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (scanUtil != null) {
			scanUtil.setScanMode(1);
			scanUtil.close();
			scanUtil = null;
		}
	}
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			Log.e("batteryReceiver", "batteryReceiver level =  " + level);
		}
	};

}
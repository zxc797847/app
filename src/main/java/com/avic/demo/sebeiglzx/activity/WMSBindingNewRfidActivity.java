package com.avic.demo.sebeiglzx.activity;

import java.io.Serializable;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.adapter.WMSRFIDBindingTableAdapter;
import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.listener.PostHttp;
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;


/**
 * 绑定新卡
 *
 * @author JamesWu
 */
@SuppressLint("SimpleDateFormat")
public class WMSBindingNewRfidActivity extends Activity implements OnClickListener {
	// 返回按钮
	private ImageView iv_back;
	private static TextView tv_DBH_name;
	private static EditText et_DBH;
	private static TextView tv_query;
	private static TextView tv_LCGDH;
	private static TextView tv_binding_CPMC;
	private static TextView tv_binding_SL;
	private static TextView tv_binding_PCH;
	private static TextView tv_binding_DDH;
	private static TextView tv_RFID_NEW_name;
	private static EditText et_NEW_BQH;
	private static TextView tv_binding_rfid;
	private static TextView tv_binding_add;
	private static TextView tv_binding_delete;
	private static TextView tv_binding_save;
	private static List<InitProRfidFkbBean> proRfidFkbBeanList;
	private static ListView tableListView;
	static WMSRFIDBindingTableAdapter adapter;
	private static InitProRfidFkbBean proRfidFkbBean;
	boolean isrun, issound = true;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	MyApplication myapp;
	private KeyReceiver keyReceiver;
	String tid = "";
	String epc = "";
	private static ProgressDialog progressDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_rfid_binding_new);
		initView();
	}

	private void initView() {

		// 初始化读写器
		initDev();
		proRfidFkbBeanList = new ArrayList<InitProRfidFkbBean>();
		adapter = new WMSRFIDBindingTableAdapter(WMSBindingNewRfidActivity.this, proRfidFkbBeanList);
		tableListView = (ListView) findViewById(R.id.wms_bingding_list);
		tv_DBH_name = (TextView) findViewById(R.id.tv_DBH_name);
		tv_LCGDH = (TextView) findViewById(R.id.tv_LCGDH);
		tv_binding_CPMC = (TextView) findViewById(R.id.tv_binding_CPMC);
		tv_binding_SL=(TextView) findViewById(R.id.tv_binding_SL);
		tv_binding_PCH = (TextView) findViewById(R.id.tv_binding_PCH);
		tv_binding_DDH = (TextView) findViewById(R.id.tv_binding_DDH);
		tv_RFID_NEW_name = (TextView) findViewById(R.id.tv_RFID_NEW_name);
		et_NEW_BQH = (EditText) findViewById(R.id.et_NEW_BQH);
		et_DBH=(EditText) findViewById(R.id.et_DBH);
		// 设置表格标题的背景颜色
		ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
		tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
		// 读卡
		tv_binding_rfid = (TextView) findViewById(R.id.tv_binding_rfid);
		tv_binding_rfid.setOnClickListener(this);
		tv_binding_add = (TextView) findViewById(R.id.tv_binding_add);
		tv_binding_add.setOnClickListener(this);
		tv_binding_delete = (TextView) findViewById(R.id.tv_binding_delete);
		tv_binding_delete.setOnClickListener(this);
		// 返回
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		// 查询
		tv_query = (TextView) findViewById(R.id.tv_query);
		tv_query.setOnClickListener(this);
		// 提交
		tv_binding_save = (TextView) findViewById(R.id.tv_binding_save);
		tv_binding_save.setOnClickListener(this);

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
			case R.id.tv_binding_rfid:
				readTidByMobileDataTerminal();
				break;

			/**
			 * 添加
			 */
			case R.id.tv_binding_add:
				String BH = et_DBH.getText().toString().trim();
				String newBqh=et_NEW_BQH.getText().toString().trim();
				String ddh=tv_binding_DDH.getText().toString().trim();
				String subDdh=ddh.substring(0, ddh.indexOf("："));
				String str2=ddh.substring(subDdh.length()+1, ddh.length());
				String lcgdh=tv_LCGDH.getText().toString().trim();
				String lcgdh1=lcgdh.substring(0, lcgdh.indexOf("："));
				String lcgdh2=lcgdh.substring(lcgdh1.length()+1, lcgdh.length());

				//订单号：
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					if (proRfidFkbBeanList.get(i).getBqh().equals(newBqh)) {
						Toast.makeText(WMSBindingNewRfidActivity.this, "读取失败，原因：该标签号【" + newBqh + "】已存在，请换卡", Toast.LENGTH_SHORT).show();
						et_NEW_BQH.setText("");
						return;
					}
				}
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					if (proRfidFkbBeanList.get(i).getDbh().equals(BH)) {
						Toast.makeText(WMSBindingNewRfidActivity.this, "添加失败，原因：该标号【" + BH + "】已存在", Toast.LENGTH_SHORT).show();
						et_DBH.setText("");
						return;
					}
				}
				proRfidFkbBean = new InitProRfidFkbBean();
				proRfidFkbBean.setBqh(newBqh);
				proRfidFkbBean.setDbh(BH);
				proRfidFkbBean.setOrderNo(str2);
				proRfidFkbBean.setIsCheck(false);
				proRfidFkbBean.setBz(lcgdh2);
				proRfidFkbBeanList.add(proRfidFkbBean);
				adapter = new WMSRFIDBindingTableAdapter(WMSBindingNewRfidActivity.this, proRfidFkbBeanList);
				tableListView.setAdapter(adapter);
				refreshData();
				break;

			/**
			 * 删除
			 */
			case R.id.tv_binding_delete:
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					InitProRfidFkbBean proRfidFkbBean = proRfidFkbBeanList.get(i);
					if (proRfidFkbBean.getIsCheck()) {
						proRfidFkbBeanList.remove(i--);
					}
				}
				tableListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;

			/**
			 * 查询
			 */
			case R.id.tv_query:
				String DBH = et_DBH.getText().toString().trim();
				if ("".equals(DBH)||DBH==null) {
					Toast.makeText(WMSBindingNewRfidActivity.this, "请输入打标号！", Toast.LENGTH_SHORT).show();
					return;
				}else{
					selectLcgdByDbh(DBH);
				}
				break;
			/**
			 * 提交
			 */
			case R.id.tv_binding_save:
				progressDialog = ProgressDialog.show(WMSBindingNewRfidActivity.this, "请稍等", "正在提交...", true, false);
				progressDialog.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						String url = URLHelper.getBaseUrl(WMSBindingNewRfidActivity.this) + "/appscjController.do?bindingNewRfid";
						String result = "";
						try {
							JSONArray jsonArr = new JSONArray();
							if (proRfidFkbBeanList.size() > 0) {
								for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
									JSONObject jsonObj = new JSONObject();
									jsonObj.put("DDH", proRfidFkbBeanList.get(i).getOrderNo());
									jsonObj.put("BQH", proRfidFkbBeanList.get(i).getBqh());
									jsonObj.put("LCGDH", proRfidFkbBeanList.get(i).getBz());
									jsonArr.put(jsonObj);
								}
								Map<String, String> paramsMap = new HashMap<String, String>();
								paramsMap.put("data", jsonArr.toString());
								result = PostHttp.RequstPostHttpNew(WMSBindingNewRfidActivity.this, url, paramsMap);
							} else {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(WMSBindingNewRfidActivity.this, "无数据提交！", Toast.LENGTH_SHORT).show();
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
										Toast.makeText(WMSBindingNewRfidActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
										refreshData();
									}
								});
							} else {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(WMSBindingNewRfidActivity.this, "提交失败！原因："+msg, Toast.LENGTH_SHORT).show();
									}
								});
							}
						} catch (final Exception e1) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(WMSBindingNewRfidActivity.this, "提交异常！", Toast.LENGTH_SHORT).show();
								}
							});
						} finally {
							progressDialog.cancel();
						}
					}
				}).start();
				break;
			default:
				break;
		}

	}

	private void selectLcgdByDbh(final String dbh) {
		progressDialog = ProgressDialog.show(WMSBindingNewRfidActivity.this, "请稍等", "正在查询信息...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSBindingNewRfidActivity.this) + "/appscjController.do?getLcgdByDbh&dbh=" + dbh;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSBindingNewRfidActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				} finally {
					progressDialog.cancel();
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					boolean success = jsonObj.getBoolean("success");
					if(success){
						JSONObject attributes = jsonObj.getJSONObject("attributes");
						final JSONArray json1= attributes.getJSONArray("data");
						final JSONObject jsonObject= (JSONObject) json1.get(0);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									String lcgdh = jsonObject.getString("lcgdh");
									String CPMC = jsonObject.getString("cpmc");
									int SL = jsonObject.getInt("pchsl");
									String DDH = jsonObject.getString("order_no");
									String PCH = jsonObject.getString("pch");
									tv_LCGDH.setText("流程跟单号："+lcgdh);
									tv_binding_PCH.setText("批次号："+PCH);
									tv_binding_SL.setText("数量："+SL);
									tv_binding_CPMC.setText("产品名称："+CPMC);
									tv_binding_DDH.setText("订单号："+DDH);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}else{
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// Toast.makeText(WMSBindingNewRfidActivity.this, msg, Toast.LENGTH_SHORT).show();
								Toast.makeText(WMSBindingNewRfidActivity.this, "没有找到产品信息！", Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSBindingNewRfidActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
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
		progressDialog = ProgressDialog.show(WMSBindingNewRfidActivity.this, "请稍等", "正在获取RFID卡信息...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSBindingNewRfidActivity.this) + "/appscjController.do?getMesRfidFkbOnCheckByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSBindingNewRfidActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();

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
						final JSONObject mesLcgdMxbJson = attributes.getJSONObject("mesLcgdMxbEntity");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									String jgnr = mesLcgdMxbJson.getString("jgnr");
									if(jgnr.contains("出库")){
										Toast.makeText(WMSBindingNewRfidActivity.this, "该产品已进入出库环节！", Toast.LENGTH_SHORT).show();
										return;
									}

									String CPBM = json.getString("cpbm");
									String CPMC = json.getString("cpmc");
									int SL = json.getInt("sl");
									String BQH = json.getString("bqh");
									String LPH = json.getString("lph");
									String PCH = json.getString("pch");
									String CPXH = json.getString("cpxh");
									String CPGG = json.getString("cpgg");
									String rfidTid = json.getString("rfidTid");
									String jgz = mesLcgdMxbJson.getString("jgz");
									String jgzgh = mesLcgdMxbJson.getString("jgzgh");
									String dqgx=mesLcgdMxbJson.getString("jgnr");
									/**
									 * 判断批次号是否一样
									 */
									if (proRfidFkbBeanList.size() > 0) {
										if (proRfidFkbBeanList.size() > 0) {
											for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
												if (proRfidFkbBeanList.get(i).getBqh().equals(BQH)) {
													Toast.makeText(WMSBindingNewRfidActivity.this, "", Toast.LENGTH_SHORT).show();
													return;
												}
											}
										}



										if (!proRfidFkbBeanList.get(0).getJgz().equals(jgz)&&!proRfidFkbBeanList.get(0).getJgzgh().equals(jgzgh)) {

											return;
										}
									}

									proRfidFkbBean = new InitProRfidFkbBean();
									proRfidFkbBean.setCpbm(CPBM);
									proRfidFkbBean.setCpmc(CPMC);
									proRfidFkbBean.setSl(new BigDecimal(SL));
									proRfidFkbBean.setBqh(BQH);
									proRfidFkbBean.setIsCheck(false);
									proRfidFkbBean.setLph(LPH);
									proRfidFkbBean.setPch(PCH);
									proRfidFkbBean.setCpxh(CPXH);
									//proRfidFkbBean.setCpgg(CPGG);
									proRfidFkbBean.setRfidTid(rfidTid);
									proRfidFkbBean.setJgz(jgz);
									proRfidFkbBean.setJgzgh(jgzgh);
									proRfidFkbBean.setDqgx(dqgx);

									tv_LCGDH.setText("流程跟单号：" + BQH);
									tv_binding_CPMC.setText("产品名称：" + CPMC);
									tv_binding_SL.setText("数量：" + SL);
									//tv_check_CPGG.setText("产品规格：" + CPGG);
									tv_binding_PCH.setText("批次号：" + PCH);
									tv_binding_DDH.setText("订单号：" + dqgx);

								} catch (JSONException e) {

								}

							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// Toast.makeText(WMSBindingNewRfidActivity.this, msg, Toast.LENGTH_SHORT).show();

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
	 * 刷新数据
	 */
	public void refreshData() {

		tv_LCGDH.setText("流程跟单号：");
		tv_binding_CPMC.setText("产品名称：");
		tv_binding_SL.setText("数量：");
		tv_binding_PCH.setText("批次号：");
		et_DBH.setText("");
		et_NEW_BQH.setText("");
		String PCH = "";
		tv_binding_DDH.setText("订单号：");
		BigDecimal totalNum = new BigDecimal(0);
		BigDecimal HGNum = new BigDecimal(0);
		proRfidFkbBean = null;
		adapter.notifyDataSetChanged();
	}

	/**
	 * 刷新数据
	 */
	public static void cleanData() {

		tv_LCGDH.setText("流程跟单号：");
		tv_binding_CPMC.setText("产品名称：");
		tv_binding_SL.setText("数量：");
		//tv_check_CPGG.setText("产品规格：");
		tv_binding_PCH.setText("批次号：");
		tv_binding_DDH.setText("订单号：");
		String PCH = "";

		BigDecimal totalNum = new BigDecimal(0);
		BigDecimal HGNum = new BigDecimal(0);

		proRfidFkbBeanList.clear();
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
		Application app = WMSBindingNewRfidActivity.this.getApplication();
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
						Toast.makeText(WMSBindingNewRfidActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
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
		unregisterReceiver(keyReceiver);
		super.onDestroy();
	}
	/**
	 * 通过tid获取标签号
	 *
	 * @param
	 * @param value
	 */
	public void getBqhByTid(final String value) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSBindingNewRfidActivity.this) + "/appController.do?getPointsCardTabletagNoByTid&tid=" + value;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSBindingNewRfidActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
								et_NEW_BQH.setText(bqh);
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSBindingNewRfidActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSBindingNewRfidActivity.this, "出现错误"+e, Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					//Toast.makeText(WMSBindingNewRfidActivity.this, result, Toast.LENGTH_SHORT).show();
				}
			}
		}).start();

	}
}
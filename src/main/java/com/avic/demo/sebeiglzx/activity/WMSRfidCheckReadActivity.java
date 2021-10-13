package com.avic.demo.sebeiglzx.activity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import android.widget.ArrayAdapter;
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
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;

/**
 * 检验
 *
 */
@SuppressLint("SimpleDateFormat")
public class WMSRfidCheckReadActivity extends Activity implements OnClickListener {
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
	private TextView tv_init_next;
	private TextView tv_proccess_see;
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
		setContentView(R.layout.activity_wms_rfid_check_read);
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
		adapter = new WMSRFIDCheckReadTableAdapter(WMSRfidCheckReadActivity.this, proRfidFkbBeanList);
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
		// 设置表格标题的背景颜色
		ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
		tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
		// 读卡
		tv_check_read_card = (TextView) findViewById(R.id.tv_check_read_card);
		tv_check_read_card.setOnClickListener(this);
		tv_init_add = (TextView) findViewById(R.id.tv_init_add);
		tv_init_add.setOnClickListener(this);
		tv_init_delete = (TextView) findViewById(R.id.tv_init_delete);
		tv_init_delete.setOnClickListener(this);
		// 返回
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		// 下一步
		tv_init_next = (TextView) findViewById(R.id.tv_init_next);
		tv_init_next.setOnClickListener(this);
		// 流程进度
		tv_proccess_see = (TextView) findViewById(R.id.tv_proccess_see);
		tv_proccess_see.setOnClickListener(this);
		et_DBH= (TextView) findViewById(R.id.et_DBH);
		LinearLayout_BH = (LinearLayout) findViewById(R.id.LinearLayout_BH);
		LinearLayout_BH.setVisibility(View.GONE);
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
			 * 添加
			 */
			case R.id.tv_init_add:
				String HGSL = et_HGSL.getText().toString().trim();
				String jgnr = tv_check_JGNR.getText().toString().trim();
				String BH = et_DBH.getText().toString().trim();
				if("当前工序：打标".equals(jgnr)){
					if("".equals(BH)||BH==null){
						Toast.makeText(WMSRfidCheckReadActivity.this, "请输入标号！", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				if (HGSL == "") {
					Toast.makeText(WMSRfidCheckReadActivity.this, "请输入合格数量！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (proRfidFkbBean == null) {
					Toast.makeText(WMSRfidCheckReadActivity.this, "请读取产品信息！", Toast.LENGTH_SHORT).show();
					return;
				}
				if (proRfidFkbBeanList.size()>0) {
					String dqgx=proRfidFkbBeanList.get(0).getDqgx();
					String dqgx1=tv_check_JGNR.getText().toString().trim();
					String dqgx2=dqgx1.substring(5);
					if(!dqgx2.equals(dqgx)){

						refreshData();
						return;
					}
				}


				BigDecimal sl = proRfidFkbBean.getSl();
				BigDecimal hgsl = new BigDecimal(HGSL);
				if (hgsl.compareTo(sl) > 0) {
					// Toast.makeText(WMSRfidCheckReadActivity.this, "合格数量【" + HGSL + "】不允许大于数量【" + sl + "】,请重新输入合格数量！", Toast.LENGTH_SHORT).show();

					return;
				}

				proRfidFkbBean.setHgsl(new BigDecimal(HGSL));
				proRfidFkbBean.setDbh(BH);
				proRfidFkbBeanList.add(proRfidFkbBean);
				adapter = new WMSRFIDCheckReadTableAdapter(WMSRfidCheckReadActivity.this, proRfidFkbBeanList);
				tableListView.setAdapter(adapter);

				refreshData();
				break;

			/**
			 * 删除
			 */
			case R.id.tv_init_delete:
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					InitProRfidFkbBean proRfidFkbBean = proRfidFkbBeanList.get(i);
					if (proRfidFkbBean.getIsCheck()) {
						proRfidFkbBeanList.remove(i--);
					}
				}
				tableListView.setAdapter(adapter);
				BigDecimal totalNum = new BigDecimal(0);
				BigDecimal hgslNUm = new BigDecimal(0);
				BigDecimal bhgslNUm = new BigDecimal(0);
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					totalNum = totalNum.add(proRfidFkbBeanList.get(i).getSl());
					hgslNUm= hgslNUm.add(proRfidFkbBeanList.get(i).getHgsl());
				}
				bhgslNUm=totalNum.subtract(hgslNUm);
				tv_display_BHGSL.setText("不合格：" + bhgslNUm.toString());
				tv_display_HGSL.setText("合格：" + hgslNUm.toString());
				tv_display_ZSL.setText("总数量：" + totalNum.toString());
				adapter.notifyDataSetChanged();
				break;

			/**
			 * 下一步
			 */
			case R.id.tv_init_next:

				if (proRfidFkbBeanList.size() <= 0) {

					return;
				}
				Intent intent = new Intent(WMSRfidCheckReadActivity.this, WMSRfidCheckResultActivity.class);
				intent.putExtra("proRfidFkbBeanList", (Serializable) proRfidFkbBeanList);
				startActivity(intent);
				break;

			/**
			 * 流程进度
			 */
			case R.id.tv_proccess_see:
				if (proRfidFkbBean == null) {
					Toast.makeText(WMSRfidCheckReadActivity.this, "请读取产品信息！", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent1 = new Intent(WMSRfidCheckReadActivity.this, WMSRfidProcessActivity.class);
				intent1.putExtra("tid", tid);
				startActivity(intent1);

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
		progressDialog = ProgressDialog.show(WMSRfidCheckReadActivity.this, "请稍等", "正在获取RFID卡信息...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckReadActivity.this) + "/appscjController.do?getMesRfidFkbOnCheckByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckReadActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();

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
						final JSONObject mesLcgdJson = attributes.getJSONObject("mesLcgdEntity");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									String jgnr = mesLcgdMxbJson.getString("jgnr");
									if(jgnr.contains("出库")){

										return;
									}
									if(jgnr.contains("打标")){
										LinearLayout_BH.setVisibility(View.VISIBLE);
									}else{
										LinearLayout_BH.setVisibility(View.GONE);
									}

									String CPBM = json.getString("cpbm");
									String CPMC = json.getString("cpmc");
									int SL = json.getInt("sl");
									String BQH = json.getString("bqh");
									String LPH = json.getString("lph");
									String PCH = json.getString("pch");
									String CPTH = json.getString("cpth");
									String CPXH = json.getString("cpxh");
									String CPGG = json.getString("cpgg");
									String rfidTid = json.getString("rfidTid");
									String jgz = mesLcgdMxbJson.getString("jgz");
									String jgzgh = mesLcgdMxbJson.getString("jgzgh");
									String dqgx=mesLcgdMxbJson.getString("jgnr");
									//String DBH=mesLcgdMxbJson.getString("dbh");

									String DBH=mesLcgdJson.getString("marking_no");
									/**
									 * 判断批次号是否一样
									 */
									if (proRfidFkbBeanList.size() > 0) {
										/*if (!proRfidFkbBeanList.get(0).getPch().equals(PCH)) {
											dialog.setTitleText("提示");
											dialog.setContentText("读取失败，原因：该批次号【" + PCH + "】与已读取的批次号【" + proRfidFkbBeanList.get(0).getPch() + "】不一致，请换卡");
											dialog.setOkBtn("关闭", new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													dialog.dismiss();
												}
											});
											dialog.show();
											return;
										}*/


										if (proRfidFkbBeanList.size() > 0) {
											for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
												if (proRfidFkbBeanList.get(i).getBqh().equals(BQH)) {

													return;
												}
											}
										}

										/*if (!proRfidFkbBeanList.get(0).getPch().equals(PCH)) {
											dialog.setTitleText("提示");
											dialog.setContentText("读取失败，原因：该批次号【" + PCH + "】与已读取的批次号【" + proRfidFkbBeanList.get(0).getPch() + "】不一致，请换卡");
											dialog.setOkBtn("关闭", new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													dialog.dismiss();
												}
											});
											dialog.show();
											return;
										}*/


										if (!proRfidFkbBeanList.get(0).getJgz().equals(jgz)&&!proRfidFkbBeanList.get(0).getJgzgh().equals(jgzgh)) {

											return;
										}

										/*else if (proRfidFkbBeanList.get(0).getBqh().equals(BQH)) {
											dialog.setTitleText("提示");
											dialog.setContentText("读取失败，原因：该标签号【" + BQH + "】已存在，请换卡");
											dialog.setOkBtn("关闭", new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													dialog.dismiss();
												}
											});
											dialog.show();
											return;
										}*/

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

									tv_check_BQH.setText("标签号：" + BQH);
									tv_check_CPMC.setText("产品名称：" + CPMC);
									tv_check_SL.setText("数量：" + SL);
									//tv_check_CPGG.setText("产品规格：" + CPGG);
									tv_check_PCH.setText("产品图号：" + CPTH);
									et_HGSL.setText(SL + "");
									et_DBH.setText(DBH);
									tv_check_JGNR.setText("当前工序：" + dqgx);

								} catch (JSONException e) {
									System.out.print(e);
								}

							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidCheckReadActivity.this, msg, Toast.LENGTH_SHORT).show();

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

		tv_check_BQH.setText("标签号：");
		tv_check_CPMC.setText("产品名称：");
		tv_check_SL.setText("数量：");
		//tv_check_CPGG.setText("产品规格：");
		tv_check_PCH.setText("产品图号：");
		et_HGSL.setText("");
		et_DBH.setText("");
		String PCH = "";
		tv_check_JGNR.setText("当前工序：");
		BigDecimal totalNum = new BigDecimal(0);
		BigDecimal HGNum = new BigDecimal(0);

		if (proRfidFkbBeanList.size() > 0) {
			for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
				totalNum = totalNum.add(proRfidFkbBeanList.get(i).getSl());
				HGNum = HGNum.add(proRfidFkbBeanList.get(i).getHgsl());
			}
			BigDecimal BHGNum = totalNum.subtract(HGNum);
			PCH = proRfidFkbBeanList.get(0).getPch();
			tv_display_BHGSL.setText("不合格：" + BHGNum.toString());
			tv_display_HGSL.setText("合格：" + HGNum.toString());
			tv_display_ZSL.setText("总数量：" + totalNum.toString());
			tv_display_PCH.setText("批次号：" + PCH);
		}
		proRfidFkbBean = null;

		adapter.notifyDataSetChanged();
	}

	/**
	 * 刷新数据
	 */
	public static void cleanData() {

		tv_check_BQH.setText("标签号：");
		tv_check_CPMC.setText("产品名称：");
		tv_check_SL.setText("数量：");
		//tv_check_CPGG.setText("产品规格：");
		tv_check_PCH.setText("产品图号：");
		et_HGSL.setText("");
		String PCH = "";

		BigDecimal totalNum = new BigDecimal(0);
		BigDecimal HGNum = new BigDecimal(0);

		proRfidFkbBeanList.clear();
		adapter.notifyDataSetChanged();

		tv_display_BHGSL.setText("不合格：");
		tv_display_HGSL.setText("合格：");
		tv_display_ZSL.setText("总数量：");
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
						Toast.makeText(WMSRfidCheckReadActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
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

}
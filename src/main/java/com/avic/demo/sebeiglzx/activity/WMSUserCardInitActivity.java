package com.avic.demo.sebeiglzx.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;

/**
 * 员工卡初始
 *
 */
@SuppressLint("SimpleDateFormat")
public class WMSUserCardInitActivity extends Activity {
	// 返回按钮
	private ImageView iv_back;
	private EditText et_wms_usercard_tid;
	private EditText et_wms_usercard_yggh;
	private EditText et_wms_usercard_epc;
	private TextView tv_wms_usercard_save;
	private TextView tv_wms_usercard_check;
	private TextView tv_wms_usercard_check_status;
	private TextView tv_wms_usercard_readCard;
	private TextView tv_wms_usercard_username;
	String tid = "";
	String epc = "";
	boolean isrun, issound = true;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	MyApplication myapp;

	private KeyReceiver keyReceiver;

	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_usercard_init);
		initView();
	}

	private void initView() {
		Application app = getApplication();
		try {
			myapp = (MyApplication) app;
		} catch (Exception e2) {
		}
		soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		soundPool.load(this, R.raw.beep, 1);
		soundPoolerr = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);


		keyReceiver = new KeyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.rfid.FUN_KEY");
		registerReceiver(keyReceiver, intentFilter);

		//autoAdaptLayout();

		et_wms_usercard_tid = (EditText) findViewById(R.id.et_wms_usercard_tid);
		et_wms_usercard_epc = (EditText) findViewById(R.id.et_wms_usercard_epc);
		et_wms_usercard_yggh = (EditText) findViewById(R.id.et_wms_usercard_yggh);
		tv_wms_usercard_username = (TextView) findViewById(R.id.tv_wms_usercard_username);
		tv_wms_usercard_save = (TextView) findViewById(R.id.tv_wms_usercard_save);
		tv_wms_usercard_check = (TextView) findViewById(R.id.tv_wms_usercard_check);
		tv_wms_usercard_check_status = (TextView) findViewById(R.id.tv_wms_usercard_check_status);
		tv_wms_usercard_readCard = (TextView) findViewById(R.id.tv_wms_usercard_readCard);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		// 读卡
		tv_wms_usercard_readCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				et_wms_usercard_tid.setText("");
				et_wms_usercard_epc.setText("");
				et_wms_usercard_yggh.setText("");
				tv_wms_usercard_username.setText("");
				readTidByMobileDataTerminal();
			}

		});

		// 保存
		tv_wms_usercard_save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				saveData();
			}
		});

		/**
		 * 检查
		 */
		tv_wms_usercard_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// check();
			}
		});

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	private void check() {
		tv_wms_usercard_check_status.setText("未检查");
		checkHjhByMobileDataTerminal();
	}

	private void saveData() {

		final String yggh = et_wms_usercard_yggh.getText().toString().trim();
		if (TextUtils.isEmpty(tid) || TextUtils.isEmpty(epc)) {
			Toast.makeText(WMSUserCardInitActivity.this, "TID和EPC不允许为空！", Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(yggh)) {
			Toast.makeText(WMSUserCardInitActivity.this, "员工工号不允许为空！", Toast.LENGTH_SHORT).show();
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String url = URLHelper.getBaseUrl(WMSUserCardInitActivity.this) + "/appscjController.do?initialTSUserCardByEmpNo&empNo=" + yggh + "&tid=" + tid + "&epc=" + epc + "&bz=1";
					String result = "";
					try {
						result = GetHttp.RequstGetHttp(url);
					} catch (Exception e1) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSUserCardInitActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
							}
						});
					}
					try {
						JSONObject jsonObj = new JSONObject(result);
						boolean success = (Boolean) jsonObj.get("success");
						final String msg = (String) jsonObj.get("msg");
						if (!success) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(WMSUserCardInitActivity.this, msg, Toast.LENGTH_SHORT).show();
								}
							});
						} else {
							// 切换主线程更新ui
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									Toast.makeText(WMSUserCardInitActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
								}
							});
						}
					} catch (final JSONException e) {
						Toast.makeText(WMSUserCardInitActivity.this, "异常:"+e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}).start();
		}

	}

	// 自适应布局
	private void autoAdaptLayout() {
		// 页面布局
		int height = (int) getResources().getDimension(R.dimen.activity_head);
		int activity_head_font_size = (int) getResources().getDimension(R.dimen.activity_head_font_size);
		int activity_button_height = (int) getResources().getDimension(R.dimen.activity_button_height);
		int activity_button_width = (int) getResources().getDimension(R.dimen.activity_button_width);
		int activity_label_min_size = (int) getResources().getDimension(R.dimen.activity_label_min_size);
		int activity_label_max_size = (int) getResources().getDimension(R.dimen.activity_label_max_size);
		int activity_textedit_height = (int) getResources().getDimension(R.dimen.activity_textedit_height);
		int activity_button_font_size = (int) getResources().getDimension(R.dimen.activity_button_font_size);
		int ll_search_height = (int) getResources().getDimension(R.dimen.ll_search_height);
		int activity_linear_layout_height = (int) getResources().getDimension(R.dimen.activity_linear_layout_height);

		LinearLayout ll_pro_rfid_mgr_shelf_search_head = (LinearLayout) findViewById(R.id.ll_pro_rfid_mgr_shelf_search_head);
		ll_pro_rfid_mgr_shelf_search_head.getLayoutParams().height = height;

		LinearLayout ll_choice2 = (LinearLayout) findViewById(R.id.ll_choice2);
		ll_choice2.getLayoutParams().height = activity_linear_layout_height;

		LinearLayout ll_choice3 = (LinearLayout) findViewById(R.id.ll_choice3);
		ll_choice3.getLayoutParams().height = activity_linear_layout_height;

		LinearLayout ll_choice4 = (LinearLayout) findViewById(R.id.ll_choice4);
		ll_choice4.getLayoutParams().height = activity_linear_layout_height;
		//
		LinearLayout ll_choice5 = (LinearLayout) findViewById(R.id.ll_choice5);
		ll_choice5.getLayoutParams().height = activity_linear_layout_height;

		LinearLayout ll_choice6 = (LinearLayout) findViewById(R.id.ll_choice6);
		ll_choice6.getLayoutParams().height = ll_search_height;
		LinearLayout ll_choice7 = (LinearLayout) findViewById(R.id.ll_choice7);
		ll_choice7.getLayoutParams().height = 100;
		LinearLayout ll_choice8 = (LinearLayout) findViewById(R.id.ll_choice8);
		ll_choice8.getLayoutParams().height = 120;
		//

		TextView tv_warming = (TextView) findViewById(R.id.tv_warming);
		tv_warming.setTextSize(20);
		//tv_warming.setTextColor(color.red);
		ViewGroup.LayoutParams tv_warmingLp = tv_warming.getLayoutParams();
		tv_warmingLp.width = 710;
		tv_warmingLp.height = 120;
		tv_warming.setLayoutParams(tv_warmingLp);

		// 字体
		((TextView) findViewById(R.id.tv_top_title)).setTextSize(activity_head_font_size);

		((TextView) findViewById(R.id.tv_wms_usercard_epc)).setTextSize(activity_label_min_size);

		((EditText) findViewById(R.id.et_wms_usercard_epc)).setTextSize(activity_label_min_size);
		((TextView) findViewById(R.id.tv_wms_usercard_tid)).setTextSize(activity_label_min_size);

		((EditText) findViewById(R.id.et_wms_usercard_tid)).setTextSize(activity_label_min_size);
		((TextView) findViewById(R.id.tv_wms_usercard_yggh)).setTextSize(activity_label_min_size);
		((EditText) findViewById(R.id.et_wms_usercard_yggh)).setTextSize(activity_label_min_size);
		((TextView) findViewById(R.id.tv_wms_usercard_check_statusName)).setTextSize(activity_label_min_size);
		((TextView) findViewById(R.id.tv_wms_usercard_check_status)).setTextSize(activity_label_min_size);
		((TextView) findViewById(R.id.tv_wms_usercard_save)).setTextSize(activity_label_min_size);
		((TextView) findViewById(R.id.tv_wms_usercard_check)).setTextSize(activity_label_min_size);
		((TextView) findViewById(R.id.tv_wms_usercard_readCard)).setTextSize(activity_label_min_size);

	}

	public void checkHJH(final String tid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSUserCardInitActivity.this) + "/appscjController.do?checkHJH&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv_wms_usercard_check_status.setText("网络已断开或者服务器停止，请联系管理员");
							// Toast.makeText(ProRfidMgrInitShelfActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					boolean success = (Boolean) jsonObj.get("success");
					final String msg = (String) jsonObj.get("msg");
					if (!success) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tv_wms_usercard_check_status.setText(msg);
							}
						});
					} else {
						JSONObject attributes = jsonObj.getJSONObject("attributes");
						JSONArray dataArr = attributes.getJSONArray("data");
						for (int i = 0; i < dataArr.length(); i++) {
							JSONObject data = dataArr.getJSONObject(i);
							// hjh = data.get("hjh") == null ? "" : String.valueOf(data.get("hjh"));
						}
						// 切换主线程更新ui
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// tv_wms_usercard_check_status.setText(msg + ",货架号：" + hjh);
								// et_wms_usercard_yggh.setText(hjh);
								// Toast.makeText(ProRfidMgrInitShelfActivity.this, "检查成功!", Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tv_wms_usercard_check_status.setText("调用接口失败，请联系管理员");
							// Toast.makeText(ProRfidMgrInitShelfActivity.this, "调用接口失败，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}

	public void getHJH(final String tid) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSUserCardInitActivity.this) + "/appscjController.do?getTSUserEmpNoByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSUserCardInitActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();

						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					boolean success = (Boolean) jsonObj.get("success");
					final String msg = (String) jsonObj.get("msg");
					if (!success) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSUserCardInitActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						JSONObject attributes = jsonObj.getJSONObject("attributes");
						final String empNo = attributes.getString("empNo");
						final String username = attributes.getString("username");

						// 切换主线程更新ui
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								et_wms_usercard_yggh.setText(empNo);
								tv_wms_usercard_username.setText(username);
							}
						});
					}
				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSUserCardInitActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
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
						// check();
						break;
					case KeyEvent.KEYCODE_F3:
						saveData();
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



	private void readTidByMobileDataTerminal() {
		try {
			tid = RfidRead.getTidValue(myapp);
			epc = RfidRead.getEpcValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSUserCardInitActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
				et_wms_usercard_tid.setText(tid);
				et_wms_usercard_epc.setText(epc);
				getHJH(tid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		finally {

		}
	}

	private void checkHjhByMobileDataTerminal() {
		try {
			tid = RfidRead.getTidValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSUserCardInitActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
				checkHJH(tid);
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
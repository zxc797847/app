package com.avic.demo.sebeiglzx.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.avic.demo.sebeiglzx.R;

import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;
import com.avic.demo.sebeiglzx.bean.SpinnearBean;
import com.avic.demo.sebeiglzx.home.MyApplication;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.listener.OnSpinnerClickListener;
import com.avic.demo.sebeiglzx.listener.OnSpinnerItemClickListener;
import com.avic.demo.sebeiglzx.listener.PostHttp;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;
import com.avic.demo.sebeiglzx.view.SpinnerViewPop;
import com.google.gson.JsonObject;

import android.support.annotation.Nullable;

/**
 * 材料入库
 *
 */
public class WMSRfidDHRKActivity extends AppCompatActivity implements OnClickListener, OnCheckedChangeListener {
	// 返回按钮
	private ImageView iv_back;
	private SpinnerViewPop spinner_clph;
	private SpinnerViewPop spinner_clzj;
	private TextView tv_init_save;
	private TextView et_clcd;

	private static ProgressDialog progressDialog;
	String clphData="";
	String clZjData="";
	MyApplication myapp;
	// 读线程：
	private Thread runThread;

	public ProgressDialog myDialog = null;

	/**下拉材料牌号名称列表集合*/
	private ArrayList<SpinnearBean> mCLPHSpinner1List =new ArrayList<SpinnearBean>();
	/**下拉材料直径列表集合*/
	private ArrayList<SpinnearBean> mCLZJpinner1List =new ArrayList<SpinnearBean>();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_rfid_dhrk);
		initView();
		initClph();
	}



	private void initView() {
		// 初始设备
		initDev();
		//dialog = CommonDialogFactory.createDialogByType(ProChooseCardDeliveryActivity.this, DialogUtil.DIALOG_TYPE_101);
		iv_back = (ImageView) findViewById(R.id.iv_back);

		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		spinner_clph=findViewById(R.id.spinner_clph);
		spinner_clzj=findViewById(R.id.spinner_clzj);
		et_clcd=findViewById(R.id.et_clcd);

		String data[] = new String[] {};
		// 声明一个ArrayAdaper，用来装载Spinner数据
		ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, R.layout.spinner_item, data);

		tv_init_save = (TextView) findViewById(R.id.tv_init_save);
		tv_init_save.setOnClickListener(this);



		//下拉菜单区域的点击事件监听
		spinner_clph.setOnSpinnerClickListener(new OnSpinnerClickListener() {
			@Override
			public void OnFinished() {
				//KeyboardUtil.hideKeyboard(MainActivity.this);//隐藏软键盘
				spinner_clph.PopupListDialog();
			}
		});
		spinner_clzj.setOnSpinnerClickListener(new OnSpinnerClickListener() {
			@Override
			public void OnFinished() {

				spinner_clph.PopupListDialog();
			}
		});
		spinner_clzj.setOnSpinnerItemClickListener(new OnSpinnerItemClickListener() {
			@Override
			public void OnFinished(int position) {
				String clzj= mCLZJpinner1List.get(position).getParaValue();
				System.out.print(clzj);
				if (!StringUtil.isEmpty(clzj)) {
					clZjData=clzj;
				}
			}
		});
		//下拉菜单列表的列表项的点击事件监听
		spinner_clph.setOnSpinnerItemClickListener(new OnSpinnerItemClickListener() {
			@Override
			public void OnFinished(int position) {
				String clph= mCLPHSpinner1List.get(position).getParaName();
				if (!StringUtil.isEmpty(clph)) {
					clphData=clph;
					getClzjByClph(clph);
				}
			}
		});

	}
	/**
	 * 初始材料牌号
	 */
	private void initClph() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidDHRKActivity.this) + "/appscjController.do?getClph";
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidDHRKActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					JSONObject attributes = jsonObj.getJSONObject("attributes");
					JSONArray dataArr = attributes.getJSONArray("data");
					if(mCLPHSpinner1List.size()<=0){
						/*SpinnearBean spinnearBean=new SpinnearBean();
						spinnearBean.setParaName("");
						spinnearBean.setParaValue("");
						mCKMCSpinner1List.add(spinnearBean);*/
					}else{
						mCLPHSpinner1List.clear();
					}

					for (int i = 1; i <= dataArr.length(); i++) {
						SpinnearBean spinnearBean=new SpinnearBean();
						JSONObject data = dataArr.getJSONObject(i - 1);
						String clph = (String) data.get("clph");
						spinnearBean.setParaName(clph);
						spinnearBean.setParaValue(clph);
						mCLPHSpinner1List.add(spinnearBean);

					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							spinner_clph.setData(mCLPHSpinner1List);//设置下拉菜单列表集合源
							spinner_clph.setSelectedIndexAndText(0);//更改下拉菜单选中的列表项下标值
							String clph= mCLPHSpinner1List.get(0).getParaValue();
							if (!StringUtil.isEmpty(clph)) {
								getClzjByClph(clph);
							}
						}
					});
				} catch (final JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidDHRKActivity.this, "获取接口数据失败，请联系管理员！", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}


	/**
	 * 初始材料直径
	 */
	private void getClzjByClph(final String clph) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidDHRKActivity.this) + "/appscjController.do?getClzjByClph&clph=" + clph;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidDHRKActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
						JSONArray dataArr = attributes.getJSONArray("data");
						if(mCLZJpinner1List.size()<=0){
							SpinnearBean spinnearBean  =new SpinnearBean();
							spinnearBean.setParaName("");
							spinnearBean.setParaValue("");
							mCLZJpinner1List.add(spinnearBean);
						}else{
							mCLZJpinner1List.clear();
						}

						for (int i = 1; i <= dataArr.length(); i++) {
							JSONObject data = dataArr.getJSONObject(i - 1);
							Integer clzj = (Integer) data.get("zj");
							SpinnearBean spinnearBean  =new SpinnearBean();
							spinnearBean.setParaName(String.valueOf(clzj));
							spinnearBean.setParaValue(String.valueOf(clzj));
							mCLZJpinner1List.add(spinnearBean);
						}

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								spinner_clzj.setData(mCLZJpinner1List);
								spinner_clzj.setSelectedIndexAndText(0);//更改下拉菜单选中的列表项下标值
							}
						});


					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidDHRKActivity.this, "获取接口数据失败，请联系管理员！", Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidDHRKActivity.this, "获取接口数据失败，请联系管理员！", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}


		}).start();
	}

	// 自适应布局
	private void autoAdaptLayout() {


	}

	/**
	 * 初始设备接口
	 */
	public void initDev() {

	}




	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
			/**
			 * 返回
			 */
			case R.id.iv_back:
				finish();
				break;
			case R.id.tv_init_save:

				submitData();
				break;

		}
	}
	/**
	 * 提交数据
	 *
	 */

	private void submitData() {
		String clzj = spinner_clzj.getText().toString().trim();
		String clph = spinner_clph.getText().toString().trim();
		String clcd = et_clcd.getText().toString().trim();
		if (StringUtil.isEmpty(clph)) {
			Toast.makeText(WMSRfidDHRKActivity.this, "请选择材料牌号！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtil.isEmpty(clzj)) {
			Toast.makeText(WMSRfidDHRKActivity.this, "请选择材料直径！", Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtil.isEmpty(clcd)) {
			Toast.makeText(WMSRfidDHRKActivity.this, "请输入材料长度！", Toast.LENGTH_SHORT).show();
			return;
		}
		progressDialog = ProgressDialog.show(WMSRfidDHRKActivity.this, "请稍等", "正在提交数据...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String clzj = spinner_clzj.getText().toString().trim();
				String clph = spinner_clph.getText().toString().trim();
				String clcd = et_clcd.getText().toString().trim();
				String url = URLHelper.getBaseUrl(WMSRfidDHRKActivity.this) + "/appscjController.do?dhrksubmitData&clzj="+clzj+"&clph="+clph+"&clcd="+clcd+"";
				String result = "";
				Map<String, String> paramsMap = new HashMap<String, String>();
				try {
					result = GetHttp.RequstGetHttp(url);
					JSONArray jsonArr = new JSONArray();
					JSONObject jsonObj = new JSONObject(result);
					boolean success = (Boolean) jsonObj.get("success");
					final String msg = (String) jsonObj.get("msg");

					if (success) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidDHRKActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidDHRKActivity.this, "提交失败！", Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (final Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidDHRKActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
					return;
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



}
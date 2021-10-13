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
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.adapter.WMSRFIDCheckTableAdapter;
import com.avic.demo.sebeiglzx.adapter.WMSRFIDInitTableAdapter;
import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;
import com.avic.demo.sebeiglzx.bean.MyApplication;
import com.avic.demo.sebeiglzx.bean.addJyfpBean;
import com.avic.demo.sebeiglzx.http.GetHttp;
import com.avic.demo.sebeiglzx.listener.PostHttp;
import com.avic.demo.sebeiglzx.utils.tools.RfidRead;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;
import com.avic.demo.sebeiglzx.utils.url.URLHelper;
import com.avic.demo.sebeiglzx.adapter.WMSRFIDCheckReadTableAdapter;

/**
 * 检验
 *
 */
@SuppressLint("SimpleDateFormat")
public class WMSRfidCheckResultActivity extends Activity implements OnClickListener {
	// 返回按钮hybz  cb_sfyxyy
	private ImageView iv_back;
	String tid = "";
	String epc = "";
	boolean isrun, issound = true;
	private SoundPool soundPool, soundPoolerr;
	boolean isreading;
	MyApplication myapp;
	private KeyReceiver keyReceiver;
	String hjh = "";
	private TextView tv_check_submit;
	private TextView tv_check_result_CPMC;
	private TextView tv_check_result_CPTH;
	private TextView tv_check_result_LPH;
	//private TextView tv_check_result_CPGG;
	private TextView tv_check_result_PCH;

	private TextView tv_check_result_ZSL;
	private TextView tv_check_result_HGSL;
	private TextView tv_check_result_BHGSL;

	//	private TextView tv_check_result_XMw;
//	private TextView tv_check_result_GHw;
	private TextView tv_check_result_JYY_XM;
	private TextView tv_check_result_JYY_GH;

	private TextView tv_check_result_JYYGH_card;
	//private TextView tv_check_result_read_BHGZRR_card;
	private TextView tv_check_result_read_BHGBQH_card;
	private TextView et_FPBQH;
	//private TextView tv_check_result_read_JGZGH_card;
	private TextView tv_check_result_read_card;
	private List<InitProRfidFkbBean> proRfidFkbBeanList;
	private static List<addJyfpBean> addJyfpBeanList;
	private static addJyfpBean addJyfpBean;
	private static ListView tableListView;
	private CheckBox ck_XZ;
	private TextView tv_fp_add;
	private TextView tv_fp_delete;
	//	private EditText et_JGYGGH;
	private static EditText et_BHGZRR;
	private static EditText et_ZRR;
	//private EditText et_JGZGH;
	//private EditText et_JGZ;
	private static TextView et_bhg_SL;
	static WMSRFIDInitTableAdapter adapter;
	static WMSRFIDCheckTableAdapter adapter1;
	//	private Spinner spinner_check_BHGZRR;
	private static Spinner spinner_check_BHGYY;
	private static Spinner spinner_FPGX;
	BigDecimal totalNum = new BigDecimal(0);
	BigDecimal HGNum = new BigDecimal(0);
	private String[] dataZRR;
	private String[] dataZRYY;
	private String[] dataCPGG;
	private static ProgressDialog progressDialog;
	private String JGYGGH;
	private String JGZ;
	private String JYYGH;
	String bol="";
	private EditText et_check_BHGYY;
	private SharedPreferences sp;
	private CheckBox cb_sfyxyy;
	private CheckBox hybz;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_rfid_check_result);
		initView();
		initData();


	}


	//根据标签号去获得工序
	private void getGxBybqh(final String BQH){
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckResultActivity.this) + "/appscjController.do?getGxBybqh&bqh=" + BQH;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					JSONObject attributes = jsonObj.getJSONObject("attributes");
					JSONArray dataArr = attributes.getJSONArray("data");

					dataCPGG = new String[dataArr.length() + 1];
					dataCPGG[0] = "";
					String zrr=et_ZRR.getText().toString();
					if("外协".equals(zrr)){
						dataCPGG = new String[1];
						dataCPGG[0]="外协";
					}else{
						for (int i = 1; i <= dataArr.length(); i++) {
							JSONObject data = dataArr.getJSONObject(i - 1);
							String gx = (String) data.get("jgnr");
							dataCPGG[i] = gx;
						}
					}
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ArrayAdapter<String> adapterGX = new ArrayAdapter<String>(WMSRfidCheckResultActivity.this, R.layout.spinner_item, dataCPGG);
							// 加载item的布局样式
							adapterGX.setDropDownViewResource(R.layout.spinner_item);
							// 最后让Spinner加载adapter
							spinner_FPGX.setAdapter(adapterGX);
						}
					});

				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "获取接口数据失败，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}







	private void initView() {
		Application app = getApplication();
		try {
			myapp = (MyApplication) app;
		} catch (Exception e2) {
		}
		initDev();
		addJyfpBeanList = new ArrayList<addJyfpBean>();
		adapter1 = new WMSRFIDCheckTableAdapter(WMSRfidCheckResultActivity.this, addJyfpBeanList);
		tableListView = (ListView) findViewById(R.id.wms_oil_seal_fp_list);
		// 设置表格标题的背景颜色
		ViewGroup tableTitle = (ViewGroup) findViewById(R.id.fp_title);
		tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
		tv_check_result_CPMC = (TextView) findViewById(R.id.tv_check_result_CPMC);
		tv_check_result_CPTH = (TextView) findViewById(R.id.tv_check_result_CPTH);
		tv_check_result_LPH = (TextView) findViewById(R.id.tv_check_result_LPH);
		//tv_check_result_CPGG = (TextView) findViewById(R.id.tv_check_result_CPGG);
		tv_check_result_PCH = (TextView) findViewById(R.id.tv_check_result_PCH);

		tv_check_result_ZSL = (TextView) findViewById(R.id.tv_check_result_ZSL);
		tv_check_result_HGSL = (TextView) findViewById(R.id.tv_check_result_HGSL);
		tv_check_result_BHGSL = (TextView) findViewById(R.id.tv_check_result_BHGSL);
		et_bhg_SL=(TextView) findViewById(R.id.et_bhg_SL);
		/*tv_check_result_XM = (TextView) findViewById(R.id.tv_check_result_XM);
		tv_check_result_GH = (TextView) findViewById(R.id.tv_check_result_GH)*/;
		tv_check_result_JYY_XM = (TextView) findViewById(R.id.tv_check_result_JYY_XM);
		tv_check_result_JYY_GH = (TextView) findViewById(R.id.tv_check_result_JYY_GH);

//		spinner_check_BHGZRR = (Spinner) findViewById(R.id.spinner_check_BHGZRR);
		spinner_check_BHGYY = (Spinner) findViewById(R.id.spinner_check_BHGYY);
		// 声明一个ArrayAdaper，用来装载Spinner数据


		spinner_check_BHGYY.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String BHGYY = spinner_check_BHGYY.getSelectedItem().toString();
				et_check_BHGYY.setText(BHGYY);
				arg0.setVisibility(View.VISIBLE);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				arg0.setVisibility(View.VISIBLE);
			}
		});
		cb_sfyxyy=(CheckBox) findViewById(R.id.cb_sfyxyy);


		hybz=(CheckBox) findViewById(R.id.hybz);
		hybz.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if(checked){
					bol="1";
				}else{
					bol="0";
				}

			}
		});

		cb_sfyxyy.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				if(checked){
					et_ZRR.setText("外协");
					et_BHGZRR.setText("外协");
					dataCPGG =new String[1];
					dataCPGG[0]="外协";
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ArrayAdapter<String> adapterGX = new ArrayAdapter<String>(WMSRfidCheckResultActivity.this, R.layout.spinner_item, dataCPGG);
							// 加载item的布局样式
							adapterGX.setDropDownViewResource(R.layout.spinner_item);
							// 最后让Spinner加载adapter
							spinner_FPGX.setAdapter(adapterGX);
						}
					});
				}else{
					et_ZRR.setText("");
					et_BHGZRR.setText("");
					dataCPGG =new String[1];
					dataCPGG[0]="";
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ArrayAdapter<String> adapterGX = new ArrayAdapter<String>(WMSRfidCheckResultActivity.this, R.layout.spinner_item, dataCPGG);
							// 加载item的布局样式
							adapterGX.setDropDownViewResource(R.layout.spinner_item);
							// 最后让Spinner加载adapter
							spinner_FPGX.setAdapter(adapterGX);
						}
					});
				}

			}
		});
		spinner_FPGX = (Spinner) findViewById(R.id.spinner_FPGX);
//		tv_check_result_read_card = (TextView) findViewById(R.id.tv_check_result_read_card);
//		tv_check_result_read_BHGZRR_card = (TextView) findViewById(R.id.tv_check_result_read_BHGZRR_card);
//		et_JGYGGH = (EditText) findViewById(R.id.et_JGYGGH);
//		et_JYYGH = (EditText) findViewById(R.id.et_JYYGH);
		et_BHGZRR = (EditText) findViewById(R.id.et_BHGZRR);
		et_ZRR = (EditText) findViewById(R.id.et_ZRR);
		/*et_JGZGH = (EditText) findViewById(R.id.et_JGZGH);
		et_JGZ = (EditText) findViewById(R.id.et_JGZ);*/
		et_check_BHGYY = (EditText) findViewById(R.id.et_check_BHGYY);
		et_FPBQH = (EditText) findViewById(R.id.et_FPBQH);


		tv_fp_add = (TextView) findViewById(R.id.tv_fp_add);
		tv_fp_add.setOnClickListener(this);
		tv_fp_delete = (TextView) findViewById(R.id.tv_fp_delete);
		tv_fp_delete.setOnClickListener(this);
		tableListView = (ListView) findViewById(R.id.wms_oil_seal_fp_list);


		// 返回
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		/**
		 * 读取员工卡
		 */
//		tv_check_result_read_card = (TextView) findViewById(R.id.tv_check_result_read_card);
//		tv_check_result_read_card.setOnClickListener(this);
		/*tv_check_result_read_BHGZRR_card = (TextView) findViewById(R.id.tv_check_result_read_BHGZRR_card);
		tv_check_result_read_BHGZRR_card.setOnClickListener(this);*/
		tv_check_result_read_BHGBQH_card = (TextView) findViewById(R.id.tv_check_result_read_BHGBQH_card);
		tv_check_result_read_BHGBQH_card.setOnClickListener(this);

		/*tv_check_result_read_JGZGH_card = (TextView) findViewById(R.id.tv_check_result_read_JGZGH_card);
		tv_check_result_read_JGZGH_card.setOnClickListener(this);*/
		/**
		 * 读取检验员
		 */
//		tv_check_result_JYYGH_card = (TextView) findViewById(R.id.tv_check_result_JYYGH_card);
//		tv_check_result_JYYGH_card.setOnClickListener(this);
		/**
		 * 提交
		 */
		tv_check_submit = (TextView) findViewById(R.id.tv_check_submit);
		tv_check_submit.setOnClickListener(this);

		spinner_FPGX.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				String gx = spinner_FPGX.getSelectedItem().toString();
				if (!"外协".equals(gx)){
					if (!StringUtil.isEmpty(gx)) {
						String bqh=et_FPBQH.getText().toString();
						getJGZBygx(gx,bqh);
					}
					arg0.setVisibility(View.VISIBLE);
				}
			}


			public void onNothingSelected(AdapterView<?> arg0) {
				arg0.setVisibility(View.VISIBLE);
			}
		});

	}

	private void getJGZBygx(final String gx, final String bqh) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckResultActivity.this) + "/appscjController.do?getJgzByGx";
				String result = "";
				try {
					Map<String, String> paramsMap = new HashMap<String, String>();
					paramsMap.put("bqh", bqh);
					paramsMap.put("gx", gx);
					result = PostHttp.RequstPostHttpNew(WMSRfidCheckResultActivity.this, url, paramsMap);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
						final String empNo = attributes.getString("empNo");
						final String username = attributes.getString("username");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								String zrrgh = et_BHGZRR.getText().toString().trim();
								if(StringUtil.isEmpty(zrrgh)){
									et_BHGZRR.setText(empNo);
									et_ZRR.setText(username);
								}
							}
						});
					}else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								et_BHGZRR.setText("");
								et_ZRR.setText("");
								Toast.makeText(WMSRfidCheckResultActivity.this, "请读员工卡获取责任人工号", Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "获取接口数据失败，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();

	}



	private void initData() {
		proRfidFkbBeanList = (List<InitProRfidFkbBean>) this.getIntent().getSerializableExtra("proRfidFkbBeanList");
		if (proRfidFkbBeanList.size() > 0) {
			InitProRfidFkbBean proRfidFkbBean = proRfidFkbBeanList.get(0);
			tv_check_result_CPMC.setText("产品名称：" + proRfidFkbBean.getCpmc());
			tv_check_result_CPTH.setText("数量：" + proRfidFkbBean.getSl());
			tv_check_result_LPH.setText("加工者：" + proRfidFkbBean.getJgz());
			tv_check_result_PCH.setText("批次号：" + proRfidFkbBean.getPch());
			//tv_check_result_CPGG.setText("产品规格:"+ proRfidFkbBean.getCpgg());
			JGYGGH = proRfidFkbBean.getJgzgh();
			JGZ = proRfidFkbBean.getJgz();

			//加工者
//			tv_check_result_XM.setText(""+JGZ);
//			tv_check_result_GH.setText(JGYGGH);

			//检验员
			sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
			String JYY = sp.getString("REALNAME","");
			JYYGH=sp.getString("EMP_NO", "");

			tv_check_result_JYY_XM.setText(JYY);
			tv_check_result_JYY_GH.setText(JYYGH);

			if (proRfidFkbBeanList.size() > 0) {
				for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
					totalNum = totalNum.add(proRfidFkbBeanList.get(i).getSl());
					HGNum = HGNum.add(proRfidFkbBeanList.get(i).getHgsl());
				}
				BigDecimal BHGNum = totalNum.subtract(HGNum);
				tv_check_result_BHGSL.setText("不合格：" + BHGNum.toString());
				tv_check_result_HGSL.setText("合格：" + HGNum.toString());
				tv_check_result_ZSL.setText("总数量：" + totalNum.toString());
			}



			initBHGYYRData();
//			initBHGZZRData();

		}
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
			 * 读卡不合格责任人
			 */
			/*case R.id.tv_check_result_read_BHGZRR_card:
				readCheckEmpNoByMobileDataTerminal();
				break;*/
			/**
			 * 读工号
			 */
			/*case R.id.tv_check_result_read_JGZGH_card:
				readEmpNoByMobileDataTerminal();
				break;*/


			/**
			 * 读卡
			 */
			case R.id.tv_check_result_read_BHGBQH_card:
				readTidByMobileDataTerminal();
				break;


			/**
			 * 提交
			 */
			case R.id.tv_check_submit:

				/*JGYGGH = et_JGZGH.getText().toString();
				if (StringUtil.isEmpty(JGYGGH)) {
					Toast.makeText(WMSRfidCheckResultActivity.this, "请读取加工员工工号！", Toast.LENGTH_SHORT).show();
					return;
				}*/
				if (StringUtil.isEmpty(JYYGH)) {
					Toast.makeText(WMSRfidCheckResultActivity.this, "请读取检验员工号！", Toast.LENGTH_SHORT).show();
					return;
				}

				BigDecimal totalNum = new BigDecimal(0);
				BigDecimal HGNum = new BigDecimal(0);
				BigDecimal BHGNum = new BigDecimal(0);
				BigDecimal sumBheg1 = new BigDecimal(0);
				if (proRfidFkbBeanList.size() > 0) {
					for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
						totalNum = totalNum.add(proRfidFkbBeanList.get(i).getSl());
						HGNum = HGNum.add(proRfidFkbBeanList.get(i).getHgsl());
						BHGNum=totalNum.subtract(HGNum);
					}
				}

				if(addJyfpBeanList.size()>0){
					for (int i = 0; i < addJyfpBeanList.size(); i++) {
						sumBheg1 = sumBheg1.add(addJyfpBeanList.get(i).getFpsl());
					}
				}


				if (BHGNum.compareTo(sumBheg1) != 0) {

					return;
				}

				submitData();
				break;

			default:
				break;


			/**
			 * 添加
			 */
			case R.id.tv_fp_add:
				String zrr = et_ZRR.getText().toString().trim();
				String zrrgh = et_BHGZRR.getText().toString().trim();
				if("0".equals(bol)){
					if (StringUtil.isEmpty(zrr)) {
						Toast.makeText(WMSRfidCheckResultActivity.this, "请读取责任人！", Toast.LENGTH_SHORT).show();
						return;
					}
				}

				BigDecimal totalNum1 = new BigDecimal(0);
				BigDecimal HGNum1 = new BigDecimal(0);
				BigDecimal bhgsl1=new BigDecimal(0);
				BigDecimal sumBheg=new BigDecimal(0);
				if (proRfidFkbBeanList.size() > 0) {
					for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
						totalNum1 = totalNum1.add(proRfidFkbBeanList.get(i).getSl());
						HGNum1 = HGNum1.add(proRfidFkbBeanList.get(i).getHgsl());
					}
					BHGNum = totalNum1.subtract(HGNum1);
					String bhgsl= et_bhg_SL.getText().toString().trim();
					if(addJyfpBeanList.size()>0){
						for (int i = 0; i < addJyfpBeanList.size(); i++) {
							sumBheg = sumBheg.add(addJyfpBeanList.get(i).getFpsl());
						}
					}
					if("0".equals(bol)) {
						if (StringUtil.isEmpty(bhgsl)) {
							Toast.makeText(WMSRfidCheckResultActivity.this, "请输入不合格数量！", Toast.LENGTH_SHORT).show();
							return;
						}
						bhgsl1=new BigDecimal(bhgsl);
						BigDecimal addBhg=sumBheg.add(bhgsl1);
						if(addBhg.compareTo(BHGNum) > 0){

							return;
						}
						if (bhgsl1.compareTo(BHGNum) > 0) {

							return;
						}
						if (totalNum1.compareTo(HGNum1) == 0) {

							return;
						}
					}
				}
				String fpgx = (String) spinner_FPGX.getSelectedItem();
				if("0".equals(bol)) {

					if (StringUtil.isEmpty(fpgx)) {
						Toast.makeText(WMSRfidCheckResultActivity.this, "请选择废品工序！", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				String fpyy=et_check_BHGYY.getText().toString().trim();
				if(StringUtil.isEmpty(fpyy)){
					Toast.makeText(WMSRfidCheckResultActivity.this, "请输入备注！", Toast.LENGTH_SHORT).show();
					return;
				}
				String bqh = (String) et_FPBQH.getText().toString().trim();
				if("0".equals(bol)) {
					if (StringUtil.isEmpty(bqh)) {
						Toast.makeText(WMSRfidCheckResultActivity.this, "请读卡获取不合格车标签号！", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				BigDecimal sumHgs=new BigDecimal(0);
				BigDecimal sumZs=new BigDecimal(0);
				BigDecimal sumBhg=new BigDecimal(0);
				if(proRfidFkbBeanList.size() > 0){
					for(int i=0;i<proRfidFkbBeanList.size();i++){
						String bqh1=proRfidFkbBeanList.get(i).getBqh();
						if(bqh1.equals(bqh)){
							sumHgs=proRfidFkbBeanList.get(i).getHgsl();
							sumZs=proRfidFkbBeanList.get(i).getSl();
							sumBhg=sumZs.subtract(sumHgs);
							if(bhgsl1.compareTo(sumBhg) > 0){

								return;
							}

							BigDecimal sumZbHgs=new BigDecimal(0);
							if(addJyfpBeanList.size() > 0){
								for(int k=0;k<addJyfpBeanList.size();k++){
									String bqh3=addJyfpBeanList.get(k).getBqh();
									if(bqh3.equals(bqh)){
										sumZbHgs = sumZbHgs.add(addJyfpBeanList.get(k).getFpsl());
									}
								}
							}
							BigDecimal sumBhg1=sumZbHgs.add(bhgsl1);
							if(sumBhg1.compareTo(sumBhg) > 0){

								return;
							}
						}

					}
				}



				addJyfpBean addJyfpBean=new addJyfpBean();
				addJyfpBean.setBqh(bqh);
				addJyfpBean.setFpgx(fpgx);
				addJyfpBean.setFpsl(bhgsl1);
				addJyfpBean.setFpyy(fpyy);
				addJyfpBean.setZrr(zrr);
				addJyfpBean.setZrrgh(zrrgh);
				addJyfpBean.setIsCheck(false);
				addJyfpBeanList.add(addJyfpBean);
				adapter1 = new WMSRFIDCheckTableAdapter(WMSRfidCheckResultActivity.this, addJyfpBeanList);
				tableListView.setAdapter(adapter1);
				cleanData();
				break;


			/**
			 * 删除
			 */
			case R.id.tv_fp_delete:
				for (int i = 0; i < addJyfpBeanList.size(); i++) {
					addJyfpBean addJyfpBean1 = addJyfpBeanList.get(i);
					if (addJyfpBean1.getIsCheck()) {
						addJyfpBeanList.remove(i--);
					}
				}
				tableListView.setAdapter(adapter1);
				adapter1.notifyDataSetChanged();
				break;
		}






	}

	public static void cleanData() {

		et_ZRR.setText("");
		et_BHGZRR.setText("");
		spinner_FPGX.setSelection(0);
		et_bhg_SL.setText("");
		spinner_check_BHGYY.setSelection(0);
	}

	/**
	 * 提交数据
	 *
	 */
	private void submitData() {
		progressDialog = ProgressDialog.show(WMSRfidCheckResultActivity.this, "请稍等", "正在提交数据...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckResultActivity.this) + "/appscjController.do?checkOut";
				String result = "";
				try {

					JSONArray jsonArr = new JSONArray();
					if (proRfidFkbBeanList.size() > 0) {
						for (int i = 0; i < proRfidFkbBeanList.size(); i++) {
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("tid", proRfidFkbBeanList.get(i).getRfidTid());
							jsonObj.put("sl", proRfidFkbBeanList.get(i).getSl());
							jsonObj.put("hgsl", proRfidFkbBeanList.get(i).getHgsl());
							jsonObj.put("bh", proRfidFkbBeanList.get(i).getDbh());
							jsonArr.put(jsonObj);
						}
						JSONArray jsonArr1 = new JSONArray();
						if (addJyfpBeanList.size() > 0) {
							for (int i = 0; i < addJyfpBeanList.size(); i++) {
								JSONObject jsonObj1 = new JSONObject();
								jsonObj1.put("fpgx", addJyfpBeanList.get(i).getFpgx());
								jsonObj1.put("fpsl", addJyfpBeanList.get(i).getFpsl());
								jsonObj1.put("zrrgh", addJyfpBeanList.get(i).getZrrgh());
								jsonObj1.put("zrr", addJyfpBeanList.get(i).getZrr());
								jsonObj1.put("fpyy", addJyfpBeanList.get(i).getFpyy());
								jsonObj1.put("bqh", addJyfpBeanList.get(i).getBqh());
								jsonArr1.put(jsonObj1);
							}

						}


						// url += "&data="+jsonArr.toString()+"&cpbm=" + proRfidFkbBean.getCpbm() + "&lph" + proRfidFkbBean.getLph() + "&gylxh=" + proRfidFkbBean.getGylxh();
						// result = GetHttp.RequstGetHttp(url);
						BigDecimal BHGNum = totalNum.subtract(HGNum);
						Map<String, String> paramsMap = new HashMap<String, String>();
						paramsMap.put("tidAndslList", jsonArr.toString());// tid,sl的json数组
						paramsMap.put("wasteList", jsonArr1.toString());// tid,sl的json数组

						//paramsMap.put("memo", JGYGGH);// 加工者工号
						paramsMap.put("jyymemo", JYYGH);// 检验员工号
						String BHGZRR = et_BHGZRR.getText() + "";
						String cphyyy=et_check_BHGYY.getText().toString().trim();;
						paramsMap.put("cphy",bol);
						paramsMap.put("bhgyy",cphyyy);


						result = PostHttp.RequstPostHttpNew(WMSRfidCheckResultActivity.this, url, paramsMap);
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
								Toast.makeText(WMSRfidCheckResultActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
								WMSRfidCheckResultActivity.this.finish();
								WMSRfidCheckReadActivity.cleanData();
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
	 * 初始不合格责任人
	 *//*
	private void initBHGZZRData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckResultActivity.this) + "/appController.do?getTSUserEmpNoAndRealName";
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					JSONObject attributes = jsonObj.getJSONObject("attributes");
					JSONArray dataArr = attributes.getJSONArray("data");
					// String data[] = new String[] { "请选择产品名称",
					// "BBT000007|驱动轴",
					// "BBT000008|曲轴" };
					dataZRR = new String[dataArr.length() + 1];
					dataZRR[0] = "";
					for (int i = 1; i <= dataArr.length(); i++) {
						JSONObject data = dataArr.getJSONObject(i - 1);
						String emp_no = (String) data.get("emp_no");
						String realname = (String) data.get("realname");
						dataZRR[i] = emp_no + "|" + realname;

					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ArrayAdapter<String> adapterZRR = new ArrayAdapter<String>(WMSRfidCheckResultActivity.this, R.layout.spinner_item, dataZRR);
							// 加载item的布局样式
							adapterZRR.setDropDownViewResource(R.layout.spinner_item);
							// 最后让Spinner加载adapter
							spinner_check_BHGZRR.setAdapter(adapterZRR);
						}
					});

				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "获取接口数据失败，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();
	}
*/




	/**
	 * 初始不合格原因
	 */
	private void initBHGYYRData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckResultActivity.this) + "/appscjController.do?getAllUnQualifiedReason";
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				try {
					JSONObject jsonObj = new JSONObject(result);
					JSONObject attributes = jsonObj.getJSONObject("attributes");
					JSONArray dataArr = attributes.getJSONArray("data");
					// String data[] = new String[] { "请选择产品名称",
					// "BBT000007|驱动轴",
					// "BBT000008|曲轴" };
					dataZRYY = new String[dataArr.length() + 1];
					dataZRYY[0] = "";
					for (int i = 1; i <= dataArr.length(); i++) {
						JSONObject data = dataArr.getJSONObject(i - 1);
						String yynr = (String) data.get("yynr");
						dataZRYY[i] = yynr;

					}

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							ArrayAdapter<String> adapterYY = new ArrayAdapter<String>(WMSRfidCheckResultActivity.this, R.layout.spinner_item, dataZRYY);
							// 加载item的布局样式
							adapterYY.setDropDownViewResource(R.layout.spinner_item);
							// 最后让Spinner加载adapter
							spinner_check_BHGYY.setAdapter(adapterYY);
						}
					});

				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "获取接口数据失败，请联系管理员", Toast.LENGTH_SHORT).show();
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
		/*Application app = WMSRfidCheckResultActivity.this.getApplication();
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
						Toast.makeText(WMSRfidCheckResultActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
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
	 * 读取检验员
	 */
	private void readCheckEmpNoByMobileDataTerminal() {
		try {
			String tid = RfidRead.getTidValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSRfidCheckResultActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
				getCheckUserInfoByTid(tid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		finally {

		}
	}

	/***
	 * 通过TID获取员工信息
	 *
	 * @param
	 */
	private void getUserInfoByTid(final String tid) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckResultActivity.this) + "/appscjController.do?getTSUserEmpNoByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
						final String empNo = attributes.getString("empNo");
						final String username = attributes.getString("username");
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								/*et_JGZGH.setText(empNo);
								et_JGZ.setText(username);*/
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidCheckResultActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "获取接口数据失败，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();

	}

	/***
	 * 通过TID获取员工信息
	 *
	 * @param
	 */
	private void getCheckUserInfoByTid(final String tid) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckResultActivity.this) + "/appscjController.do?getTSUserEmpNoByTid&tid=" + tid;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
						final String empNo = attributes.getString("empNo");
						final String username = attributes.getString("username");

						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								et_BHGZRR.setText(empNo);
								et_ZRR.setText(username);
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(WMSRfidCheckResultActivity.this, msg, Toast.LENGTH_SHORT).show();
							}
						});
					}

				} catch (JSONException e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "获取接口数据失败，请联系管理员", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		}).start();

	}




	private void readTidByMobileDataTerminal() {
		try {
			String tid = RfidRead.getTidValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSRfidCheckResultActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
				selectBqhByTid(tid);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}
		finally {

		}
	}

	private void selectBqhByTid(final String tid1) {
		progressDialog = ProgressDialog.show(WMSRfidCheckResultActivity.this, "请稍等", "正在获取RFID卡信息...", true, false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				String url = URLHelper.getBaseUrl(WMSRfidCheckResultActivity.this) + "/appscjController.do?getMesRfidFkbOnCheckByTid&tid=" + tid1;
				String result = "";
				try {
					result = GetHttp.RequstGetHttp(url);
				} catch (Exception e1) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WMSRfidCheckResultActivity.this, "网络已断开或者服务器停止，请联系管理员", Toast.LENGTH_SHORT).show();
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
									String BQH = json.getString("bqh");
									et_FPBQH.setText(BQH);
									getGxBybqh(BQH);

								} catch (JSONException e) {

								}

							}
						});
					} else {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// Toast.makeText(WMSRfidCheckReadActivity.this, msg, Toast.LENGTH_SHORT).show();

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

	private void checkHjhByMobileDataTerminal() {
		try {
			String tid = RfidRead.getTidValue(myapp);
			if (StringUtil.isEmpty(tid)) {
				soundPoolerr.play(1, 1, 1, 0, 0, 1);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WMSRfidCheckResultActivity.this, "读卡失败，请把RFID卡离手持机在10cm内！", Toast.LENGTH_SHORT).show();
					}
				});
			} else {
				soundPool.play(1, 1, 1, 0, 0, 1);
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
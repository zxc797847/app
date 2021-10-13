package com.avic.demo.sebeiglzx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.bean.CkBean;
import com.avic.demo.sebeiglzx.http.GetHttp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NetworkConfigActivity extends Activity {

    private Button btn_home_network_config_save;
    private Button btn_home_network_config_getCk;
    private EditText home_network_config_ip, home_network_config_port, home_network_config_proName;
    private String home_network_config_ip_value, home_network_config_port_value, home_network_config_proname_value;
    private SharedPreferences sp;
    private Spinner home_network_config_spinner_ck;
    // 返回按钮
    private ImageView iv_back;
    private String ckIdValue = "";
    private CkBean ckbean = null;
    private ArrayAdapter<CkBean> ckAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_config);
        initView();
    }

    private void initView() {
        // 获得实例对象
        sp = this.getSharedPreferences("networkConfigInfo", Context.MODE_PRIVATE);
        btn_home_network_config_save = (Button) findViewById(R.id.btn_home_network_config_save);
        btn_home_network_config_getCk = (Button) findViewById(R.id.btn_home_network_config_getCk);
        home_network_config_ip = (EditText) findViewById(R.id.home_network_config_ip);
        home_network_config_port = (EditText) findViewById(R.id.home_network_config_port);
        home_network_config_proName = (EditText) findViewById(R.id.home_network_config_proName);
        home_network_config_spinner_ck = (Spinner) findViewById(R.id.home_network_config_spinner_ck);
        // 获取网络连接配置配置内容并且显示
        home_network_config_ip.setText(sp.getString("NETWORK_CONFIG_IP", ""));
        home_network_config_port.setText(sp.getString("NETWORK_CONFIG_PORT", ""));
        home_network_config_proName.setText(sp.getString("NETWORK_CONFIG_PRONAME", ""));

        // 保存
        btn_home_network_config_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                home_network_config_ip_value = home_network_config_ip.getText().toString();
                home_network_config_port_value = home_network_config_port.getText().toString();
                home_network_config_proname_value = home_network_config_proName.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("NETWORK_CONFIG_IP", home_network_config_ip_value);
                editor.putString("NETWORK_CONFIG_PORT", home_network_config_port_value);
                editor.putString("NETWORK_CONFIG_PRONAME", home_network_config_proname_value);
                editor.commit();
                Toast.makeText(v.getContext(), "保存成功！", Toast.LENGTH_SHORT).show();
            }
        });

    }

}

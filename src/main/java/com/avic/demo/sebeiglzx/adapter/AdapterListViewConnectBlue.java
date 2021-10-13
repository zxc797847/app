package com.avic.demo.sebeiglzx.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avic.demo.sebeiglzx.bean.ConnectBlueBean;
import com.avic.demo.sebeiglzx.R;

import java.util.List;

public class AdapterListViewConnectBlue extends BaseAdapter {

    private Context mContext;
    private List<ConnectBlueBean> mDatas;
    private LayoutInflater mInflater;
    public boolean flage = true;

    public AdapterListViewConnectBlue(Context mContext, List<ConnectBlueBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(this.mContext);
    }

    public AdapterListViewConnectBlue(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.adapter_connect_blue, null);
            holder = new ViewHolder();
            holder.cb_isCheck = (CheckBox) convertView.findViewById(R.id.cb_isCheck);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_rssivalue = (TextView) convertView.findViewById(R.id.tv_rssivalue);
            holder.tv_conn_status = (TextView) convertView.findViewById(R.id.tv_conn_status);
            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final ConnectBlueBean ConnectBlueBean = mDatas.get(position);
        if (ConnectBlueBean != null) {
            holder.tv_name.setText("蓝牙名称：" + ConnectBlueBean.name);
            holder.tv_address.setText("蓝牙地址：" + ConnectBlueBean.address);
            holder.tv_type.setText("蓝牙类型：" + ConnectBlueBean.type);
            holder.tv_rssivalue.setText("蓝牙型号：" + ConnectBlueBean.rssivalue);
            if (ConnectBlueBean.isConnect) {
                holder.tv_conn_status.setText("已连接");
                holder.tv_conn_status.setBackgroundColor(R.color.blue);
            } else {
                holder.tv_conn_status.setText("未连接");
                holder.tv_conn_status.setBackgroundColor(R.color.red);
            }

            // 根据isSelected来设置checkbox的显示状况
            holder.cb_isCheck.setVisibility(View.VISIBLE);
            holder.cb_isCheck.setChecked(ConnectBlueBean.isCheck);
            // 注意这里设置的不是onCheckedChangListener，还是值得思考一下的
            holder.cb_isCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ConnectBlueBean.isCheck) {
                        ConnectBlueBean.isCheck = false;
                    } else {
                        ConnectBlueBean.isCheck = true;
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        public CheckBox cb_isCheck;

        public TextView tv_name;
        public TextView tv_address;
        public TextView tv_type;
        public TextView tv_rssivalue;
        public TextView tv_conn_status;
    }

}

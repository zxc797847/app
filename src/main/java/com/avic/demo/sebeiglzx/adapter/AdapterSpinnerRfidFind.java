package com.avic.demo.sebeiglzx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avic.demo.sebeiglzx.bean.ProRfidFindSpinnerBean;
import com.avic.demo.sebeiglzx.R;

import java.util.List;

public class AdapterSpinnerRfidFind extends BaseAdapter {
    private Context mContext;
    private List<ProRfidFindSpinnerBean> mDatas;
    private LayoutInflater mInflater;

    public AdapterSpinnerRfidFind(Context mContext, List<ProRfidFindSpinnerBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(this.mContext);
    }

    public AdapterSpinnerRfidFind(Context context) {
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

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {


        ViewHolder holder ;
        if (view == null){
            view = mInflater.inflate(R.layout.adapter_spinner_rfid_find,null);
            holder = new ViewHolder();
            holder.checkboxOperateData = (CheckBox) view.findViewById(R.id.checkbox_operate_data);
            //holder.checkboxOperateData.setVisibility(View.INVISIBLE);
            holder.tv_bqh = (TextView) view.findViewById(R.id.tv_bqh);
            //holder.tv_bqh.setText("");

            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        final ProRfidFindSpinnerBean proRfidFindSpinnerBean = mDatas.get(i);
        if (proRfidFindSpinnerBean != null) {


            //holder.tv_bqh.setText("下面是标签号");
            //holder.tv_bqh.setText(i+"."+proRfidFindSpinnerBean.getBqh());
            //holder.tv_bqh.setText("请读卡");
            //根据isSelected来设置checkbox的显示情况
            //holder.checkboxOperateData.setVisibility(View.INVISIBLE);
            //holder.checkboxOperateData.setChecked(proRfidFindSpinnerBean.isCheck());

            holder.tv_bqh.setText((i+1)+"、标签号："+proRfidFindSpinnerBean.getBqh());
            holder.checkboxOperateData.setVisibility(View.VISIBLE);
            holder.checkboxOperateData.setChecked(proRfidFindSpinnerBean.isCheck());




        }else{
            holder.tv_bqh.setText("请读卡");
            holder.checkboxOperateData.setVisibility(View.INVISIBLE);
        }

        //选择框
        holder.checkboxOperateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (proRfidFindSpinnerBean.isCheck()) {
                    proRfidFindSpinnerBean.setCheck(false);
                } else {
                    proRfidFindSpinnerBean.setCheck(true);
                }
            }
        });
        return view;
    }



    class ViewHolder {

        public CheckBox checkboxOperateData;
        public TextView tv_bqh;
    }

}

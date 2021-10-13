package com.avic.demo.sebeiglzx.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avic.demo.sebeiglzx.bean.ProRfidFindListViewBean;
import com.avic.demo.sebeiglzx.R;

import java.util.List;

public class AdapterListViewRfidFind extends BaseAdapter {

    private Context mContext;
    private List<ProRfidFindListViewBean> listViewData;
    private LayoutInflater mInflater;

    public AdapterListViewRfidFind(Context mContext, List<ProRfidFindListViewBean> listViewData) {
        this.mContext = mContext;
        this.listViewData = listViewData;
        mInflater = LayoutInflater.from(this.mContext);
    }

    public AdapterListViewRfidFind(Context context) {
        this.mContext = context;

    }




    @Override
    public int getCount() {
        return listViewData.size();
    }

    @Override
    public Object getItem(int i) {
        return listViewData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (view == null){
            view = mInflater.inflate(R.layout.adapter_listview_rfid_find,null);
            holder = new ViewHolder();
            holder.checkbox_list_view = (CheckBox) view.findViewById(R.id.checkbox_list_view);
            holder.tv_list_view_bqh = (TextView) view.findViewById(R.id.tv_list_view_bqh);


            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        final ProRfidFindListViewBean proRfidFindListViewBean = listViewData.get(i);
        if (proRfidFindListViewBean != null) {
            if (!(holder.tv_list_view_bqh.getText().toString()).equals(i+1+"."+proRfidFindListViewBean.getBqh())){
                holder.tv_list_view_bqh.setText(i+1+"、标签号："+proRfidFindListViewBean.getBqh());

                //根据isSelected来设置checkbox的显示情况
                holder.checkbox_list_view.setVisibility(View.VISIBLE);
                holder.checkbox_list_view.setChecked(proRfidFindListViewBean.isListview_check());
                Log.e("=====>>记录",proRfidFindListViewBean.getEpc()+"======"+proRfidFindListViewBean.getBqh()+"==========="+proRfidFindListViewBean.isListview_check());
            }

        }

        if (proRfidFindListViewBean.isListview_check()) {
            view.setBackgroundColor(Color.RED);
            holder.tv_list_view_bqh.setTextColor(Color.WHITE);
        }else{
            view.setBackgroundColor(Color.WHITE);
            holder.tv_list_view_bqh.setTextColor(Color.BLACK);
        }
        //选择框
        holder.checkbox_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (proRfidFindListViewBean.isListview_check()) {
                    proRfidFindListViewBean.setListview_check(false);
                } else {
                    proRfidFindListViewBean.setListview_check(true);
                }
            }
        });
        return view;
    }



    class ViewHolder {

        public CheckBox checkbox_list_view;
        public TextView tv_list_view_bqh;
    }
}

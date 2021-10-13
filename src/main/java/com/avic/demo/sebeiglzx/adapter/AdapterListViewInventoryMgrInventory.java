package com.avic.demo.sebeiglzx.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.bean.PddMxBean;
import com.avic.demo.sebeiglzx.bean.ProRfidFkbBean;

import java.util.List;

public  class AdapterListViewInventoryMgrInventory extends BaseAdapter {

    private Context mContext;
    private List<PddMxBean> mDatas;
    private LayoutInflater mInflater;

    public AdapterListViewInventoryMgrInventory(Context mContext, List<PddMxBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;

        mInflater = LayoutInflater.from(this.mContext);

    }

    public AdapterListViewInventoryMgrInventory(Context context) {
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.adapter_listview_inventory_mgr_inventory, null);
            holder = new ViewHolder();
            holder.checkboxOperateData = (CheckBox) convertView.findViewById(R.id.checkbox_operate_data);
            holder.tv_pd_bqh = (TextView) convertView.findViewById(R.id.tv_pd_bqh);
            holder.tv_pd_cpmc = (TextView) convertView.findViewById(R.id.tv_pd_cpmc);
            holder.tv_pd_sybm = (TextView) convertView.findViewById(R.id.tv_pd_sybm);
            holder.tv_pd_szwz = (TextView) convertView.findViewById(R.id.tv_pd_szwz);
            holder.tv_pd_sbbh = (TextView) convertView.findViewById(R.id.tv_pd_sbbh);
            holder.tv_pd_cpgg = (TextView) convertView.findViewById(R.id.tv_pd_cpgg);
            holder.tv_pd_zbfzr = (TextView) convertView.findViewById(R.id.tv_pd_sbfzr);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

      final PddMxBean pddMxBean = mDatas.get(position);
        if (pddMxBean != null) {
            holder.tv_pd_bqh.setText((position+1)+"标签号:"+ pddMxBean.getBqh());
            holder.tv_pd_cpmc.setText("设备名称:" +pddMxBean.getCpmc());
            holder.tv_pd_sybm .setText("使用部门:" +pddMxBean.getSybm());
            holder.tv_pd_szwz.setText("所在位置:" + pddMxBean.getSzwz());
            holder.tv_pd_sbbh.setText("设备编号:" + pddMxBean.getSbbh());
            holder.tv_pd_cpgg.setText("设备规格:" + pddMxBean.getCpgg());
            holder.tv_pd_zbfzr.setText("设备负责人:" + pddMxBean.getSbfzr());

            // 根据isSelected来设置checkbox的显示状况
            holder.checkboxOperateData.setVisibility(View.VISIBLE);
            holder.checkboxOperateData.setChecked(pddMxBean.getIsCheck());

        }

        // 注意这里设置的不是onCheckedChangListener，还是值得思考一下的
        holder.checkboxOperateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pddMxBean.getIsCheck()) {
                    pddMxBean.setIsCheck(false);
                } else {
                    pddMxBean.setIsCheck(true);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {

        public CheckBox checkboxOperateData;
        public TextView tv_pd_bqh;
        public TextView tv_pd_cpmc;
        public TextView tv_pd_sybm;
        public TextView tv_pd_szwz;
        public TextView tv_pd_sbbh;
        public TextView tv_pd_cpgg;
        public TextView tv_pd_zbfzr;
    }
}

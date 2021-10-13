package com.avic.demo.sebeiglzx.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.bean.InspectBean;

import java.util.HashMap;
import java.util.List;

public class AdapterListViewinspect extends BaseAdapter {
    private Context mContext;
    private List<InspectBean> mDatas;
    private LayoutInflater mInflater;
    private HashMap<String,Boolean> states=new HashMap<String,Boolean>();
    private SparseIntArray checked = new SparseIntArray();
    private int optionID1, optionID2;

    public AdapterListViewinspect(Context mContext, List<InspectBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(this.mContext);

    }

    public AdapterListViewinspect(Context context) {
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
    private Integer index = -1;
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.adapter_listview_inspect, null);
            holder = new ViewHolder();
            holder.checkboxOperateData = (CheckBox) convertView.findViewById(R.id.checkbox_inspect_datal);
            holder.tv_jcx = (TextView) convertView.findViewById(R.id.tv_jcx);
            holder.rg = (RadioGroup) convertView.findViewById(R.id.radioGroup1);
           holder.red_true=(RadioButton)convertView.findViewById(R.id.red_true);
           optionID1 = holder.red_true.getId();
           holder.red_false=(RadioButton)convertView.findViewById(R.id.red_false);
           optionID2 = holder.red_false.getId();

           holder.rg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = (Integer) v.getTag();
                    }
                    return false;
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.rg.setTag(position);
        }

        final InspectBean inspectBean = mDatas.get(position);
        if (inspectBean != null) {
            holder.tv_jcx.setText((position+1)+"、检查项:" + inspectBean.getJcx());

            // 根据isSelected来设置checkbox的显示状况
            holder.checkboxOperateData.setVisibility(View.VISIBLE);
            holder.checkboxOperateData.setChecked(inspectBean.getIsCheck());
        }

        // 注意这里设置的不是onCheckedChangListener，还是值得思考一下的
        holder.checkboxOperateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inspectBean.getIsCheck()) {
                    inspectBean.setIsCheck(false);
                } else {
                    inspectBean.setIsCheck(true);
                }
            }
        });
       //设置单项按钮RadioGroup/RadioButton
        holder.rg.setOnCheckedChangeListener(null);
        if (checked.indexOfKey(position) > -1) {
            holder.rg.check(checked.get(position));
        } else {
            checked.put(position,optionID1);
        }
        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if(checkedId > -1){
                    checked.put(position, checkedId);
                    RadioButton rbtn = (RadioButton) group.findViewById(checkedId);
                    if (rbtn.getText().toString().equals("正常")) {
                        inspectBean.setTrueCheck(true);
                    } else if (rbtn.getText().toString().equals("异常")) {
                        inspectBean.setTrueCheck(false);
                    }
                }else {
                    if (checked.indexOfKey(position) > -1) {
                        checked.removeAt(checked.indexOfKey(position));
                    }
                }
            }
        });
        holder.rg.clearFocus();
        if (index != -1 && index == position) {
            holder.rg.requestFocus();
        }
        return convertView;
    }
    class ViewHolder {

        public CheckBox checkboxOperateData;
        public TextView tv_jcx;
        public RadioButton red_true;
        public RadioButton red_false;
        public RadioGroup rg;
    }
}

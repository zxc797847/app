package com.function;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.avic.demo.sebeiglzx.R;

import java.util.List;
import java.util.Map;

public class ReadTemperatureListAdapter extends BaseAdapter {

    List<Map<String, String>> temList ;
    private Context mContext;
    public ReadTemperatureListAdapter(List<Map<String, String>> temList,Context context){
        this.temList = temList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return temList==null?0:temList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_tem_list,parent,false);
            holder = new ViewHolder();
            holder.tv_epc = (TextView) convertView.findViewById(R.id.textView_temList_epc);
            holder.tv_tem = (TextView) convertView.findViewById(R.id.textView_temList_temperature);
            convertView.setTag(holder);   //将Holder存储到convertView中
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_epc.setText(temList.get(position).get("epc"));
        holder.tv_tem.setText(temList.get(position).get("tem"));
        return convertView;
    }
    static class ViewHolder{
        TextView tv_epc;
        TextView tv_tem;
    }

}

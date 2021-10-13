package com.avic.demo.sebeiglzx.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.bean.addJyfpBean;


public class WMSRFIDCheckTableAdapter extends BaseAdapter {
	private List<addJyfpBean> list;
	private LayoutInflater inflater;

	public WMSRFIDCheckTableAdapter(Context context, List<addJyfpBean> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		int ret = 0;
		if (list != null) {
			ret = list.size();
		}
		return ret;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final addJyfpBean addJyfpBean = (addJyfpBean) this.getItem(position);

		ViewHolder viewHolder;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = inflater.inflate(R.layout.list_addfp, null);
			viewHolder.goodId = (TextView) convertView
					.findViewById(R.id.text_id);
			viewHolder.tv_FPGX = (TextView) convertView
					.findViewById(R.id.tv_FPGX);
			viewHolder.tv_ZRR = (TextView) convertView
					.findViewById(R.id.tv_ZRR);
			viewHolder.tv_BQH = (TextView) convertView.findViewById(R.id.tv_BQH);
			viewHolder.tv_FPSL = (TextView) convertView.findViewById(R.id.tv_FPSL);
			
			viewHolder.ck_XZ = (CheckBox) convertView.findViewById(R.id.ck_XZ1);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
	
		viewHolder.goodId.setText((position + 1) + "");
		viewHolder.goodId.setTextSize(13);
		viewHolder.tv_FPGX.setText(addJyfpBean.getFpgx());
		viewHolder.tv_FPGX.setTextSize(13);
		viewHolder.tv_ZRR.setText(addJyfpBean.getZrr());
		viewHolder.tv_ZRR.setTextSize(13);
		viewHolder.tv_BQH.setText(addJyfpBean.getBqh());
		viewHolder.tv_BQH.setTextSize(13);
		viewHolder.tv_FPSL.setText(addJyfpBean.getFpsl() + "");
		viewHolder.tv_FPSL.setTextSize(13);
		viewHolder.ck_XZ.setChecked(addJyfpBean.getIsCheck());

		viewHolder.ck_XZ
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (addJyfpBean.getIsCheck()) {
							addJyfpBean.setIsCheck(false);
						} else {
							addJyfpBean.setIsCheck(true);
						}
					}
				});

		return convertView;
	}

	public static class ViewHolder {
		public TextView goodId;
		public TextView tv_FPGX;
		public CheckBox ck_XZ;
		public TextView tv_FPSL;
		public TextView tv_ZRR;
		public TextView tv_BQH;
	}

}
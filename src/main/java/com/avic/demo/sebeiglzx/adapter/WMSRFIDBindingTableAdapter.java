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
import com.avic.demo.sebeiglzx.bean.InitProRfidFkbBean;

public class WMSRFIDBindingTableAdapter extends BaseAdapter {
	private List<InitProRfidFkbBean> list;
	private LayoutInflater inflater;

	public WMSRFIDBindingTableAdapter(Context context, List<InitProRfidFkbBean> list) {
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

		final InitProRfidFkbBean proRfidFkbBean = (InitProRfidFkbBean) this.getItem(position);

		ViewHolder viewHolder;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = inflater.inflate(R.layout.list_wms_bingdin_new_item, null);
			viewHolder.goodId = (TextView) convertView
					.findViewById(R.id.text_id);
			viewHolder.tv_BQH = (TextView) convertView
					.findViewById(R.id.tv_BQH);
			viewHolder.order_no = (TextView) convertView
					.findViewById(R.id.order_no);
			viewHolder.tv_DBH = (TextView) convertView.findViewById(R.id.tv_DBH);
			viewHolder.ck_XZ = (CheckBox) convertView.findViewById(R.id.ck_XZ);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.goodId.setText((position + 1) + "");
		viewHolder.goodId.setTextSize(13);
		viewHolder.tv_DBH.setText(proRfidFkbBean.getDbh());
		viewHolder.tv_DBH.setTextSize(13);
		viewHolder.tv_BQH.setText(proRfidFkbBean.getBqh());
		viewHolder.tv_BQH.setTextSize(13);
		viewHolder.order_no.setText(proRfidFkbBean.getOrderNo());
		viewHolder.order_no.setTextSize(13);
		viewHolder.ck_XZ.setChecked(proRfidFkbBean.getIsCheck());

		viewHolder.ck_XZ
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (proRfidFkbBean.getIsCheck()) {
							proRfidFkbBean.setIsCheck(false);
						} else {
							proRfidFkbBean.setIsCheck(true);
						}
					}
				});

		return convertView;
	}

	public static class ViewHolder {
		public TextView goodId;
		public TextView order_no;
		public CheckBox ck_XZ;
		public TextView tv_BQH;
		public TextView tv_DBH;
		
	}

}
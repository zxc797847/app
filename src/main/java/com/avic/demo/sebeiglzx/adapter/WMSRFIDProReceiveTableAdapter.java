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


public class WMSRFIDProReceiveTableAdapter extends BaseAdapter {
	private List<InitProRfidFkbBean> list;
	private LayoutInflater inflater;

	public WMSRFIDProReceiveTableAdapter(Context context, List<InitProRfidFkbBean> list) {
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

			convertView = inflater.inflate(R.layout.list_wms_receive_read_item, null);
			viewHolder.goodId = (TextView) convertView
					.findViewById(R.id.text_id);
			viewHolder.tv_CPMC = (TextView) convertView
					.findViewById(R.id.tv_CPMC);
			viewHolder.tv_BQH = (TextView) convertView
					.findViewById(R.id.tv_BQH);
			viewHolder.tv_SL = (TextView) convertView.findViewById(R.id.tv_SL);
			viewHolder.ck_XZ = (CheckBox) convertView.findViewById(R.id.ck_XZ);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.goodId.setText((position + 1) + "");
		viewHolder.goodId.setTextSize(13);
		viewHolder.tv_CPMC.setText(proRfidFkbBean.getCpmc());
		viewHolder.tv_CPMC.setTextSize(13);
		viewHolder.tv_BQH.setText(proRfidFkbBean.getBqh());
		viewHolder.tv_BQH.setTextSize(13);
		viewHolder.tv_SL.setText(proRfidFkbBean.getSl() + "");
		viewHolder.tv_SL.setTextSize(13);
		viewHolder.ck_XZ.setChecked(proRfidFkbBean.getIsCheck());

		// ע���������õĲ���onCheckedChangListener������ֵ��˼��һ�µ�
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
		public TextView tv_CPMC;
		public CheckBox ck_XZ;
		public TextView tv_BQH;
		public TextView tv_SL;
	}

}
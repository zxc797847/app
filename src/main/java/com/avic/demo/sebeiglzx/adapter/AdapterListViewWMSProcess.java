package com.avic.demo.sebeiglzx.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.service.dreams.DreamService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.bean.MesLcgdMxbEntity;
import com.avic.demo.sebeiglzx.utils.tools.StringUtil;

public class AdapterListViewWMSProcess extends BaseAdapter {
	private Context mContext;
	private List<MesLcgdMxbEntity> mDatas;
	private LayoutInflater mInflater;

	public AdapterListViewWMSProcess(Context mContext, List<MesLcgdMxbEntity> mDatas) {
		this.mContext = mContext;
		this.mDatas = mDatas;

		mInflater = LayoutInflater.from(this.mContext);

	}

	public AdapterListViewWMSProcess(Context context) {
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
			// �������
			convertView = mInflater.inflate(R.layout.adapter_listview_wms_process, null);
			holder = new ViewHolder();
			holder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
			holder.tv_JYRQ = (TextView) convertView.findViewById(R.id.tv_JYRQ);
			holder.tv_JYR = (TextView) convertView.findViewById(R.id.tv_JYR);
			holder.tv_JGZ = (TextView) convertView.findViewById(R.id.tv_JGZ);
			holder.tv_JSRQ = (TextView) convertView.findViewById(R.id.tv_JSRQ);
			holder.rl_right = (android.widget.RelativeLayout) convertView.findViewById(R.id.rl_right);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MesLcgdMxbEntity mesLcgdMxbEntity = mDatas.get(position);
		if (mesLcgdMxbEntity != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String XH = StringUtil.isEmpty(mesLcgdMxbEntity.getXh()) ? "" : mesLcgdMxbEntity.getXh();
			String JGNR = StringUtil.isEmpty(mesLcgdMxbEntity.getJgnr()) ? "" : mesLcgdMxbEntity.getJgnr();
			String ZT = StringUtil.isEmpty(mesLcgdMxbEntity.getZt_en()) ? "" : mesLcgdMxbEntity.getZt_en();
			String JGRQStr = "";
			String JSRQStr = "";
			JGRQStr = mesLcgdMxbEntity.getJyrq() == null ? "" : sdf.format(mesLcgdMxbEntity.getJyrq());
			JSRQStr = mesLcgdMxbEntity.getJsrq() == null ? "" : sdf.format(mesLcgdMxbEntity.getJsrq());
			String JGZ = StringUtil.isEmpty(mesLcgdMxbEntity.getJgz()) ? "" : mesLcgdMxbEntity.getJgz();
			String JYY = StringUtil.isEmpty(mesLcgdMxbEntity.getJyy()) ? "" : mesLcgdMxbEntity.getJyy();

			holder.tv_num.setText((position + 1) + "");
			holder.tv_title.setText("【" + XH + "】" + JGNR);
			holder.tv_state.setText(ZT);
			holder.tv_JYRQ.setText("检验日期：" + JGRQStr);
			holder.tv_JSRQ.setText("合格数【" +mesLcgdMxbEntity.getHgs().intValue()+"】,废品数【"+mesLcgdMxbEntity.getFps().intValue()+"】" );
			holder.tv_JGZ.setText("加工者：" + JGZ);
			holder.tv_JYR.setText("检验员：" + JYY);
			if ("finish".equals(mesLcgdMxbEntity.getZt_en())) {
				holder.tv_title.setTextColor(Color.WHITE);
				holder.tv_state.setTextColor(Color.WHITE);
				holder.tv_JYRQ.setTextColor(Color.WHITE);
				holder.tv_JSRQ.setTextColor(Color.WHITE);
				holder.tv_JGZ.setTextColor(Color.WHITE);
				holder.tv_JYR.setTextColor(Color.WHITE);
				Resources resources = mContext.getResources();
				Drawable drawable = resources.getDrawable(com.avic.demo.sebeiglzx.R.drawable.bg_cirle_green);
				holder.tv_num.setBackgroundDrawable(drawable);
				Drawable drawable2 = resources.getDrawable(com.avic.demo.sebeiglzx.R.drawable.bg_process_background_green);
				holder.rl_right.setBackgroundDrawable(drawable2);

			} else if ("working".equals(mesLcgdMxbEntity.getZt_en())) {
				holder.tv_title.setTextColor(Color.WHITE);
				holder.tv_state.setTextColor(Color.WHITE);
				holder.tv_JSRQ.setTextColor(Color.WHITE);
				holder.tv_JYRQ.setTextColor(Color.WHITE);
				holder.tv_JGZ.setTextColor(Color.WHITE);
				holder.tv_JYR.setTextColor(Color.WHITE);
				Resources resources = mContext.getResources();
				Drawable drawable = resources.getDrawable(com.avic.demo.sebeiglzx.R.drawable.bg_cirle_red);
				holder.tv_num.setBackgroundDrawable(drawable);
				Drawable drawable2 = resources.getDrawable(com.avic.demo.sebeiglzx.R.drawable.bg_process_background_red);
				holder.rl_right.setBackgroundDrawable(drawable2);
			} else {
				holder.tv_title.setTextColor(Color.WHITE);
				holder.tv_state.setTextColor(Color.WHITE);
				holder.tv_JYRQ.setTextColor(Color.WHITE);
				holder.tv_JSRQ.setTextColor(Color.WHITE);
				holder.tv_JYR.setTextColor(Color.WHITE);
				holder.tv_JGZ.setTextColor(Color.WHITE);
				Resources resources = mContext.getResources();
				Drawable drawable = resources.getDrawable(com.avic.demo.sebeiglzx.R.drawable.bg_cirle_gray);
				holder.tv_num.setBackgroundDrawable(drawable);
				Drawable drawable2 = resources.getDrawable(com.avic.demo.sebeiglzx.R.drawable.bg_process_background_gray);
				holder.rl_right.setBackgroundDrawable(drawable2);
			}
		}

		return convertView;
	}

	class ViewHolder {
		public TextView tv_num;
		public TextView tv_title;
		public TextView tv_state;
		public TextView tv_JYRQ;
		public TextView tv_JGZ;
		public TextView tv_JYR;
		public TextView tv_JSRQ;
		public android.widget.RelativeLayout rl_right;
	}

}
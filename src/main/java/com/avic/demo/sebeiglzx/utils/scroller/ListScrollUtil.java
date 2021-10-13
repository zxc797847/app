package com.avic.demo.sebeiglzx.utils.scroller;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListScrollUtil {
	/**
	 * ��̬����listview�ĸ߶�
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		int totalHeight = 0;
		View view;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, null, listView);
			// ���Ϊ��Ļ���
			/*int i1 = View.MeasureSpec
					.makeMeasureSpec(SysUtil.getScreenWidth(GewaraShowApp
							.getGewaraShowContext().getApplicationContext()),
							View.MeasureSpec.EXACTLY);*/
			// ������Ļ��ȼ���߶�
			int i2 = View.MeasureSpec.makeMeasureSpec(600,400);
			view.measure(600, i2);
			totalHeight += view.getMeasuredHeight();
		}
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}

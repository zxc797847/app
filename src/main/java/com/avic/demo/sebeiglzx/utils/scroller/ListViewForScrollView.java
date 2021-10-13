package com.avic.demo.sebeiglzx.utils.scroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class ListViewForScrollView extends ListView {
	public ListViewForScrollView(Context context) {
		super(context);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ListViewForScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	/**
	 * ��д�÷������ﵽʹListView��ӦScrollView��Ч��
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//				MeasureSpec.AT_MOST);
//		super.onMeasure(widthMeasureSpec, expandSpec);
		 int me = MeasureSpec.makeMeasureSpec(400, MeasureSpec.AT_MOST);
	        super.onMeasure(widthMeasureSpec, me);
	}
	
	public void setTouch(final ListViewForScrollView listView){
		super.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
	            // ����ָ����listviewʱ���ø��ؼ�����,���ܹ���
	            case MotionEvent.ACTION_DOWN:
	            	listView.requestDisallowInterceptTouchEvent(true);
	            case MotionEvent.ACTION_MOVE:
	                break;
	           
	            case MotionEvent.ACTION_CANCEL:
	                // ����ָ�ɿ�ʱ���ø��ؼ����»�ȡ����
	            	listView.requestDisallowInterceptTouchEvent(false);
	                break;
	        }
	        return false;
			}
		});
	}
}

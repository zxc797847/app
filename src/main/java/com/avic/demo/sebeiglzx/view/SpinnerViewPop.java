package com.avic.demo.sebeiglzx.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avic.demo.sebeiglzx.R;
import com.avic.demo.sebeiglzx.adapter.MySpinnerPopListArrayAdapter;
import com.avic.demo.sebeiglzx.bean.SpinnearBean;
import com.avic.demo.sebeiglzx.listener.OnSpinnerClickListener;
import com.avic.demo.sebeiglzx.listener.OnSpinnerItemClickListener;
import com.avic.demo.sebeiglzx.utils.DialogUtil;
import com.avic.demo.sebeiglzx.utils.PopWindowUtil;

import java.util.ArrayList;

/**
 * @Created HaiyuKing
 * @Used 下拉菜单区域：自定义——继承RelativeLayout
 */
public class SpinnerViewPop extends RelativeLayout {
	/**下拉菜单文本区域*/
	private TextView titleTextView;
	/**接收传递过来的列表项文本集合*/
	private ArrayList<SpinnearBean> mTitleTextList = null;//原始类型为String

	/**下拉菜单区域的点击事件：用于显示下拉菜单对话框*/
	private OnSpinnerClickListener listener = null;
	/**列表对话框的列表项点击事件：用于将选中的列表项赋值给下拉菜单区域*/
	private OnSpinnerItemClickListener itemListener = null;

	/**是否执行下拉菜单区域的点击事件监听的状态值，默认为false（直接打开下拉菜单），如果为true的话，则先执行点击事件监听，搭配OnSpinnerClickListener使用*/
	private boolean handedPop = false;
	/**上下文，用于展现对话框的载体*/
	private Context mContext;
	/**选中的列表项的下标值*/
	private int selecteItem = 0;

	/**下拉菜单是否可编辑*/
	private boolean canEditable = true;
	/**文本的颜色：默认黑色*/
	private int textDefaultColor = 0;

	public static final String TYPE_POPWINDOW = "popwindow";//popwindow样式下拉菜单
	public static final String TYPE_DIALOG = "radiodialog";//单选下拉菜单对话框

	/**下拉菜单的类型：popwindow 或者 dialog*/
	private String spinnerType = TYPE_POPWINDOW;

	/**
	 * 这里构造方法也很重要，不加这个很多属性不能再XML里面定义*/
	public SpinnerViewPop(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;
		//引用布局：一个文本和右侧的图标
		LayoutInflater.from(context).inflate(R.layout.spinnerview_pop_view, this, true);

		//获取textview对象，并设置点击的监听事件——判断
		titleTextView = (TextView) findViewById(R.id.titleTextView);
		textDefaultColor = getResources().getColor(R.color.spinnerpop_normal_text_color);
		//默认文本颜色
		titleTextView.setTextColor(textDefaultColor);
		titleTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//弹出对话框
				if(canEditable){
					if (handedPop) {
						listener.OnFinished();
					} else {
						PopupListDialog();
					}
				}
			}
		});
	}

	/**
	 * 弹出列表对话框*/
	public void PopupListDialog() {

		showSelectedState(true);//设置下拉菜单文本框为选中/默认样式

		if (null == mTitleTextList) {
			mTitleTextList = new ArrayList<SpinnearBean>();
		}

		//自定义列表项的点击事件监听——在MySpinnerListArrayAdapter类中自定义的接口
		MySpinnerPopListArrayAdapter.OnMyItemClickListener itemClickListener = new MySpinnerPopListArrayAdapter.OnMyItemClickListener() {
			@Override
			public void OnMyItemClick(int position) {
				//如果position == -1，标明是点击弹出框外面的区域
				if(position != -1){
					if(spinnerType.equals(TYPE_POPWINDOW)){
						PopWindowUtil.closePopupWindows();//关闭列表对话框
					}else{
						DialogUtil.closeDialog();//关闭列表对话框
					}
					//此处需要判断，如果是多选对话框的话，需要特殊处理
					setSelectedIndexAndText(position);

					if (null != itemListener) {
						itemListener.OnFinished(position);
					}
				}
				showSelectedState(false);//设置下拉菜单文本框为选中/默认样式
			}
		};

		//原始类型为String
		ArrayList<String> itemTextList = new ArrayList<String>();
		for(int i=0;i<mTitleTextList.size();i++){
			itemTextList.add(mTitleTextList.get(i).getParaName());
		}
		if(spinnerType.equals(TYPE_POPWINDOW)) {
			PopWindowUtil.showPopupWindows(mContext, this, mTitleTextList, itemClickListener, selecteItem);
		}else{
			DialogUtil.showListDialog(mContext, mTitleTextList, itemClickListener,selecteItem);
		}
	}

	//设置列表项文本集合——常用
	public void setData(ArrayList<SpinnearBean> mArrayList) {
		this.mTitleTextList = mArrayList;
	}

	/**设置选中的下标值以及文本以及SpinnearModel中的选中状态值*/
	public void setSelectedIndexAndText(int index){
		titleTextView.setText(mTitleTextList.get(index).getParaName().toString());
		selecteItem = index;
		for(int i = 0;i<mTitleTextList.size();i++){
			mTitleTextList.get(i).setSelectedState(false);//设置选中状态为false
		}
		mTitleTextList.get(index).setSelectedState(true);
	}

	//设置下拉菜单区域是否执行点击事件的状态值
	public void setHandedPopup(boolean hand) {
		handedPop = hand;
	}

	public void setSpinnerType(String spinnerType) {
		this.spinnerType = spinnerType;
	}
	public String getSpinnerType() {
		return spinnerType;
	}

	//设置下拉菜单区域的点击事件监听
	public void setOnSpinnerClickListener(OnSpinnerClickListener listener) {
		this.listener = listener;
	}
	//设置列表对话框的列表项点击事件监听
	public void setOnSpinnerItemClickListener(OnSpinnerItemClickListener itemListener) {
		this.itemListener = itemListener;
	}

	//获得文本
	public String getText() {
		return titleTextView.getText().toString();
	}

	//获得选中的列表项的下标值
	public int getSeletedItem() {
		return selecteItem;
	}

	//设置文本颜色
	public void setTextColor(int color){
		textDefaultColor = color;
		titleTextView.setTextColor(textDefaultColor);
	}
	//设置提示语
	public void setHint(String hint) {
		titleTextView.setHint(hint);
	}

	//设置右侧的图标
	private void setDrawableRight(Drawable rightIcon) {
		rightIcon.setBounds(0, 0, rightIcon.getMinimumWidth(), rightIcon.getMinimumHeight());
		titleTextView.setCompoundDrawables(null, null, rightIcon, null);
	}

	/*===========================是否展现选中状态===========================*/
	private void showSelectedState(boolean isSelected){
		//选中状态
		if(isSelected){
			//修改箭头图标
			setDrawableRight(ContextCompat.getDrawable(mContext,R.drawable.spinnerview_pop_icon_shang));
			//修改文本颜色
			titleTextView.setTextColor(getResources().getColor(R.color.spinnerpop_selected_text_color));
		}else{
			//修改箭头图标
			setDrawableRight(ContextCompat.getDrawable(mContext,R.drawable.spinnerview_pop_icon_xia));
			//修改文本颜色
			titleTextView.setTextColor(textDefaultColor);
		}
	}

	/**
	 * 设置是否可编辑*/
	public void setEditable(boolean canEdit){
		canEditable = canEdit;
		if(canEditable){
			//修改背景颜色--白色
			//在使用shape的同时，用代码修改shape的颜色属性http://blog.csdn.net/wangdong20/article/details/37966333
			GradientDrawable myGrad = (GradientDrawable)getBackground();
			myGrad.setColor(ContextCompat.getColor(mContext,R.color.spinnerpop_canedit_bg_color));
		}else{
			//修改背景颜色--灰色
			GradientDrawable myGrad = (GradientDrawable)getBackground();
			myGrad.setColor(ContextCompat.getColor(mContext,R.color.spinnerpop_notedit_bg_color));
		}
	}
}
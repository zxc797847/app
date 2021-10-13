package com.avic.demo.sebeiglzx.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import com.avic.demo.sebeiglzx.R;

import com.avic.demo.sebeiglzx.activity.ProRfidMgrInitShelfActivity;
import com.avic.demo.sebeiglzx.activity.WMSBindingNewRfidActivity;
import com.avic.demo.sebeiglzx.activity.WMSRfidCheckReadActivity;
import com.avic.demo.sebeiglzx.activity.WMSRfidDHRKActivity;
import com.avic.demo.sebeiglzx.activity.WMSRfidInitActivity;
import com.avic.demo.sebeiglzx.activity.WMSRfidOutStoreActivity;
import com.avic.demo.sebeiglzx.activity.WMSRfidProReceiveActivity;
import com.avic.demo.sebeiglzx.activity.WMSRfidProcessActivity;
import com.avic.demo.sebeiglzx.activity.WMSRfidWasteCheckActivity;
import com.avic.demo.sebeiglzx.activity.WMSUserCardInitActivity;
import com.avic.demo.sebeiglzx.activity.ab.view.AbOnItemClickListener;
import com.avic.demo.sebeiglzx.activity.ab.view.AbSlidingPlayView;
import com.avic.demo.sebeiglzx.adapter.AdapterGridView;
import com.avic.demo.sebeiglzx.adapter.AdapterGridViewHot;


/*
* 首页
* */
public class Home_F extends Fragment {

    // 分类的九宫格
    private GridView gridView_classify;
    // 热门市场的九宫格
    private AdapterGridView adapter_GridView_classify;
    private AdapterGridViewHot adapterGridViewHot;
    // 首页轮播
    private AbSlidingPlayView viewPager;
    private Context context;
    // 扫一扫
    // private ImageView iv_shao;
    // 分类九宫格的资源文件
    //private int[] pic_path_classify = { R.drawable.menu_guide_1, R.drawable.menu_guide_4, R.drawable.meu_guide_22, R.drawable.guide_home_sbbd,R.drawable.menu_guide_20,R.drawable.menu_guide_111,R.drawable.menu_guide_91};
    /*private int[] pic_path_classify = { R.drawable.menu_guide_388,R.drawable.menu_guide_28,R.drawable.menu_guide_29,R.drawable.menu_guide_36,R.drawable.menu_guide_202,R.drawable.menu_guide_366,R.drawable.menu_guide_220,R.drawable.menu_guide_399};*/
    private int[] pic_path_classify = { R.drawable.menu_guide_168,R.drawable.menu_guide_268,R.drawable.menu_guide_368,R.drawable.menu_guide_468,R.drawable.menu_guide_568,R.drawable.menu_guide_668,R.drawable.menu_guide_768,R.drawable.menu_guide_868,R.drawable.menu_guide_968,R.drawable.menu_guide_1068};
    // 热门市场的资源文件
    //private int[] pic_path_hot = { R.drawable.menu_1, R.drawable.menu_2, R.drawable.menu_3, R.drawable.menu_4, R.drawable.menu_5, R.drawable.menu_6 };
    /** 存储首页轮播的界面 */
    private ArrayList<View> allListView;
    /** 首页轮播的界面的资源 */
    private int[] resId = { R.drawable.menu_viewpager_20, R.drawable.menu_viewpager_20, R.drawable.menu_viewpager_20, R.drawable.menu_viewpager_20, R.drawable.menu_viewpager_20 };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_f, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        context = view.getContext();
        gridView_classify = (GridView) view.findViewById(R.id.my_gridview);
        gridView_classify.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter_GridView_classify = new AdapterGridView(getActivity(), pic_path_classify);
        //adapterGridViewHot = new AdapterGridViewHot(getActivity(), pic_path_hot);
        gridView_classify.setAdapter(adapter_GridView_classify);
        viewPager = (AbSlidingPlayView) view.findViewById(R.id.viewPager_menu);
        // 设置播放方式为顺序播放
        viewPager.setPlayType(1);
        // 设置播放间隔时间
        viewPager.setSleepTime(3000);

        gridView_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (0 == arg2) {
                    // 初始
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSRfidInitActivity.class);
                    startActivity(intent);
                }else if (1 == arg2) {
                    // 派工
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSRfidProReceiveActivity.class);
                    startActivity(intent);
                }else if (2 == arg2) {
                    // 流程进度
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSRfidProcessActivity.class);
                    startActivity(intent);
                }else if (3 == arg2) {
                    // 检验
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSRfidCheckReadActivity.class);
                    startActivity(intent);
                }else if (4 == arg2) {
                    //出库
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSRfidOutStoreActivity.class);
                    startActivity(intent);
                }else if (5 == arg2) {
                    // 废品审核
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSRfidWasteCheckActivity.class);
                    startActivity(intent);
                }else if (6 == arg2) {
                    // 重新绑卡
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSBindingNewRfidActivity.class);
                    startActivity(intent);
                }else if (7 == arg2) {
                    // 材料入库
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSRfidDHRKActivity.class);
                    startActivity(intent);
                }else if (8 == arg2) {
                    // 材料入库
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ProRfidMgrInitShelfActivity.class);
                    startActivity(intent);
                }else if (9 == arg2) {
                    // 材料入库
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), WMSUserCardInitActivity.class);
                    startActivity(intent);
                }
            }
        });
        initViewPager();
    }

    private void initViewPager() {
        if (allListView != null) {
            allListView.clear();
            allListView = null;
        }
        allListView = new ArrayList<View>();
        for (int i = 0; i < resId.length; i++) {
            // 导入ViewPager的布局
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.pic_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.pic_item);
            imageView.setImageResource(resId[i]);
            allListView.add(view);
        }
        viewPager.addViews(allListView);
        // 开始轮播
        viewPager.startPlay();
        viewPager.setOnItemClickListener(new AbOnItemClickListener() {
            @Override
            public void onClick(int position) {
                // 跳转到详情界面
            }
        });
    }

}

package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyOrder_tab_Adapter;
import com.hbhongfei.hfcable.fragment.MyOrderAllFragment;
import com.hbhongfei.hfcable.fragment.MyOrderUnDeliveryFragment;
import com.hbhongfei.hfcable.fragment.MyOrderUnPaymenFragment;
import com.hbhongfei.hfcable.fragment.MyOrderUnSendFragment;

import java.util.ArrayList;
import java.util.List;

public class MyOrderActivity extends AppCompatActivity {

    private TabLayout tab_FindFragment_title; //定义TabLayout
    private ViewPager vp_FindFragment_pager; //定义viewPager
    private FragmentPagerAdapter fAdapter; //定义adapter
    private List<Fragment> list_fragment; //定义要装fragment的列表
    private List<String> list_title; //tab名称列表
    private MyOrderAllFragment myOrderAllFragment; //全部订单的fragment
    private MyOrderUnPaymenFragment myOrderUnPaymenFragment; //未付款的订单fragment
    private MyOrderUnDeliveryFragment myOrderUnDeliveryFragment;//未收货订单
    private MyOrderUnSendFragment myOrderUnSendFragment;//待发货订单
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setElevation(0);
        actionBar.setDisplayHomeAsUpEnabled(true);
        initControls();
    }

    /**
     * 初始化各控件
     */
    private void initControls() {
        tab_FindFragment_title = (TabLayout)findViewById(R.id.tab_FindFragment_title);

        vp_FindFragment_pager = (ViewPager)findViewById(R.id.vp_FindFragment_pager);
        //关闭预加载，默认一次只加载一个Fragment
        vp_FindFragment_pager.setOffscreenPageLimit(3);
        //初始化各fragment
        myOrderAllFragment = new MyOrderAllFragment();
        myOrderUnPaymenFragment = new MyOrderUnPaymenFragment();
        myOrderUnSendFragment=new MyOrderUnSendFragment();
        myOrderUnDeliveryFragment=new MyOrderUnDeliveryFragment();
        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(myOrderAllFragment);
        list_fragment.add(myOrderUnPaymenFragment);
        list_fragment.add(myOrderUnSendFragment);
        list_fragment.add(myOrderUnDeliveryFragment);
        //将名称加载tab名字列表，正常情况下，我们应该在values/arrays.xml中进行定义然后调用
        list_title = new ArrayList<>();
        list_title.add("全部");
        list_title.add("待付款");
        list_title.add("待发货");
        list_title.add("待收货");
        //设置TabLayout的模式
        tab_FindFragment_title.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(0)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(1)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(2)));
        tab_FindFragment_title.addTab(tab_FindFragment_title.newTab().setText(list_title.get(3)));
        fAdapter =new MyOrder_tab_Adapter(getSupportFragmentManager(),list_fragment,list_title);
        //viewpager加载adapter
        vp_FindFragment_pager.setAdapter(fAdapter);
        //TabLayout加载viewpager
        tab_FindFragment_title.setupWithViewPager(vp_FindFragment_pager);
    }
}

package com.hbhongfei.hfcable.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * tab的适配器
 * Created by 苑雪元 on 2016/8/3.
 */
public class MyOrder_tab_Adapter extends FragmentPagerAdapter {
    private static final int TAB_COUNT = 4;
    private List<Fragment> list_fragment; //fragment列表
    private List<String> list_Title; //tab名的列表
    public MyOrder_tab_Adapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }
    @Override
    public Fragment getItem(int position) {
//        return list_fragment.get(position);
        return (list_fragment == null || list_fragment.size() < TAB_COUNT) ? null : list_fragment.get(position);
    }
    @Override
    public int getCount() {
        return  list_Title == null ? 0 : list_Title.size();
    }
    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position % list_Title.size());
    }

    //防止重新销毁视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        super.destroyItem(container, position, object);
    }
}

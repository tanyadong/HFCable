package com.hbhongfei.hfcable.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 苑雪元 on 2016/7/20.
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list_fragment;
    public MainFragmentAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        list_fragment = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_fragment.size();
    }

    //防止重新销毁视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //如果注释这行，那么不管怎么切换，page都不会被销毁
        super.destroyItem(container, position, object);
    }
}

package com.hbhongfei.hfcable.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
}

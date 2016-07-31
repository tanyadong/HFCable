package com.hbhongfei.hfcable.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MainFragmentAdapter;
import com.hbhongfei.hfcable.fragment.IndexFragment;
import com.hbhongfei.hfcable.fragment.InfoFragment;
import com.hbhongfei.hfcable.fragment.MarketFragment;
import com.hbhongfei.hfcable.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private ViewPager viewPager;
    private List<Fragment> list_fragment;
    private LinearLayout layout_index,layout_info,layout_market,layout_mine;
    private ImageView imageView_index,imageView_info,imageView_market,imageView_mine;
    private TextView textView_index,textView_info,textView_market,textView_mine;
    private Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化界面
        initView();

        //点击事件
        click();
    }

    /**
     * 点击事件
     */
    private void click() {
        this.layout_index.setOnClickListener(this);
        this.layout_info.setOnClickListener(this);
        this.layout_market.setOnClickListener(this);
        this.layout_mine.setOnClickListener(this);
        //设置ViewPager滑动监听
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {

        this.layout_index = (LinearLayout) findViewById(R.id.ll_main_page);
        this.layout_info = (LinearLayout) findViewById(R.id.ll_main_find);
        this.layout_market = (LinearLayout) findViewById(R.id.ll_main_mess);
        this.layout_mine = (LinearLayout) findViewById(R.id.ll_main_mine);

        this.textView_index = (TextView) findViewById(R.id.txt_main);
        this.textView_info = (TextView) findViewById(R.id.txt_find);
        this.textView_market = (TextView) findViewById(R.id.txt_mess);
        this.textView_mine = (TextView) findViewById(R.id.txt_mine);

        this.imageView_index = (ImageView) findViewById(R.id.img_main);
        this.imageView_info = (ImageView) findViewById(R.id.img_find);
        this.imageView_market = (ImageView) findViewById(R.id.img_mess);
        this.imageView_mine = (ImageView) findViewById(R.id.img_mine);

        viewPager = (ViewPager) findViewById(R.id.viewPager);

        IndexFragment indexFragment = new IndexFragment();
        InfoFragment infoFragment = new InfoFragment();
        MarketFragment marketFragment = new MarketFragment();
        MineFragment mineFragment = new MineFragment();

        list_fragment = new ArrayList<>();

        list_fragment.add(indexFragment);
        list_fragment.add(infoFragment);
        list_fragment.add(marketFragment);
        list_fragment.add(mineFragment);

        MainFragmentAdapter m1 = new MainFragmentAdapter(getSupportFragmentManager(),list_fragment);

        viewPager.setAdapter(m1);

    }


    /**
     * 点击首页时显示
     */
    private void showHome(){
        imageView_index.setImageResource(R.mipmap.tabbar_home_highlighted);
        textView_index.setTextColor(Color.parseColor("#FF8000"));
        imageView_info.setImageResource(R.mipmap.tabbar_message_center);
        textView_info.setTextColor(Color.parseColor("#000000"));
        textView_info.setTextColor(Color.parseColor("#000000"));
        imageView_market.setImageResource(R.mipmap.tabbar_discover);
        textView_market.setTextColor(Color.parseColor("#000000"));
        imageView_mine.setImageResource(R.mipmap.tabbar_profile);
        textView_mine.setTextColor(Color.parseColor("#000000"));
    }

    /**
     * 点击资讯时显示
     */
    private void showInfo(){
        imageView_market.setImageResource(R.mipmap.tabbar_message_center_highlighted);
        textView_market.setTextColor(Color.parseColor("#FF8000"));
        imageView_index.setImageResource(R.mipmap.tabbar_home);
        textView_index.setTextColor(Color.parseColor("#000000"));
        imageView_info.setImageResource(R.mipmap.tabbar_discover);
        textView_info.setTextColor(Color.parseColor("#000000"));
        imageView_mine.setImageResource(R.mipmap.tabbar_profile);
        textView_mine.setTextColor(Color.parseColor("#000000"));
    }

    /**
     * 点击行情时显示
     */
    private void showMarket(){
        imageView_info.setImageResource(R.mipmap.tabbar_discover_highlighted);
        textView_info.setTextColor(Color.parseColor("#FF8000"));
        imageView_index.setImageResource(R.mipmap.tabbar_home);
        textView_index.setTextColor(Color.parseColor("#000000"));
        imageView_market.setImageResource(R.mipmap.tabbar_message_center);
        textView_market.setTextColor(Color.parseColor("#000000"));
        imageView_mine.setImageResource(R.mipmap.tabbar_profile);
        textView_mine.setTextColor(Color.parseColor("#000000"));
    }

    /**
     * 点击我的时显示
     */
    private void showMine(){
        imageView_mine.setImageResource(R.mipmap.tabbar_profile_highlighted);
        textView_mine.setTextColor(Color.parseColor("#FF8000"));
        imageView_index.setImageResource(R.mipmap.tabbar_home);
        textView_index.setTextColor(Color.parseColor("#000000"));
        imageView_market.setImageResource(R.mipmap.tabbar_message_center);
        textView_market.setTextColor(Color.parseColor("#000000"));
        imageView_info.setImageResource(R.mipmap.tabbar_discover);
        textView_info.setTextColor(Color.parseColor("#000000"));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_main_page:
                showHome();
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_main_mess:
                showInfo();
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_main_find:
                showMarket();
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_main_mine:
                showMine();
                viewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                showHome();
                break;
            case 1:
                showInfo();
                break;
            case 2:
                showMarket();
                break;
            case 3:
                showMine();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;

import java.util.ArrayList;

public class GuideActivity extends Activity {
    private ViewPager mViewPager;
    private ArrayList<View> mViews;
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private MyHandler mMyHandler;
    private LinearLayout mIndicatorLayout;        //用来装小圆点的Linearlayout
    private ArrayList<ImageView> mIndicatorList;//装小圆点的集合
    SharedPreferences share;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*//取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
        setContentView(R.layout.activity_guide);
        share = getSharedPreferences("showWelcomm", Context.MODE_PRIVATE);
        editor = share.edit();

        mMyHandler= new MyHandler();
        mIndicatorList = new ArrayList<ImageView>();
        iniView();
        iniViewPager();        //初始化ViewPager

        new Thread(new Runnable() {
            @Override
            public void run() {
                mMyHandler.sendEmptyMessageDelayed(9, 200);
            }
        }).start();

    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 9:
                    /*
                     * 这里必须用handler来延迟启动不然会报异常
                     * android.view.WindowManager$BadTokenException: Unable to add window -- token null is not valid; is your activity running?
                     */
                    mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
                    mPopupWindow.showAtLocation(findViewById(R.id.txt_Activity), Gravity.CENTER, 0, 0);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 初始化组件
     */
    private void iniView() {
        mPopupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
        mViewPager = (ViewPager)mPopupView.findViewById(R.id.pager);
        //包裹小圆点的LinearLayout
        mIndicatorLayout = (LinearLayout)mPopupView.findViewById(R.id.indicatorLayout);
        mPopupWindow = new PopupWindow(
                mPopupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true
        );
    }

    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private void setGuided(){
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_GUIDE_ACTIVITY, "false");
        editor.commit();
    }

    /**
     * 初始化引导页面
     */
    private void iniViewPager() {
        View v1 =  getLayoutInflater().inflate(R.layout.viewpager_page1, null);
        View v2 =  getLayoutInflater().inflate(R.layout.viewpager_page2, null);
        View v3 =  getLayoutInflater().inflate(R.layout.viewpager_page3, null);

        //点击最后一张图片的立即体验后退出
        TextView start = (TextView)v3.findViewById(R.id.TV_close_guide_go);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                setGuided();
                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mViews = new ArrayList<View>();
        mViews.add(v1);
        mViews.add(v2);
        mViews.add(v3);


        //设置适配器
        mViewPager.setAdapter(new MyPagerAdapter());
        //设置监听事件
        mViewPager.setOnPageChangeListener(new MyPagerChangeListener());

        //加入小圆点
        for (int i = 0; i < mViews.size(); i++) {
            ImageView indicator = new ImageView(this);
            if (i == 0) {
                indicator.setPadding(10, 0, 10, 0);
                indicator.setImageResource(R.mipmap.guide_indicator_active);
            }else {
                indicator.setPadding(20,0,20,0);
                indicator.setImageResource(R.mipmap.guide_indicator_inactive);
            }

            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            mIndicatorLayout.addView(indicator);
            //把小圆点图片存入集合,好让切换图案片的时候动态改变小圆点背景
            mIndicatorList.add(indicator);
        }

    }


    /**
     * 适配器
     * @author tanyadong
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager)arg0).removeView(mViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {

        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager)arg0).addView(mViews.get(arg1));
            return mViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            int count=mViews.size();
            for (int i = 0; i < count; i++) {

            }
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }
    }

    private class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            Log.i("zj", "onPagerChange position=" + position);
            for (int i = 0; i < mViews.size(); i++) {
                mIndicatorList.get(i).setImageResource(R.mipmap.guide_indicator_inactive);
            }
            mIndicatorList.get(position).setImageResource(R.mipmap.guide_indicator_active);
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //防止handler引起的内存泄露
        mMyHandler.removeCallbacksAndMessages(null);
    }

}

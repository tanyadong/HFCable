package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.ImagePaperAdapter;
import com.hbhongfei.hfcable.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {
    private String typeName;
    private LayoutInflater inflater;
    private ViewPager mviewPager;
    private ListView listView;
    private ImageView img1,img2,img3,img4;
    private TextView textView1,textView2;
    private Button btn_typeName1,btn_typeName2,btn_typeName3,btn_typeName4,btn_typeName5,btn_typeName6;
    /**用于小圆点图片*/
    private List<ImageView> dotViewList;
    /**用于存放轮播效果图片*/
    private List<ImageView> list;
//    添加小圆点控件，
    private LinearLayout dotLayout;

    private int currentItem  = 0;//当前页面

    boolean isAutoPlay = true;//是否自动轮播

    private ScheduledExecutorService scheduledExecutorService;
    Intent intent;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what == 100){
                mviewPager.setCurrentItem(currentItem);
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initView();
        setDate();
        onClick();
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("测试:"+i);
        }
        MyAdapter adapter = new MyAdapter(this,R.layout.intentionlayout,list);
        listView.setDivider(null);
        listView.setAdapter(adapter);

        if(isAutoPlay){
            startPlay();
        }
    }
    public void initView(){
        inflater = LayoutInflater.from(MainActivity1.this);
        listView= (ListView) findViewById(R.id.lv);
        mviewPager = (ViewPager) findViewById(R.id.myviewPager);
        dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
         img1 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
         img2 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
         img3 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
         img4 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
        btn_typeName1= (Button) findViewById(R.id.btn_type_name1);
        btn_typeName2= (Button) findViewById(R.id.btn_type_name2);
        btn_typeName3= (Button) findViewById(R.id.btn_type_name3);
        btn_typeName4= (Button) findViewById(R.id.btn_type_name4);
        btn_typeName5= (Button) findViewById(R.id.btn_type_name5);
        btn_typeName6= (Button) findViewById(R.id.btn_type_name6);
    }
    /**
     * 点击事件
     */
    public void onClick(){
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        btn_typeName1.setOnClickListener(this);

    }
    /**
     * 设置数据
     */
    public void setDate(){
        dotViewList = new ArrayList<ImageView>();
        dotLayout.removeAllViews();
        list = new ArrayList<ImageView>();
        img1.setBackgroundResource(R.mipmap.main_img1);
        img2.setBackgroundResource(R.mipmap.main_img2);
        img3.setBackgroundResource(R.mipmap.main_img3);
        img4.setBackgroundResource(R.mipmap.main_img4);

        list.add(img1);
        list.add(img2);
        list.add(img3);
        list.add(img4);
        //加入小圆点
        for (int i = 0; i < list.size(); i++) {
            ImageView indicator = new ImageView(this);
            if (i == 0) {
                indicator.setPadding(20, 0, 20, 0);
                indicator.setImageResource(R.mipmap.point_unpressed);
            } else {
                indicator.setPadding(20, 0, 20, 0);
                indicator.setImageResource(R.mipmap.point_pressed);
            }

            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            dotLayout.addView(indicator);
            //把小圆点图片存入集合,好让切换图案片的时候动态改变小圆点背景
            dotViewList.add(indicator);
        }
        ImagePaperAdapter adapter = new ImagePaperAdapter((ArrayList) list);
        mviewPager.setAdapter(adapter);
        mviewPager.setCurrentItem(0);
        mviewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
        //根据他的参数说明，第一个参数是执行的任务，第二个参数是第一次执行的间隔，第三个参数是执行任务的周期；
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img:
                intent=new Intent(getApplicationContext(),CompanyInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_type_name1:
                typeName=btn_typeName1.getText().toString();
                intent=new Intent(this,ProdectListActivity.class);
                intent.putExtra("typeName",typeName);
                startActivity(intent);
                break;
        }
    }

    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (mviewPager) {
                currentItem = (currentItem+1)%list.size();
                handler.sendEmptyMessage(100);
            }
        }
    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {


        boolean isAutoPlay = false;
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    System.out.println(" 手势滑动，空闲中");
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    System.out.println(" 界面切换中");
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (mviewPager.getCurrentItem() == mviewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        mviewPager.setCurrentItem(0);
                        System.out.println(" 滑动到最后一张");
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (mviewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        mviewPager.setCurrentItem(mviewPager.getAdapter().getCount() - 1);
                        System.out.println(" 滑动到第一张");
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onPageSelected(int position) {
            Log.i("zj", "onPagerChange position=" + position);
            for (int i = 0; i < list.size(); i++) {
                dotViewList.get(i).setImageResource(R.mipmap.point_unpressed);
            }
            dotViewList.get(position).setImageResource(R.mipmap.point_pressed);
        }

    }
}

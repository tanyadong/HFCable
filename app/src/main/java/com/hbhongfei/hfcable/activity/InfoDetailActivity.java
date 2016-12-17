package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Information;

import java.io.IOException;

public class InfoDetailActivity extends AppCompatActivity {
    private ImageView infoDetail_img;
    private TextView infoDetail_title;
    private TextView infoDetail_content,infoDetail_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        initView();
        toolBar();
        try {
            setValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_infoDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置状态栏背景为透明
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_info_detail);
        mCollapsingToolbarLayout.setTitle("资讯详情");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    /**
     * 初始化
     */
    private void initView(){
        SplashActivity.ID=2;
        infoDetail_content= (TextView) findViewById(R.id.infoDetail_content);
        infoDetail_img= (ImageView) findViewById(R.id.infoDetail_img);
        infoDetail_title= (TextView) findViewById(R.id.infoDetail_title);
        infoDetail_time= (TextView) findViewById(R.id.infoDetail_time);
    }
    /**
     * 数据
     */
    private void setValues() throws IOException {
        Intent intent=getIntent();
        Information information= (Information) intent.getSerializableExtra("data");
        infoDetail_title.setText(information.getTitle());
        AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
        infoDetail_img.setTag(information.getImgUrl());
        asyncBitmapLoader.loadImage(this,infoDetail_img,information.getImgUrl());
        infoDetail_content.setText(information.getDetailContent());
        infoDetail_time.setText(information.getTime());
    }



}

package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        initView();
        try {
            setValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

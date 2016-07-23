package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.hbhongfei.hfcable.R;

public class MyPasswordActivity extends AppCompatActivity {

    private EditText pwd_before,pwd_update,pwd_sure;
    private String S_before,S_update,S_sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_password);
        toolBar();
        initView();
    }

    /**
     * 展示toolbar
     */
    private void toolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    /**
     * 初始化界面
     */
    private void initView(){
        pwd_before = (EditText) findViewById(R.id.Etext_myPassword_before);
        pwd_update = (EditText) findViewById(R.id.Etext_myPassword_update);
        pwd_sure = (EditText) findViewById(R.id.Etext_myPassword_sure);
    }

    /**
     * 获取数据
     */
    private void getValues(){
        S_before = pwd_before.getText().toString().trim();
        S_update = pwd_update.getText().toString().trim();
        S_sure = pwd_sure.getText().toString().trim();
    }
}

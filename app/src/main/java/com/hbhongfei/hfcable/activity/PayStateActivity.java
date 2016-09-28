package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.CustomDialog;

public class PayStateActivity extends AppCompatActivity implements View.OnClickListener {

    private Button success;
    private TextView  money;
    private String S_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_state);
        //toolbar
        toolBar();

        //获取数据
        getValues();

        //初始化界面
        initView();

        //设置数据
        setValues();

        //点击事件
        setOnclick();


    }


    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pay_state);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    /**
     * 获取数据
     */
    private void getValues() {
        Intent intent = getIntent();
        S_money = intent.getStringExtra("amount");
    }

    /**
     * 初始化界面
     */
    private void initView() {
        money = (TextView) findViewById(R.id.pay_state_money);
        success = (Button) findViewById(R.id.pay_success);

    }

    /**
     * 设置数据
     */
    private void setValues() {
        money.setText("￥"+S_money);
    }

    /**
     * 设置点击事件
     */
    private void setOnclick(){
        success.setOnClickListener(this);
    }

    /**
     * 跳转到MainActivity
     */
    private void  toMain(){
        Intent intent = new Intent();
        intent.setClass(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.pay_success:
                toMain();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            toMain();
        }
        return super.onKeyDown(keyCode, event);
    }
}

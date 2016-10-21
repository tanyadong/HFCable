package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.LoginConnection;

public class CommentActivity extends AppCompatActivity {

    private String S_cableRingId,S_phoneNumber;
    private static final String USER = LoginConnection.USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //初始化数据
        initValues();
    }

    /**
     * 初始化数据
     */
    private void initValues(){
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        Intent intent = getIntent();
        S_cableRingId = intent.getStringExtra("cableRingId");
    }
}

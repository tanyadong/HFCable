package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hbhongfei.hfcable.R;

public class CompanyInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
    }
}

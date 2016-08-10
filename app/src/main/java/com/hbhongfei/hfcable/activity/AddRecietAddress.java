package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.hbhongfei.hfcable.R;

public class AddRecietAddress extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout Rlayout_localArea;
    private EditText consigne_editview,add_phone_editview,add_localArea_editview,add_addressDetail_editview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reciet_address);
        initview();
        setValues();
        click();
    }
    /**
     * 初始化组件
     */
    private void  initview(){
        Rlayout_localArea= (RelativeLayout) findViewById(R.id.Rlayout_localArea);
        consigne_editview= (EditText) findViewById(R.id.consigne_editview);
        add_phone_editview= (EditText) findViewById(R.id.add_phone_editview);
        add_phone_editview= (EditText) findViewById(R.id.add_addressDetail_editview);
        add_localArea_editview= (EditText) findViewById(R.id.add_localArea_editview);
    }

    private void setValues(){

    }
    /**
     * 点击事件
     */
    private void click(){
        Rlayout_localArea.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Rlayout_localArea:
                break;
        }
    }
}

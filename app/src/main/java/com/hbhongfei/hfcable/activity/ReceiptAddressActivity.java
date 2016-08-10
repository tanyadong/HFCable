package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;

public class ReceiptAddressActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lview_recriptAddress;
    private TextView add_recriptAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        setValue();
        click();
    }
    private void initView(){
        lview_recriptAddress= (ListView) findViewById(R.id.lview_recriptAddress);
        add_recriptAddress= (TextView) findViewById(R.id.add_recriptAddress_tview);
    }

    private void setValue(){

    }
    private void click(){
        add_recriptAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_recriptAddress_tview:
                Intent intent=new Intent(ReceiptAddressActivity.this,AddRecietAddress.class);
                startActivity(intent);
                break;
        }
    }
}

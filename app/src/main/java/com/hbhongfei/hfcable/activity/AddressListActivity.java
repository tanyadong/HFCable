package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.ShoppingAddressListConnection;

public class AddressListActivity extends AppCompatActivity {

    private ListView lview_recriptAddress;
    private TextView add_recriptAddress;
    private LinearLayout layout_address_emity;
    private static final String USER = LoginConnection.USER;
    private String S_phoneNumber;//用户名
    ShoppingAddressListConnection shoppingAddressListConnection;
    private LinearLayout noInternet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        //toolbar
        toolBar();
        //初始化界面
        initView();
    }

    @Override
    protected void onPostResume() {
        //初始化数据
        initValues();
        super.onPostResume();
    }

    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }
    /**
     * 初始化界面
     */
    private void  initView(){
        lview_recriptAddress= (ListView) findViewById(R.id.lview_recriptAddress);
        layout_address_emity= (LinearLayout) findViewById(R.id.layout_address_emity);
        add_recriptAddress= (TextView) findViewById(R.id.add_recriptAddress_tview);
        add_recriptAddress.setVisibility(View.GONE);
        noInternet = (LinearLayout) findViewById(R.id.no_internet_receipt_address);
    }


    /**
     * 初始化数据
     */
    private void initValues(){
        //获取当前用户
        SharedPreferences spf =getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        //获取地址
        shoppingAddressListConnection=new ShoppingAddressListConnection(this,S_phoneNumber,this,lview_recriptAddress,layout_address_emity,noInternet);
        shoppingAddressListConnection.addressListConnection();
    }



}

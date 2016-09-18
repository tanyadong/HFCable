package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.ShoppingAddressListConnection;
import com.hbhongfei.hfcable.util.ZdyListView;

public class AddressListActivity extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener {

    private ZdyListView lview_recriptAddress;
    private TextView add_recriptAddress;
    private LinearLayout layout_address_emity;
    private static final String USER = LoginConnection.USER;
    private String S_phoneNumber;//用户名
    private int pageNo=1; //当前页数
    boolean isLastRow=false; //是否滚动到最后一行
    ShoppingAddressListConnection shoppingAddressListConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);

        //toolbar
        toolBar();
        //初始化界面
        initView();

        //初始化数据
        initValues();
        //设置点击事件
        click();
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
        lview_recriptAddress= (ZdyListView) findViewById(R.id.lview_recriptAddress);
        lview_recriptAddress.init(getApplicationContext());
        add_recriptAddress= (TextView) findViewById(R.id.add_recriptAddress_tview);
        layout_address_emity= (LinearLayout) findViewById(R.id.layout_address_emity);
    }


    /**
     * 设置点击事件
     */
    private void click(){
        add_recriptAddress.setOnClickListener(this);
        lview_recriptAddress.setOnScrollListener(this);
    }

    /**
     * 初始化数据
     */
    private void initValues(){
        //获取当前用户
        SharedPreferences spf =getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        //获取地址
        shoppingAddressListConnection=new ShoppingAddressListConnection(this,S_phoneNumber,this,lview_recriptAddress,layout_address_emity);
        shoppingAddressListConnection.addressListConnection(pageNo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_recriptAddress_tview:
                Intent intent=new Intent(AddressListActivity.this,AddRecietAddress.class);
                intent.putExtra("tag","add");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE ){
            //动态加载数据
            Toast.makeText(this.getApplication(),"aaaa"+"",Toast.LENGTH_SHORT).show();
            lview_recriptAddress.onLoading(getApplicationContext());
            pageNo++;
            new LoadDataThread().start();
            lview_recriptAddress.LoadingComplete();
            isLastRow=false;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }
    }

    /**
     * 动态加载数据
     */
    private class LoadDataThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(1000);
                shoppingAddressListConnection.addressListConnection(pageNo);
                return;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

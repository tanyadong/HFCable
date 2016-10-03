package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NetUtils;
import com.hbhongfei.hfcable.util.ShoppingAddress_conn;

import org.json.JSONException;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * 收货地址管理
 */
public class ReceiptAddressActivity extends AppCompatActivity implements View.OnClickListener,BGARefreshLayout.BGARefreshLayoutDelegate{
    private ListView lview_recriptAddress;
    BGARefreshLayout mRefreshLayout;
    private TextView add_recriptAddress;
    private LinearLayout layout_address_emity,noInternet;
    private static final String USER = LoginConnection.USER;
    private String S_phoneNumber;//用户名
    private  Dialog dialog;

    ShoppingAddress_conn shoppingAddressListConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        toolbar();
        initView();
        initRefreshLayout();
        //获取地址

        click();
    }

    @Override
    protected void onPostResume() {
        //开始加载数据
        setValue();
        super.onPostResume();
    }

    /**
     * 设置toolbar
     */
    private void toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView(){
        mRefreshLayout= (BGARefreshLayout) findViewById(R.id.layout_address_listview);
        lview_recriptAddress= (ListView) findViewById(R.id.lview_recriptAddress);
        add_recriptAddress= (TextView) findViewById(R.id.add_recriptAddress_tview);
        layout_address_emity= (LinearLayout) findViewById(R.id.layout_address_emity);
        noInternet = (LinearLayout) findViewById(R.id.no_internet_receipt_address);
    }

    /**
     * 初始化下拉刷新组件
     */
    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout)findViewById(R.id.layout_address_listview);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getApplication(),true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载中");
    }

    private void setValue(){
        SharedPreferences spf =getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        shoppingAddressListConnection=new ShoppingAddress_conn(this,S_phoneNumber,this,lview_recriptAddress,layout_address_emity,noInternet);
        shoppingAddressListConnection.addressListConnection();
    }


    private void click(){
        add_recriptAddress.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_recriptAddress_tview:
                Intent intent=new Intent(ReceiptAddressActivity.this,AddRecietAddress.class);
                intent.putExtra("tag","add");
                startActivity(intent);
                break;
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        if(NetUtils.isConnected(this)){
            shoppingAddressListConnection.addressListConnection();
            mRefreshLayout.endRefreshing();
        }else{
            Toast.makeText(this,"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}

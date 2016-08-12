package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.ShoppingAddress_conn;

/**
 * 收货地址管理
 */
public class ReceiptAddressActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView lview_recriptAddress;
    private TextView add_recriptAddress;
    private static final String USER = LoginConnection.USER;
    private String S_phoneNumber;//用户名
    private  Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_address);
        toolbar();
        initView();
        setValue();
        //获取地址
        ShoppingAddress_conn shoppingAddress_conn=new ShoppingAddress_conn(S_phoneNumber,this,lview_recriptAddress);
        shoppingAddress_conn.addressListConnection();
//        addresListConnection();
        click();
    }

    /**
     * 设置toolbar
     */
    private void toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView(){
        lview_recriptAddress= (ListView) findViewById(R.id.lview_recriptAddress);
        add_recriptAddress= (TextView) findViewById(R.id.add_recriptAddress_tview);
    }

    private void setValue(){
        SharedPreferences spf =getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
    }
    private void click(){
        add_recriptAddress.setOnClickListener(this);
    }

    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ReceiptAddressActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

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
}

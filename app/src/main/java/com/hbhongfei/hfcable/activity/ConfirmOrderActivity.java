package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter_myShopping;
import com.hbhongfei.hfcable.entity.CablesInfo;
import com.hbhongfei.hfcable.entity.TypeInfo;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name,location,telphone,money,commit;
    private ListView products;
    private List<Map<String,Object>> list;// 新建适配器 ，绑定数据
    private SimpleAdapter simpleAdapter;
    private String[] from = { "product_name", "introduce", "product_price", "product_num" };
    private int[] to = { R.id.Tview_confirm_order_product_name, R.id.Tview_confirm_order_product_introduce,R.id.Tview_confirm_order_product_price,R.id.Tview_confirm_order_product_num };

    private static final String USER = LoginConnection.USER;
    private ArrayList<Map<String,Object>> proInfos;

    private  String S_phoneNumber;
    private Double S_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        setOnClick();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initValues();
        setValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        this.name = (TextView) findViewById(R.id.Tview_confirm_order_name);
        this.location = (TextView) findViewById(R.id.Tview_confirm_order_location);
        this.telphone = (TextView) findViewById(R.id.Tview_confirm_order_tel);
        this.money = (TextView) findViewById(R.id.Tview_confirm_order_money);
        this.commit = (TextView) findViewById(R.id.Tview_confirm_order_commit);
        this.products = (ListView) findViewById(R.id.list_confirm_order_product);
    }

    /**
     * 设置点击事件
     */
    private void setOnClick(){
        this.commit.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initValues(){
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        Intent intent = getIntent();
        proInfos = (ArrayList<Map<String,Object>>) intent.getSerializableExtra("proInfos");
        S_money = intent.getDoubleExtra("price",0.00);
    }

    /**
     * 设置数据
     */
    private void setValues(){
        list = new ArrayList<Map<String, Object>>();
        // 新增数据
        for (int i = 0; i < proInfos.size(); i++) {
            Map map = new HashMap<String, Object>();
            Map proInfo = proInfos.get(i);
            //map放入两个键值对，键名与from对应，
            for (int j=0;j<to.length;j++){
                map.put(from[j],proInfo.get(from[j]));
            }
            //往list添加数据
            list.add(map);
        }

        simpleAdapter = new SimpleAdapter(this, list, R.layout.content_comfirm_order_products,from,to);

        products.setAdapter(simpleAdapter);

        money.setText(String.valueOf(S_money));

        address();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Tview_confirm_order_commit:
                //提交订单
                break;
        }
    }


    /**
     * 连接服务
     */
    public void address(){
        String url = Url.url("/androidAddress/getDefauleAddress");
        Map<String,String> params =new HashMap<>();
        params.put("phoneNumber", S_phoneNumber);
        RequestQueue mQueue = Volley.newRequestQueue(ConfirmOrderActivity.this);

        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,jsonObjectListener,errorListener, params);
        mQueue.add(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String  msg = jsonObject.getString("msg");
                if (msg.equals("success")){
                    JSONObject address = jsonObject.getJSONObject("address");
                    String userName = address.getString("consignee");
                    name.setText(userName);
                    String phone = address.getString("phone");
                    telphone.setText(phone);
                    String detailAddress = address.getString("detailAddress");
                    location.setText(detailAddress);
                }else if (msg.equals("filed")){

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ConfirmOrderActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

}

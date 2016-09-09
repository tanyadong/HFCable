package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.ConfirmOrderAdapter;
import com.hbhongfei.hfcable.pojo.ShoppingAddress;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name,location,telphone,money,commit;
    private ListView products;
    private LinearLayout Llocation;
    private List<Map<String,Object>> list;// 新建适配器 ，绑定数据
//    private SimpleAdapter simpleAdapter;
    private ConfirmOrderAdapter confirmOrderAdapter;
    private String[] from = { "product_name", "introduce", "product_price", "product_num" ,"product_package","product_iamge"};

    private static final String USER = LoginConnection.USER;
    private ArrayList<Map<String,Object>> proInfos;
    private Map<String,String> packageMap;

    private  String S_phoneNumber;
    private Double S_money;
    private ShoppingAddress shoppingAddress;
    private int tag= 0;
    private static final String TEMP_INFO = "temp_info";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        toolBar();
        initView();

        setOnClick();

    }

    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_condirm);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initValues();
        setValues();
        if (tag==0){
            address();
        }
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
        this.Llocation  = (LinearLayout) findViewById(R.id.Layout_confirm_order_location);
    }

    /**
     * 设置点击事件
     */
    private void setOnClick(){
        this.commit.setOnClickListener(this);
        this.Llocation.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initValues(){
        Intent intent = getIntent();
        proInfos = (ArrayList<Map<String,Object>>) intent.getSerializableExtra("proInfos");
        S_money = intent.getDoubleExtra("price",0.00);
        packageMap = (Map<String, String>) intent.getSerializableExtra("map");
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
            if (proInfo.get("product_package").equals("10米")){
                proInfo.put("product_price",proInfo.get("product_price"));
            }else if(proInfo.get("product_package").equals("1盘")){
                Double price = (Double) proInfo.get("product_price");
                proInfo.put("product_price",price*10);
            }else{
                Double price = (Double) proInfo.get("product_price");
                Integer package1 = Integer.valueOf((String) proInfo.get("product_package"));
                String s = packageMap.get(proInfo.get("product_package"));
                String packagePrice = s.substring(0,s.indexOf("."));
                proInfo.put("product_price",package1/10*price+Integer.valueOf(packagePrice));
            }
            for (int j=0;j<from.length;j++){
                map.put(from[j],proInfo.get(from[j]));
            }
            //往list添加数据
            list.add(map);
        }

        confirmOrderAdapter = new ConfirmOrderAdapter(this,list);
        products.setAdapter(confirmOrderAdapter);

        money.setText(String.valueOf(S_money));
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.Tview_confirm_order_commit:
                //提交订单
                break;
            case R.id.Layout_confirm_order_location:
                //转换地址
                Intent intent1 = getIntent();
                intent.setClass(ConfirmOrderActivity.this,AddressListActivity.class);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this,"onActivityResult",Toast.LENGTH_SHORT).show();
        if (requestCode==0&&resultCode==0){
            shoppingAddress = (ShoppingAddress) data.getSerializableExtra("shoppingAddress");
            name.setText(shoppingAddress.getConsignee());
            telphone.setText(shoppingAddress.getPhone());
            location.setText(shoppingAddress.getLocalArea()+shoppingAddress.getDetailAddress());
            tag = 1;
        }else{
            tag = 0;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 连接地址服务
     */
    public void address(){
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
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
                    JSONObject address = jsonObject.getJSONObject("shoppingAddress");
                    String userName = address.getString("consignee");
                    name.setText(userName);
                    String phone = address.getString("phone");
                    telphone.setText(phone);
                    String detailAddress = address.getString("detailAddress");
                    String localArea = address.getString("localArea");
                    location.setText(localArea+detailAddress);
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

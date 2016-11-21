package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.ConfirmOrderAdapter;
import com.hbhongfei.hfcable.pojo.Order;
import com.hbhongfei.hfcable.pojo.ShoppingAddress;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name, location, telphone, money, commit;
    private ListView products;
    private LinearLayout Llocation;
    private List<Map<String, Object>> list;// 新建适配器 ，绑定数据
    private ArrayList<Order> list_order ;
    private JSONObject json = new JSONObject();
    private ConfirmOrderAdapter confirmOrderAdapter;
    private String[] from = {"product_name", "color", "product_price", "product_num", "product_package", "product_iamge"};
    private String addressId = null;
    private static final String USER = LoginConnection.USER;
    private ArrayList<Map<String, Object>> proInfos;
    private int pro_count;
    private Map<String, String> packageMap;
    Map<String, String> param;
    private String S_phoneNumber;
    private Double S_money;
    private ShoppingAddress shoppingAddress;
    private int tag = 0;
    private SparseArray array = new SparseArray();
    private String random;
    private DecimalFormat df = new DecimalFormat("0.00");

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
        if (tag == 0) {
            address();
        }
        initValues();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            }
        }
    };
    /**
     * 初始化界面
     */
    private void initView() {
        this.name = (TextView) findViewById(R.id.Tview_confirm_order_name);
        this.location = (TextView) findViewById(R.id.Tview_confirm_order_location);
        this.telphone = (TextView) findViewById(R.id.Tview_confirm_order_tel);
        this.money = (TextView) findViewById(R.id.Tview_confirm_order_money);
        this.commit = (TextView) findViewById(R.id.Tview_confirm_order_commit);
        this.products = (ListView) findViewById(R.id.list_confirm_order_product);
        this.Llocation = (LinearLayout) findViewById(R.id.Layout_confirm_order_location);
    }

    /**
     * 设置点击事件
     */
    private void setOnClick() {
        this.commit.setOnClickListener(this);
        this.Llocation.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initValues() {
        Intent intent = getIntent();
        if (intent.getSerializableExtra("proInfos") != null) {
            proInfos = (ArrayList<Map<String, Object>>) intent.getSerializableExtra("proInfos");
            S_money = intent.getDoubleExtra("price", 0.00);
            packageMap = (Map<String, String>) intent.getSerializableExtra("map");
            JSONArray mJsonArray = new JSONArray();
             pro_count=proInfos.size();
            for (int i = 0; i <pro_count; i++) {
                Map<String, Object> itemMap = proInfos.get(i);
                Iterator<Map.Entry<String, Object>> iterator = itemMap.entrySet().iterator();
                JSONObject object = new JSONObject();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    try {
                        object.put(entry.getKey(), entry.getValue());
                    } catch (JSONException e) {
                    }
                }
                mJsonArray.put(object);
            }

            SharedPreferences sp = this.getSharedPreferences("comfirmOrder", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("proInfos", mJsonArray.toString());
            editor.putString("S_money", df.format(S_money) + "");
            editor.commit();
        } else {
            List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
            SharedPreferences sp = this.getSharedPreferences("comfirmOrder", Context.MODE_PRIVATE);
            String result = sp.getString("proInfos", "");
            try {
                JSONArray array = new JSONArray(result);
                int count=array.length();
                for (int i = 0; i <count; i++) {
                    JSONObject itemObject = array.getJSONObject(i);
                    Map<String, Object> itemMap = new HashMap<String, Object>();
                    JSONArray names = itemObject.names();
                    if (names != null) {
                         int name_count=names.length();
                        for (int j = 0; j < name_count; j++) {
                            String name = names.getString(j);
                            if (name.equals("product_price")) {
                                Double value = itemObject.getDouble(name);
                                itemMap.put(name, value);
                            } else {
                                Object value = itemObject.get(name);
                                itemMap.put(name, value);
                            }
                        }
                    }
                    datas.add(itemMap);
                }
            } catch (JSONException e) {

            }
            proInfos = (ArrayList<Map<String, Object>>) datas;
            S_money = Double.parseDouble(sp.getString("S_money", "1"));
        }

        setValues();
    }

    /**
     * 设置数据
     */
    private void setValues(){
        list = new ArrayList<Map<String, Object>>();
        list_order = new ArrayList<>();;
        if (proInfos != null) {
            random = RandomStringUtils.randomNumeric(20);
            pro_count=proInfos.size();
            for (int i = 0; i < pro_count; i++) {
                Map map = new HashMap<String, Object>();
                Map proInfo = proInfos.get(i);
                //map放入两个键值对，键名与from对应，
                int from_count=from.length;
                for (int j = 0; j < from_count; j++) {
                    map.put(from[j], proInfo.get(from[j]));
                }
                array.put(i, (String) proInfo.get("product_name"));
                //往list添加数据
                list.add(map);
            }
            confirmOrderAdapter = new ConfirmOrderAdapter(this, list);
            products.setAdapter(confirmOrderAdapter);
            money.setText(String.valueOf(df.format(S_money)));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Tview_confirm_order_commit:
                if (addressId!=null){
                    Intent intent = new Intent();
                    //保存订单
                    saveOrder();
                    //确认支付界面
                    intent.setClass(ConfirmOrderActivity.this, OrderPayActivity.class);
                    intent.putExtra("money", money.getText());
                    intent.putExtra("order_no",random);
                    Bundle bundle = new Bundle();
                    bundle.putSparseParcelableArray("introduce", array);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,"请确认收获地址哦",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.Layout_confirm_order_location:
                //转换地址
                Intent intent = new Intent();
                intent.setClass(ConfirmOrderActivity.this, AddressListActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }

    public void saveOrder() {
        // 订单编号
        pro_count=proInfos.size();
        for (int i = 0; i < pro_count; i++) {
            param = new HashMap<>();
            Map proInfo = proInfos.get(i);
            param.put("id" + i, (String) proInfo.get("id"));
            Double monty = (Double) proInfo.get("product_price") * (Integer) proInfo.get("product_num");
            param.put("money" + i, df.format(monty));
            param.put("addressId", addressId);
            param.put("orderNumber", random);
            try {
                json.put("order" + i, param);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String url = Url.url("/androidOrder/save");
        // 订单编号
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json, jsonSuccessListener, errorListener);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonSuccessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("msg");
                if (msg.equals("success")) {
                    Toast.makeText(ConfirmOrderActivity.this, "保存订单成功", Toast.LENGTH_SHORT).show();
                } else if (msg.equals("fail")) {
                    Toast.makeText(ConfirmOrderActivity.this, "保存订单失败", Toast.LENGTH_SHORT).show();
                }/*else if(msg.equals("have")){
                    Toast.makeText(ConfirmOrderActivity.this, "订单失效", Toast.LENGTH_SHORT).show();
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == 0) {
            if (data != null) {
                shoppingAddress = (ShoppingAddress) data.getSerializableExtra("shoppingAddress");
                addressId = shoppingAddress.getId();
                name.setText(shoppingAddress.getConsignee());
                telphone.setText(shoppingAddress.getPhone());
                location.setText(shoppingAddress.getLocalArea() + shoppingAddress.getDetailAddress());
                tag = 1;
            }
        } else {
            tag = 0;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 连接地址服务
     */
    public void address() {
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber", null);
        String url = Url.url("/androidAddress/getDefauleAddress");
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", S_phoneNumber);
        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url, jsonObjectListener, errorListener, params);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("msg");
                if (msg.equals("success")) {
                    final JSONObject address = jsonObject.getJSONObject("shoppingAddress");
                    String userName = address.getString("consignee");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                addressId=address.getString("id");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    name.setText(userName);
                    String phone = address.getString("phone");
                    telphone.setText(phone);
                    String detailAddress = address.getString("detailAddress");
                    String localArea = address.getString("localArea");
                    location.setText(localArea + detailAddress);
                } else if (msg.equals("filed")) {

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ConfirmOrderActivity.this, "链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

}

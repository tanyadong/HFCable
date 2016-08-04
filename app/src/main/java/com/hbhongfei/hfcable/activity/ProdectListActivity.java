package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter;
import com.hbhongfei.hfcable.adapter.SpinnerListAdapter;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.util.ConnectionProduct;
import com.hbhongfei.hfcable.util.MySpinner;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdectListActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout prodectType_spinner;
    private TextView prodectType_textView;
    private ListView prodectList_listView;
    private String array[],typeName;
    private int width;
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodect_list);
        mQueue = Volley.newRequestQueue(this);
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        //初始化
        initView();
        connInter();
        setValues();
//        onClickLisener();
    }

    public void initView(){
        prodectType_spinner = (LinearLayout) findViewById(R.id.prodectType_spinner);
        prodectType_textView = (TextView) findViewById(R.id.prodectType_textView);
        prodectList_listView= (ListView) findViewById(R.id.prodectlist_listView);
    }

    /**
     * 连接服务
    /**
     * 展示当前用户管理任务连接服务
     */
    public void connInter(){
        String url = Url.url("androidType/getType");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,
                jsonObjectListener,errorListener);
        mQueue.add(jsonObjectRequest);
    }

    /**
     * 连接服务
     * 根据种类查询产品
     * */
    public void connInterByType(String typeName) throws JSONException {
        String url = Url.url("androidProduct/getProduct");
        Map<String,String> map=new HashMap<>();
        map.put("typeName",typeName);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectProductListener,errorListener,map);
        mQueue.add(normalPostRequest);
    }
    /**
     * 成功的监听器
     * 返回的是任务的信息列表
     */
    private Response.Listener<JSONObject> jsonObjectProductListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                List<Product> list=new ArrayList<>();
                JSONArray jsonArray=jsonObject.getJSONArray("productList");

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Product product=new Product();
                    product.setProdectName(jsonObject1.getString("prodectName"));
                    product.setPrice(jsonObject1.getDouble("price"));
                    product.setDetail(jsonObject1.getString("detail"));
                    product.setLineCoreType(jsonObject1.getString("lineCoreType"));
                    product.setSpecifications(jsonObject1.getString("specifications"));
                    product.setModel(jsonObject1.getString("model"));
                    JSONObject jsonObject2=jsonObject1.getJSONObject("type");
                    product.setTypeName((String) jsonObject2.get("typeName"));
                    JSONArray jsonArray1=jsonObject1.getJSONArray("productImages");
                    ArrayList<String> list1=new ArrayList<>();
                    for(int j=0;j<jsonArray1.length();j++){
                        list1.add((String) jsonArray1.get(i));
                    }
                    product.setProductImages(list1);
                    list.add(product);
                }
                MyAdapter adapter = new MyAdapter(ProdectListActivity.this,R.layout.intentionlayout,list);
                prodectList_listView.setDivider(null);
                prodectList_listView.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
    /**
     *获取种类成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            List<String> type_list=new ArrayList<>();
            try {
                jsonArray = jsonObject.getJSONArray("list");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = (JSONObject)jsonArray.getJSONObject(i);
                    String typeName=jsonObject1.getString("typeName");
                    type_list.add(typeName);
                }
               onClickLisener(type_list);
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
            Toast.makeText(ProdectListActivity.this, "无法连接服务器", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };


    /**
     * 设置数据
     */
    public void setValues(){
        Intent intent=getIntent();
        typeName=intent.getStringExtra("typeName");
        prodectType_textView.setText(typeName);
        //获取屏幕宽度
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        try {
            ConnectionProduct connectionProduct=new ConnectionProduct(this,prodectList_listView);
            connectionProduct.connInterByType(typeName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 点击事件
     */
    public void onClickLisener(final List<String> list){
        Intent intent=getIntent();
        typeName=intent.getStringExtra("typeName");
        prodectType_textView.setText(typeName);
        //获取屏幕宽度
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        prodectType_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySpinner mySpinner = new MySpinner(ProdectListActivity.this, width, list);
                mySpinner.showAsDropDown(prodectType_spinner, 0, 0);//显示在rl_spinner的下方
                mySpinner.setOnItemClickListener(new SpinnerListAdapter.onItemClickListener() {
                    @Override
                    public void click(int position, View view) {
                        prodectType_textView.setText(list.get(position));
                        try {
                            ConnectionProduct connectionProduct=new ConnectionProduct(ProdectListActivity.this,prodectList_listView);
                            connectionProduct.connInterByType(typeName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


}

package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter;
import com.hbhongfei.hfcable.pojo.Product;
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

public class MyFavoriteActivity extends AppCompatActivity {
    private ListView list_myFavorite;
    private LinearLayout layout_favority_emity;
    String S_phoneNumber;
    private static final String USER = LoginConnection.USER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initView();
        setValues();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        list_myFavorite = (ListView) findViewById(R.id.listView_myFavorite);
        layout_favority_emity= (LinearLayout) findViewById(R.id.layout_favority_emity);
    }
    private void setValues(){
        List<Product> list = new ArrayList<Product>();
        SharedPreferences spf =getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        //链接服务
        getCollection();

    }
    private void getCollection(){
        RequestQueue mQueue= Volley.newRequestQueue(this);
        String url = Url.url("/androidCollecton/getCollection");
        Map<String,String> map=new HashMap<>();
        map.put("userName",S_phoneNumber);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectCollectionListener,errorListener,map);
        mQueue.add(normalPostRequest);
    }


    /**
     * 成功的监听器
     * 返回的收藏状态信息
     */
    private Response.Listener<JSONObject> jsonObjectCollectionListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                List<Product> list=new ArrayList<>();
                JSONArray jsonArray=jsonObject.getJSONArray("productList");
                if(jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        Product product=new Product();
                        product.setId(jsonObject1.getString("id"));
                        product.setPrice(jsonObject1.getDouble("price"));
                        product.setSpecifications(jsonObject1.getString("specifications"));
                        JSONObject jsonObject2=jsonObject1.getJSONObject("type");
                        JSONArray jsonArray1=jsonObject1.getJSONArray("productImages");
                        //有图片时加入到产品图片集合
                        if(jsonArray1.length()>0){
                            ArrayList<String> list1=new ArrayList<>();
                            for(int j=0;j<jsonArray1.length();j++){
                                list1.add((String) jsonArray1.get(i));
                            }
                            product.setProductImages(list1);
                        }
                        list.add(product);
                    }
                }else{
                    layout_favority_emity.setVisibility(View.VISIBLE);
                    Toast.makeText(MyFavoriteActivity.this,"您还没有收藏",Toast.LENGTH_SHORT).show();
                }
                MyAdapter adapter = new MyAdapter(MyFavoriteActivity.this, R.layout.intentionlayout,list);
                list_myFavorite.setDivider(null);
                list_myFavorite.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ;
    };

    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(MyFavoriteActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

}

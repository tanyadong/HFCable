package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter;
import com.hbhongfei.hfcable.adapter.ProductRecyclerAdapter;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.pojo.TypeTwo;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.FullyGridLayoutManager;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NetUtils;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import listener.RecyclerItemClickListener;

public class MyFavoriteActivity extends AppCompatActivity implements IErrorOnclick {
    private LinearLayout noInternet;
    String S_phoneNumber;
    private static final String USER = LoginConnection.USER;
    private Dialog dialog;
    private String url;
    private ProductRecyclerAdapter productRecyclerAdapter;
    private RecyclerView productRecView;
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
        dialog = new Dialog(this);
        productRecView = (RecyclerView) findViewById(R.id.listView_myFavorite);
        noInternet = (LinearLayout) findViewById(R.id.no_internet_my_favorite);
    }
    private void setValues(){
        SharedPreferences spf =getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        //链接服务
        getCollection();

    }
    private void getCollection(){
        dialog.showDialog("正在加载中....");
        url = Url.url("/androidCollecton/getCollection");
        Map<String,String> map=new HashMap<>();
        map.put("userName",S_phoneNumber);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectCollectionListener,errorListener,map);
        MySingleton.getInstance(this).addToRequestQueue(normalPostRequest);
    }


    /**
     * 成功的监听器
     * 返回的收藏状态信息
     */
    private Response.Listener<JSONObject> jsonObjectCollectionListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            analysisDataOfMyFavirute(jsonObject);
        }

    };
    private void analysisDataOfMyFavirute(JSONObject jsonObject){
        try {
            List<Product> list=new ArrayList<>();
            JSONArray jsonArray=jsonObject.optJSONArray("productList");
            if(jsonArray.length()>0){
                int count=jsonArray.length();
                for(int i=0;i<count;i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Product product=new Product();
                    product.setId(jsonObject1.getString("id"));
                    product.setPrice(jsonObject1.getDouble("price"));
                    product.setSpecifications(jsonObject1.getString("specifications"));
                    product.setApplicationRange(jsonObject1.getString("applicationRange"));
                    product.setSpecifications(jsonObject1.getString("specifications"));
                    product.introduce=(jsonObject1.getString("introduce"));
                    product.setConductorMaterial(jsonObject1.getString("conductorMaterial"));
                    product.setCoreNumber(jsonObject1.getString("coreNumber"));
                    product.setCrossSection(jsonObject1.getString("crossSection"));
                    product.setImplementationStandards(jsonObject1.getString("implementationStandards"));
                    product.setDiameterLimit(jsonObject1.getString("diameterLimit"));
                    product.setOutsideDiameter(jsonObject1.getString("outsideDiameter"));
                    product.setSheathMaterial(jsonObject1.getString("sheathMaterial"));
                    product.setVoltage(jsonObject1.getString("voltage"));
                    product.setReferenceWeight(jsonObject1.getString("referenceWeight"));
                    product.setPurpose(jsonObject1.getString("purpose"));
                    TypeTwo typeTwo = new TypeTwo();
                    JSONObject jsonObject2=jsonObject1.getJSONObject("typeTwo");
                    typeTwo.setTypeTwoName(jsonObject2.getString("typeTwoName"));
                    product.setTypeTwo(typeTwo);
                    JSONArray jsonArray1=jsonObject1.optJSONArray("productImages");
                    //有图片时加入到产品图片集合
                    if(jsonArray1.length()>0){
                        ArrayList<String> list1=new ArrayList<>();
                        int img_count=jsonArray1.length();
                        for(int j=0;j<img_count;j++){
                            list1.add((String) jsonArray1.get(j));
                        }
                        product.setProductImages(list1);
                    }
                    list.add(product);
                }
            }else{
                Error.toSetting(noInternet, R.mipmap.nothing, "您还没有收藏宝贝", "快去收藏一个吧!", new IErrorOnclick() {
                    @Override
                    public void errorClick() {
                        Toast.makeText(MyFavoriteActivity.this,"您还没有收藏",Toast.LENGTH_SHORT).show();
                    }
                });
            }
//            MyAdapter adapter = new MyAdapter(MyFavoriteActivity.this, R.layout.intentionlayout,list);
//            list_myFavorite.setDivider(null);
//            list_myFavorite.setDividerHeight(10);
//            list_myFavorite.setAdapter(adapter);
            productRecyclerAdapter = new ProductRecyclerAdapter(MyFavoriteActivity.this,list);
            //RecyclerView子项的点击事件
            productRecView.addOnItemTouchListener(new RecyclerItemClickListener(this, productRecyclerAdapter.onItemClickListener));
            productRecView.setAdapter(productRecyclerAdapter);
            //解决scorllview嵌套recycleview冲突问题
            final FullyGridLayoutManager manager = new FullyGridLayoutManager(this, 2);
            manager.setOrientation(GridLayoutManager.VERTICAL);
            manager.setSmoothScrollbarEnabled(true);
            manager.setAutoMeasureEnabled(true);
            productRecView.setLayoutManager(manager);
            productRecView.setHasFixedSize(true);
            productRecView.setNestedScrollingEnabled(false);
            dialog.cancle();
        } catch (JSONException e) {
            e.printStackTrace();
            dialog.cancle();
        }
    }
    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dialog.cancle();
            MySingleton mySingleton = new MySingleton(MyFavoriteActivity.this);
            if (mySingleton.getCacheString(url)!=null){
                if(volleyError instanceof NoConnectionError){
                    Toast.makeText(MyFavoriteActivity.this,"没有网络",Toast.LENGTH_SHORT).show();
                }else if(volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError){
                    Toast.makeText(MyFavoriteActivity.this,"服务器端异常",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MyFavoriteActivity.this,"不好啦，出错啦",Toast.LENGTH_SHORT).show();
                }
                analysisDataOfMyFavirute(mySingleton.getCache(url));
            }else {
                if (volleyError instanceof NoConnectionError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "没有网络哦", "点击设置", MyFavoriteActivity.this);

                } else if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "大事不妙啦", "服务器出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            Toast.makeText(MyFavoriteActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "大事不妙啦", "出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            Toast.makeText(MyFavoriteActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    };

    @Override
    public void errorClick() {
        NetUtils.openSetting(this);
    }
}

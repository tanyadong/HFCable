package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.adapter.ProductRecyclerAdapter;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.pojo.TypeTwo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import listener.RecyclerItemClickListener;

/**
 * Created by 谭亚东 on 2016/8/2.
 * 获取种类服务
 */
public class ConnectionProduct {
    private Context context;
    private RecyclerView productRecyclerView;
    private ProductRecyclerAdapter productRecyclerAdapter;
    private List<Product> list;
    private int page = 1;
    private String url;
    public int countPage = 0;
    private Dialog dialog;

    public ConnectionProduct(Context context, RecyclerView productRecyclerView) {
        this.context = context;
        this.productRecyclerView = productRecyclerView;

    }

    Handler mHandler = new Handler() {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //告诉适配器，数据变化了，从新加载listview
                    productRecyclerAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 连接服务
     * 根据种类查询产品
     */
    public void connInterByType(String newProducts, int pageNo,Dialog dialog) throws JSONException {
        this.dialog = dialog;
        page = pageNo;
        url = Url.url("/androidProduct/getProduct");
        Map<String,String> map = new HashMap<>();
        map.put("newProducts", newProducts);
        map.put("pageNo", String.valueOf(pageNo));
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectProductListener, errorListener, map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 解析最新产品
     * @param jsonObject
     */
    private void analysisDataOfProduct(JSONObject jsonObject){
        try {
            list=new ArrayList<>();
            final JSONObject json_page = jsonObject.getJSONObject("page");
            final int totalPages=json_page.getInt("totalPages");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    countPage=totalPages;
                }
            });
            JSONArray jsonArray = json_page.optJSONArray("list");
            int count=jsonArray.length();
            for(int i=0;i<count;i++){
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                //typeTwo
                JSONObject jsonObject2 = jsonObject1.getJSONObject("typeTwo");
                TypeTwo typeTwo = new TypeTwo();
                typeTwo.setTypeTwoName(jsonObject2.getString("typeTwoName"));
                //产品
                Product product=new Product();
                product.setTypeTwo(typeTwo);
                product.setId(jsonObject1.getString("id"));
                product.setPrice(jsonObject1.getDouble("price"));
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

                JSONArray jsonArray1=jsonObject1.optJSONArray("productImages");
                //有图片时加入到产品图片集合
                int pro_count=jsonArray1.length();
                if(pro_count>0){
                    ArrayList<String> list1=new ArrayList<>();
                    for(int j=0;j<pro_count;j++){
                        list1.add((String) jsonArray1.get(j));
                    }
                    product.setProductImages(list1);
                }
                list.add(product);
            }
            if(page==1) {
                productRecyclerAdapter = new ProductRecyclerAdapter((Activity) context, list);
                //RecyclerView子项的点击事件
                productRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, productRecyclerAdapter.onItemClickListener));
                productRecyclerView.setAdapter(productRecyclerAdapter);
                //解决scorllview嵌套recycleview冲突问题
                final FullyGridLayoutManager manager = new FullyGridLayoutManager(context, 2);
                manager.setOrientation(GridLayoutManager.VERTICAL);
                manager.setSmoothScrollbarEnabled(true);
                manager.setAutoMeasureEnabled(true);
                productRecyclerView.setLayoutManager(manager);
                productRecyclerView.setHasFixedSize(true);
                productRecyclerView.setNestedScrollingEnabled(false);
            }else{
                productRecyclerAdapter.addItem(list);
                mHandler.sendEmptyMessage(1);
            }
            dialog.cancle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectProductListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            analysisDataOfProduct(jsonObject);
        }
    };

    /**
     * 失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            MySingleton mySingleton = new MySingleton(context);
            if (mySingleton.getCache(url)!=null){
                analysisDataOfProduct(mySingleton.getCache(url));
            }
        }
    };
}

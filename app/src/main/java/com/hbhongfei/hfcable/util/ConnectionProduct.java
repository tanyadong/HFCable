package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter;
import com.hbhongfei.hfcable.pojo.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 谭亚东 on 2016/8/2.
 * 获取种类服务
 */
public class ConnectionProduct {
    private Context context;
    private ListView listView;
    List<String> type_list=new ArrayList<>();
    public ConnectionProduct(Context context,ListView listView) {
        this.context = context;
        this.listView=listView;
    }

    /**
     * 连接服务
     * 根据种类查询产品
     * */
    public void connInterByType(String typeName) throws JSONException {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        String url = Url.url("/androidProduct/getProduct");
        Map<String,String> map=new HashMap<>();
        map.put("typeName",typeName);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectProductListener,errorListener,map);
        mQueue.add(normalPostRequest);
    }
    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectProductListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                List<Product> list=new ArrayList<>();
                JSONArray jsonArray=jsonObject.getJSONArray("productList");
                int count=jsonArray.length();
                for(int i=0;i<count;i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    Product product=new Product();
                    product.setId(jsonObject1.getString("id"));
                    product.setPrice(jsonObject1.getDouble("price"));
                    product.setApplicationRange(jsonObject1.getString("applicationRange"));
                    product.setSpecifications(jsonObject1.getString("specifications"));
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

                    JSONArray jsonArray1=jsonObject1.getJSONArray("productImages");
                    //有图片时加入到产品图片集合
                    if(jsonArray1.length()>0){
                        ArrayList<String> list1=new ArrayList<>();
                        for(int j=0;j<jsonArray1.length();j++){
                            list1.add((String) jsonArray1.get(j));
                        }
                        product.setProductImages(list1);
                    }
                    list.add(product);

                }
                MyAdapter adapter = new MyAdapter(context, R.layout.intentionlayout,list);
                listView.setDivider(null);
                listView.setAdapter(adapter);
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
            Toast.makeText(context, "请求数据失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };
}

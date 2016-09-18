package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.adapter.Address_all_Adapter;
import com.hbhongfei.hfcable.pojo.ShoppingAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 谭亚东 on 2016/8/11.
 */
public class ShoppingAddress_conn {
    String phoneNum;
    private Context context;
    private  Dialog dialog;
    private ListView listView;
    private LinearLayout linearLayout;
    public ShoppingAddress_conn(String phoneNum,Context context,ListView listView,LinearLayout linearLayout) {
        this.phoneNum = phoneNum;
        this.context=context;
        this.listView=listView;
        this.linearLayout=linearLayout;
    }

    /**
     * 获取地址服务
     * @param
     */
    public void addressListConnection(){

            dialog = new Dialog(context);
            dialog.showDialog("正在加载中。。。");
        RequestQueue queue= Volley.newRequestQueue(context);
        String url= Url.url("/androidAddress/getAddress");
        Map<String,String> map=new HashMap<>();
        map.put("userName",phoneNum);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,getSuccessListener,errorListener,map);
        queue.add(normalPostRequest);
    }


    /**
     * 获取收货地址成功的监听器
     */
    public Response.Listener<JSONObject> getSuccessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                    List<ShoppingAddress> list = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("address_list");
                if(jsonArray.length()>0){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ShoppingAddress shoppingAddress = new ShoppingAddress();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        shoppingAddress.setId(jsonObject1.getString("id"));
                        shoppingAddress.setConsignee(jsonObject1.getString("consignee"));
                        shoppingAddress.setDetailAddress(jsonObject1.getString("detailAddress"));
                        shoppingAddress.setLocalArea(jsonObject1.getString("localArea"));
                        shoppingAddress.setPhone(jsonObject1.getString("phone"));
                        shoppingAddress.setTag(jsonObject1.getInt("tag"));
                        list.add(shoppingAddress);
                    }
                    //给listview添加数据
                    Address_all_Adapter address_all_adapter = new Address_all_Adapter(context, list, phoneNum, listView, linearLayout);
                    listView.setAdapter(address_all_adapter);
                    listView.setDivider(null);
                    listView.setDividerHeight(30);
                    dialog.cancle();
                }else{
                    dialog.cancle();
                    linearLayout.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 获取收货地址成功的监听器
     */
//    public Response.Listener<JSONObject> getSuccessListener = new Response.Listener<JSONObject>() {
//        @Override
//        public void onResponse(JSONObject jsonObject) {
//            try {
//                List<ShoppingAddress> list = new ArrayList<>();
//                JSONArray jsonArray = jsonObject.getJSONArray("address_list");
//                if(jsonArray.length()>0){
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        ShoppingAddress shoppingAddress = new ShoppingAddress();
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        shoppingAddress.setId(jsonObject1.getString("id"));
//                        shoppingAddress.setConsignee(jsonObject1.getString("consignee"));
//                        shoppingAddress.setDetailAddress(jsonObject1.getString("detailAddress"));
//                        shoppingAddress.setLocalArea(jsonObject1.getString("localArea"));
//                        shoppingAddress.setPhone(jsonObject1.getString("phone"));
//                        shoppingAddress.setTag(jsonObject1.getInt("tag"));
//                        list.add(shoppingAddress);
//                    }
//                    //给listview添加数据
//                    if(page==1) {
//                        addressListAdapter = new Address_all_Adapter(context, list, phoneNum, listView, linearLayout);
//                        listView.setAdapter(addressListAdapter);
//                        listView.setDivider(null);
//                        listView.setDividerHeight(30);
////                        dialog.cancle();
//                    }else{
//                        addressListAdapter.addItems(list);
//                        myHandler.sendEmptyMessage(0);
//                        listView.setAdapter(addressListAdapter);
//                    }
//
//                }else{
//                    if(page==1){
////                        dialog.cancle();
//                    }
//                    linearLayout.setVisibility(View.VISIBLE);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    };
    /**
     *  失败的监听器
     */
    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dialog.cancle();
            Toast.makeText(context,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };
}

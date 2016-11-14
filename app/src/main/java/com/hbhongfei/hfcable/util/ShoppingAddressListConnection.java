package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.AddressListAdapter;
import com.hbhongfei.hfcable.pojo.ShoppingAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 苑雪元 on 2016/8/11.
 */
public class ShoppingAddressListConnection {
    String phoneNum;
    private Context context;
    private  Dialog dialog;
    private ListView listView;
    private LinearLayout linearLayout,noInternet;
    private Activity activity;
    AddressListAdapter addressListAdapter;
    int page=1;
    public ShoppingAddressListConnection(Activity activity,String phoneNum, Context context, ListView listView, LinearLayout linearLayout,LinearLayout noIternet) {
        this.phoneNum = phoneNum;
        this.activity = activity;
        this.context=context;
        this.listView=listView;
        this.linearLayout=linearLayout;
        this.noInternet = noIternet;
    }

  Handler myHandler=new Handler(){
      @Override
      public void handleMessage(Message msg) {
          super.handleMessage(msg);
          if(msg.what==0){
              addressListAdapter.notifyDataSetChanged();
          }
      }
  };

    /**
     * 获取地址服务
     * @param
     */
    public void addressListConnection(){
        dialog=new Dialog(context);
        dialog.showDialog("正在加载中。。。");

        String url= Url.url("/androidAddress/getAddress");
        Map<String,String> map=new HashMap<>();
        map.put("userName",phoneNum);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,getSuccessListener,errorListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
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
                int address_count=jsonArray.length();
                if(address_count>0){
                    for (int i = 0; i < address_count; i++) {
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
                        addressListAdapter = new AddressListAdapter(activity, context, list, phoneNum, listView, linearLayout);
                        listView.setAdapter(addressListAdapter);
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
     *  失败的监听器
     */
    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dialog.cancle();
            if (volleyError instanceof NoConnectionError){
                Error.toSetting(noInternet, R.mipmap.internet_no, "没有网络哦", "点击设置", new IErrorOnclick() {
                    @Override
                    public void errorClick() {
                        NetUtils.openSetting(activity);
                    }
                });
            }else if(volleyError instanceof NetworkError ||volleyError instanceof ServerError ||volleyError instanceof TimeoutError){
                Error.toSetting(noInternet,R.mipmap.internet_no,"不好啦","服务器出错啦",null);
            }else{
                Error.toSetting(noInternet,R.mipmap.internet_no,"不好啦","出错啦",null);
            }
        }
    };
}

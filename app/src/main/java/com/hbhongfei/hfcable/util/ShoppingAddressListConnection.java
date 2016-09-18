package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
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
    private LinearLayout linearLayout;
    private Activity activity;
    AddressListAdapter addressListAdapter;
    int page=1;
    public ShoppingAddressListConnection(Activity activity,String phoneNum, Context context, ListView listView, LinearLayout linearLayout) {
        this.phoneNum = phoneNum;
        this.activity = activity;
        this.context=context;
        this.listView=listView;
        this.linearLayout=linearLayout;
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
    public void addressListConnection(int pageNo){
        RequestQueue queue= Volley.newRequestQueue(context);
        page=pageNo;
        dialog=new Dialog(context);
        dialog.showDialog("正在加载中。。。");

        String url= Url.url("/androidAddress/getAddress");
        Map<String,String> map=new HashMap<>();
        System.out.println(phoneNum);

        map.put("userName",phoneNum);
        map.put("pageNo",String.valueOf(pageNo));
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
                    if(page==1) {
                        addressListAdapter = new AddressListAdapter(activity, context, list, phoneNum, listView, linearLayout);
                        listView.setAdapter(addressListAdapter);
                    }else{
                        addressListAdapter.addItems(list);
                        myHandler.sendEmptyMessage(0);
                    }
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
            Toast.makeText(context,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };
}

package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
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
    private Dialog dialog;
    private ListView listView;
    private LinearLayout linearLayout, noInternet;
    private Activity activity;
private String url;
    public ShoppingAddress_conn(Context context, String phoneNum, Activity activity, ListView listView, LinearLayout linearLayout, LinearLayout noInternet) {
        this.phoneNum = phoneNum;
        this.context = context;
        this.listView = listView;
        this.linearLayout = linearLayout;
        this.activity = activity;
        this.noInternet = noInternet;
    }

    /**
     * 获取地址服务
     *
     * @param
     */
    public void addressListConnection() {
        dialog = new Dialog(context);
        dialog.showDialog("正在加载中。。。");
        url = Url.url("/androidAddress/getAddress");
        Map<String, String> map = new HashMap<>();
        map.put("userName", phoneNum);
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, getSuccessListener, errorListener, map);
        MySingleton.getInstance(activity.getApplicationContext()).addToRequestQueue(normalPostRequest);
    }
    /**
     * 获取收货地址成功的监听器
     */
    public Response.Listener<JSONObject> getSuccessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            analysisDataOfAddress(jsonObject);
        }
    };
    /**
     * 失败的监听器
     */
    public Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dialog.cancle();
            MySingleton mySingleton = new MySingleton(context);
            if (mySingleton.getCacheString(url)!=null){
                if(volleyError instanceof NoConnectionError){
                    Toast.makeText(context,"没有网络",Toast.LENGTH_SHORT).show();
                }else if(volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError){
                    Toast.makeText(context,"服务器端异常",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"不好啦，出错啦",Toast.LENGTH_SHORT).show();
                }
                noInternet.setVisibility(View.GONE);
                analysisDataOfAddress(mySingleton.getCache(url));
            }else {
                if (volleyError instanceof NoConnectionError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "没有网络哦", "点击设置", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            NetUtils.openSetting(activity);
                        }
                    });
                } else if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "服务器出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {

                        }
                    });
                } else {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                        }
                    });
                }
            }
        }
    };

    /**
     * 解析json
     * @param jsonObject
     */
    private void analysisDataOfAddress(JSONObject jsonObject){
        try {
            List<ShoppingAddress> list = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray("address_list");
            if (jsonArray.length() > 0) {
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
                Address_all_Adapter address_all_adapter = new Address_all_Adapter(activity,context, list, phoneNum, listView, linearLayout,noInternet);
                listView.setAdapter(address_all_adapter);
                listView.setDivider(null);
                listView.setDividerHeight(30);
                dialog.cancle();
            }else{
                dialog.cancle();
                linearLayout.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            dialog.cancle();
            e.printStackTrace();
        }
    }
}

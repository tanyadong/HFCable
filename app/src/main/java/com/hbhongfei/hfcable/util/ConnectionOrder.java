package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
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
import com.hbhongfei.hfcable.activity.OrderDetailActivity;
import com.hbhongfei.hfcable.adapter.MyOrder_all_Adapter;
import com.hbhongfei.hfcable.pojo.Logistics;
import com.hbhongfei.hfcable.pojo.Order;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.pojo.ShoppingAddress;
import com.hbhongfei.hfcable.pojo.ShoppingCart;
import com.hbhongfei.hfcable.pojo.TypeTwo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 谭亚东 on 2016/8/2.
 * 获取订单
 */
public class ConnectionOrder {
    private Context context;
    private ListView listView;
    ArrayList<Order> list;
    int page = 1;
    private int countPage = 0;
    private MyOrder_all_Adapter myOrder_all_adapter;
    private Activity activity;
    private LinearLayout noInternet;
    public boolean isResult;
private String url;
    public ConnectionOrder(Activity activity, Context context, ListView listView, LinearLayout noInternet,boolean isResult) {
        this.context = context;
        this.listView = listView;
        this.activity = activity;
        this.noInternet = noInternet;
        this.isResult=isResult;
    }

    Handler mMandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                myOrder_all_adapter.notifyDataSetChanged();
            }
        }
    };


    /**
     * 连接服务
     * 根据用户ID查询订单(查询所有)
     */
    public void connInterByUserId(String userid, int pageNo) throws JSONException {
        page=pageNo;
        url = Url.url("/androidOrder/list");
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", userid);
        map.put("pageno", String.valueOf(pageNo));
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectOrderListener, errorListener, map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 连接服务
     * 未付款订单
     */
    public void connInterUnPay(int pageNo, String userid, int tag, int cancleOrNot) throws JSONException {
        page=pageNo;
         url = Url.url("/androidOrder/unPayList");
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", userid);
        map.put("tag", String.valueOf(tag));
        map.put("cancleOrNot", String.valueOf(cancleOrNot));
        map.put("pageno", String.valueOf(pageNo));
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectOrderListener, errorListener, map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 连接服务
     * 未发货
     */
    public void connInterUnSend(int pageNo, String userid) throws JSONException {
        page=pageNo;
         url = Url.url("/androidOrder/unSendList");
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", userid);
        map.put("pageno", String.valueOf(pageNo));
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectOrderListener, errorListener, map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 连接服务
     * 未发货
     */
    public void connInterUnDelivery(int pageNo, String userid) throws JSONException {
        page=pageNo;
         url = Url.url("/androidOrder/unDeliveryList");
        Map<String, String> map = new HashMap<>();
        map.put("phoneNum", userid);
        map.put("pageno", String.valueOf(pageNo));
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectOrderListener, errorListener, map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }



    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectOrderListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            analysisDataOfOrder(jsonObject);
        }
    };

    /**
     * 解析返回的订单数据
     * @param jsonObject
     */
    private void analysisDataOfOrder(JSONObject jsonObject){
        try {
            list=new ArrayList<>();
            final JSONObject json_page = jsonObject.getJSONObject("page");
            final int totalPages=json_page.getInt("totalPages");
            if(totalPages!=0){
                noInternet.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                mMandler.post(new Runnable() {
                    @Override
                    public void run() {
                        countPage=totalPages;
                    }
                });
                JSONArray jsonArray = json_page.getJSONArray("list");
                int count = jsonArray.length();

                for (int i = 0; i < count; i++) {
                    JSONObject json_order = jsonArray.getJSONObject(i);
                    JSONObject json_shCart = json_order.getJSONObject("shoppingCart");
                    JSONObject json_address = json_order.getJSONObject("shoppingAddress");
                    TypeTwo typeTwo = new TypeTwo();
                    /**  *******订单信息*********/
                    final Order order = new Order();
                    order.id = json_order.getString("id");
                    order.orderNumber = json_order.getString("orderNumber");
                    order.tag = json_order.getInt("tag");  //是否付款
                    order.shipOrNot = json_order.getInt("shipOrNot");
                    order.money = json_order.getDouble("money");
                    order.completeOrNot = json_order.getInt("completeOrNot");
                    order.cancleOrNot = json_order.getInt("cancleOrNot");
                    order.orderTime = json_order.getLong("orderTime");
                    System.out.println(json_order.getLong("orderTime"));
                    order.deleteOrNot = json_order.getInt("deleteOrNot");
                    /**  *******购物车信息*********/
                    ShoppingCart shoppingCart = new ShoppingCart();
                    shoppingCart.id = json_shCart.getString("id");
                    shoppingCart.quantity = json_shCart.getInt("quantity");
                    shoppingCart.packages = json_shCart.getString("packages");
                    shoppingCart.color = json_shCart.getString("color");
                    shoppingCart.unitPrice = json_shCart.getDouble("unitPrice");
                    /**  *******收获地址*********/
                    ShoppingAddress address = new ShoppingAddress();
                    address.setConsignee(json_address.getString("consignee"));
                    address.setPhone(json_address.getString("phone"));
                    address.setLocalArea(json_address.getString("localArea"));
                    address.setDetailAddress(json_address.getString("detailAddress"));
                    /**  *******物流信息*********/
                    Logistics logistics = new Logistics();
//                    if(json_order.getJSONObject("logistics")!=null){
//                        JSONObject json_logistics=json_order.getJSONObject("logistics");
//                        logistics.logisticsCompanyName=json_logistics.getString("logisticsCompanyName");
//                        logistics.logisticsNumber=json_logistics.getString("logisticsNumber");
//                    }

                    /**  *******产品信息*********/
                    JSONObject json_pro = json_shCart.getJSONObject("product");
                    Product product = new Product();
                    typeTwo.typeTwoName = json_pro.getJSONObject("typeTwo").getString("typeTwoName");
                    product.setId(json_pro.getString("id"));
                    product.setPrice(json_pro.getDouble("price"));
                    product.setTypeTwo(typeTwo);
                    product.setApplicationRange(json_pro.getString("applicationRange"));
                    product.setSpecifications(json_pro.getString("specifications"));
                    product.setConductorMaterial(json_pro.getString("conductorMaterial"));
                    product.setCoreNumber(json_pro.getString("coreNumber"));
                    product.setCrossSection(json_pro.getString("crossSection"));
                    product.setImplementationStandards(json_pro.getString("implementationStandards"));
                    product.setDiameterLimit(json_pro.getString("diameterLimit"));
                    product.setOutsideDiameter(json_pro.getString("outsideDiameter"));
                    product.setSheathMaterial(json_pro.getString("sheathMaterial"));
                    product.setVoltage(json_pro.getString("voltage"));
                    product.setReferenceWeight(json_pro.getString("referenceWeight"));
                    product.setPurpose(json_pro.getString("purpose"));
                    JSONArray array = json_pro.getJSONArray("productImages");
                    //有图片时加入到产品图片集合
                    if (array.length() > 0) {
                        ArrayList<String> list1 = new ArrayList<>();
                        for (int k = 0; k < array.length(); k++) {
                            list1.add((String) array.get(k));
                        }
                        product.setProductImages(list1);
                    }
                    shoppingCart.product = product;
                    order.shoppingCart = shoppingCart;
                    order.logistics = logistics;
                    order.shoppingAddress = address;
                    list.add(order);
                }
                if(page==1){
                    myOrder_all_adapter = new MyOrder_all_Adapter(context, R.layout.item_my_order, list);
                    listView.setAdapter(myOrder_all_adapter);

                }else {
                    myOrder_all_adapter.addItems(list);
                    mMandler.sendEmptyMessage(0);
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(context.getApplicationContext(), OrderDetailActivity.class);
                        intent.putExtra("order",list.get(position));
                        intent.putExtra("position",position);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                        mMandler.post(new Runnable() {
                            @Override
                            public void run() {
                                isResult=true;
                            }
                        });
                    }
                });

            } else {
                //没有数据
                Error.toSetting(noInternet, R.mipmap.order_empty, "没有订单记录哦", "赶紧去下单吧", new IErrorOnclick() {
                    @Override
                    public void errorClick() {
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
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
                analysisDataOfOrder(mySingleton.getCache(url));
            }else {
                if (volleyError instanceof NoConnectionError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "没有网络哦", "点击设置", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            NetUtils.openSetting(activity);
                        }
                    });

                    listView.setVisibility(View.GONE);
                    noInternet.setVisibility(View.VISIBLE);
                } else if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "大事不妙啦", "服务器出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
//                        Toast.makeText(context, "出错啦", Toast.LENGTH_SHORT).show();
                        }
                    });
                    listView.setVisibility(View.GONE);
                    noInternet.setVisibility(View.VISIBLE);
                } else {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "大事不妙啦", "出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
//                        Toast.makeText(context, "出错啦", Toast.LENGTH_SHORT).show();
                        }
                    });
                    listView.setVisibility(View.GONE);
                    noInternet.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    /**
     * 获取总页数
     */
    public int getTotalPage() {
        return countPage;
    }
}

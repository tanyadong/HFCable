package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyOrder_all_Adapter;
import com.hbhongfei.hfcable.pojo.Order;
import com.hbhongfei.hfcable.pojo.Product;
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
    int page=1;
    private int countPage=0;
    Dialog dialog;
    private MyOrder_all_Adapter myOrder_all_adapter;
    public ConnectionOrder(Context context, ListView listView) {
        this.context = context;
        this.listView=listView;
    }
    Handler mMandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                myOrder_all_adapter.notifyDataSetChanged();
            }
// else if (msg.what==1) {
//                countPage=msg.arg1;
////                getTotalPage();
//                Toast.makeText(context,countPage+" aaaaa",Toast.LENGTH_SHORT).show();
//            }
        }
    };


    /**
     * 连接服务
     * 根据用户ID查询订单(查询所有)
     * */
    public void connInterByUserId(String userid,int pageNo) throws JSONException {

        dialog=new Dialog(context);
        String url = Url.url("/androidOrder/list");
        Map<String,String> map=new HashMap<>();
        map.put("phoneNum",userid);
        map.put("pageno",String.valueOf(pageNo));
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectOrderListener,errorListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 连接服务
     * 未付款订单
     * */
    public void connInterUnPay(int page,String userid,int tag,int cancleOrNot) throws JSONException {
        dialog=new Dialog(context);
        String url = Url.url("/androidOrder/unPayList");
        Map<String,String> map=new HashMap<>();
        map.put("phoneNum",userid);
        map.put("tag",String.valueOf(tag));
        map.put("cancleOrNot",String.valueOf(cancleOrNot));
        map.put("pageno",String.valueOf(page));
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectOrderListener,errorListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 连接服务
     * 未发货
     * */
    public void connInterUnSend(int page,String userid) throws JSONException {
        dialog=new Dialog(context);
        String url = Url.url("/androidOrder/unSendList");
        Map<String,String> map=new HashMap<>();
        map.put("phoneNum",userid);
        map.put("pageno",String.valueOf(page));
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectOrderListener,errorListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 连接服务
     * 未发货
     * */
    public void connInterUnDelivery(int page,String userid) throws JSONException {
        dialog=new Dialog(context);
        String url = Url.url("/androidOrder/unDeliveryList");
        Map<String,String> map=new HashMap<>();
        map.put("phoneNum",userid);
        map.put("pageno",String.valueOf(page));
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectOrderListener,errorListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }
    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectOrderListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                list=new ArrayList<>();
                JSONObject json_page=jsonObject.getJSONObject("page");
                countPage=json_page.getInt("totalPages");
                getTotalPage();
//                Message msg=new Message();
//                msg.what=1;
//                msg.arg1=pageCount;
//                mMandler.sendMessage(msg);
                JSONArray jsonArray=json_page.getJSONArray("list");
                int count=jsonArray.length();
                for(int i=0;i<count;i++){
                    JSONObject json_order=jsonArray.getJSONObject(i);
                    JSONObject json_shCart=json_order.getJSONObject("shoppingCart");
                    TypeTwo typeTwo=new TypeTwo();
                    /**  *******订单信息*********/
                    Order order=new Order();
                    order.id=json_order.getString("id");
                    order.orderNumber=json_order.getString("orderNumber");
                    order.tag=json_order.getInt("tag");  //是否付款
                    order.shipOrNot=json_order.getInt("shipOrNot");
                    order.money=json_order.getDouble("money");
                    order.completeOrNot=json_order.getInt("completeOrNot");
                    order.cancleOrNot=json_order.getInt("cancleOrNot");
                    /**  *******购物车信息*********/
                    ShoppingCart shoppingCart=new ShoppingCart();
                    shoppingCart.id=json_shCart.getString("id");
                    shoppingCart.quantity=json_shCart.getInt("quantity");
                    shoppingCart.packages=json_shCart.getString("packages");
                    shoppingCart.color=json_shCart.getString("color");
                    shoppingCart.unitPrice=json_shCart.getDouble("unitPrice");
                    /**  *******产品信息*********/
                    JSONObject json_pro=json_shCart.getJSONObject("product");
                    Product product=new Product();
                    typeTwo.typeTwoName=json_pro.getJSONObject("typeTwo").getString("typeTwoName");
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
                    JSONArray array=json_pro.getJSONArray("productImages");
                    //有图片时加入到产品图片集合
                    if(array.length()>0){
                        ArrayList<String> list1=new ArrayList<>();
                        for(int k=0;k<array.length();k++){
                            list1.add((String) array.get(k));
                        }
                        product.setProductImages(list1);
                    }
                    shoppingCart.product=product;
                    order.shoppingCart=shoppingCart;
                    list.add(order);
                }
                if(page==1) {
                    myOrder_all_adapter = new MyOrder_all_Adapter(context, R.layout.item_my_order,list);
                    listView.setAdapter(myOrder_all_adapter);
                }else{

                }
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

    /**
     * 获取总页数
     */
    public int getTotalPage(){
        return countPage;
    }
}

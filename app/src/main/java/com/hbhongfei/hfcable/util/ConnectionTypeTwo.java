package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter_typeTwo;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.pojo.TypeTwo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 谭亚东 on 2016/8/2.
 * 获取二级种类
 */
public class ConnectionTypeTwo {
    private Context context;
    private ExpandableListView listView;
    List<TypeTwo> typeTwo_list;
    ArrayList<Product> pro_list;
    ArrayList<ArrayList<Product>> list;
    int page=1;
    public int totalCount;
    private MyAdapter_typeTwo myAdapter_typeTwo;
    private Activity activity;
    private LinearLayout noInternet;
    private String url;
    public ConnectionTypeTwo(Activity activity, Context context, ExpandableListView listView, LinearLayout noInternet) {
        this.context = context;
        this.listView=listView;
        this.activity = activity;
        this.noInternet = noInternet;
    }
    Handler mMandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                myAdapter_typeTwo.notifyDataSetChanged();
            }
        }
    };
    /**
     * 连接服务
     * 根据种类查询二级种类
     * */
    public void connInterByType(String typeName,int pageNo) throws JSONException {
        page=pageNo;
         url= Url.url("/androidTypeTwo/getTypeTwo");
        Map<String,String> map=new HashMap<>();
        map.put("typeName",typeName);
        map.put("pageNo",String.valueOf(pageNo));
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectTypeTwoListener,errorListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * jiexi二级种类
     * @param jsonObject
     */
    private void analysisDataOfTypeTwo(final JSONObject jsonObject){
        try {
            list=new ArrayList<>();
            typeTwo_list=new ArrayList<>();
            JSONArray jsonArray=jsonObject.getJSONArray("typeTwolist");
            final int count=jsonArray.length();
            if (count>0){
                listView.setVisibility(View.VISIBLE);
                noInternet.setVisibility(View.GONE);
                mMandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            totalCount=jsonObject.getInt("totalPages");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                for(int i=0;i<count;i++){
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    JSONObject jsonObject2=jsonObject1.getJSONObject("typeTwo");
                    TypeTwo typeTwo=new TypeTwo();
                    typeTwo.setId(jsonObject2.getString("id"));
                    typeTwo.setTypeTwoName(jsonObject2.getString("typeTwoName"));
                    typeTwo_list.add(typeTwo); //添加父组件
                    JSONArray jsonArray1=jsonObject1.getJSONArray("products");
                    pro_list=new ArrayList<>();
                    int count_product=jsonArray1.length();
                    for(int j=0;j<count_product;j++){
                        JSONObject object= (JSONObject) jsonArray1.get(j);
                        Product product=new Product();
                        product.setId(object.getString("id"));
                        product.setPrice(object.getDouble("price"));
                        product.setTypeTwo(typeTwo);
                        product.setApplicationRange(object.getString("applicationRange"));
                        product.setSpecifications(object.getString("specifications"));
                        product.introduce=object.getString("introduce");
                        product.setConductorMaterial(object.getString("conductorMaterial"));
                        product.setCoreNumber(object.getString("coreNumber"));
                        product.setCrossSection(object.getString("crossSection"));
                        product.setImplementationStandards(object.getString("implementationStandards"));
                        product.setDiameterLimit(object.getString("diameterLimit"));
                        product.setOutsideDiameter(object.getString("outsideDiameter"));
                        product.setSheathMaterial(object.getString("sheathMaterial"));
                        product.setVoltage(object.getString("voltage"));
                        product.setReferenceWeight(object.getString("referenceWeight"));
                        product.setPurpose(object.getString("purpose"));

                        JSONArray array=object.getJSONArray("productImages");
                        //有图片时加入到产品图片集合
                        int proimg_count=array.length();
                        if(proimg_count>0){
                            ArrayList<String> list1=new ArrayList<>();
                            for(int k=0;k<proimg_count;k++){
                                list1.add((String) array.get(k));
                            }
                            product.setProductImages(list1);
                        }
                        pro_list.add(product);
                    }
                    list.add(pro_list);
                }

                if(page==1) {
                    myAdapter_typeTwo = new MyAdapter_typeTwo(typeTwo_list, list, context);
                    listView.setAdapter(myAdapter_typeTwo);
                }else{
                    myAdapter_typeTwo.addGroup(typeTwo_list);
                    myAdapter_typeTwo.addChild(list);
                    mMandler.sendEmptyMessage(0);
                }
                int group_count=myAdapter_typeTwo.getGroupCount();
                for (int i = 0; i < group_count; i++) {
                    listView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                }
            }else {
                //没有数据
                Error.toSetting(noInternet, R.mipmap.nothing, "暂无数据哦", "换一个试试", new IErrorOnclick() {
                    @Override
                    public void errorClick() {
                        Toast.makeText(context,"暂无数据",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectTypeTwoListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(final JSONObject jsonObject) {
            analysisDataOfTypeTwo(jsonObject);
        }
    };

    /**
     *  失败的监听器
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
                analysisDataOfTypeTwo(mySingleton.getCache(url));
            }else {
                if (volleyError instanceof NoConnectionError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "没有网络哦", "点击设置", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            NetUtils.openSetting(activity);
                        }
                    });
                } else if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "大事不妙啦", "服务器出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            Toast.makeText(context, "出错啦", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "大事不妙啦", "出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            Toast.makeText(context, "出错啦", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    };
}

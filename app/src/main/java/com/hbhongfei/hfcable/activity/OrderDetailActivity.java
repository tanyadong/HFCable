package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.LogisticsData;
import com.hbhongfei.hfcable.pojo.Order;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.util.ConnectionOrder;
import com.hbhongfei.hfcable.util.DateUtils;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textview_order_num,textview_order_time,textview_order_state;  //订单信息
    private RelativeLayout rl_order_logistics,rl_order_product; //最新物流信息
    LinearLayout rl_bottom_btn;
    private RelativeLayout rl_orderdetail_item_btn;
    private TextView tview_order_logistics,tview_order_logistics_time,tview_order_freight,tview_order_totalmoney;
    private TextView name,location,telphone;
    private TextView Tview_myOrder_stage,Tview_myOrder_type,Tview_myOrder_introduce,Tview_myOrder_price;
    private ImageView image_myOrder,Image_myOrder_delete;
    private Button btn_order_cancle,btn_order_pay,btn_order_confirmReceipt,btn_order_viewlogistics;
//    KdniaoTrackQueryAPI api;
    private Order order;
    private  Product product;
    private ArrayList<LogisticsData> list;
    private String state;//物流状态
    Dialog dialog;
    Intent intent=new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initview();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //数据
        setValues();
        onClick();
    }

    public Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    SparseArray<Object> array= (SparseArray<Object>) msg.obj;
                    list= (ArrayList<LogisticsData>) array.get(0);
                    state= (String) array.get(1);
                    break;
            }
        }
    };
    public void initview(){
        //订单
        textview_order_num= (TextView) findViewById(R.id.textview_order_num);
        textview_order_time= (TextView) findViewById(R.id.textview_order_time);
        textview_order_state= (TextView) findViewById(R.id.textview_order_state);
        //地址
        this.name = (TextView) findViewById(R.id.Tview_confirm_order_name);
        this.location = (TextView) findViewById(R.id.Tview_confirm_order_location);
        this.telphone = (TextView) findViewById(R.id.Tview_confirm_order_tel1);
        rl_order_product= (RelativeLayout) findViewById(R.id.rl_order_product);
        rl_order_logistics= (RelativeLayout) findViewById(R.id.rl_order_logistics);
        //物流信息
        tview_order_logistics= (TextView) findViewById(R.id.tview_order_logistics);
        tview_order_logistics_time= (TextView) findViewById(R.id.tview_logistics_time);
        Tview_myOrder_stage= (TextView)rl_order_product.findViewById(R.id.Tview_myOrder_stage);
        Tview_myOrder_stage.setVisibility(View.GONE);
        Tview_myOrder_type = (TextView) rl_order_product.findViewById(R.id.Tview_myOrder_type);
        Tview_myOrder_introduce = (TextView) rl_order_product.findViewById(R.id.Tview_myOrder_introduce);
        image_myOrder = (ImageView) rl_order_product.findViewById(R.id.image_myOrder);
        Image_myOrder_delete = (ImageView) rl_order_product.findViewById(R.id.Image_myOrder_delete);
        Image_myOrder_delete.setVisibility(View.GONE);
        Tview_myOrder_price= (TextView) rl_order_product.findViewById(R.id.Tview_myOrder_price);
        tview_order_freight= (TextView) findViewById(R.id.tview_order_freight);
        tview_order_totalmoney= (TextView) findViewById(R.id.tview_order_totalmoney);

        rl_bottom_btn= (LinearLayout) findViewById(R.id.rl_bottom_btn);
        rl_orderdetail_item_btn= (RelativeLayout) findViewById(R.id.rl_orderdetail_item_btn);
        btn_order_cancle= (Button) rl_bottom_btn.findViewById(R.id.btn_order_cancle);
        btn_order_pay= (Button) rl_bottom_btn.findViewById(R.id.btn_order_goPay);
        btn_order_confirmReceipt= (Button) rl_bottom_btn.findViewById(R.id.btn_order_confirmReceipt);
        btn_order_viewlogistics= (Button) rl_bottom_btn.findViewById(R.id.btn_order_viewlogistics);
    }
    public void setValues(){
        Intent intent=getIntent();
        order= (Order) intent.getSerializableExtra("order");

        if(order.shipOrNot==1){
            dialog=new Dialog(this);
            dialog.showDialog("正在加载中");
            try {
                getLogitis();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //顶部订单信息
        String orderTime= DateUtils.timeStampToStr(order.orderTime);
        textview_order_num.setText(order.orderNumber);
        textview_order_time.setText(orderTime);
        if(order.cancleOrNot==1){
            textview_order_state.setText("订单已取消");
            btn_order_cancle.setVisibility(View.GONE);
            btn_order_pay.setVisibility(View.GONE);
            if(order.shipOrNot==1){
                btn_order_viewlogistics.setVisibility(View.VISIBLE);
                btn_order_viewlogistics.setOnClickListener(this);
            }else{

                rl_orderdetail_item_btn.setVisibility(View.GONE);
            }
        }else if(order.cancleOrNot==0&&order.tag==1){
            textview_order_state.setText("待付款");
            rl_order_logistics.setVisibility(View.GONE);
            btn_order_confirmReceipt.setVisibility(View.GONE);
            btn_order_viewlogistics.setVisibility(View.GONE);
            btn_order_pay.setOnClickListener(this);
            btn_order_cancle.setOnClickListener(this);
        }else if(order.cancleOrNot==0&&order.shipOrNot==2&&order.tag==2){
            textview_order_state.setText("待发货");
            btn_order_pay.setVisibility(View.GONE);
            btn_order_cancle.setOnClickListener(this);
        }else if(order.cancleOrNot==0&&order.shipOrNot==1&&order.completeOrNot==0){
            textview_order_state.setText("待收货");
            btn_order_pay.setVisibility(View.GONE);
            btn_order_confirmReceipt.setVisibility(View.VISIBLE);
            btn_order_viewlogistics.setOnClickListener(this); //查看物流
            btn_order_confirmReceipt.setOnClickListener(this);
            btn_order_cancle.setOnClickListener(this);
        }
        if(order.cancleOrNot==0&&order.completeOrNot==1){
            textview_order_state.setText("已完成，欢迎您再次购买");
            btn_order_cancle.setVisibility(View.GONE);
            btn_order_pay.setVisibility(View.GONE);
            btn_order_confirmReceipt.setVisibility(View.GONE);
            btn_order_viewlogistics.setVisibility(View.VISIBLE);
            btn_order_viewlogistics.setOnClickListener(this);
        }
        //收货地址
        name.setText(order.shoppingAddress.getConsignee());
        telphone.setText(order.shoppingAddress.getPhone());
        location.setText(order.shoppingAddress.getLocalArea()+order.shoppingAddress.getDetailAddress());

        //产品信息
        product=order.shoppingCart.product;
        Tview_myOrder_type.setText(product.getTypeTwo().typeTwoName);
        String introduce=order.getShoppingCart().getProduct().introduce;
        Tview_myOrder_introduce.setText(introduce);
        String unit_money=String.valueOf(order.shoppingCart.unitPrice);
        Tview_myOrder_price.setText(unit_money);        //单价
        //设置图片
        ArrayList<String> imgs=order.getShoppingCart().getProduct().getProductImages();
        //加载图片
        String url=null;
        if(imgs.size()>0){
             url= Url.url(imgs.get(0));
        }
        Glide.with(this.getApplicationContext())
                .load(url)
                .placeholder(R.mipmap.man)
                .error(R.mipmap.man)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(image_myOrder);
        tview_order_freight.setText("￥0");
        tview_order_totalmoney.setText(String.valueOf(order.money));
    }
    /**
     * 连接服务
     * 查看物流
     * */
    public void getLogitis() throws JSONException {
        String url = Url.url("/androidLogitis/getLogitis");
        Map<String,String> map=new HashMap<>();
        map.put("LogisticsCode","STO");
        map.put("LogisticsNum","3313367297754");
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectOrderListener,errorListener,map);
        MySingleton.getInstance(this).addToRequestQueue(normalPostRequest);
        dialog.cancle();
        rl_order_logistics.setVisibility(View.VISIBLE); //查询成功显示组件
    }
    public void onClick(){
        rl_order_logistics.setOnClickListener(this);
        btn_order_pay.setOnClickListener(this);
        rl_order_product.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        ConnectionOrder connectionOrder=new ConnectionOrder(this);
        switch (v.getId()){
            case R.id.rl_order_logistics:
                toLogisticsActivity();
                break;
            case R.id.rl_order_product:
                intent.setClass(this,ProdectInfoActivity.class);
                intent.putExtra("product",order.shoppingCart.product);
                startActivity(intent);
                break;
            case R.id.btn_order_cancle:
                connectionOrder.cancleOrder(order.orderNumber);
                break;
            case R.id.btn_order_viewlogistics:
                toLogisticsActivity();
                break;
            case R.id.btn_order_goPay:
                Intent intent=new Intent(this, OrderPayActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_order_confirmReceipt:
                connectionOrder.confirmReceipt(order.orderNumber);
                break;
        }
    }
    public void toLogisticsActivity(){
        intent.setClass(this,LogisticsDetailsActivity.class);
        Bundle bundle = new Bundle();//该类用作携带数据
        if(product.getProductImages().size()!=0){
            String img=product.getProductImages().get(0);
            bundle.putString("image",img);
        }
        bundle.putString("state",state);
        bundle.putSerializable("logistics",order.logistics);
        bundle.putSerializable("list",list);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectOrderListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                JSONArray traces=jsonObject.getJSONArray("Traces");
                if(traces==null){
                    tview_order_logistics.setText("等待快递公司览件");
                    Long time=order.logistics.logisticsTime;
                    String logisticsTime=DateUtils.timeStampToStr(time);
                    tview_order_logistics_time.setText(logisticsTime);
                }else {
                    ArrayList<LogisticsData> list=new ArrayList<>();
                    int count=traces.length();
                    JSONObject logitis=traces.getJSONObject(count-1);
                    tview_order_logistics.setText(logitis.getString("AcceptStation"));
                    tview_order_logistics_time.setText(logitis.getString("AcceptTime"));
                    for(int i=count-1;i>=0;i--){
                        LogisticsData logisticsData=new LogisticsData();
                        JSONObject logitis1=traces.getJSONObject(i);
                        logisticsData.setTime(logitis1.getString("AcceptTime"));
                        logisticsData.setContext(logitis1.getString("AcceptStation"));
                        list.add(logisticsData);
                    }
                    SparseArray<Object> sparseArray=new SparseArray();
                    sparseArray.put(0,list);
                    sparseArray.put(1,jsonObject.getString("State"));
                    Message message=new Message();
                    message.what=0;
                    message.obj=sparseArray;
                    handler.sendMessage(message);
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
            Toast.makeText(OrderDetailActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };
}

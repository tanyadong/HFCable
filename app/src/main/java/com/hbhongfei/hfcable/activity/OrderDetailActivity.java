package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.Order;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.util.DateUtils;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textview_order_num,textview_order_time,textview_order_state;  //订单信息
    private RelativeLayout rl_order_logistics,rl_order_product; //最新物流信息
    RelativeLayout rl_bottom_btn;
    private TextView tview_order_logistics,tview_order_logistics_time,tview_order_freight,tview_order_totalmoney;
    private TextView name,location,telphone;
    private TextView Tview_myOrder_stage,Tview_myOrder_type,Tview_myOrder_introduce,Tview_myOrder_price;
    private ImageView image_myOrder,Image_myOrder_delete;
    private Button btn_order_cancle,btn_order_pay;
//    KdniaoTrackQueryAPI api;
    private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initview();
        //数据
        setValues();
        onClick();
    }

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

        rl_bottom_btn= (RelativeLayout) findViewById(R.id.rl_bottom_btn);
        btn_order_cancle= (Button) rl_bottom_btn.findViewById(R.id.btn_order_cancle);
        btn_order_pay= (Button) rl_bottom_btn.findViewById(R.id.btn_order_goPay);
    }
    public void setValues(){
        Intent intent=getIntent();
        order= (Order) intent.getSerializableExtra("order");
        //顶部订单信息
        String orderTime= DateUtils.timeStampToStr(order.orderTime);
        Toast.makeText(this,orderTime,Toast.LENGTH_SHORT).show();
        textview_order_num.setText(order.orderNumber);
        textview_order_time.setText(orderTime);
        if(order.cancleOrNot==1){
            textview_order_state.setText("订单已取消");
        }else if(order.cancleOrNot==0&&order.tag==1){
            textview_order_state.setText("待付款");
        }else if(order.cancleOrNot==0&&order.shipOrNot==2&&order.tag==2){
            textview_order_state.setText("待发货");
        }else if(order.cancleOrNot==0&&order.shipOrNot==1){
            textview_order_state.setText("待收货");

        }else if(order.cancleOrNot==0&&order.completeOrNot==1){
            textview_order_state.setText("已完成，欢迎您再次购买");
        }
        //收货地址
        name.setText(order.shoppingAddress.getConsignee());
        telphone.setText(order.shoppingAddress.getPhone());
        location.setText(order.shoppingAddress.getLocalArea()+order.shoppingAddress.getDetailAddress());
        //物流
        try {
            getLogitis();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //产品信息
        Product product=order.shoppingCart.product;
        Tview_myOrder_type.setText(product.getTypeTwo().typeTwoName);
        String introduce=order.getShoppingCart().getProduct().introduce;
        Tview_myOrder_introduce.setText(introduce);
        String unit_money=String.valueOf(order.shoppingCart.unitPrice);
        Tview_myOrder_price.setText(unit_money);        //单价
        //设置图片
        ArrayList<String> imgs=order.getShoppingCart().getProduct().getProductImages();
        //加载图片
        if(imgs.size()>0){
            String url= Url.url(imgs.get(0));
            Glide.with(this.getApplicationContext())
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_myOrder);
        }

        tview_order_freight.setText("￥0");
        tview_order_totalmoney.setText(String.valueOf(order.money));
    }
    /**
     * 连接服务
     * 未发货
     * */
    public void getLogitis() throws JSONException {
        String url = Url.url("/androidLogitis/getLogitis");
        Map<String,String> map=new HashMap<>();
        map.put("LogisticsCode","STO");
        map.put("LogisticsNum","3313367297754");
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectOrderListener,errorListener,map);
        MySingleton.getInstance(this).addToRequestQueue(normalPostRequest);
    }
    public void onClick(){
        rl_order_logistics.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_order_logistics:
                Intent intent=new Intent(this,LogisticsDetailsActivity.class);
                startActivity(intent);
                break;
        }
    }


    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectOrderListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {

                JSONObject traces=jsonObject.getJSONObject("Traces");
                if(traces!=null){

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

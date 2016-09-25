package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.OrderPayActivity;
import com.hbhongfei.hfcable.pojo.Order;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 苑雪元 on 2016/8/3.
 */
public class MyOrder_all_Adapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    protected LayoutInflater inflater;
    protected int resource;
    private List<Order> list;
    public MyOrder_all_Adapter(Context context,int resource, List<Order> list){
        inflater = LayoutInflater.from(context);
        this.context =context;
        this.list = list;
        this.resource = resource;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyOrderViewHolder vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource,null);
            vh = new MyOrderViewHolder();
            vh.Tview_myOrder_stage= (TextView) convertView.findViewById(R.id.Tview_myOrder_stage);
            vh.Tview_myOrder_type = (TextView) convertView.findViewById(R.id.Tview_myOrder_type);
            vh.Tview_myOrder_introduce = (TextView) convertView.findViewById(R.id.Tview_myOrder_introduce);
            vh.Tview_myOrder_money = (TextView) convertView.findViewById(R.id.Tview_myOrder_money);
            vh.image_myOrder = (ImageView) convertView.findViewById(R.id.image_myOrder);
            vh.image_success= (ImageView) convertView.findViewById(R.id.image_success);
            vh.Image_myOrder_delete = (ImageView) convertView.findViewById(R.id.Image_myOrder_delete);
            vh.btn_order_cancle= (Button) convertView.findViewById(R.id.btn_order_cancle);
            vh.btn_order_pay= (Button) convertView.findViewById(R.id.btn_order_goPay);
            convertView.setTag(vh);
        }else {
            vh = (MyOrderViewHolder) convertView.getTag();
        }
        //赋值
        String typeTwoName=list.get(position).getShoppingCart().getProduct().getTypeTwo().typeTwoName;
        vh.Tview_myOrder_type.setText(typeTwoName);
        String introduce=list.get(position).getShoppingCart().getProduct().introduce;
        vh.Tview_myOrder_introduce.setText(introduce);
        vh.Tview_myOrder_money.setText(String.valueOf(list.get(position).getMoney()));
        //设置图片
        ArrayList<String> imgs=list.get(position).getShoppingCart().getProduct().getProductImages();
        //加载图片
        if(imgs.size()>0){
            String url= Url.url(imgs.get(0));
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(vh.image_myOrder);
        }

        //如果是未付款订单
        if(list.get(position).getTag()==1){
            if(list.get(position).cancleOrNot==0) { //未取消订单
                vh.Tview_myOrder_stage.setText("等待付款");
                vh.Tview_myOrder_stage.setTextColor(context.getResources().getColor(R.color.colorRed));
                //未取消订单时取消订单
                vh.btn_order_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancleOrder(list.get(position).getOrderNumber());
                    }
                });
                //去付款
                vh.btn_order_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, OrderPayActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
            vh.Image_myOrder_delete.setVisibility(View.GONE);
            vh.image_success.setVisibility(View.GONE);
        }
        if(list.get(position).completeOrNot==1){ //已经完成
                vh.Tview_myOrder_stage.setVisibility(View.GONE);
                vh.btn_order_cancle.setVisibility(View.GONE);
                vh.btn_order_pay.setVisibility(View.GONE);
                vh.Image_myOrder_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(list.get(position).getOrderNumber());
                    }
                });
        }
        //未发货
        if(list.get(position).getTag()==2&&list.get(position).shipOrNot==2){
            vh.Tview_myOrder_stage.setText("等待出库");
            vh.Tview_myOrder_stage.setTextColor(context.getResources().getColor(R.color.colorRed));
            vh.Image_myOrder_delete.setVisibility(View.GONE);
            vh.image_success.setVisibility(View.GONE);
            vh.btn_order_pay.setVisibility(View.GONE);
        }
        //未收货
        if(list.get(position).getTag()==2&&list.get(position).shipOrNot==1&&list.get(position).completeOrNot==0){
            vh.Tview_myOrder_stage.setText("已发货");
            vh.Tview_myOrder_stage.setTextColor(context.getResources().getColor(R.color.colorRed));
            vh.Image_myOrder_delete.setVisibility(View.GONE);
            vh.image_success.setVisibility(View.GONE);
            vh.btn_order_pay.setText("确认收货");
        }

        return convertView;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 取消订单
     */
    public void cancleOrder(String orderNum){
        String url = Url.url("/androidOrder/cancleOrder");
        Map<String,String> map=new HashMap<>();
        map.put("orderNum",orderNum);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonCancleOrderListener,errorListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 删除订单
     */
    public void deleteOrder(String orderNum){
        String url = Url.url("/androidOrder/deleteOrder");
        Map<String,String> map=new HashMap<>();
        map.put("orderNum",orderNum);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonCancleOrderListener,errorListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    public static class MyOrderViewHolder{
        TextView Tview_myOrder_type;
        ImageView image_myOrder;
        ImageView Image_myOrder_delete,image_success;
        TextView Tview_myOrder_name;
        TextView Tview_myOrder_stage;
        TextView Tview_myOrder_introduce;
        TextView Tview_myOrder_pay;
        TextView Tview_myOrder_money;
        Button btn_order_cancle,btn_order_pay;

    }

    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonCancleOrderListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg=jsonObject.getString("msg");
                if(msg.equals("success")){
                    Toast.makeText(context,"取消订单成功",Toast.LENGTH_SHORT).show();
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
}

package com.hbhongfei.hfcable.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Map;

/**
 * Created by 谭亚东 on 2016/8/3.
 */
public class MyOrder_all_Adapter extends BaseAdapter{

    private Context context;
    protected LayoutInflater inflater;
    protected int resource;
    private ArrayList<Order> list;
    private int position_tag;
    public MyOrder_all_Adapter(Activity context){
        this.context=context;
    }
    public MyOrder_all_Adapter(Context context,int resource, ArrayList<Order> list){
        inflater = LayoutInflater.from(context);
        this.context =context;
        this.list = list;
        this.resource = resource;
    }
    public void addItems(ArrayList<Order> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }
    public void deleteItems(int position){
        this.list.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if (list!=null){
            return list.size();
        }else {
            return 0;
        }
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
            convertView = inflater.inflate(R.layout.item_my_order,null);
            vh = new MyOrderViewHolder();
            vh.rl_myorder_item= convertView.findViewById(R.id.rl_myorder_item);
            vh.ll_order_bottom= (LinearLayout) convertView.findViewById(R.id.ll_order_bottom);
            vh.Tview_myOrder_stage= (TextView) vh.rl_myorder_item.findViewById(R.id.Tview_myOrder_stage);
            vh.Tview_myOrder_type = (TextView) vh.rl_myorder_item.findViewById(R.id.Tview_myOrder_type);
            vh.Tview_myOrder_introduce = (TextView) vh.rl_myorder_item.findViewById(R.id.Tview_myOrder_introduce);
            vh.tv_buy_num= (TextView) vh.rl_myorder_item.findViewById(R.id.tv_buy_num);
            vh.tv_package= (TextView) vh.rl_myorder_item.findViewById(R.id.tv_package);
            vh.Tview_myOrder_price= (TextView) vh.rl_myorder_item.findViewById(R.id.Tview_myOrder_price);
            vh.Tview_myOrder_money = (TextView) convertView.findViewById(R.id.Tview_myOrder_money);
            vh.Tview_myOrder_pay= (TextView) convertView.findViewById(R.id.Tview_myOrder_pay);
            vh.image_myOrder = (ImageView) vh.rl_myorder_item.findViewById(R.id.image_myOrder);
            vh.image_success= (ImageView) convertView.findViewById(R.id.image_success);
            vh.Image_myOrder_delete = (ImageView) vh.rl_myorder_item.findViewById(R.id.Image_myOrder_delete);
            vh.btn_order_cancle= (Button) convertView.findViewById(R.id.btn_order_cancle);
            vh.btn_order_pay= (Button) convertView.findViewById(R.id.btn_order_goPay);
            vh.btn_order_confirmReceipt= (Button) convertView.findViewById(R.id.btn_order_confirmReceipt);
            convertView.setTag(vh);
        }else {
            vh = (MyOrderViewHolder) convertView.getTag();
        }
        //赋值

        final Order order=list.get(position);
        String typeTwoName=list.get(position).getShoppingCart().getProduct().getTypeTwo().typeTwoName;
        vh.Tview_myOrder_type.setText(typeTwoName);
        String introduce=list.get(position).getShoppingCart().getProduct().introduce;
        vh.Tview_myOrder_introduce.setText(introduce);
        vh.tv_buy_num.setText(String.valueOf(order.shoppingCart.quantity));
        if(order.shoppingCart.packages.endsWith("10米")){
            vh.tv_package.setText("10米");
        }else if(order.shoppingCart.packages.endsWith("1盘")){
            vh.tv_package.setText("100米/盘");
        }else{
            vh.tv_package.setText(order.shoppingCart.packages+"米"+"/"+"轴");
        }
        String unit_money=String.valueOf(list.get(position).shoppingCart.unitPrice);
        vh.Tview_myOrder_price.setText(unit_money);
        vh.Tview_myOrder_money.setText(String.valueOf(list.get(position).getMoney()));
        //设置图片
        ArrayList<String> imgs=list.get(position).getShoppingCart().getProduct().getProductImages();
        //加载图片
        String url=null;
        if(!imgs.isEmpty()) {
            url=Url.url(imgs.get(0));
        }
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.background)
                .error(R.mipmap.loading_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(vh.image_myOrder);


        //未付款未取消订单
        if(list.get(position).tag==1 && list.get(position).cancleOrNot==0){
            //未取消订单
            vh.Tview_myOrder_stage.setText("等待付款");
            vh.Tview_myOrder_stage.setTextColor(context.getResources().getColor(R.color.colorRed));
            vh.Tview_myOrder_pay.setText("需付款");

            //未取消订单时取消订单
            vh.btn_order_cancle.setVisibility(View.VISIBLE);
            vh.btn_order_pay.setVisibility(View.VISIBLE);
            vh.Image_myOrder_delete.setVisibility(View.GONE);
            vh.btn_order_confirmReceipt.setVisibility(View.GONE);
            vh.btn_order_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancleOrder(list.get(position).orderNumber,position);
                }
            });
            //去付款
            vh.btn_order_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, OrderPayActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("money",String.valueOf(order.getMoney()));
                    intent.putExtra("order_no",order.getOrderNumber());
                    Bundle bundle = new Bundle();
                    SparseArray array = new SparseArray();
                    array.put(0,order.getShoppingCart().getProduct().introduce);
                    bundle.putSparseParcelableArray("introduce",array);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }
        if(list.get(position).completeOrNot==1){ //已经完成
            vh.Tview_myOrder_stage.setVisibility(View.GONE);
            vh.Tview_myOrder_pay.setText("实付款");
            vh.btn_order_cancle.setVisibility(View.GONE);
            vh.btn_order_pay.setVisibility(View.GONE);
            vh.btn_order_confirmReceipt.setVisibility(View.GONE);
            vh.Image_myOrder_delete.setVisibility(View.VISIBLE);
            vh.Image_myOrder_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteOrder(order.orderNumber,position);
                }
            });
            vh.image_success.setVisibility(View.VISIBLE);
        }

        if(order.cancleOrNot==1){ //已quxiao
            vh.Tview_myOrder_stage.setText("已取消");
            vh.Tview_myOrder_pay.setText("实付款");
            vh.Tview_myOrder_stage.setTextColor(context.getResources().getColor(R.color.black));
            vh.Image_myOrder_delete.setVisibility(View.VISIBLE);
            vh.btn_order_cancle.setVisibility(View.GONE);
            vh.btn_order_pay.setVisibility(View.GONE);
            vh.btn_order_confirmReceipt.setVisibility(View.GONE);
            vh.Image_myOrder_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  deleteOrder(order.orderNumber,position);
                }
            });
        }

        //已经付款但是没有发货
        if(list.get(position).tag==2&&list.get(position).shipOrNot==2&&list.get(position).cancleOrNot==0){
            vh.Tview_myOrder_stage.setText("等待出库");
            vh.Tview_myOrder_pay.setText("实付款");
            vh.Tview_myOrder_stage.setTextColor(context.getResources().getColor(R.color.colorRed));
            vh.Image_myOrder_delete.setVisibility(View.GONE);
            vh.btn_order_confirmReceipt.setVisibility(View.GONE);
            vh.btn_order_pay.setVisibility(View.GONE);
            vh.btn_order_cancle.setVisibility(View.VISIBLE);
            vh.btn_order_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   cancleOrder(order.orderNumber,position);
                }
            });
        }
        //未收货
        if(order.tag==2&&order.shipOrNot==1&&order.completeOrNot==0){
            if(order.cancleOrNot==0){
                vh.Tview_myOrder_stage.setText("已发货");
                vh.Tview_myOrder_stage.setTextColor(context.getResources().getColor(R.color.colorRed));

                vh.Tview_myOrder_pay.setText("实付款");
                vh.Image_myOrder_delete.setVisibility(View.GONE);
                vh.image_success.setVisibility(View.GONE);

                vh.btn_order_pay.setVisibility(View.GONE);
                vh.btn_order_cancle.setVisibility(View.VISIBLE);
                vh.btn_order_confirmReceipt.setVisibility(View.VISIBLE);
                vh.btn_order_confirmReceipt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmReceipt(order.orderNumber,position);
                    }
                });
                vh.btn_order_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancleOrder(order.orderNumber,position);
                    }
                });
            }

        }

//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context.getApplicationContext(), OrderDetailActivity.class);
//                intent.putExtra("order",list.get(position));
//                intent.putExtra("position",position);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }




    /**
     * 取消订单
     */
    public  void cancleOrder(String orderNum,int position){
        position_tag=position;
        String url = Url.url("/androidOrder/cancleOrder");
        Map<String,String> map=new HashMap<>();
        map.put("orderNum",orderNum);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonCancleOrderListener,errorOrderListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 删除订单
     */
    public void deleteOrder(String orderNum,int position){
       position_tag=position;
        String url = Url.url("/androidOrder/deleteOrder");
        Map<String,String> map=new HashMap<>();
        map.put("orderNum",orderNum);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonCancleOrderListener,errorOrderListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }
    /**
     * 确认收货
     */
    public void confirmReceipt(String orderNum,int position){
        position_tag=position;
        String url = Url.url("/androidOrder/confirmReceipt");
        Map<String,String> map=new HashMap<>();
        map.put("orderNum",orderNum);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonCancleOrderListener,errorOrderListener,map);
        MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
    }

    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonCancleOrderListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            String msg= null;
            try {
                msg = jsonObject.getString("msg");
                if(msg.equals("cancle")){
                    if(position_tag!=-1){
                        list.get(position_tag).cancleOrNot=1;
                        notifyDataSetChanged();
                    }

                    Toast.makeText(context,"取消订单成功",Toast.LENGTH_SHORT).show();
                }else if(msg.equals("delete")){
                    if(position_tag!=-1){
                        deleteItems(position_tag);
                    }

                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                }else if(msg.equals("confirm")) {
                    if(position_tag!=-1){
                        list.get(position_tag).completeOrNot=1;
                        notifyDataSetChanged();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorOrderListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(context, "请求数据失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };


    public static class MyOrderViewHolder{
        View rl_myorder_item;
        private LinearLayout ll_order_bottom;
        private TextView Tview_myOrder_type;
        private ImageView image_myOrder;
        private ImageView Image_myOrder_delete,image_success;
        private TextView Tview_myOrder_stage;
        private TextView Tview_myOrder_introduce;
        private TextView Tview_myOrder_price;
        private TextView Tview_myOrder_money,Tview_myOrder_pay;
        private TextView tv_buy_num,tv_package;
        private Button btn_order_cancle,btn_order_pay,btn_order_confirmReceipt;

    }

}

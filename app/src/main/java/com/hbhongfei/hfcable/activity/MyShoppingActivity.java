package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter_myShopping;
import com.hbhongfei.hfcable.entity.CablesInfo;
import com.hbhongfei.hfcable.entity.TypeInfo;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.NormalPostRequestArray;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyShoppingActivity extends AppCompatActivity implements MyAdapter_myShopping.CheckInterface,
        MyAdapter_myShopping.ModifyCountInterface, MyAdapter_myShopping.GroupEdtorListener ,View.OnClickListener{
    private TextView  tvTotalPrice,tvDelete,tvGoToPay;
    private LinearLayout llInfo,llCart,cart_empty;
    private RelativeLayout llShar;
    private ExpandableListView exListView;
    private CheckBox allChekbox;

    private Context context;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private MyAdapter_myShopping selva;
    private List<TypeInfo> groups = new ArrayList<TypeInfo>();// 组元素数据列表
    private Map<String, List<CablesInfo>> children = new HashMap<String, List<CablesInfo>>();// 子元素数据列表
    private int flag = 0;
    private String S_phoneNumber;
    private static final String USER = LoginConnection.USER;
    private Dialog dialog;
    private ArrayList<String> proIds = new ArrayList<>();
    private ArrayList<Map<String,Object>> proInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping);
        context = this;
        initView();
//        initDatas();
        setOnClick();

    }
    /**
     * 初始化界面
     */
    private void initView(){
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvGoToPay = (TextView) findViewById(R.id.tv_go_to_pay);

        llShar = (RelativeLayout) findViewById(R.id.ll_shar);
        llInfo = (LinearLayout) findViewById(R.id.ll_info);
        llCart = (LinearLayout) findViewById(R.id.ll_cart);
        cart_empty = (LinearLayout) findViewById(R.id.layout_cart_empty);

        exListView = (ExpandableListView) findViewById(R.id.exListView);

        allChekbox = (CheckBox) findViewById(R.id.all_chekbox);
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
    }
    /**
     * 设置点击事件
     */
    private void setOnClick(){
        allChekbox.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvGoToPay.setOnClickListener(this);
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
     * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
     */
    private void initDatas() {
        /*for (int i = 0; i < 3; i++) {
            groups.add(new TypeInfo(i + "", "弘飞电缆的第" + (i + 1) + "个种类"));
            List<CablesInfo> cablesInfos = new ArrayList<CablesInfo>();
            for (int j = 0; j <= i; j++) {
                int[] img = {R.mipmap.main_img1, R.mipmap.main_img2, R.mipmap.main_img3, R.mipmap.main_img4, R.mipmap.main_img1, R.mipmap.main_img2};
                cablesInfos.add(new CablesInfo(j + "", "电缆"+(j+1), groups.get(i)
                        .getName() + "的第" + (j + 1) + "个电缆","红色","大号", 12.00 + new Random().nextInt(23), new Random().nextInt(5) + 1, img[i * j]));
            }
            children.put(groups.get(i).getId(), cablesInfos);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
        }*/

        connInter();

    }



    /**
     * 连接服务
     */
    public void connInter(){
        dialog = new Dialog(this.context);
        dialog.showDialog("正在加载中...");
        Map<String,String> params =new HashMap<>();
        params.put("phoneNumber", S_phoneNumber);
        String url = Url.url("/androidShoppingCart/list");
        System.out.println(url);
        RequestQueue mQueue = Volley.newRequestQueue(this.context);

        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,jsonObjectListener,errorListener, params);
        mQueue.add(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                JSONArray shoppingCarts = jsonObject.getJSONArray("shoppingCarts");
                if (shoppingCarts.length()!=0){
                    for (int i=0;i<shoppingCarts.length();i++){
                        JSONObject shoppingCart= (JSONObject) shoppingCarts.opt(i);//shoppingCarts的第一组
                        /****************产品种类*******************/
                        JSONObject type = shoppingCart.getJSONObject("type");
                        groups.add(new TypeInfo(i + "", type.getString("typeName")));//添加组
                        /****************产品信息*******************/
                        JSONArray products = shoppingCart.getJSONArray("product");
                        List<CablesInfo> cablesInfos = new ArrayList<>();
                        for(int j=0;j<products.length();j++){
                        /**************购物车信息*****************/
                            JSONObject product= (JSONObject) products.opt(j);
                            int quantity = product.getInt("quantity");
                            String color = product.getString("color");
                            String specifications = product.getString("specifications");
                            String id = product.getString("id");

                        /**************产品信息*****************/
                            JSONObject productInfo = product.getJSONObject("product");
                            String productName = productInfo.getString("prodectName");//名
                            String detail = productInfo.getString("detail");//简介
                            Double price = productInfo.getDouble("price");//价格
                            JSONArray images = productInfo.getJSONArray("productImages");
                            String image = (String) images.get(0);
                            if (image==null){
                                image = "/images/b3043ec169634ba5a7d2631200723a67.jpeg";
                            }
                            cablesInfos.add(new CablesInfo(id, productName, detail,color,specifications,price, quantity,image));
                        }
                        children.put(groups.get(i).getId(), cablesInfos);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
                    }
                    selva = new MyAdapter_myShopping(groups, children, MyShoppingActivity.this);
                    selva.setCheckInterface( MyShoppingActivity.this);// 关键步骤1,设置复选框接口
                    selva.setModifyCountInterface( MyShoppingActivity.this);// 关键步骤2,设置数量增减接口
                    selva.setmListener( MyShoppingActivity.this);//设置监听器接口
                    exListView.setAdapter(selva);
                    for (int i = 0; i < selva.getGroupCount(); i++) {
                        exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                    }
                }else{
                    clearCart();
                }

                dialog.cancle();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();
            }
        }
    };

    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(context,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            dialog.cancle();
        }
    };





    /*private void initEvents() {
        selva = new MyAdapter_myShopping(groups, children, this);
        selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
        selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
        selva.setmListener(this);//设置监听器接口
        exListView.setAdapter(selva);

        for (int i = 0; i < selva.getGroupCount(); i++) {
            exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        groups.clear();
        children.clear();
        initDatas();
//        setCartNum();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selva=null;
        groups.clear();
        children.clear();
        totalPrice=0;
        totalCount=0;
    }
    /**
     * 设置标题购物车产品数量
     */
    private void setCartNum() {
        int count = 0;
        /*for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(allChekbox.isChecked());
            TypeInfo group = groups.get(i);
            List<CablesInfo> childs = children.get(group.getId());
            for (CablesInfo cablesInfo : childs) {
                count += 1;
            }
        }*/

        //购物车已清空
        if(count==0){
//            clearCart();
        } else{
        }
    }

    /**
     * 清除购物车
     */
    private void clearCart() {
        llCart.setVisibility(View.GONE);
        cart_empty.setVisibility(View.VISIBLE);
    }

    /**
     * 是否选中
     * @return
     */
    private boolean isAllCheck() {

        for (TypeInfo group : groups) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        TypeInfo group = groups.get(groupPosition);
        List<CablesInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            allChekbox.setChecked(true);
        else
            allChekbox.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        TypeInfo group = groups.get(groupPosition);
        List<CablesInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck()) {
            allChekbox.setChecked(true);// 全选
        } else {
            allChekbox.setChecked(false);// 反选
        }
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void groupEdit(int groupPosition) {
        groups.get(groupPosition).setEdtor(true);
        selva.notifyDataSetChanged();
    }

    /**
     * 连接服务
     */
    public void updateQuantity(String proId,String quantity){
        dialog = new Dialog(this.context);
        dialog.showDialog("正在修改中...");
        Map<String,String> params =new HashMap<>();
        params.put("id",proId);
        params.put("quantity",quantity);
        String url = Url.url("/androidShoppingCart/quantity");
        System.out.println(url);
        RequestQueue mQueue = Volley.newRequestQueue(this.context);

        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,updateListener,errorListener, params);
        mQueue.add(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> updateListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("quantity");
                if (s.equals("success")){
                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }else if(s.equals("filed")){
                    Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();

            }
        }
    };



    /**
     *  增加
     * @param groupPosition 组元素位置
     * @param childPosition 子元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        CablesInfo product = (CablesInfo) selva.getChild(groupPosition,childPosition);
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        //联网修改
        updateQuantity(product.getId(),String.valueOf(currentCount));
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    /**
     * 减少
     * @param groupPosition 组元素位置
     * @param childPosition 子元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        CablesInfo product = (CablesInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        if (currentCount == 1)
            return;
        currentCount--;
        product.setCount(currentCount);
        //联网修改
        updateQuantity(product.getId(),String.valueOf(currentCount));
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        TypeInfo group = groups.get(groupPosition);
        List<CablesInfo> childs = children.get(group.getId());
        for (int i=0;i<childs.size();i++){
            delete(childs.get(i).getId());
            children.get(groups.get(groupPosition).getId()).remove(childPosition);
        }
        if (childs.size() == 0) {
            groups.remove(groupPosition);
        }
        if (groups.size()==0){
            clearCart();
        }
        selva.notifyDataSetChanged();
        calculate();
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++) {
            TypeInfo group = groups.get(i);
            List<CablesInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                Map<String,Object> map = new HashMap<>();
                CablesInfo cable = childs.get(j);
                if (cable.isChoosed()) {
                    totalCount++;
                    map.put("product_name",cable.getName());
                    map.put("introduce",cable.getIntroduce());
                    map.put("product_price",cable.getPrice());
                    map.put("product_num",cable.getCount());
                    proInfos.add(map);
                    totalPrice += cable.getPrice() * cable.getCount();
                }
            }
        }
        tvTotalPrice.setText("￥" + totalPrice);
        tvGoToPay.setText("去支付(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if(totalCount==0){
//            setCartNum();
        } else{
        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog alert;
        switch (v.getId()) {
            //全部选或者反选
            case R.id.all_chekbox:
                doCheckAll();
                break;
            //删除电缆
            case R.id.tv_delete:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定删除？")
                        .setContentText("删除将不可恢复")
                        .setConfirmText("删除")
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                doDelete();
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;
            case R.id.tv_go_to_pay:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("操作提示")
                        .setContentText("总计:\n" + totalCount + "种商品\n" + totalPrice + "元")
                        .setConfirmText("支付")
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(MyShoppingActivity.this,ConfirmOrderActivity.class);
                                intent.putExtra("proInfos",proInfos);
                                intent.putExtra("price",totalPrice);
                                startActivity(intent);
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                break;

        }
    }
    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(allChekbox.isChecked());
            TypeInfo group = groups.get(i);
            List<CablesInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(allChekbox.isChecked());
            }
        }
        selva.notifyDataSetChanged();
        calculate();
    }
    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete() {
        List<TypeInfo> toBeDeleteGroups = new ArrayList<>();// 待删除的组元素列表
        for (int i = 0; i < groups.size(); i++) {
            TypeInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<CablesInfo> toBeDeleteProducts = new ArrayList<>();// 待删除的子元素列表
            List<CablesInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                    //链接网络进行删除
                    delete(childs.get(j).getId());
                }
            }
            childs.removeAll(toBeDeleteProducts);
        }
        groups.removeAll(toBeDeleteGroups);
        //记得重新设置购物车
//        setCartNum();
        if (groups.size()==0){
            clearCart();
        }
        selva.notifyDataSetChanged();
    }



    /**
     * 连接服务
     */
    public void delete(String id){
        dialog = new Dialog(this.context);
        dialog.showDialog("正在删除中...");
        Map<String,String> params =new HashMap<>();
        params.put("id", id);
        String url = Url.url("/androidShoppingCart/delete");
        System.out.println(url);
        RequestQueue mQueue = Volley.newRequestQueue(this.context);

        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,deleteListener,errorListener, params);
        mQueue.add(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> deleteListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("delete");
                if (s.equals("success")){
                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }else if(s.equals("filed")){
                    Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();

            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_shopping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (flag == 0) {
            llInfo.setVisibility(View.GONE);
            tvGoToPay.setVisibility(View.GONE);
            llShar.setVisibility(View.VISIBLE);
            item.setTitle("完成");
        } else if (flag == 1) {
            llInfo.setVisibility(View.VISIBLE);
            tvGoToPay.setVisibility(View.VISIBLE);
            llShar.setVisibility(View.GONE);
            item.setTitle("删除");
        }
        flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
        return super.onOptionsItemSelected(item);
    }
}

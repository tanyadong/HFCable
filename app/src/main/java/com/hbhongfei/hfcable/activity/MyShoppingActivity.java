package com.hbhongfei.hfcable.activity;

import android.content.Context;
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

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter_myShopping;
import com.hbhongfei.hfcable.pojo.CablesInfo;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.pojo.TypeInfo;
import com.hbhongfei.hfcable.pojo.TypeTwo;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NetUtils;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyShoppingActivity extends AppCompatActivity implements MyAdapter_myShopping.CheckInterface,
        MyAdapter_myShopping.ModifyCountInterface, MyAdapter_myShopping.GroupEdtorListener, View.OnClickListener, IErrorOnclick {
    private TextView tvTotalPrice, tvDelete, tvGoToPay;
    private LinearLayout llInfo, llCart, cart_empty, noInternet;
    private RelativeLayout llShar;
    private ExpandableListView exListView;
    private CheckBox allChekbox;
    private Context context;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private MyAdapter_myShopping selva;
    private List<TypeInfo> groups = new ArrayList<TypeInfo>();// 组元素数据列表
    private Map<String, List<Product>> child = new HashMap<>();
    private Map<String, List<CablesInfo>> children = new HashMap<String, List<CablesInfo>>();// 子元素数据列表
    private int flag = 0;
    private String S_phoneNumber;
    private static final String USER = LoginConnection.USER;
    private Dialog dialog;
    private ArrayList<String> proIds = new ArrayList<>();
    private ArrayList<Map<String, Object>> proInfos;
    private Map<String, String> packageMap;
    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping);
        context = this;
        initView();
        setOnClick();
    }

    /**
     * 初始化界面
     */
    private void initView() {
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
        S_phoneNumber = spf.getString("phoneNumber", null);

        noInternet = (LinearLayout) findViewById(R.id.no_internet_my_shopping);
    }

    /**
     * 设置点击事件
     */
    private void setOnClick() {
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
        connInter();
    }


    /**
     * 购物车连接服务
     */
    public void connInter() {
        dialog = new Dialog(this.context);
        dialog.showDialog("正在加载中...");
        Map<String, String> params = new HashMap<>();
        params.put("phoneNumber", S_phoneNumber);
        String url = Url.url("/androidShoppingCart/list");
        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url, jsonObjectListener, errorListener, params);
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                JSONArray shoppingCarts = jsonObject.getJSONArray("shoppingCarts");
                if (shoppingCarts.length() != 0) {

                    int count = shoppingCarts.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject shoppingCart = (JSONObject) shoppingCarts.opt(i);//shoppingCarts的第一组
                        /****************产品种类*******************/
                        JSONObject type = shoppingCart.getJSONObject("type");
                        groups.add(new TypeInfo(i + "", type.getString("typeTwoName")));//添加组
                        /****************产品信息*******************/
                        JSONArray products = shoppingCart.getJSONArray("product");
                        JSONArray productImages = shoppingCart.getJSONArray("productImage");
                        List<CablesInfo> cablesInfos = new ArrayList<>();
                        List<Product> list_product = new ArrayList<>();
                        int pro_count=products.length();
                        for (int j = 0; j < pro_count; j++) {
                            /**************购物车信息*****************/
                            JSONObject product = (JSONObject) products.opt(j);
                            JSONArray images = productImages.getJSONArray(j);
                            int quantity = product.getInt("quantity");
                            String color = product.getString("color");
                            String specifications = product.getString("packages");//包装
                            String id = product.getString("id");
                            Double unit_price = product.getDouble("unitPrice");

                            /**************产品信息*****************/
                            JSONObject productInfo = product.getJSONObject("product");

                            //typeTwo  所有的产品信息
                            JSONObject jsonObject2 = productInfo.getJSONObject("typeTwo");
                            TypeTwo typeTwo = new TypeTwo();
                            typeTwo.setTypeTwoName(jsonObject2.getString("typeTwoName"));
                            //产品

                            Product product1 = new Product();
                            String productName = productInfo.getString("introduce");//名
                            String voltage = productInfo.getString("voltage");//电压
                            product1.setTypeTwo(typeTwo);
                            product1.setId(productInfo.getString("id"));
                            product1.setPrice(productInfo.getDouble("price"));
                            product1.setApplicationRange(productInfo.getString("applicationRange"));
                            product1.introduce = productName;
                            product1.setConductorMaterial(productInfo.getString("conductorMaterial"));
                            product1.setCoreNumber(productInfo.getString("coreNumber"));
                            product1.setCrossSection(productInfo.getString("crossSection"));
                            product1.setImplementationStandards(productInfo.getString("implementationStandards"));
                            product1.setDiameterLimit(productInfo.getString("diameterLimit"));
                            product1.setOutsideDiameter(productInfo.getString("outsideDiameter"));
                            product1.setSheathMaterial(productInfo.getString("sheathMaterial"));
                            product1.setVoltage(voltage);
                            product1.setReferenceWeight(productInfo.getString("referenceWeight"));
                            product1.setPurpose(productInfo.getString("purpose"));

                            JSONArray jsonArray1 = productInfo.getJSONArray("productImages");
                            //有图片时加入到产品图片集合
                            if (jsonArray1.length() > 0) {
                                ArrayList<String> list1 = new ArrayList<>();
                                int img_count=jsonArray1.length();
                                for (int k = 0; k < img_count; k++) {
                                    list1.add((String) jsonArray1.get(k));
                                }
                                product1.setProductImages(list1);
                            }
                            JSONObject image = (JSONObject) images.opt(0);
                            String img;
                            if (image != null) {
                                img = (String) image.getString("image");//存在问题
                            } else {
                                img = "/images/b3043ec169634ba5a7d2631200723a67.jpeg";
                            }
                            list_product.add(product1);
                            cablesInfos.add(new CablesInfo(id, productName, voltage, color, specifications, unit_price, quantity, img));
                        }
                        child.put(groups.get(i).getId(), list_product);
                        children.put(groups.get(i).getId(), cablesInfos);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
                    }
                    selva = new MyAdapter_myShopping(groups, children, MyShoppingActivity.this, packageMap);
                    selva.setCheckInterface(MyShoppingActivity.this);// 关键步骤1,设置复选框接口
                    selva.setModifyCountInterface(MyShoppingActivity.this);// 关键步骤2,设置数量增减接口
                    selva.setmListener(MyShoppingActivity.this);//设置监听器接口
                    exListView.setAdapter(selva);
                    int group_count=selva.getGroupCount();
                    for (int i = 0; i < group_count; i++) {
                        exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
                    }
                    exListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            Intent intent = new Intent(MyShoppingActivity.this, ProdectInfoActivity.class);
                            intent.putExtra("product", child.get(groups.get(groupPosition).getId()).get(childPosition));
                            startActivity(intent);
                            return true;
                        }
                    });
                } else {
                    clearCart();
                }
                shaftConnInter();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();
            }
        }
    };

    /**
     * 添加数据失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dialog.cancle();
            if (volleyError instanceof NoConnectionError) {
                Error.toSetting(noInternet, R.mipmap.internet_no, "没有网络哦", "点击设置", MyShoppingActivity.this);
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
    };

    /**
     * 获取包装方式
     */
    public void shaftConnInter() {
        String url = Url.url("/androidShaft/list");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                shaftjsonObjectListener, errorListener);
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> shaftjsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            packageMap = new HashMap<>();
            try {
                jsonArray = jsonObject.optJSONArray("list");
                if (jsonArray!=null){
                    int shaft_count=jsonArray.length();
                    for (int i = 0; i < shaft_count; i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.getJSONObject(i);
                        String shaftName = jsonObject1.getString("shaftName");
                        String shaftPrice = jsonObject1.getString("shaftPrice");
                        packageMap.put(shaftName, shaftPrice);
                    }
                    dialog.cancle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        allChekbox.setChecked(false);
        groups.clear();
        children.clear();
        totalCount = 0;
        totalPrice = 0.00;
        tvTotalPrice.setText("￥" + df.format(totalPrice));
        tvGoToPay.setText("去支付(" + totalCount + ")");
        initDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selva = null;
        groups.clear();
        children.clear();
        totalPrice = 0;
        totalCount = 0;
    }

    /**
     * 设置标题购物车产品数量
     */
    private void setCartNum() {
        int count = 0;
        //购物车已清空
        if (count == 0) {
        } else {
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
     *
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
        int child_count=childs.size();
        for (int i = 0; i < child_count; i++) {
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
        int child_count=childs.size();
        for (int i = 0; i < child_count; i++) {
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
     * 修改连接服务
     */
    public void updateQuantity(String proId, String quantity) {
        dialog = new Dialog(this.context);
        dialog.showDialog("正在修改中...");
        Map<String, String> params = new HashMap<>();
        params.put("id", proId);
        params.put("quantity", quantity);
        String url = Url.url("/androidShoppingCart/quantity");
        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url, updateListener, errorListener, params);
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> updateListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("quantity");
                if (s.equals("success")) {
                    Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                } else if (s.equals("filed")) {
                    Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();

            }
        }
    };
    private Response.ErrorListener updateErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(context, "链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            dialog.cancle();

        }
    };


    /**
     * 增加
     *
     * @param groupPosition 组元素位置
     * @param childPosition 子元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        CablesInfo product = (CablesInfo) selva.getChild(groupPosition, childPosition);
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        //联网修改
        updateQuantity(product.getId(), String.valueOf(currentCount));
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    /**
     * 减少
     *
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
        updateQuantity(product.getId(), String.valueOf(currentCount));
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        TypeInfo group = groups.get(groupPosition);
        List<CablesInfo> childs = children.get(group.getId());
        int child_count=childs.size();
        for (int i = 0; i < child_count; i++) {
            delete(childs.get(i).getId());
            children.get(groups.get(groupPosition).getId()).remove(childPosition);
        }
        if (childs.size() == 0) {
            groups.remove(groupPosition);
        }
        if (groups.size() == 0) {
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
        proInfos = new ArrayList<>();
        int groups_count=groups.size();
        for (int i = 0; i < groups_count; i++) {
            TypeInfo group = groups.get(i);
            List<CablesInfo> childs = children.get(group.getId());
            int child_count=childs.size();
            for (int j = 0; j < child_count; j++) {
                Map<String, Object> map = new HashMap<>();
                CablesInfo cable = childs.get(j);
                if (cable.isChoosed()) {
                    totalCount++;
                    map.put("id", cable.getId());
                    map.put("product_name", cable.getName());
                    map.put("introduce", cable.getIntroduce());
                    map.put("color", cable.getColor());
                    map.put("product_price", cable.getPrice());
                    map.put("product_num", cable.getCount());
                    map.put("product_package", cable.getSpecifications());
                    map.put("product_iamge", cable.getGoodsImg());
                    proInfos.add(map);
                    if (cable.getSpecifications().equals("10米")) {
                        //10米价格增长
                        totalPrice += cable.getPrice() * cable.getCount();//价格
                    } else if (cable.getSpecifications().equals("1盘")) {
                        totalPrice += cable.getPrice() * cable.getCount();
                    } else {
                        String s = packageMap.get(cable.getSpecifications());
                        totalPrice += cable.getPrice() * cable.getCount();
                    }
                }
            }
        }
        tvTotalPrice.setText("￥" + df.format(totalPrice));
        tvGoToPay.setText("去支付(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if (totalCount == 0) {
        } else {
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
                        .setContentText("总计:\n" + totalCount + "种商品\n" + df.format(totalPrice) + "元")
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
                                Intent intent = new Intent(MyShoppingActivity.this, ConfirmOrderActivity.class);
                                intent.putExtra("proInfos", proInfos);
                                intent.putExtra("map", (Serializable) packageMap);
                                intent.putExtra("price", totalPrice);
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
        int group_count=groups.size();
        for (int i = 0; i <group_count; i++) {
            groups.get(i).setChoosed(allChekbox.isChecked());
            TypeInfo group = groups.get(i);
            List<CablesInfo> childs = children.get(group.getId());
            int child_count=childs.size();
            for (int j = 0; j < child_count; j++) {
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
        int group_count=groups.size();
        for (int i = 0; i < group_count; i++) {
            TypeInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<CablesInfo> toBeDeleteProducts = new ArrayList<>();// 待删除的子元素列表
            List<CablesInfo> childs = children.get(group.getId());
            int child_count=childs.size();
            for (int j = 0; j < child_count; j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                    //链接网络进行删除
                    delete(childs.get(j).getId());
                }
            }
            childs.removeAll(toBeDeleteProducts);
        }
        groups.removeAll(toBeDeleteGroups);
        if (groups.size() == 0) {
            clearCart();
        }
        selva.notifyDataSetChanged();
    }


    /**
     * 连接服务
     */
    public void delete(String id) {
        dialog = new Dialog(this.context);
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        String url = Url.url("/androidShoppingCart/delete");
        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url, deleteListener, errorListener, params);
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> deleteListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("delete");
                if (s.equals("success")) {
                    Toast.makeText(MyShoppingActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                } else if (s.equals("filed")) {
                    Toast.makeText(MyShoppingActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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


    @Override
    public void errorClick() {
        NetUtils.openSetting(this);
    }
}

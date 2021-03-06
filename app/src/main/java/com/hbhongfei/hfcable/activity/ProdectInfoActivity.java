package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.ImagePaperAdapter;
import com.hbhongfei.hfcable.adapter.SkuAdapter;
import com.hbhongfei.hfcable.pojo.Bean;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.pojo.SkuItme;
import com.hbhongfei.hfcable.util.CallTel;
import com.hbhongfei.hfcable.util.DataUtil;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NetUtils;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.ToastUtil;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProdectInfoActivity extends AppCompatActivity implements View.OnClickListener, PopupWindow.OnDismissListener {
    private LayoutInflater inflater;
    private ImageView img1;
    private ViewPager mviewPager;
    private LinearLayout prodectList_LLayout_phone, prodectList_LLayout_collect, prodectList_LLayout_shoppingCat, Layout_add;
    private TextView prodectInto_simpleDeclaration, prodectInfo_price, prodectInfo_package, prodectInfo_model, prodectInfo_coreType,
            prodectInfo_last_price;
    private TextView prodectInfo_voltage, prodectInfo_crossSection, prodectInfo_coreNumber,
            prodectInfo_purpose, prodectInfo_applicationRange, prodectInfo_outsideDiameter,
            prodectInfo_diameterLimit, prodectInfo_implementationStandards, prodectInfo_conductorMaterial,
            prodectInfo_sheathMaterial, prodectInfo_referenceWeight;
    private TextView prodect_addCart;
    private RelativeLayout selectSpec_layout;
    LinearLayout layout;
    private Product product;
    //    popwindow弹框
    private TextView prodectInfo_s, prodectInfo_s1, prodectInfo_s2;
    String color;//
    String packages;//包装方式
    List<SkuItme> mList;// sku数据

    List<Bean> mColorList;// 颜色列表
    List<Bean> mPackageList;// 尺码列表

    GridView gvColor;// 颜色
    GridView gvPackage;// 规格
    SkuAdapter skuColorAdapter;// 颜色适配器
    SkuAdapter skuPackageAdapter;// 规格适配器
    TextView tvSkuName;// 显示sku
    TextView tv_name;// 显示库存
    ImageView pop_del, iv_pic, prodectList_img_collect;//关闭图片
    Button btn_sure;//确定按钮
    TextView pop_add, pop_reduce, pop_num;
    TextView prodectList_tview_collect;
    private final int ADDORREDUCE = 1;
    boolean isAddCart = false;
    int color_position = 0;
    int spec_position = 0;
    String S_phoneNumber;
    Double unitPrice;
    private static final String USER = LoginConnection.USER;
    private Dialog dialog;
    private List<String> package_list;
    private List<Double> package_list_num;
    private List<String> package_list_price;
    private Double D_price, D_beforePrice, D_tagPrice;
    private int Tag = 0;
    private Map<String, String> price_map;
    /**
     * 弹出商品订单信息详情
     */
    private PopupWindow popWindow;
    /**
     * 用于小圆点图片
     */
    private List<ImageView> dotViewList;
    /**
     * 用于存放轮播效果图片
     */
    private List<ImageView> list;
    //    添加小圆点控件，
    private LinearLayout dotLayout;

    private int currentItem = 0;//当前页面

    boolean isAutoPlay = true;//是否自动轮播
    /**
     * ViewPager当前显示页的下标
     */
    int position1 = 0;
    private ScheduledExecutorService scheduledExecutorService;
    Intent intent;
    String tag;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                mviewPager.setCurrentItem(currentItem);
            } else if (msg.what == 1) {
                price_map = (Map<String, String>) msg.obj;
            }
        }

    };

    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodect_info);
        toolBar();
        initVIew();
        setDate();
        click();
        if (isAutoPlay) {
            startPlay();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * 展示toolbar
     */
    private void toolBar() {
        tag = getIntent().getStringExtra("tag");
        Toolbar toolbar = (Toolbar) findViewById(R.id.product_info_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void initVIew() {
        dialog = new Dialog(this);
        inflater = LayoutInflater.from(this);
        mviewPager = (ViewPager) findViewById(R.id.myviewPager);
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        img1 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
        prodectList_LLayout_phone = (LinearLayout) findViewById(R.id.prodectList_LLayout_phone);
        prodectList_LLayout_collect = (LinearLayout) findViewById(R.id.prodectList_LLayout_collect);
        prodectList_LLayout_shoppingCat = (LinearLayout) findViewById(R.id.prodectList_LLayout_shoppingCat);
        prodect_addCart = (TextView) findViewById(R.id.prodect_addCart);
        //产品信息
        prodectInfo_package = (TextView) findViewById(R.id.prodectInfo_specifications);

        prodectInto_simpleDeclaration = (TextView) findViewById(R.id.prodectInto_simpleDeclaration_tview);
        prodectInfo_price = (TextView) findViewById(R.id.prodectInfo_price_textView);
        prodectInfo_model = (TextView) findViewById(R.id.prodectInfo_model_textview);
        prodectInfo_voltage = (TextView) findViewById(R.id.prodectInfo_voltage_textView);
        prodectInfo_crossSection = (TextView) findViewById(R.id.prodectInfo_crossSection_textView);
        prodectInfo_coreNumber = (TextView) findViewById(R.id.prodectInfo_coreNumber_textView);
        prodectInfo_purpose = (TextView) findViewById(R.id.prodectInfo_purpose_textView);
        prodectInfo_applicationRange = (TextView) findViewById(R.id.prodectInfo_applicationRange_textView);
        prodectInfo_outsideDiameter = (TextView) findViewById(R.id.prodectInfo_outsideDiameter_textView);
        prodectInfo_diameterLimit = (TextView) findViewById(R.id.prodectInfo_diameterLimit_textView);
        prodectInfo_implementationStandards = (TextView) findViewById(R.id.prodectInfo_implementationStandards_textView);
        prodectInfo_conductorMaterial = (TextView) findViewById(R.id.prodectInfo_conductorMaterial_textView);
        prodectInfo_sheathMaterial = (TextView) findViewById(R.id.prodectInfo_sheathMaterial_textView);
        prodectInfo_referenceWeight = (TextView) findViewById(R.id.prodectInfo_referenceWeight_textView);


        selectSpec_layout = (RelativeLayout) findViewById(R.id.selectSpec_layout);//选择颜色规格

        prodectList_img_collect = (ImageView) findViewById(R.id.prodectList_img_collect);
        prodectList_tview_collect = (TextView) findViewById(R.id.prodectList_tview_collect);
        //选择规格和颜色
        prodectInfo_s = (TextView) findViewById(R.id.prodectInfo_s);
        prodectInfo_s1 = (TextView) findViewById(R.id.prodectInfo_s1);
        prodectInfo_s2 = (TextView) findViewById(R.id.prodectInfo_s2);
    }

    /**
     * 设置数据
     */
    public void setDate() {
        SharedPreferences spf = getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber", "");
        list = new ArrayList<ImageView>();
        dotViewList = new ArrayList<ImageView>();
        dotLayout.removeAllViews();
        //获取产品列表的详细产品信息
        intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        //规格即名称
        prodectInto_simpleDeclaration.setText(product.introduce);
        //价格
        D_beforePrice = product.getPrice();
        prodectInfo_price.setText(String.valueOf(product.getPrice()));
        //型号
        prodectInfo_model.setText(product.getTypeTwo().getTypeTwoName());
        //电压
        prodectInfo_voltage.setText(product.getVoltage());
        prodectInfo_crossSection.setText(product.getCrossSection());
        prodectInfo_coreNumber.setText(product.getCoreNumber());
        prodectInfo_purpose.setText(product.getPurpose());
        prodectInfo_applicationRange.setText(product.getApplicationRange());
        prodectInfo_outsideDiameter.setText(product.getOutsideDiameter());
        prodectInfo_diameterLimit.setText(product.getDiameterLimit());
        prodectInfo_implementationStandards.setText(product.getImplementationStandards());
        prodectInfo_conductorMaterial.setText(product.getConductorMaterial());
        prodectInfo_sheathMaterial.setText(product.getSheathMaterial());
        prodectInfo_referenceWeight.setText(product.getReferenceWeight());

        //产品图片
        if (product.getProductImages().size() != 0) {
            for (String s : product.getProductImages()) {
                //获取图片并显示
                String url = Url.url(s);
                img1 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
                Glide.with(this)
                        .load(url)
                        .placeholder(R.mipmap.background)
                        .error(R.mipmap.loading_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img1);
                list.add(img1);
                //给图片添加点击事件。查看大图
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        // 跳到查看大图界面
                        Intent intent = new Intent(ProdectInfoActivity.this,
                                ShowBigPictrue.class);
                        intent.putExtra("position", position1);
                        intent.putStringArrayListExtra("image_List", product.getProductImages());
                        startActivity(intent);
                    }
                });
            }
            setSmallDot(list);
        } else {
            img1.setImageResource(R.mipmap.loading_error);
        }
        //设置收藏
        isOrNotColl();
    }


    /**
     * 设置小圆点
     *
     * @param list
     */
    public void setSmallDot(List<ImageView> list) {
        //加入小圆点
        int indicator_count=list.size();
        for (int i = 0; i < indicator_count; i++) {
            ImageView indicator = new ImageView(this);
            if (i == 0) {
                indicator.setPadding(20, 0, 20, 0);
                indicator.setImageResource(R.mipmap.point_unpressed);
            } else {
                indicator.setPadding(20, 0, 20, 0);
                indicator.setImageResource(R.mipmap.point_pressed);
            }
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dotLayout.addView(indicator);
            //把小圆点图片存入集合,好让切换图案片的时候动态改变小圆点背景
            dotViewList.add(indicator);
        }
        ImagePaperAdapter adapter = new ImagePaperAdapter((ArrayList) list);
        mviewPager.setAdapter(adapter);
        mviewPager.setCurrentItem(0);
        mviewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
        //根据他的参数说明，第一个参数是执行的任务，第二个参数是第一次执行的间隔，第三个参数是执行任务的周期；
    }

    private void click() {
        prodectList_LLayout_phone.setOnClickListener(this);
        prodectList_LLayout_collect.setOnClickListener(this);
        prodectList_LLayout_shoppingCat.setOnClickListener(this);
        prodect_addCart.setOnClickListener(this);
        selectSpec_layout.setOnClickListener(this);
    }

    /**
     * 跳转到登录界面
     *
     * @param
     */
    private void toLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //拨打电话
            case R.id.prodectList_LLayout_phone:
                SharedPreferences settings = getSharedPreferences("SAVE_PHONE", Activity.MODE_PRIVATE);
                final String phone = settings.getString("phoneNum", "");
                //打电话对话框
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("确定要拨打电话？")
                        .setContentText(phone)
                        .setConfirmText("确认")
                        .showCancelButton(true)
                        .setCancelText("取消")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                CallTel callTel = new CallTel(ProdectInfoActivity.this);
                                callTel.call(phone);
                                sDialog.dismiss();
                            }
                        })
                        .show();
                break;
            //添加收藏
            case R.id.prodectList_LLayout_collect:
                //收藏服务
                if (!TextUtils.isEmpty(S_phoneNumber)) {
                    collectionCollect();
                } else {
                    toLogin();
                }
                break;
            //购物车
            case R.id.prodectList_LLayout_shoppingCat:
                if (!TextUtils.isEmpty(S_phoneNumber)) {
                    Intent intent = new Intent(this, MyShoppingActivity.class);
                    startActivity(intent);
                }else {
                    toLogin();
                }
                break;
            //添加购物车
            case R.id.prodect_addCart:
                if (!TextUtils.isEmpty(S_phoneNumber)) {
                    if (!TextUtils.isEmpty(color) && !TextUtils.isEmpty(packages)) {
                        //添加到购物车
                        addShoppingCartCoon();
                    } else {
                        isAddCart = true;
                        showPopwindow();
                    }
                } else {
                    toLogin();
                }
                break;
            //弹出框中的添加数量
            case R.id.pop_add:
                if (!pop_num.getText().toString().equals("750")) {
                    String num_add = Integer.valueOf(pop_num.getText().toString()) + ADDORREDUCE + "";
                    D_price += D_tagPrice;
                    prodectInfo_last_price.setText("金额:" + df.format(D_price));
                    pop_num.setText(num_add);
                } else {
                    Toast.makeText(this, "不能超过最大产品数量", Toast.LENGTH_SHORT).show();
                }
                break;
            //弹出框中的减少数量
            case R.id.pop_reduce:
                if (!pop_num.getText().toString().equals("1")) {
                    String num_reduce = Integer.valueOf(pop_num.getText().toString()) - ADDORREDUCE + "";
                    pop_num.setText(num_reduce);
                    D_price -= D_tagPrice;
                    prodectInfo_last_price.setText("金额:" + df.format(D_price));
                } else {
                    Toast.makeText(this, "购买数量不能低于1件", Toast.LENGTH_SHORT).show();
                }
                break;
            //选择规格事件
            case R.id.selectSpec_layout:
                //显示颜色和规格
                showPopwindow();
                break;
            //弹出框中的删除按钮
            case R.id.pop_del:
                showColorandSpec();
                D_tagPrice = null;
                dismiss();
                break;
            //弹出框的确定按钮
            case R.id.btn_sure:
                if (!TextUtils.isEmpty(S_phoneNumber)) {
                    if (TextUtils.isEmpty(color)) {
                        Toast.makeText(this, "亲，你还没有选择颜色哟~", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(packages)) {
                        Toast.makeText(this, "亲，你还没有选择包装方式哟~", Toast.LENGTH_SHORT).show();
                    } else {
                        showColorandSpec();
                        if (isAddCart) {
                            //添加到购物车服务
                            addShoppingCartCoon();
                            dismiss();
                            isAddCart = false;
                        }
                        dismiss();
                    }
                } else {
                    toLogin();
                }

                break;
        }
    }

    /**
     * 添加购物车服务
     */
    private void addShoppingCartCoon() {
        String url = Url.url("/androidShoppingCart/save");
        Map<String, String> map = new HashMap<>();
        map.put("productId", product.getId());
        map.put("color", color);
        map.put("packages", packages);
        map.put("quantity", pop_num.getText().toString());
        map.put("userName", S_phoneNumber);
        if (packages.equals("10米")) {
            //10米价格增长
            unitPrice = product.getPrice();  //价格
        } else if (packages.equals("1盘")) {
            unitPrice = product.getPrice() * 10;
        } else {
            String shaftPrice = price_map.get(packages);
            unitPrice = Double.parseDouble(packages) / 10 * product.getPrice() + Double.parseDouble(shaftPrice);
        }
        map.put("shoppingPrice", df.format(unitPrice).toString());
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectAddShoppingListener, errorListener, map);
        MySingleton.getInstance(this).addToRequestQueue(normalPostRequest);
    }

    /**
     * 添加收藏服务
     */
    private void collectionCollect() {
        String url = Url.url("/androidCollecton/sava");
        Map<String, String> map = new HashMap<>();
        map.put("productId", product.getId());
        map.put("userName", S_phoneNumber);
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectCollectionListener, errorListener, map);
        MySingleton.getInstance(this).addToRequestQueue(normalPostRequest);
    }
    /**
     * 添加购物车成功的监听器
     * 返回的添加购物车的状态信息
     */
    private Response.Listener<JSONObject> jsonObjectAddShoppingListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("msg");
                if (TextUtils.equals(msg, "成功")) {
                    ToastUtil.showCompletedCustomToastShortWithResId(ProdectInfoActivity.this,R.layout.view_tips,"添加购物车成功",R.mipmap.success);
                } else {
                    ToastUtil.showCompletedCustomToastShortWithResId(ProdectInfoActivity.this,R.layout.view_tips,"添加购物车失败",R.mipmap.error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };
    /**
     * s收藏成功的监听器
     * 返回的收藏状态信息
     */
    private Response.Listener<JSONObject> jsonObjectCollectionListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("msg");
                if (TextUtils.equals(msg, "收藏成功")) {
                    prodectList_img_collect.setImageResource(R.mipmap.heart_red);
                    prodectList_tview_collect.setText("已收藏");
                } else if (TextUtils.equals(msg, "取消收藏")) {
                    prodectList_img_collect.setImageResource(R.mipmap.heart);
                    prodectList_tview_collect.setText("收藏");
                } else if (TextUtils.equals(msg, "取消收藏失败")) {
                    ToastUtil.showCompletedCustomToastShortWithResId(ProdectInfoActivity.this,R.layout.view_tips,"取消收藏失败",R.mipmap.error);
                } else {
                    Toast.makeText(ProdectInfoActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    /**
     * 失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ProdectInfoActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
            dialog.cancle();
            dismiss();
        }
    };

    /**
     * 判断是否收藏
     */
    private void isOrNotColl() {
        dialog.showDialog("正在加载...");
        String url = Url.url("/androidCollecton/isCollection");
        Map<String, String> map = new HashMap<>();
        map.put("productId", product.getId());
        map.put("userName", S_phoneNumber);
        //此处有问题，无网状态下无法连接网络，直接崩溃,
        //通过先判断有无网进行解决
        if (NetUtils.isConnected(this)){
            NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectIsListener, errorListener, map);
            MySingleton.getInstance(this).addToRequestQueue(normalPostRequest);
        }else{
            dialog.cancle();
        }

    }

    /**
     * 成功的监听器
     * 返回的收藏状态信息
     */
    private Response.Listener<JSONObject> jsonObjectIsListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            String msg = null;
            try {
                msg = jsonObject.getString("msg");
                if (TextUtils.equals(msg, "isexist")) {
                    prodectList_img_collect.setImageResource(R.mipmap.heart_red);
                    prodectList_tview_collect.setText("已收藏");
                }
                dialog.cancle();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();
            }
        }
    };

    /**
     * 显示颜色规格
     */
    private void showColorandSpec() {
        if (!TextUtils.isEmpty(color) || !TextUtils.isEmpty(packages)) {
            prodectInfo_package.setVisibility(View.GONE);
            prodectInfo_s.setVisibility(View.VISIBLE);
            prodectInfo_s1.setVisibility(View.VISIBLE);
            prodectInfo_s2.setVisibility(View.VISIBLE);
            prodectInfo_s2.setText(color);
            prodectInfo_s1.setText(packages);
        } else {
            prodectInfo_package.setVisibility(View.VISIBLE);
            prodectInfo_s.setVisibility(View.INVISIBLE);
            prodectInfo_s1.setVisibility(View.INVISIBLE);
            prodectInfo_s2.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 设置popwindow
     */
    private void showPopwindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.adapter_popwindow, null);
        gvPackage = (GridView) view.findViewById(R.id.gv_specifications);
        gvColor = (GridView) view.findViewById(R.id.gv_color);
        tvSkuName = (TextView) view.findViewById(R.id.tv_sku);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        Layout_add = (LinearLayout) view.findViewById(R.id.Layout_add);
        prodectInfo_last_price = (TextView) view.findViewById(R.id.tv_price);
        prodectInfo_last_price.setText("金额:" + df.format(D_beforePrice));
        iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
        pop_del = (ImageView) view.findViewById(R.id.pop_del);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);
        pop_add = (TextView) view.findViewById(R.id.pop_add);
        pop_reduce = (TextView) view.findViewById(R.id.pop_reduce);
        pop_num = (TextView) view.findViewById(R.id.pop_num);
        Layout_add.setVisibility(View.GONE);
        tv_name.setText(product.getSpecifications());
        D_price = D_beforePrice;
        D_tagPrice = D_price;
        //设置弹窗的图片
        if (product.getProductImages().size() != 0) {
            String url = Url.url(product.getProductImages().get(0));
            //加载图片
            Glide.with(this)
                    .load(url)
                    .placeholder(R.mipmap.background)
                    .error(R.mipmap.loading_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_pic);

        } else {
            iv_pic.setImageResource(R.mipmap.loading_error);
        }
        pop_del.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        pop_add.setOnClickListener(this);
        pop_reduce.setOnClickListener(this);
        addData();
        pop_num.setText(pop_num.getText().toString());
        prodectInfo_last_price.setText(prodectInfo_last_price.getText().toString());
        //设置popowindow属性
        popWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        //设置popwindow的动画效果
        popWindow.setAnimationStyle(R.style.popWindow_anim_style);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popWindow.showAtLocation(findViewById(R.id.selectSpec_layout), Gravity.BOTTOM, 0, 0);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.getBackground().setAlpha(50);
        popWindow.update();
        //设置activity背景变暗淡
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 0.5f;
        this.getWindow().setAttributes(params);

        //点击其他地方消失
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
            }
        });
    }

    /**
     * 设置产品包装方式
     */
    public void setSpecificationsData() {
        skuPackageAdapter = new SkuAdapter(mPackageList, this);
        //设置默认选中的位置
        gvPackage.setAdapter(skuPackageAdapter);
        skuPackageAdapter.setItemClickListener(new SkuAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Bean bean, int position) {
                switch (Integer.parseInt(bean.getStates())) {
                    case 0:
                        // 清空规格
                        pop_num.setText("1");
                        mPackageList = DataUtil.clearAdapterStates(mPackageList);
                        skuPackageAdapter.notifyDataSetChanged();
                        // 清空颜色
                        mColorList = DataUtil.clearAdapterStates(mColorList);
                        skuColorAdapter.notifyDataSetChanged();
                        packages = "";
                        if (!TextUtils.isEmpty(color)) {
                            tvSkuName.setText("请选择规格");
                            mColorList = DataUtil.setAdapterStates(mColorList, color);
                            skuColorAdapter.notifyDataSetChanged();
                        } else {
                            tvSkuName.setText("请选择规格,颜色");
                        }
                        if (position != 0) {
                            Double price = D_price - Double.parseDouble(package_list_price.get(position - 1));
                            prodectInfo_last_price.setText("金额:" + df.format(D_price));
                        } else {
                            prodectInfo_last_price.setText("金额:" + df.format(D_beforePrice));
                            Layout_add.setVisibility(View.GONE);
                        }
                        break;
                    case 1:
                        // 选中规格
                        mPackageList = DataUtil.updateAdapterStates(mPackageList, "0", position);
                        skuPackageAdapter.notifyDataSetChanged();
                        spec_position = position;
                        bean.setStates("0");
                        gvPackage.setSelection(position);
                        Layout_add.setVisibility(View.VISIBLE);
                        if (position != 0) {
                            D_price = D_beforePrice;
                            Double price1 = Double.parseDouble(package_list_price.get(position - 1)) + package_list_num.get(position - 1) / 10 * D_price;
                            prodectInfo_last_price.setText("金额:" + df.format(price1));
                            String s = String.valueOf(package_list_num.get(position - 1));
                            packages = s.substring(0, s.indexOf("."));
                            Layout_add.setVisibility(View.GONE);
                        } else {
                            //包装方式为盘
                            pop_num.setText("1");
                            new SweetAlertDialog(ProdectInfoActivity.this, SweetAlertDialog.NORMAL_TYPE)
                                    .setTitleText("选择计量单位")
                                    .setConfirmText("10米")
                                    .setCancelText("1盘")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            //10米
                                            D_price = D_beforePrice;
                                            D_tagPrice = D_price;
                                            prodectInfo_last_price.setText("金额:" + df.format(D_price));
                                            packages = "10米";
                                            sweetAlertDialog.dismiss();
                                        }
                                    })
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            //1盘-100米
                                            packages = "1盘";
                                            D_price = D_beforePrice * 10;
                                            D_tagPrice = D_price;
                                            prodectInfo_last_price.setText("金额:" + df.format(D_price));
                                            sweetAlertDialog.dismiss();
                                        }
                                    }).show();
                        }
                        if (!TextUtils.isEmpty(color)) {
                            // 选择规格的同事选择颜色
                            tvSkuName.setText("规格:" + color + " " + packages);
                        } else {
                            tvSkuName.setText("请选择颜色");
                        }

                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * 通过颜色适配器设置颜色数据
     */
    public void setColorData() {
        skuColorAdapter = new SkuAdapter(mColorList, this);
        skuColorAdapter.setSelectedPosition(color_position);
        gvColor.setAdapter(skuColorAdapter);
        skuColorAdapter.setItemClickListener(new SkuAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Bean bean, int position) {
                color = bean.getName();
                switch (Integer.parseInt(bean.getStates())) {
                    case 0:
                        // 清空颜色
                        mColorList = DataUtil.clearAdapterStates(mColorList);
                        skuColorAdapter.notifyDataSetChanged();
                        color = "";
                        // 判断是否选中了规格
                        if (TextUtils.isEmpty(packages)) {
                            tvSkuName.setText("请选择规格,颜色");
                        } else {
                            tvSkuName.setText("请选择颜色");
                        }
                        break;
                    case 1:
                        // 选中颜色
                        color_position = position;
                        mColorList = DataUtil.updateAdapterStates(mColorList, "0", color_position);

                        skuColorAdapter.notifyDataSetChanged();
                        List<String> list = DataUtil.getSizeListByColor(mList, color);
                        if (!TextUtils.isEmpty(packages)) {
                            // 计算改颜色与尺码对应的库存
//                            stock = DataUtil.getStockByColorAndSize(mList,color, specifications);
                            tvSkuName.setText("规格:" + color + " " + packages);
                        } else {
                            // 根据颜色计算库存
                            tvSkuName.setText("请选择规格");
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 添加数据
     */
    public void addData() {
        mList = new ArrayList<SkuItme>();
        //颜色规格列表
        mColorList = new ArrayList<Bean>();
        mPackageList = new ArrayList<Bean>();

        package_list = new ArrayList<>();
        package_list_num = new ArrayList<>();
        package_list_price = new ArrayList<>();
        //轴的连接
        package_list.add("盘");
        shaftConnInter();
        //颜色的链接
        colorConnInter();
    }

    /**
     * 获取产品包装方式服务
     */
    public void shaftConnInter() {
        dialog.showDialog("正在加载中。。。");
        String url = Url.url("/androidShaft/list");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                jsonObjectListener, errorListener);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * 获取包装方式成功的监听
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            try {
                price_map = new HashMap();
                jsonArray = jsonObject.optJSONArray("list");
                if(jsonArray!=null){
                    int count = jsonArray.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.getJSONObject(i);
                        String shaftName = jsonObject1.getString("shaftName");
                        String shaftPrice = jsonObject1.getString("shaftPrice");
                        package_list.add(shaftName + "米轴");
                        package_list_num.add(Double.parseDouble(shaftName));
                        package_list_price.add(shaftPrice);
                        price_map.put(shaftName, shaftPrice);
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = price_map;
                    handler.sendMessage(message);
                    int package_count=package_list.size();
                    if(package_count>0){
                        for (int i = 0; i < package_count; i++) {
                            Bean bean = new Bean();
                            bean.setName(package_list.get(i));
                            bean.setStates("1");
                            mPackageList.add(bean);
                        }
                        setSpecificationsData();
                    }else {
                        Toast.makeText(ProdectInfoActivity.this,"该产品没有包装方式",Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 获取产品颜色服务
     */
    public void colorConnInter() {
        String url = Url.url("/androidColor/getColor");
        Map<String, String> param = new HashMap<>();
        param.put("typeTwoName", product.getTypeTwo().getTypeTwoName());
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonColorListener, errorListener, param);
        MySingleton.getInstance(this).addToRequestQueue(normalPostRequest);
    }

    /**
     * 获取产品颜色成功的监听器
     */
    private Response.Listener<JSONObject> jsonColorListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            try {
                jsonArray = jsonObject.optJSONArray("colorList");
                if(jsonArray!=null){
                    int count = jsonArray.length();
                    for (int i = 0; i < count; i++) {
                        JSONObject jsonObject1 = (JSONObject) jsonArray.getJSONObject(i);
                        String colorName = jsonObject1.getString("colorName");
                        //设置颜色
                        Bean bean = new Bean();
                        bean.setName(colorName);
                        bean.setStates("1");
                        mColorList.add(bean);
                    }
                    setColorData();
                }else {
                    Toast.makeText(ProdectInfoActivity.this,"该产品没有添加颜色",Toast.LENGTH_SHORT).show();
                    dismiss();
                }
                dialog.cancle();
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        color = "";
        packages = "";
        color_position = 0;
        spec_position = 0;
        prodectInfo_s1.setText(null);
        prodectInfo_s2.setText(null);
        handler.removeCallbacksAndMessages(null);

    }

    /**
     * 关闭弹框
     */
    private void dismiss() {
        if (popWindow!=null){
            popWindow.dismiss();
        }
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 1f;
        this.getWindow().setAttributes(params);
    }

    @Override
    public void onDismiss() {

    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            synchronized (mviewPager) {
                currentItem = (currentItem + 1) % list.size();
                handler.sendEmptyMessage(100);
            }
        }
    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    System.out.println(" 手势滑动，空闲中");
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    System.out.println(" 界面切换中");
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (mviewPager.getCurrentItem() == mviewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        mviewPager.setCurrentItem(0);
                        System.out.println(" 滑动到最后一张");
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (mviewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        mviewPager.setCurrentItem(mviewPager.getAdapter().getCount() - 1);
                        System.out.println(" 滑动到第一张");
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int position) {
            position1 = position;
            int page_count=list.size();
            for (int i = 0; i < page_count; i++) {
                dotViewList.get(i).setImageResource(R.mipmap.point_unpressed);
            }
            dotViewList.get(position).setImageResource(R.mipmap.point_pressed);
        }

    }

}

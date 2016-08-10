package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.ImagePaperAdapter;
import com.hbhongfei.hfcable.adapter.SkuAdapter;
import com.hbhongfei.hfcable.pojo.Bean;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.pojo.SkuItme;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.CallTel;
import com.hbhongfei.hfcable.util.DataUtil;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProdectInfoActivity extends AppCompatActivity implements View.OnClickListener,PopupWindow.OnDismissListener {
    private LayoutInflater inflater;
    private ImageView img1;
    private ViewPager mviewPager;
    private LinearLayout prodectList_LLayout_phone, prodectList_LLayout_collect, prodectList_LLayout_shoppingCat;
    private TextView prodectInto_simpleDeclaration, prodectInfo_price,
            prodectInfo_intro, prodectInfo_specifications, prodectInfo_model, prodectInfo_coreType,
            prodectInfo_type, prodectInfo_detail;
    private TextView prodect_addCart;
    private RelativeLayout selectSpec_layout;
    private LinearLayout all_choice_layout, prodect_bottom;
    private View mGrayLayout;
    private boolean isPopWindowShowing=false;
    LinearLayout layout;
    private int mnSeclectItem = 0;
    private ArrayList<String> mArrayList = new ArrayList<String>();
    private Product product;
//    popwindow弹框
    private TextView prodectInfo_s,prodectInfo_s1,prodectInfo_s2;
    String color;//
    String specifications;//规格
    List<SkuItme> mList;// sku数据

    List<Bean> mColorList;// 颜色列表
    List<Bean> mSpecificationsList;// 尺码列表

    GridView gvColor;// 颜色
    GridView gvSpecifications;// 规格
    SkuAdapter skuColorAdapter;// 颜色适配器
    SkuAdapter skuSpecificationsAdapter;// 规格适配器
    TextView tvSkuName;// 显示sku
    TextView tv_name;// 显示库存
    ImageView pop_del,iv_pic,prodectList_img_collect;//关闭图片
    Button btn_sure;//确定按钮
    TextView pop_add,pop_reduce,pop_num;
    TextView prodectList_tview_collect;
    private final int ADDORREDUCE=1;
    boolean isAddCart=false;
    int color_position=0;
    int spec_position=0;
    boolean isFirst=true;
    private static final String USER = LoginConnection.USER;
    String S_phoneNumber;
    RequestQueue mQueue;
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
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 100) {
                mviewPager.setCurrentItem(currentItem);
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodect_info);
        mQueue = Volley.newRequestQueue(this);
        toolBar();
        initVIew();
        setDate();
        click();
        if (isAutoPlay) {
            startPlay();
        }
    }
    /**
     * 展示toolbar
     */
    private void toolBar() {
        tag = getIntent().getStringExtra("tag");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void initVIew() {
        inflater = LayoutInflater.from(this);
        mviewPager = (ViewPager) findViewById(R.id.myviewPager);
        dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        img1 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
        prodectList_LLayout_phone = (LinearLayout) findViewById(R.id.prodectList_LLayout_phone);
        prodectList_LLayout_collect = (LinearLayout) findViewById(R.id.prodectList_LLayout_collect);
        prodectList_LLayout_shoppingCat = (LinearLayout) findViewById(R.id.prodectList_LLayout_shoppingCat);
        prodect_addCart = (TextView) findViewById(R.id.prodect_addCart);
//        mGrayLayout=findViewById(R.id.all_choice_layout);
        //产品信息
        prodectInfo_coreType = (TextView) findViewById(R.id.prodectInfo_coreType_textView);
        prodectInfo_detail = (TextView) findViewById(R.id.prodectInfo_detail_textView);
        prodectInfo_intro = (TextView) findViewById(R.id.prodectInfo_intro_textView);
        prodectInfo_model = (TextView) findViewById(R.id.prodectInfo_model_textview);
        prodectInfo_price = (TextView) findViewById(R.id.prodectInfo_price_textView);
        prodectInfo_specifications = (TextView) findViewById(R.id.prodectInfo_specifications);
        prodectInfo_type = (TextView) findViewById(R.id.prodectInfo_type_textView);
        prodectInto_simpleDeclaration = (TextView) findViewById(R.id.prodectInto_simpleDeclaration_tview);
        selectSpec_layout = (RelativeLayout) findViewById(R.id.selectSpec_layout);//选择颜色规格
        prodect_bottom = (LinearLayout) findViewById(R.id.prodect_bottom);
        prodectList_img_collect= (ImageView) findViewById(R.id.prodectList_img_collect);
        prodectList_tview_collect= (TextView) findViewById(R.id.prodectList_tview_collect);
        //选择规格和颜色
        prodectInfo_s= (TextView) findViewById(R.id.prodectInfo_s);
        prodectInfo_s1= (TextView) findViewById(R.id.prodectInfo_s1);
        prodectInfo_s2= (TextView) findViewById(R.id.prodectInfo_s2);

        //初始化对话框
//        popWindow = new BabyPopWindow(ProdectInfoActivity.this);
    }

    /**
     * 设置数据
     */
    public void setDate() {
        SharedPreferences spf =getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        list = new ArrayList<ImageView>();
        dotViewList = new ArrayList<ImageView>();
        dotLayout.removeAllViews();
        //获取产品列表的详细产品信息
        intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        prodectInfo_detail.setText(product.getDetail());
        prodectInfo_coreType.setText(product.getLineCoreType());
        prodectInfo_price.setText(String.valueOf(product.getPrice()));
        prodectInfo_model.setText(product.getModel());
        prodectInfo_type.setText(product.getTypeName());
        prodectInto_simpleDeclaration.setText(product.getProdectName());//产品名称
        //产品图片
        if(product.getProductImages()!=null){
            for (String s:product.getProductImages()){
                //获取图片并显示
                String url= Url.url(s);
                img1 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
                img1.setTag(url);
                AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
                asyncBitmapLoader.loadImage(this,img1,url);
                list.add(img1);
                //给图片添加点击事件。查看大图
                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        // 跳到查看大图界面
                        Intent intent = new Intent(ProdectInfoActivity.this,
                                ShowBigPictrue.class);
                        intent.putExtra("position", position1);
                        intent.putStringArrayListExtra("image_List",product.getProductImages());
                        startActivity(intent);
                    }
                });
            }
            setSmallDot(list);
        }else {
            img1.setImageResource(R.mipmap.main_img1);
        }
        //设置收藏
        isOrNotColl();
    }


    /**
     * 设置小圆点
     * @param list
     */
    public void setSmallDot(List<ImageView>list){
        //加入小圆点
        for (int i = 0; i < list.size(); i++) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //拨打电话
            case R.id.prodectList_LLayout_phone:
                SharedPreferences settings = getSharedPreferences("SAVE_PHONE", Activity.MODE_PRIVATE);
                final String phone=settings.getString("phoneNum","");
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
                                CallTel callTel=new CallTel(ProdectInfoActivity.this);
                                callTel.call(phone);
                                sDialog.dismiss();
                            }
                        })
                           .show();
                Toast.makeText(this, phone, Toast.LENGTH_SHORT).show();
                break;
            //添加收藏
            case R.id.prodectList_LLayout_collect:
                //收藏服务
                collectionCollect();
                break;
            //购物车
            case R.id.prodectList_LLayout_shoppingCat:
                Intent intent=new Intent(this,MyShoppingActivity.class);
                startActivity(intent);
                break;
            //添加购物车
            case R.id.prodect_addCart:
                if(!TextUtils.isEmpty(color) && !TextUtils.isEmpty(specifications)){
                    //添加到购物车
                    addShoppingCartCoon();
                    Toast.makeText(this, pop_num.getText().toString()+"添加成功",Toast.LENGTH_SHORT).show();
                }else{
                    isAddCart=true;
                    showPopwindow();
                }
                break;
            //弹出框中的添加数量
            case R.id.pop_add:
                if (!pop_num.getText().toString().equals("750")) {
                    String num_add=Integer.valueOf(pop_num.getText().toString())+ADDORREDUCE+"";
                    pop_num.setText(num_add);
                }else {
                    Toast.makeText(this, "不能超过最大产品数量", Toast.LENGTH_SHORT).show();
                }
                break;
            //弹出框中的减少数量
            case R.id.pop_reduce:
                if (!pop_num.getText().toString().equals("1")) {
                    String num_reduce=Integer.valueOf(pop_num.getText().toString())-ADDORREDUCE+"";
                    pop_num.setText(num_reduce);
                }else {
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
//                popWindow.dismiss();
                dismiss();
                break;
            //弹出框的确定按钮
            case R.id.btn_sure:
                if (TextUtils.isEmpty(color)) {
                    Toast.makeText(this, "亲，你还没有选择颜色哟~", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(specifications)) {
                    Toast.makeText(this, "亲，你还没有选择规格哟~",Toast.LENGTH_SHORT).show();
                }else {
                    showColorandSpec();
                    if (isAddCart){
                        //添加到购物车服务
                        addShoppingCartCoon();
                        dismiss();
                        isAddCart=false;
                    }
                    dismiss();
                }
                break;
        }
    }
    /**
     *添加购物车服务
     */
    private void  addShoppingCartCoon(){
        String url = Url.url("/androidShoppingCart/sava");
        Map<String,String> map=new HashMap<>();
        map.put("productId",product.getId());
        map.put("color",color);
        map.put("specifications",specifications);
        map.put("quantity",pop_num.getText().toString());
        map.put("userName",S_phoneNumber);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectAddShoppingListener,errorListener,map);
        mQueue.add(normalPostRequest);
    }


    /**
     * 添加收藏服务
     */
    private void  collectionCollect(){
        String url = Url.url("/androidCollecton/sava");
        Map<String,String> map=new HashMap<>();
        map.put("productId",product.getId());
        map.put("userName",S_phoneNumber);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectCollectionListener,errorListener,map);
        mQueue.add(normalPostRequest);
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
                if(TextUtils.equals(msg,"成功")){
                    new SweetAlertDialog(ProdectInfoActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("添加购物车成功")
                            .show();
                }else{
                    new SweetAlertDialog(ProdectInfoActivity.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("添加购物车失败")
                            .show();
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
                if(TextUtils.equals(msg, "收藏成功")){
                    prodectList_img_collect.setImageResource(R.mipmap.heart_red);
                    prodectList_tview_collect.setText("已收藏");
                    new SweetAlertDialog(ProdectInfoActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("添加收藏成功")
                            .show();
                }
                else if (TextUtils.equals(msg, "取消收藏")) {
                    new SweetAlertDialog(ProdectInfoActivity.this,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("取消收藏成功")
                            .show();
                    prodectList_img_collect.setImageResource(R.mipmap.heart);
                    prodectList_tview_collect.setText("收藏");
                } else if(TextUtils.equals(msg, "取消收藏失败")){
                    Toast.makeText(ProdectInfoActivity.this, "取消收藏失败", Toast.LENGTH_SHORT).show();
                }else {
                    new SweetAlertDialog(ProdectInfoActivity.this,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("收藏失败")
                            .show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ;
    };

    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ProdectInfoActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

    /**
     * 判断是否收藏
     */
    private void  isOrNotColl(){
        String url = Url.url("/androidCollecton/isCollection");
        Map<String,String> map=new HashMap<>();
        map.put("productId",product.getId());
        map.put("userName",S_phoneNumber);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectIsListener,errorListener,map);
        mQueue.add(normalPostRequest);
    }
    /**
     * 成功的监听器
     * 返回的手长状态信息
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
            } catch (JSONException e) {
                e.printStackTrace();
            }


        };
    };

    /**
 * 显示颜色规格
 */
    private void showColorandSpec(){
        if(!TextUtils.isEmpty(color)|| !TextUtils.isEmpty(specifications)){
            prodectInfo_specifications.setVisibility(View.GONE);
            prodectInfo_s.setVisibility(View.VISIBLE);
            prodectInfo_s1.setVisibility(View.VISIBLE);
            prodectInfo_s2.setVisibility(View.VISIBLE);
            prodectInfo_s2.setText(color);
            prodectInfo_s1.setText(specifications);
        }else {
            prodectInfo_specifications.setVisibility(View.VISIBLE);
            prodectInfo_s.setVisibility(View.INVISIBLE);
            prodectInfo_s1.setVisibility(View.INVISIBLE);
            prodectInfo_s2.setVisibility(View.INVISIBLE);
        }
    }
    /**
     * 设置popwindow
     */
    private void showPopwindow() {
        View view=LayoutInflater.from(this).inflate(R.layout.adapter_popwindow, null);
        gvSpecifications = (GridView) view.findViewById(R.id.gv_specifications);
        gvColor = (GridView) view.findViewById(R.id.gv_color);
        tvSkuName = (TextView) view.findViewById(R.id.tv_sku);
        tv_name= (TextView) view.findViewById(R.id.tv_name);
        iv_pic= (ImageView) view.findViewById(R.id.iv_pic);
        pop_del= (ImageView) view.findViewById(R.id.pop_del);
        btn_sure= (Button) view.findViewById(R.id.btn_sure);
        pop_add=(TextView) view.findViewById(R.id.pop_add);
        pop_reduce=(TextView) view.findViewById(R.id.pop_reduce);
        pop_num=(TextView) view.findViewById(R.id.pop_num);

        tv_name.setText(product.getProdectName());
        //设置弹窗的图片
        if(product.getProductImages()!=null){
            String url=Url.url(product.getProductImages().get(0));
            iv_pic.setTag(url);
            AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
            asyncBitmapLoader.loadImage(this,iv_pic,url);
        }else {
            iv_pic.setImageResource(R.mipmap.ic_launcher);
        }

        pop_del.setOnClickListener(this);
        btn_sure.setOnClickListener(this);
        pop_add.setOnClickListener(this);
        pop_reduce.setOnClickListener(this);
        addData();
        setColorData();
        setSpecificationsData();
        pop_num.setText(pop_num.getText().toString());
        //设置popowindow属性
        popWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,false);
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
        WindowManager.LayoutParams params=this.getWindow().getAttributes();
        params.alpha=0.5f;
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
     * 设置产品规格
     */
    public void setSpecificationsData(){
        skuSpecificationsAdapter = new SkuAdapter(mSpecificationsList, this);
        //设置默认选中的位置
        skuSpecificationsAdapter.setSelectedPosition(spec_position);
        gvSpecifications.setAdapter(skuSpecificationsAdapter);
        skuSpecificationsAdapter.setItemClickListener(new SkuAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Bean bean, int position) {
                // TODO Auto-generated method stub
                specifications = bean.getName();
                switch (Integer.parseInt(bean.getStates())) {
                    case 0:
                        // 清空规格
                        mSpecificationsList= DataUtil.clearAdapterStates(mSpecificationsList);
                        skuSpecificationsAdapter.notifyDataSetChanged();
                        // 清空颜色
                        mColorList=DataUtil.clearAdapterStates(mColorList);
                        skuColorAdapter.notifyDataSetChanged();
                        specifications = "";
                        if (!TextUtils.isEmpty(color)) {
                            tvSkuName.setText("请选择规格");
                            mColorList=DataUtil.setAdapterStates(mColorList,color);
                            skuColorAdapter.notifyDataSetChanged();
                        } else {
                            tvSkuName.setText("请选择规格,颜色");
                        }
                        break;
                    case 1:
                        // 选中规格
                        mSpecificationsList=DataUtil.updateAdapterStates(mSpecificationsList, "0", position);
                        skuSpecificationsAdapter.notifyDataSetChanged();
                        spec_position=position;
                        bean.setStates("0");
                        gvSpecifications.setSelection(position);
                        if (!TextUtils.isEmpty(color)) {
                            // 选择规格的同事选择颜色
                            tvSkuName.setText("规格:" + color + " " + specifications);
//
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
    public void setColorData(){
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
                        mColorList=DataUtil.clearAdapterStates(mColorList);
                        skuColorAdapter.notifyDataSetChanged();
                        color = "";
                        // 判断是否选中了规格
                        if(TextUtils.isEmpty(specifications)){
                            tvSkuName.setText("请选择规格,颜色");
                        }else {
                            tvSkuName.setText("请选择颜色");
                        }
                        break;
                    case 1:
                        // 选中颜色
                        color_position=position;
                        mColorList=DataUtil.updateAdapterStates(mColorList,"0", color_position);

                        skuColorAdapter.notifyDataSetChanged();
                        List<String> list = DataUtil.getSizeListByColor(mList,color);
                        if (!TextUtils.isEmpty(specifications)) {
                            // 计算改颜色与尺码对应的库存
//                            stock = DataUtil.getStockByColorAndSize(mList,color, specifications);
                            tvSkuName.setText("规格:" + color + " " + specifications);
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
    public void addData() {

        mList = new ArrayList<SkuItme>();
        //颜色规格列表
        mColorList = new ArrayList<Bean>();
        mSpecificationsList = new ArrayList<Bean>();
        List<String> color_list=new ArrayList<>();
        List<String> specifications_list=new ArrayList<>();
        for(int i=0;i<5;i++){
            color_list.add("颜色"+i);
        }
        for(int i=0;i<5;i++){
            specifications_list.add("规格"+i);
        }
        //所有的颜色和规格
        //颜色列表
        for (int i = 0; i <color_list.size(); i++) {
            Bean bean = new Bean();
            bean.setName(color_list.get(i));
            bean.setStates("1");
            mColorList.add(bean);
        }
        for (int i = 0; i < specifications_list.size(); i++) {
            Bean bean = new Bean();
            bean.setName(specifications_list.get(i));
            bean.setStates("1");
            mSpecificationsList.add(bean);
        }

    }
    @Override
    public void onDismiss() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        color="";
        specifications="";
        color_position=0;
        spec_position=0;
        prodectInfo_s1.setText(null);
        prodectInfo_s2.setText(null);
    }

/**
 * 关闭弹框
 */
    private void dismiss(){
        popWindow.dismiss();
        WindowManager.LayoutParams params=this.getWindow().getAttributes();
        params.alpha=1f;
        this.getWindow().setAttributes(params);
    }
    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
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
            // TODO Auto-generated method stub
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
            Log.i("zj", "onPagerChange position=" + position);
            for (int i = 0; i < list.size(); i++) {
                dotViewList.get(i).setImageResource(R.mipmap.point_unpressed);
            }
            dotViewList.get(position).setImageResource(R.mipmap.point_pressed);
        }

    }

    /**
     * actionBar返回键
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

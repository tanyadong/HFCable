package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MainFragmentAdapter;
import com.hbhongfei.hfcable.fragment.BaseFragment;
import com.hbhongfei.hfcable.fragment.IndexFragment;
import com.hbhongfei.hfcable.fragment.InfoFragment;
import com.hbhongfei.hfcable.fragment.MarketFragment;
import com.hbhongfei.hfcable.fragment.MineFragment;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Constants;
import com.hbhongfei.hfcable.util.CustomDialog;
import com.hbhongfei.hfcable.util.IAlertDialogListener;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.UpLoadImage;
import com.hbhongfei.hfcable.util.Url;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ViewPager.OnPageChangeListener, IAlertDialogListener {

    private ViewPager viewPager;
    private List<BaseFragment> list_fragment;
    private LinearLayout layout_index, layout_info, layout_market, layout_mine;
    private ImageView imageView_index, imageView_info, imageView_market, imageView_mine;
    private TextView textView_index, textView_info, textView_market, textView_mine;
    private Handler myHandler = new Handler();

    private static final String USER = LoginConnection.USER;
    private ImageView head;
    private TextView myName;
    private String S_name, S_phoneNumber, S_head;
    private View headerLayout;
    private NavigationView navigationView;
    private Drawable drawable;
    private Bitmap b;
    private int tag = 0;
    private UpLoadImage upLoadImage;
    private AsyncBitmapLoader asyncBitmapLoader;
    private Toolbar toolbar;
    private int id;
    private IndexFragment indexFragment = null;
    private InfoFragment infoFragment = null;
    private MarketFragment marketFragment = null;
    private MineFragment mineFragment = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        toolbar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //初始化界面
        initView();

        //点击事件
        click();
    }

    /**
     * toolbar
     */
    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
    }
    @Override
    protected void onPostResume() {
        id = SplashActivity.ID;
            if (id==4){
                showMine();
                viewPager.setCurrentItem(3);
            }else if(id==1){
                showHome();
                viewPager.setCurrentItem(0);
            }else if(id==3){
                showMarket();
                viewPager.setCurrentItem(2);
            }else{
                showInfo();
                viewPager.setCurrentItem(1);
            }
        //初始化数据
        initValues();
        super.onPostResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 跳转页面
     * 苑雪元
     * 2016/07/21
     *
     * @param c 要跳转的页面
     */
    private void intent(Class c) {
        Intent i = new Intent();
        i.setClass(this, c);
        startActivity(i);
    }

    /**
     * 判断是否登录
     */
    private void toLoginOrNot(Class c){
        if (S_phoneNumber!=null){
            intent(c);
        }else{
            toLogin();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.Rlayout_mine_favorite:
                toLoginOrNot(MyFavoriteActivity.class);
                break;
            case R.id.Rlayout_mine_order:
                toLoginOrNot(MyOrderActivity.class);
                break;
            case R.id.Rlayout_mine_shopping:
                toLoginOrNot(MyShoppingActivity.class);
                break;
            case R.id.mine_edit:
                toLoginOrNot(MyInfoActivity.class);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 点击事件
     */
    private void click() {
        this.layout_index.setOnClickListener(this);
        this.layout_info.setOnClickListener(this);
        this.layout_market.setOnClickListener(this);
        this.layout_mine.setOnClickListener(this);
        this.head.setOnClickListener(this);
        //设置ViewPager滑动监听
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        //main
        this.layout_index = (LinearLayout) findViewById(R.id.ll_main_page);
        this.layout_info = (LinearLayout) findViewById(R.id.ll_main_find);
        this.layout_market = (LinearLayout) findViewById(R.id.ll_main_mess);
        this.layout_mine = (LinearLayout) findViewById(R.id.ll_main_mine);

        this.textView_index = (TextView) findViewById(R.id.txt_main);
        this.textView_info = (TextView) findViewById(R.id.txt_mess);
        this.textView_market = (TextView) findViewById(R.id.txt_find);
        this.textView_mine = (TextView) findViewById(R.id.txt_mine);

        this.imageView_index = (ImageView) findViewById(R.id.img_main);
        this.imageView_info = (ImageView) findViewById(R.id.img_mess);
        this.imageView_market = (ImageView) findViewById(R.id.img_find);
        this.imageView_mine = (ImageView) findViewById(R.id.img_mine);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indexFragment = new IndexFragment();
        infoFragment = new InfoFragment();
        marketFragment = new MarketFragment();
        mineFragment = new MineFragment();

        list_fragment = new ArrayList<>();

        list_fragment.add(indexFragment);
        list_fragment.add(infoFragment);
        list_fragment.add(marketFragment);
        list_fragment.add(mineFragment);

        MainFragmentAdapter m1 = new MainFragmentAdapter(getSupportFragmentManager(),list_fragment);

        viewPager.setAdapter(m1);
        //关闭预加载，默认一次只加载一个Fragment
        viewPager.setOffscreenPageLimit(3);
        //关闭预加载，默认一次只加载一个Fragment
        //mine
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        head = (ImageView) headerLayout.findViewById(R.id.Iamge_mine_head);
        myName = (TextView) headerLayout.findViewById(R.id.Tview_mine_myName);

        upLoadImage = new UpLoadImage(this);
    }
    /**
     * 初始化数据
     */
    private void initValues() {
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        //用户名
        S_name = spf.getString("nickName", null);
        if (S_name == null) {
            S_name = "还未登录哦";
            myName.setText(S_name);
        } else {
            myName.setText(S_name);
        }
        S_phoneNumber = spf.getString("phoneNumber", null);

        //头像
        S_head = spf.getString("headPortrait", null);
        if (S_head != null && tag == 0) {
            String url = Url.url(S_head);
            head.setTag(url);
            asyncBitmapLoader = new AsyncBitmapLoader();
            asyncBitmapLoader.loadImage(this, head, url);
        } else if (tag != 0 && S_head != null) {
            head.setImageBitmap(b);
        } else {
            drawable = MainActivity.this.getResources().getDrawable(R.mipmap.head_portrait);
            head.setImageDrawable(drawable);
        }
    }

    /**
     * 点击首页时显示
     */
    private void showHome(){
        imageView_index.setImageResource(R.mipmap.home_red);
        textView_index.setTextColor(Color.parseColor("#ff0000"));
        imageView_market.setImageResource(R.mipmap.market_grey);
        textView_market.setTextColor(Color.parseColor("#000000"));
        imageView_info.setImageResource(R.mipmap.info_grey);
        textView_info.setTextColor(Color.parseColor("#000000"));
        imageView_mine.setImageResource(R.mipmap.cable_grey);
        textView_mine.setTextColor(Color.parseColor("#000000"));
        //隐藏menu
        SplashActivity.ID = 1;
    }

    /**
     * 点击资讯时显示
     */
    private void showInfo() {
        imageView_info.setImageResource(R.mipmap.info_red);
        textView_info.setTextColor(Color.parseColor("#ff0000"));
        imageView_index.setImageResource(R.mipmap.home_grey);
        textView_index.setTextColor(Color.parseColor("#000000"));
        imageView_market.setImageResource(R.mipmap.market_grey);
        textView_market.setTextColor(Color.parseColor("#000000"));
        imageView_mine.setImageResource(R.mipmap.cable_grey);
        textView_mine.setTextColor(Color.parseColor("#000000"));

        SplashActivity.ID = 2;
    }

    /**
     * 点击行情时显示
     */
    private void showMarket(){
        imageView_market.setImageResource(R.mipmap.market_red);
        textView_market.setTextColor(Color.parseColor("#ff0000"));
        imageView_index.setImageResource(R.mipmap.home_grey);
        textView_index.setTextColor(Color.parseColor("#000000"));
        imageView_info.setImageResource(R.mipmap.info_grey);
        textView_info.setTextColor(Color.parseColor("#000000"));
        imageView_mine.setImageResource(R.mipmap.cable_grey);
        textView_mine.setTextColor(Color.parseColor("#000000"));
        //隐藏menu
        SplashActivity.ID =3;
    }

    /**
     * 点击我的时显示
     */
    private void showMine() {
        imageView_mine.setImageResource(R.mipmap.cable_red);
        textView_mine.setTextColor(Color.parseColor("#ff0000"));
        imageView_index.setImageResource(R.mipmap.home_grey);
        textView_index.setTextColor(Color.parseColor("#000000"));
        imageView_info.setImageResource(R.mipmap.info_grey);
        textView_info.setTextColor(Color.parseColor("#000000"));
        imageView_market.setImageResource(R.mipmap.market_grey);
        textView_market.setTextColor(Color.parseColor("#000000"));
        //隐藏menu
        SplashActivity.ID = 4;
    }

    //****************************************************************
    // 判断应用是否进行登录过
    //****************************************************************
    private static final String LOGINORNOT = LoginConnection.USER;

    private boolean isLogin(Context context, String className) {
        if (context == null || className == null || "".equalsIgnoreCase(className)) {
            return false;
        }
        SharedPreferences spf = context.getSharedPreferences(LOGINORNOT, Context.MODE_PRIVATE);
        String loginOrNot = spf.getString("id", null);
        if (loginOrNot != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 跳转到登录界面
     */
    private void toLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

        boolean isLogin = isLogin(MainActivity.this, MainActivity.this.getClass().getName());
        switch (v.getId()) {
            case R.id.ll_main_page:
                showHome();
                viewPager.setCurrentItem(0);
                break;
            case R.id.ll_main_mess:
                showInfo();
                viewPager.setCurrentItem(1);
                break;
            case R.id.ll_main_find:
                showMarket();
                viewPager.setCurrentItem(2);
                break;
            case R.id.ll_main_mine:
                    showMine();
                    viewPager.setCurrentItem(3);
                break;
            case R.id.Iamge_mine_head:
                if (S_phoneNumber != null) {
                    //修改头像
                    showDialog();
                }else{
                    toLogin();
                }
                break;
        }
    }

    /**
     * 选择拍照还是相册
     */
    private void showDialog() {
        CustomDialog.selectHead(this, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH", Constants.PHOTONAME);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = MainActivity.this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent(MainActivity.this, ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH", picturePath);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.CROP_BEAUTY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                b = BitmapFactory.decodeFile(path);
                if (b != null) {
                    tag++;
                    head.setImageBitmap(b);
                    //上传
                    upLoadImage.load(b, S_phoneNumber);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                showHome();
                break;
            case 1:
                showInfo();
                break;
            case 2:
                showMarket();

                break;
            case 3:
                showMine();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CustomDialog.showOkOrCancleDialog(this,"您确定要退出么？",this);
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 退出应用时的确定按钮
     */
    @Override
    public void onSureClick() {
        finish();
    }

    /**
     * 选择照片点击事件
     */
    @Override
    public void selectFromAlbumClick() {
        //相册中选择
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, Constants.PICK_PHOTO);
    }

    /**
     * 拍照的点击事件
     */
    @Override
    public void photoClick() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 下面这句指定调用相机拍照后的照片存储的路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.PHOTONAME)));
        startActivityForResult(intent, Constants.TAKE_PHOTO);
    }

}

package com.hbhongfei.hfcable.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.Company;
import com.hbhongfei.hfcable.util.Url;

public class CompanyInfoActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private ImageView logo;
    private TextView companyInfo_name_textView,companyInfo_introduce_textView,companyInfo_product_textView,
            companyInfo_address,companyInfo_tel,companyInfo_postcode_textView,companyInfo_email_textView;
    private AppBarLayout companyInfoABL;
    private LinearLayout companyInfoLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        toolBar();
        initView();
        setValues();
        setOnclick();
    }
    /**
     * 初始化
     */
    private void initView(){
        logo= (ImageView) findViewById(R.id.companyInfo_logo);
        companyInfo_name_textView= (TextView) findViewById(R.id.companyInfo_name_textView);
        companyInfo_address= (TextView) findViewById(R.id.companyInfo_address_textView);
        companyInfo_email_textView= (TextView) findViewById(R.id.companyInfo_email_textView);
        companyInfo_tel= (TextView) findViewById(R.id.companyInfo_tel_textView);
        companyInfo_introduce_textView= (TextView) findViewById(R.id.companyInfo_introduce_textView);
        companyInfo_postcode_textView= (TextView) findViewById(R.id.companyInfo_postcode_textView);
        companyInfo_product_textView= (TextView) findViewById(R.id.companyInfo_product_textView);
        companyInfoABL = (AppBarLayout) findViewById(R.id.abl_company_info);
        companyInfoLL = (LinearLayout) findViewById(R.id.company_info_LL);
    }

    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_company_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置状态栏背景为透明
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_info_detail);
        mCollapsingToolbarLayout.setTitle("资讯详情");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    /**
     * 设置点击事件
     */
    private void setOnclick(){
        companyInfoABL.addOnOffsetChangedListener(this);
    }

    /**
     * 赋值
     */
    private  void  setValues(){
        Intent intent=getIntent();
        Company company= (Company) intent.getSerializableExtra("company");
        companyInfo_name_textView.setText(company.getCompanyName());
        companyInfo_postcode_textView.setText(String.valueOf(company.getZipCode()));
        companyInfo_tel.setText(company.getTelephone());
        companyInfo_product_textView.setText(company.getProductIntroduction());
        companyInfo_address.setText(company.getAddress());
        companyInfo_email_textView.setText(company.getEmail());
        companyInfo_introduce_textView.setText(company.getDescription());
        //网络加载图片
        String url= Url.url(company.getLogo());
        Glide.with(this)
                .load(url)
                .placeholder(R.mipmap.background)
                .error(R.mipmap.loading_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( logo );
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {//展开状态
            companyInfoLL.setBackgroundColor(Color.parseColor("#ffffff"));
            companyInfo_name_textView.setTextColor(Color.parseColor("#000000"));
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {////折叠状态
            companyInfoLL.setBackgroundColor(Color.parseColor("#ff0000"));
            companyInfo_name_textView.setTextColor(Color.parseColor("#ffffff"));
        } else { //中间状态
        }
    }
}

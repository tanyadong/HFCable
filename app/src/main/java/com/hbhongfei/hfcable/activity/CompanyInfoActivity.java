package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.Company;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Url;

public class CompanyInfoActivity extends AppCompatActivity {
    private ImageView logo;
    private TextView companyInfo_name_textView,companyInfo_introduce_textView,companyInfo_product_textView,
            companyInfo_address,companyInfo_tel,companyInfo_postcode_textView,companyInfo_email_textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        initView();
        setValues();
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
        logo.setTag(url);
        AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
        asyncBitmapLoader.loadImage(this,logo,url);
    }

}

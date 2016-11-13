package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class InputCompanyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText EcompanyName,ElegalName,Etel;
    private TextView Tsave;
    private String S_phone,S_password,S_name,S_sex,S_birthday,photo,S_companName,S_legalName,S_companyTel;
    private Dialog dialog;
    private LoginConnection loginConnection;
    private static final String USER = LoginConnection.USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_company_info);

        //toolbar
        toolbar();

        //初始化界面
        initView();

        //初始化数据
        initValue();

        //设置点击事件
        setOnclick();

    }


    /**
     * toolbar
     */
    private void toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inputCompanyInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
    }

    /**
     * 初始化界面
     */
    private void initView(){
        dialog = new Dialog(this);

        this.EcompanyName = (EditText) findViewById(R.id.Etext_inputCompanyInfo_name);
        this.ElegalName = (EditText) findViewById(R.id.Etext_inputCompanyInfo_name_legal);
        this.Etel = (EditText) findViewById(R.id.Etext_inputCompanyInfo_tel);

        this.Tsave = (TextView) findViewById(R.id.Tview_inputCompanyInfo_saveInfo);
    }

    /**
     * 初始化数据
     */
    private void initValue(){
        Intent intent = getIntent();
        S_phone = intent.getStringExtra("phoneNumber");
        S_password = intent.getStringExtra("password");
        photo = intent.getStringExtra("photo");
        S_name = intent.getStringExtra("nickName");
        S_sex = intent.getStringExtra("sex");
        S_birthday = intent.getStringExtra("birthday");
    }

    /**
     * 设置点击事件
     */
    private void setOnclick(){
        this.Tsave.setOnClickListener(this);
    }

    /**
     * 获取数据
     */
    private void getValues(){
        S_companName = this.EcompanyName.getText().toString().trim();
        S_legalName = this.ElegalName.getText().toString().trim();
        S_companyTel = this.Etel.getText().toString().trim();
    }

    /**
     * 判断是否为空
     */
    private boolean IsEmpty(){
        if (TextUtils.isEmpty(S_companName)){
            Toast.makeText(this,"请填写公司名称",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(S_legalName)){
            Toast.makeText(this,"请填写公司法人姓名",Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(S_companyTel)){
            Toast.makeText(this,"请填写公司联系电话",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //保存信息
            case R.id.Tview_inputCompanyInfo_saveInfo:
                getValues();
                if (IsEmpty()){
                    savePersonAndCompanyInfoConnInter();
                }
                break;
        }
    }

    /**
     * 保存个人信息时连接服务
     */
    private void savePersonAndCompanyInfoConnInter(){
        dialog.showDialog("正在保存...");
        Map<String,String> params =new HashMap<>();
        params.put("phoneNumber", S_phone);
        params.put("photo",photo);
        params.put("nickName",S_name);
        params.put("tag","1");
        params.put("sex",S_sex);
        params.put("birthday",S_birthday);
        params.put("companyName",S_companName);
        params.put("legalPersonName",S_legalName);
        params.put("companyTel",S_companyTel);
        String url = Url.url("/androidUser/addPersonInfo");
        RequestQueue mQueue = Volley.newRequestQueue(this);
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
                String s = jsonObject.getString("savePersonInfo");
                if (s.equals("success")){
                    Toast.makeText(InputCompanyInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = InputCompanyInfoActivity.this.getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
                    editor.putString("tag", "1");
                    editor.apply();
                    loginConnection = new LoginConnection(InputCompanyInfoActivity.this);
                    loginConnection.connInter(S_phone,S_password);
                    dialog.cancle();
                }else {
                    dialog.cancle();
                    Toast.makeText(InputCompanyInfoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                }
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
            Toast.makeText(InputCompanyInfoActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            dialog.cancle();
        }
    };
}

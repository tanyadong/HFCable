package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.CheckPhoneNumber;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Txt_tel_fragment_user,Txt_tel_fragment_password,Txt_tel_fragment_password_sure,Txt_tel_fragment_verification_code;
    private TextView TView_tel_fragment_get_verification_code;
    private String S_user,S_password,S_password_sure,S_verificationCode,S_w;
    private Button Btn_register;
    private boolean Tag =true;
    private LoginConnection loginConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);//初始化界面
        //初始化界面
        initView();

        //初始化数据
        initValue();

        //初始化点击事件
        click();
    }



    /**
     * 初始化点击事件
     */
    private void click() {
        this.TView_tel_fragment_get_verification_code.setOnClickListener(this);
        this.Btn_register.setOnClickListener(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        this.Txt_tel_fragment_user = (EditText) findViewById(R.id.Txt_tel_fragment_user);
        this.Txt_tel_fragment_password = (EditText) findViewById(R.id.Txt_tel_fragment_password);
        this.Txt_tel_fragment_password_sure = (EditText) findViewById(R.id.Txt_tel_fragment_password_sure);
        this.Txt_tel_fragment_verification_code = (EditText) findViewById(R.id.Txt_tel_fragment_verification_code);
        this.TView_tel_fragment_get_verification_code = (TextView) findViewById(R.id.TView_tel_fragment_get_verification_code);
        this.Btn_register = (Button) findViewById(R.id.Btn_register);
    }

    public void initValue(){
        Intent intent = getIntent();
        S_w = intent.getStringExtra("W");
        if (S_w.equals("register")){
            this.Btn_register.setText("立即注册");
        }else if(S_w.equals("forget")){
            this.Btn_register.setText("找回密码");
        }
    }

    /**
     * 获取数据
     */
    private void getValues(){
        S_user = this.Txt_tel_fragment_user.getText().toString().trim();
        S_password = this.Txt_tel_fragment_password.getText().toString().trim();
        S_password_sure = this.Txt_tel_fragment_password.getText().toString().trim();
        S_verificationCode = this.Txt_tel_fragment_verification_code.getText().toString().trim();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //获取验证码
            case R.id.TView_tel_fragment_get_verification_code:
                getValues();
                getVerificationCode(S_user);
                break;
            //进行注册
            case R.id.Btn_register:
                if (S_w.equals("register")){
                    //注册
                    getValues();
                    register();
                }else if(S_w.equals("forget")){
                    //找回密码
                    getValues();
                    forget();
                }
                break;
        }
    }

    /**
     * 验证是否为空
     */
    private boolean isEmpty(){
        if (TextUtils.isEmpty(S_user)){
            Toast.makeText(RegisterActivity.this,"请填写手机号", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else if(!CheckPhoneNumber.checkPhoneNum(S_user)){
            Toast.makeText(RegisterActivity.this,"请填写正确的手机号", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else if(TextUtils.isEmpty(S_password)){
            Toast.makeText(RegisterActivity.this,"请填写密码", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else if(!(S_password.length()>=6 && S_password.length()<=16)){
            Toast.makeText(RegisterActivity.this,"请填写合适长度的密码", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else if(TextUtils.isEmpty(S_password_sure)){
            Toast.makeText(RegisterActivity.this,"请填写确认密码", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else if(!(S_password.equals(S_password_sure))){
            Toast.makeText(RegisterActivity.this,"两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else if(TextUtils.isEmpty(S_verificationCode)){
            Toast.makeText(RegisterActivity.this,"请填写验证码", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else{
            Tag = true;
        }
        return Tag;
    }

    /**
     * 进行注册
     */
    public void register(){
        if (isEmpty()){
            Toast.makeText(RegisterActivity.this,"信息完善", Toast.LENGTH_SHORT).show();
            checkPhone();
        }
    }

    /**
     * 找回密码
     */
    public  void forget(){
        if (isEmpty()){
            Toast.makeText(RegisterActivity.this,"信息完善", Toast.LENGTH_SHORT).show();
            checkPhone();
        }
    }


    /**
     * 获取验证码
     * @param S_tel
     */
    private void getVerificationCode(String S_tel){

    }

    /**
     * 检查手机号是否已经注册时连接服务
     */
    private void checkPhone(){
        Map<String,String> params =new HashMap<>();
        params.put("userName", S_user);
        String url = Url.url("androidUser/exist");
        System.out.println(url);
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,checkPhoneListener,errorListener, params);
        mQueue.add(request);
    }

    /**
     * 检查手机号是否已经注册时连接服务
     * 成功的监听器
     */
    private Response.Listener<JSONObject> checkPhoneListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("exist");
                if (s.equals("yes")){
                    if (S_w.equals("register")){
                        //注册
                        Toast.makeText(RegisterActivity.this, "手机号已经注册，请更换手机号或者找回密码", Toast.LENGTH_SHORT).show();
                    }else if(S_w.equals("forget")){
                        //找回密码
                        updateConnInter();
                    }
                }else {
                    if (S_w.equals("register")){
                        //进行注册
                        registerConnInter();

                    }else if(S_w.equals("forget")){
                        //找回密码
                        Toast.makeText(RegisterActivity.this, "手机号未注册，请注册", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 找回密码时连接服务
     */
    private void updateConnInter(){
        Map<String,String> params =new HashMap<>();
        params.put("phoneNumber", S_user);
        params.put("password", S_password);
        String url = Url.url("androidUser/updatePassword");
        System.out.println(url);
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,updateListener,errorListener, params);
        mQueue.add(request);
    }

    /**
     * 找回密码时连接服务
     * 成功的监听器
     */
    private Response.Listener<JSONObject> updateListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("updatePassword");
                if (s.equals("success")){
                    Toast.makeText(RegisterActivity.this, "更新密码成功", Toast.LENGTH_SHORT).show();
                }else if(s.equals("filed")) {
                    Toast.makeText(RegisterActivity.this, "更新密码失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 注册时连接服务
     */
    private void registerConnInter(){
        Map<String,String> params =new HashMap<>();
        params.put("phoneNumber", S_user);
        params.put("password", S_password);
        String url = Url.url("androidUser/add");
        System.out.println(url);
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
                String s = jsonObject.getString("add");
                if (s.equals("success")){
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    loginConnection = new LoginConnection(RegisterActivity.this);
                    loginConnection.connInter(S_user,S_password);
                }else {
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(RegisterActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

}

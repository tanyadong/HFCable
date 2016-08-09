package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Txt_tel_fragment_user,Txt_tel_fragment_password,Txt_tel_fragment_password_sure,Txt_tel_fragment_verification_code;
    private TextView TView_tel_fragment_get_verification_code;
    private String S_user,S_password,S_password_sure,S_verificationCode,S_w;
    private Button Btn_register;
    private LoginConnection loginConnection;
    private Dialog dialog;
    private boolean SMSTag = false;

    private static final String APPKEY = "1395ca88614b6";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static final String APPSECRET = "8d76cf7037f4800ba0649619dd25ee06";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result" + result);
                // 短信验证成功后，操作
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                    //进行注册操作
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    SMSTag = true;
                    checkPhone();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 验证码发送成功
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }
            } else {
                ((Throwable) data).printStackTrace();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        if (status==603){
                            Toast.makeText(RegisterActivity.this, "输入的手机号不正确，请重新填写", Toast.LENGTH_SHORT).show();
                        }else if (status==466){
                            Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                        }else if (status==408||status==468){
                            Toast.makeText(RegisterActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                        }else if(status==477){
                            Toast.makeText(RegisterActivity.this, "当前手机号发送短信的数量超过限额，请稍后再进行注册", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);//初始化界面
        initSDK();
        //初始化界面
        initView();

        //初始化数据
        initValue();

        //初始化点击事件
        click();
    }


    /**
     * 初始化短信sdk
     */
    public void initSDK() {
        // 初始化短信SDK
        SMSSDK.initSDK(this, APPKEY, APPSECRET, false);
        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int arg0, int arg1, Object arg2) {
                Message msg = new Message();
                msg.arg1 = arg0;
                msg.arg2 = arg1;
                msg.obj = arg2;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);
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
        dialog = new Dialog(this);
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
        S_password_sure = this.Txt_tel_fragment_password_sure.getText().toString().trim();
        S_verificationCode = this.Txt_tel_fragment_verification_code.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //获取验证码
            case R.id.TView_tel_fragment_get_verification_code:
                getValues();
                SMSSDK.getVerificationCode("86",S_user);
                dialog.showDialog("验证码发送中");
                break;
            //进行注册
            case R.id.Btn_register:
                //注册或者找回密码
                getValues();
                if (isEmpty()){
                    Toast.makeText(RegisterActivity.this, "请完善全部信息", Toast.LENGTH_SHORT).show();
                }else if(!checkPhoneNumber()){
                    Toast.makeText(RegisterActivity.this,"手机号不正确，请重新填写",Toast.LENGTH_SHORT).show();
                }else if(!PasswordLength()){
                    Toast.makeText(RegisterActivity.this,"输入的密码太长或太短，请重新填写",Toast.LENGTH_SHORT).show();
                }else if(!surePassword()){
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不相同",Toast.LENGTH_SHORT).show();
                }
                if (!isEmpty()&&checkPhoneNumber()&&PasswordLength()&&surePassword()){
                    Toast.makeText(RegisterActivity.this,"信息完善", Toast.LENGTH_SHORT).show();
                    SMSSDK.submitVerificationCode("86",S_user,S_verificationCode);
                }
                break;
        }
    }

    /**
     * 判断输入的所有数据是否为空
     * @return true表示为空，false表示不为空
     */
    private boolean isEmpty(){
        return S_user.isEmpty()||S_password.isEmpty()||S_password_sure.isEmpty()||S_verificationCode.isEmpty();
    }

    /**
     * 验证验证码是否正确
     * @return true为正确，false为不正确
     */
    private boolean verificationCode(){
        return SMSTag;
    }

    /**
     * 验证手机号格式是否正确
     * @return true 表示正确，false表示不正确
     */
    private boolean checkPhoneNumber(){
        return CheckPhoneNumber.checkPhoneNum(S_user);
    }
    /**
     * 判断密码是否是6-16位
     */
    private boolean PasswordLength(){
        return S_password.length()>=6 && S_password.length()<=16;
    }

    /**
     * 两次的密码是否一致
     * @return
     */
    public boolean surePassword(){
        return S_password.equals(S_password_sure);
    }

    /**
     * 检查手机号是否已经注册时连接服务
     */
    private void checkPhone(){
        if (S_w.equals("register")){
            //注册
            dialog.showDialog("正在注册...");
        }else if(S_w.equals("forget")){
            //找回密码
            dialog.showDialog("正在找回...");
        }
        Map<String,String> params =new HashMap<>();
        params.put("userName", S_user);
        String url = Url.url("/androidUser/exist");
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
                        dialog.cancle();
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
                        dialog.cancle();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();
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
        String url = Url.url("/androidUser/updatePassword");
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
                    dialog.cancle();
                    Toast.makeText(RegisterActivity.this, "更新密码成功", Toast.LENGTH_SHORT).show();
                }else if(s.equals("filed")) {
                    dialog.cancle();
                    Toast.makeText(RegisterActivity.this, "更新密码失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dialog.cancle();
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
        String url = Url.url("/androidUser/add");
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
                    dialog.cancle();
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    loginConnection = new LoginConnection(RegisterActivity.this);
                    loginConnection.connInter(S_user,S_password);
                }else {
                    dialog.cancle();
                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(RegisterActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            dialog.cancle();
        }
    };

}

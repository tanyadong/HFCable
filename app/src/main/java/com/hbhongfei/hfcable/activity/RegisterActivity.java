package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Txt_tel_fragment_user,Txt_tel_fragment_password,Txt_tel_fragment_password_sure,Txt_tel_fragment_verification_code;
    private TextView TView_tel_fragment_get_verification_code;
    private String S_user,S_password,S_password_sure,S_verificationCode,S_w;
    private Button Btn_register;

    private static String APPKEY = "1395ca88614b6";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "8d76cf7037f4800ba0649619dd25ee06";
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

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 已经验证
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }

            } else {
                ((Throwable) data).printStackTrace();
//                Toast.makeText(RegisterActivity.this, "验证码错误",
//                        Toast.LENGTH_SHORT).show();
                int status = 0;
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    status = object.optInt("status");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(RegisterActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }

        };
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
        Btn_register.setOnClickListener(this);
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

                SMSSDK.getVerificationCode("86",S_user);
//                getVerificationCode(S_user);
                break;
            //进行注册
            case R.id.Btn_register:
                if (S_w.equals("register")){
                    //注册
                    getValues();
                    SMSSDK.submitVerificationCode("86",S_user,S_verificationCode);

                }else if(S_w.equals("forget")){
                    //找回密码
                    getValues();
                    SMSSDK.submitVerificationCode("86",S_user,S_verificationCode);
                }
                break;
        }
    }



}

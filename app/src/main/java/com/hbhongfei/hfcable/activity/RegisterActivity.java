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

import com.hbhongfei.hfcable.R;

import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText Txt_tel_fragment_user,Txt_tel_fragment_password,Txt_tel_fragment_password_sure,Txt_tel_fragment_verification_code;
    private TextView TView_tel_fragment_get_verification_code;
    private String S_user,S_password,S_password_sure,S_verificationCode,S_w;
    private Button Btn_register;

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

                }else if(S_w.equals("forget")){
                    //找回密码
                }
                break;
        }
    }

    /**
     * 获取验证码
     * @param S_tel
     */
    private void getVerificationCode(String S_tel){

    }

}

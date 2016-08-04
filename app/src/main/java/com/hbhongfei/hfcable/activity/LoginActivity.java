package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.LoginConnection;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText Txt_login_user,Txt_login_password;
    private TextView Txt_login_forget_password,Txt_login_sign_in;
    private Button Btn_login_login;
    private String S_user,S_password;
    private boolean Tag =true;
    private LoginConnection loginConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化界面
        initView();

        //初始化点击事件
        click();
    }


    /**
     * 初始化界面
     */
    private void initView(){
        this.Txt_login_user = (EditText) findViewById(R.id.Txt_login_user);
        this.Txt_login_password = (EditText) findViewById(R.id.Txt_login_password);
        this.Txt_login_forget_password = (TextView) findViewById(R.id.Txt_login_forget_password);
        this.Txt_login_sign_in = (TextView) findViewById(R.id.Txt_login_sign_in);
        this.Btn_login_login = (Button) findViewById(R.id.Btn_login_login);
    }

    /**
     * 初始化点击事件
     */
    private void click(){
        this.Txt_login_forget_password.setOnClickListener(this);
        this.Txt_login_sign_in.setOnClickListener(this);
        this.Btn_login_login.setOnClickListener(this);
    }

    /**
     * 获取数据
     */
    private void getValues(){
        this.S_user = this.Txt_login_user.getText().toString().trim();
        this.S_password = this.Txt_login_password.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //忘记密码
            case R.id.Txt_login_forget_password:
                forgetPwdClick();
                break;
            //注册
            case R.id.Txt_login_sign_in:
                registerClick();
                break;
            //登录
            case R.id.Btn_login_login:
                getValues();
                loginClick();
                break;
        }
    }

    /**
     * 点击忘记密码
     */
    private void forgetPwdClick(){
        Intent intent = new Intent();
        intent.putExtra("W","forget");
        intent.setClass(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * 点击登录
     */
    private void loginClick(){
        if (isEmpty()){
            loginConnection = new LoginConnection(LoginActivity.this);
            loginConnection.connInter(S_user,S_password);
        }
    }

    /**
     * 验证是否为空
     */
    private boolean isEmpty(){
        if (TextUtils.isEmpty(S_user)){
            Toast.makeText(LoginActivity.this,"请填写账号", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else if(TextUtils.isEmpty(S_password)){
            Toast.makeText(LoginActivity.this,"请填写密码", Toast.LENGTH_SHORT).show();
            Tag = false;
        }else {
            Tag =true;
        }
        return Tag;
    }


    /**
     * 点击注册
     */
    private void registerClick(){
        Intent intent = new Intent();
        intent.putExtra("W","register");
        intent.setClass(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

}

package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;

public class MyPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText pwd_before,pwd_update,pwd_sure;
    private String S_before,S_update,S_sure;
    private ImageView cancel,done;
    private Boolean tag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_password);
        toolBar();
        initView();
        setOnclick();
    }

    /**
     * Toast
     * @param s 要显示的内容
     */
    private void toast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    /**
     * 展示toolbar
     */
    private void toolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    /**
     * 初始化界面
     */
    private void initView(){
        pwd_before = (EditText) findViewById(R.id.Etext_myPassword_before);
        pwd_update = (EditText) findViewById(R.id.Etext_myPassword_update);
        pwd_sure = (EditText) findViewById(R.id.Etext_myPassword_sure);
        cancel = (ImageView) findViewById(R.id.Iamge_myPassword_cancel);
        done = (ImageView) findViewById(R.id.Iamge_myPassword_done);
    }

    /**
     * 获取数据
     */
    private void getValues(){
        S_before = pwd_before.getText().toString().trim();
        S_update = pwd_update.getText().toString().trim();
        S_sure = pwd_sure.getText().toString().trim();
    }

    /**
     * 设置点击事件
     */
    private void setOnclick(){
        cancel.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    /**
     * 校验数据
     * 进行简单的校验，非空之类,新密码是相同，原密码是否正确
     */
    private boolean checkValues(){
        //先获取数据
        getValues();
        //校验
        if (TextUtils.isEmpty(S_before)){
            toast("原密码为空，请填写完全");
            tag =false;
        }else if (TextUtils.isEmpty(S_update)){
            toast("新密码为空,请填写完全");
            tag =false;
        }else if (TextUtils.isEmpty(S_sure)){
            toast("确认密码为空,请填写完全");
            tag =false;
        }else if(!passwordBefore()){
            toast("原密码不正确");
            setValuesEmpty();
            tag =false;
        }else if (!S_update.equals(S_sure)){
            toast("密码不一致，请重新填写");
            setValuesEmpty();
            tag =false;
        }else {
            tag = true;
        }
        return tag;
    }

    /**
     * 将密码置空
     */
    private void setValuesEmpty(){
        pwd_before.setText("");
        pwd_update.setText("");
        pwd_sure.setText("");

    }

    /**
     * 验证原密码是否相同
     */
    private boolean passwordBefore(){
        return  true;
    }

    /**
     * 界面跳转事件
     * @param c 要跳转的界面的class
     */
    private void intent(Class c){
        Intent i = new Intent();
        i.setClass(MyPasswordActivity.this,c);
        startActivity(i);
    }

    /**
     * 将数据进行保存
     */
    private void saveValues(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Iamge_myPassword_cancel:
                intent(MyInfoActivity.class);
                this.finish();
                break;
            case R.id.Iamge_myPassword_done:
                if (checkValues()){
                    toast("信息保存成功");
                    saveValues();
                    intent(MyInfoActivity.class);
                    this.finish();
                }
                break;
        }
    }
}

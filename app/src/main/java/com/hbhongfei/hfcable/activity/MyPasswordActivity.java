package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.HintTestSize;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText pwd_before, pwd_update, pwd_sure;
    private String S_before, S_update, S_sure,S_phoneNumber;
    private ImageView cancel, done;
    private Boolean tag = true;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_password);
        toolBar();
        initView();
        setOnclick();
    }

    @Override
    protected void onResume() {
        initValues();
        super.onResume();
    }

    /**
     * Toast
     *
     * @param s 要显示的内容
     */
    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myPassword_toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        dialog = new Dialog(this);
        pwd_before = (EditText) findViewById(R.id.Etext_myPassword_before);
        pwd_update = (EditText) findViewById(R.id.Etext_myPassword_update);
        pwd_sure = (EditText) findViewById(R.id.Etext_myPassword_sure);
        cancel = (ImageView) findViewById(R.id.Image_myPassword_cancel);
        done = (ImageView) findViewById(R.id.Image_myPassword_done);
        HintTestSize hintTestSize=new HintTestSize(pwd_before,"原密码：");
        hintTestSize.setHintTextSize();
        HintTestSize hintTestSize1=new HintTestSize(pwd_update,"请设置6-20位新密码：");
        hintTestSize1.setHintTextSize();
        HintTestSize hintTestSize2=new HintTestSize(pwd_sure,"确认密码：");
        hintTestSize2.setHintTextSize();
    }

    /**
     * 初始化数据
     */
    private void initValues(){
        Intent intent = getIntent();
        S_phoneNumber = intent.getStringExtra("phoneNumber");
    }
    /**
     * 获取数据
     */
    private void getValues() {
        S_before = pwd_before.getText().toString().trim();
        S_update = pwd_update.getText().toString().trim();
        S_sure = pwd_sure.getText().toString().trim();
    }

    /**
     * 设置点击事件
     */
    private void setOnclick() {
        cancel.setOnClickListener(this);
        done.setOnClickListener(this);
    }

    /**
     * 校验数据
     * 进行简单的校验，非空之类,新密码是相同，原密码是否正确
     */
    private boolean checkValues() {
        //先获取数据
        getValues();
        //校验
        if (TextUtils.isEmpty(S_before)) {
            toast("原密码为空，请填写完全");
            tag = false;
        } else if (TextUtils.isEmpty(S_update)) {
            toast("新密码为空,请填写完全");
            tag = false;
        } else if (TextUtils.isEmpty(S_sure)) {
            toast("确认密码为空,请填写完全");
            tag = false;
        } else if (!passwordBefore()) {
            toast("原密码不正确");
            setValuesEmpty();
            tag = false;
        } else if (!S_update.equals(S_sure)) {
            toast("密码不一致，请重新填写");
            setValuesEmpty();
            tag = false;
        }else if(!(S_update.length()>=6 && S_update.length()<=16)){
            toast("输入的密码太长或太短，请重新填写");
            setValuesEmpty();
            tag = false;
        } else{
            tag = true;
        }
        return tag;
    }

    /**
     * 将密码置空
     */
    private void setValuesEmpty() {
        pwd_before.setText("");
        pwd_update.setText("");
        pwd_sure.setText("");

    }

    /**
     * 验证原密码是否相同
     */
    private boolean passwordBefore() {
        return true;
    }

    /**
     * 界面跳转事件
     *
     */
    private void intent() {
        Intent i = new Intent();
        i.putExtra("back","back");
        setResult(1,i);
        this.finish();
    }
    @Override
    public void onBackPressed() {
        //数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("back", "back");
        //设置返回数据
        this.setResult(RESULT_OK, intent);
        //关闭Activity
        this.finish();
    }

    /**
     * 修改密码时连接服务
     */
    private void saveValues() {
        dialog.showDialog("正在保存...");
        Map<String,String> params =new HashMap<>();
        params.put("phoneNumber", S_phoneNumber);
        params.put("password", S_before);
        params.put("password1", S_update);
        String url = Url.url("/androidUser/updatePassword1");
        System.out.println(url);
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,updateListener,errorListener, params);
        mQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Image_myPassword_cancel:
                intent();
                break;
            case R.id.Image_myPassword_done:
                if (checkValues()) {
                    saveValues();
                }
                break;
        }
    }

    /**
     * 找回密码时连接服务
     * 成功的监听器
     */
    private Response.Listener<JSONObject> updateListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("updatePassword1");
                if (s.equals("success")){
                    dialog.cancle();
                    Toast.makeText(MyPasswordActivity.this, "更新密码成功", Toast.LENGTH_SHORT).show();
                    intent();
                }else if(s.equals("filed")) {
                    dialog.cancle();
                    Toast.makeText(MyPasswordActivity.this, "更新密码失败", Toast.LENGTH_SHORT).show();
                    setValuesEmpty();
                }else if(s.equals("noUser")){
                    dialog.cancle();
                    Toast.makeText(MyPasswordActivity.this, "输入的原密码不正确", Toast.LENGTH_SHORT).show();
                    setValuesEmpty();

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
            Toast.makeText(MyPasswordActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            dialog.cancle();
        }
    };

}

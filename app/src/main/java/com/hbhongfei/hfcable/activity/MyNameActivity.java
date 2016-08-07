package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.HintTestSize;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyNameActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText myName;
    private TextView number;
    private String S_name,S_phoneNumber;
    private ImageView cancel, done;
    private static final String USER = LoginConnection.USER;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_name);
        toolBar();
        initView();
        count();
        setOnClick();
        initValue();
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.myName_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        dialog = new Dialog(this);
        myName = (EditText) findViewById(R.id.Etext_myName_text);
        number = (TextView) findViewById(R.id.Tview_myName_number);
        cancel = (ImageView) findViewById(R.id.Image_myName_cancel);
        done = (ImageView) findViewById(R.id.Image_myName_done);
        HintTestSize hintTestSize=new HintTestSize(myName,"请输入您的昵称：");
        hintTestSize.setHintTextSize();
    }

    /**
     * 统计用户输入的字符数，并显示
     */
    private void count() {
        myName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                getValues();
                number.setText("" + S_name.length());
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initValue(){
        Intent intent = getIntent();
        S_name = intent.getStringExtra("nickName");
        S_phoneNumber = intent.getStringExtra("phoneNumber");
    }

    /**
     * 获取数据
     */
    private void getValues() {
        S_name = myName.getText().toString().trim();
    }

    /**
     * 设置点击事件
     */
    private void setOnClick() {
        this.cancel.setOnClickListener(this);
        this.done.setOnClickListener(this);
    }

    /**
     * 界面跳转事件
     *
     * @param c 要跳转的界面的class
     */
    private void intent(Class c) {
        Intent i = new Intent();
        i.putExtra("back","back");
        setResult(2,i);
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
     * 检查数据
     *
     * @return 数据是否则正确
     */
    private boolean checkValues() {
        //获取数据
        getValues();
        if (TextUtils.isEmpty(S_name)) {
            toast("昵称不能为空，请输入");
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Image_myName_cancel:
                intent(MyInfoActivity.class);
                break;
            case R.id.Image_myName_done:
                if (checkValues()) {
                    saveValues();
                }
                break;
        }
    }


    /**
     * 将数据进行保存
     * 修改昵称时进行连接服务
     */
    private void saveValues() {
        dialog.showDialog("正在保存...");
        Map<String,String> params =new HashMap<>();
        params.put("nickName", S_name);
        params.put("phoneNumber", S_phoneNumber);
        String url = Url.url("androidUser/updateNickName");
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
                String s = jsonObject.getString("updateNickName");
                if (s.equals("success")){
                    SharedPreferences.Editor editor = getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
                    editor.putString("nickName", S_name);
                    editor.apply();
                    Intent i = new Intent();
                    i.putExtra("nickName",S_name);
                    setResult(0,i);
                    dialog.cancle();
                    finish();
                }else {
                    Toast.makeText(MyNameActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
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
            Toast.makeText(MyNameActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            dialog.cancle();
        }
    };
}

package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;
import com.hbhongfei.hfcable.utils.WheelView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout myInfo_name,myInfo_sex,myInfo_password;
    private TextView sex,name,loginOut;
    private String S_sex = "男",S_name,S_phoneNumber;
    private static final String[] SEX = new String[]{"男", "女"};
    private static final String USER = LoginConnection.USER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initView();
        setOnClick();
        toolBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MyInfoActivity.this, "onPostResume", Toast.LENGTH_SHORT).show();
        initValues();
    }

    private void toolBar(){
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(true);
    }

    /**
     * 初始化界面
     */
    private void initView(){
        myInfo_name = (RelativeLayout) findViewById(R.id.Rlayout_myInfo_name);
        myInfo_sex = (RelativeLayout) findViewById(R.id.Rlayout_myInfo_sex);
        myInfo_password = (RelativeLayout) findViewById(R.id.Rlayout_myInfo_password);
        sex = (TextView) findViewById(R.id.Tview_myInfo_sex);
        name = (TextView) findViewById(R.id.Tview_myInfo_name);
        loginOut = (TextView) findViewById(R.id.Tview_myInfo_login_out);
    }

    /**
     * 设置点击事件
     */
    private void setOnClick(){
        this.myInfo_name.setOnClickListener(this);
        this.myInfo_sex.setOnClickListener(this);
        this.myInfo_password.setOnClickListener(this);
        this.loginOut.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    private void initValues(){
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_name = spf.getString("nickName", null);
        S_sex = spf.getString("sex",null);
        S_phoneNumber = spf.getString("phoneNumber",null);
        sex.setText(S_sex);
        name.setText(S_name);
    }


    /**
     * 退出登录
     */
    private void loginOut(){
        SharedPreferences spf = this.getSharedPreferences(USER, Context.MODE_PRIVATE);
        spf.edit().clear().apply();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.Rlayout_myInfo_name:
                intent.putExtra("phoneNumber",S_phoneNumber);
                intent.putExtra("nickName",S_name);
                intent.setClass(MyInfoActivity.this,MyNameActivity.class);
                startActivityForResult(intent,0);
                break;
            case R.id.Rlayout_myInfo_sex:
                showPopwindow();
                break;
            case R.id.Rlayout_myInfo_password:
                intent.putExtra("phoneNumber",S_phoneNumber);
                intent.setClass(MyInfoActivity.this,MyPasswordActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.Tview_myInfo_login_out:
                loginOut();
                intent.setClass(MyInfoActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode){
            case 0:
                //来自用户名
                S_name = data.getStringExtra("nickName");
                name.setText(S_name);
                break;
            case 1:
                System.out.println("password");
                break;
        }
        String result = data.getExtras().getString("result");//得到新Activity 关闭后返回的数据
    }

    /**
     *  显示popWindow
     */
    private void showPopwindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sex_selector, null);

        WheelView wva = (WheelView) view.findViewById(R.id.w);

        wva.setOffset(1);
        wva.setItems(Arrays.asList(SEX));
        //选择事件
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                S_sex = item;
            }
        });

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFFFFFF);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setBackgroundDrawable(getDrawable(R.color.colorWhite));
        }else{
            window.setBackgroundDrawable(dw);
        }

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(MyInfoActivity.this.findViewById(R.id.Rlayout_myInfo_sex),Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        ImageView done = (ImageView) view.findViewById(R.id.Image_sex_done);
        ImageView close = (ImageView) view.findViewById(R.id.Image_sex_close);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveSex();
                window.dismiss();//关闭
            }
        });
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                window.dismiss();//关闭
            }
        });

        /*//popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });*/
    }

    /**
     * 将数据进行保存
     * 修改性别时进行连接服务
     */
    private void saveSex() {
        Map<String,String> params =new HashMap<>();
        params.put("sex", S_sex);
        params.put("phoneNumber", S_phoneNumber);
        String url = Url.url("androidUser/updateSex");
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
                String s = jsonObject.getString("updateSex");
                if (s.equals("success")){
                    Toast.makeText(MyInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
                    editor.putString("sex", S_sex);
                    editor.apply();
                    sex.setText(S_sex);
                }else {
                    Toast.makeText(MyInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MyInfoActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };
}

package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 苑雪元 on 2016/8/4.
 */
public class LoginConnection {

    private Context context;
    private int i=-1;
    private String S_user,S_password,S_id;
    public static final String USER = "hfcable_user";

    public LoginConnection(Context context){
        this.context = context;
    }

    /**
     * 保存数据
     */
    private void saveValues(String user,String password,String id){
        SharedPreferences.Editor editor = context.getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
        editor.putString("userName", user);
        editor.putString("password", password);
        editor.putString("id", id);
        editor.apply();
    }

    /**
     * 连接服务
     */
    public void connInter(String S_user,String S_password){
        Map<String,String> params =new HashMap<>();
        params.put("userName", S_user);
        params.put("password", S_password);
        String url = Url.url("androidUser/login");
        System.out.println(url);
        RequestQueue mQueue = Volley.newRequestQueue(this.context);

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
                String s = jsonObject.getString("login");
                JSONObject userInfo = jsonObject.getJSONObject("user");
                if (s.equals("success")){
                    S_user = userInfo.getString("phoneNumber");
                    S_password = userInfo.getString("password");
                    S_id = userInfo.getString("id");
                    saveValues(S_user,S_password,S_id);
                    showDialog();
                }else if(s.equals("filed")){
                    Toast.makeText(context, "账号密码错误", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

    /**
     * 显示dialog
     */
    public void showDialog(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在登录....");
        pDialog.show();
        pDialog.setCancelable(false);
        new CountDownTimer(800 * 7, 800) {
            public void onTick(long millisUntilFinished) {
                i++;
                switch (i){
                    case 0:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.success_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                i = -1;
                pDialog.setTitleText("登录成功!")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                Intent intent  = new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
        }.start();
    }

}

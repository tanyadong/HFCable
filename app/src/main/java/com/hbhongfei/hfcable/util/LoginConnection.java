package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 苑雪元 on 2016/8/4.
 */
public class LoginConnection {

    private Activity context;
    private int i=-1;
    private String S_phoneNumber,S_id,S_sex,S_nickName,S_headPortrait;
    public static final String USER = "hfcable_user";
    private Dialog dialog;
    Intent intent;

    public LoginConnection(Activity context){
        this.context = context;

    }

    /**
     * 保存数据
     */
    private void saveValues(){
        SharedPreferences.Editor editor = context.getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
        editor.putString("phoneNumber", S_phoneNumber);
        editor.putString("id", S_id);
        editor.putString("sex", S_sex);
        editor.putString("nickName", S_nickName);
        editor.putString("headPortrait", S_headPortrait);
        editor.apply();
    }

    /**
     * 连接服务
     */
    public void connInter(String S_user,String S_password){
        dialog = new Dialog(this.context);
        dialog.showDialog("正在登录中...");
        Map<String,String> params =new HashMap<>();
        params.put("userName", S_user);
        params.put("password", S_password);
        String url = Url.url("/androidUser/login");
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
                if (s.equals("success")){
                    JSONObject userInfo = jsonObject.getJSONObject("user");
                    S_phoneNumber = userInfo.getString("phoneNumber");
                    S_id = userInfo.getString("id");
                    S_sex = userInfo.getString("sex");
                    S_nickName = userInfo.getString("nickName");
                    S_headPortrait = userInfo.getString("headPortrait");
                    saveValues();
                    //dialog消失
                   dialog.cancle();
                    //跳转界面

                    Intent intent  = new Intent(context,MainActivity.class);
                    context.startActivity(intent);
                }else if(s.equals("filed")){
                    Toast.makeText(context, "账号密码错误", Toast.LENGTH_SHORT).show();
                    //dialog消失
                    dialog.cancle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                //dialog消失
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
            Toast.makeText(context,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            //dialog消失
            dialog.cancle();
        }
    };



}

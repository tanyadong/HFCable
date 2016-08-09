package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.util.NormalPostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 谭亚东 on 2016/8/7.
 * 上欻图片文件
 */
public class UpLoadImage {
    private Context context;
    private Dialog dialog;
    private String imageUrl;
    private static final String USER = LoginConnection.USER;
    public UpLoadImage(Context context) {
        this.context = context;
    }

    public void  load(Bitmap photodata,String phone) {
        dialog = new Dialog(this.context);
        dialog.showDialog("上传图片中...");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
            photodata.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.close();
            byte[] buffer = baos.toByteArray();
            System.out.println("图片的大小："+buffer.length);

            //将图片的字节流数据加密成base64字符输出
            String photo = Base64.encodeToString(buffer, 0, buffer.length,Base64.DEFAULT);
            String url = Url.url("/androidUser/imageUpload");
            RequestQueue queue = Volley.newRequestQueue(context);
            Map<String, String> map = new HashMap<>();
            map.put("photo", photo);
            map.put("phoneNumber",phone);
            NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectProductListener, errorListener, map);
            queue.add(normalPostRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectProductListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("updateImage");
                if (s.equals("success")){
                    imageUrl = jsonObject.getString("imageUrl");
                    SharedPreferences.Editor editor = context.getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
                    editor.putString("headPortrait", imageUrl);
                    editor.apply();
                    Toast.makeText(context, "更新照片成功", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }else {
                    Toast.makeText(context, "更新照片失败", Toast.LENGTH_SHORT).show();
                    dialog.cancle();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     * 失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(context, "请求数据失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
            dialog.cancle();
        }
    };

}
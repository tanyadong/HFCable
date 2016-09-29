package com.hbhongfei.hfcable.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NormalPostRequest;

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

    public UpLoadImage(Context context) {
        this.context = context;
    }

    public void load(Bitmap photodata) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
            photodata.compress(Bitmap.CompressFormat.PNG, 100, baos);
            baos.close();
            byte[] buffer = baos.toByteArray();
            System.out.println("图片的大小："+buffer.length);

            //将图片的字节流数据加密成base64字符输出
            String photo = Base64.encodeToString(buffer, 0, buffer.length,Base64.DEFAULT);
//            photo=URLEncoder.encode(photo,"UTF-8");
            String url = "http://192.168.1.105:8080/HFCable/androidUser/getUserInfo";
            Map<String, String> map = new HashMap<>();
            map.put("photo", photo);
//            map.put("name", "woshishishi");
            NormalPostRequest normalPostRequest = new NormalPostRequest(url, jsonObjectProductListener, errorListener, map);
            MySingleton.getInstance(context).addToRequestQueue(normalPostRequest);
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
            Toast.makeText(context, "cehnggong", Toast.LENGTH_SHORT).show();
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
        }
    };

}
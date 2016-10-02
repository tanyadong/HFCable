package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.activity.MainActivity;
import com.hbhongfei.hfcable.activity.SplashActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 上传多个图片
 * Created by 苑雪元 on 2016/10/1.
 */
public class UploadImages {

    private Context context;
    private static RequestQueue mSingleQueue;

    public UploadImages(Context context){
        this.context = context;
    }

    public  void doUploadTest(List<String> list, String S_text, String S_phoneNumber, final Dialog dialog){
        mSingleQueue = Volley.newRequestQueue(context);
        String url = Url.url("/androidCableRing/save"); //换成自己的测试url地址
        Map<String, String> params = new HashMap<String, String>();
        params.put("content", S_text);
        params.put("userName", S_phoneNumber);
        List<File> fs = new ArrayList<File>();
        for (int i=0;i<list.size();i++){
            File f = new File(list.get(i));
            if(!f.exists()){
                Toast.makeText(context, "图片不存在，测试无效", Toast.LENGTH_SHORT).show();
                return;
            }
            fs.add(f);
        }

        MultipartRequest request = new MultipartRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                dialog.cancle();
                SplashActivity.ID = 4;
//                context.removeTempFromPref();
//                context.removeTextTempFromPref();
                Intent intent = new Intent(context,MainActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, "uploadSuccess,response = " + response, Toast.LENGTH_SHORT).show();
                Log.i("YanZi", "success,response = " + response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancle();
                Toast.makeText(context, "uploadError,response = " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.i("YanZi", "error,response = " + error.getMessage());
            }
        }, "f_file[]", fs, params); //注意这个key必须是f_file[],后面的[]不能少
        mSingleQueue.add(request);
    }

}

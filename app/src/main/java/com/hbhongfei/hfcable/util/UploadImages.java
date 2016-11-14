package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
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
    private static final int QUANLITY = 10;
    private boolean tag;
    private final String PATH = Environment.getExternalStorageDirectory().toString() + "/HFCable/picture";

    public UploadImages(Context context) {
        this.context = context;
    }

    public void doUploadTest(List<String> list, List<String> listThumbnail, String S_text, String S_phoneNumber, final Dialog dialog, boolean tag) {
        this.tag = tag;
        mSingleQueue = Volley.newRequestQueue(context);
        String url = Url.url("/androidCableRing/save"); //换成自己的测试url地址
        Map<String, String> params = new HashMap<String, String>();
        params.put("content", S_text);
        params.put("userName", S_phoneNumber);

        List<File> fs = new ArrayList<File>();
        int count=list.size();
        for (int i = 0; i < count; i++) {
            File file = new File(list.get(i));
            if (!file.exists()) {
                return;
            }

            //判断是否上传原图
            if (this.tag) {
                fs.add(file);
            } else {
                //压缩图片
                File f = new File(ZipImages.compressImage(list.get(i), list.get(i)
                        , QUANLITY));
                fs.add(f);
            }
        }

        MultipartRequest request = new MultipartRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                dialog.cancle();
                SplashActivity.ID = 4;
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, "发布成功", Toast.LENGTH_SHORT).show();
                Log.i("YanZi", "success,response = " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancle();
                Toast.makeText(context, "发布失败", Toast.LENGTH_SHORT).show();
                Log.i("YanZi", "error,response = " + error.getMessage());
            }
        }, "f_file[]", fs, params); //注意这个key必须是f_file[],后面的[]不能少
        mSingleQueue.add(request);
    }

}

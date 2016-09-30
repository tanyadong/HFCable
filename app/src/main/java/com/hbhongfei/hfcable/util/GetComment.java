package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ClearCacheRequest;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.activity.InputMyInfoActivity;
import com.hbhongfei.hfcable.activity.MainActivity;
import com.hbhongfei.hfcable.adapter.CommentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 苑雪元 on 2016/9/7.
 */
public class GetComment {


    private Context context;
    private String id;
    private ListView listView;
    private List<Map<String,Object>> list;
    private Map<String, Object> map;
    private CommentAdapter commentAdapter;
    RequestQueue mQueue;

    public GetComment(Context context,String id,ListView view){
        this.context = context;
        this.id = id;
        this.listView = view;
    }
    private RequestQueue getRequestQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
        File cacheDir = new File(context.getCacheDir(),"volley");
        DiskBasedCache cache = new DiskBasedCache(cacheDir);
        mQueue.start();

        // clear all volley caches.
        mQueue.add(new ClearCacheRequest(cache, null));
        return mQueue;
    }
    /**
     * 连接服务
     */
    public void connInter(){
        Map<String,String> params =new HashMap<>();
        params.put("id", this.id);
        String url = Url.url("/androidComment/getComment");
        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,jsonObjectListener,errorListener, params);
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            list = new ArrayList<>();
            try {
                if(jsonObject.getJSONArray("comments")!=null||!jsonObject.getJSONArray("comments").equals("null")){
                    JSONArray jsonArray = jsonObject.getJSONArray("comments");
                    for (int i=0;i<jsonArray.length();i++){
                        map = new HashMap<>();
                        JSONObject comment = jsonArray.getJSONObject(i);
                        //用户
                        JSONObject user = comment.getJSONObject("user");
                        //昵称
                        String nickName = user.getString("nickName");
                        map.put("nickName",nickName);
                        //评论
                        String commentContent = comment.getString("commentContent");
                        map.put("commentContent",commentContent);
                        //适配器
                        list.add(map);
                    }
                    commentAdapter = new CommentAdapter(context,list);
                    listView.setAdapter(commentAdapter);
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

}

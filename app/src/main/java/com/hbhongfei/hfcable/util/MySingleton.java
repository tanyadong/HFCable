package com.hbhongfei.hfcable.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by 谭亚东 on 2016/9/20.
 */
public class MySingleton {
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private static MySingleton mInstance;

    public MySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRuquestQueue();
    }

    //异步获取单实例
    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRuquestQueue() {
        if (mRequestQueue == null) {
            //getApplication()方法返回一个当前进程的全局应用上下文，这就意味着
            //它的使用情景为：你需要一个生命周期独立于当前上下文的全局上下文，
            //即就是它的存活时间绑定在进程中而不是当前某个组建。
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        File cacheDir = new File(mCtx.getCacheDir(), "volley");
        DiskBasedCache cache = new DiskBasedCache(cacheDir);
//        mRequestQueue.start();

        // clear all volley caches.
//        mRequestQueue.add(new ClearCacheRequest(cache, null));
        getRuquestQueue().add(req);
    }

    /**
     * 获取缓存数据
     * @param url
     * @return
     */
    public JSONObject getCache(String url){
        if(mRequestQueue.getCache().get(url)!=null){
            JSONObject jsonObject = null;
            try {
                jsonObject =  new JSONObject(new String(mRequestQueue.getCache().get(url).data).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }else{
            return null;
        }
    }

    public String getCacheString(String url){
        if(mRequestQueue.getCache().get(url)!=null){
            return new String(mRequestQueue.getCache().get(url).data).toString();
        }else{
            return null;
        }
    }

    /**
     * 清除缓存
     * @param url
     */
    public void clearCache(String url){
        mRequestQueue.getCache().remove(url);
    }


}

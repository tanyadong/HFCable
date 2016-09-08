package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.adapter.CommentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 保存评论
 * Created by 苑雪元 on 2016/9/7.
 */
public class SaveComment {


    private Context context;
    private String cableRingId;
    private String phone;
    private String commentContent;

    public SaveComment(Context context, String cableRingId, String phone,String commentContent){
        this.context = context;
        this.cableRingId = cableRingId;
        this.phone = phone;
        this.commentContent = commentContent;
    }
    /**
     * 连接服务
     */
    public void connInter(){
        Map<String,String> params =new HashMap<>();
        params.put("id", this.cableRingId);
        params.put("phoneNumber", this.phone);
        params.put("commentContent",this.commentContent);
        String url = Url.url("/androidComment/saveComment");
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
                String msg = jsonObject.getString("save");
                if (msg.equals("success")){
                    Toast.makeText(context,"评论成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"评论失败",Toast.LENGTH_SHORT).show();
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

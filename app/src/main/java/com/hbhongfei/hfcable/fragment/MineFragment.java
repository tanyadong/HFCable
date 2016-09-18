package com.hbhongfei.hfcable.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.CableRingAdapter;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private List<Map<String, Object>> list;
    private Map<String, Object> map;
    private Dialog dialog;
    private CableRingAdapter cableRingAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MineFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(v);
        setOnClick();
//        //初始化数据
        initValues();
        return v;
    }

    @Override
    public void onResume() {
//        initValues();
        super.onResume();
    }


    /**
     * 初始化界面
     * 苑雪元
     * 2016/07/21
     */
    public void initView(View v) {
        dialog = new Dialog(MineFragment.this.getActivity());
        mListView = (ListView) v.findViewById(R.id.ListView_cableZone);
        //下载刷新
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.SWRefresh_cableCZone);
        //更新
        refresh();

    }

    /**
     * 设置数据
     */
    private void refresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.colorGreen,R.color.colorRed,R.color.colorBalck,R.color.colorBlue);
        swipeRefreshLayout.setProgressViewEndTarget(true, 100);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       shaftConnInter();
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(1);
                    }
                }).start();
            }
        });
    }
    //handler
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    swipeRefreshLayout.setRefreshing(false);
                    cableRingAdapter.notifyDataSetChanged();
                    //swipeRefreshLayout.setEnabled(false);
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 设置点击事件
     * 苑雪元
     * 2016/07/21
     */
    private void setOnClick() {
    }


    /**
     * 初始化数据
     * 苑雪元
     * 2016/07/21
     */
    private void initValues() {
        shaftConnInter();
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 获取产品种类服务
     */
    public void shaftConnInter() {
        String url = Url.url("/androidCableRing/list");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, shaftjsonObjectListener, errorListener);
        RequestQueue mQueue = Volley.newRequestQueue(MineFragment.this.getActivity());
        mQueue.add(jsonObjectRequest);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> shaftjsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            list = new ArrayList<>();
            try {
                jsonArray = jsonObject.getJSONArray("cableRings");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject cableRing = (JSONObject) jsonArray.getJSONObject(i);
                    String id = cableRing.getString("id");
                    map.put("id", id);
                    String content = cableRing.getString("content");
                    map.put("content", content);
                    Long createTime = cableRing.getLong("createTime");
                    map.put("createTime", createTime);

                    //图片
                    JSONArray ims=cableRing.getJSONArray("cableRingImages");
                    //有图片时加入到产品图片集合
                    ArrayList<String> images=new ArrayList<>();
                    if(ims.length()>0){
                        for(int j=0;j<ims.length();j++){
                            images.add((String) ims.get(j));
                        }
                    }
                    map.put("images", images);
                    JSONObject user = cableRing.getJSONObject("user");
                    String nickName = user.getString("nickName");
                    map.put("nickName", nickName);
                    String headPortrait = user.getString("headPortrait");
                    map.put("headPortrait", headPortrait);
                    list.add(map);
                }
                cableRingAdapter = new CableRingAdapter(MineFragment.this.getContext(), list);
                mListView.setAdapter(cableRingAdapter);
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
            Toast.makeText(MineFragment.this.getActivity(), "链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
//            dialog.cancle();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}

package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.SpinnerListAdapter;
import com.hbhongfei.hfcable.util.ConnectionTypeTwo;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.MySpinner;
import com.hbhongfei.hfcable.util.NetUtils;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

public class ProdectListActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate,IErrorOnclick {
    private LinearLayout prodectType_spinner;
    private TextView prodectType_textView;
    private ExpandableListView prodectList_listView;
    //下拉和分页框架

    private BGARefreshLayout mRefreshLayout;
    private String typeName;
    private int width;
    private int pageNo=1;//D当前页数
    ConnectionTypeTwo typtTwoConnection;
    private LinearLayout noInternet;
    private     Dialog dialog;
    private String url_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodect_list);
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        //初始化
        initView();
        initRefreshLayout();
        setValues();
        connInter();
    }

    public void initView(){
        prodectType_spinner = (LinearLayout) findViewById(R.id.prodectType_spinner);
        prodectType_textView = (TextView) findViewById(R.id.prodectType_textView);
        prodectList_listView= (ExpandableListView) findViewById(R.id.prodectlist_listView);
        noInternet = (LinearLayout) findViewById(R.id.no_internet_product_list);
    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout)findViewById(R.id.rl_expandable_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getApplication(),true);
        // 设置下拉刷新和上拉加载更多的风格
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载中");
    }




    /**
     * 连接服务
     * 展示当前用户管理任务连接服务
     */
    public void connInter(){
        dialog.showDialog("正在加载中...");
        url_type= Url.url("/androidType/getType");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url_type,null,jsonObjectListener,getTypeErrorListener);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
    private void analysisDataOfAddress(JSONObject jsonObject){
        JSONArray jsonArray;
        List<String> type_list=new ArrayList<>();
        try {
            jsonArray = jsonObject.getJSONArray("list");
            if (jsonArray.length()>0){
                type_list.add("全部");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = (JSONObject)jsonArray.getJSONObject(i);
                    String typeName=jsonObject1.getString("typeName");
                    type_list.add(typeName);
                }
                //点击事件
                onClickLisener(type_list);

            }else{
                Error.toSetting(noInternet, R.mipmap.nothing, "暂无数据哦", "换一个试试", new IErrorOnclick() {
                    @Override
                    public void errorClick() {
                        Toast.makeText(ProdectListActivity.this,"暂无数据",Toast.LENGTH_SHORT).show();
                    }
                });
            }
            dialog.cancle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     *获取种类成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            analysisDataOfAddress(jsonObject);
        }
    };

    /**
     *  获取种类失败的监听器
     */
    private Response.ErrorListener getTypeErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            dialog.cancle();
            MySingleton mySingleton = new MySingleton(ProdectListActivity.this);
            if (mySingleton.getCacheString(url_type)!=null){
                if(volleyError instanceof NoConnectionError){
                    Toast.makeText(ProdectListActivity.this,"没有网络",Toast.LENGTH_SHORT).show();
                }else if(volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError){
                    Toast.makeText(ProdectListActivity.this,"服务器端异常",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ProdectListActivity.this,"不好啦，出错啦",Toast.LENGTH_SHORT).show();
                }
                noInternet.setVisibility(View.GONE);
                analysisDataOfAddress(mySingleton.getCache(url_type));
            }else {
                if (volleyError instanceof NoConnectionError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "没有网络哦", "点击设置", ProdectListActivity.this);
                } else if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError) {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "大事不妙啦", "服务器出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            Toast.makeText(ProdectListActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "大事不妙啦", "出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                            Toast.makeText(ProdectListActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    };


    /**
     *  获取种类失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(ProdectListActivity.this, "数据请求失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };


    /**
     * 设置数据
     */
    public void setValues(){
        dialog=new Dialog(this);
        Intent intent=getIntent();
        typeName=intent.getStringExtra("typeName");
        prodectType_textView.setText(typeName);
        //获取屏幕宽度
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        try {
            typtTwoConnection=new ConnectionTypeTwo(ProdectListActivity.this,ProdectListActivity.this,prodectList_listView,noInternet);
            dialog.showDialog("正在加载中");
            typtTwoConnection.connInterByType(typeName,pageNo);
            dialog.cancle();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /**
     * 点击事件
     */
    public void onClickLisener(final List<String> list){
        prodectType_spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    MySpinner mySpinner = new MySpinner(ProdectListActivity.this, width, list);
                    mySpinner.showAsDropDown(prodectType_spinner, 0, 0);//显示在rl_spinner的下方
                    mySpinner.setOnItemClickListener(new SpinnerListAdapter.onItemClickListener() {
                        @Override
                        public void click(int position, View view) {
                            if(NetUtils.isConnected(ProdectListActivity.this)) {
                                typeName = list.get(position);
                                prodectType_textView.setText(list.get(position));
                                try {
                                    pageNo = 1;
                                    dialog.showDialog("正在加载中");
                                    typtTwoConnection.connInterByType(list.get(position), pageNo);
                                    dialog.cancle();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                dialog.cancle();
                                Toast.makeText(ProdectListActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        });
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        pageNo=1;
        if(NetUtils.isConnected(this)){
            new MyAsyncTack().execute();
        }else{
            Toast.makeText(this,"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(NetUtils.isConnected(this)) {
            if (pageNo <typtTwoConnection.totalCount ) {
                pageNo++;
                new MyAsyncTack().execute();
            } else {
                mRefreshLayout.endLoadingMore();
                return false;
            }
        }else{
            Toast.makeText(this,"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void errorClick() {
        NetUtils.openSetting(this);
    }

    class MyAsyncTack extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                typtTwoConnection.connInterByType(typeName,pageNo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRefreshLayout.endLoadingMore();
            mRefreshLayout.endRefreshing();
            super.onPostExecute(aVoid);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

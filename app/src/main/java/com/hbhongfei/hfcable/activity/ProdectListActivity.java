package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.SpinnerListAdapter;
import com.hbhongfei.hfcable.util.ConnectionTypeTwo;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.MySpinner;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdectListActivity extends AppCompatActivity implements View.OnClickListener,AbsListView.OnScrollListener {
    private LinearLayout prodectType_spinner;
    private TextView prodectType_textView;
    private ExpandableListView prodectList_listView;
    private String typeName;
    private int width;
    private RequestQueue mQueue;
    private Dialog dialog;
    private boolean isLastRow=false;
    private View footer;
    private TextView loading;
    private int count=0;
    private int pageNo=1;//D当前页数
    ConnectionTypeTwo typtTwoConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodect_list);
        mQueue = Volley.newRequestQueue(this);
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        //初始化
        initView();
        setValues();
        connInter();
        getTotolCount();//总页数
        click();

//        onClickLisener();
    }
    Handler mMandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                count=msg.arg1;
            }
        }
    };
    public void initView(){
        prodectType_spinner = (LinearLayout) findViewById(R.id.prodectType_spinner);
        prodectType_textView = (TextView) findViewById(R.id.prodectType_textView);
        prodectList_listView= (ExpandableListView) findViewById(R.id.prodectlist_listView);
        footer= LayoutInflater.from(this).inflate(R.layout.footer,null);
        loading= (TextView) footer.findViewById(R.id.id);
    }
    /**
     * 获得总页数
     */
    private void getTotolCount() {
        String url = Url.url("/androidTypeTwo/getTotalRecord");
        Map<String,String> map=new HashMap<>();
        map.put("typeName",typeName);
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,jsonObjectProductListener,errorListener,map);
        mQueue.add(normalPostRequest);
    }

    /**
     * 成功的监听器
     * 返回的是产品种类
     */
    private Response.Listener<JSONObject> jsonObjectProductListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                JSONObject object=jsonObject.getJSONObject("page");
                Message message=new Message();
                message.what=0;
                message.arg1=object.getInt("totalPages");

                mMandler.sendMessage(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public void click(){
//        prodectList_listView.setOnPageLoadListener(this);
        prodectList_listView.setOnScrollListener(this);
    }

    /**
     * 连接服务
    /**
     * 展示当前用户管理任务连接服务
     */
    public void connInter(){
        String url = Url.url("/androidType/getType");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,
                jsonObjectListener,errorListener);
        mQueue.add(jsonObjectRequest);
    }

    /**
     *获取种类成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            List<String> type_list=new ArrayList<>();
            try {
                jsonArray = jsonObject.getJSONArray("list");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = (JSONObject)jsonArray.getJSONObject(i);
                    String typeName=jsonObject1.getString("typeName");
                    type_list.add(typeName);
                }
                //点击事件
               onClickLisener(type_list);
            } catch (JSONException e) {
                e.printStackTrace();
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
        Intent intent=getIntent();
        typeName=intent.getStringExtra("typeName");
        prodectType_textView.setText(typeName);
        //获取屏幕宽度
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        try {
            typtTwoConnection=new ConnectionTypeTwo(ProdectListActivity.this,prodectList_listView);
            typtTwoConnection.connInterByType(typeName,pageNo);
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
                        typeName=list.get(position);
                        prodectType_textView.setText(list.get(position));
                        try {
                            typtTwoConnection.connInterByType(list.get(position),pageNo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

//
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE &&pageNo<count){
            //动态加载数据
            Toast.makeText(this,"aaaa"+count,Toast.LENGTH_SHORT).show();
            loading.setVisibility(View.VISIBLE);
            new LoadDataThread().start();
            prodectList_listView.addFooterView(footer);
        }
//        isLastRow=false;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }
    }

//    @Override
//    public void onPageChanging(int pageIndex) {
//        //            typtTwoConnection.connInterByType(typeName,pageNo);
//        new LoadDataThread().start();
//    }
//
//    @Override
//    public boolean canLoadData() {
//        return true;
//    }


    /**
     * 动态加载数据
     */
    private class LoadDataThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                pageNo++;
                typtTwoConnection.connInterByType(typeName,pageNo);
                prodectList_listView.removeFooterView(footer);
                Thread.sleep(1000);
                return;
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

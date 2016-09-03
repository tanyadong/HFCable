package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProdectListActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout prodectType_spinner;
    private TextView prodectType_textView;
    private ExpandableListView prodectList_listView;
    private String typeName;
    private int width;
    private RequestQueue mQueue;
    private Dialog dialog;
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
//        onClickLisener();
    }

    public void initView(){
        prodectType_spinner = (LinearLayout) findViewById(R.id.prodectType_spinner);
        prodectType_textView = (TextView) findViewById(R.id.prodectType_textView);
        prodectList_listView= (ExpandableListView) findViewById(R.id.prodectlist_listView);

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
            ConnectionTypeTwo typtTwoConnection=new ConnectionTypeTwo(ProdectListActivity.this,prodectList_listView);
            typtTwoConnection.connInterByType(typeName);
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
                            ConnectionTypeTwo typtTwoConnection=new ConnectionTypeTwo(ProdectListActivity.this,prodectList_listView);
                            typtTwoConnection.connInterByType(list.get(position));
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


}

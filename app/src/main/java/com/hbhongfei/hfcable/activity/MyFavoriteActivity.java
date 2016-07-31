package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter;

import java.util.ArrayList;

public class MyFavoriteActivity extends AppCompatActivity {
    private ListView list_myFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        initView();
        setValues();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        list_myFavorite = (ListView) findViewById(R.id.listView_myFavorite);
    }

    private void setValues(){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("测试:"+i);
        }
        MyAdapter myAdapter = new MyAdapter(this,R.layout.intentionlayout,list);
        list_myFavorite.setAdapter(myAdapter);
    }
}

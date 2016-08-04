package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter;
import com.hbhongfei.hfcable.pojo.Product;

import java.util.ArrayList;
import java.util.List;

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
        List<Product> list = new ArrayList<Product>();

        MyAdapter myAdapter = new MyAdapter(this,R.layout.intentionlayout,list);
        list_myFavorite.setAdapter(myAdapter);
    }
}

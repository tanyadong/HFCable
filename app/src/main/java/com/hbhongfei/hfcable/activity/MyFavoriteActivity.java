package com.hbhongfei.hfcable.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter;
import com.hbhongfei.hfcable.adapter.MyAdapter_myShopping;

import java.util.ArrayList;
import java.util.List;

public class MyFavoriteActivity extends AppCompatActivity {
    private ListView list_myFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);

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

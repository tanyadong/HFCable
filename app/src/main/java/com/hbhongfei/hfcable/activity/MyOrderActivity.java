package com.hbhongfei.hfcable.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter_myShopping;

import java.util.ArrayList;

public class MyOrderActivity extends AppCompatActivity {

    private ListView list_myOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        initView();

        setValues();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        list_myOrder = (ListView) findViewById(R.id.listView_myOrder);
    }

    /**
     * 设置数据
     */
    private void setValues(){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("测试:"+i);
        }
        MyAdapter_myShopping myAdapter_myShopping = new MyAdapter_myShopping(this,R.layout.intention_my_shopping_layout,list);
        list_myOrder.setAdapter(myAdapter_myShopping);
    }
}

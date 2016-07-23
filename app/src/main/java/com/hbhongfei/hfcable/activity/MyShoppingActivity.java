package com.hbhongfei.hfcable.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter_myShopping;

import java.util.ArrayList;

public class MyShoppingActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView list_myShopping;
    private LinearLayout selectAll,selectAll1;
    private ImageView select,select1;
    private TextView money,account,accountNumber,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping);
        initView();
        setOnClick();
        setValues();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        list_myShopping = (ListView) findViewById(R.id.listView_myShopping);
        //默认状态下
        selectAll = (LinearLayout) findViewById(R.id.Layout_myShopping_selectAll);
        select = (ImageView) findViewById(R.id.Image_myShopping_select);
        money = (TextView) findViewById(R.id.Tview_mySHopping_money);
        account = (TextView) findViewById(R.id.Tview_myShopping_account);
        accountNumber = (TextView) findViewById(R.id.Tview_myShopping_accountNumber);
        //编辑状态下的全选
        selectAll1 = (LinearLayout) findViewById(R.id.Layout_myShopping_selectAll1);
        select1 = (ImageView) findViewById(R.id.Image_myShopping_select1);
        delete = (TextView) findViewById(R.id.Tview_myShopping_delete);
    }

    /**
     * 设置点击事件
     */
    private void setOnClick(){
        selectAll.setOnClickListener(this);
        selectAll1.setOnClickListener(this);
        account.setOnClickListener(this);
        delete.setOnClickListener(this);
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
        list_myShopping.setAdapter(myAdapter_myShopping);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Layout_myShopping_selectAll:
                break;
            case R.id.Layout_myShopping_selectAll1:
                break;
            case R.id.Tview_myShopping_account:
                break;
            case R.id.Tview_myShopping_delete:
                break;
        }
    }
}

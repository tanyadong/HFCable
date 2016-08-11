package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView name,location,telphone,money,commit;
    private ListView products;
    private List<Map<String,Object>> list;// 新建适配器 ，绑定数据
    private SimpleAdapter simpleAdapter;
    private String[] from = { "product_name", "introduce", "product_price", "product_num" };
    private int[] to = { R.id.Tview_confirm_order_product_name, R.id.Tview_confirm_order_product_introduce,R.id.Tview_confirm_order_product_price,R.id.Tview_confirm_order_product_num };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initView();

        setOnClick();

        setValues();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        this.name = (TextView) findViewById(R.id.Tview_confirm_order_name);
        this.location = (TextView) findViewById(R.id.Tview_confirm_order_location);
        this.telphone = (TextView) findViewById(R.id.Tview_confirm_order_tel);
        this.money = (TextView) findViewById(R.id.Tview_confirm_order_money);
        this.commit = (TextView) findViewById(R.id.Tview_confirm_order_commit);
        this.products = (ListView) findViewById(R.id.list_confirm_order_product);
    }

    private void setOnClick(){
        this.commit.setOnClickListener(this);
    }

    private void setValues(){
        list = new ArrayList<Map<String, Object>>();
        // 新增数据
        for (int i = 0; i < 20; i++) {
            Map map = new HashMap<String, Object>();
            //map放入两个键值对，键名与from对应，
            for (int j=0;j<to.length;j++){
                map.put(from[j],i+"");
            }
            //往list添加数据
            list.add(map);
        }

        simpleAdapter = new SimpleAdapter(this, list, R.layout.content_comfirm_order_products,from,to);

        products.setAdapter(simpleAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Tview_confirm_order_commit:
                //提交订单
                break;
        }
    }
}

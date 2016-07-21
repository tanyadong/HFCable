package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter;
import com.hbhongfei.hfcable.adapter.SpinnerListAdapter;
import com.hbhongfei.hfcable.util.MySpinner;

import java.util.ArrayList;

public class ProdectListActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout prodectType_spinner;
    private TextView prodectType_textView;
    private ListView prodectList_listView;
    private String array[],typeName;
    private int width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodect_list);
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        //初始化
        initView();
        setValues();
        onClickLisener();
    }

    public void initView(){
        prodectType_spinner = (LinearLayout) findViewById(R.id.prodectType_spinner);
        prodectType_textView = (TextView) findViewById(R.id.prodectType_textView);
        prodectList_listView= (ListView) findViewById(R.id.prodectlist_listView);
    }
    /**
     * 设置数据
     */
    public void setValues(){
        Intent intent=getIntent();
        typeName=intent.getStringExtra("typeName");
        array = new String[]{"item1", "item2", "item3", "item4", "item5", "item6"};
        prodectType_textView.setText(typeName);
        //获取屏幕宽度
        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();

        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            list.add("测试:"+i);
        }
        MyAdapter adapter = new MyAdapter(this,R.layout.intentionlayout,list);
        prodectList_listView.setDivider(null);
        prodectList_listView.setAdapter(adapter);
    }
    /**
     * 点击事件
     */
    public void onClickLisener(){
        prodectType_spinner.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prodectType_spinner:
                MySpinner mySpinner = new MySpinner(ProdectListActivity.this, width, array);
                mySpinner.showAsDropDown(prodectType_spinner, 0, 0);//显示在rl_spinner的下方
                mySpinner.setOnItemClickListener(new SpinnerListAdapter.onItemClickListener() {
                    @Override
                    public void click(int position, View view) {
                        prodectType_textView.setText(array[position]);
                    }
                });
            break;
        }
    }
}

package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.SpinnerListAdapter;

import java.util.List;

public class MySpinner extends PopupWindow implements AdapterView.OnItemClickListener {

    private List<String> type_list;
    private MySpinner mWindow;
    private SpinnerListAdapter.onItemClickListener mListener;
    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySpinner(Activity activity, int width, List<String> list){
        LayoutInflater inflater=activity.getLayoutInflater();
        View contentView=inflater.inflate(R.layout.myspinner_layout, null);
        // 设置PopupWindow的View 
        this.setContentView(contentView);
        // 设置PopupWindow弹出窗体的宽
        this.setWidth(width);
        // 设置PopupWindow弹出窗体的高  
        this.setHeight(android.view.WindowManager.LayoutParams.WRAP_CONTENT);  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);
        //设置点击窗口外边窗口消失
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 刷新状态  
        this.update();   
        // 实例化一个ColorDrawable颜色
//        ColorDrawable dw = new ColorDrawable(0xffffffff);
//        this.setBackgroundDrawable(dw);
//        this.list=arrayList;
        this.type_list=list;
        ListView listView=(ListView) contentView.findViewById(R.id.lv_list);
        mWindow=this;
        SpinnerListAdapter adapter=new SpinnerListAdapter(mWindow,activity, type_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        MySpinner.this.dismiss();
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
    }

    public void close(){
        this.dismiss();
    }

    public int position(){
        return 0;   
    }

    public void setOnItemClickListener(SpinnerListAdapter.onItemClickListener listener){
        this.mListener=listener;
    }

    public SpinnerListAdapter.onItemClickListener getListener(){
        //可以通过this的实例来获取设置好的listener
        return mListener;       
    }
}
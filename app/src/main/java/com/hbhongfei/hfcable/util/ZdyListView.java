package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;

public class ZdyListView extends ListView {
     private Context context;
     private View footer;
     private ProgressBar progressBar;
     private TextView tv;

     public ZdyListView(Context context) {
         super(context);
         this.context=context;
         init(context);
     }
     public ZdyListView(Context context, AttributeSet attrs) {
         super(context, attrs);
        init(context);
    }
    public ZdyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
         init(context);
     }
     public void init(Context context) {
         this.context=context;
         footer= LayoutInflater.from(context).inflate(R.layout.footer, null);
         progressBar=(ProgressBar) footer.findViewById(R.id.progressBar);
         tv=(TextView) footer.findViewById(R.id.loading_textview);
         this.addFooterView(footer);
    }

     //正在加载数据，将listview底部提示文字置为"正在加载中。。。。"
     public void onLoading(Context context){
            progressBar.setVisibility(VISIBLE);
            tv.setText("正在加载中。。。。");
    }

     //加载完毕，将listView底部提示文字改为"上拉加载更多"
     public void LoadingComplete(){
         progressBar.setVisibility(GONE);
         tv.setVisibility(GONE);
         this.removeFooterView(footer);
     }


//     //重写onMeasure，解决scrollview与listview冲突

            @Override
            public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
                super.onMeasure(widthMeasureSpec, expandSpec);
        }
}
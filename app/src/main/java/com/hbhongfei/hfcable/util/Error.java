package com.hbhongfei.hfcable.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;

/**
 * 没有网络显示，并且跳转到设置界面
 * Created by 苑雪元 on 2016/9/28.
 */
public class Error {


    public static void toSetting(VolleyError volleyError, LinearLayout noInternet,int img_item,String text_item1,String text_item2, final IErrorOnclick errorOnclick){

        ImageView img;
        TextView text1;
        TextView text2;
        if (volleyError instanceof com.android.volley.NoConnectionError){
            noInternet.setVisibility(View.VISIBLE);
            img = (ImageView) noInternet.findViewById(R.id.img_item);
            text1 = (TextView) noInternet.findViewById(R.id.text_item1);
            text2 = (TextView) noInternet.findViewById(R.id.text_item2);
            text1.setText(text_item1);
            text2.setText(text_item2);
            img.setImageResource(img_item);
            noInternet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    NetUtils.openSetting(activity);
                    //回调方法
                    errorOnclick.errorClick();

                }
            });
        }
    }

}

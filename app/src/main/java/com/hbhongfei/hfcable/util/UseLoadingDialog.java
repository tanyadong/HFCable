package com.hbhongfei.hfcable.util;

import android.content.Context;

/**
 * 使用自定义的加载dialog
 * Created by 苑雪元 on 2016/9/30.
 */
public class UseLoadingDialog {

    private Context context;

    public  UseLoadingDialog(Context context){
        this.context = context;
    }

    private LoadingDialog dialog;
    /**
     * 展示加载的dialog
     */
    public  void showDialog(){
        dialog = new LoadingDialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 取消dialog
     */
    public  void cancle(){
        dialog.dismiss();
    }

}

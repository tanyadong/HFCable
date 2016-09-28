package com.hbhongfei.hfcable.util;

import android.content.Context;

import com.hbhongfei.hfcable.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 苑雪元 on 2016/8/6.
 * dialog sweet_alert-dialog
 */
public class Dialog {

    private Context context;
    private SweetAlertDialog pDialog;

    public Dialog(Context cOntext){
        this.context = cOntext;
    }


    /**
     *展示dialog
     */
    public void showDialog(String text){
        pDialog = new SweetAlertDialog(this.context, SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText(text);
        pDialog.show();
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.red_btn_bg_pressed_color));
        //计时器
    }

    /**
     * dialog消失
     */
    public void cancle(){
        pDialog.cancel();
    }

}

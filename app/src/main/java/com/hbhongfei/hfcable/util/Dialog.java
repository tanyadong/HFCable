package com.hbhongfei.hfcable.util;

import android.content.Context;

import com.hbhongfei.hfcable.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by 苑雪元 on 2016/8/6.
 * dialog
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
        /*new CountDownTimer(800 * 7, 800) {
            public void onTick(long millisUntilFinished) {
                i++;
                switch (i){
                    case 0:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.blue_btn_bg_color));
                        break;
                    case 1:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_50));
                        break;
                    case 2:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.success_stroke_color));
                        break;
                    case 3:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_deep_teal_20));
                        break;
                    case 4:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.material_blue_grey_80));
                        break;
                    case 5:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.warning_stroke_color));
                        break;
                    case 6:
                        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.success_stroke_color));
                        break;
                }
            }

            public void onFinish() {
                i = -1;
                pDialog.setTitleText("登录成功!")
                        .setConfirmText("OK")
                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

            }
        }.start();*/
    }

    /**
     * dialog消失
     */
    public void cancle(){
        pDialog.cancel();
    }

}

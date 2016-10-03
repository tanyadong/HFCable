package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.hbhongfei.hfcable.R;


public class SplashActivity extends Activity {

    public static int ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        boolean mFirst = isFirstEnter(SplashActivity.this,SplashActivity.this.getClass().getName());
//        boolean isLogin = isLogin(SplashActivity.this,SplashActivity.this.getClass().getName());
        if(mFirst)
            mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY,5000);
        else
            mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY,4000);
    }

    //****************************************************************
    // 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
    //****************************************************************
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private boolean isFirstEnter(Context context,String className){
        if(context==null || className==null||"".equalsIgnoreCase(className)){
            return false;
        }
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                .getString(KEY_GUIDE_ACTIVITY, "");//取得所有类名 如 com.my.MainActivity
        if(mResultStr.equalsIgnoreCase("false")){
            return false;
        }else{
            return true;
        }
    }

    //****************************************************************
    // 判断应用是否进行登录过
    //****************************************************************
   /* private static final String LOGINORNOT = LoginConnection.USER;
    private boolean isLogin(Context context,String className){
        if(context==null || className==null||"".equalsIgnoreCase(className)){
            return false;
        }
        SharedPreferences spf = context.getSharedPreferences(LOGINORNOT, Context.MODE_PRIVATE);
        String loginOrNot = spf.getString("id", null);
        if (loginOrNot!=null){
            return true;
        }else{
            return false;
        }

    }*/


    //*************************************************
    // Handler:跳转至不同页面
    //*************************************************
//    private final static int SWITCH_LOGINACTIVITY = 500;
    private final static int SWITCH_GUIDACTIVITY = 501;
    private final static int SWITCH_MAINACTIVITY = 502;
    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                /*case SWITCH_LOGINACTIVITY:
                    Intent mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
                    break;*/
                case SWITCH_GUIDACTIVITY:
                    Intent mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, GuideActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
                    break;
                case SWITCH_MAINACTIVITY:
                    ID = 1;
                    mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, MainActivity.class);
//                    mIntent.putExtra("id",1);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();

                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //防止handler引起的内存泄露
        mHandler.removeCallbacksAndMessages(null);
    }
}
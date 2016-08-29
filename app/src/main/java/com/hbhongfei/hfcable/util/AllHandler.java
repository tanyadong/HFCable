package com.hbhongfei.hfcable.util;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.hbhongfei.hfcable.activity.LoginActivity;

/**
 * Created by dell1 on 2016/8/29.
 */
public class AllHandler {

    private final static int SWITCH_LOGINACTIVITY = 500;
    private final static int SWITCH_SHOWMINE = 503;
//    public Handler mHandler = new Handler(){
//        public void handleMessage(Message msg) {
//            switch(msg.what){
//                case SWITCH_LOGINACTIVITY:
//                    Intent mIntent = new Intent();
//                    mIntent.setClass(MainActivity.this, LoginActivity.class);
//                    MainActivity.this.startActivity(mIntent);
//                    MainActivity.this.finish();
//                    break;
//                case SWITCH_SHOWMINE:
//                    showMine();
//                    viewPager.setCurrentItem(3);
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
}

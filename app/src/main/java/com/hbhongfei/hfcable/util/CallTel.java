package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by 苑雪元 on 2016/5/10.
 */
public class CallTel {
    private Context context;
    public CallTel(Context context){
        this.context = context;
    }
    public void call(String telnum){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + telnum));
        context.startActivity(intent);
    }
}

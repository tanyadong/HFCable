package com.hbhongfei.hfcable.util;

import android.app.*;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;

/**
 *
 * 加载的dialog
 * Created by 苑雪元 on 2016/9/30.
 */
public class LoadingDialog extends android.app.Dialog {

    private TextView textView;
    private Context context;

    public LoadingDialog(Context context) {
        super(context,R.style.loadingDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        textView = (TextView)findViewById(R.id.tv);
        textView.setText("正在加载....");
        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
        linearLayout.getBackground().setAlpha(210);
    }


}

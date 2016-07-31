package com.hbhongfei.hfcable.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.widget.EditText;

/**
 * Created by dell1 on 2016/7/31.
 */
public class HintTestSize {
    private EditText editText;
    private String string;

    public HintTestSize(EditText editText, String string) {
        this.editText = editText;
        this.string = string;
    }

    public void setHintTextSize(){
        SpannableString ss = new SpannableString(string);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15,true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editText.setHint(new SpannedString(ss));
    }
}

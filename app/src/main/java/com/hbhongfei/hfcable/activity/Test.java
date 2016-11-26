package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.ToastUtil;

public class Test extends AppCompatActivity {
private Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn= (Button) findViewById(R.id.button);

    }
    public void btnClick(View view){
        ToastUtil.showCompletedCustomToastShortWithResId(this,R.layout.view_tips,"添加购物车失败",R.mipmap.error);
    }
}

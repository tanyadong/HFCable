package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.utils.WheelView;

import java.util.Arrays;

public class MyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout myInfo_name,myInfo_sex,myInfo_password;
    private TextView sex;
    private String S_sex = "男";
    private static final String[] SEX = new String[]{"男", "女"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initView();
        setOnClick();
        toolBar();
    }

    private void toolBar(){
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(true);
    }

    /**
     * 初始化界面
     */
    private void initView(){
        myInfo_name = (RelativeLayout) findViewById(R.id.Rlayout_myInfo_name);
        myInfo_sex = (RelativeLayout) findViewById(R.id.Rlayout_myInfo_sex);
        myInfo_password = (RelativeLayout) findViewById(R.id.Rlayout_myInfo_password);
        sex = (TextView) findViewById(R.id.Tview_myInfo_sex);
    }

    /**
     * 设置点击事件
     */
    private void setOnClick(){
        this.myInfo_name.setOnClickListener(this);
        this.myInfo_sex.setOnClickListener(this);
        this.myInfo_password.setOnClickListener(this);
    }

    /**
     * 设置值
     */
    private void setValues(){
        sex.setText(S_sex);
    }

    /**
     * 跳转页面
     * @param c 要跳转的界面的class
     */
    private void intent(Class c){
        Intent i = new Intent();
        i.setClass(MyInfoActivity.this,c);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Rlayout_myInfo_name:
                intent(MyNameActivity.class);
                break;
            case R.id.Rlayout_myInfo_sex:
                showPopwindow();
                break;
            case R.id.Rlayout_myInfo_password:
                intent(MyPasswordActivity.class);
                break;
        }
    }

    /**
     *  显示popWindow
     */
    private void showPopwindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sex_selector, null);

        WheelView wva = (WheelView) view.findViewById(R.id.w);

        wva.setOffset(1);
        wva.setItems(Arrays.asList(SEX));
        //选择事件
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                S_sex = item;
            }
        });

        // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

        final PopupWindow window = new PopupWindow(view,WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xbee00000);
        window.setBackgroundDrawable(dw);

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(MyInfoActivity.this.findViewById(R.id.Rlayout_myInfo_sex),Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        TextView first = (TextView) view.findViewById(R.id.Tview_sexSelector_sure);
        first.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setValues();

                window.dismiss();//关闭
            }
        });

        /*//popWindow消失监听方法
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                System.out.println("popWindow消失");
            }
        });*/
    }
}

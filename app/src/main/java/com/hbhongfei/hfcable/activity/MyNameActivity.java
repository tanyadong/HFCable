package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.HintTestSize;

public class MyNameActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText myName;
    private TextView number;
    private String S_name;
    private ImageView cancel, done;
    private Boolean tag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_name);
        toolBar();
        initView();
        count();
        setOnClick();
    }

    /**
     * Toast
     *
     * @param s 要显示的内容
     */
    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myName_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        myName = (EditText) findViewById(R.id.Etext_myName_text);
        number = (TextView) findViewById(R.id.Tview_myName_number);
        cancel = (ImageView) findViewById(R.id.Image_myName_cancel);
        done = (ImageView) findViewById(R.id.Image_myName_done);
//        SpannableString ss = new SpannableString("我是测试hint");//定义hint的值
//        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(10,true);//设置字体大小 true表示单位是sp
//        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        myName.setHint(new SpannedString(ss));
        HintTestSize hintTestSize=new HintTestSize(myName,"请输入您的昵称：");
        hintTestSize.setHintTextSize();
    }

    /**
     * 统计用户输入的字符数，并显示
     */
    private void count() {
        myName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                getValues();
                System.out.println(S_name);
                number.setText("" + S_name.length());
            }
        });
    }

    /**
     * 获取数据
     */
    private void getValues() {
        S_name = myName.getText().toString().trim();
    }

    /**
     * 设置点击事件
     */
    private void setOnClick() {
        this.cancel.setOnClickListener(this);
        this.done.setOnClickListener(this);
    }

    /**
     * 界面跳转事件
     *
     * @param c 要跳转的界面的class
     */
    private void intent(Class c) {
        Intent i = new Intent();
        i.setClass(MyNameActivity.this, c);
        startActivity(i);
    }

    /**
     * 检查数据
     *
     * @return 数据是否则正确
     */
    private boolean checkValues() {
        //获取数据
        getValues();
        if (TextUtils.isEmpty(S_name)) {
            toast("昵称不能为空，请输入");
            tag = false;
        }
        return tag;
    }

    /**
     * 将数据进行保存
     */
    private void saveValues() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Image_myName_cancel:
                intent(MyInfoActivity.class);
                break;
            case R.id.Image_myName_done:
                if (checkValues()) {
                    toast("昵称修改成功");
                    saveValues();
                    intent(MyInfoActivity.class);
                }
                break;
        }
    }
}

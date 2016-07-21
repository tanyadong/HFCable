package com.hbhongfei.hfcable.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;

public class MyNameActivity extends AppCompatActivity {
    private EditText myName;
    private TextView number;
    private String S_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_name);
        initView();
        count();
    }

    /**
     * 初始化界面
     */
    private void initView(){
        myName = (EditText) findViewById(R.id.Etext_myName_text);
        number = (TextView) findViewById(R.id.Tview_myName_number);
    }

    /**
     * 统计用户输入的字符数，并显示
     */
    private void count(){
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
                number.setText(""+S_name.length());
//                System.out.println(S_name.length());
            }
        });
    }

    /**
     * 获取数据
     */
    private void getValues(){
        S_name = myName.getText().toString().trim();
    }
}

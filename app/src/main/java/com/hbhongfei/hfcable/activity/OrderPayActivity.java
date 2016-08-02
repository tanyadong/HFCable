package com.hbhongfei.hfcable.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.MyRadioGroup;

public class OrderPayActivity extends AppCompatActivity  {
    private MyRadioGroup radioGroup;
    private RadioButton radioButton_apliy,radioButton_weixin,radioButton_yinlian;
    LinearLayout appliy_layout,weixin_layout,yinlian_layout;
    Button btn_confimgPay;
    String tag="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        toolBar();
        initView();
        onClick();

    }
    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.orderPay_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    //初始化
    private void initView(){
        radioGroup= (MyRadioGroup) findViewById(R.id.radio_group);
        radioButton_apliy= (RadioButton) findViewById(R.id.order_pay_alipay_radioBtn);
        radioButton_weixin= (RadioButton) findViewById(R.id.order_pay_weixin_radioBtn);
        radioButton_yinlian= (RadioButton) findViewById(R.id.order_pay_yinlian_radioBtn);
        weixin_layout= (LinearLayout) findViewById(R.id.order_pay_weixin_LLayout);
        appliy_layout= (LinearLayout) findViewById(R.id.order_pay_alipay_LLayout);
        yinlian_layout= (LinearLayout) findViewById(R.id.order_pay_yinlian_LLayout);
        btn_confimgPay= (Button) findViewById(R.id.btn_orderPay);
    }
    private void onClick(){
        //绑定一个匿名监听器
        //单选按钮
        radioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.order_pay_alipay_radioBtn:
                        appliy_layout.setBackgroundResource(R.drawable.linnerlayout_border_red);
                        weixin_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        yinlian_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        tag="支付宝";
                        break;
                    case R.id.order_pay_weixin_radioBtn:
                        weixin_layout.setBackgroundResource(R.drawable.linnerlayout_border_red);
                        appliy_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        yinlian_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        tag="微信";
                        break;
                    case R.id.order_pay_yinlian_radioBtn:
                        yinlian_layout.setBackgroundResource(R.drawable.linnerlayout_border_red);
                        weixin_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        appliy_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        tag="银联";
                        break;
                }

            }
        });
        //确认按钮的点击事件
        btn_confimgPay.setOnClickListener(btn_clickListener);
    }

   private View.OnClickListener btn_clickListener=new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           switch (tag){
               case "支付宝":
                   Toast.makeText(OrderPayActivity.this,tag,Toast.LENGTH_SHORT).show();
                   break;
               case "微信":
                   Toast.makeText(OrderPayActivity.this,tag,Toast.LENGTH_SHORT).show();
                   break;
               case "银联":
                   Toast.makeText(OrderPayActivity.this,tag,Toast.LENGTH_SHORT).show();
                   break;
               case "":
                   Toast.makeText(OrderPayActivity.this,"请选择支付方式",Toast.LENGTH_SHORT).show();
                   break;
           }
       }
   };
}

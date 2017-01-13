package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.CustomDialog;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.GetIP;
import com.hbhongfei.hfcable.util.MyRadioGroup;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;
import com.pingplusplus.android.Pingpp;
import com.pingplusplus.android.PingppLog;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderPayActivity extends AppCompatActivity implements
        View.OnClickListener{

    /**
     * 开发者需要填一个服务端URL 该URL是用来请求支付需要的charge。务必确保，URL能返回json格式的charge对象。
     * 服务端生成charge 的方式可以参考ping++官方文档，地址 https://pingxx.com/guidance/server/import
     * <p/>
     * 【 http://218.244.151.190/demo/charge 】是 ping++ 为了方便开发者体验 sdk 而提供的一个临时 url 。
     * 该 url 仅能调用【模拟支付控件】，开发者需要改为自己服务端的 url 。
     */
    private static String YOUR_URL = "/pay";
    public static final String URL = Url.url(YOUR_URL);
    //银联支付渠道
    private static final String CHANNEL_UPACP = "upacp";
    //微信支付渠道
    private static final String CHANNEL_WECHAT = "wx";
    //支付支付渠道
    private static final String CHANNEL_ALIPAY = "alipay";
    private MyRadioGroup radioGroup;
    private RadioButton radioButton_apliy, radioButton_weixin, radioButton_yinlian;
    LinearLayout appliy_layout, weixin_layout, yinlian_layout;
    private TextView order_payMoney_textview;
    private Button btn_confimgPay;
    String tag = "";
    private String S_money;
    private int amount;
    private String order_no;//订单号
    private String client_ip;//客户端的IPV4
    private String subject;//商品的标题
    private String body;//商品的描述信息
    private Dialog dialog;
    private Map<String, Object> map;
    private SparseArray array;
    private StringBuffer buffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        toolBar();
        initView();
        //初始化数据
        initValue();
        //设置点击事件
        onClick();
        //设置需要使用的支付方式
        PingppLog.DEBUG = true;

    }

    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.orderPay_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    //初始化
    private void initView() {
        order_payMoney_textview = (TextView) findViewById(R.id.order_payMoney_textview);
        radioGroup = (MyRadioGroup) findViewById(R.id.radio_group);
        radioButton_apliy = (RadioButton) findViewById(R.id.order_pay_alipay_radioBtn);
        radioButton_weixin = (RadioButton) findViewById(R.id.order_pay_weixin_radioBtn);
        radioButton_yinlian = (RadioButton) findViewById(R.id.order_pay_yinlian_radioBtn);
        weixin_layout = (LinearLayout) findViewById(R.id.order_pay_weixin_LLayout);
        appliy_layout = (LinearLayout) findViewById(R.id.order_pay_alipay_LLayout);
        yinlian_layout = (LinearLayout) findViewById(R.id.order_pay_yinlian_LLayout);
        btn_confimgPay = (Button) findViewById(R.id.btn_orderPay);
    }

    /**
     * 初始化数据
     */
    private void initValue() {
        dialog=new Dialog(this);
        Intent intent = getIntent();
        S_money = intent.getStringExtra("money");
        //判断钱后边是否有两位
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){2})?$");
        Matcher isMoney = pattern.matcher(S_money);
        if ( !isMoney.matches() ) {//不是后边有两位小数的情况，进行加0
            S_money = S_money + "0";
        }
        order_no = intent.getStringExtra("order_no");
        order_payMoney_textview.setText("￥"+S_money);
        Bundle bundle = intent.getExtras();
        array = bundle.getSparseParcelableArray("introduce");
        String replaceable = String.format("[%s, \\s.]", NumberFormat.getCurrencyInstance(Locale.CHINA).getCurrency().getSymbol(Locale.CHINA));
        String moneyStr = S_money.toString().replaceAll(replaceable, "");
        amount = Integer.valueOf(new BigDecimal(moneyStr).toString());

        //获取ipv4
        client_ip = GetIP.GetIp();
        subject = "弘飞线缆";
        buffer = new StringBuffer();
        int count=array.size();
        for (int i=0;i<count;i++){
            buffer.append(array.get(i));
            if (i!=array.size()-1){
                buffer.append(",");
            }
        }
        body =buffer.toString();
    }

    /**
     * 存入数据，并将此数据放到json中
     *
     * @param channel 支付类型
     */
    private void putValues(String channel) {
        map = new HashMap<>();
        map.put("channel", channel);
        map.put("amount", amount);
        map.put("body",body);
        map.put("order_no",order_no);
        map.put("client_ip",client_ip);
    }

    private void onClick() {
        //绑定一个匿名监听器
        //单选按钮
        radioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.order_pay_alipay_radioBtn:
                        appliy_layout.setBackgroundResource(R.drawable.linnerlayout_border_red);
                        weixin_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        yinlian_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        tag = "支付宝";
                        break;
                    case R.id.order_pay_weixin_radioBtn:
                        weixin_layout.setBackgroundResource(R.drawable.linnerlayout_border_red);
                        appliy_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        yinlian_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        tag = "微信";
                        break;
                    case R.id.order_pay_yinlian_radioBtn:
                        yinlian_layout.setBackgroundResource(R.drawable.linnerlayout_border_red);
                        weixin_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        appliy_layout.setBackgroundResource(R.drawable.linnerlayout_border);
                        tag = "银联";
                        break;
                }

            }
        });
        //确认按钮的点击事件
        btn_confimgPay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (tag) {
            case "支付宝":
                putValues(CHANNEL_ALIPAY);
                new PaymentTask().execute(map);
                break;
            case "微信":
                putValues(CHANNEL_WECHAT);
                new PaymentTask().execute(map);
                break;
            case "银联":
                putValues(CHANNEL_UPACP);
                new PaymentTask().execute(map);
                break;
            case "":
                Toast.makeText(OrderPayActivity.this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                break;
        }

    }


    class PaymentTask extends AsyncTask<Map<String, Object>, Void, String> {

        @Override
        protected String doInBackground(Map<String, Object>... params) {
            Map<String, Object> paymentRequest = params[0];
            String data = null;
            String json = new Gson().toJson(paymentRequest);
            try {
                //向Your Ping++ Server SDK请求数据
                data = postJson(URL, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            dialog.showDialog("正在发起支付");
            //按键点击之后的禁用，防止重复点击
            btn_confimgPay.setOnClickListener(null);
        }

        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                CustomDialog.payDialog(OrderPayActivity.this,"fail");
                dialog.cancle();
                return;
            }
            dialog.cancle();
//            Pingpp.createPayment(ClientSDKActivity.this, data);
            //QQ钱包调起支付方式  “qwalletXXXXXXX”需与AndroidManifest.xml中的data值一致
            //建议填写规则:qwallet + APP_ID
            Pingpp.createPayment(OrderPayActivity.this, data, "qwalletapp_v90SuTjXH4yTrrPi");
        }
    }

    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        btn_confimgPay.setOnClickListener(this);
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {

                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                if (result.equals("success")) {
                    //此处进行验证
                    //链接验证服务
                    updatePayState();
                } else {
                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
//                    Toast.makeText(this,errorMsg+"--"+extraMsg,Toast.LENGTH_SHORT).show();
                    CustomDialog.payDialog(this,result);
                }

            }
        }
    }
    /**
     * 更改支付状态
     */
    private void updatePayState() {
        Toast.makeText(this,"支付成功",Toast.LENGTH_SHORT).show();
        Map<String,String> params =new HashMap<>();
        params.put("orderNum", order_no);
        String url = Url.url("/androidOrder/updatePayState");
        //使用自己书写的NormalPostRequest类，
        com.android.volley.Request<JSONObject> request = new NormalPostRequest(url,updateListener,errorListener, params);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }

    /**
     * 找回密码时连接服务
     * 成功的监听器
     */
    private com.android.volley.Response.Listener<JSONObject> updateListener = new com.android.volley.Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("msg");
                if (s.equals("success")){
                    Intent intent = new Intent();
                    intent.setClass(OrderPayActivity.this, PayStateActivity.class);
                    intent.putExtra("amount", S_money);
                    startActivity(intent);
                    finish();
                }else if(s.equals("filed")) {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };



    /**
     * 请求url并返回值
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    private static String postJson(String url, String json) throws IOException {
        MediaType type = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(type, json);
        Request request = new Request.Builder().url(url).post(body).build();

        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        if (response != null) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


    /**
     *  失败的监听器
     */
    private com.android.volley.Response.ErrorListener errorListener = new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(OrderPayActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
        }
    };
}

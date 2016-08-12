package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.entity.City;
import com.hbhongfei.hfcable.entity.ShoppingAddress;
import com.hbhongfei.hfcable.util.CheckPhoneNumber;
import com.hbhongfei.hfcable.util.HintTestSize;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddRecietAddress extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout Rlayout_localArea;
    private EditText consigne_editview,add_phone_editview,add_addressDetail_editview;
    private City city ;
    private TextView add_localArea_textview;
    private Button btn_address_save;
    private ArrayList<City> toCitys;
    private static final String USER = LoginConnection.USER;
    private String S_phoneNumber;//用户名
    private String consignee,phone,localArea,detailAddress;
    private RequestQueue mQueue;
    private String tag;  //判断是哪个界面跳转过来的
    private ShoppingAddress shoppingAddress;
    SweetAlertDialog sweetAlertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_reciet_address);
        mQueue= Volley.newRequestQueue(this);
        sweetAlertDialog=new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE);

        initview();
        //初始化数据
        initValues();
//        //保存收货地址的服务
//        saveConnection();
        click();
    }
    /**
     * 初始化组件
     */
    private void  initview(){
        Rlayout_localArea= (RelativeLayout) findViewById(R.id.Rlayout_localArea);
        consigne_editview= (EditText) findViewById(R.id.consigne_editview);
        add_phone_editview= (EditText) findViewById(R.id.add_phone_editview);
        add_addressDetail_editview= (EditText) findViewById(R.id.add_addressDetail_editview);
        HintTestSize hintTestSize=new HintTestSize(add_addressDetail_editview,"街道楼牌号等");
        hintTestSize.setHintTextSize();
        add_localArea_textview= (TextView) findViewById(R.id.add_localArea_textview);
        btn_address_save= (Button) findViewById(R.id.btn_add_address_save);
    }

    /**
     * 初始化数据
     */
    private void initValues(){
        Intent intent=getIntent();
        tag=intent.getStringExtra("tag");
        shoppingAddress= (ShoppingAddress) intent.getSerializableExtra("shoppingAddress");
       if(shoppingAddress!=null) {
           consigne_editview.setText(shoppingAddress.getConsignee());
           add_phone_editview.setText(shoppingAddress.getPhone());
           add_addressDetail_editview.setText(shoppingAddress.getDetailAddress());
           add_localArea_textview.setText(shoppingAddress.getLocalArea());
       }
    }
    /**
     * 获取各个组件的值
     */
    private void getValue(){
        SharedPreferences spf =getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber",null);
        consignee=consigne_editview.getText().toString().trim();
        phone=add_phone_editview.getText().toString().trim();
        localArea=add_localArea_textview.getText().toString().trim();
        detailAddress=add_addressDetail_editview.getText().toString().trim();
    }
    /**
     * 点击事件
     */
    private void click(){
        Rlayout_localArea.setOnClickListener(this);
        btn_address_save.setOnClickListener(this);
    }

    /**
     * 保存地址
     * @param
     */
private void saveConnection(){
    String url = Url.url("/androidAddress/sava");
    Map<String,String> map=new HashMap<>();
    map.put("consignee",consignee);
    map.put("phone",phone);
    map.put("localArea",localArea);
    map.put("detailAddress",detailAddress);
    map.put("userName",S_phoneNumber);
    NormalPostRequest normalPostRequest=new NormalPostRequest(url,saveSuccessListener,errorListener,map);
    mQueue.add(normalPostRequest);
}

    /**
     * 收货地址保存成功的监听器
     */
    private Response.Listener<JSONObject> saveSuccessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg=jsonObject.getString("msg");
                if(TextUtils.equals(msg,"success")){
                    toReceiptAddress();
                    Toast.makeText(AddRecietAddress.this,"保存成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddRecietAddress.this,"保存失败",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(AddRecietAddress.this,"连接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);

        }
    };
    /**
     * 修改地址
     * @param
     */
    private void editConnection(){
        String url = Url.url("/androidAddress/update");
        Map<String,String> map=new HashMap<>();
        map.put("consignee",consignee);
        map.put("phone",phone);
        map.put("localArea",localArea);
        map.put("detailAddress",detailAddress);
        map.put("addressId",shoppingAddress.getId());
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,updateSuccessListener,errorListener,map);
        mQueue.add(normalPostRequest);
    }

    /**
     * 地址修改成功的监听器
     */
    private Response.Listener<JSONObject> updateSuccessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg=jsonObject.getString("msg");
                if(TextUtils.equals(msg,"success")){
                    toReceiptAddress();
                    Toast.makeText(AddRecietAddress.this,"更新成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddRecietAddress.this,"更新失败",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 跳转到地址管理页面
     */
    private void toReceiptAddress(){
        Intent intent=new Intent(AddRecietAddress.this,ReceiptAddressActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 判断内容是否空
     * @param
     */
    private boolean isEmipy(){
        return  consignee.isEmpty()|| phone.isEmpty()||localArea.isEmpty()||detailAddress.isEmpty();
    }

    /**
     * 检查手机号格式
     * @param
     */
    private boolean checkPhoneNumber(){
        return CheckPhoneNumber.checkPhoneNum(phone);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Rlayout_localArea:
                Intent in = new Intent(this, CitySelect1Activity.class);
                in.putExtra("city", city);
                startActivityForResult(in, 1);
                break;
            case R.id.btn_add_address_save:
                getValue();
                if(isEmipy()){
                    Toast.makeText(this,"请完善信息",Toast.LENGTH_LONG).show();
                }else if(!checkPhoneNumber()){
                    Toast.makeText(this,"手机号格式不正确",Toast.LENGTH_LONG).show();
                }
                if(!isEmipy()&&checkPhoneNumber()){
                    if(tag.equals("add")){
                        saveConnection();
                    }else if(tag.equals("edit")){
                        editConnection();
                    }

                }
                break;
        }
    }
    /**
     * 删除地址的服务
     */
    private void deleteConnection(){
//        RequestQueue queue= Volley.newRequestQueue(mContext);
        String url= Url.url("/androidAddress/deleteAddress");
        Map<String,String> map=new HashMap<>();
        map.put("addressId",shoppingAddress.getId());
        NormalPostRequest normalPostRequest=new NormalPostRequest(url,deleteSuccessListener,errorListener,map);
        mQueue.add(normalPostRequest);
    }

    /**
     * 删除收货地址成功的监听器
     */
    private Response.Listener<JSONObject> deleteSuccessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg=jsonObject.getString("msg");
                if(msg.equals("success")){
                    sweetAlertDialog.dismiss();
                    toReceiptAddress();
                    Toast.makeText(AddRecietAddress.this,"删除成功",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(AddRecietAddress.this,"删除失败",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 8){
            if(requestCode == 1){
                city = data.getParcelableExtra("city");
                add_localArea_textview.setText(city.getProvince()+city.getCity()+city.getDistrict());

            }else if(requestCode == 2){
                toCitys = data.getParcelableArrayListExtra("toCitys");
                StringBuffer ab = new StringBuffer();
                for (int i = 0; i < toCitys.size(); i++) {
                    if(i==toCitys.size()-1){//如果是最后一个城市就不需要逗号
                        ab.append(toCitys.get(i).getCity());
                    }else{
                        ab.append(toCitys.get(i).getCity()+"， ");//如果不是最后一个城市就需要逗号
                    }
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_address_delete, menu);
        if(tag.equals("edit")){
            menu.getItem(0).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.del_address_itme:
                sweetAlertDialog.setContentText("您确定要删除吗？")
                        .showCancelButton(true)
                        .setTitleText("删除")
                        .setCancelText("取消")
                        .setConfirmText("确认")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                deleteConnection();
//								sweetAlertDialog.dismiss();
                            }
                        })
                        .show();

                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

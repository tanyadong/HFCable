package com.hbhongfei.hfcable.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.ImageCropActivity;
import com.hbhongfei.hfcable.activity.MyFavoriteActivity;
import com.hbhongfei.hfcable.activity.MyInfoActivity;
import com.hbhongfei.hfcable.activity.MyOrderActivity;
import com.hbhongfei.hfcable.activity.MyShoppingActivity;
import com.hbhongfei.hfcable.util.Constants;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private LinearLayout myInfoEdit;
    private ImageView myhead;
    private TextView myName;
    private String S_name,S_phoneNumber;
    private RelativeLayout shopping,order,favorite;
    private static final String USER = LoginConnection.USER;
    private Dialog dialog;


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(v);
        setOnClick();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        initValues();
    }

    /**
     * 初始化界面
     * 苑雪元
     * 2016/07/21
     * @param v 主界面
     */
    public void initView(View v){
        dialog = new Dialog(this.getActivity());
        myInfoEdit = (LinearLayout) v.findViewById(R.id.mine_edit);
        myhead = (ImageView) v.findViewById(R.id.Iamge_mine_head);
        myName = (TextView) v.findViewById(R.id.Tview_mine_myName);
        shopping = (RelativeLayout) v.findViewById(R.id.Rlayout_mine_shopping);
        order = (RelativeLayout) v.findViewById(R.id.Rlayout_mine_order);
        favorite = (RelativeLayout) v.findViewById(R.id.Rlayout_mine_favorite);
    }

    /**
     * 设置点击事件
     * 苑雪元
     * 2016/07/21
     */
    private void setOnClick(){
        this.shopping.setOnClickListener(this);
        this.order.setOnClickListener(this);
        this.favorite.setOnClickListener(this);
        this.myInfoEdit.setOnClickListener(this);
        this.myhead.setOnClickListener(this);
    }

    /**
     * 初始化数据
     * 苑雪元
     * 2016/07/21
     */
    private void initValues(){
        SharedPreferences spf = this.getActivity().getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_name = spf.getString("nickName", null);
        S_phoneNumber = spf.getString("phoneNumber",null);
        this.myName.setText(S_name);

    }

    /**
     * 跳转页面
     * 苑雪元
     * 2016/07/21
     * @param c 要跳转的页面
     */
    private void intent(Class c){
        Intent i = new Intent();
        i.setClass(this.getActivity(), c);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Rlayout_mine_favorite:
                intent(MyFavoriteActivity.class);
                break;
            case R.id.Rlayout_mine_order:
                intent(MyOrderActivity.class);
                break;
            case R.id.Rlayout_mine_shopping:
                intent(MyShoppingActivity.class);
                break;
            case R.id.Iamge_mine_head:
                //修改头像
                showDialog();
                break;
            case R.id.mine_edit:
                intent(MyInfoActivity.class);
                break;
        }
    }

    /**
     * 选择拍照还是相册
     */
    private void showDialog(){
        new SweetAlertDialog(this.getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("选择途径")
                .setContentText("一张美美的头像")
                .setCancelText("拍照")
                .setConfirmText("相册")
                //设置标题图片
                .setCustomImage(R.mipmap.man)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //拍照
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 下面这句指定调用相机拍照后的照片存储的路径
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.PHOTONAME)));
                        startActivityForResult(intent, Constants.TAKE_PHOTO);
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        //相册中选择
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, Constants.PICK_PHOTO);
                        sDialog.cancel();
                    }
                })
                .show();
    }
    @Override
    public   void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            Intent intent = new Intent(MineFragment.this.getActivity(), ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH",Constants.PHOTONAME);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = MineFragment.this.getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent(MineFragment.this.getActivity(), ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH",picturePath);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.CROP_BEAUTY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                Bitmap b = BitmapFactory.decodeFile(path);
                if (b != null) {
                    myhead.setImageBitmap(b);
//                    setImage(path);
                }
            }
        }
    }

   /* *//**
     * 更改头像
     *//*
    public void setImage(String path){
        Map<String,String> params =new HashMap<>();
        params.put("image", path);
        params.put("Content-Type","multipart/form-data");
        params.put("phoneNumber", S_phoneNumber);
        String url = Url.url("androidUser/updateNickName");
        System.out.println(url);
        RequestQueue mQueue = Volley.newRequestQueue(this);

        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,jsonObjectListener,errorListener, params);
        mQueue.add(request);
    }

    *//**
     * 成功的监听器
     *//*
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String s = jsonObject.getString("updateNickName");
                if (s.equals("success")){
                    SharedPreferences.Editor editor = getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
                    editor.putString("nickName", S_name);
                    editor.apply();
                    Intent i = new Intent();
                    i.putExtra("nickName",S_name);
                    setResult(0,i);
                    finish();
                }else {
                    Toast.makeText(MyNameActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    *//**
     *  失败的监听器
     *//*
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(MyNameActivity.this,"链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };*/

}

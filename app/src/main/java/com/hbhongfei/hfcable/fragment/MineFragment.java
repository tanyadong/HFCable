package com.hbhongfei.hfcable.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Constants;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.UpLoadImage;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
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
    private String S_name,S_phoneNumber,S_head,S_head1;
    private RelativeLayout shopping,order,favorite;
    private static final String USER = LoginConnection.USER;
    private Dialog dialog;
    private AsyncBitmapLoader asyncBitmapLoader;
    private UpLoadImage upLoadImage;
    private int tag = 0;
    private Bitmap b;
    private Drawable drawable;

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
        initValues();
        super.onResume();
    }

    /**
     * 初始化界面
     * 苑雪元
     * 2016/07/21
     * @param v 主界面
     */
    public void initView(View v){
        upLoadImage =new UpLoadImage(this.getActivity());
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
        S_head  =spf.getString("headPortrait",null);
        this.myName.setText(S_name);
//        imageHead();
        if (tag==0){
            String url = Url.url(S_head);
            myhead.setTag(url);
            asyncBitmapLoader = new AsyncBitmapLoader();
            asyncBitmapLoader.loadImage(MineFragment.this.getContext(),myhead,url);
        }else{
            myhead.setImageBitmap(b);
        }
    }

    /**
     * 跳转页面
     * 苑雪元
     * 2016/07/21
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
        if (tag==0){
            drawable = MineFragment.this.getActivity().getResources().getDrawable(R.mipmap.man);
        }else{
            drawable =new BitmapDrawable(b);
        }
        new SweetAlertDialog(this.getActivity(), SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("选择途径")
                .setContentText("一张美美的头像")
                .setCancelText("拍照")
                .setConfirmText("相册")
                //设置标题图片
                .setCustomImage(drawable)
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
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image");
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
                 b = BitmapFactory.decodeFile(path);
                if (b != null) {
                    tag++;
                    myhead.setImageBitmap(b);
                    //上传
                    upLoadImage.load(b,S_phoneNumber);
                }
            }
        }
    }
}

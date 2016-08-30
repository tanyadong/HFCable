package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.Constants;
import com.hbhongfei.hfcable.util.DatepickerFragment;
import com.hbhongfei.hfcable.utils.WheelView;

import java.io.File;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class InputMyInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout Rhead,Rname,Rsex,Rbirthday;
    private TextView Tname,Tsex,Tbirthday,Tsave;
    private ImageView Ihead;
    private static final String[] SEX = new String[]{"男", "女"};
    private String S_name,S_sex,S_birthday;
    private Bitmap b;
    private Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_my_info);

        //toolbar
        toolbar();

        //初始化界面
        initView();

        //设置点击事件
        setOnclick();

    }

    /**
     * toolbar
     */
    private void toolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_inputMyInfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
    }

    /**
     * 初始化界面
     */
    private void initView(){
        this.Rhead = (RelativeLayout) findViewById(R.id.Rlayout_inputMyInfo_head);
        this.Rname = (RelativeLayout) findViewById(R.id.Rlayout_inputMyInfo_name);
        this.Rsex = (RelativeLayout) findViewById(R.id.Rlayout_inputMyInfo_sex);
        this.Rbirthday = (RelativeLayout) findViewById(R.id.Rlayout_inputMyInfo_birthday);

        this.Tname = (TextView) findViewById(R.id.Tview_inputMyInfo_name);
        this.Tsex = (TextView) findViewById(R.id.Tview_inputMyInfo_sex);
        this.Tbirthday = (TextView) findViewById(R.id.Tview_inputMyInfo_birthday);
        this.Ihead = (ImageView) findViewById(R.id.Iamge_inputMyInfo_head);
        this.Tsave = (TextView) findViewById(R.id.Tview_inputMyInfo_saveInfo);
    }

    /**
     * 设置点击事件
     */
    private void setOnclick(){
        this.Rhead.setOnClickListener(this);
        this.Rname.setOnClickListener(this);
        this.Rsex.setOnClickListener(this);
        this.Rbirthday.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            //设置头像
            case R.id.Rlayout_inputMyInfo_head:
                showDialog();
                break;
            //设置姓名
            case R.id.Rlayout_inputMyInfo_name:
                intent.setClass(InputMyInfoActivity.this,MyNameActivity.class);
                intent.putExtra("input","input");
                startActivityForResult(intent,0);
                break;
            //设置性别
            case R.id.Rlayout_inputMyInfo_sex:
                showPopwindow();
                break;
            //设置生日
            case R.id.Rlayout_inputMyInfo_birthday:
                datePicker();
                break;
            //保存个人信息
            case R.id.Tview_inputMyInfo_saveInfo:
                break;
        }
    }

    /**
     * 选择拍照还是相册
     */
    private void showDialog(){
        drawable = InputMyInfoActivity.this.getResources().getDrawable(R.mipmap.man);
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
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
            Intent intent = new Intent(InputMyInfoActivity.this, ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH",Constants.PHOTONAME);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = InputMyInfoActivity.this.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Intent intent = new Intent(InputMyInfoActivity.this, ImageCropActivity.class);
            intent.putExtra("PHOTO_PATH",picturePath);
            startActivityForResult(intent, Constants.CROP_BEAUTY);
        }
        if (requestCode == Constants.CROP_BEAUTY && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                final String path = data.getStringExtra("path");
                b = BitmapFactory.decodeFile(path);
                if (b != null) {
                    Ihead.setImageBitmap(b);
                    //上传
//                    upLoadImage.load(b,S_phoneNumber);
                }
            }
        }
        if (resultCode==0){
            S_name = data.getStringExtra("nickName");
            Tname.setText(S_name);
        }
    }

    /**
     * 日期对话框
     */
    private void datePicker(){
        DatepickerFragment datepickerFragment=new DatepickerFragment();
        datepickerFragment.show(getFragmentManager(), null);
    }
    /**
     * 显示用户选择的年月日
     * @param year
     * @param month
     * @param date
     */
    public void setDate(int year,int month,int date){
        String month1 = null,date1 = null;
        if(month<10){
            month1="0"+(month+1);
        }else{
            month1=String.valueOf(month+1);
        }
        if(date<10){
            date1="0"+(date);
        }else{
            date1=String.valueOf(date);
        }
        Tbirthday.setText(year + "-" + month1 + "-" + date1);
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

        final PopupWindow window = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        window.setFocusable(true);

        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFFFFFF);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setBackgroundDrawable(getDrawable(R.color.colorWhite));
        }else{
            window.setBackgroundDrawable(dw);
        }

        // 设置popWindow的显示和消失动画
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        // 在底部显示
        window.showAtLocation(InputMyInfoActivity.this.findViewById(R.id.Rlayout_inputMyInfo_sex), Gravity.BOTTOM, 0, 0);

        // 这里检验popWindow里的button是否可以点击
        ImageView done = (ImageView) view.findViewById(R.id.Image_sex_done);
        ImageView close = (ImageView) view.findViewById(R.id.Image_sex_close);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Tsex.setText(S_sex);
                window.dismiss();//关闭
            }
        });
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                window.dismiss();//关闭
            }
        });
    }
}

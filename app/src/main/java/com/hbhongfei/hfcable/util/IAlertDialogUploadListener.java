package com.hbhongfei.hfcable.util;

/**
 * 选择上传照片的方式
 * Created by 苑雪元 on 2016/9/20.
 */
public interface IAlertDialogUploadListener {

    public  void before();//上传原图的点击事件

    public void zip();//上传缩略图的点击事件
}

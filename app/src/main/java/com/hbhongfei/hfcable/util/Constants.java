package com.hbhongfei.hfcable.util;

import android.os.Environment;

/**
 * Created by 苑雪元 on 2016/08/04
 * 常量类
 */
public class Constants {
    public static final String EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStorageDirectory().getPath();
    /**
     * 裁剪图片地址,图片在Intent之间传输有大小限制
     */
    public static final String FILENAME = EXTERNAL_STORAGE_DIRECTORY + "/crop.png";
    /**拍照的图片路径*/
    public static final String PHOTONAME = EXTERNAL_STORAGE_DIRECTORY+ "/aa.png";

    public static final int TAKE_PHOTO = 5;
    public static final int PICK_PHOTO = 6;
    public static final int CROP_BEAUTY = 7;
}

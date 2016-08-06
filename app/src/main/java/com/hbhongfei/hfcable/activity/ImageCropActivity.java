package com.hbhongfei.hfcable.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.Constants;
import com.hbhongfei.hfcable.util.CropImageView;
import com.hbhongfei.hfcable.util.FileUtis;


/**
 * Created by 苑雪元 on 2016/08/04
 */
public class ImageCropActivity extends Activity {
    private Bitmap mBitmap;
    private String mPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            mPhotoPath = b.getString("PHOTO_PATH");

        }

        final CropImageView cropImageView = (CropImageView) findViewById(R.id.cropimageview);
        if (!TextUtils.isEmpty(mPhotoPath)) {
            try {
                mBitmap = BitmapFactory.decodeFile(mPhotoPath);
                Bitmap bm = FileUtis.scaleBitmap(mBitmap, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
                mBitmap.recycle();
                cropImageView.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cropImageView.setImageResoure(R.mipmap.man);
        }
        findViewById(R.id.sure).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 保存图片到本地
                Bitmap bm = cropImageView.getCropImage();
                FileUtis.saveBitmap2File(bm, Constants.FILENAME);

                Intent intent = getIntent();
                // 将剪裁图片路径传递回去
                intent.putExtra("path", Constants.FILENAME);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}

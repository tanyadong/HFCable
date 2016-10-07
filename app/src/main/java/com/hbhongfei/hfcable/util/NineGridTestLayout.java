package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.ShowBigPictrue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 苑雪元 on 2016/9/19.
 */
public class NineGridTestLayout extends NineGridLayout {

    protected static final int MAX_W_H_RATIO = 3;

    public NineGridTestLayout(Context context) {
        super(context);
    }

    public NineGridTestLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(final RatioImageView imageView, String url, final int parentWidth) {
        String url1 = Url.url(url);
        Glide.with(mContext)
                .load(url1)
                .placeholder(R.mipmap.background)
                .error(R.mipmap.background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
        return false;// true 代表按照九宫格默认大小显示(此时不要调用setOneImageLayoutParams)；false 代表按照自定义宽高显示。
    }

    @Override
    protected void displayImage(RatioImageView imageView, String url) {
        String url1 = Url.url(url);
        Glide.with(mContext)
                .load(url1)
                .placeholder(R.mipmap.background)
                .error(R.mipmap.background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    protected void onClickImage(int i, String url, List<String> urlList) {
        // 跳到查看大图界面
        Intent intent = new Intent(mContext,ShowBigPictrue.class);
        intent.putExtra("position", i);
        intent.putStringArrayListExtra("image_List", (ArrayList<String>) urlList);
        mContext.startActivity(intent);
    }
}
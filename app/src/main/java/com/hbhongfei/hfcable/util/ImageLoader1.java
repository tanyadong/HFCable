package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dell1 on 2016/7/24.
 */
public class ImageLoader1{
    MemoryCache memoryCache = new MemoryCache();
    private LruCache<String,Bitmap> mcatch;
//    FileCache fileCache;
    Context mContext;
    private static ImageLoader1 mImageLoader = null;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    // 线程池
    ExecutorService executorService;
    public static ImageLoader1 getInstance(Context context) {
        if (null == mImageLoader) {
            mImageLoader = new ImageLoader1(context);
        }
        return mImageLoader;
    }
    public ImageLoader1(Context context) {
//        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
        mContext=context;
    }


    // 最主要的方法
    public void loadImage(String url,NetworkImageView imageView) {
//String url1= (String) imageView.getTag();
            //			//声明一个队列
            RequestQueue queue = Volley.newRequestQueue(mContext);
            imageView.setDefaultImageResId(R.mipmap.ic_launcher);
            imageView.setErrorImageResId(R.mipmap.ic_launcher);
            imageView.setImageUrl(url, new ImageLoader(queue, new BitmapCatch()));
    }

    /**
     * 实现带缓存得分imageCatch
     */
    public class BitmapCatch implements ImageLoader.ImageCache {

        private int maxCatch=10*1024*1024;

        public BitmapCatch() {
            mcatch=new LruCache<>(maxCatch);
        }

        @Override
        public Bitmap getBitmap(String s) {
            return mcatch.get(s);
        }
        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            mcatch.put(s,bitmap);
        }
    }

}
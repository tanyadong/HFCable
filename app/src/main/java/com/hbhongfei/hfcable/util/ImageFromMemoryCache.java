package com.hbhongfei.hfcable.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

public class ImageFromMemoryCache {
    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。
     * 硬引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    private static final int SOFT_CACHE_SIZE = 15;  //软引用缓存容量
    private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache;  //软引用缓存
    private static ImageFromMemoryCache imageCacheInstance = null;
    
    public ImageFromMemoryCache() {
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_SIZE, 0.75f, true) {
            private static final long serialVersionUID = 6040103833179403728L;
            @Override
            protected boolean removeEldestEntry(Entry<String, SoftReference<Bitmap>> eldest) {
                if (size() > SOFT_CACHE_SIZE){   
                    return true; 
                } 
                return false;
            }
        };
    }
                                                                                  
    /**
     * 从缓存中获取图片
     */
    public static Bitmap getBitmapFromCache(String url) {

        if( imageCacheInstance == null ){
    		imageCacheInstance = new ImageFromMemoryCache();
    	}
    	//Log.i("ImageFromMemoryCache", "getBitmapFromCache url:" + url);
        Bitmap bitmap;
//        //先从硬引用缓存中获取
//        synchronized (mLruCache) {
//            bitmap = mLruCache.get(url);
//            if (bitmap != null) {
//                //如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是最后被删除
//                mLruCache.remove(url);
//                mLruCache.put(url, bitmap);
//                Log.i("ImageFromMemoryCache", "find in LruCache");
//                return bitmap;
//            }
//        }
        //如果硬引用缓存中找不到，到软引用缓存中找
        synchronized (mSoftCache) {
            SoftReference<Bitmap> bitmapReference = mSoftCache.get(url);
            if (bitmapReference != null) {
                bitmap = bitmapReference.get();
                if (bitmap != null) {
                    //将图片移回硬缓存
//                    mLruCache.put(url, bitmap);
//                    mSoftCache.remove(url);
                    Log.i("ImageFromMemoryCache", "find in mSoftCache");
                    return bitmap;
                } else {
                    mSoftCache.remove(url);
                }
            }
        }
        return null;
    }
                                                                                  
    /**
     * 添加图片到缓存
     */
    public static void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (mSoftCache) {
            	mSoftCache.put(url, new SoftReference<Bitmap>(bitmap));
            }
        }
    }
                                                                                  
    public void clearCache() {
        mSoftCache.clear();
    }
}
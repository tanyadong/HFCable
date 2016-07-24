package com.hbhongfei.hfcable.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.hbhongfei.hfcable.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoader2 {
	private Context mContext = null;
	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	private List<String> urlList = new ArrayList<String>();
	private ExecutorService executorService = Executors.newFixedThreadPool(5);  // 固定五个线程来执行任务
	private static ImageLoader2 mImageLoader = null;
	public static ImageLoader2 getInstance(Context context) {
		if (null == mImageLoader) {
			mImageLoader = new ImageLoader2(context);
		}
		return mImageLoader;
	}

	private ImageLoader2(Context context) {
		mContext = context;
		fileCache = new FileCache(mContext);
	}

	public void loadImage(String url, NetworkImageView networkImageView) {
		Bitmap bm = memoryCache.get(url);
		//内存中不存在，在sd卡查找
		if (null != bm) {
			networkImageView.setImageBitmap(bm);
		}else {
			File f = fileCache.getFile(url);
			Bitmap fbm = decodeFile(f);
			//在sd卡不存在，网络加载
			if (fbm != null) {
				String iurl = (String) networkImageView.getTag();
				if (iurl.equals(url)) {
					networkImageView.setImageBitmap(fbm);
				} else {
					networkImageView.setImageResource(R.drawable.arrow_down);
				}
			} else {
				networkImageView.setImageResource(R.drawable.arrow_down);
				executorService.execute(new LoadThread(url, networkImageView));
//				downLoadPic(url, networkImageView);
			}
		}

	}
	/**
	 * 实现带缓存得分imageCatch
	 */
	public class BitmapCatch implements ImageLoader.ImageCache{
		private LruCache<String,Bitmap> mcatch;
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
	class LoadThread implements Runnable{
		String iUrl = "";
		NetworkImageView imageView = null;
		LoadThread(String url, NetworkImageView image) {
			iUrl = url;
			imageView = image;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(300);
			} catch (Exception e) {
				e.printStackTrace();
			}

			downLoadPic(iUrl);
			File f = null;
			f = fileCache.getFile(iUrl);
			final Bitmap bitmap = decodeFile(f);
			if (null != bitmap) {
				String url = (String) imageView.getTag();
				if (!iUrl.equals(url)) {
					return;
				}
				Activity activity = (Activity) imageView.getContext();
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						imageView.setImageBitmap(bitmap);
						urlList.remove(iUrl);
						memoryCache.put(iUrl, bitmap);
					}
				});
			}
		}

	}
	/**
	 * 从sd卡加载图片
	 * @param f
	 * @return
	 */
	private Bitmap decodeFile(File f) {
		int ws = 320;
		int hs = 320;
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			o.inPreferredConfig = Bitmap.Config.RGB_565;
			o.inPurgeable = true;
			o.inInputShareable = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			int width_tmp = o.outWidth;
			int height_tmp = o.outHeight;
			int scale = 1;

			while (true) {
				if (width_tmp / 2 < ws || height_tmp / 2 < hs)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 从网络下载图片
	 * @param url
	 * @return
	 */
	private void downLoadPic(String url) {
		File file = fileCache.getFile(url);
		if (!file.exists()) {
			// from web
			try {
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl
						.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(file);
				Utils.CopyStream(is, os);
				os.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}
	/**
	 * 从网络下载图片
	 * @param url
	 * @return
	 */
//	private void downLoadPic(String url, NetworkImageView networkImageView) {
//		File file = fileCache.getFile(url);
//		if (!file.exists()) {
//			// from web
//			//声明一个队列
//			RequestQueue queue= Volley.newRequestQueue(mContext);
//			networkImageView.setDefaultImageResId(R.mipmap.ic_launcher);
//			networkImageView.setErrorImageResId(R.mipmap.ic_launcher);

//		}
//
//	}
}

package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class AsyncBitmapLoader {

	/**
	 * 内存图片软引用缓冲
	 */
	private ImageCallBack imageCallBack = null;
	private ImageView imageView = null;
	private static String TAG = "AnysncBitmapLoader";
	Context mcontent;
	public AsyncBitmapLoader() {
	}


	public void loadImage(Context context, final ImageView imageView, final String imageURL) {
		mcontent=context;
		Bitmap resultBitmap = null;
		resultBitmap = ImageFromMemoryCache.getBitmapFromCache(imageURL);
		//内存中不存在，在sd卡查找
		if (null != resultBitmap) {
			imageView.setImageBitmap(resultBitmap);
		} else {
			Bitmap fbm = ImageFromFileCache.getImage(imageURL);
			//在sd卡不存在，网络加载
			if (fbm != null) {
				String iurl = (String) imageView.getTag();
				if (iurl.equals(imageURL)) {
					imageView.setImageBitmap(fbm);
				}else{
//					imageView.setImageResource(R.mipmap.ic_launcher);
				}
			} else {
//				imageView.setImageResource(R.mipmap.ic_launcher);
//				executorService.execute(new LoadThread(url, image));
				new Thread(new Runnable() {
					@Override
					public void run() {
						ImageFromHttp.getImg(mcontent,imageURL,imageView);
					}
				}).start();
			}
		}
	}

	/**
	 * 回调接口
	 * @author onerain
	 *
	 */
	public interface ImageCallBack
	{
		public void imageLoad(ImageView imageView, Bitmap bitmap);
	}
}
	
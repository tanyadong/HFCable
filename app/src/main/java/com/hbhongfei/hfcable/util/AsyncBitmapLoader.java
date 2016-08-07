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
//	private Handler handler = new Handler() {
//		/* (non-Javadoc)
//		 * @see android.os.Handler#handleMessage(android.os.Message)
//		 */
//		@Override
//		public void handleMessage(Message msg)
//		{
//			// TODO Auto-generated method stub
//			if( (imageCallBack == null) || (imageView == null) ) {
//				Log.e(TAG, "handler handleMessage imageCallBack or imageView is null");
//				return;
//			}
//
//			imageCallBack.imageLoad(imageView, (Bitmap)msg.obj);
//		}
//	};
//
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

//	public Bitmap loadBitmap(Context context,final ImageView imageView, final String imageURL)
//	{
//		mcontent=context;
//		Bitmap resultBitmap = null;
////		this.imageCallBack = imageCallBack;
//		this.imageView = imageView;
//		resultBitmap = ImageFromMemoryCache.getBitmapFromCache(imageURL);
//		if (resultBitmap!=null){
//			imageView.setImageBitmap(resultBitmap);
//
//		}else if (ImageFromFileCache.getImage(imageURL)!=null){
//			imageView.setImageBitmap(ImageFromFileCache.getImage(imageURL));
//		}else
////		do {
////			resultBitmap = ImageFromMemoryCache.getBitmapFromCache(imageURL);
////
////		    if (resultBitmap != null) {
////		        // 文件缓存中获取
////		    	resultBitmap = ImageFromFileCache.getImage(imageURL);
////		    	imageView.setImageBitmap(resultBitmap);
////		        if (resultBitmap == null) {
////
////					//如果不在内存缓存中，也不在本地（被jvm回收掉），则开启线程下载图片
////					new Thread()
////					{
////						/* (non-Javadoc)
////						 * @see java.lang.Thread#run()
////						 */
////						@Override
////						public void run()
////						{
////							// TODO Auto-generated method stu
//////						Bitmap bitmap = ImageFromHttp.downloadBitmap(imageURL);
//////							if(bitmap != null){
//////								ImageFromFileCache.saveBitmap(imageURL, bitmap);
//////				            	ImageFromMemoryCache.addBitmapToCache(imageURL, bitmap);
//////
//////								Message msg = handler.obtainMessage(0, bitmap);
//////								handler.sendMessage(msg);
//////				            	Log.i(TAG, "getImageByUrl find from http ");
//////							}
////							ImageFromHttp.getImg(mcontent,imageURL,imageView);
////						}
////					}.start();
////
////		        } else {
////		        	ImageFromMemoryCache.addBitmapToCache(imageURL, resultBitmap);
////		        	return resultBitmap;
////		        }
////		    }
////		    else{
////		    	Log.i(TAG, "getImageByUrl find in memory");
////		    	break;
////		    }
////
////		}while(false);
////
//	    return resultBitmap;
//	}
		
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
	
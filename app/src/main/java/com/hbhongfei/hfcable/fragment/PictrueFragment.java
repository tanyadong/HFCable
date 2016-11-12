package com.hbhongfei.hfcable.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.Url;

/*	
 **��ʾ��ͼ��ʵ�֣����ҿ��ԷŴ���С
 *zzn
 *QQ728121881
 */
@SuppressLint("ValidFragment")
public class PictrueFragment extends Fragment {

	private String image;
	private WindowManager windowManager;
	@SuppressLint("ValidFragment")
	public PictrueFragment(String image) {
		this.image=image;
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.scale_pic_item, null);
		setStatusBarColor();
		initView(view);
		return view;
	}

	private void initView(View view) {
		ImageView imageView = (ImageView) view.findViewById(R.id.scale_pic_item);
		//获取图片并显示

		String url = Url.url(image);
		Glide.with(getActivity())
				.load(url)
				.placeholder(R.mipmap.background)
				.error(R.mipmap.loading_error)
				.diskCacheStrategy(DiskCacheStrategy.ALL)
				.into(imageView );

	}
	/**
	* @author  谭亚东
	* @Title:
	* @Description: 设置状态栏颜色为黑色
	* @date 2016/11/11 22:22
	*/
	public  void setStatusBarColor(){
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		Window window = getActivity().getWindow();
		//取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		//需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
		//设置状态栏颜色
		window.setStatusBarColor(getResources().getColor(R.color.black));
	}

	}
}

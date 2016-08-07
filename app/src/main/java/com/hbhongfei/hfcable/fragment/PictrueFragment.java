package com.hbhongfei.hfcable.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Url;

/*	
 **��ʾ��ͼ��ʵ�֣����ҿ��ԷŴ���С
 *zzn
 *QQ728121881
 */
@SuppressLint("ValidFragment")
public class PictrueFragment extends Fragment {

	private int resId;
	private String image;
	@SuppressLint("ValidFragment")
	public PictrueFragment(String image) {
		this.image=image;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.scale_pic_item, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		ImageView imageView = (ImageView) view.findViewById(R.id.scale_pic_item);
		//获取图片并显示

		String url = Url.url(image);
		imageView.setTag(url);
		AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
		asyncBitmapLoader.loadImage(getContext(),imageView,url);
//		imageView.setImageResource(resId);

	}

}

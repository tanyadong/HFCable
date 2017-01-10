package com.hbhongfei.hfcable.adapter;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImagePaperAdapter extends PagerAdapter {

	private ArrayList<ImageView>list;
	public ImagePaperAdapter(ArrayList<ImageView> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
    @Override    
    public void destroyItem(ViewGroup container, int position,    
            Object object) {    
        //Warning：不要在这里调用removeView  
    }
	
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView view = list.get(position) ;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//版本在5.0以上才能添加此动画
			view.setTransitionName("transition_book_img");
		}
		ViewParent vp =  view.getParent();
		if(vp != null){
			ViewGroup parent = (ViewGroup)vp;
			parent.removeView(view);
		}
		//上面这些语句必须加上，如果不加的话，就会产生则当用户滑到第四个的时候就会触发这个异常
		//原因是我们试图把一个有父组件的View添加到另一个组件。
		((ViewPager)container).addView(list.get(position));
		return list.get(position);
	}

}
package com.hbhongfei.hfcable.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.fragment.PictrueFragment;
import com.zdp.aseo.content.AseoZdpAseo;

/*	
*显示大图
*/
public class ShowBigPictrue extends FragmentActivity {

	private ViewPager viewPager;
	private int[] resId = { R.mipmap.main_img1, R.mipmap.main_img2,
			R.mipmap.main_img3,R.mipmap.main_img4};
	/**得到上一个界面点击图片的位置*/
	private int position=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置无标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_bigpicture);
		Intent intent=getIntent();
		position=intent.getIntExtra("position", 0);
		AseoZdpAseo.initType(this, AseoZdpAseo.INSERT_TYPE);
		initViewPager();
	}
	
private void initViewPager(){
		
		viewPager = (ViewPager) findViewById(R.id.viewPager_show_bigPic);
		ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		//��ת���ڼ�������
		viewPager.setCurrentItem(position);
		
	}
	
	private class ViewPagerAdapter extends FragmentStatePagerAdapter{

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			int show_resId=resId[position];
			return new PictrueFragment(show_resId);
		}

		@Override
		public int getCount() {
			return resId.length;
		}

		
	}

}

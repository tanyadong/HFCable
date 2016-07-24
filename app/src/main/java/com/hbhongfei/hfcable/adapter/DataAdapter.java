package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.ImageLoader1;
import com.hbhongfei.hfcable.util.Information;

import java.util.ArrayList;
import java.util.List;


public class DataAdapter extends BaseAdapter {
	Context mContext = null;
	LayoutInflater inflater;
	List<Information> newsData = new ArrayList<Information>();
	public DataAdapter(Context context, List<Information> nList) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		newsData = nList;
	}

	@Override
	public int getCount() {
		return newsData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView hView = null;
		if (null == convertView) {
			hView = new HolderView();
			convertView = inflater.inflate(R.layout.information_layout, null);
			hView.image = (NetworkImageView) convertView.findViewById(R.id.info_img);
			hView.title = (TextView) convertView.findViewById(R.id.info_title_textView);
			hView.brief = (TextView)convertView.findViewById(R.id.info_content_textVeiw);
			convertView.setTag(hView);
		} else {
			hView = (HolderView) convertView.getTag();
		}
		hView.title.setText(newsData.get(position).getTitle());
//		if(!"".equals(newsData.get(position).getContent()) && newsData.get(position).getContent().length()>26){
//			String str=newsData.get(position).getContent().trim().substring(0, 24);
//			Toast.makeText(mContext,newsData.get(position).getContent()+"dddddddddddd",Toast.LENGTH_SHORT).show();
//			hView.brief.setText(NetUtil2.replaceBlank(str));
//		} else {
//			hView.brief.setText("");
//		}
		hView.brief.setText(newsData.get(position).getBrief());
		
//		if (NetUtil2.CURRENT_SPEAK.equals(newsData.get(position).getUrl())) {
//			hView.speak.setVisibility(View.VISIBLE);
//			hView.title.setTextColor(Color.RED);
//		} else {
//			hView.speak.setVisibility(View.GONE);
//			hView.title.setTextColor(Color.WHITE);
//		}


		if (null != newsData.get(position).getImgUrl()
				&& !"".equals(newsData.get(position).getImgUrl())) {
			hView.image.setTag(newsData.get(position).getImgUrl());
//			ImageLoader1 imageLoader=new ImageLoader1(mContext);
//			imageLoader.DisplayImage(newsData.get(position).getImgUrl(), hView.image);
			ImageLoader1.getInstance(mContext).loadImage(newsData.get(position).getImgUrl(), hView.image);
		} else {
			hView.image.setImageResource(R.drawable.icon_image_default);
		}
		
		return convertView;
	}

	public class HolderView {
		private NetworkImageView image = null;
		private TextView brief= null;
		private TextView title = null;
	}
	
}

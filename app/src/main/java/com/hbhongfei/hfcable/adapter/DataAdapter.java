package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
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
	public List<Information> addItems(List<Information> list){
		newsData.addAll(list);
		notifyDataSetChanged();
		return newsData;
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
			convertView = inflater.inflate(R.layout.list_item_card, null);
			hView.image = (ImageView) convertView.findViewById(R.id.info_img);
			hView.title = (TextView) convertView.findViewById(R.id.info_titlecard_textView);
			hView.brief = (TextView)convertView.findViewById(R.id.info_content_textVeiw);
			hView.time= (TextView) convertView.findViewById(R.id.info_time_textView);
			convertView.setTag(hView);
		} else {
			hView = (HolderView) convertView.getTag();
		}
		hView.title.setText(newsData.get(position).getTitle());
		hView.brief.setText(newsData.get(position).getBrief());
		hView.time.setText(newsData.get(position).getTime());


		if (null != newsData.get(position).getImgUrl()
				&& !"".equals(newsData.get(position).getImgUrl())) {
			hView.image.setTag(newsData.get(position).getImgUrl());
			AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
			asyncBitmapLoader.loadImage(mContext,hView.image,newsData.get(position).getImgUrl());
		} else {
			hView.image.setImageResource(R.drawable.icon_image_default);
		}
		return convertView;
	}

	public class HolderView {
		private ImageView image = null;
		private TextView brief= null;
		private TextView title = null;
		private TextView time=null;
	}
	
}

package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.InfoDetailActivity;
import com.hbhongfei.hfcable.util.Information;
import com.hbhongfei.hfcable.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;


public class DataAdapter extends BaseAdapter {
	Context mContext = null;
	LayoutInflater inflater;
	List<Information> newsData = null;
	public DataAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		if (newsData == null) {
			newsData = new ArrayList<>();
		}
	}

	public List<Information> addItem(Information information) {
		newsData.add(information);
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
	public View getView(final int position, View convertView, ViewGroup parent) {

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
			Glide.with(mContext)
					.load(newsData.get(position).getImgUrl())
					.placeholder(R.mipmap.background)
					.error(R.mipmap.loading_error)
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(hView.image);
		} else {
			hView.image.setImageResource(R.mipmap.loading_error);
		}
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, InfoDetailActivity.class);
				intent.putExtra("data", newsData.get(position));
				mContext.startActivity(intent);
			}
		});

		return convertView;
	}

	public class HolderView {
		private ImageView image = null;
		private TextView brief= null;
		private TextView title = null;
		private TextView time=null;
	}
	
}

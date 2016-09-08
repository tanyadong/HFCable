package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hbhongfei.hfcable.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 苑雪元 on 2016/9/7.
 */
public class MineImageAdapter extends BaseAdapter {
    private List<String> mDataList = new ArrayList<String>();
    LayoutInflater inflater;
    Context context;
    public MineImageAdapter(Context context,ArrayList<String> list){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.mDataList = list;
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView hView = null;
        if (null == convertView) {
            hView = new HolderView();
            convertView = inflater.inflate(R.layout.item_publish, null);
            hView.image = (ImageView) convertView.findViewById(R.id.item_grid_image);

            convertView.setTag(hView);
        } else {
            hView = (HolderView) convertView.getTag();
        }

//        if

        return convertView;
    }

    public class HolderView {

        private ImageView image= null;
    }



}

package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 2列ListView的适配器
 * @author tongleer.com
 *
 */
public class MyAdapter_myShopping extends BaseAdapter implements View.OnClickListener {
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    protected ArrayList<String> list;
    protected ViewHolder vh;
    public MyAdapter_myShopping(Context context, int resource, ArrayList<String> list){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        if(list==null){
            this.list=new ArrayList<>();
        }else{
            this.list = list;
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        vh = null;
        if (convertView == null ) {
            convertView = inflater.inflate(resource, null);
            vh = new ViewHolder();
            vh.select = (ImageView) convertView.findViewById(R.id.Image_myShoppingIntention_select);
            vh.image = (ImageView) convertView.findViewById(R.id.Image_myShoppingIntention_image);
            vh.name = (TextView) convertView.findViewById(R.id.Tview_myShoppingIntention_name);
            vh.introduce = (TextView) convertView.findViewById(R.id.Tview_myShoppingIntention_introduce);
            vh.money = (TextView) convertView.findViewById(R.id.Tview_myShoppingIntention_money);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        //赋值
        vh.select.setOnClickListener(this);
        for (String s:list){
            vh.name.setText(s);
        }
        /*
        int distance =  list.size() - position*2;
        int cellCount = distance >= 2? 2:distance;
        final List<String> itemList = list.subList(position*2,position*2+cellCount);
        if (itemList.size() >0) {
            vh.tv1.setText(itemList.get(0));
            vh.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, itemList.get(0)+"----"+position, Toast.LENGTH_SHORT).show();
                }
            });
            if (itemList.size() >1){
                vh.tv2.setVisibility(View.VISIBLE);
                vh.tv2.setText(itemList.get(1));
                vh.layout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, itemList.get(1), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                vh.tv2.setVisibility(View.INVISIBLE);
            }
        }*/
        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Image_myShoppingIntention_select:
                vh.select.setImageResource(R.mipmap.selected);
                break;
        }
    }

    /**
     * 封装ListView中item控件以优化ListView
     * @author tongleer
     *
     */
    public static class ViewHolder{
        ImageView select,image;
        TextView name,introduce,money;
    }
}
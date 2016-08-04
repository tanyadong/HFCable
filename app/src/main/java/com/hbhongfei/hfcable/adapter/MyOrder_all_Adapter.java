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

import java.util.List;
import java.util.Map;

/**
 * Created by 苑雪元 on 2016/8/3.
 */
public class MyOrder_all_Adapter extends BaseAdapter {

    private Context context;
    protected LayoutInflater inflater;
    protected int resource;
    private List<Map<String,String>> list;
    public MyOrder_all_Adapter(Context context,int resource, List<Map<String,String>> list){
        inflater = LayoutInflater.from(context);
        this.context =context;
        this.list = list;
        this.resource = resource;
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
        MyOrderViewHolder vh = null;
        if (convertView == null) {
            convertView = inflater.inflate(resource,null);
            vh = new MyOrderViewHolder();
            vh.Tview_myOrder_type = (TextView) convertView.findViewById(R.id.Tview_myOrder_type);
            vh.Tview_myOrder_name = (TextView) convertView.findViewById(R.id.Tview_myOrder_name);
            vh.Tview_myOrder_introduce = (TextView) convertView.findViewById(R.id.Tview_myOrder_introduce);
            vh.Tview_myOrder_money = (TextView) convertView.findViewById(R.id.Tview_myOrder_money);
            vh.image_myOrder = (ImageView) convertView.findViewById(R.id.image_myOrder);
            vh.Image_myOrder_delete = (ImageView) convertView.findViewById(R.id.Image_myOrder_delete);
            convertView.setTag(vh);
        }else {
            vh = (MyOrderViewHolder) convertView.getTag();
        }

        //赋值
        vh.Tview_myOrder_type.setText(list.get(position).get("type"));
        vh.Tview_myOrder_name.setText(list.get(position).get("name"));
        vh.Tview_myOrder_introduce.setText(list.get(position).get("introduce"));
        vh.Tview_myOrder_money.setText(list.get(position).get("money"));
        //设置图片
//        vh.image_myOrder.setImageResource();
        vh.Image_myOrder_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除操作
                Toast.makeText(context,list.get(position).get("name"),Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }


    public static class MyOrderViewHolder{
        TextView Tview_myOrder_type;
        ImageView image_myOrder;
        ImageView Image_myOrder_delete;
        TextView Tview_myOrder_name;
        TextView Tview_myOrder_introduce;
        TextView Tview_myOrder_money;
    }
}

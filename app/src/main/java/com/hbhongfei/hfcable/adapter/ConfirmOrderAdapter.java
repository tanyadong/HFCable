package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Url;

import java.util.List;
import java.util.Map;

/**
 * Created by 苑雪元 on 2016/9/3.
 */
public class ConfirmOrderAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,Object>> list;
    private Map<String, Object> map;
    private LayoutInflater layoutInflater;

    public ConfirmOrderAdapter(Context context,List<Map<String,Object>> list){
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        if (convertView==null){
            convertView = layoutInflater.inflate(R.layout.content_comfirm_order_products, null);
            holder =new  viewHolder();
            holder.product_name = (TextView) convertView.findViewById(R.id.Tview_confirm_order_product_name);
            holder.introduce = (TextView) convertView.findViewById(R.id.Tview_confirm_order_product_introduce);
            holder.product_price = (TextView) convertView.findViewById(R.id.Tview_confirm_order_product_price);
            holder.product_num = (TextView) convertView.findViewById(R.id.Tview_confirm_order_product_num);
            holder.product_package = (TextView) convertView.findViewById(R.id.tv_unit_price);
            holder.head = (ImageView) convertView.findViewById(R.id.Image_confirm_order_picture);
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }

        //设置数据
        if (list.size()>0){
            map = list.get(position);//(String) map.get("product_name")
//        Toast.makeText(context,holder.product_name.getText().toString().trim(),Toast.LENGTH_SHORT).show();
        holder.product_name.setText((String) map.get("product_name"));
        holder.introduce.setText((String)map.get("introduce"));
        holder.product_price.setText((Double)map.get("product_price")+"");
        holder.product_num.setText((Integer) map.get("product_num")+"");
        holder.product_package.setText("单位:"+(String)map.get("product_package"));
        String url = Url.url((String)map.get("product_iamge"));
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.head);
        }

        return convertView;
    }

    public class viewHolder{
        TextView product_name;
        TextView introduce;
        TextView product_price;
        TextView product_num;
        TextView product_package;
        ImageView head;
    }
}

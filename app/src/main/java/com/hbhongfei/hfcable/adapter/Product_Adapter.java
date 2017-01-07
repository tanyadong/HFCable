package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.util.Url;

import java.util.ArrayList;

public class Product_Adapter extends BaseAdapter {
    private ArrayList<Product> product_list;//数据源
    private Context context;
    private Product product;
    private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
    public onItemClickListener itemClickListener;// 接口回调
    private int selectedPosition;// 选中的位置
    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public Product_Adapter(ArrayList<Product> list, Context context) {
        super();  
        this.mInflater = LayoutInflater.from(context);  
        this.product_list = list;
        this.context=context;
    }

    @Override  
    public int getCount() {  
        return product_list.size();
    }  
  
    @Override  
    public Object getItem(int position) {  
        return product_list.get(position);
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {  
            holder = new ViewHolder();  
            convertView = mInflater.inflate(R.layout.type_two_product_item, null);
            /** 得到各个控件的对象 */
            holder.cable_img = (ImageView) convertView.findViewById(R.id.type_two_cable_img);//线缆图
            holder.cable_name= (TextView) convertView.findViewById(R.id.type_two_cable_name_tv);//线缆名
            holder.cable_price= (TextView) convertView.findViewById(R.id.type_two_cable_price_tv);//线缆价格
            holder.cable_description= (TextView)convertView.findViewById(R.id.type_two_cable_description_tv);//线缆描述
            holder.cable_new= (ImageView) convertView.findViewById(R.id.type_two_cable_new_img);//线缆是否是最新产品
            holder.layout = (LinearLayout) convertView.findViewById(R.id.type_two_product_ll);
            convertView.setTag(holder);// 绑定ViewHolder对象  
        } else {  
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象  
        }
        /** 设置TextView显示的内容，即我们存放在动态数组中的数据 */
        product = product_list.get(position);
        holder.cable_name.setText(product.getSpecifications());
        holder.cable_price.setText(product.getPrice()+"");
        holder.cable_description.setText(product.getIntroduce());
        //新产品显示改字段
        if (product.getIsNew().equals("是")) {//是新产品
            holder.cable_new.setVisibility(View.VISIBLE);
        } else {
            holder.cable_new.setVisibility(View.GONE);
        }
        //有图片时加载
        if (product.getProductImages().size()!=0) {
            String url= Url.url(product.getProductImages().get(0));
            //加载图片
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.background)
                    .error(R.mipmap.loading_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.cable_img);
        } else {
            holder.cable_img.setImageResource(R.mipmap.loading_error);
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override  
            public void onClick(View v) {  
                if (itemClickListener != null) {
                        itemClickListener.onItemClick(product_list.get(position), position);
                }
            }  
        });
        return convertView;
    }  
  
    public final class ViewHolder {
        public ImageView cable_img;
        public TextView cable_name;
        public TextView cable_price;
        public TextView cable_description;
        public ImageView cable_new;
        public LinearLayout layout;
    }
  
    public interface onItemClickListener {  
        public void onItemClick(Product product, int position);
    }  
}  
package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.Product;

import java.util.ArrayList;

public class Product_Adapter extends BaseAdapter {
    private ArrayList<Product> list;//数据源
    private Context context;
    private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局
    public onItemClickListener itemClickListener;// 接口回调
    private int selectedPosition;// 选中的位置
    public void setItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public Product_Adapter(ArrayList<Product> list, Context context) {
        super();  
        this.mInflater = LayoutInflater.from(context);  
        this.list = list;
        this.context=context;
    }

    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return list.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return list.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
        return position;  
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub  
        ViewHolder holder;  
        if (convertView == null) {  
            holder = new ViewHolder();  
            convertView = mInflater.inflate(R.layout.gridview_item, null);
            /** 得到各个控件的对象 */  
            holder.title = (TextView) convertView.findViewById(R.id.ItemText);
            holder.layout = (LinearLayout) convertView.findViewById(R.id.grid_layout);
            convertView.setTag(holder);// 绑定ViewHolder对象  
        } else {  
            holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象  
        }
        /** 设置TextView显示的内容，即我们存放在动态数组中的数据 */  
        holder.title.setText(list.get(position).getSpecifications());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override  
            public void onClick(View v) {  
                // TODO Auto-generated method stub  
                if (itemClickListener != null) {  
                        itemClickListener.onItemClick(list.get(position), position);
                }
            }  
        });
        return convertView;
    }  
  
    public final class ViewHolder {  
        public TextView title;  
        public LinearLayout layout;  
    }  
  
    public interface onItemClickListener {  
        public void onItemClick(Product product, int position);
    }  
}  
package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.ProdectInfoActivity;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * 2列ListView的适配器
 * @author tongleer.com
 *
 */
public class MyAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    protected List<Product> list;
    protected String tag;
    public MyAdapter(Context context, int resource, List<Product> list){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        if(list==null){
            this.list=new ArrayList<>();
        }else{
            this.list = list;
        }
    }

    public MyAdapter(Context context, int resource, ArrayList<Product> list, String tag) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.resource = resource;
        if(list==null){
            this.list=new ArrayList<>();
        }else{
            this.list = list;
        }
        this.tag = tag;
    }

    @Override
    public int getCount() {
        if(list.size()%2>0) {
            return list.size()/2+1;
        } else {
            return list.size()/2;
        }
//        if(list == null){ return 0; }else{ return list.size()/2 + list.size()%2; }
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
        ViewHolder vh = null;
        if (convertView == null ) {
            convertView = inflater.inflate(resource, null);
            vh = new ViewHolder();
            vh.layout1= (LinearLayout) convertView.findViewById(R.id.prodectList_LLayout1);
            vh.layout2= (LinearLayout) convertView.findViewById(R.id.prodectList_LLayout2);
            vh.prodect_introduce1=(TextView)convertView.findViewById(R.id.prodect_introduce1);
            vh.prodect_introduce2=(TextView)convertView.findViewById(R.id.prodect_introduce2);
            vh.prodect_price= (TextView) convertView.findViewById(R.id.prodect_price);
            vh.prodect_price1= (TextView) convertView.findViewById(R.id.prodect_price1);
            vh.prodect_price2= (TextView) convertView.findViewById(R.id.prodect_price2);
            vh.prodect_imgView1= (ImageView) convertView.findViewById(R.id.prodect_imgView1);
            vh.prodect_imgView2= (ImageView) convertView.findViewById(R.id.prodect_imgView2);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }
        int distance =  list.size() - position*2;
        int cellCount = distance >= 2? 2:distance;
        final List<Product> itemList = list.subList(position*2,position*2+cellCount);
        if (itemList.size() >0) {
            vh.prodect_introduce1.setText(itemList.get(0).getProdectName());
            vh.prodect_price1.setText(String.valueOf(itemList.get(0).getPrice()));
//            Toast.makeText(context,itemList.get(0).getProductImages().size(),Toast.LENGTH_SHORT).show();
            //有图片时加载
            if(itemList.get(0).getProductImages()!=null){
               String url=Url.url(itemList.get(0).getProductImages().get(0));
            vh.prodect_imgView1.setTag(url);
            AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
            asyncBitmapLoader.loadImage(context,vh.prodect_imgView1,url);
            }else {
                vh.prodect_imgView1.setImageResource(R.drawable.icon_image_default);
            }
            vh.layout1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, ProdectInfoActivity.class);
                    intent.putExtra("product",itemList.get(0));
                    intent.putExtra("tag",tag);
                    context.startActivity(intent);
                }
            });
            if (itemList.size() >1){
                vh.layout2.setVisibility(View.VISIBLE);
                vh.layout2.setBackgroundResource(R.drawable.list_background);
                vh.prodect_introduce2.setVisibility(View.VISIBLE);
                vh.prodect_price2.setVisibility(View.VISIBLE);
                vh.prodect_imgView2.setVisibility(View.VISIBLE);
                vh.prodect_introduce2.setText(itemList.get(1).getProdectName());
                vh.prodect_price2.setText(String.valueOf(itemList.get(1).getPrice()));
                //有图片时加载
                if(itemList.get(1).getProductImages()!=null){
                    String url=Url.url(itemList.get(1).getProductImages().get(1));
                    vh.prodect_imgView2.setTag(url);
                    AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
                    asyncBitmapLoader.loadImage(context,vh.prodect_imgView2,url);
                }else {
                    vh.prodect_imgView2.setImageResource(R.drawable.icon_image_default);
                }
//                vh.prodect_imgView2.setImageResource(R.drawable.icon_image_default);
                vh.layout2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, ProdectInfoActivity.class);
                        intent.putExtra("product",itemList.get(1));
                        intent.putExtra("tag",tag);
                        context.startActivity(intent);
                    }
                });
            }else{

                vh.layout2.setVisibility(View.INVISIBLE);

            }
        }
        return convertView;
    }
    /**
     * 封装ListView中item控件以优化ListView
     * @author tongleer
     *
     */
    public static class ViewHolder{
        LinearLayout layout1;
        LinearLayout layout2;
        TextView prodect_introduce1,prodect_price1,prodect_price;
        TextView prodect_introduce2,prodect_price2;
        ImageView prodect_imgView2,prodect_imgView1;
    }
}
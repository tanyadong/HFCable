package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.ProdectInfoActivity;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.util.ScreenUtils;
import com.hbhongfei.hfcable.util.Url;

import java.util.ArrayList;
import java.util.List;

/**
 * 2列ListView的适配器
 * @author 谭亚东
 *
 */
public class MyAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected int resource;
    protected List<Product> list;
    protected String tag;
    private static final int ANIMATED_ITEMS_COUNT = 4;
    private boolean animateItems = false;
    private int lastAnimatedPosition = -1;
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

    public void addItem(List<Product> product)
    {
        list.addAll(product);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(list.size()%2>0) {
            return list.size()/2+1;
        } else {
            return list.size()/2;
        }
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
//            vh.layout2= (LinearLayout) convertView.findViewById(R.id.prodectList_LLayout2);
            vh.prodect_introduce1=(TextView)convertView.findViewById(R.id.prodect_introduce1);
//            vh.prodect_introduce2=(TextView)convertView.findViewById(R.id.prodect_introduce2);
//            vh.prodect_price= (TextView) convertView.findViewById(R.id.prodect_price);
            vh.prodect_price1= (TextView) convertView.findViewById(R.id.prodect_price1);
//            vh.prodect_price2= (TextView) convertView.findViewById(R.id.prodect_price2);
            vh.prodect_imgView1= (ImageView) convertView.findViewById(R.id.prodect_imgView1);
//            vh.prodect_imgView2= (ImageView) convertView.findViewById(R.id.prodect_imgView2);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder)convertView.getTag();
        }

        int distance =  list.size() - position*2;
        int cellCount = distance >= 2? 2:distance;
        final List<Product> itemList = list.subList(position*2,position*2+cellCount);
        if (itemList.size() >0) {
            runEnterAnimation(vh.layout1, position);
            vh.prodect_introduce1.setText(itemList.get(0).getSpecifications());
            vh.prodect_price1.setText(String.valueOf(itemList.get(0).getPrice()));
            //有图片时加载
            if(itemList.get(0).getProductImages().size()!=0){
               String url=Url.url(itemList.get(0).getProductImages().get(0));
                //加载图片
                Glide.with(context)
                        .load(url)
                        .placeholder(R.mipmap.background)
                        .error(R.mipmap.loading_error)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(vh.prodect_imgView1);
            }else {
                vh.prodect_imgView1.setImageResource(R.mipmap.loading_error);
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
                runEnterAnimation(vh.layout2, position);
                vh.layout2.setVisibility(View.VISIBLE);
                vh.layout2.setBackgroundResource(R.drawable.list_background);
                vh.prodect_introduce2.setVisibility(View.VISIBLE);
                vh.prodect_price2.setVisibility(View.VISIBLE);
                vh.prodect_imgView2.setVisibility(View.VISIBLE);
                vh.prodect_introduce2.setText(itemList.get(1).getSpecifications());
                vh.prodect_price2.setText(String.valueOf(itemList.get(1).getPrice()));
                //有图片时加载
                if(itemList.get(1).getProductImages().size()!=0){
                    String url=Url.url(itemList.get(1).getProductImages().get(0));
                    //加载图片
                    Glide.with(context)
                            .load(url)
                            .placeholder(R.mipmap.background)
                            .error(R.mipmap.loading_error)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(vh.prodect_imgView2);

                }else {
                    vh.prodect_imgView2.setImageResource(R.mipmap.loading_error);
                }
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

    private void runEnterAnimation(View view, int position) {
        if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(ScreenUtils.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setStartDelay(100 * position)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }
}
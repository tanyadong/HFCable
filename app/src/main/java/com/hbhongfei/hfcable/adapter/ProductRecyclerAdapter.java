package com.hbhongfei.hfcable.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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

import listener.RecyclerItemClickListener;


public class ProductRecyclerAdapter extends RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder> {
    protected List<Product> list;
    private static final int ANIMATED_ITEMS_COUNT = 4;
    private Activity context;
    private boolean animateItems = false;
    private int lastAnimatedPosition = -1;
    protected String tag;
    private Product product;
    public ProductRecyclerAdapter(Activity context , List<Product> mlist) {
        TypedValue mTypedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        list = updateItems(mlist,true);
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout layout1;
        public TextView prodect_introduce1, prodect_price1;
        public ImageView  prodect_imgView1;

        public int position;

        public ViewHolder(View v) {
            super(v);
            layout1= (LinearLayout) v.findViewById(R.id.prodectList_LLayout1);
            prodect_imgView1 = (ImageView) v.findViewById(R.id.prodect_imgView1);
            prodect_introduce1=(TextView)v.findViewById(R.id.prodect_introduce1);
            prodect_price1= (TextView) v.findViewById(R.id.prodect_price1);
        }
    }
    public void addItem(List<Product> product)
    {
        list.addAll(product);
        notifyDataSetChanged();
    }

    private void runEnterAnimation(View view, int position) {
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

    public List<Product> updateItems(List<Product> books, boolean animated) {
        List<Product> list = new ArrayList<>();
        animateItems = animated;
        lastAnimatedPosition = -1;
        list.addAll(books);
        notifyDataSetChanged();
        return list;
    }


    @Override
    public ProductRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.intentionlayout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        runEnterAnimation(holder.layout1, position);
        product = list.get(position);
        holder.prodect_introduce1.setText(product.getSpecifications());
        holder.prodect_price1.setText(String.valueOf(product.getPrice()));
        //有图片时加载
        if(product.getProductImages().size()!=0){
            String url= Url.url(product.getProductImages().get(0));
            //加载图片
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.background)
                    .error(R.mipmap.loading_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.prodect_imgView1);
        }else {
            holder.prodect_imgView1.setImageResource(R.mipmap.loading_error);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(context, ProdectInfoActivity.class);
            intent.putExtra("product",list.get(position));
            intent.putExtra("tag",tag);

            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                            view.findViewById(R.id.prodectList_LLayout1),context.getString(R.string.transition_book_img));
            ActivityCompat.startActivity(context, intent, options.toBundle());

        }
    };
}

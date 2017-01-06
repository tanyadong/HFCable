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
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.MarketChartActivity;
import com.hbhongfei.hfcable.pojo.MarketInfo;
import com.hbhongfei.hfcable.util.ScreenUtils;

import java.util.List;

import listener.RecyclerItemClickListener;


public class MarketRecyclerAdapter extends RecyclerView.Adapter<MarketRecyclerAdapter.ViewHolder> {
    protected List<MarketInfo> list;
    private static final int ANIMATED_ITEMS_COUNT = 4;
    private Activity context;
    private boolean animateItems = false;
    private int lastAnimatedPosition = -1;
    protected String tag;
    private MarketInfo marketInfo;
    public MarketRecyclerAdapter(Activity context , List<MarketInfo> mlist) {
        TypedValue mTypedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        list = mlist;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView market_area;
        public TextView market_product_name;
        public TextView market_average_price;
        public TextView market_fallorise;
        public TextView market_minprice;
        public TextView market_maxprice;
        public TextView market_data;

        public int position;

        public ViewHolder(View v) {
            super(v);
            market_area = (TextView) v.findViewById(R.id.market_area);
            market_average_price= (TextView) v.findViewById(R.id.market_average_price);
            market_fallorise= (TextView) v.findViewById(R.id.market_fallorise);
            market_maxprice= (TextView) v.findViewById(R.id.market_maxprice);
            market_minprice= (TextView) v.findViewById(R.id.market_minprice);
            market_product_name= (TextView) v.findViewById(R.id.market_product_name);
            market_data= (TextView) v.findViewById(R.id.market_data);
        }
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

//    public List<Product> updateItems(List<Product> books, boolean animated) {
//        List<Product> list = new ArrayList<>();
//        animateItems = animated;
//        lastAnimatedPosition = -1;
//        list.addAll(books);
//        notifyDataSetChanged();
//        return list;
//    }


    @Override
    public MarketRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_market, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        runEnterAnimation(holder.layout1, position);
        marketInfo = list.get(position);
        holder.market_area.setText(marketInfo.getArea());
        holder.market_product_name.setText(marketInfo.getProductName());
        holder.market_average_price.setText(marketInfo.getAveragePrice());
        holder.market_fallorise.setText(marketInfo.getFallOrise());
        if(Float.parseFloat(marketInfo.getFallOrise()) > 0){
            holder.market_fallorise.setTextColor(context.getResources().getColor(R.color.colorRed));
        }else{
            holder.market_fallorise.setTextColor(context.getResources().getColor(R.color.green));
        }
        holder.market_minprice.setText(marketInfo.getMinPrice());
        holder.market_maxprice.setText(marketInfo.getMaxPrice());
        holder.market_data.setText(marketInfo.getData());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(context, MarketChartActivity.class);
            intent.putExtra("marketInfo",list.get(position));
            intent.putExtra("tag",tag);

            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                            view.findViewById(R.id.market_ll),context.getString(R.string.transition_book_img));
            ActivityCompat.startActivity(context, intent, options.toBundle());

        }
    };
}

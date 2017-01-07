package com.hbhongfei.hfcable.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.MarketChartActivity;
import com.hbhongfei.hfcable.pojo.MarketInfo;

import java.util.List;

import listener.RecyclerItemClickListener;


public class MarketRecyclerAdapter extends RecyclerView.Adapter<MarketRecyclerAdapter.ViewHolder> {
    protected List<MarketInfo> list;
    private Activity context;
    protected String tag;
    private MarketInfo marketInfo;
    public MarketRecyclerAdapter(Activity context , List<MarketInfo> mlist) {
        if (list != null) {
            list.clear();
        }
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
            context.startActivity(intent);
        }
    };
}

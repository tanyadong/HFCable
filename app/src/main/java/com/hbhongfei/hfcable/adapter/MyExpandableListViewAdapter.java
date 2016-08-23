package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.MarketInfo;

import java.util.List;

/**
 * Created by 谭亚东 on 2016/8/22.
 * expandablelist列表，不同的原材料有各自的列表
 */
public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> group_list;
    private List<List<MarketInfo>> item_list;
    private ExpandableListView expandableListView;
//    private List<List<String>> item_list2;
    private LayoutInflater inflater;
    public MyExpandableListViewAdapter(Context context,List<String> group_list,List<List<MarketInfo>> item_list,ExpandableListView listView) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.item_list=item_list;
        this.group_list=group_list;
        this.expandableListView=listView;
    }

    @Override
    public int getGroupCount() {
        return group_list.size();
    }



    @Override
    public int getChildrenCount(int groupPosition) {
//        Toast.makeText()
        return item_list.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group_list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return item_list.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView=inflater.inflate(R.layout.market_parentlist_layout, null);
            groupHolder = new GroupHolder();
            groupHolder.txt = (TextView) convertView.findViewById(R.id.parent_text);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.txt.setText(group_list.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ItemHolder itemHolder = null;
//        Toast.makeText(context,item_list.toString(),Toast.LENGTH_SHORT).show();
        if (convertView == null) {
            convertView=inflater.inflate(R.layout.market_childlist_layout, null);
            itemHolder = new ItemHolder();
            itemHolder.market_area = (TextView) convertView.findViewById(R.id.market_area);
            itemHolder.market_average_price= (TextView) convertView.findViewById(R.id.market_average_price);
            itemHolder.market_factory_area= (TextView) convertView.findViewById(R.id.market_factory_area);
            itemHolder.market_fallorise= (TextView) convertView.findViewById(R.id.market_fallorise);
            itemHolder.market_maxprice= (TextView) convertView.findViewById(R.id.market_maxprice);
            itemHolder.market_minprice= (TextView) convertView.findViewById(R.id.market_minprice);
            itemHolder.market_product_name= (TextView) convertView.findViewById(R.id.market_product_name);
            itemHolder.market_data= (TextView) convertView.findViewById(R.id.market_data);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) convertView.getTag();
        }
        itemHolder.market_area.setText(item_list.get(groupPosition).get(
                childPosition).getArea());
        itemHolder.market_product_name.setText(item_list.get(groupPosition).get(
                childPosition).getProductName());
        itemHolder.market_average_price.setText(item_list.get(groupPosition).get(
                childPosition).getAveragePrice());
        itemHolder.market_fallorise.setText(item_list.get(groupPosition).get(
                childPosition).getFallOrise());
        if(Integer.parseInt(item_list.get(groupPosition).get(
                childPosition).getFallOrise())>0){
            itemHolder.market_fallorise.setTextColor(context.getResources().getColor(R.color.colorRed));
        }else{
            itemHolder.market_fallorise.setTextColor(context.getResources().getColor(R.color.green));
        }
        itemHolder.market_minprice.setText(item_list.get(groupPosition).get(
                childPosition).getMinPrice());
        itemHolder.market_maxprice.setText(item_list.get(groupPosition).get(
                childPosition).getMaxPrice());
        itemHolder.market_factory_area.setText(item_list.get(groupPosition).get(
                childPosition).getFactoryArea());
        itemHolder.market_data.setText(item_list.get(groupPosition).get(
                childPosition).getData());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void notifyDataSetChanged() {

        int groupCount = expandableListView.getCount();
        Toast.makeText(context,groupCount+"",Toast.LENGTH_SHORT).show();
        Log.i("-------", "groupCount="+groupCount);
        super.notifyDataSetChanged();
        for (int i=0; i<groupCount; i++) {
            expandableListView.expandGroup(i);
        };
    }
    class GroupHolder {
        public TextView txt;
    }

    class ItemHolder {
        public TextView market_area;
        public TextView market_product_name;
        public TextView market_average_price;
        public TextView market_fallorise;
        public TextView market_minprice;
        public TextView market_maxprice;
        public TextView market_factory_area;
        public TextView market_data;
    }
}





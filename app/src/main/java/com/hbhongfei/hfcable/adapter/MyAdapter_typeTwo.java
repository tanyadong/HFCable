package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.ProdectInfoActivity;
import com.hbhongfei.hfcable.pojo.Product;
import com.hbhongfei.hfcable.pojo.TypeTwo;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级子类的适配器
 * @author 谭亚东
 *
 */
public class MyAdapter_typeTwo extends BaseExpandableListAdapter {
    private List<TypeTwo> groups;
    private ArrayList<ArrayList<Product>> children;
    private Context context;

    public  int flag = 0;


    /**
     * 构造函数
     *
     * @param groups   组元素列表
     * @param children 子元素列表
     * @param context
     */
    public MyAdapter_typeTwo(List<TypeTwo> groups, ArrayList<ArrayList<Product>> children, Context context) {
        this.groups = groups;
        this.children = children;
        this.context = context;
    }
    public List<TypeTwo> addGroup(List<TypeTwo> list){
        groups.addAll(list);
        this.notifyDataSetChanged();
        return groups;
    }
    public ArrayList<ArrayList<Product>> addChild(ArrayList<ArrayList<Product>> list){
        children.addAll(list);
        this.notifyDataSetChanged();
        return children;
    }


    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return children.get(groupPosition).get(childPosition);
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
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final GroupViewHolder gholder;
        if (convertView == null) {
            gholder = new GroupViewHolder();
            convertView = View.inflate(context, R.layout.market_parentlist_layout, null);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.parent_text);//二级种类名
            gholder.img_type_two_direction = (ImageView) convertView.findViewById(R.id.type_two_direction_img);//向下向上箭头
            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
        }
        if (isExpanded) {//展开子项，为打开状态
            gholder.img_type_two_direction.setBackgroundResource(R.mipmap.type_two_up);
        } else {//隐藏子项，为关闭状态
            gholder.img_type_two_direction.setBackgroundResource(R.mipmap.type_two_down);
        }
        final TypeTwo group = (TypeTwo) getGroup(groupPosition);
        gholder.tv_group_name.setText(group.getTypeTwoName());
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {
        final ChildViewHolder cholder;
        if (convertView == null) {
            cholder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.product_grvidview_layout, null);
            cholder.gridView = (GridView) convertView.findViewById(R.id.gv_product);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag();
        }

        if(children.get(groupPosition)!=null) {
            Product_Adapter adapter = new Product_Adapter(children.get(groupPosition), context);
            cholder.gridView.setAdapter(adapter);
            adapter.setItemClickListener(new Product_Adapter.onItemClickListener() {
                @Override
                public void onItemClick(Product product, int position) {
                    Intent intent = new Intent(context, ProdectInfoActivity.class);
                    intent.putExtra("product", product);
                    context.startActivity(intent);
                }
            });
        }
        notifyDataSetChanged();
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;

    }

    /**
     * 组元素绑定器
     */
    private class GroupViewHolder {
        TextView tv_group_name;
        ImageView img_type_two_direction;
    }

    /**
     * 子元素绑定器
     */
    private class ChildViewHolder {
        GridView gridView;
    }
}
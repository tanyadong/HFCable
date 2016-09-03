package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.ProdectInfoActivity;
import com.hbhongfei.hfcable.entity.TypeInfo;
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
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    public  int flag = 0;
    private GroupEdtorListener mListener;

    public GroupEdtorListener getmListener() {
        return mListener;
    }

    public void setmListener(GroupEdtorListener mListener) {
        this.mListener = mListener;
    }

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

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
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
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.parent_text);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
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
        Product_Adapter adapter=new Product_Adapter(children.get(groupPosition),context);
        cholder.gridView.setAdapter(adapter);
        adapter.setItemClickListener(new Product_Adapter.onItemClickListener() {
            @Override
            public void onItemClick(Product product, int position) {
                Intent intent=new Intent(context, ProdectInfoActivity.class);
                intent.putExtra("product",product);
                context.startActivity(intent);
            }
        });
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
    }

    /**
     * 子元素绑定器
     */
    private class ChildViewHolder {
        GridView gridView;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子item
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }

    /**
     * 监听编辑状态
     */
    public interface GroupEdtorListener{
        void groupEdit(int groupPosition);
    }
    /**
     * 使某个组处于编辑状态
     * <p>
     * groupPosition组的位置
     */
    class GroupViewClick implements View.OnClickListener {
        private int groupPosition;
        private Button edtor;
        private TypeInfo group;

        public GroupViewClick(int groupPosition, Button edtor, TypeInfo group) {
            this.groupPosition = groupPosition;
            this.edtor = edtor;
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            int groupId = v.getId();
            if (groupId == edtor.getId()) {
                if (group.isEdtor()) {
                    group.setEdtor(false);
                } else {
                    group.setEdtor(true);

                }
                notifyDataSetChanged();
            }
        }
    }
}
package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.CablesInfo;
import com.hbhongfei.hfcable.pojo.TypeInfo;
import com.hbhongfei.hfcable.util.Url;

import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * 购物车的适配器
 * @author 苑雪元
 *
 */
public class MyAdapter_myShopping extends BaseExpandableListAdapter {
    private List<TypeInfo> groups;
    private Map<String, List<CablesInfo>> children;
    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    public  int flag = 0;
    private GroupEdtorListener mListener;
    private Map<String,String> map;

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
    public MyAdapter_myShopping(List<TypeInfo> groups, Map<String, List<CablesInfo>> children, Context context,Map<String,String> map) {
        this.groups = groups;
        this.children = children;
        this.context = context;
        this.map = map;
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
        String groupId = groups.get(groupPosition).getId();
        return children.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<CablesInfo> childs = children.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final GroupViewHolder gholder;
        if (convertView == null) {
            gholder = new GroupViewHolder();
            convertView = View.inflate(context, R.layout.item_shopcart_group, null);
            gholder.cb_check = (CheckBox) convertView.findViewById(R.id.determine_checkbox);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_source_name);
            gholder.store_edtor = (Button) convertView.findViewById(R.id.tv_store_edtor);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
        }
        final TypeInfo group = (TypeInfo) getGroup(groupPosition);

        gholder.tv_group_name.setText(group.getName());
        gholder.cb_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
            }
        });
        gholder.cb_check.setChecked(group.isChoosed());
        if (group.isEdtor()) {
            gholder.store_edtor.setText("完成");
        } else {
            gholder.store_edtor.setText("编辑");
        }
        gholder.store_edtor.setOnClickListener(new GroupViewClick(groupPosition,gholder.store_edtor,group));
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {
        final ChildViewHolder cholder;
        if (convertView == null) {
            cholder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.item_shopcart_product, null);
            cholder.cb_check = (CheckBox) convertView.findViewById(R.id.check_box);
            cholder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_name);
            cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            cholder.tv_package = (TextView) convertView.findViewById(R.id.tv_package);
            cholder.iv_increase = (TextView) convertView.findViewById(R.id.tv_add);
            cholder.iv_decrease = (TextView) convertView.findViewById(R.id.tv_reduce);
            cholder.tv_count = (TextView) convertView.findViewById(R.id.tv_num);
            cholder.rl_no_edtor = (RelativeLayout) convertView.findViewById(R.id.rl_no_edtor);


            cholder.tv_introduce = (TextView) convertView.findViewById(R.id.tv_introduce);
            cholder.tv_buy_num = (TextView) convertView.findViewById(R.id.tv_buy_num);
            cholder.ll_edtor = (LinearLayout) convertView.findViewById(R.id.ll_edtor);
            cholder.tv_introduce2 = (TextView) convertView.findViewById(R.id.tv_introduce2);
            cholder.tv_color = (TextView) convertView.findViewById(R.id.tv_color);
            cholder.tv_specifications = (TextView) convertView.findViewById(R.id.tv_specifications);
            cholder.tv_goods_delete = (TextView) convertView.findViewById(R.id.tv_goods_delete);
            cholder.iv_adapter_list_pic= (ImageView) convertView.findViewById(R.id.iv_adapter_list_pic);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag();
        }

        if (groups.get(groupPosition).isEdtor() == true) {
            cholder.ll_edtor.setVisibility(View.VISIBLE);
            cholder.rl_no_edtor.setVisibility(View.GONE);
        } else {
            cholder.ll_edtor.setVisibility(View.GONE);
            cholder.rl_no_edtor.setVisibility(View.VISIBLE);
        }
        final CablesInfo cablesInfo = (CablesInfo) getChild(groupPosition, childPosition);
        if (cablesInfo != null) {
            cholder.tv_product_name.setText(cablesInfo.getName());
            cholder.tv_price.setText("￥" + cablesInfo.getPrice());
            if (cablesInfo.getSpecifications().equals("1盘")){
                //盘的单价
                cholder.tv_package.setText("单位:100米/盘");
            }else if (cablesInfo.getSpecifications().equals("10米")){
                //10米的单价
                cholder.tv_package.setText("单位:10米");
            }else{
                cholder.tv_package.setText("单位:"+cablesInfo.getSpecifications()+"米/轴");
            }
            cholder.tv_count.setText(cablesInfo.getCount() + "");
            //加载图片
            String url = Url.url(cablesInfo.getGoodsImg());
            Glide.with(context)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(cholder.iv_adapter_list_pic);

            cholder.tv_introduce.setText(cablesInfo.getColor());
            cholder.tv_introduce2.setText(cablesInfo.getIntroduce());
            cholder.tv_color.setText(cablesInfo.getColor());
            if (cablesInfo.getSpecifications().equals("1盘")){
                cholder.tv_specifications.setText("单位:100米/盘");
            }else if (cablesInfo.getSpecifications().equals("10米")){
                cholder.tv_specifications.setText("单位:10米");
            }else{
                cholder.tv_specifications.setText("单位:"+cablesInfo.getSpecifications()+"米/轴");
            }
            cholder.tv_buy_num.setText("x" + cablesInfo.getCount());
            cholder.cb_check.setChecked(cablesInfo.isChoosed());
            cholder.cb_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cablesInfo.setChoosed(((CheckBox) v).isChecked());
                    cholder.cb_check.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
                }
            });
            cholder.iv_increase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露增加接口
                }
            });
            cholder.iv_decrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露删减接口
                }
            });
            //删除 购物车
            cholder.tv_goods_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定删除？")
                            .setContentText("删除将不可恢复")
                            .setConfirmText("删除")
                            .setCancelText("取消")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    modifyCountInterface.childDelete(groupPosition, childPosition);
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();

                }
            });
        }
        return convertView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    /**
     * 组元素绑定器
     */
    private class GroupViewHolder {
        CheckBox cb_check;
        TextView tv_group_name;
        Button store_edtor;
    }

    /**
     * 子元素绑定器
     */
    private class ChildViewHolder {
        CheckBox cb_check;
        ImageView iv_adapter_list_pic;
        TextView tv_product_name;
        TextView tv_price;
        TextView iv_increase;
        TextView tv_count;
        TextView iv_decrease;
        RelativeLayout rl_no_edtor;
        TextView tv_introduce;
        TextView tv_buy_num;
        LinearLayout ll_edtor;
        TextView tv_introduce2;
        TextView tv_goods_delete;
        TextView tv_color;//颜色
        TextView tv_specifications;//规格
        TextView tv_package;//包装方式
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
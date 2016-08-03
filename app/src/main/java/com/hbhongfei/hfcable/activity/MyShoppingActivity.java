package com.hbhongfei.hfcable.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyAdapter_myShopping;
import com.hbhongfei.hfcable.entity.CablesInfo;
import com.hbhongfei.hfcable.entity.TypeInfo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyShoppingActivity extends AppCompatActivity implements MyAdapter_myShopping.CheckInterface,
        MyAdapter_myShopping.ModifyCountInterface, MyAdapter_myShopping.GroupEdtorListener ,View.OnClickListener{
    private TextView  tvTotalPrice,tvDelete,tvGoToPay;
    private LinearLayout llInfo,llCart,cart_empty;
    private RelativeLayout llShar;
    private ExpandableListView exListView;
    private CheckBox allChekbox;

    private Context context;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private MyAdapter_myShopping selva;
    private List<TypeInfo> groups = new ArrayList<TypeInfo>();// 组元素数据列表
    private Map<String, List<CablesInfo>> children = new HashMap<String, List<CablesInfo>>();// 子元素数据列表
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping);
        context = this;
        initView();
        initDatas();
        setOnClick();

        initEvents();
    }
    /**
     * 初始化界面
     */
    private void initView(){
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvGoToPay = (TextView) findViewById(R.id.tv_go_to_pay);

        llShar = (RelativeLayout) findViewById(R.id.ll_shar);
        llInfo = (LinearLayout) findViewById(R.id.ll_info);
        llCart = (LinearLayout) findViewById(R.id.ll_cart);
        cart_empty = (LinearLayout) findViewById(R.id.layout_cart_empty);

        exListView = (ExpandableListView) findViewById(R.id.exListView);

        allChekbox = (CheckBox) findViewById(R.id.all_chekbox);
    }
    /**
     * 设置点击事件
     */
    private void setOnClick(){
        allChekbox.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        tvGoToPay.setOnClickListener(this);
    }

    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
     * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
     */
    private void initDatas() {
        for (int i = 0; i < 3; i++) {
            groups.add(new TypeInfo(i + "", "弘飞电缆的第" + (i + 1) + "个种类"));
            List<CablesInfo> cablesInfos = new ArrayList<CablesInfo>();
            for (int j = 0; j <= i; j++) {
                int[] img = {R.mipmap.main_img1, R.mipmap.main_img2, R.mipmap.main_img3, R.mipmap.main_img4, R.mipmap.main_img1, R.mipmap.main_img2};
                cablesInfos.add(new CablesInfo(j + "", "电缆"+(j+1), groups.get(i)
                        .getName() + "的第" + (j + 1) + "个电缆", 12.00 + new Random().nextInt(23), new Random().nextInt(5) + 1, img[i * j]));
            }
            children.put(groups.get(i).getId(), cablesInfos);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
        }

    }

    private void initEvents() {
        selva = new MyAdapter_myShopping(groups, children, this);
        selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
        selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
        selva.setmListener(this);//设置监听器接口
        exListView.setAdapter(selva);

        for (int i = 0; i < selva.getGroupCount(); i++) {
            exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCartNum();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        selva=null;
        groups.clear();
        totalPrice=0;
        totalCount=0;
        children.clear();
    }
    /**
     * 设置购物车产品数量
     */
    private void setCartNum() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(allChekbox.isChecked());
            TypeInfo group = groups.get(i);
            List<CablesInfo> childs = children.get(group.getId());
            for (CablesInfo cablesInfo : childs) {
                count += 1;
            }
        }

        //购物车已清空
        if(count==0){
            clearCart();
        } else{
        }
    }

    /**
     * 清除购物车
     */
    private void clearCart() {
        llCart.setVisibility(View.GONE);
        cart_empty.setVisibility(View.VISIBLE);
    }

    /**
     * 是否选中
     * @return
     */
    private boolean isAllCheck() {

        for (TypeInfo group : groups) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        TypeInfo group = groups.get(groupPosition);
        List<CablesInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            allChekbox.setChecked(true);
        else
            allChekbox.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosition, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        TypeInfo group = groups.get(groupPosition);
        List<CablesInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck()) {
            allChekbox.setChecked(true);// 全选
        } else {
            allChekbox.setChecked(false);// 反选
        }
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void groupEdit(int groupPosition) {
        groups.get(groupPosition).setEdtor(true);
        selva.notifyDataSetChanged();
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        CablesInfo product = (CablesInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {

        CablesInfo product = (CablesInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        if (currentCount == 1)
            return;
        currentCount--;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void childDelete(int groupPosition, int childPosition) {
        children.get(groups.get(groupPosition).getId()).remove(childPosition);
        TypeInfo group = groups.get(groupPosition);
        List<CablesInfo> childs = children.get(group.getId());
        if (childs.size() == 0) {
            groups.remove(groupPosition);
        }
        selva.notifyDataSetChanged();
        //     handler.sendEmptyMessage(0);
        calculate();
    }

    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++) {
            TypeInfo group = groups.get(i);
            List<CablesInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                CablesInfo cable = childs.get(j);
                if (cable.isChoosed()) {
                    totalCount++;
                    totalPrice += cable.getPrice() * cable.getCount();
                }
            }
        }

        tvTotalPrice.setText("￥" + totalPrice);
        tvGoToPay.setText("去支付(" + totalCount + ")");
        //计算购物车的金额为0时候清空购物车的视图
        if(totalCount==0){
            setCartNum();
        } else{
        }
    }

    @Override
    public void onClick(View v) {
        AlertDialog alert;
        switch (v.getId()) {
            //全部选或者反选
            case R.id.all_chekbox:
                doCheckAll();
                break;
            //删除电缆
            case R.id.tv_delete:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                doDelete();
                            }
                        });
                alert.show();
                break;
            case R.id.tv_go_to_pay:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.show();
                break;

        }
    }
    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
            groups.get(i).setChoosed(allChekbox.isChecked());
            TypeInfo group = groups.get(i);
            List<CablesInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(allChekbox.isChecked());
            }
        }
        selva.notifyDataSetChanged();
        calculate();
    }
    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete() {
        List<TypeInfo> toBeDeleteGroups = new ArrayList<TypeInfo>();// 待删除的组元素列表
        for (int i = 0; i < groups.size(); i++) {
            TypeInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<CablesInfo> toBeDeleteProducts = new ArrayList<CablesInfo>();// 待删除的子元素列表
            List<CablesInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                }
            }
            childs.removeAll(toBeDeleteProducts);
        }
        groups.removeAll(toBeDeleteGroups);
        //记得重新设置购物车
        setCartNum();
        selva.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_shopping, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (flag == 0) {
            llInfo.setVisibility(View.GONE);
            tvGoToPay.setVisibility(View.GONE);
            llShar.setVisibility(View.VISIBLE);
            item.setTitle("完成");
        } else if (flag == 1) {
            llInfo.setVisibility(View.VISIBLE);
            tvGoToPay.setVisibility(View.VISIBLE);
            llShar.setVisibility(View.GONE);
            item.setTitle("删除");
        }
        flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
        return super.onOptionsItemSelected(item);
    }
}

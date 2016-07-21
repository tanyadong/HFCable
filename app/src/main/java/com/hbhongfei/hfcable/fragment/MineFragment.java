package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.MyFavoriteActivity;
import com.hbhongfei.hfcable.activity.MyInfoActivity;
import com.hbhongfei.hfcable.activity.MyOrderActivity;
import com.hbhongfei.hfcable.activity.MyShoppingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    private LinearLayout myInfoEdit;
    private ImageView myhead;
    private TextView myName;
    private String S_name;
    private RelativeLayout shopping,order,favorite;


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(v);
        setOnClick();
        initValues();
        return v;
    }

    /**
     * 初始化界面
     * 苑雪元
     * 2016/07/21
     * @param v 主界面
     */
    public void initView(View v){
        myInfoEdit = (LinearLayout) v.findViewById(R.id.mine_edit);
        myhead = (ImageView) v.findViewById(R.id.Iamge_mine_head);
        myName = (TextView) v.findViewById(R.id.Tview_mine_myName);
        shopping = (RelativeLayout) v.findViewById(R.id.Rlayout_mine_shopping);
        order = (RelativeLayout) v.findViewById(R.id.Rlayout_mine_order);
        favorite = (RelativeLayout) v.findViewById(R.id.Rlayout_mine_favorite);
    }

    /**
     * 设置点击事件
     * 苑雪元
     * 2016/07/21
     */
    private void setOnClick(){
        this.shopping.setOnClickListener(this);
        this.order.setOnClickListener(this);
        this.favorite.setOnClickListener(this);
        this.myInfoEdit.setOnClickListener(this);
        this.myhead.setOnClickListener(this);
    }

    /**
     * 初始化设置值
     * 苑雪元
     * 2016/07/21
     */
    private void initValues(){
        this.myName.setText(S_name);
    }

    /**
     * 跳转页面
     * 苑雪元
     * 2016/07/21
     * @param c 要跳转的页面
     */
    private void intent(Class c){
        Intent i = new Intent();
        i.setClass(this.getActivity(), c);
        startActivity(i);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Rlayout_mine_favorite:
                intent(MyFavoriteActivity.class);
                break;
            case R.id.Rlayout_mine_order:
                intent(MyOrderActivity.class);
                break;
            case R.id.Rlayout_mine_shopping:
                intent(MyShoppingActivity.class);
                break;
            case R.id.Iamge_mine_head:
                break;
            case R.id.mine_edit:
                intent(MyInfoActivity.class);
                break;
        }

    }
}

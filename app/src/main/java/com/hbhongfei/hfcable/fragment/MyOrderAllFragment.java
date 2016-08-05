package com.hbhongfei.hfcable.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyOrder_all_Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全部订单的页面
 */
public class MyOrderAllFragment extends Fragment {
    private ListView ListView_myOrderAll;
    private List<Map<String,String>> list;
    private Map<String,String> map;

    public MyOrderAllFragment() {
        // Required empty public constructor
    }

    public static MyOrderAllFragment newInstance(String param1, String param2) {
        MyOrderAllFragment fragment = new MyOrderAllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_order_all, container, false);
        initView(v);
        getValues();
        MyOrder_all_Adapter adapter = new MyOrder_all_Adapter(getActivity(),R.layout.item_my_order,list);
        ListView_myOrderAll.setAdapter(adapter);
        return v;
    }

    /**
     * 初始化界面
     * @param v
     */
    private void initView(View v) {
        ListView_myOrderAll = (ListView) v.findViewById(R.id.ListView_myOrderAll);
    }

    /**
     * 获取数据
     */
    private void getValues(){
        //暂时模拟
        list = new ArrayList<>();
        for (int i =0;i<10;i++){
            map = new HashMap<>();
            map.put("name","电缆"+i);
            map.put("type","种类"+i);
            map.put("introduce","很好的电缆很好的电缆很好的电缆很好的电缆很好的电缆很好的电缆很好的电缆很好的电缆很好的电缆");
            map.put("money","14.00");
            list.add(map);
        }
    }


}

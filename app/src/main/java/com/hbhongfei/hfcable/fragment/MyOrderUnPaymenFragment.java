package com.hbhongfei.hfcable.fragment;

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
 * 没有付款的订单
 */
public class MyOrderUnPaymenFragment extends Fragment {

    private ListView ListView_myOrderUnPayment;
    private List<Map<String,String>> list;
    private Map<String,String> map;

    public MyOrderUnPaymenFragment() {
    }

    public static MyOrderUnPaymenFragment newInstance(String param1, String param2) {
        MyOrderUnPaymenFragment fragment = new MyOrderUnPaymenFragment();
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
        View v = inflater.inflate(R.layout.fragment_my_order_un_paymen, container, false);
        initView(v);
        getValues();
        MyOrder_all_Adapter adapter = new MyOrder_all_Adapter(getActivity(),R.layout.item_my_order,list);
        ListView_myOrderUnPayment.setAdapter(adapter);
        return v;
    }
    /**
     * 初始化界面
     * @param v
     */
    private void initView(View v) {
        ListView_myOrderUnPayment = (ListView) v.findViewById(R.id.ListView_myOrderUnPayment);
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
            map.put("introduce","很好的电缆");
            map.put("money","14.00");
            list.add(map);
        }
    }

}

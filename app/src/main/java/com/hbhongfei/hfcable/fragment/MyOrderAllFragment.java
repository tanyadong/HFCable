package com.hbhongfei.hfcable.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.ConnectionOrder;
import com.hbhongfei.hfcable.util.LoginConnection;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * 全部订单的页面
 */
public class MyOrderAllFragment extends Fragment {
    private static final String USER = LoginConnection.USER;
    private ListView ListView_myOrderAll;
    private List<Map<String,String>> list;
    private Map<String,String> map;
    private String S_phoneNumber;
    private int pageNo=1;
    private int countPage;
    ConnectionOrder connectionOrder=null;
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
        SharedPreferences spf = this.getActivity().getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber", null);
        try {
            connectionOrder = new ConnectionOrder(getActivity().getApplicationContext(),ListView_myOrderAll );
            connectionOrder.connInterByUserId(S_phoneNumber,pageNo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}

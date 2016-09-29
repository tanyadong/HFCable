package com.hbhongfei.hfcable.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.ConnectionOrder;
import com.hbhongfei.hfcable.util.LoginConnection;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * 没有付款的订单
 */
public class MyOrderUnDeliveryFragment extends BaseFragment {
    private static final String USER = LoginConnection.USER;
    private ListView ListView_myOrderUnPayment;
    private List<Map<String,String>> list;
    private Map<String,String> map;
    private String S_phoneNumber;
    private int pageNo=1;
    private int countPage;
    private TextView tview_empty,tview_empty_to;
    private ImageView image_empty;
    LinearLayout view;
    ConnectionOrder connectionOrder=null;
    private boolean isVisable = false;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    public MyOrderUnDeliveryFragment() {
    }

    public static MyOrderUnDeliveryFragment newInstance(String param1, String param2) {
        MyOrderUnDeliveryFragment fragment = new MyOrderUnDeliveryFragment();
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
        initvalue();
        isPrepared = true;
//        getValues();
        lazyLoad();
        return v;
    }


    /**
     * 初始化界面
     * @param v
     */
    private void initView(View v) {
        ListView_myOrderUnPayment = (ListView) v.findViewById(R.id.ListView_myOrderUnPayment);
        view= (LinearLayout) v.findViewById(R.id.layout_unpayorder_empty);
        tview_empty= (TextView) v.findViewById(R.id.tview_empty);
        tview_empty_to= (TextView) v.findViewById(R.id.tview_empty_to);
        image_empty= (ImageView) v.findViewById(R.id.image_empty);
    }
    public void initvalue(){
        image_empty.setImageResource(R.mipmap.order_empty);
        tview_empty.setText("您还没有相关订单");
    }
    /**
     * 获取数据
     */
    private void getValues() {
        SharedPreferences spf = this.getActivity().getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber", null);
        try {
            connectionOrder = new ConnectionOrder(getActivity().getApplicationContext(), ListView_myOrderUnPayment,view);
            connectionOrder.connInterUnDelivery(pageNo,S_phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getValues();
        mHasLoadedOnce=true;
    }
}

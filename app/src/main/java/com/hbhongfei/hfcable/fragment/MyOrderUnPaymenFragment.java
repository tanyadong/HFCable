package com.hbhongfei.hfcable.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MyOrder_all_Adapter;
import com.hbhongfei.hfcable.util.ConnectionOrder;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NetUtils;

import org.json.JSONException;

import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * 没有付款的订单
 */
public class MyOrderUnPaymenFragment extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private static final String USER = LoginConnection.USER;
    private ListView ListView_myOrderUnPayment;
    private BGARefreshLayout mRefreshLayout;
    private String S_phoneNumber;
    private int pageNo=1;
    private Dialog dialog;
    ConnectionOrder connectionOrder=null;
    MyOrder_all_Adapter myOrderAllAdapter=null;
    private LinearLayout noInternet;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    public boolean isResult;//是否从订单详情返回
    private boolean isViewCreated;
    public MyOrderUnPaymenFragment() {
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
        initRefreshLayout();

        isResult=false;
        isViewCreated=true;
        dialog=new Dialog(getActivity());
        myOrderAllAdapter=new MyOrder_all_Adapter(this.getActivity(),isResult);

        connectionOrder = new ConnectionOrder(MyOrderUnPaymenFragment.this.getActivity(),MyOrderUnPaymenFragment.this.getContext(), ListView_myOrderUnPayment,noInternet,dialog);
        return v;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&isViewCreated){
            getValues();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getUserVisibleHint()){
            getValues();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(myOrderAllAdapter.isResult){
            getValues();
            isResult=false;
        }
    }

    /**
     * 初始化界面
     * @param v
     */
    private void initView(View v) {
        mRefreshLayout= (BGARefreshLayout) v.findViewById(R.id.my_order_unpay_freshlayout);
        ListView_myOrderUnPayment = (ListView) v.findViewById(R.id.ListView_myOrderUnPayment);
        noInternet= (LinearLayout) v.findViewById(R.id.no_internet_my_order_un_paymen);
    }
    private void initRefreshLayout() {
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder stickinessRefreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置正在加载更多时的文本
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
    }
    /**
     * 获取数据
     */
    private void getValues() {
        SharedPreferences spf = this.getActivity().getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber", null);
        new MyAsyncTack().execute();
    }
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        if(NetUtils.isConnected(getActivity())){
            pageNo=1;
            new MyAsyncTack().execute();
            mRefreshLayout.endRefreshing();
        }else {
            Toast.makeText(getActivity(),"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(NetUtils.isConnected(getActivity())){
            if(pageNo<connectionOrder.getTotalPage()){
                pageNo++;
                new MyAsyncTack().execute();
                mRefreshLayout.endLoadingMore();
                return true;
            }else {
                mRefreshLayout.endLoadingMore();
                return false;
            }
        }else {
            Toast.makeText(getActivity(),"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            return  false;
        }
    }


    class MyAsyncTack extends AsyncTask<Map<String,String>,Void,Void> {
        @Override
        protected Void doInBackground(Map<String, String>... params) {
            try {
                connectionOrder.connInterUnPay(pageNo,S_phoneNumber,1,0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            dialog.showDialog("正在加载中");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRefreshLayout.endLoadingMore();
            dialog.cancle();
            super.onPostExecute(aVoid);
        }
    }

}

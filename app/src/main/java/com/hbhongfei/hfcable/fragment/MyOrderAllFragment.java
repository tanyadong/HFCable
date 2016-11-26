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
 * 全部订单的页面
 */
public class MyOrderAllFragment  extends Fragment implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private static final String USER = LoginConnection.USER;
   private ListView ListView_myOrderAll;
    private BGARefreshLayout mRefreshLayout;

    private String S_phoneNumber;
    private int pageNo=1;
    ConnectionOrder connectionOrder=null;
    MyOrder_all_Adapter myOrderAllAdapter=null;
    private LinearLayout noInternet;
    private Dialog dialog;
    public boolean isResult;//是否从订单详情返回
    private boolean isViewCreated; //判断view是否加载
    public MyOrderAllFragment() {
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
        isViewCreated=true;
        initRefreshLayout();
        isResult=false;
        dialog=new Dialog(getActivity());
        myOrderAllAdapter=new MyOrder_all_Adapter(this.getActivity(),isResult);
        connectionOrder = new ConnectionOrder(MyOrderAllFragment.this.getActivity(),MyOrderAllFragment.this.getContext(),ListView_myOrderAll,noInternet,dialog);
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
        mRefreshLayout= (BGARefreshLayout) v.findViewById(R.id.my_all_order_freshlayout);
        ListView_myOrderAll = (ListView) v.findViewById(R.id.ListView_myOrderAll);
        noInternet= (LinearLayout) v.findViewById(R.id.no_internet_my_order_all);
    }

    private void initRefreshLayout() {
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder stickinessRefreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置正在加载更多时的文本
        stickinessRefreshViewHolder.setLoadingMoreText("正在加载中");
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
    }
    /**
     * 获取数据
     */
    private void getValues(){
        SharedPreferences spf = this.getActivity().getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber", null);
        pageNo=1;
        new MyAsyncTack().execute();
    }
    //下拉刷新
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
    //上拉加载
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (NetUtils.isConnected(getActivity())){
            int count=connectionOrder.getTotalPage();
            if(pageNo<count){
                pageNo++;
                ListView_myOrderAll.setVisibility(View.VISIBLE);
                new MyAsyncTack().execute();
                mRefreshLayout.endLoadingMore();
                return true;
            }else {
                mRefreshLayout.endLoadingMore();
                return false;
            }
        }else {
            Toast.makeText(getActivity(),"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    class MyAsyncTack extends AsyncTask<Map<String,String>,Void,Void> {
        @Override
        protected Void doInBackground(Map<String, String>... params) {
            try {
                connectionOrder.connInterByUserId(S_phoneNumber,pageNo);
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

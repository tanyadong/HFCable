package com.hbhongfei.hfcable.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.ConnectionOrder;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.NetUtils;

import org.json.JSONException;

import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

import static android.app.Activity.RESULT_CANCELED;

/**
 * 全部订单的页面
 */
public class MyOrderAllFragment extends BaseFragment  implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private static final String USER = LoginConnection.USER;
   private ListView ListView_myOrderAll;
    private BGARefreshLayout mRefreshLayout;

    private String S_phoneNumber;
    private int pageNo=1;
    ConnectionOrder connectionOrder=null;
    private LinearLayout noInternet;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private Dialog dialog;
    public boolean isResult;//是否从订单详情返回

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
        initRefreshLayout();

//        isPrepared = true;
//        lazyLoad();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        isPrepared = true;
        lazyLoad();
//        isResult=false;


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== Activity.RESULT_OK){
            if(resultCode==RESULT_CANCELED){
                Toast.makeText(getActivity(),"eee",Toast.LENGTH_SHORT).show();
                getValues();
            }
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
        connectionOrder = new ConnectionOrder(MyOrderAllFragment.this.getActivity(),MyOrderAllFragment.this.getContext(),ListView_myOrderAll,noInternet);
        pageNo=1;
        new MyAsyncTack().execute();

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getValues();
        Toast.makeText(getActivity(),"aaaa",Toast.LENGTH_SHORT).show();
        mHasLoadedOnce=true;
    }
    //下拉刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        if(NetUtils.isConnected(getActivity())){
            pageNo=1;
            new MyAsyncTack().execute();
//            ListView_myOrderAll.setVisibility(View.VISIBLE);

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
                Toast.makeText(getActivity(),"已经是全部数据",Toast.LENGTH_SHORT).show();
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
            dialog=new Dialog(getActivity());
            dialog.showDialog("正在加载中");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.cancle();
            mRefreshLayout.endLoadingMore();
            super.onPostExecute(aVoid);
        }
    }


}

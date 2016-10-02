package com.hbhongfei.hfcable.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.ConnectionOrder;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.LoginConnection;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * 全部订单的页面
 */
public class MyOrderAllFragment extends BaseFragment  implements BGARefreshLayout.BGARefreshLayoutDelegate{
    private static final String USER = LoginConnection.USER;
   private ListView ListView_myOrderAll;
    private List<Map<String,String>> list;
    private Map<String,String> map;
    private String S_phoneNumber;
    private int pageNo=1;
    private int countPage;
    ConnectionOrder connectionOrder=null;
    private LinearLayout noInternet;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private Dialog dialog;
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
        ListView_myOrderAll = (ListView) v.findViewById(R.id.ListView_myOrderAll);
        noInternet= (LinearLayout) v.findViewById(R.id.no_internet_my_order_all);
    }
    /**
     * 获取数据
     */
    private void getValues(){
        SharedPreferences spf = this.getActivity().getSharedPreferences(USER, Context.MODE_PRIVATE);
        S_phoneNumber = spf.getString("phoneNumber", null);
            connectionOrder = new ConnectionOrder(MyOrderAllFragment.this.getActivity(),MyOrderAllFragment.this.getContext(),ListView_myOrderAll,noInternet);
            dialog=new Dialog(getActivity());
            dialog.showDialog("正在加载中");
            new MyAsyncTack().execute();
//            connectionOrder.connInterByUserId(S_phoneNumber,pageNo);
            dialog.cancle();
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        getValues();
        mHasLoadedOnce=true;
    }
    //下拉刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {

    }
    //上拉加载
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
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
            super.onPostExecute(aVoid);
        }
    }


}

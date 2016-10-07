package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.MarketChartActivity;
import com.hbhongfei.hfcable.activity.SplashActivity;
import com.hbhongfei.hfcable.adapter.MyExpandableListViewAdapter;
import com.hbhongfei.hfcable.pojo.MarketInfo;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.NetUtils;
import com.squareup.okhttp.OkHttpClient;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,IErrorOnclick  {
    private ExpandableListView expandableListView;
    private LinearLayout ll_market_head;
    private ArrayList<String> group_list;
    private ArrayList<MarketInfo> child_list;
    private ArrayList<List<MarketInfo>> item_list=new ArrayList<>();;;
    private View view;
    private Dialog dialog;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    //下拉和分页框架
    private static final String TAG = IndexFragment.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private MyExpandableListViewAdapter myExpandableListViewAdapter=null;
    int index=1;
    private LinearLayout noInternet;

    public MarketFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_market, container, false);
        initView(view);
        initRefreshLayout();
        isPrepared = true;
        //懒加载
        lazyLoad();
        return view;
    }

    @Override
    public void onResume() {
        Toast.makeText(MarketFragment.this.getActivity(),SplashActivity.ID+"",Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    Handler mHandler = new Handler(){
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    myExpandableListViewAdapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout)view.findViewById(R.id.rl_expandable_refreshview_market_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder stickinessRefreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置正在加载更多时的文本
        stickinessRefreshViewHolder.setLoadingMoreText("正在加载中");
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
    }


    /**
     * 动态加载数据
     */
    private void setValues(final ArrayList<List<MarketInfo>> item_list1) {
        myExpandableListViewAdapter = new MyExpandableListViewAdapter(getActivity(), group_list, item_list1, expandableListView);
        expandableListView.setAdapter(myExpandableListViewAdapter);
        //为ExpandableListView的子列表单击事件设置监听器
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), MarketChartActivity.class);
                intent.putExtra("marketInfo",item_list1.get(groupPosition).get(childPosition));
                startActivity(intent);
                return true;
            }
        });
        for (int i = 0; i < myExpandableListViewAdapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }

    private void initView(View view) {
//        SplashActivity.ID=3;
        group_list = new ArrayList<>();
        dialog = new Dialog(getActivity());
        ll_market_head= (LinearLayout) view.findViewById(R.id.market_title_llayout);
        expandableListView = (ExpandableListView) view.findViewById(R.id.market_expendlist);
        noInternet = (LinearLayout) view.findViewById(R.id.no_internet_market);
    }

    /**
     * 访问网络
     */
    private  String netWork(final int index) {
        final String url = "http://material.cableabc.com/matermarket/spotshow_00" + index + ".html";
        com.squareup.okhttp.Request request = new com.squareup.okhttp.Request.Builder().url(url).build();

        OkHttpClient client = new OkHttpClient();
        com.squareup.okhttp.Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response!=null) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    //下拉刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        dialog.showDialog("正在加载中");
        if (NetUtils.isConnected(getActivity())) {
            for (int i = 1; i <= 4; i++) {
                new MarketTask().execute(i);
            }
            mRefreshLayout.endRefreshing();
        }else{
            dialog.cancle();
            Toast.makeText(getActivity(),"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }
        dialog.cancle();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    class MarketTask extends AsyncTask<Integer, Void, ArrayList<List<MarketInfo>>> {
        @Override
        protected ArrayList<List<MarketInfo>> doInBackground(Integer... params) {

                index = params[0];
                String data = netWork(index);
                return parse(data, index);
        }
        @Override
        protected void onPreExecute() {
            //请求之前
        }

        /**
         * 请求之后的操作
         * @param data
         */
        @Override
        protected void onPostExecute(final ArrayList<List<MarketInfo>> data) {
            if(data.size()==4){
                dialog.cancle();
                ll_market_head.setVisibility(View.VISIBLE);
                setValues(data);
            }
        }
    }
    /**
     * 初始化数据
     */
    public void initValues() throws IOException {
        //父列表数据
        group_list.add("铜");
        group_list.add("铝");
        group_list.add("橡胶");
        group_list.add("塑料");
        dialog.showDialog("正在加载中");
        if(NetUtils.isConnected(getActivity())){
            for (int i=1;i<=4;i++){
                new MarketTask().execute(i);
            }
        }else{
            dialog.cancle();
            Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络哦","点击设置",MarketFragment.this);
        }

    }

    /**
     * 解析html
     *
     * @param html
     */
    public ArrayList<List<MarketInfo>> parse(String html, final int index) {
        Document doc = Jsoup.parse(html);
        //Elements
        Element table = doc.getElementsByTag("table").first();
        Elements lists = table.getElementsByTag("tr");
        child_list = new ArrayList<>();
        for (int j = 1; j < lists.size(); j++) {
            Element item = lists.get(j);
            Elements els = item.getElementsByTag("td");
            MarketInfo marketInfo = new MarketInfo();
            //遍历所有的列
            marketInfo.setArea(els.get(0).text());
            marketInfo.setProductName(els.get(1).text());
            marketInfo.setAveragePrice(els.get(2).text());
            marketInfo.setFallOrise(els.get(3).text());
            marketInfo.setMinPrice(els.get(4).text());
            marketInfo.setMaxPrice(els.get(5).text());
            marketInfo.setFactoryArea(els.get(6).text());
            marketInfo.setData(els.get(7).text());
            String url = els.get(8).getElementsByTag("a").attr("href");
            marketInfo.setTrend("http://material.cableabc.com" + url);
            child_list.add(marketInfo);
        }
        //父列表添加子列表
        item_list.add(child_list);
        return item_list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //防止handler引起的内存泄露
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        try {
            initValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mHasLoadedOnce=true;
    }

    @Override
    public void errorClick() {
        NetUtils.openSetting(MarketFragment.this.getActivity());
    }
}

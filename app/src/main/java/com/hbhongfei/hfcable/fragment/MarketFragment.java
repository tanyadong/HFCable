package com.hbhongfei.hfcable.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.MarketRecyclerAdapter;
import com.hbhongfei.hfcable.pojo.MarketInfo;
import com.hbhongfei.hfcable.util.CaheInterceptor;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.NetUtils;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,IErrorOnclick, RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    private RecyclerView marketRecyclerView;
    private ArrayList<MarketInfo> child_list;
    private View view;
    private Dialog dialog;
    private Request request;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    //下拉和分页框架
    private BGARefreshLayout mRefreshLayout;
    private MarketRecyclerAdapter marketRecyclerAdapter = null;
    int index=1;
    private LinearLayout noInternet;
    private RapidFloatingActionHelper rfabHelper;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton rfaButton;
    private OkHttpClient mOkHttpClient;
    private int position = 1;
    public MarketFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_market, container, false);
        initView(view);
        initFBA(view);
        initRefreshLayout();
        initOkHttpClient(); //初始化okhttp请求
        isPrepared = true;
        //懒加载
        lazyLoad();
        return view;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    dialog.cancle();
                    Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络哦","点击设置",MarketFragment.this);
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
    private void setValues(final ArrayList<MarketInfo> item_list1) {
        marketRecyclerAdapter = new MarketRecyclerAdapter(getActivity(), item_list1);
        //RecyclerView子项的点击事件
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setSmoothScrollbarEnabled(true);
        manager.setAutoMeasureEnabled(true);
        marketRecyclerView.setLayoutManager(manager);
        marketRecyclerView.setAdapter(marketRecyclerAdapter);
        marketRecyclerView.setHasFixedSize(true);
        marketRecyclerView.setNestedScrollingEnabled(false);
    }

    private void initView(View view) {
        dialog = new Dialog(getActivity());
        marketRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_market);
        noInternet = (LinearLayout) view.findViewById(R.id.no_internet_market);
    }

    private void initOkHttpClient() {
        File sdcache=getActivity().getExternalCacheDir();
        if (sdcache == null){
            return;
        }
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new CaheInterceptor(getActivity()))
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();

    }
    private void initFBA(View view) {
        rfaLayout = (RapidFloatingActionLayout) view.findViewById(R.id.rfab_group_sample_rl);
        rfaButton = (RapidFloatingActionButton) view.findViewById(R.id.rfab_group_sample_rfab);
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getResources().getString(R.string.market_copper))
                .setResId(R.mipmap.copper)
                .setIconNormalColor(0xffFFA500)
                .setIconPressedColor(0xffFFFFE0)
                .setLabelColor(0xffFFA500)
                .setWrapper(0)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getResources().getString(R.string.market_aluminum))
                .setResId(R.mipmap.aluminum)
                .setIconNormalColor(0xffF5DEB3)
                .setIconPressedColor(0xffC0C0dd)
                .setLabelColor(0xffF5DEB3)
                .setWrapper(1)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getResources().getString(R.string.market_rubber))
                .setResId(R.mipmap.rubber)
                .setIconNormalColor(0xff056f00)
                .setIconPressedColor(0xff0d5302)
                .setLabelColor(0xff056f00)
                .setWrapper(2)
        );
        items.add(new RFACLabelItem<Integer>()
                .setLabel(getResources().getString(R.string.market_plastic))
                .setResId(R.mipmap.plastic)
                .setIconNormalColor(0xff00BFFF)
                .setIconPressedColor(0xff00BFF0)
                .setLabelColor(0xff00BFFF)
                .setWrapper(3)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(ABTextUtil.dip2px(getContext(), 5))
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(ABTextUtil.dip2px(getContext(), 5))
        ;
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();
    }
    private  String netWork(final int index) {
        final String url = "http://material.cableabc.com/matermarket/spotshow_00" + index + ".html";
        CacheControl.Builder builder = new CacheControl.Builder();
        request = new Request.Builder()
                .cacheControl(builder.build())
                .url(url)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    return response.body().string();
                }else {
                    if(response.cacheResponse()!=null){
                        return response.body().string();
                    }else {
                        mHandler.sendEmptyMessage(2);
                    }
                }

        }catch (final Exception e){
            e.printStackTrace();
        }
        return null;
    }
    //下拉刷新
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        dialog.showDialog("正在加载中");
        if (NetUtils.isConnected(getActivity())) {
            new MarketTask().execute(position);
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
    /**
     * @author 谭亚东
     * @Description: 展开的文字点击事件
     * @date 2017/1/7  17:14
     */
    @Override
    public void onRFACItemLabelClick(int i, RFACLabelItem rfacLabelItem) {
        rfabHelper.toggleContent();
        position = i+1;
        new MarketTask().execute(i+1);
    }
    /**
     * @author 谭亚东
     * @Description: 图片的点击事件
     * @date 2017/1/7  17:14
     */
    @Override
    public void onRFACItemIconClick(int i, RFACLabelItem rfacLabelItem) {
        position = i+1;
        new MarketTask().execute(i+1);
        rfabHelper.toggleContent();
    }


    class MarketTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
                index = params[0];
                String data = netWork(index);
                return data;
        }
        @Override
        protected void onPreExecute() {
            //请求之前
        }
        /**
         * 请求之后的操作
         * @param html
         */
        @Override
        protected void onPostExecute(final String html) {
            if(html == null){
                return;
            }
            parseHtml(html);
            dialog.cancle();
            setValues(child_list);
        }
    }
    /**
     * 初始化数据
     */
    public void initValues() throws IOException {
        dialog.showDialog("正在加载中");
        if (NetUtils.isConnected(getActivity())){
            new MarketTask().execute(1);
        }else {
            dialog.cancle();
            Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络哦","点击设置",MarketFragment.this);
        }
            
    }

    /**
     * 解析html
     * @param html
     */
    public ArrayList<MarketInfo> parseHtml(final String html) {
        Document doc = Jsoup.parse(html);
        //Elements
        Element table = doc.getElementsByTag("table").first();
        Elements lists = table.getElementsByTag("tr");
        child_list = new ArrayList<>();
        int market_count=lists.size();
        for (int j = 1; j < market_count; j++) {
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
        return child_list;
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
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        position = 1;
    }
}

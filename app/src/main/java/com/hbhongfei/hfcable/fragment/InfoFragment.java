package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.InfoDetailActivity;
import com.hbhongfei.hfcable.adapter.DataAdapter;
import com.hbhongfei.hfcable.util.CaheInterceptor;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.Information;
import com.hbhongfei.hfcable.util.NetUtils;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.OkHttpClient;

import static android.widget.Toast.makeText;

public class InfoFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,IErrorOnclick  {
    //下拉和分页框架
    private BGARefreshLayout mRefreshLayout;
    private View view;
    private ListView info_listView;
    private DataAdapter mAdapter = null;
    ArrayList<Information> info_list = null;
    String total = null;
    private int index = 0;
    private int count;
    private LinearLayout noInternet;
    public Handler mHandler;
    Information information=null;
    private int firstIndex = 0;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private Dialog dialog;
    private OkHttpClient mOkHttpClient;
    public InfoFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_info, container, false);
        mAdapter = new DataAdapter(getActivity());
        //声明一个队列
        setHasOptionsMenu(true);
        initRefreshLayout();
        // 初始化handler
        handlerMessage();
        initView(view);
        initOkHttpClient();

        isPrepared = true;
        lazyLoad();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
    /**
     * @author  tanyadong
     * @Title: handlerMessage
     * @Description: 创建handler
     * @date 2016-10-26 上午11:14:33
     */
    public void handlerMessage() {
        // 得到当前线程的Looper实例，由于当前线程是UI线程也可以通过Looper.getMainLooper()得到
        Looper looper = Looper.myLooper();
        // 此处甚至可以不需要设置Looper，因为 Handler默认就使用当前线程的Looper
        mHandler = new MessageHandler(looper);
    }
    //设置懒加载
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        dialog=new Dialog(getActivity());
        index=0;
        dialog.showDialog("正在加载中...");
        if(NetUtils.isConnected(getActivity())){
            new MyAsyncTack().execute();
        }else {
            dialog.cancle();
            Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络哦","点击设置",InfoFragment.this);
        }
        mHasLoadedOnce=true;
    }
    /**
     * @author tanyadong
     * @Description: handler,处理返回结果
     * @date 2016-10-26 上午11:15:53
     */
    class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            int iswitch = msg.arg1;
            switch (iswitch) {
                case 1:
                    //告诉适配器，数据变化了，从新加载listview
                    mAdapter.notifyDataSetChanged();
                    //设置加载中为false
                    info_listView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    dialog.cancle();
                    Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络哦","点击设置",InfoFragment.this);
                default:
                    break;
            }
        }
    }

    /**
     * 初始化组件
     */
    private  void  initView(View view){
        info_listView = (ListView) view.findViewById(R.id.fragment_info_listView);
        noInternet = (LinearLayout) view.findViewById(R.id.no_internet_info);
    }
    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout)view.findViewById(R.id.rl_listview_refresh);
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder stickinessRefreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        // 设置正在加载更多时的文本
        stickinessRefreshViewHolder.setLoadingMoreText("正在加载中");
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);
    }
    /**
     * 初始化据
     * @param
     */
    /*private  void setValues(final ArrayList<Information> list){
        mAdapter.updateItems(list);
        //添加头和尾
        info_listView.setAdapter(mAdapter);
        info_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                intent.putExtra("data", list.get(position));
                getActivity().startActivity(intent);
            }
        });
    }*/

    /**
     * 加载数据
     */
    private ArrayList<Information>  loadData( final int index)  {
        final String url="http://news.cableabc.com/gc_"+index+".html";
        String info_html= netWork(url);
        if (info_html != null) {
            return parse(info_html);
        }
        return null;
    }

    /**
     * 解析html
     * @param
     */
    protected ArrayList<Information>  parse(String html) {
        info_list=new ArrayList<Information>();
        //初始化okhttp
        Document doc = Jsoup.parse(html);
        Elements a = doc.getElementById("main_cont_ContentPlaceHolder1_pager").getElementsByTag("a");
        final String s_url=a.last().attr("href");
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                total=s_url.substring(s_url.indexOf("_")+1,s_url.lastIndexOf("."));
                count=Integer.parseInt(total);
            }
        });
        //Elements
        Elements topnews = doc.getElementsByClass("list31_newlist1");
         for (Element link : topnews) {
             information = new Information();
             information.setTitle(link.getElementsByClass("list31_title1").text());
             information.setBrief(link.getElementsByClass("list31_text1").text());
             information.setImgUrl(link.getElementsByTag("img").attr("src"));
             information.setContentUrl(link.getElementsByClass("Pic").attr("href"));
             final String data_html = loadContentData(information.getContentUrl());
             final Information info = parseContent(data_html,information);
             getActivity().runOnUiThread(new Runnable() {
                 @Override
                 public void run() {
                     dialog.cancle();
                     if (info == null){
                         return;
                     }
                     mAdapter.addItem(info);
                     if(firstIndex==0){//只设置一次adapter
                         info_listView.setAdapter(mAdapter);
                         firstIndex++;
                     }
                     mRefreshLayout.endLoadingMore();
                     mRefreshLayout.endRefreshing();
                 }
             });

         }
        return info_list;

    }

    public void initOkHttpClient() {
            File sdcache=getActivity().getExternalCacheDir();
            int cacheSize = 10 * 1024 * 1024;
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .addInterceptor(new CaheInterceptor(getActivity()))
                    .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
            mOkHttpClient = builder.build();
    }
    public  String netWork(String url) {
        CacheControl.Builder builder = new CacheControl.Builder();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .cacheControl(builder.build())
                .url(url)
                .build();
        try {
            final okhttp3.Response response = mOkHttpClient.newCall(request).execute();;
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
   /**
   * @author  谭亚东
   * @Title:
   * @Description: 获取资讯详情在主页显示时间
   * @date 2016/10/29 14:42
   */
    private String loadContentData(final String url){
        CacheControl.Builder builder = new CacheControl.Builder();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .cacheControl(builder.build())
                .url(url)
                .build();
        try {
            final okhttp3.Response response = mOkHttpClient.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }else {
                if(response.cacheResponse()!=null){
                    return response.body().string();
                }else {
                    Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "服务器出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {
                        }
                    });
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 解析html
     * @param html
     */
    protected Information parseContent(String html, final Information info) {
        if (html.isEmpty() || html.equals("") || html == null){
            return null;
        }
        Document doc = Jsoup.parse(html);
        //获取资讯时间
        Elements time = doc.getElementsByClass("contentspage");
        final String s_time=time.get(0).getElementsByTag("span").first().text();
        final String s_source=time.get(0).getElementsByTag("span").get(1).text();
        //Elements
        //获取资讯内容
        Element id = doc.getElementById("main_ContentPlaceHolder1_pnlContent");
        Elements contents=id.getElementsByTag("p");
        String content=new String();
        for (int i=2;i<contents.size();i++){
            content+="\u3000\u3000"+contents.get(i).text();
            content+="\n";
        }
        info.setTime(s_time+s_source);
        info.setDetailContent(content);
        return info;
    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        index=0;
        if(NetUtils.isConnected(getActivity())){
            new MyAsyncTack().execute();
        }else{
            makeText(getActivity(),"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(index<count) {
            index++;
            if(NetUtils.isConnected(getActivity())){
                // 如果网络可用，则加载网络数据
                new MyAsyncTack().execute();
                return true;
            }else{
                makeText(getActivity(),"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
                mRefreshLayout.endLoadingMore();
                return false;
            }

        }else{
            mRefreshLayout.endLoadingMore();
            return false;
        }
    }

    @Override
    public void errorClick() {
        NetUtils.openSetting(InfoFragment.this.getActivity());
    }

    /**
     * 异步执行加载
     */
    class MyAsyncTack extends AsyncTask<Void,Void,ArrayList<Information>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected ArrayList<Information>  doInBackground(Void... params) {
            return loadData(index);
        }

        @Override
        protected void   onPostExecute(ArrayList<Information> list ) {
            dialog.cancle();
            super.onPostExecute(list);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //防止handler引起的内存泄露
        mHandler.removeCallbacksAndMessages(null);
    }
}
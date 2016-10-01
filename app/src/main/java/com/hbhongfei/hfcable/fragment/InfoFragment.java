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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.InfoDetailActivity;
import com.hbhongfei.hfcable.activity.SplashActivity;
import com.hbhongfei.hfcable.adapter.DataAdapter;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.Information;
import com.hbhongfei.hfcable.util.IsNetworkAvailable;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.NetUtils;

import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */

public class InfoFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate,IErrorOnclick  {
    //下拉和分页框架
    private static final String TAG = IndexFragment.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private View view;
    private ListView info_listView;
    private DataAdapter mAdapter = null;
    List<Information> info_list = new ArrayList<Information>();
    boolean isFirst = true;
    Button reload = null;
    LinearLayout loadLayout = null;
    TextView loading = null;
    String total = null;
    private int index = 0;
    private int count=1;
    private LinearLayout noInternet;


    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private Dialog dialog;
    public InfoFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_info, container, false);
        //声明一个队列
        setHasOptionsMenu(true);
        initRefreshLayout();

        initView(view);
        isPrepared = true;
        lazyLoad();

        return view;
    }


    //设置懒加载
    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        dialog=new Dialog(getActivity());
        mAdapter = new DataAdapter(getActivity(), info_list);
        info_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                intent.putExtra("data", info_list.get(position));
                getActivity().startActivity(intent);
            }
        });
        //添加头和尾
        info_listView.setAdapter(mAdapter);
        dialog.showDialog("正在加载中");
        loadData(index);
        mHasLoadedOnce=true;
    }
    Handler mHandler = new Handler(){
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    count=msg.arg1;
                    break;
                case 1:
                    //告诉适配器，数据变化了，从新加载listview
                    mAdapter.notifyDataSetChanged();
                    //设置加载中为false
                    info_listView.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    reload.setVisibility(View.VISIBLE);
                    if(isAdded()){
                        loading.setText(getString(R.string.tip_text_data_fail));
                    }
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 初始化组件
     */
    private  void  initView(View view){
        SplashActivity.ID=2;
        info_listView = (ListView) view.findViewById(R.id.fragment_info_listView);
        loadLayout= (LinearLayout) view.findViewById(R.id.fragment_load_layout);
        loading = (TextView) view.findViewById(R.id.fragment_loading);
        reload = (Button)view.findViewById(R.id.fragment_reload);
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
     * 初始化数据
     */
    private  void setValues(final List<Information> list){
        mAdapter = new DataAdapter(getActivity(), list);
        //添加头和尾
        info_listView.setAdapter(mAdapter);
        info_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                intent.putExtra("data", list.get(position));
                getActivity().startActivity(intent);
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload.setVisibility(View.GONE);
                loadData(0);
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData( final int index){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://news.cableabc.com/gc_"+index+".html";
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            parse(s);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            dialog.cancle();
                            if (volleyError instanceof NoConnectionError){
                                Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络哦","点击设置",InfoFragment.this);
                            }else if(volleyError instanceof NetworkError||volleyError instanceof ServerError||volleyError instanceof TimeoutError){
                                Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "服务器出错啦", new IErrorOnclick() {
                                    @Override
                                    public void errorClick() {
                                    }
                                });
                            }else{
                                Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "出错啦", new IErrorOnclick() {
                                    @Override
                                    public void errorClick() {

                                    }
                                });
                            }
                        }
                    });
                MySingleton.getInstance(getActivity()).addToRequestQueue(request);
                }
        }).start();

    }

    /**
     * 解析html
     * @param
     */
    protected void parse(String html) {
        Document doc = Jsoup.parse(html);
        Elements a = doc.getElementById("main_cont_ContentPlaceHolder1_pager").getElementsByTag("a");
        String s_url=a.last().attr("href");
        total=s_url.substring(s_url.indexOf("_")+1,s_url.lastIndexOf("."));
        Message message=new Message();
        message.what=0;
        message.arg1=Integer.parseInt(total);
        mHandler.sendMessage(message);
        //Elements
        Elements topnews = doc.getElementsByClass("list31_newlist1");
         for (Element link : topnews) {
            Information information = new Information();
            information.setTitle(link.getElementsByClass("list31_title1").text());
            information.setBrief(link.getElementsByClass("list31_text1").text());
            information.setImgUrl(link.getElementsByTag("img").attr("src"));
            information.setContentUrl(link.getElementsByClass("Pic").attr("href"));
            loadContentData(information.getContentUrl(), information);
         }
    }
    /**
     * 解析资讯详情
     * @param url
     */
    private void loadContentData(String url, final Information information){
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                parseContent(s,information);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (volleyError instanceof NoConnectionError){
                    Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络哦","点击设置",InfoFragment.this);
                }else if(volleyError instanceof NetworkError||volleyError instanceof ServerError||volleyError instanceof TimeoutError){
                    Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "服务器出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {

                        }
                    });
                }else{
                    Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "出错啦", new IErrorOnclick() {
                        @Override
                        public void errorClick() {

                        }
                    });
                }
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }
    /**
     * 解析html
     * @param html
     */
    protected void parseContent(String html, final Information information) {
        Document doc = Jsoup.parse(html);
        //获取资讯时间
        Elements time = doc.getElementsByClass("contentspage");
        String s_time=time.get(0).getElementsByTag("span").first().text();
        String s_source=time.get(0).getElementsByTag("span").get(1).text();
        //Elements
        //获取资讯内容
        Element id = doc.getElementById("main_ContentPlaceHolder1_pnlContent");
        Elements contents=id.getElementsByTag("p");
        String content=new String();
        for (int i=2;i<contents.size();i++){
            content+="\u3000\u3000"+contents.get(i).text();
            content+="\n";
        }
        information.setTime(s_time+s_source);
        information.setDetailContent(content);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                dialog.cancle();
                info_list.add(information);
                mAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        index=0;
        if(IsNetworkAvailable.isNetworkAvailable(getActivity())){
            new MyAsyncTack().execute();
        }else{
            Toast.makeText(getActivity(),"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
            mRefreshLayout.endRefreshing();
        }
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(index<count) {
            index++;
            if(IsNetworkAvailable.isNetworkAvailable(getActivity())){
                // 如果网络可用，则加载网络数据
                new MyAsyncTack().execute();
            }else{
                Toast.makeText(getActivity(),"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
                mRefreshLayout.endLoadingMore();
                return false;
            }

        }else{
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    @Override
    public void errorClick() {
        NetUtils.openSetting(InfoFragment.this.getActivity());
    }

    /**
     * 异步执行加载
     */
    class MyAsyncTack extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            dialog.showDialog("正在加载中");
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {
            loadData(index);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.cancle();
            mRefreshLayout.endLoadingMore();
            mRefreshLayout.endRefreshing();
            super.onPostExecute(aVoid);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //防止handler引起的内存泄露
        mHandler.removeCallbacksAndMessages(null);
    }
}
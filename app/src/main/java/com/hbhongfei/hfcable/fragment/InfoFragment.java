package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.InfoDetailActivity;
import com.hbhongfei.hfcable.adapter.DataAdapter;
import com.hbhongfei.hfcable.util.Information;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment implements AbsListView.OnScrollListener {
    RequestQueue queue=null;
    private View view;
    private ListView info_listView;
    private DataAdapter mAdapter = null;
    List<Information> info_list = new ArrayList<Information>();
    List<Information> list ;
    boolean isFirst = true;
    Button reload = null;
    LinearLayout loadLayout = null;
    TextView loading = null;
    String total = null;
    //底部footer
    View footer;

    //是否加载数据中
    boolean isLoading = false;
    boolean isLastRow=false;
    //Toast显示状态
    boolean isToast = false;
    private int index = 0;
    private int count;
    private ArrayList<String> strings;
    public InfoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_info, container, false);
        //声明一个队列
        queue= Volley.newRequestQueue(getActivity());
        initView(view);
        //获取最大数量
        setValues();
        click();
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mHandler.sendEmptyMessage(0);
            if(isFirst){
                isFirst = false;
                loadData(index);
            }
        } else {

        }
    }
    Handler mHandler = new Handler(){
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    isToast = false;
                    break;
                case 1:
//                   info_list.clear();
                    //添加数据
                    info_list.addAll((List<Information>) msg.obj);
                    //告诉适配器，数据变化了，从新加载listview
                    mAdapter.notifyDataSetChanged();
//                    //移除底部footer
//                    info_listView.removeFooterView(footer);
                    //设置加载中为false
                    isLoading = false;
                    count=msg.arg1;
//                    while (index<count) {
//                        index++;
//                        return;
//                    }
                    loadLayout.setVisibility(View.GONE);
                    info_listView.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
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
        info_listView = (ListView) view.findViewById(R.id.fragment_info_listView);
        loadLayout= (LinearLayout) view.findViewById(R.id.fragment_load_layout);
        loading = (TextView) view.findViewById(R.id.fragment_loading);
        reload = (Button)view.findViewById(R.id.fragment_reload);
        footer = LayoutInflater.from(getActivity()).inflate(R.layout.footer, null);
    }

    /**
     * 初始化数据
     */
    private  void setValues(){
        mAdapter = new DataAdapter(getActivity(), info_list);
        //添加头和尾
        info_listView.setAdapter(mAdapter);
        info_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                intent.putExtra("data", info_list.get(position));
                getActivity().startActivity(intent);
            }

        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setText(getString(R.string.tip_text_data_loading));
                reload.setVisibility(View.GONE);
                loadData(index);
            }
        });
    }
/***
 * 点击事件
 */
    private void click(){
        info_listView.setOnScrollListener(this);
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
                            loading.setVisibility(View.GONE);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //请求失败
                            isFirst = true;
                        }
                    });
                    queue.add(request);
                }
        }).start();

    }

    /**
     * 解析html
     * @param
     */
    protected void parse(String html) {
        list=new ArrayList<>();
        Document doc = Jsoup.parse(html);
            Elements a = doc.getElementById("main_cont_ContentPlaceHolder1_pager").getElementsByTag("a");
            String s_url=a.last().attr("href");
            total=s_url.substring(s_url.indexOf("_")+1,s_url.lastIndexOf("."));
        //Elements
        Elements topnews = doc.getElementsByClass("list31_newlist1");
        int count = mAdapter.getCount();
        //如果所有的记录选项等于数据集的条数，则移除列表底部视图
        if(count == topnews.size()*Integer.parseInt(total)){
            info_listView.removeFooterView(footer);
            Toast.makeText(getActivity(), "已是全部数据!", Toast.LENGTH_LONG).show();
        }else {
//            for (int i = count; i < count + pageSize; i++) {
             for (Element link : topnews) {
                    Information information = new Information();
                    information.setTitle(link.getElementsByClass("list31_title1").text());
                    information.setBrief(link.getElementsByClass("list31_text1").text());
                    information.setImgUrl(link.getElementsByTag("img").attr("src"));
                    information.setContentUrl(link.getElementsByClass("Pic").attr("href"));
                    loadContentData(information.getContentUrl(), information);
                    list.add(information);
                }
                Message msg = new Message();
                msg.what = 1;
                msg.arg1=Integer.parseInt(total);
                msg.obj = list;
                mHandler.sendMessage(msg);
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
                System.out.println(volleyError);
            }
        });
        queue.add(request);
    }
    /**
     * 解析html
     * @param html
     */
    protected void parseContent(String html,Information information) {
//        list=new ArrayList<>();
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
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && index<count){
            //动态加载数据
            Toast.makeText(getActivity(),"aaaa"+count+"",Toast.LENGTH_SHORT).show();
            info_listView.addFooterView(footer);
            index++;
            loadData(index);

//            new LoadDataThread().start();
            mAdapter.notifyDataSetChanged();
            isLastRow=false;
        }

    }
    /**
     * 动态加载数据
     */
    private class LoadDataThread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(1000);
                loadData(index);

                return;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
//    /**
//     * 监听移动滚动条
//     */
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem,
//                         int visibleItemCount, int totalItemCount) {
//        // 如果正在加载，就return，防止加载多次
//        if(isLoading)
//            return;
//        // 得到listview中显示在界面底部的id
//        int lastItemid = info_listView.getLastVisiblePosition();
//        // 如果是listview中显示在界面底部的id=滚动条中Item总数，说明滑动到底部了，并且当前页<=总页数
//        if((lastItemid+1) == totalItemCount){
//            //设置正在加载中
//            isLoading = true;
//            if(totalItemCount > 0){
//                //现在底部footer
//                info_listView.addFooterView(footer);
//                new Thread(){
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        try {
//                            Thread.sleep(600);
//                            loadData(index);
//                        } catch (InterruptedException e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
//            }
//        }
//  }

    /**
     * 监听移动滚动条
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }
    }
}
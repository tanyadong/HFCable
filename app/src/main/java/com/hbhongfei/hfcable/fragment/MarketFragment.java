package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.MarketChartActivity;
import com.hbhongfei.hfcable.adapter.MyExpandableListViewAdapter;
import com.hbhongfei.hfcable.pojo.MarketInfo;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.NetUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends Fragment implements IErrorOnclick {
    private ExpandableListView expandableListView;
    private ArrayList<String> url_list = new ArrayList<>();;
    private ArrayList<String> group_list = null;
    private ArrayList<MarketInfo> child_list;
    private ArrayList<List<MarketInfo>> item_list=new ArrayList<>();;;
    private ArrayList<List<MarketInfo>> item_list1=new ArrayList<>();
    private ArrayList<Integer> groups=new ArrayList<>();
    private View view;
    private Dialog dialog;
    boolean isFirst = true;
    private MyExpandableListViewAdapter myExpandableListViewAdapter=null;
    int i=1;
    private LinearLayout noInternet;

    public MarketFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_market, container, false);

        initView(view);

     // 初始化数据
        initValues();

        return view;
    }

    @Override
    public void onResume() {
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
                default:
                    break;
            }
        }
    };

    //kaiqi 一个新的线程，异步处理数据


    /**
     * 动态加载数据
     */
    private void setValues() {
        myExpandableListViewAdapter = new MyExpandableListViewAdapter(getActivity(), group_list, item_list1, expandableListView,dialog);
        expandableListView.setAdapter(myExpandableListViewAdapter);
        //为ExpandableListView的子列表单击事件设置监听器
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), MarketChartActivity.class);
                intent.putExtra("marketInfo", item_list1.get(groupPosition).get(childPosition));
                startActivity(intent);
                return true;
            }
        });
    }

    private void initView(View view) {
        group_list = new ArrayList<>();
        dialog = new Dialog(getActivity());
        expandableListView = (ExpandableListView) view.findViewById(R.id.market_expendlist);
        noInternet = (LinearLayout) view.findViewById(R.id.no_internet_market);
    }

    /**
     * 访问网络
     */
    private void netWork(final int index) {
            final String url="http://material.cableabc.com/matermarket/spotshow_00"+index+".html";

                StringRequest request = new StringRequest(Request.Method.GET,url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //解析界面
                        parse(s,index);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (volleyError instanceof NoConnectionError){
                            Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络哦","点击设置",MarketFragment.this);
                        }else if(volleyError instanceof NetworkError ||volleyError instanceof ServerError ||volleyError instanceof TimeoutError){
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
     * 初始化数据
     */
    public void initValues() {
        //父列表数据
        group_list.add("铜");
        group_list.add("铝");
        group_list.add("橡胶");
        group_list.add("塑料");
        while (i<=4){
            netWork(i);
            i++;
        }

    }

    /**
     * 解析html
     *
     * @param html
     */
    protected void parse(String html, final int index) {
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
        if(i==1){
            myExpandableListViewAdapter = new MyExpandableListViewAdapter(getActivity(), group_list, item_list, expandableListView,dialog);
            expandableListView.setAdapter(myExpandableListViewAdapter);
        }
// else{
//            myExpandableListViewAdapter.addItems(item_list);
//            mHandler.sendEmptyMessage(0);
//        }

//            Message message=new Message();
//            message.what=1;
//            message.arg1=index;
//            message.obj=item_list;
//            mHandler.sendMessage(message);

//        dialog.cancle();
    }
    @Override
    public void onStop() {
        super.onStop();
        item_list.clear();
    }





    @Override
    public void onDestroy() {
        super.onDestroy();
        //防止handler引起的内存泄露
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void errorClick() {
        NetUtils.openSetting(MarketFragment.this.getActivity());
    }
}

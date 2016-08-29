package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.MarketChartActivity;
import com.hbhongfei.hfcable.adapter.MyExpandableListViewAdapter;
import com.hbhongfei.hfcable.pojo.MarketInfo;
import com.hbhongfei.hfcable.util.Dialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends Fragment {
    private ExpandableListView expandableListView;
    private ArrayList<String> url_list=null;
//    private TreeSet<String> url_list;
    private List<String> group_list=null;
    private List<MarketInfo> child_list=null;
    private List<List<MarketInfo>> item_list=null;
    private View view;
    private RequestQueue queue;
    private Dialog dialog;
    public MarketFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_market, container, false);
        queue= Volley.newRequestQueue(getContext());
        initview(view);
        // 初始化数据
        initvalues();
        return view;

    }

    /**
     * 动态加载数据
     */
    private void setvalues() {
        MyExpandableListViewAdapter myExpandableListViewAdapter=new MyExpandableListViewAdapter(getActivity(),group_list,item_list,expandableListView);
        expandableListView.setAdapter(myExpandableListViewAdapter);
        //为ExpandableListView的子列表单击事件设置监听器
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent=new Intent(getActivity(), MarketChartActivity.class);
                intent.putExtra("marketInfo",item_list.get(groupPosition).get(childPosition));
                startActivity(intent);
                return true;
            }
        });


}

    private void initview(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.market_expendlist);
        dialog=new Dialog(getActivity());
        item_list = new ArrayList<List<MarketInfo>>();
    }
    /**
     * 访问网络
     */
    private void netWork(final String url){

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getContext(),url,Toast.LENGTH_SHORT).show();
         String s=url;
                StringRequest request=new StringRequest(Request.Method.GET, s, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        parse(s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //请求失败
                        dialog.cancle();
                        System.out.println(volleyError);
                    }
                });
                queue.add(request);
//            }
//        }).start();
    }
    /**
     * 初始化数据
     */
    public void initvalues() {
        group_list = new ArrayList<>();
        url_list=new ArrayList<>();
        //父列表数据
        group_list.add("铜");
        group_list.add("铝");
        group_list.add("橡胶");
        group_list.add("塑料");
        dialog.showDialog("正在加载中。。。");
        //遍历地址集合
        url_list.add("http://material.cableabc.com/matermarket/spotshow_001.html");
        url_list.add("http://material.cableabc.com/matermarket/spotshow_002.html");
        url_list.add("http://material.cableabc.com/matermarket/spotshow_003.html");
        url_list.add("http://material.cableabc.com/matermarket/spotshow_004.html");
        for(String string:url_list) {
            netWork(string);
        }
//      适配器，加载数据
        setvalues();
    }
    /**
     * 解析html
     * @param html
     */
    protected void parse(String html) {
        Document doc = Jsoup.parse(html);
        //Elements
        Element table = doc.getElementsByTag("table").first();
        Elements lists = table.getElementsByTag("tr");
        child_list=new ArrayList<>();
        for (int j = 1; j < lists.size(); j++) {
            Element item = lists.get(j);
            Elements els = item.getElementsByTag("td");
            MarketInfo marketInfo=new MarketInfo();
            //遍历所有的列
            marketInfo.setArea(els.get(0).text());
            marketInfo.setProductName(els.get(1).text());
            marketInfo.setAveragePrice(els.get(2).text());
            marketInfo.setFallOrise(els.get(3).text());
            marketInfo.setMinPrice(els.get(4).text());
            marketInfo.setMaxPrice(els.get(5).text());
            marketInfo.setFactoryArea(els.get(6).text());
            marketInfo.setData(els.get(7).text());
            String url=els.get(8).getElementsByTag("a").attr("href");
            marketInfo.setTrend("http://material.cableabc.com"+url);
            child_list.add(marketInfo);
        }
        //父列表添加子列表
        item_list.add(child_list);
        dialog.cancle();
    }

    @Override
    public void onStop() {
        super.onStop();
        group_list.clear();
        child_list.clear();
        item_list.clear();
        item_list=null;
        child_list=null;
        group_list=null;
    }
}

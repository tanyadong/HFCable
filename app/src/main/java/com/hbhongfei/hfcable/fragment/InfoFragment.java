package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
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
public class InfoFragment extends Fragment{
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
    public InfoFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_info, container, false);
//        ActionBar actionbar =
//        actionbar.setDisplayHomeAsUpEnabled(true);
        //声明一个队列
        queue= Volley.newRequestQueue(getActivity());
        initView();
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
                loadData();
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
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    info_list.clear();
                    //添加数据
                    info_list.addAll((List<Information>) msg.obj);
                    mAdapter.notifyDataSetChanged();
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
    private  void  initView(){
        info_listView = (ListView) view.findViewById(R.id.fragment_info_listView);
        loadLayout= (LinearLayout) view.findViewById(R.id.fragment_load_layout);
        loading = (TextView) view.findViewById(R.id.fragment_loading);
        reload = (Button) view.findViewById(R.id.fragment_reload);

    }
    /**
     * 初始化数据
     */
    private  void setValues(){
        mAdapter = new DataAdapter(getActivity(), info_list);
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
    }
/***
 * 点击事件
 */
    private void click(){
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setText(getString(R.string.tip_text_data_loading));
                reload.setVisibility(View.GONE);
                Toast.makeText(getActivity(),"1111111111111111",Toast.LENGTH_SHORT).show();
                loadData();
            }

        });
    }
    /**
     * 加载数据
     */
    private void loadData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://news.cableabc.com/gc_0.html";
                StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        parse(s);
                        loading.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        isFirst = true;
                        mHandler.sendEmptyMessage(2);
                        System.out.println(volleyError);
                    }
                });
                queue.add(request);
            }
        }).start();
    }

    /**
     * 解析html
     * @param html
     */
    protected void parse(String html) {
        list=new ArrayList<>();
        Document doc = Jsoup.parse(html);
        //Elements
        Elements topnews = doc.getElementsByClass("list31_newlist1");

        for (Element link : topnews) {
            Information information=new Information();
            information.setTitle(link.getElementsByClass("list31_title1").text());
            information.setBrief(link.getElementsByClass("list31_text1").text());
            information.setImgUrl(link.getElementsByTag("img").attr("src"));
            information.setContentUrl(link.getElementsByClass("Pic").attr("href"));
            System.out.println(information.toString());
            list.add(information);
        }
        Message msg = new Message();
        msg.what = 1;
        msg.obj = list;
        mHandler.sendMessage(msg);
    }



}
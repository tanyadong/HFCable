package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.ImageLoader1;
import com.hbhongfei.hfcable.util.Information;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class InfoDetailActivity extends AppCompatActivity {
    private RequestQueue queue;
    private NetworkImageView infoDetail_img;
    private TextView infoDetail_title;
    private TextView infoDetail_content;
    private String[] array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_detail);
        //声明一个队列
        queue= Volley.newRequestQueue(this);
        initView();
        try {
            setValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    private void initView(){
        infoDetail_content= (TextView) findViewById(R.id.infoDetail_content);
        infoDetail_img= (NetworkImageView) findViewById(R.id.infoDetail_img);
        infoDetail_title= (TextView) findViewById(R.id.infoDetail_title);
    }
    /**
     * 数据
     */
    private void setValues() throws IOException {
        Intent intent=getIntent();
        Information information= (Information) intent.getSerializableExtra("data");
        infoDetail_title.setText(information.getTitle());
        ImageLoader1.getInstance(this).loadImage(information.getImgUrl(),infoDetail_img);
        loadData(information.getContentUrl());
    }
    private void loadData(String url){

                StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        parse(s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println(volleyError);
                    }
                });
                queue.add(request);
//                return s;
    }
    /**
     * 解析html
     * @param html
     */
    protected void parse(String html) {
//        list=new ArrayList<>();

        Document doc = Jsoup.parse(html);
        //Elements
        Element id = doc.getElementById("main_ContentPlaceHolder1_pnlContent");
        Elements contents=id.getElementsByTag("p");
        String content=new String();
        for (int i=2;i<contents.size();i++){
            content+="\u3000\u3000"+contents.get(i).text();
            content+="\n";
        }
        infoDetail_content.setText(content);
    }
}

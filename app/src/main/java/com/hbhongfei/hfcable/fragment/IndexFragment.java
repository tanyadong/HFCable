package com.hbhongfei.hfcable.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.CompanyInfoActivity;
import com.hbhongfei.hfcable.activity.ProdectListActivity;
import com.hbhongfei.hfcable.adapter.ImagePaperAdapter;
import com.hbhongfei.hfcable.pojo.Company;
import com.hbhongfei.hfcable.util.ConnectionProduct;
import com.hbhongfei.hfcable.util.Url;
import com.hbhongfei.hfcable.util.showbigpictude.ScaleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ScaleView scaleView;
    private String typeName;
    private LayoutInflater inflater;
    private ViewPager mviewPager;
    private ListView listView;
    private ImageView img1;
    private TextView textView1, textView2;
    private Button btn_typeName1, btn_typeName2, btn_typeName3, btn_typeName4, btn_typeName5, btn_typeName6;
    private RequestQueue mQueue;
    /**
     * 用于小圆点图片
     */
    private List<ImageView> dotViewList;
    /**
     * 用于存放轮播效果图片
     */
    private List<ImageView> list;
    //    添加小圆点控件，
    private LinearLayout dotLayout;

    private int currentItem = 0;//当前页面

    boolean isAutoPlay = true;//是否自动轮播

    private ScheduledExecutorService scheduledExecutorService;
    Intent intent;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 100) {
                mviewPager.setCurrentItem(currentItem);
            }
        }

    };

    public IndexFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_index, container, false);
        mQueue = Volley.newRequestQueue(this.getActivity());

        initView(view);
        connInter();
        setDate();
//        setTypeValue();
        onClick();
        if (isAutoPlay) {
            startPlay();
        }
        return view;
    }

    public void initView(View view) {
        inflater = LayoutInflater.from(getActivity());
//        scaleView= (ScaleView) view.findViewById(R.id.scrollView);
        listView = (ListView) view.findViewById(R.id.lv);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        mviewPager = (ViewPager)view.findViewById(R.id.myviewPager);
        dotLayout = (LinearLayout)view.findViewById(R.id.dotLayout);
        img1 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
        btn_typeName1 = (Button) view.findViewById(R.id.btn_type_name1);
        btn_typeName2 = (Button) view.findViewById(R.id.btn_type_name2);
        btn_typeName3 = (Button) view.findViewById(R.id.btn_type_name3);
        btn_typeName4 = (Button) view.findViewById(R.id.btn_type_name4);
        btn_typeName5 = (Button) view.findViewById(R.id.btn_type_name5);
        btn_typeName6 = (Button) view.findViewById(R.id.btn_type_name6);
    }

    /**
     * 点击事件
     */
    public void onClick() {
        btn_typeName1.setOnClickListener(this);
        btn_typeName2.setOnClickListener(this);
        btn_typeName3.setOnClickListener(this);
        btn_typeName4.setOnClickListener(this);
        btn_typeName5.setOnClickListener(this);
        btn_typeName6.setOnClickListener(this);
    }

    /**
     * 设置数据
     */
    public void setDate() {
        ConnectionProduct connectionProduct=new ConnectionProduct(IndexFragment.this.getActivity(),listView);
        try {
            connectionProduct.connInterByType("全部");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        list = new ArrayList<ImageView>();
        dotViewList = new ArrayList<ImageView>();
        dotLayout.removeAllViews();
        int[] resId = { R.mipmap.main_img1, R.mipmap.main_img2,
                R.mipmap.main_img3,R.mipmap.main_img4};
        for (int i = 0; i < resId.length; i++) {
            img1 = (ImageView) inflater.inflate(R.layout.scroll_vew_item, null);
            img1.setImageResource(resId[i]);
            img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    intent =new Intent(IndexFragment.this.getActivity(), CompanyInfoActivity.class);
                    startActivity(intent);
                }
            });
            list.add(img1);
        }
        //加入小圆点
        for (int i = 0; i < list.size(); i++) {
            ImageView indicator = new ImageView(getContext());
            if (i == 0) {
                indicator.setPadding(20, 0, 20, 0);
                indicator.setImageResource(R.mipmap.point_unpressed);
            } else {
                indicator.setPadding(20, 0, 20, 0);
                indicator.setImageResource(R.mipmap.point_pressed);
            }
            indicator.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dotLayout.addView(indicator);
            //把小圆点图片存入集合,好让切换图案片的时候动态改变小圆点背景
            dotViewList.add(indicator);
        }
        ImagePaperAdapter adapter = new ImagePaperAdapter((ArrayList) list);
        mviewPager.setAdapter(adapter);
        mviewPager.setCurrentItem(0);
        mviewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
        //根据他的参数说明，第一个参数是执行的任务，第二个参数是第一次执行的间隔，第三个参数是执行任务的周期；
    }
    /**
     * 获取产品种类服务
     */
    public void connInter(){
        String url = Url.url("androidType/getType");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,
                jsonObjectListener,errorListener);
                mQueue.add(jsonObjectRequest);
    }




    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            List<String> type_list=new ArrayList<>();
            try {
                jsonArray = jsonObject.getJSONArray("list");
                System.out.println("length"+jsonArray.length());
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject1 = (JSONObject)jsonArray.getJSONObject(i);
                    String typeName=jsonObject1.getString("typeName");
                    type_list.add(typeName);
                }
                setTypeValue(type_list);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     *  失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(IndexFragment.this.getActivity(), "无法连接服务器", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };


    /**
     * 获取公司信息
     */
    public void connInterGetCompanyInfo(){
        String url = Url.url("androidCompany/getCompanyInfo");
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET,url,null,
                jsonObjectCompanyListener,errorListener);
        mQueue.add(jsonObjectRequest);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectCompanyListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            List<Company> company_list=new ArrayList<>();
            try {
                jsonArray = jsonObject.getJSONArray("companyList");

                for(int i=0;i<jsonArray.length();i++){
                    Company company=new Company();
                    JSONObject jsonObject1 = (JSONObject)jsonArray.getJSONObject(i);
                    company.setLogo(jsonObject1.getString("logo"));
                    company.setAddress(jsonObject1.getString("address"));
                    company.setCompanyName(jsonObject1.getString("companyName"));
                    company.setDescription(jsonObject1.getString("description"));
                    company.setEmail(jsonObject1.getString("email"));
                    company.setProductIntroduction(jsonObject1.getString("productIntroduction"));
                    company.setTelephone(jsonObject1.getString("telephone"));
                    company.setZipCode(jsonObject1.getInt("zipCode"));
                    JSONArray jsonArray1=jsonObject1.getJSONArray("list");
                    ArrayList<String> list=new ArrayList<>();
                    for (int j=0;j<jsonArray1.length();j++){
                        JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                        list.add(jsonObject2.getString("image"));
                    }
                    company.setList(list);


                }
//                setTypeValue(type_list);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 设置种类名称
     */
    public void setTypeValue(List<String> list){

        btn_typeName1.setText(list.get(0));
        btn_typeName2.setText(list.get(1));
        btn_typeName3.setText(list.get(2));
        btn_typeName4.setText(list.get(3));
        btn_typeName5.setText(list.get(4));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_type_name1:
                typeName = btn_typeName1.getText().toString();
                intent = new Intent(getActivity(), ProdectListActivity.class);
                intent.putExtra("typeName", typeName);
                startActivity(intent);
                break;
            case R.id.btn_type_name2:
                typeName = btn_typeName2.getText().toString();
                intent = new Intent(getActivity(), ProdectListActivity.class);
                intent.putExtra("typeName", typeName);
                startActivity(intent);
                break;
            case R.id.btn_type_name3:
                 typeName = btn_typeName3.getText().toString();
                intent = new Intent(getActivity(), ProdectListActivity.class);
                intent.putExtra("typeName", typeName);
                startActivity(intent);
                break;
            case R.id.btn_type_name4:
                typeName = btn_typeName4.getText().toString();
                intent = new Intent(getActivity(), ProdectListActivity.class);
                intent.putExtra("typeName", typeName);
                startActivity(intent);
                break;
            case R.id.btn_type_name5:
                typeName = btn_typeName5.getText().toString();
                intent = new Intent(getActivity(), ProdectListActivity.class);
                intent.putExtra("typeName", typeName);
                startActivity(intent);
                break;
            case R.id.btn_type_name6:
                typeName = btn_typeName6.getText().toString();
                intent = new Intent(getActivity(), ProdectListActivity.class);
                intent.putExtra("typeName", typeName);
                startActivity(intent);
                break;
        }
    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (mviewPager) {
                currentItem = (currentItem + 1) % list.size();
                handler.sendEmptyMessage(100);
            }
        }
    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay = false;
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    System.out.println(" 手势滑动，空闲中");
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    System.out.println(" 界面切换中");
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (mviewPager.getCurrentItem() == mviewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        mviewPager.setCurrentItem(0);
                        System.out.println(" 滑动到最后一张");
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (mviewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        mviewPager.setCurrentItem(mviewPager.getAdapter().getCount() - 1);
                        System.out.println(" 滑动到第一张");
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onPageSelected(int position) {
            Log.i("zj", "onPagerChange position=" + position);
            for (int i = 0; i < list.size(); i++) {
                dotViewList.get(i).setImageResource(R.mipmap.point_unpressed);
            }
            dotViewList.get(position).setImageResource(R.mipmap.point_pressed);
        }

    }
}

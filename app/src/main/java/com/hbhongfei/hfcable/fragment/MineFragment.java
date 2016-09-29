package com.hbhongfei.hfcable.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.CableRingAdapter;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener,BGARefreshLayout.BGARefreshLayoutDelegate {
    private View view;
    private ListView mListView;
    private List<Map<String, Object>> list;
    private Map<String, Object> map;
    private Dialog dialog;
    private CableRingAdapter cableRingAdapter;
    private BGARefreshLayout mRefreshLayout;
    private int pageNo=1;//当前页数
    private int count;//总页数
    private int page;

    public MineFragment() {
    }
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    count=msg.arg1;
                    break;
                default:
                    break;
            }

        }

    };
    Handler mHandler = new Handler(){
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //告诉适配器，数据变化了，从新加载listview
                    cableRingAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        setOnClick();
//        //初始化数据
        initValues();
        //初始化刷新
        initRefreshLayout();
        return view;
    }

    @Override
    public void onResume() {
//        initValues();
        super.onResume();
    }

    /**
     * 初始化下拉上啦刷新
     */
    private void initRefreshLayout() {
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity().getApplication(),true);
        // 设置正在加载更多时的文本
        refreshViewHolder.setLoadingMoreText("正在加载中");
        // 设置下拉刷新和上拉加载更多的风
        mRefreshLayout.setRefreshViewHolder(refreshViewHolder);
        // 为了增加下拉刷新头部和加载更多的通用性，提供了以下可选配置选项  -------------START
//        mRefreshLayout.setPullDownRefreshEnable(false);
    }

    /**
     * 初始化界面
     * 苑雪元
     * 2016/07/21
     */
    public void initView(View v) {
        dialog = new Dialog(MineFragment.this.getActivity());
        mListView = (ListView) v.findViewById(R.id.ListView_cableZone);
        //下载刷新
        mRefreshLayout = (BGARefreshLayout)view.findViewById(R.id.Refresh_cableCZone);
    }

    /**
     * 设置点击事件
     * 苑雪元
     * 2016/07/21
     */
    private void setOnClick() {

    }


    /**
     * 初始化数据
     * 苑雪元
     * 2016/07/21
     */
    private void initValues() {
        shaftConnInter("1");
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * 获取缆圈信息服务
     */
    public void shaftConnInter(String pageNo) {
        Map<String,String> params =new HashMap<>();
        params.put("pageNo",pageNo);
        String url = Url.url("/androidCableRing/listPage");
        System.out.println(url);
        page = Integer.valueOf(pageNo);
        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url,shaftjsonObjectListener,errorListener, params);
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> shaftjsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            JSONArray jsonArray;
            list = new ArrayList<>();
            try {
                //获取总的页数，利用handler传值
                Message message=new Message();
                message.what=1;
                message.arg1=jsonObject.getInt("totalPages");
                handler.sendMessage(message);
                //获取具体的电缆信息
                jsonArray = jsonObject.getJSONArray("cableRings");
                for (int i = 0; i < jsonArray.length(); i++) {
                    map = new HashMap<>();
                    JSONObject cableRing = (JSONObject) jsonArray.getJSONObject(i);
                    String id = cableRing.getString("id");
                    map.put("id", id);
                    String content = cableRing.getString("content");
                    map.put("content", content);
                    Long createTime = cableRing.getLong("createTime");
                    map.put("createTime", createTime);
                    //图片
                    JSONArray ims=cableRing.getJSONArray("cableRingImages");
                    ArrayList<String> images=new ArrayList<>();
                    if(ims.length()>0){
                        for(int j=0;j<ims.length();j++){
                            images.add((String) ims.get(j));
                        }
                    }
                    map.put("images", images);
                    JSONObject user = cableRing.getJSONObject("user");
                    String nickName = user.getString("nickName");
                    map.put("nickName", nickName);
                    String headPortrait = user.getString("headPortrait");
                    map.put("headPortrait", headPortrait);
                    list.add(map);
                }
                if(page==1) {
                    cableRingAdapter = new CableRingAdapter(MineFragment.this.getContext(), list);
                    mListView.setAdapter(cableRingAdapter);
                    mListView.setDivider(null);
                }else{
                    cableRingAdapter.addItem(list);
                    mHandler.sendEmptyMessage(1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 失败的监听器
     */
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(MineFragment.this.getActivity(), "链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };


    /**
     * 下拉刷新
     * @param refreshLayout
     * @throws JSONException
     */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        pageNo=1;
        new MyAsyncTack().execute();
    }

    /**
     * 上拉加载
     * @param refreshLayout
     * @return
     */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(pageNo<count) {
            pageNo++;
            // 如果网络可用，则加载网络数据
            new MyAsyncTack().execute();
        }else{
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    class MyAsyncTack extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params) {
            shaftConnInter(""+pageNo);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRefreshLayout.endLoadingMore();
            mRefreshLayout.endRefreshing();
            super.onPostExecute(aVoid);
        }
    }
}

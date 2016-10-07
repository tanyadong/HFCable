package com.hbhongfei.hfcable.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.LoginActivity;
import com.hbhongfei.hfcable.activity.SplashActivity;
import com.hbhongfei.hfcable.activity.WriteCableRingActivity;
import com.hbhongfei.hfcable.adapter.CommentAdapter;
import com.hbhongfei.hfcable.util.DateUtils;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NetUtils;
import com.hbhongfei.hfcable.util.NineGridTestLayout;
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
public class MineFragment extends BaseFragment implements View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate, IErrorOnclick {
    private View view;
    private ListView mListView;
    private List<Map<String, Object>> list;
    private Map<String, Object> map;
    private Dialog dialog;
    private CableRingAdapters cableRingAdapter;
    private BGARefreshLayout mRefreshLayout;
    private int pageNo = 1;//当前页数
    private int count;//总页数
    private int page;
    private static final String LOGINORNOT = LoginConnection.USER;
    String loginOrNot;
    private CommentAdapter commentAdapter;
    private boolean tag = false;//判断是都是冲刷一次
    private static final int ALL = 10;//代表默认的刷新个数
    private boolean toWrite;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private LinearLayout noInternet;
    private FloatingActionButton fab;

    public MineFragment() {
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    count = msg.arg1;
                    break;
                default:
                    break;
            }

        }

    };
    Handler mHandler = new Handler() {
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
        //初始化刷新
        initRefreshLayout();
        isPrepared = true;
        toWrite = false;
        lazyLoad();
        return view;
    }

    @Override
    public void onResume() {
        getValues();
        if (toWrite){
            initValues();
            toWrite = false;
        }
        super.onResume();
    }

    /**
     * 初始化下拉上啦刷新
     */
    private void initRefreshLayout() {
        // 为BGARefreshLayout设置代理
        mRefreshLayout.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        BGARefreshViewHolder refreshViewHolder = new BGANormalRefreshViewHolder(getActivity().getApplication(), true);
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
        SplashActivity.ID = 4;
        dialog = new Dialog(MineFragment.this.getActivity());
        mListView = (ListView) v.findViewById(R.id.ListView_cableZone);
        //下载刷新
        mRefreshLayout = (BGARefreshLayout) view.findViewById(R.id.Refresh_cableCZone);
        noInternet = (LinearLayout) view.findViewById(R.id.no_internet_mine);

        fab = (FloatingActionButton) v.findViewById(R.id.fab_mine);
    }

    /**
     * 设置点击事件
     * 苑雪元
     * 2016/07/21
     */
    private void setOnClick() {
        fab.setOnClickListener(this);
    }


    /**
     * 初始化数据
     * 苑雪元
     * 2016/07/21
     */
    private void initValues() {
        shaftConnInter("1",ALL+"");
    }


    /**
     * 获取数据
     */
    private void getValues() {
        SharedPreferences spf = MineFragment.this.getContext().getSharedPreferences(LOGINORNOT, Context.MODE_PRIVATE);
        loginOrNot = spf.getString("id", null);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.fab_mine:
                if (loginOrNot != null) {
                    intent.setClass(MineFragment.this.getActivity(), WriteCableRingActivity.class);
                    intent.putExtra("tag", "main");
                    toWrite = true;
                } else {
                    intent.setClass(MineFragment.this.getActivity(), LoginActivity.class);
                    Toast.makeText(MineFragment.this.getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        startActivity(intent);
    }

    /**
     * 获取缆圈信息服务
     */
    public void shaftConnInter(String pageNo,String all) {
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo);
        params.put("all", all);
        String url = Url.url("/androidComment/getAllComment");
        System.out.println(url);
        page = Integer.valueOf(pageNo);
        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url, shaftjsonObjectListener, errorListener, params);
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> shaftjsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            noInternet.setVisibility(View.GONE);
            JSONArray jsonArray;
            list = new ArrayList<>();
            try {
                //获取总的页数，利用handler传值
                Message message = new Message();
                message.what = 1;
                message.arg1 = jsonObject.getInt("totalPages");
                handler.sendMessage(message);
                //获取具体的电缆信息
                if (jsonObject.optJSONArray("cableRings") != null) {
                    jsonArray = jsonObject.optJSONArray("cableRings");
                    if (jsonArray.length() != 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            map = new HashMap<>();
                            JSONObject cableRings = jsonArray.optJSONObject(i);
                            //缆圈信息
                            JSONObject cableRing = cableRings.getJSONObject("cableRing");
                            map.put("id", cableRing.getString("id"));//缆圈的id
                            map.put("content", cableRing.getString("content"));//缆圈的文字内容
                            map.put("createTime", cableRing.getLong("createTime"));//缆圈的创建时间
                            JSONObject user = cableRing.getJSONObject("user");//缆圈的发布者
                            map.put("nickName", user.getString("nickName"));//缆圈的发布者昵称
                            map.put("headPortrait", user.getString("headPortrait"));//缆圈的发布者头像
                            JSONArray ims = cableRing.getJSONArray("cableRingImages");
                            ArrayList<String> images = new ArrayList<>();
                            if (ims.length() > 0) {
                                for (int j = 0; j < ims.length(); j++) {
                                    images.add((String) ims.get(j));
                                }
                            }
                            map.put("images", images);//缆圈的图片内容
                            //缆圈的评论内容
                            if (cableRings.optJSONArray("comments") != null) {
                                List<Map<String, Object>> listComment = new ArrayList<>();
                                JSONArray comments = cableRings.optJSONArray("comments");
                                for (int j = 0; j < comments.length(); j++) {
                                    Map<String, Object> commentMap = new HashMap<>();
                                    JSONObject comment = comments.optJSONObject(j);
                                    commentMap.put("nickName", comment.getJSONObject("user").getString("nickName"));
                                    commentMap.put("commentContent", comment.getString("commentContent"));
                                    listComment.add(commentMap);
                                }
                                map.put("comments", listComment);
                            } else {
                                map.put("comments", null);
                            }
                            list.add(map);
                        }

                    } else {
                        Error.toSetting(noInternet, R.mipmap.nothing, "暂时没有缆圈信息", "赶紧去发布一个吧", MineFragment.this);
                    }
                    if (page == 1&& tag == false) {
                        cableRingAdapter = new CableRingAdapters(MineFragment.this.getContext(), list, fab);
                        mListView.setAdapter(cableRingAdapter);
                        mListView.setDivider(null);
                        mListView.setDividerHeight(30);
                    } else if (page!=1 &&tag == false){
                        cableRingAdapter.addItem(list);
                        mHandler.sendEmptyMessage(1);
                    }else{
                        tag = false;
                        cableRingAdapter.updateItem(list);
                        mHandler.sendEmptyMessage(1);
                    }
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
            if (volleyError instanceof NoConnectionError) {
                Error.toSetting(noInternet, R.mipmap.internet_no, "没有网络哦", "点击设置", MineFragment.this);
            } else if (volleyError instanceof NetworkError || volleyError instanceof ServerError || volleyError instanceof TimeoutError) {
                Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "服务器出错啦", new IErrorOnclick() {
                    @Override
                    public void errorClick() {

                    }
                });
            } else {
                Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "出错啦", new IErrorOnclick() {
                    @Override
                    public void errorClick() {
                    }
                });
            }

        }
    };


    /**
     * 下拉刷新
     *
     * @param refreshLayout
     * @throws JSONException
     */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) throws JSONException {
        pageNo = 1;
        new MyAsyncTack().execute();
    }

    /**
     * 上拉加载
     *
     * @param refreshLayout
     * @return
     */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if (pageNo < count) {
            pageNo++;
            // 如果网络可用，则加载网络数据
            new MyAsyncTack().execute();
        } else {
            mRefreshLayout.endLoadingMore();
            return false;
        }
        return true;
    }

    @Override
    public void errorClick() {
        NetUtils.openSetting(MineFragment.this.getActivity());
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        initValues();
        mHasLoadedOnce = true;
    }

    class MyAsyncTack extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            shaftConnInter("" + pageNo,ALL+"");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mRefreshLayout.endLoadingMore();
            mRefreshLayout.endRefreshing();
            super.onPostExecute(aVoid);
        }
    }


    class CableRingAdapters extends BaseAdapter {

        public static final int VIEW_HEADER = 0;
        public static final int VIEW_MOMENT = 1;
        private InputMethodManager imm;

        private List<Map<String, Object>> mList;
        private Context mContext;
        private FloatingActionButton btn;

        public CableRingAdapters(Context context, List<Map<String, Object>> list, FloatingActionButton btn) {
            mList = list;
            mContext = context;
            this.btn = btn;
        }

        public void addItem(List<Map<String, Object>> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

        public void updateItem(List<Map<String, Object>> list){
            this.mList = list;
            notifyDataSetChanged();
        }


        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? VIEW_HEADER : VIEW_MOMENT;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_cable_ring, null);
                holder.mContent = (TextView) convertView.findViewById(R.id.content);
                holder.head = (ImageView) convertView.findViewById(R.id.author_icon);
                holder.nickName = (TextView) convertView.findViewById(R.id.name);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.comment = (RelativeLayout) convertView.findViewById(R.id.comment_cable_ring);
                holder.listView_comment = (ListView) convertView.findViewById(R.id.ListView_comment);
                holder.tComment = (TextView) convertView.findViewById(R.id.Tview_our_comment);
                holder.linear = (LinearLayout) convertView.findViewById(R.id.emo_linear);
                holder.commentContent = (EditText) convertView.findViewById(R.id.et_our_comment);
                holder.cableLinear = (LinearLayout) convertView.findViewById(R.id.item_cable_ring_linear);
                holder.nineGridTestLayout = (NineGridTestLayout) convertView.findViewById(R.id.layout_nine_grid);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //获取当前用户
            SharedPreferences spf = mContext.getSharedPreferences(LoginConnection.USER, Context.MODE_PRIVATE);
            final String S_phoneNumber = spf.getString("phoneNumber", null);

            //设置数据
            Map<String, Object> map = new HashMap<>();
            map = mList.get(position);
            //设置内容
            holder.mContent.setText((String) map.get("content"));
            //设置昵称
            holder.nickName.setText((String) map.get("nickName"));
            //设置创建时间
            Long date = (Long) map.get("createTime");
            holder.time.setText(DateUtils.convertTimeToFormat(date));
            //设置头像
            String url = Url.url((String) map.get("headPortrait"));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.img_loading)
                    .error(R.mipmap.img_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.head);
            //设置点击评论
            WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            final int width = manager.getDefaultDisplay().getWidth();
            final int height = manager.getDefaultDisplay().getHeight();

            //点击item关闭键盘
            final ViewHolder finalHolder = holder;
            holder.cableLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    // 关闭软键盘
                    imm.hideSoftInputFromWindow(finalHolder.commentContent.getWindowToken(), 0);
                    finalHolder.linear.setVisibility(View.GONE);
                    finalHolder.comment.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.VISIBLE);
                }
            });

            // 评论图标点击事件
            holder.comment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (finalHolder.linear.isShown() == false) {
                        finalHolder.linear.setVisibility(View.VISIBLE);
                        finalHolder.comment.setVisibility(View.GONE);
                        // 获取编辑框焦点
                        finalHolder.commentContent.setFocusable(true);
                        finalHolder.commentContent.requestFocus();
                        // 打开软键盘
                        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                        btn.setVisibility(View.GONE);
                    }
                }
            });

            //输入问题是按钮的变化
            holder.commentContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String commentText = finalHolder.commentContent.getText().toString().trim();
                    if (commentText != null && !commentText.equals("")) {
                        finalHolder.tComment.setBackgroundResource(R.color.colorRed);
                        finalHolder.tComment.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                    } else {
                        finalHolder.tComment.setBackgroundResource(R.color.colorGray);
                        finalHolder.tComment.setTextColor(mContext.getResources().getColor(R.color.colorBalck));
                    }
                }
            });

            final String[] id = {(String) map.get("id")};
            //展示评论
            List<Map<String, Object>> commentList = new ArrayList<>();
            commentList = (List<Map<String, Object>>) map.get("comments");
            commentAdapter = new CommentAdapter(mContext, commentList);

            holder.listView_comment.setAdapter(commentAdapter);

            //提交评论
            holder.tComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String commentText = finalHolder.commentContent.getText().toString().trim();
                    //不为空
                    if (commentText != null && !commentText.equals("")) {
                        connInter(id[0], S_phoneNumber, commentText);
                        finalHolder.commentContent.setText("");
                    } else {
                        Toast.makeText(mContext, "请输入内容", Toast.LENGTH_SHORT).show();
                    }
                    // 关闭软键盘
                    imm.hideSoftInputFromWindow(finalHolder.commentContent.getWindowToken(), 0);
                    finalHolder.linear.setVisibility(View.GONE);
                    finalHolder.comment.setVisibility(View.VISIBLE);
                    btn.setVisibility(View.VISIBLE);
                }
            });


            //设置图片
            ArrayList<String> images = (ArrayList<String>) map.get("images");
            holder.nineGridTestLayout.setIsShowAll(false); //当传入的图片数超过9张时，是否全部显示
            holder.nineGridTestLayout.setSpacing(10); //动态设置图片之间的间隔
            holder.nineGridTestLayout.setUrlList(images); //最后再设置图片url
            return convertView;
        }

        private class ViewHolder {
            TextView mContent, nickName, time, tComment;
            RelativeLayout comment;
            ImageView head;
            ListView listView_comment;
            LinearLayout linear, cableLinear;
            EditText commentContent;
            NineGridTestLayout nineGridTestLayout;
        }

    }


    /**
     * 连接服务
     */
    public void connInter(String cableRingId, String phone, String commentContent) {
        Map<String, String> params = new HashMap<>();
        params.put("id", cableRingId);
        params.put("phoneNumber", phone);
        params.put("commentContent", commentContent);
        String url = Url.url("/androidComment/saveComment");
        //使用自己书写的NormalPostRequest类，
        Request<JSONObject> request = new NormalPostRequest(url, jsonObjectListener, errorListener, params);
        MySingleton.getInstance(MineFragment.this.getContext()).addToRequestQueue(request);
    }

    /**
     * 成功的监听器
     */
    private Response.Listener<JSONObject> jsonObjectListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("save");
                if (msg.equals("success")) {
                    Toast.makeText(MineFragment.this.getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                    //通知重新加载
                    tag = true;
                    shaftConnInter(1+"",(pageNo*ALL)+"");
                } else {
                    Toast.makeText(MineFragment.this.getContext(), "评论失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}

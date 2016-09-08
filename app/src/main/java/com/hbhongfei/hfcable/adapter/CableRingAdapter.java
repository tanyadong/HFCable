package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.CommentActivity;
import com.hbhongfei.hfcable.util.GetComment;
import com.hbhongfei.hfcable.util.LoginConnection;
import com.hbhongfei.hfcable.util.SaveComment;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by 苑雪元 on 16-4-12.
 */
public class CableRingAdapter extends BaseAdapter {

    public static final int VIEW_HEADER = 0;
    public static final int VIEW_MOMENT = 1;
    private InputMethodManager imm;

    private List<Map<String,Object>> mList;
    private Context mContext;

    public CableRingAdapter(Context context, List<Map<String,Object>> list) {
        mList = list;
        mContext = context;
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

            holder.imageView1 = (ImageView) convertView.findViewById(R.id.Image_cableRing_1);
            holder.imageView2 = (ImageView) convertView.findViewById(R.id.Image_cableRing_2);
            holder.imageView3 = (ImageView) convertView.findViewById(R.id.Image_cableRing_3);
            holder.imageView4 = (ImageView) convertView.findViewById(R.id.Image_cableRing_4);
            holder.imageView5 = (ImageView) convertView.findViewById(R.id.Image_cableRing_5);
            holder.imageView6 = (ImageView) convertView.findViewById(R.id.Image_cableRing_6);
            holder.imageView7 = (ImageView) convertView.findViewById(R.id.Image_cableRing_7);
            holder.imageView8 = (ImageView) convertView.findViewById(R.id.Image_cableRing_8);
            holder.imageView9 = (ImageView) convertView.findViewById(R.id.Image_cableRing_9);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        //获取当前用户
        SharedPreferences spf = mContext.getSharedPreferences(LoginConnection.USER, Context.MODE_PRIVATE);
        final String S_phoneNumber = spf.getString("phoneNumber",null);

        //设置数据
        Map<String,Object> map = new HashMap<>();
        map = mList.get(position);
        //设置内容
        holder.mContent.setText((String) map.get("content"));
        //设置昵称
        holder.nickName.setText((String) map.get("nickName"));
        //设置创建时间
        holder.time.setText((String) map.get("createTime"));
        //设置头像
        String url = Url.url((String) map.get("headPortrait"));
        Glide.with(mContext)
                .load(url)
                .placeholder(R.mipmap.man)
                .error(R.mipmap.man)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.head );


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
            }
        });

        // 评论图标点击事件
        holder.comment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(finalHolder.linear.isShown()==false){
                    finalHolder.linear.setVisibility(View.VISIBLE);
                    finalHolder.comment.setVisibility(View.GONE);
                    /**
                     * 评论图标点击事件
                     */
                    // 获取编辑框焦点
                    finalHolder.commentContent.setFocusable(true);
                    finalHolder.commentContent.requestFocus();
                    // 打开软键盘
                    imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
                if (commentText!=null&&!commentText.equals("")){
                    finalHolder.tComment.setBackgroundResource(R.color.colorRed);
                    finalHolder.tComment.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
                }else{
                    finalHolder.tComment.setBackgroundResource(R.color.colorGray);
                    finalHolder.tComment.setTextColor(mContext.getResources().getColor(R.color.colorBalck));
                }
            }
        });

        final String[] id = {(String) map.get("id")};
        //提交评论
        holder.tComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = finalHolder.commentContent.getText().toString().trim();
                //不为空
                if (commentText!=null&&!commentText.equals("")) {
                    SaveComment saveComment = new SaveComment(mContext,id[0],S_phoneNumber,commentText);
                    saveComment.connInter();
                    finalHolder.commentContent.setText("");

                    // 点击后通知适配器数据发生了变化
                    notifyDataSetChanged();

                }else{
                    Toast.makeText(mContext, "请输入内容" + "", Toast.LENGTH_SHORT).show();
                }
                // 关闭软键盘
                imm.hideSoftInputFromWindow(finalHolder.commentContent.getWindowToken(), 0);
                finalHolder.linear.setVisibility(View.GONE);
                finalHolder.comment.setVisibility(View.VISIBLE);

            }
        });


        //展示评论
        //根据id获取评论
        GetComment getComment = new GetComment(mContext, id[0],holder.listView_comment);
        getComment.connInter();

        //设置图片
        ArrayList<String> images= (ArrayList<String>) map.get("images");

        if (images.size()==0){
            holder.imageView1.setVisibility(View.GONE);
            holder.imageView2.setVisibility(View.GONE);
            holder.imageView3.setVisibility(View.GONE);
            holder.imageView4.setVisibility(View.GONE);
            holder.imageView5.setVisibility(View.GONE);
            holder.imageView6.setVisibility(View.GONE);
            holder.imageView7.setVisibility(View.GONE);
            holder.imageView8.setVisibility(View.GONE);
            holder.imageView9.setVisibility(View.GONE);
        }else if(images.size()==1){
            String url1 = Url.url(images.get(0));
            System.out.println(url1);
            Glide.with(mContext)
                    .load(url1)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            holder.imageView2.setVisibility(View.GONE);
            holder.imageView3.setVisibility(View.GONE);
            holder.imageView4.setVisibility(View.GONE);
            holder.imageView5.setVisibility(View.GONE);
            holder.imageView6.setVisibility(View.GONE);
            holder.imageView7.setVisibility(View.GONE);
            holder.imageView8.setVisibility(View.GONE);
            holder.imageView9.setVisibility(View.GONE);
        }else if (images.size()==2){
            url = Url.url(images.get(0));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            url = Url.url(images.get(1));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView2 );
            holder.imageView3.setVisibility(View.GONE);
            holder.imageView4.setVisibility(View.GONE);
            holder.imageView5.setVisibility(View.GONE);
            holder.imageView6.setVisibility(View.GONE);
            holder.imageView7.setVisibility(View.GONE);
            holder.imageView8.setVisibility(View.GONE);
            holder.imageView9.setVisibility(View.GONE);
        }else if (images.size()==3){
            url = Url.url(images.get(0));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            url = Url.url(images.get(1));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView2 );
            url = Url.url(images.get(2));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView3 );
            holder.imageView4.setVisibility(View.GONE);
            holder.imageView5.setVisibility(View.GONE);
            holder.imageView6.setVisibility(View.GONE);
            holder.imageView7.setVisibility(View.GONE);
            holder.imageView8.setVisibility(View.GONE);
            holder.imageView9.setVisibility(View.GONE);
        }else if (images.size()==4){
            url = Url.url(images.get(0));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            url = Url.url(images.get(1));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView2 );
            url = Url.url(images.get(2));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView3 );
            url = Url.url(images.get(3));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView4 );
            holder.imageView5.setVisibility(View.GONE);
            holder.imageView6.setVisibility(View.GONE);
            holder.imageView7.setVisibility(View.GONE);
            holder.imageView8.setVisibility(View.GONE);
            holder.imageView9.setVisibility(View.GONE);
        }else if (images.size()==5){
            url = Url.url(images.get(0));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            url = Url.url(images.get(1));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView2 );
            url = Url.url(images.get(2));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView3 );
            url = Url.url(images.get(3));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView4 );
            url = Url.url(images.get(4));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView5 );
            holder.imageView6.setVisibility(View.GONE);
            holder.imageView7.setVisibility(View.GONE);
            holder.imageView8.setVisibility(View.GONE);
            holder.imageView9.setVisibility(View.GONE);
        }else if (images.size()==6){
            url = Url.url(images.get(0));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            url = Url.url(images.get(1));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView2 );
            url = Url.url(images.get(2));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView3 );
            url = Url.url(images.get(3));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView4 );
            url = Url.url(images.get(4));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView5 );
            url = Url.url(images.get(5));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView6 );
            holder.imageView7.setVisibility(View.GONE);
            holder.imageView8.setVisibility(View.GONE);
            holder.imageView9.setVisibility(View.GONE);
        }else if (images.size()==7){
            url = Url.url(images.get(0));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            url = Url.url(images.get(1));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView2 );
            url = Url.url(images.get(2));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView3 );
            url = Url.url(images.get(3));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView4 );
            url = Url.url(images.get(4));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView5 );
            url = Url.url(images.get(5));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView6 );
            url = Url.url(images.get(6));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView7 );
            holder.imageView8.setVisibility(View.GONE);
            holder.imageView9.setVisibility(View.GONE);
        }else if (images.size()==8){
            url = Url.url(images.get(0));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            url = Url.url(images.get(1));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView2 );
            url = Url.url(images.get(2));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView3 );
            url = Url.url(images.get(3));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView4 );
            url = Url.url(images.get(4));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView5 );
            url = Url.url(images.get(5));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView6 );
            url = Url.url(images.get(6));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView7 );
            url = Url.url(images.get(7));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView8 );
            holder.imageView9.setVisibility(View.GONE);
        }else if (images.size()==9){
            url = Url.url(images.get(0));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView1 );
            url = Url.url(images.get(1));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView2 );
            url = Url.url(images.get(2));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView3 );
            url = Url.url(images.get(3));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView4 );
            url = Url.url(images.get(4));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView5 );
            url = Url.url(images.get(5));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView6 );
            url = Url.url(images.get(6));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView7 );
            url = Url.url(images.get(7));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView8 );
            url = Url.url(images.get(8));
            Glide.with(mContext)
                    .load(url)
                    .placeholder(R.mipmap.man)
                    .error(R.mipmap.man)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into( holder.imageView9 );
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView mContent,nickName,time,tComment;
        RelativeLayout comment;
        ImageView head,imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9;
        ListView listView_comment;
        LinearLayout linear,cableLinear;
        EditText commentContent;
    }
}

package com.hbhongfei.hfcable.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 苑雪元 on 2016/9/7.
 */
public class CommentAdapter extends BaseAdapter{

    private List<Map<String,Object>> list;
    private Context context;

    public CommentAdapter(Context context, List<Map<String,Object>> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list!=null){
            return list.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (list!=null){
            return list.get(position);
        }else{
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if (list!=null){
            return position;
        }else{
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if ( convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_comment, null);
            holder.nickName = (TextView) convertView.findViewById(R.id.Tview_item_comment_nickName);
            holder.comment = (TextView) convertView.findViewById(R.id.Tview_item_comment_content);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据

        Map<String,Object> map = new HashMap<>();
        if (list!=null){
            map = list.get(position);
            //昵称
            String S_nickName = (String) map.get("nickName");
            holder.nickName.setText(S_nickName);

            //评论内容
            String S_comment = (String) map.get("commentContent");
            holder.comment.setText(S_comment);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView nickName,comment;
    }

    public void update(List<Map<String,Object>> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }
}

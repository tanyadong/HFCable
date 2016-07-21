package com.hbhongfei.hfcable.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.util.MySpinner;

import java.util.ArrayList;

public class SpinnerListAdapter extends BaseAdapter {
    private ArrayList<Object> list;
    private int mPosition;
    private String[] mItems;
    private MySpinner mSpinner;
    private Activity mActivity;
    private onItemClickListener mListener;

//    public SpinnerListAdapter(MySpinner spinner, Activity activity, ArrayList<Object> arrayList){
//        this.mActivity=activity;
//        this.list=arrayList;
//        this.mSpinner =spinner;
//    }
    public SpinnerListAdapter(MySpinner spinner, Activity activity, String[] items){
        this.mActivity=activity;
        this.mItems=items;
        this.mSpinner =spinner;
    }
    public int getCount() {
        return mItems.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder {
        public TextView text;
    }

    public View getView(final int arg0, final View arg1, ViewGroup arg2) {
        //获取设置好的listener
        mListener= mSpinner.getListener();
        View view=arg1;
        ViewHolder holder=null;
        if(view==null){
            view= View.inflate(mActivity, R.layout.baseadapter_provider, null);
            holder = new ViewHolder();
            holder.text=(TextView) view.findViewById(R.id.typeName_textview);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        holder.text.setText(mItems[arg0]);
//        holder.text.setText(list.get(arg0).g);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mPosition=arg0; 
                mSpinner.close();
                mListener.click(mPosition,arg1);
            }
        });
        return view;
    }

    //回调接口
    public interface onItemClickListener{
        public void click(int position, View view);
    }   
}
package com.hbhongfei.hfcable.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.AddRecietAddress;
import com.hbhongfei.hfcable.activity.MyOrderActivity;
import com.hbhongfei.hfcable.activity.MyShoppingActivity;
import com.hbhongfei.hfcable.pojo.ShoppingAddress;
import com.hbhongfei.hfcable.util.NormalPostRequest;
import com.hbhongfei.hfcable.util.ShoppingAddress_conn;
import com.hbhongfei.hfcable.util.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class AddressListAdapter extends BaseAdapter {
	Context mContext = null;
	LayoutInflater inflater;
	List<ShoppingAddress> list = new ArrayList<ShoppingAddress>();
	String phoneNum;
	ListView listView;
	LinearLayout linearLayout;
	SweetAlertDialog sweetAlertDialog=null;
	Activity activity;
	public AddressListAdapter(
			Activity activity,Context context, List<ShoppingAddress> nList, String S_phone, ListView listView, LinearLayout linearLayout) {
		mContext = context;
		this.activity = activity;
		inflater = LayoutInflater.from(context);
		this.list = nList;
		this.listView=listView;
		this.phoneNum=S_phone;
		this.linearLayout=linearLayout;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		HolderView hView = null;
		if (null == convertView) {
			hView = new HolderView();
			convertView = inflater.inflate(R.layout.item_address_list, null);
			hView.name = (TextView) convertView.findViewById(R.id.textview_address_list_name);
			hView.phoneNumber = (TextView) convertView.findViewById(R.id.textview_address_list_phoneNumber);
			hView.address = (TextView) convertView.findViewById(R.id.textview_address_list_detail);
			hView.addressDefault = (TextView) convertView.findViewById(R.id.textview_address_list_detail_default);
			hView.edit = (ImageView) convertView.findViewById(R.id.Image_address_list);
			hView.addressList = (RelativeLayout) convertView.findViewById(R.id.Rlayout_address_list);
			convertView.setTag(hView);
		} else {
			hView = (HolderView) convertView.getTag();
		}
		hView.name.setText(list.get(position).getConsignee());
		hView.phoneNumber.setText(list.get(position).getPhone());
		hView.address.setText(list.get(position).getLocalArea()+list.get(position).getDetailAddress());
		if (list.get(position).getTag()==1){
			hView.addressDefault.setVisibility(View.VISIBLE);
		} else {
			hView.addressDefault.setVisibility(View.GONE);
		}
		//编辑地址事件
		hView.edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext.getApplicationContext(), AddRecietAddress.class);
				intent.putExtra("shoppingAddress",list.get(position));
				intent.putExtra("tag","edit");
				mContext.startActivity(intent);
			}
		});
		hView.addressList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent= activity.getIntent();
				intent.putExtra("shoppingAddress",list.get(position));
				activity.setResult(0, intent);
				activity.finish();
			}
		});

		return convertView;
	}

	public class HolderView {
		private TextView name,phoneNumber,address,addressDefault;
		private ImageView edit;
		private RelativeLayout addressList;
	}
}

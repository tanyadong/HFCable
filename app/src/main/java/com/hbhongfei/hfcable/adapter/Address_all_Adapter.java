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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.activity.AddRecietAddress;
import com.hbhongfei.hfcable.pojo.ShoppingAddress;
import com.hbhongfei.hfcable.util.MySingleton;
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


public class Address_all_Adapter extends BaseAdapter {
	Context mContext = null;
	LayoutInflater inflater;
	List<ShoppingAddress> list = new ArrayList<ShoppingAddress>();
	String phoneNum;
	ListView listView;
	LinearLayout linearLayout;
	SweetAlertDialog sweetAlertDialog=null;
	private LinearLayout noInternet;
	private Activity activity;
	public Address_all_Adapter(Activity activity,Context context, List<ShoppingAddress> nList, String S_phone, ListView listView, LinearLayout linearLayout,LinearLayout noInternet) {
		mContext = context;
		inflater = LayoutInflater.from(context);
		this.list = nList;
		this.listView=listView;
		this.phoneNum=S_phone;
		this.linearLayout=linearLayout;
		this.noInternet = noInternet;
		this.activity = activity;
	}
	public List<ShoppingAddress> addItems(List<ShoppingAddress> list){
		this.list.addAll(list);
		return this.list;
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
			convertView = inflater.inflate(R.layout.address_list_layout, null);
			hView.img_defaule_address = (ImageView) convertView.findViewById(R.id.img_defaule_address);
			hView.textview_address_detail = (TextView) convertView.findViewById(R.id.textview_address_detail);
			hView.textview_address_name = (TextView)convertView.findViewById(R.id.textview_address_name);
			hView.textview_edit_address= (TextView) convertView.findViewById(R.id.textview_edit_address);
			hView.textview_delete_address= (TextView) convertView.findViewById(R.id.textview_delete_address);
			hView.textview_address_phoneNumber= (TextView) convertView.findViewById(R.id.textview_address_phoneNumber);

			hView.textview_defaule_address= (TextView) convertView.findViewById(R.id.textview_defaule_address);
			hView.llayout_defaule_address= (LinearLayout) convertView.findViewById(R.id.llayout_defaule_address);
			hView.llayout_edit_address= (LinearLayout) convertView.findViewById(R.id.llayout_edit_address);
			hView.llayout_delete_address= (LinearLayout) convertView.findViewById(R.id.llayout_delete_address);
			convertView.setTag(hView);
		} else {
			hView = (HolderView) convertView.getTag();
		}
		hView.textview_address_name.setText(list.get(position).getConsignee());
		hView.textview_address_phoneNumber.setText(list.get(position).getPhone());
		hView.textview_address_detail.setText(list.get(position).getLocalArea()+list.get(position).getDetailAddress());
		if (list.get(position).getTag()==1){
			hView.img_defaule_address.setImageResource(R.mipmap.selected);
			hView.textview_defaule_address.setTextColor(mContext.getResources().getColor(R.color.colorRed));
			hView.llayout_defaule_address.setClickable(false);
		} else {
			hView.img_defaule_address.setImageResource(R.mipmap.select);
			hView.textview_defaule_address.setTextColor(mContext.getResources().getColor(R.color.common_signin_btn_light_text_default));
		}
		//删除地址事件
		hView.llayout_delete_address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				new SweetAlertDialog(mContext,SweetAlertDialog.WARNING_TYPE)
				sweetAlertDialog=new SweetAlertDialog(mContext,SweetAlertDialog.WARNING_TYPE);
				sweetAlertDialog.setContentText("您确定要删除吗？")
						.showCancelButton(true)
						.setTitleText("删除")
						.setCancelText("取消")
						.setConfirmText("确认")
						.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								sweetAlertDialog.dismiss();
							}
						})
						.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sweetAlertDialog) {
								deleteConnection(list.get(position).getId());
								sweetAlertDialog.dismiss();
							}
						})
						.show();
			}
		});
		//编辑地址事件
		hView.llayout_edit_address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(mContext.getApplicationContext(), AddRecietAddress.class);
				intent.putExtra("shoppingAddress",list.get(position));
				intent.putExtra("tag","edit");
				mContext.startActivity(intent);
			}
		});
		//设置默认地址
		if(list.get(position).getTag()==0){
			hView.llayout_defaule_address.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setDefauleConnection(list.get(position).getId());
				}
			});
		}

		return convertView;
	}

	public class HolderView {

		private TextView textview_address_name= null;
		private TextView textview_address_phoneNumber = null;
		private TextView textview_address_detail=null;
		private TextView textview_defaule_address=null;
		private TextView textview_edit_address=null;
		private TextView textview_delete_address=null;
		private ImageView img_defaule_address=null;
		private LinearLayout llayout_defaule_address,llayout_edit_address,llayout_delete_address;
	}

	/**
	 * 删除地址的服务
	 */
	private void deleteConnection(String id){

		String url= Url.url("/androidAddress/deleteAddress");
		Map<String,String> map=new HashMap<>();
		map.put("addressId",id);
		NormalPostRequest normalPostRequest=new NormalPostRequest(url,deleteSuccessListener,errorListener,map);
		MySingleton.getInstance(mContext).addToRequestQueue(normalPostRequest);
	}

    /**
     * 删除收货地址成功的监听器
     */
    private Response.Listener<JSONObject> deleteSuccessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("msg");
                if (msg.equals("success")) {
                    sweetAlertDialog.dismiss();
                    ShoppingAddress_conn shoppingAddress_conn=new ShoppingAddress_conn(mContext,phoneNum,activity,listView,linearLayout,noInternet);
                    shoppingAddress_conn.addressListConnection();
                    Address_all_Adapter.this.notifyDataSetChanged();
                    Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, "链接网络失败", Toast.LENGTH_SHORT).show();
            Log.e("TAG", volleyError.getMessage(), volleyError);
        }
    };

    /**
     * 设置默认地址的服务
     */
    private void setDefauleConnection(String id) {
        String url = Url.url("/androidAddress/setDefauleAddress");
        Map<String, String> map = new HashMap<>();
        map.put("addressId", id);
        map.put("userName", phoneNum);
        NormalPostRequest normalPostRequest = new NormalPostRequest(url, setDefauleSuccessListener, errorListener, map);
        MySingleton.getInstance(mContext).addToRequestQueue(normalPostRequest);
    }

    /**
     * 设置默认收货地址成功的监听器
     */
    private Response.Listener<JSONObject> setDefauleSuccessListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject jsonObject) {
            try {
                String msg = jsonObject.getString("msg");
                if (msg.equals("success")) {
					ShoppingAddress_conn shoppingAddress_conn=new ShoppingAddress_conn(mContext,phoneNum,activity,listView,linearLayout,noInternet);
					shoppingAddress_conn.addressListConnection();
                    Toast.makeText(mContext, "设置成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "设置失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}

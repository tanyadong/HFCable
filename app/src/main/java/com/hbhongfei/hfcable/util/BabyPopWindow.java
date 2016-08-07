//package com.hbhongfei.hfcable.util;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.PopupWindow;
//import android.widget.PopupWindow.OnDismissListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hbhongfei.hfcable.R;
//import com.hbhongfei.hfcable.adapter.SkuAdapter;
//import com.hbhongfei.hfcable.pojo.Bean;
//import com.hbhongfei.hfcable.pojo.Constants;
//import com.hbhongfei.hfcable.pojo.Data;
//import com.hbhongfei.hfcable.pojo.SkuItme;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import cn.smssdk.gui.GroupListView;
//
//
///**
// * 宝贝详情界面的弹窗
// * @author http://yecaoly.taobao.com
// *
// */
//@SuppressLint("CommitPrefEdits")
//public class BabyPopWindow extends PopupWindow implements OnDismissListener,GroupListView.OnItemClickListener,OnClickListener {
////	private TextView pop_choice_16g,pop_choice_32g,pop_choice_16m,pop_choice_32m,pop_choice_black,pop_choice_white,pop_add,pop_reduce,pop_num,pop_ok;
////	private ImageView pop_del;
//
//
//	private OnDismissListener dismissListener;
//	private final int ADDORREDUCE=1;
//	private Context context;
//
//	private String str_color="";
//	private String str_type="";
//	// 数据接口
//	// ArrayList数组，listview原始数据
//	private ArrayList<String> mArrayList;
//
//
//	List<SkuItme> mList;// sku数据
//
//	List<Bean> mColorList;// 颜色列表
//	List<Bean> mSpecificationsList;// 尺码列表
//	GridView gvColor;// 颜色
//	GridView gvSpecifications;// 规格
//	SkuAdapter skuColorAdapter;// 颜色适配器
//	SkuAdapter skuSpecificationsAdapter;// 规格适配器
//	String color;//
//	String specifications;//规格
//	TextView tvSkuName;// 显示sku
//	TextView tvSkuStock;// 显示库存
//	ImageView pop_del;//关闭图片
//	Button btn_sure;//确定按钮
//	int stock = 0;// 库存
//
//	public BabyPopWindow(Context context,GridView gvColor,GridView gvSpecifications,TextView tvSkuName) {
//		this.context=context;
//		this.gvColor=gvColor;
//		this.gvSpecifications=gvSpecifications;
//		this.tvSkuName=tvSkuName;
//	}
//
//	/**
//	 * 通过颜色适配器设置颜色数据
//	 */
//	public String setColorData(){
//
//		skuColorAdapter = new SkuAdapter(mColorList, context);
//		gvColor.setAdapter(skuColorAdapter);
//		skuColorAdapter.setItemClickListener(new SkuAdapter.onItemClickListener() {
//			final String color1=new String();
//			@Override
//			public void onItemClick(Bean bean, int position) {
//				// TODO Auto-generated method stub
//				color = bean.getName();
//				switch (Integer.parseInt(bean.getStates())) {
//					case 0:
//						// 清空规格
//						mSpecificationsList=DataUtil.clearAdapterStates(mSpecificationsList);
//						skuSpecificationsAdapter.notifyDataSetChanged();
//						// 清空颜色
//						mColorList=DataUtil.clearAdapterStates(mColorList);
//						skuColorAdapter.notifyDataSetChanged();
//						color = "";
//						// 判断使用选中了规格
//						if (!TextUtils.isEmpty(specifications)) {
//							// 选中尺码，计算库存
//							stock =DataUtil.getSizeAllStock(mList,specifications);
////							if (stock > 0) {
////								tvSkuStock.setText("库存：" + stock + "");
////							}
//							tvSkuName.setText("请选择规格");
//							// 获取该尺码对应的颜色列表
//							List<String> list = DataUtil.getColorListBySize(mList,specifications);
//							if (list != null && list.size() > 0) {
//								// 更新颜色列表
//								mColorList = DataUtil.setSizeOrColorListStates(mColorList,list, color);
//								skuColorAdapter.notifyDataSetChanged();
//							}
//							mSpecificationsList=DataUtil.setAdapterStates(mSpecificationsList,specifications);
//							skuSpecificationsAdapter.notifyDataSetChanged();
//						} else {
//							// 所有库存
//							stock = DataUtil.getAllStock(mList);
//							tvSkuName.setText("请选择规格,颜色分类");
//						}
//						break;
//					case 1:
//						// 选中颜色
//						mColorList=DataUtil.updateAdapterStates(mColorList,"0", position);
//						skuColorAdapter.notifyDataSetChanged();
//						Toast.makeText(context,color,Toast.LENGTH_SHORT).show();
//						// 计算改颜色对应的尺码列表
//						List<String> list = DataUtil.getSizeListByColor(mList,color);
//						if (!TextUtils.isEmpty(specifications)) {
//							// 计算改颜色与尺码对应的库存
//							stock = DataUtil.getStockByColorAndSize(mList,color, specifications);
//							tvSkuName.setText("规格:" + color + " " + specifications);
//							if (list != null && list.size() > 0) {
//								// 更新尺码列表
//								mSpecificationsList = DataUtil.setSizeOrColorListStates(mSpecificationsList,list, specifications);
//								skuSpecificationsAdapter.notifyDataSetChanged();
//							}
//						} else {
//							// 根据颜色计算库存
//							stock = DataUtil.getColorAllStock(mList,color);
//							tvSkuName.setText("请选择规格");
//							if (list != null && list.size() > 0) {
//								// 更新尺码列表
//								mSpecificationsList = DataUtil.setSizeOrColorListStates(mSpecificationsList,list, "");
//								skuSpecificationsAdapter.notifyDataSetChanged();
//							}
//						}
//
//						break;
//					default:
//						break;
//				}
//			}
//		});
//		return color;
//	}
//
//	/**
//	 * 通过颜色适配器设置规格数据
//	 */
//	public String setSpecificationsData(){
//		skuSpecificationsAdapter = new SkuAdapter(mSpecificationsList, context);
//		gvSpecifications.setAdapter(skuSpecificationsAdapter);
//		skuSpecificationsAdapter.setItemClickListener(new SkuAdapter.onItemClickListener() {
//
//			@Override
//			public void onItemClick(Bean bean, int position) {
//				// TODO Auto-generated method stub
//				specifications = bean.getName();
//				switch (Integer.parseInt(bean.getStates())) {
//					case 0:
//						// 清空尺码
//						mSpecificationsList=DataUtil.clearAdapterStates(mSpecificationsList);
//						skuSpecificationsAdapter.notifyDataSetChanged();
//						// 清空颜色
//						mColorList=DataUtil.clearAdapterStates(mColorList);
//						skuColorAdapter.notifyDataSetChanged();
//						specifications = "";
//						if (!TextUtils.isEmpty(color)) {
//							// 计算改颜色对应的所有库存
//							stock = DataUtil.getColorAllStock(mList,color);
////							if (stock > 0) {
////								tvSkuStock.setText("库存：" + stock + "");
////							}
//							tvSkuName.setText("请选择尺码");
//							// 计算改颜色对应的尺码列表
//							List<String> list = DataUtil.getSizeListByColor(mList,color);
//							if (list != null && list.size() > 0) {
//								// 更新尺码列表
//								mSpecificationsList = DataUtil.setSizeOrColorListStates(mSpecificationsList,list, specifications);
//								skuSpecificationsAdapter.notifyDataSetChanged();
//							}
//							mColorList=DataUtil.setAdapterStates(mColorList,color);
//							skuColorAdapter.notifyDataSetChanged();
//						} else {
//							// 获取所有库存
//							stock = DataUtil.getAllStock(mList);
//							tvSkuName.setText("请选择尺码,颜色分类");
//						}
//						break;
//					case 1:
//						// 选中尺码
//						mSpecificationsList=DataUtil.updateAdapterStates(mSpecificationsList, "0", position);
//						skuSpecificationsAdapter.notifyDataSetChanged();
//						// 获取该尺码对应的颜色列表
//						List<String> list = DataUtil.getColorListBySize(mList,specifications);
//						if (!TextUtils.isEmpty(color)) {
//							// 计算改颜色与尺码对应的库存
//							stock = DataUtil.getStockByColorAndSize(mList,color, specifications);
//							tvSkuName.setText("规格:" + color + " " + specifications);
////							if (stock > 0) {
////								tvSkuStock.setText("库存：" + stock + "");
////							}
//							if (list != null && list.size() > 0) {
//								// 更新颜色列表
//								mColorList = DataUtil.setSizeOrColorListStates(mColorList,list, color);
//								skuColorAdapter.notifyDataSetChanged();
//							}
//						} else {
//							// 计算改尺码的所有库存
//							stock = DataUtil.getSizeAllStock(mList,specifications);
////							if (stock > 0) {
////								tvSkuStock.setText("库存：" + stock + "");
////							}
//							tvSkuName.setText("请选择颜色分类");
//							if (list != null && list.size() > 0) {
//								mColorList =  DataUtil.setSizeOrColorListStates(mColorList,list, "");
//								skuColorAdapter.notifyDataSetChanged();
//							}
//						}
//						break;
//					default:
//						break;
//				}
//			}
//		});
//		Toast.makeText(context,specifications+"////////////",Toast.LENGTH_SHORT).show();
//		return specifications;
//	}
//
//
//
//
//
//	public void setOnDismissListener(OnDismissListener listener){
//		this.dismissListener=listener;
//	}
//
//	// 当popWindow消失时响应
//	@Override
//	public void onDismiss() {
//
//	}
//
//
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//			case R.id.pop_del:
////				listener.onClickOKPop();
//				break;
//			case R.id.btn_sure:
////				listener.onClickOKPop();
//				if (color.equals("")) {
//					Toast.makeText(context,color,Toast.LENGTH_SHORT).show();
//					Toast.makeText(context, "亲，你还没有选择颜色哟~", Toast.LENGTH_SHORT).show();
//				}else if (specifications.equals("")) {
//					Toast.makeText(context, "亲，你还没有选择规格哟~",Toast.LENGTH_SHORT).show();
//				}else {
//					HashMap<String, Object> allHashMap=new HashMap<String,Object>();
//					allHashMap.put("color",color);
//					allHashMap.put("type",str_type);
////					allHashMap.put("num",pop_num.getText().toString());
//					allHashMap.put("id",Data.arrayList_cart_id+=1);
//
//					Data.arrayList_cart.add(allHashMap);
////					setSaveData();
//
//				}
//				break;
//		}
//	}
//	/**保存购物车的数据*/
//	private void setSaveData(){
//		SharedPreferences sp=context.getSharedPreferences("SAVE_CART", Context.MODE_PRIVATE);
//		Editor editor=sp.edit();
//		editor.putInt("ArrayCart_size", Data.arrayList_cart.size());
//		for (int i = 0; i < Data.arrayList_cart.size(); i++) {
//			editor.remove("ArrayCart_type_"+i);
//			editor.remove("ArrayCart_color_"+i);
//			editor.remove("ArrayCart_num_"+i);
//			editor.putString("ArrayCart_type_"+i, Data.arrayList_cart.get(i).get("type").toString());
//			editor.putString("ArrayCart_color_"+i, Data.arrayList_cart.get(i).get("color").toString());
//			editor.putString("ArrayCart_num_"+i, Data.arrayList_cart.get(i).get("num").toString());
//
//		}
//	}
//
//
//	/**
//	 * 模拟数据
//	 */
//	public void addData() {
//		mList = new ArrayList<SkuItme>();
//		//颜色规格列表
//		mColorList = new ArrayList<Bean>();
//		mSpecificationsList = new ArrayList<Bean>();
//		//所有的颜色和规格
//		String[] colorArr = Constants.colorArr;
//		String[] sizeArr = Constants.sizeArr;
//		int color = colorArr.length;
//		int size = sizeArr.length;
//		for (int i = 0; i < color; i++) {
//			Bean bean = new Bean();
//			bean.setName(colorArr[i]);
//			bean.setStates("1");
//			mColorList.add(bean);
//		}
//		for (int i = 0; i < size; i++) {
//			Bean bean = new Bean();
//			bean.setName(sizeArr[i]);
//			bean.setStates("1");
//			mSpecificationsList.add(bean);
//		}
//
//		String color0 = colorArr[0];
//		String size0 = sizeArr[0];
//		String color1 = colorArr[1];
//		String size1 = sizeArr[1];
//		String color2 = colorArr[2];
//		String size2 = sizeArr[2];
//		String color3 = colorArr[3];
//		String size3 = sizeArr[3];
//		String color4 = colorArr[4];
//		String size4 = sizeArr[4];
//		String size5 = sizeArr[5];
//		SkuItme item0 = new SkuItme();
//		item0.setId("1");
//		item0.setSkuColor(color0);
//		item0.setSkuSpecifications(size0);
//		item0.setSkuStock(10);
//		mList.add(item0);
//		SkuItme item1 = new SkuItme();
//		item1.setId("2");
//		item1.setSkuColor(color0);
//		item1.setSkuSpecifications(size1);
//		item1.setSkuStock(1);
//		mList.add(item1);
//		SkuItme item2 = new SkuItme();
//		item2.setId("3");
//		item2.setSkuColor(color1);
//		item2.setSkuSpecifications(size0);
//		item2.setSkuStock(12);
//		mList.add(item2);
//		SkuItme item3 = new SkuItme();
//		item3.setId("4");
//		item3.setSkuColor(color1);
//		item3.setSkuSpecifications(size2);
//		item3.setSkuStock(123);
//		mList.add(item3);
//		SkuItme item4 = new SkuItme();
//		item4.setId("5");
//		item4.setSkuColor(color1);
//		item4.setSkuSpecifications(size1);
//		item4.setSkuStock(53);
//		mList.add(item4);
//		SkuItme item5 = new SkuItme();
//		item5.setId("6");
//		item5.setSkuColor(color2);
//		item5.setSkuSpecifications(size1);
//		item5.setSkuStock(13);
//		mList.add(item5);
//		SkuItme item6 = new SkuItme();
//		item6.setId("7");
//		item6.setSkuColor(color0);
//		item6.setSkuSpecifications(size3);
//		item6.setSkuStock(18);
//		mList.add(item6);
//		SkuItme item7 = new SkuItme();
//		item7.setId("8");
//		item7.setSkuColor(color2);
//		item7.setSkuSpecifications(size3);
//		item7.setSkuStock(14);
//		mList.add(item7);
//		SkuItme item8 = new SkuItme();
//		item8.setId("9");
//		item8.setSkuColor(color1);
//		item8.setSkuSpecifications(size3);
//		item8.setSkuStock(22);
//		mList.add(item8);
//		SkuItme item9 = new SkuItme();
//		item9.setId("10");
//		item9.setSkuColor(color0);
//		item9.setSkuSpecifications(size4);
//		item9.setSkuStock(29);
//		mList.add(item9);
//		SkuItme item10 = new SkuItme();
//		item10.setId("11");
//		item10.setSkuColor(color2);
//		item10.setSkuSpecifications(size5);
//		item10.setSkuStock(64);
//		mList.add(item10);
//		SkuItme item11 = new SkuItme();
//		item11.setId("12");
//		item11.setSkuColor(color3);
//		item11.setSkuSpecifications(size2);
//		item11.setSkuStock(70);
//		mList.add(item11);
//		SkuItme item12 = new SkuItme();
//		item12.setId("13");
//		item12.setSkuColor(color4);
//		item12.setSkuSpecifications(size0);
//		item12.setSkuStock(80);
//		mList.add(item12);
//		SkuItme item13 = new SkuItme();
//		item13.setId("14");
//		item13.setSkuColor(color3);
//		item13.setSkuSpecifications(size4);
//		item13.setSkuStock(35);
//		mList.add(item13);
//		SkuItme item14 = new SkuItme();
//		item14.setId("15");
//		item14.setSkuColor(color4);
//		item14.setSkuSpecifications(size1);
//		item14.setSkuStock(62);
//		mList.add(item14);
//		SkuItme item15 = new SkuItme();
//		item15.setId("16");
//		item15.setSkuColor(color3);
//		item15.setSkuSpecifications(size5);
//		item15.setSkuStock(41);
//		mList.add(item15);
//		SkuItme item16 = new SkuItme();
//		item16.setId("17");
//		item16.setSkuColor(color1);
//		item16.setSkuSpecifications(size5);
//		item16.setSkuStock(39);
//		mList.add(item16);
//		SkuItme item17 = new SkuItme();
//		item17.setId("18");
//		item17.setSkuColor(color4);
//		item17.setSkuSpecifications(size5);
//		item17.setSkuStock(37);
//		mList.add(item17);
//		SkuItme item18 = new SkuItme();
//		item18.setId("19");
//		item18.setSkuColor(color4);
//		item18.setSkuSpecifications(size2);
//		item18.setSkuStock(44);
//		mList.add(item18);
//		SkuItme item19 = new SkuItme();
//		item19.setId("20");
//		item19.setSkuColor(color4);
//		item19.setSkuSpecifications(size3);
//		item19.setSkuStock(61);
//		mList.add(item19);
//	}
//
//	@Override
//	public void onItemClick(GroupListView groupListView, View view, int i, int i1) {
//
//	}
//}
//
//

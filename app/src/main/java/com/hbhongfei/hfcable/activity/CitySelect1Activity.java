package com.hbhongfei.hfcable.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.City;
import com.hbhongfei.hfcable.pojo.MyRegion;
import com.hbhongfei.hfcable.util.CityUtils;

import java.util.ArrayList;

/**
 * ����ѡ����
 * 
 */
public class CitySelect1Activity extends AppCompatActivity implements OnClickListener {

	private ListView lv_city;
	private ArrayList<MyRegion> regions;

	private CityAdapter adapter;
	private static int PROVINCE = 0x00;
	private static int CITY = 0x01;
	private static int DISTRICT = 0x02;
	private CityUtils util;

	private TextView[] tvs = new TextView[3];
	private int[] ids = { R.id.rb_province, R.id.rb_city, R.id.rb_district };//����ʡ����

	private City city;
	int last, current;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city2);//������ѡ��ҳ��
		ActionBar actionBar=getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		viewInit();

	}
	
	/*
	 * ��ʼ��
	 */
	private void viewInit() {

		city = new City();
		Intent in = getIntent();
		city = in.getParcelableExtra("city");
		
		
		int count=tvs.length;
		for (int i = 0; i < count; i++) {
			tvs[i] = (TextView) findViewById(ids[i]);//��Ӧ�ĳ���Id
			tvs[i].setOnClickListener(this);//ѡ���Ӧ���еĵ���¼�
		}

		if(city==null){
			city = new City();
			city.setProvince("");
			city.setCity("");
			city.setDistrict("");
		}else{
			if(city.getProvince()!=null&&!city.getProvince().equals("")){
				tvs[0].setText(city.getProvince());//ʡ
			}
			if(city.getCity()!=null&&!city.getCity().equals("")){
				tvs[1].setText(city.getCity());//��
			}
			if(city.getDistrict()!=null&&!city.getDistrict().equals("")){
				tvs[2].setText(city.getDistrict());//����
			}
		}

		
		findViewById(R.id.scrollview).setVisibility(View.GONE);
		
		
		util = new CityUtils(this, hand);
		util.initProvince();
//		tvs[current].setBackgroundColor(0xff999999);
		tvs[current].setBackgroundColor(Color.RED);
		lv_city = (ListView) findViewById(R.id.lv_city);

		regions = new ArrayList<MyRegion>();
		adapter = new CityAdapter(this);
		lv_city.setAdapter(adapter);
		
	}

	protected void onStart() {
		super.onStart();
		lv_city.setOnItemClickListener(onItemClickListener);

	};


	@SuppressLint("HandlerLeak")
	Handler hand = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case 1:

				regions = (ArrayList<MyRegion>) msg.obj;
				adapter.clear();
				adapter.addAll(regions);
				adapter.update();
				break;

			case 2:
				regions = (ArrayList<MyRegion>) msg.obj;
				adapter.clear();
				adapter.addAll(regions);
				adapter.update();
				break;

			case 3:
				regions = (ArrayList<MyRegion>) msg.obj;
				adapter.clear();
				adapter.addAll(regions);
				adapter.update();
				break;
			}
		};
	};

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

			if (current == PROVINCE) {
				String newProvince = regions.get(arg2).getName();
				if (!newProvince.equals(city.getProvince())) {
					city.setProvince(newProvince);
					tvs[0].setText(regions.get(arg2).getName());
					city.setRegionId(regions.get(arg2).getId());
					city.setProvinceCode(regions.get(arg2).getId());
					city.setCityCode("");
					city.setDistrictCode("");
					tvs[1].setText("市");
					tvs[2].setText("区 ");
				}
				
				current = 1;
				//���ʡ���б��е�ʡ�ݾͳ�ʼ�������б�
				util.initCities(city.getProvinceCode());
			} else if (current == CITY) {
				String newCity = regions.get(arg2).getName();
				if (!newCity.equals(city.getCity())) {
					city.setCity(newCity);
					tvs[1].setText(regions.get(arg2).getName());
					city.setRegionId(regions.get(arg2).getId());
					city.setCityCode(regions.get(arg2).getId());
					city.setDistrictCode("");
					tvs[2].setText("区 ");
				}
				
				util.initDistricts(city.getCityCode());
				current = 2;

			} else if (current == DISTRICT) {
				current = 2;
				city.setDistrictCode(regions.get(arg2).getId());
				city.setRegionId(regions.get(arg2).getId());
				city.setDistrict(regions.get(arg2).getName());
				tvs[2].setText(regions.get(arg2).getName());

			}
			tvs[last].setBackgroundColor(Color.WHITE);
			tvs[current].setBackgroundColor(Color.RED);
			last = current;
		}
	};

	//

	class CityAdapter extends ArrayAdapter<MyRegion> {

		LayoutInflater inflater;

		public CityAdapter(Context con) {
			super(con, 0);
			inflater = LayoutInflater.from(CitySelect1Activity.this);
		}

		@Override
		public View getView(int arg0, View v, ViewGroup arg2) {
			v = inflater.inflate(R.layout.city_item, null);
			TextView tv_city = (TextView) v.findViewById(R.id.tv_city);
			tv_city.setText(getItem(arg0).getName());
			return v;
		}

		public void update() {
			this.notifyDataSetChanged();
		}
	}

	@Override
	public void onClick(View v) {

		if (ids[0] == v.getId()) {
			current = 0;
			util.initProvince();
			tvs[last].setBackgroundColor(Color.WHITE);
			tvs[current].setBackgroundColor(Color.RED);
			last = current;
		} else if (ids[1] == v.getId()) {
			if (city.getProvinceCode() == null|| city.getProvinceCode().equals("")) {
				current = 0;
				Toast.makeText(CitySelect1Activity.this, "您还没有选择省份",
						Toast.LENGTH_SHORT).show();
				return;
			}
			util.initCities(city.getProvinceCode());
			current = 1;
			tvs[last].setBackgroundColor(Color.WHITE);
			tvs[current].setBackgroundColor(Color.RED);
			last = current;
		} else if (ids[2] == v.getId()) {
			if (city.getProvinceCode() == null
					|| city.getProvinceCode().equals("")) {
				Toast.makeText(CitySelect1Activity.this, "您还没有选择省份",
						Toast.LENGTH_SHORT).show();
				current = 0;
				util.initProvince();
				return;
			} else if (city.getCityCode() == null
					|| city.getCityCode().equals("")) {
				Toast.makeText(CitySelect1Activity.this, "您还没有选择城市",
						Toast.LENGTH_SHORT).show();
				current = 1;
				util.initCities(city.getProvince());
				return;
			}
			current = 2;
			util.initDistricts(city.getCityCode());
			tvs[last].setBackgroundColor(Color.WHITE);
			tvs[current].setBackgroundColor(Color.RED);
			last = current;
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_address, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.add_address_itme:
				Intent in = new Intent();
				in.putExtra("city", city);
				setResult(8,in);
				finish();
				break;
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}

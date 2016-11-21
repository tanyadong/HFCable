package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.adapter.NodeProgressAdapter;
import com.hbhongfei.hfcable.pojo.Logistics;
import com.hbhongfei.hfcable.pojo.LogisticsData;
import com.hbhongfei.hfcable.util.NodeProgressView;
import com.hbhongfei.hfcable.util.Url;

import java.util.ArrayList;

public class LogisticsDetailsActivity extends AppCompatActivity {
    private TextView tview_logistics_state,tview_logistics_company,tview_logistics_num;
    private ImageView img_logistics_proimg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logistics_details);
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(true);
        initview();
    }


    @Override
    protected void onResume() {
        super.onResume();
        setValues();

    }

    public void initview(){
        img_logistics_proimg= (ImageView) findViewById(R.id.img_logistics_proimg);
        tview_logistics_state= (TextView) findViewById(R.id.tview_logistics_state);
        tview_logistics_company= (TextView) findViewById(R.id.tview_logistics_company);
        tview_logistics_num= (TextView) findViewById(R.id.tview_logistics_num);
    }
   public void  setValues(){
       Intent intent=getIntent();
       Bundle bundle=intent.getExtras();
       final ArrayList<LogisticsData> logisticsDatas= (ArrayList<LogisticsData>)bundle.getSerializable("list");

       Logistics logistics= (Logistics) bundle.getSerializable("logistics");
       String image=bundle.getString("image");
       String state=bundle.getString("state");

       //物流状态
       String url= Url.url(image);
       Glide.with(this.getApplicationContext())
               .load(url)
               .placeholder(R.mipmap.background)
               .error(R.mipmap.loading_error)
               .diskCacheStrategy(DiskCacheStrategy.ALL)
               .into(img_logistics_proimg);
       if(state.endsWith("2")){
           tview_logistics_state.setText("快件在途中");
       }else if(state.endsWith("3")){
           tview_logistics_state.setText("快件已签收");
       }else if(state.endsWith("4")){
           tview_logistics_state.setText("问题件");
       }
       tview_logistics_company.setText(logistics.logisticsCompanyName);
       tview_logistics_num.setText(logistics.logisticsNumber);

       NodeProgressView nodeProgressView = (NodeProgressView) findViewById(R.id.npv_NodeProgressView);
       nodeProgressView.setNodeProgressAdapter(new NodeProgressAdapter() {

           @Override
           public int getCount() {
               return logisticsDatas.size();
           }

           @Override
           public ArrayList<LogisticsData> getData() {
               return logisticsDatas;
           }
       });
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

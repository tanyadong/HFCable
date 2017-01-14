package com.hbhongfei.hfcable.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.Legend;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.MarketInfo;
import com.hbhongfei.hfcable.util.Dialog;
import com.hbhongfei.hfcable.util.Error;
import com.hbhongfei.hfcable.util.IErrorOnclick;
import com.hbhongfei.hfcable.util.MyMarkerView;
import com.hbhongfei.hfcable.util.MySingleton;
import com.hbhongfei.hfcable.util.NetUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MarketChartActivity extends AppCompatActivity implements IErrorOnclick {
    private String url;
    private String area, produceName;
    private List<String> averagePrice_list;
    private List<String> data_list;
    private MarketInfo marketInfo;
    private LineChart mChart;
    ArrayList<String> xVals;
    ArrayList<Entry> yVals;
    private Dialog dialog;
    private LinearLayout noInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_market_chart);
        //返回键
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        //初始化组件
        initView();
        //初始化数据
        initvalues();

    }

    private void initView() {
        dialog = new Dialog(this);
        SplashActivity.ID=3;
        mChart = (LineChart) findViewById(R.id.chart1);
        data_list = new ArrayList<>();
        averagePrice_list = new ArrayList<>();
        xVals = new ArrayList<String>();
        yVals = new ArrayList<>();
        noInternet = (LinearLayout) findViewById(R.id.no_internet_market_chart);
    }


    /**
     * 初始化数据
     */
    public void initvalues() {
        marketInfo = (MarketInfo) getIntent().getSerializableExtra("marketInfo");
        url = marketInfo.getTrend();
        area = marketInfo.getArea();
        produceName = marketInfo.getProductName();
        dialog.showDialog("正在加载中。。。");
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        parseMonth(s);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dialog.cancle();
                        MySingleton mySingleton = new MySingleton(MarketChartActivity.this);
                        if (mySingleton.getCacheString(url)!=null){
                            //加载缓存
                            Toast.makeText(MarketChartActivity.this,"网络连接失败，请检查您的网络",Toast.LENGTH_SHORT).show();
                            noInternet.setVisibility(View.GONE);
                            parseMonth(mySingleton.getCacheString(url).toString());
                        }else{
                            if (volleyError instanceof NoConnectionError){
                                Error.toSetting(noInternet,R.mipmap.internet_no,"没有网络加载数据失败","点击设置",MarketChartActivity.this);
                            }else if(volleyError instanceof NetworkError||volleyError instanceof ServerError||volleyError instanceof TimeoutError){
                                Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "服务器出错啦", new IErrorOnclick() {
                                    @Override
                                    public void errorClick() {
                                    }
                                });
                            }else{
                                Error.toSetting(noInternet, R.mipmap.internet_no, "不好啦", "出错啦", new IErrorOnclick() {
                                    @Override
                                    public void errorClick() {

                                    }
                                });
                            }
                        }

                    }
                });
                MySingleton.getInstance(MarketChartActivity.this).addToRequestQueue(request);
            }
        }).start();

    }
    /**
     * 解析html,获取一个月的记录
     *
     * @param html
     */
    protected void parseMonth(String html) {
        Document doc = Jsoup.parse(html);
        //Elements
        Element table = doc.getElementsByTag("table").first();
        Elements lists = table.getElementsByTag("tr");
        int count=lists.size();
        for (int i = 1; i < count; i++) {
            Element item = lists.get(i);
            Elements els = item.getElementsByTag("td");
            averagePrice_list.add(els.get(1).text());
            data_list.add(els.get(0).text());
        }
        //显示图表
        showChart();
    }

    /**
     * 曲线图
     */
    private void showChart() {
        // 设置在Y轴上是否是从0开始显示
        mChart.setStartAtZero(false);
        //是否在Y轴显示数据，就是曲线上的数据
        mChart.setDrawYValues(true);
        //设置网格
        mChart.setDrawBorder(true);
        mChart.setBorderPositions(new BarLineChartBase.BorderPosition[]{
                BarLineChartBase.BorderPosition.BOTTOM});
        //在chart上的右下角加描述
        mChart.setDescription("曲线图");
        mChart.setNoDataTextDescription("没有数据");
        //设置透明度
        mChart.setAlpha(0.8f);
        //设置网格底下的那条线的颜色
        mChart.setBorderColor(Color.rgb(213, 216, 214));
        //设置Y轴前后倒置
        mChart.setInvertYAxisEnabled(false);
        //设置高亮显示
        mChart.setHighlightEnabled(true);
        //设置是否可以触摸，如为false，则不能拖动，缩放等
        mChart.setTouchEnabled(true);
        //设置是否可以拖拽，缩放
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        //设置是否能扩大扩小
        mChart.setPinchZoom(true);

        // 设置背景颜色
        // mChart.setBackgroundColor(Color.GRAY);
        //设置点击chart图对应的数据弹出标注
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        // define an offset to change the original position of the marker
        // (optional)
        mv.setOffsets(-mv.getMeasuredWidth() / 2, -mv.getMeasuredHeight());
        // set the marker to the chart
        mChart.setMarkerView(mv);
        // highlighted Entry)
        mChart.setHighlightIndicatorEnabled(true);
        //设置字体格式，如正楷
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "OpenSans-Regular.ttf");
        mChart.setValueTypeface(tf);

        XLabels xl = mChart.getXLabels();
        xl.setAvoidFirstLastClipping(true);//如果设置为true，则在绘制时会避免“剪掉”在x轴上的图表或屏幕边缘的第一个和最后一个坐标轴标签项。
        xl.setAdjustXLabels(true);
        xl.setPosition(XLabels.XLabelPosition.BOTTOM); // 设置X轴的数据在底部显示
        xl.setTypeface(tf); // 设置字体
        xl.setTextSize(10f); // 设置字体大小
        xl.setSpaceBetweenLabels(4); // 设置数据之间的间距

        YLabels yl = mChart.getYLabels();
        yl.setTypeface(tf); // 设置字体
        yl.setTextSize(10f); // s设置字体大小
//        yl.setLabelCount(5); // 设置Y轴最多显示的数据个数

        // 加载数据
        setData();
//        mChart.setData(lineData);
        //从X轴进入的动画
        mChart.animateX(1000);
        mChart.animateY(1000);   //从Y轴进入的动画
        mChart.animateXY(1000, 1000);    //从XY轴一起进入的动画

        //设置最小的缩放
        mChart.setScaleMinima(0.0f, 1f);
        //设置视口
//         mChart.centerViewPort(10, 50);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();  //下边显示的文字
        l.setForm(Legend.LegendForm.LINE);  //设置图最下面显示的类型
        l.setTypeface(tf);
        l.setTextSize(15);
        l.setTextColor(getResources().getColor(R.color.colorRed));
        l.setFormSize(30f); // set the size of the legend forms/shapes
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        // 刷新图表
        mChart.invalidate();
        dialog.cancle();
    }
    /**
     * 设置数据

     * @return
     */

    private void setData() {
        int data_count=data_list.size();
        for (int i = 0; i < data_count; i++) {
            xVals.add(data_list.get(i));
        }
        int averagePrice_count=averagePrice_list.size();
        for (int i = 0; i < averagePrice_count; i++) {
            boolean isNum = averagePrice_list.get(i).matches("[0-9]+");
            if(isNum){
                yVals.add(new Entry(Float.parseFloat(averagePrice_list.get(i)), i));
            }
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, area + " " + produceName);
        set1.setDrawCubic(true);  //设置曲线为圆滑的线

        set1.setCubicIntensity(0.2f);
        set1.setDrawFilled(false);  //设置包括的范围区域填充颜色
        set1.setDrawCircles(true);  //设置有圆点
        set1.setLineWidth(1f);    //设置线的宽度
        set1.setCircleSize(5f);   //设置小圆的大小
        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(Color.rgb(104, 241, 175));    //设置曲线的颜色
        set1.setColor(getResources().getColor(R.color.colorRed));
        // create a data object with the datasets
        LineData data = new LineData(xVals, set1);
        // set data
        mChart.setData(data);
    }


    @Override
    public void errorClick() {
        NetUtils.openSetting(this);
    }

}

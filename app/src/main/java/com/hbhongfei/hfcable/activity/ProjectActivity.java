package com.hbhongfei.hfcable.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.Project;
import com.hbhongfei.hfcable.util.Url;

public class ProjectActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private TextView project_title,project_content;
    private ImageView project_img;
    Intent intent;
    private AppBarLayout projectInfoABL;
    private LinearLayout projectInfoLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        toolBar();
        //初始化组件
        initview();
        setOnclick();
        initValues();
    }
    /**
     * 展示toolbar
     */
    private void toolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_project_info);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(0);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//设置状态栏背景为透明
        //使用CollapsingToolbarLayout必须把title设置到CollapsingToolbarLayout上，设置到Toolbar上则不会显示
        /*CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_project_info_detail);
        mCollapsingToolbarLayout.setTitle("项目详情");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色*/
    }


    /**
     * 设置点击事件
     */
    private void setOnclick(){
        projectInfoABL.addOnOffsetChangedListener(this);
    }

    /**
     * 初始化组件
     */
    private void initview() {
        project_title= (TextView) findViewById(R.id.project_title_tv);
        project_content= (TextView) findViewById(R.id.project_content_tv);
        project_img= (ImageView) findViewById(R.id.project_img);
        projectInfoABL = (AppBarLayout) findViewById(R.id.abl_project_info);
        projectInfoLL = (LinearLayout) findViewById(R.id.project_info_LL);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {//展开状态
            projectInfoLL.setBackgroundColor(Color.parseColor("#ffffff"));
            project_title.setTextColor(Color.parseColor("#000000"));
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {////折叠状态
            projectInfoLL.setBackgroundColor(Color.parseColor("#ff0000"));
            project_title.setTextColor(Color.parseColor("#ffffff"));
        } else { //中间状态
        }
    }

    /**
     *加载数据
     */
    private void initValues() {
        Project project= (Project) getIntent().getSerializableExtra("project");
        project_title.setText(project.getProjectName());
        project_content.setText(project.getIntroduce());
        String imgurl=project.getProjectImg();
        String url= Url.url(imgurl);
        Glide.with(this)
                .load(url)
                .placeholder(R.mipmap.background)
                .error(R.mipmap.loading_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into( project_img );

    }
}

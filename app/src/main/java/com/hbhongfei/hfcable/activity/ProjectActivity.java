package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.Project;
import com.hbhongfei.hfcable.util.Url;

public class ProjectActivity extends AppCompatActivity {
    private TextView project_title,project_content;
    private ImageView project_img;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        setActionBar();
        //初始化组件
        initview();
        initValues();
    }
    private void setActionBar(){
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
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

    /**
     * 初始化组件
     */
    private void initview() {
        project_title= (TextView) findViewById(R.id.project_title_tv);
        project_content= (TextView) findViewById(R.id.project_content_tv);
        project_img= (ImageView) findViewById(R.id.project_img);
    }
}

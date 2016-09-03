package com.hbhongfei.hfcable.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;
import com.hbhongfei.hfcable.pojo.Project;
import com.hbhongfei.hfcable.util.AsyncBitmapLoader;
import com.hbhongfei.hfcable.util.Url;

public class ProjectActivity extends AppCompatActivity {
    private TextView project_title,project_content;
    private ImageView project_img;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        //初始化组件
        initview();
        initValues();
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
        project_img.setTag(url);

        AsyncBitmapLoader asyncBitmapLoader=new AsyncBitmapLoader();
        asyncBitmapLoader.loadImage(this,project_img,url);
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

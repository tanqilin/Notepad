package com.admin.notepad.index;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.notepad.R;
import com.bumptech.glide.Glide;

/*
* 【2017,10,03 14:56】创建于637寝室
* 查看日志详细信息
* */
public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String LOG_ID = "log_id";
    public static final String LOG_NAME = "log_name";
    public static final String LOG_IMAGE = "log_image";

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView titleImage;
    private Toolbar toolbar;
    private ImageView comeBack;
    private TextView logTitle;
    private TextView logContent;
    private TextView LogCreateTime;

    private int logId = 0;
    private int logImage = 0;
    private String logName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsion_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleImage = (ImageView) findViewById(R.id.card_image_view);
        logTitle = (TextView) findViewById(R.id.log_title);
        logContent = (TextView) findViewById(R.id.log_content);
        LogCreateTime = (TextView) findViewById(R.id.log_create_time);

        // 设置ActionBar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 获取上一个页面传过来的值
        Intent intent = getIntent();
        initFindAndShowLog(intent);
    }

    // 从数据库中查询并显示日志
    private void initFindAndShowLog(Intent intent){
        int logId = intent.getIntExtra(LOG_ID,0);
        String logName = intent.getStringExtra(LOG_NAME);
        int logImage = intent.getIntExtra(LOG_IMAGE,0);

        // 设置页面标题
        collapsingToolbar.setTitle(logName);
        // 加载显示标题栏图片
        Glide.with(this).load(logImage).into(titleImage);

        // 显示日子相关信息
        logTitle.setText(logName);
        LogCreateTime.setText("2017-10-3 16:20");
        logContent.setText(generateCardContent(logName));
    }

    // 构建显示内容,用于测试
    private String generateCardContent(String cardName){
        StringBuilder cardContent = new StringBuilder();
        for(int i=0 ; i < 200 ;i++){
            cardContent.append(cardName);
        }
        return cardContent.toString();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.come_back:finish();break;
            default:break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 点击返回，返回上一页
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.admin.notepad.index;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.notepad.R;
import com.admin.notepad.adapter.RecyclerCardAdapter;
import com.admin.notepad.db.UserLog;
import com.admin.notepad.dbService.LogService;
import com.admin.notepad.model.CardModel;

import java.util.ArrayList;
import java.util.List;

public class LogManageActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView appTitle;
    private ImageView comeBack;

    private SwipeRefreshLayout swipe_refresh;   // 下拉刷新
    private RecyclerView recyclerView;
    private List<CardModel> cardList = new ArrayList<>();   // 数据列表
    private RecyclerCardAdapter cardAdapter;    // 适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_manage);

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        appTitle = (TextView) findViewById(R.id.app_title);
        comeBack = (ImageView) findViewById(R.id.come_back);
        appTitle.setText("日志列表");
        comeBack.setOnClickListener(this);

        // 渲染List
        initRecyclerViewCard();
        // 设置下拉刷新
        swipe_refresh.setColorSchemeResources(R.color.colorAccent);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initRecyclerViewCard();
                swipe_refresh.setRefreshing(false); // 关闭下拉刷新
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.come_back:finish();break;
            default:break;
        }
    }

    // 初始化数据用于渲染CardView
    private void initRecyclerViewCard(){
        recyclerView = (RecyclerView) findViewById(R.id.log_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this,1); // 布局管理
        recyclerView.setLayoutManager(layoutManager);

        List<UserLog> logs = LogService.GetAllUserLog();
        cardList.clear();
        for (UserLog log : logs){
            cardList.add(new CardModel(log));
        }

        // Toast.makeText(IndexActivity.this,"加载中...",Toast.LENGTH_SHORT).show();
        // 把数据放入适配器
        cardAdapter = new RecyclerCardAdapter(cardList,R.layout.activity_recycler_item_row);
        recyclerView.setAdapter(cardAdapter);
    }

    // 启动活动
    public static void actionStart(Context context){
        Intent intent = new Intent(context,LogManageActivity.class);
        context.startActivity(intent);
    }
}

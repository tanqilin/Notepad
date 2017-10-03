package com.admin.notepad.index;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.admin.notepad.MainActivity;
import com.admin.notepad.R;
import com.admin.notepad.adapter.RecyclerCardAdapter;
import com.admin.notepad.model.CardModel;
import com.admin.notepad.util.FileUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IndexActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //记录用户首次点击返回键的时间,用于实现双击退出应用功能
    private long firstTime = 0;
    private SwipeRefreshLayout swipe_refresh;   // 下拉刷新
    private NavigationView navigationView;
    private ImageView drawerLayoutBack;         // 抽屉背景图
    private ImageView drawerLayoutHead;         // 抽屉头像
    private RecyclerView recyclerView;          // 超级ListView

    private CardModel[] cards = {new CardModel("进击的巨人",R.drawable.saber),
            new CardModel("未闻花名",R.drawable.junji),
            new CardModel("进击的巨人",R.drawable.msn),
            new CardModel("进击的巨人",R.drawable.junji)};

    private List<CardModel> cardList = new ArrayList<>();   // 数据列表
    private RecyclerCardAdapter cardAdapter;    // 适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // 设置标题
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("我的小日记");

        // 悬浮图标
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateActivity.actionStart(IndexActivity.this);
            }
        });

        // 设置抽屉属性
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initDrawerUserSetting();
        initActivityStartTitle(drawer);

        // 渲染 RecyclerView List
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2); // 布局管理
        recyclerView.setLayoutManager(layoutManager);
        initRecyclerViewCard();

        // 设置下拉刷新
        swipe_refresh.setColorSchemeResources(R.color.colorAccent);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 响应下拉动作
                initDrawerUserSetting();
                initRecyclerViewCard();
                swipe_refresh.setRefreshing(false); // 关闭下拉刷新
            }
        });
    }

    // 处理抽屉控件中的点击事件
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            ThisMeActivity.actionStart(IndexActivity.this);
        } else if (id == R.id.nav_gallery) {
            LogManageActivity.actionStart(IndexActivity.this);
        } else if (id == R.id.nav_weather) {
            WeatherActivity.actionStart(IndexActivity.this);
        } else if (id == R.id.nav_manage) {
            GroupActivity.actionStart(IndexActivity.this);
        } else if (id == R.id.nav_share) {
            PrivacyActivity.actionStart(IndexActivity.this);
        } else if (id == R.id.nav_setting) {
            SettingActivity.actionStart(IndexActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // 初始化抽屉菜单中用户的设置信息
    private void initDrawerUserSetting(){
        // 抽屉中的控件不能直接获取到
        View headerView = navigationView.getHeaderView(0);
        drawerLayoutBack = (ImageView) headerView.findViewById(R.id.index_background);
        drawerLayoutHead = (ImageView) headerView.findViewById(R.id.icon_image);

        // 从缓存中加载用户设置好的图片
        SharedPreferences pref = getSharedPreferences("Setting",MODE_PRIVATE);
        String background = pref.getString("background", null);
        String headimage = pref.getString("head", null);
        if(background != null)
            Glide.with(this).load(background).into(drawerLayoutBack);
        if(headimage != null)
            Glide.with(this).load(headimage).into(drawerLayoutHead);
    }

    // 设置状态栏为透明
    private void initActivityStartTitle(DrawerLayout drawer){
        // 使用代码根据按android版本设置导航栏为透明色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //将侧边栏顶部延伸至status bar
                drawer.setFitsSystemWindows(true);
                //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                drawer.setClipToPadding(false);
            }
        }
    }

    // 初始化数据用于渲染CardView
    private void initRecyclerViewCard(){
        cardList.clear();
        for (int i = 0;i < 25; i++){
            Random random = new Random();
            int index = random.nextInt(cards.length);
            cardList.add(cards[index]);
        }

        // 把数据放入适配器
        cardAdapter = new RecyclerCardAdapter(cardList);
        recyclerView.setAdapter(cardAdapter);
    }

    // 启动活动
    public static void actionStart(Context context){
        Intent intent = new Intent(context,IndexActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(IndexActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }
}

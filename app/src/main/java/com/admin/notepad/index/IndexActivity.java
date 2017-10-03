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
import com.admin.notepad.util.FileUtil;

public class IndexActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //记录用户首次点击返回键的时间,用于实现双击退出应用功能
    private long firstTime = 0;
    private SwipeRefreshLayout swipe_refresh;   // 下拉刷新
    private NavigationView navigationView;
    private ImageView drawerLayoutBack;
    private ImageView drawerLayoutHead;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initDrawerUserSetting();

        // 设置下拉刷新
        swipe_refresh.setColorSchemeResources(R.color.colorAccent);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 响应下拉动作
                initDrawerUserSetting();
                swipe_refresh.setRefreshing(false); // 关闭下拉刷新
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_weather) {
            WeatherActivity.actionStart(IndexActivity.this);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

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
            drawerLayoutBack.setImageURI(Uri.parse(background));
        if(headimage != null)
            drawerLayoutHead.setImageURI(Uri.parse(headimage));
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

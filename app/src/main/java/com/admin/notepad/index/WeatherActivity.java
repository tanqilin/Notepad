package com.admin.notepad.index;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.notepad.R;
import com.admin.notepad.gson.Bing;
import com.admin.notepad.gson.Forecast;
import com.admin.notepad.gson.Weather;
import com.admin.notepad.service.AutoUpdateService;
import com.admin.notepad.util.HttpUtil;
import com.admin.notepad.util.Utility;
import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpdateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;           // 空气质量指标
    private TextView pm25Text;          // PM指标
    private TextView comfortText;       // 舒适度
    private TextView carwashText;       // 洗车
    private TextView sportText;         // 运动

    private ImageView bingImage;        // 必应每日一图
    public SwipeRefreshLayout swipeRefresh; // 下拉刷新
    public DrawerLayout drawerLayout;
    private Button comeBack;
    public String weatherId;    // 当前城市，切换时更改此值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        // 初始化控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout_view);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        comfortText = (TextView) findViewById(R.id.comfort_text);
        carwashText = (TextView) findViewById(R.id.car_wash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingImage = (ImageView) findViewById(R.id.bing_pic_img);    // 每日一图

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        comeBack = (Button) findViewById(R.id.come_back);
        swipeRefresh.setColorSchemeResources(R.color.colorAccent);  // 设置下拉bar颜色
        comeBack.setOnClickListener(this);

        // 下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });

        // 获取缓存信息
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefer.getString("weather",null);
        if(weatherString != null && getIntent().getStringExtra("weather_id") == null){
            Weather weather = Utility.handleWeatherResponse(weatherString);
            weatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            weatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.VISIBLE);
            requestWeather(weatherId);
        }

        // 从缓存中获取必应每日一图
        String bingPic = prefer.getString("bing_pic",null);
        if(bingPic != null){
            Glide.with(this).load(bingPic).into(bingImage);
        }else{
            loadBingPic();
        }
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.come_back:finish(); break;
            default:break;
        }
    }

    /*
    *  加载必应每日一图
    * */
    public void loadBingPic(){
        String requestBingPic = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                // 把加载的必应图片路径存入缓存
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 解析必应JSON字符
                        Bing bing = Utility.handleBingResponse(responseText);
                        String url = "http://cn.bing.com"+bing.url;
                        // 把加载的必应图片路径存入缓存
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        editor.putString("bing_pic",url);
                        editor.apply();
                        // 加载图片
                        Glide.with(WeatherActivity.this).load(url).into(bingImage);
                    }
                });
            }
        });
    }

     /*
    *  根据区域天气ID获取天气信息
    * */
    public void requestWeather(final String weatherId){
        // 和风天气API
        String weatherURL = "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=c6d2e83e000f495284f69e9eb7f48c59";
        HttpUtil.sendOkHttpRequest(weatherURL, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else{
                            Toast.makeText(WeatherActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        }
                        // 关闭下拉刷新
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /*
    *  显示获取到的天气信息
    * */
    public void showWeatherInfo(Weather weather){
        weatherId = weather.basic.weatherId;
        String cityName = weather.basic.cityName;   //获取城市名字
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature + "℃";  // 温度
        String weatherInfo = weather.now.more.info;

        // 基本天气情况
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);

        // 显示未来几天天气情况
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList){
            View view  = LayoutInflater.from(this).inflate(R.layout.activity_weather_forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max +"℃");
            minText.setText(forecast.temperature.min +"℃");
            forecastLayout.addView(view);
        }

        // 显示空气质量
        if(weather != null){
            try{
                aqiText.setText(weather.aqi.city.aqi);
                pm25Text.setText(weather.aqi.city.pm25);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // 系统建议
        String comfort = "舒适度：" + weather.suggestion.comfort.info;
        String carWash = "洗车指数：" + weather.suggestion.carWash.info;
        String sport   = "运动建议：" + weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carwashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);

        // 当成功更新天气后启动服务(服务会在8小时后启动)
        if(weather != null && "ok".equals(weather.status)){
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else{
            Toast.makeText(this, "天气更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    // 启动活动
    public static void actionStart(Context context){
        Intent intent = new Intent(context,WeatherActivity.class);
        context.startActivity(intent);
    }
}

package com.admin.notepad.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;

import com.admin.notepad.R;

import com.admin.notepad.gson.Bing;
import com.admin.notepad.gson.Weather;
import com.admin.notepad.index.WeatherActivity;
import com.admin.notepad.util.HttpUtil;
import com.admin.notepad.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/*
*  后台服务，自动更新当前区域的天气情况
* */
public class AutoUpdateService extends Service {

    public AutoUpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
      return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        updateWeather();    // 更新天气
        updateBingPic();    // 更新每日一图

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int onHour = 1*60*60*1000;  // 8小时更新一次(单位毫秒)
        long triggerAtTime = SystemClock.elapsedRealtime() + onHour;
        Intent i = new Intent(this,AutoUpdateService.class);
        PendingIntent pi = PendingIntent.getService(this,0 ,i ,0);
        alarmManager.cancel(pi);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);

        return super.onStartCommand(intent,flags,startId);
    }

    /*
    *  自动更新天气
    * */
    private void updateWeather(){
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefer.getString("weather",null);
        if(weatherString != null){
            // 从缓存中获取地区ID
            Weather weather = Utility.handleWeatherResponse(weatherString);
            String weatherId = weather.basic.weatherId;
            // 和风天气API
            String weatherURL = "https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=c6d2e83e000f495284f69e9eb7f48c59";
            HttpUtil.sendOkHttpRequest(weatherURL, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    Weather weather = Utility.handleWeatherResponse(responseText);
                    // 把更新后的天气存入缓存
                    if (weather != null && "ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weather",responseText);
                        editor.apply();
                    }
                    // 在标题栏中显示
                    showNotification(weather);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /*
    *  更新每日一图
    * */
    private void updateBingPic(){
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
                Bing bing = Utility.handleBingResponse(responseText);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic","http://cn.bing.com" + bing.url);
                editor.apply();
            }
        });
    }


    // 在前台显示服务通知
    public void showNotification(Weather weather){
        // 点击通知跳转到详细页面
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("weather_id",weather.basic.weatherId);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);

        // 在通知栏中显示更新后的天气
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(weather.basic.cityName +"   " + weather.now.temperature + "℃")    // 标题
                .setContentText(weather.suggestion.comfort.info)        // 内容
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.app)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.app))
                .setContentIntent(pi) // 给通知设置点击响应
                .setAutoCancel(true) // 点击后自动关闭通知
                .build();
        startForeground(1, notification);
    }
}

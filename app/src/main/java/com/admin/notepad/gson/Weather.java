package com.admin.notepad.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Admin on 2017/6/24.
 *
 *  和风天气返回的总的JSON数据解析
 */

public class Weather {
    public String status;   // 请求状态

    public Basic basic;     // 基本天气信息

    public AQI aqi;         // 城市空气质量

    public Now now;         // 当前天气情况，温湿度和风力情况

    public Suggestion suggestion;   // 根据天气情况给的建议

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList; // 未来几天天气情况
}

package com.admin.notepad.gson;

/**
 * Created by Admin on 2017/6/24.
 *  城市空气质量
 */

public class AQI {
    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}

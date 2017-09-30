package com.admin.notepad.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/6/24.
 *  用于解析和风天气API返回的JSON信息中的 basic 对象信息
 *  基本天气
 */

public class Basic {

    // 给字段确定别名
    @SerializedName("city")
    public String cityName;     // 城市

    @SerializedName("cnty")
    public String countryName;   // 国家

    @SerializedName("id")
    public String weatherId;    // 天气查看ID

    public Update update;       // 更新时间

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }

}

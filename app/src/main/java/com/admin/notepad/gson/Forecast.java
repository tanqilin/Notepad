package com.admin.notepad.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/6/24.
 *  未来几天天气情况
 */

public class Forecast {

    public String date;     // 未来时间

    @SerializedName("tmp")
    public Temperature temperature;  // 温度

    @SerializedName("cond")
    public More more;

    public class Temperature{
        public String max;      // 最高温度
        public String min;      // 最底温度
    }

    public class More{
        @SerializedName("txt_d")
        public String info;     // 雨水情况
    }
}

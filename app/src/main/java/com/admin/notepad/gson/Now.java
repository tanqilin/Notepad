package com.admin.notepad.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/6/24.
 *
 *  当前天气情况，温湿度和风力情况
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;  // 温度

    @SerializedName("cond")
    public More more;

    public class More{
        @SerializedName("txt")
        public String info;     // 天气情况
    }
}

package com.admin.notepad.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/6/24.
 *
 *  根据天气情况给的建议
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;     // 舒适度

    @SerializedName("cw")
    public CarWash carWash;     // 洗车建议

    public Sport sport;         // 运动

    public class Comfort{
        @SerializedName("txt")
        public String info;     // 详细信息
    }

    public class CarWash{
        @SerializedName("txt")
        public String info;     // 详细信息
    }

    public class Sport{
        @SerializedName("txt")
        public String info;     // 详细信息
    }
}

package com.admin.notepad.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/9/29.
 *  市区下边的县
 */

public class County extends DataSupport {
    private int id;
    private int cityId;
    private String countyName;
    private String weatherId;

    /* 获取值 */
    public int getId() {
        return id;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    /* 设置字段值 */
    public void setId(int id) {
        this.id = id;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}

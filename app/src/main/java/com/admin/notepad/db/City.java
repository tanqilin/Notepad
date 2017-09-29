package com.admin.notepad.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/9/29.
 *  省份下的市区表
 */

public class City extends DataSupport {
    private int id;
    private int cityCode;
    private int provinceId;
    private String cityName;

    /* 获取值 */
    public int getId() {
        return id;
    }

    public int getCityCode() {
        return cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public String getCityName() {
        return cityName;
    }

    /* 设置字段值 */
    public void setId(int id) {
        this.id = id;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}

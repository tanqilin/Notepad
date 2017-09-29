package com.admin.notepad.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/9/29.
 * 存放省份信息
 */

public class Province extends DataSupport{
    private int id;
    private int provinceCode;
    private String provinceName;

    /* 获取值 */
    public int getId() {
        return id;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    /* 设置字段值 */
    public void setId(int id) {
        this.id = id;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}

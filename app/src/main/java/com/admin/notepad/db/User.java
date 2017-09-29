package com.admin.notepad.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/9/29.
 * App 用户信息表
 */

public class User extends DataSupport {
    private int id;
    private String picture;
    private String name;
    private String phoneNumber;
    private String AppKey;

    /* 获取值 */
    public int getId() {
        return id;
    }

    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAppKey() {
        return AppKey;
    }

    /* 设置字段值 */
    public void setId(int id) {
        this.id = id;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAppKey(String appKey) {
        AppKey = appKey;
    }
}

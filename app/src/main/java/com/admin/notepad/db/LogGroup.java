package com.admin.notepad.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/9/29.
 * 用户日志分组表
 */

public class LogGroup extends DataSupport {
    private int id;
    private int userId;
    private String groupName;
    private boolean isSafe;         // 是否设置安全密码
    private String password;        // 密码
    private boolean isSave;         // 是佛保存
    private boolean isDelete;       // 是否删除
    private String updateTime;      // 更新时间
    private String createTime;      // 创建时间

    /* 获取值 */
    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean isSafe() {
        return isSafe;
    }

    public String getPassword() {
        return password;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public boolean isSave() {
        return isSave;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    /* 设置值 */

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSave(boolean save) {
        isSave = save;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}

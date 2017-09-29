package com.admin.notepad.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Admin on 2017/9/29.
 * 日志上传图片表
 */

public class LogImage extends DataSupport {
    private int id;
    private int logId;
    private int userId;
    private String imagePath;       // 图片本地路径
    private String webPath;         // 图片web路径
    private String isSave;          // 是否保存
    private String isDelete;        // 是否删除
    private String createTime;      // 创建时间
    private String updateTime;      // 更新时间

    /* 获取值 */
    public int getId() {
        return id;
    }

    public int getLogId() {
        return logId;
    }

    public int getUserId() {
        return userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getWebPath() {
        return webPath;
    }

    public String getIsSave() {
        return isSave;
    }

    public String getIsDelete() {
        return isDelete;
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

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setWebPath(String webPath) {
        this.webPath = webPath;
    }

    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}

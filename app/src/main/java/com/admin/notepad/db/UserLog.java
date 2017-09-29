package com.admin.notepad.db;

import org.litepal.crud.DataSupport;


/**
 * Created by Admin on 2017/9/29.
 * 用户写的日志存放表
 */

public class UserLog extends DataSupport {
    private int id;
    private int groupId;        // 所属分组ID
    private String title;       // 标题
    private String userId;      // 创建人
    private String content;     // 内容
    private String isSave;      // 是否保存
    private String isDelete;    // 是否删除
    private String createTime;  // 创建时间
    private String updateTime;  // 更新时间

    /* 获取值 */
    public int getId() {
        return id;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
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

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContent(String content) {
        this.content = content;
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

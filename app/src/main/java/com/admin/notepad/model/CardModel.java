package com.admin.notepad.model;

import com.admin.notepad.db.UserLog;

/**
 * Created by Admin on 2017/10/3.
 * 用来渲染 Card 视图的模型
 */

public class CardModel {
    private int id;
    private String logTitle;
    private String logImage;
    private String createTime;

    public CardModel(UserLog log){
        this.id = log.getId();
        this.logTitle = log.getTitle();
        this.logImage = log.getImage();
        this.createTime = log.getCreateTime();
    }

    public int getLogId(){ return id;}

    public String getLogTitle(){
        return logTitle;
    }

    public String getLogImage(){
        return logImage;
    }

    public String getLogCreateTime(){return createTime;}
}

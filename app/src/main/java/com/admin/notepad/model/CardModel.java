package com.admin.notepad.model;

/**
 * Created by Admin on 2017/10/3.
 * 用来渲染 Card 视图的模型
 */

public class CardModel {
    private String logTitle;
    private int logImage;

    public CardModel(String name,int imageId){
        this.logTitle = name;
        this.logImage = imageId;
    }

    public String getLogTitle(){
        return logTitle;
    }

    public int getLogImageId(){
        return logImage;
    }
}

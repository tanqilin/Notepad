package com.admin.notepad.model;

/**
 * Created by Admin on 2017/10/3.
 */

public class GroupModel {
    private int groupId;
    private String groupName;

    public GroupModel(int id,String name){
        this.groupId = id;
        this.groupName = name;
    }

    public int getGroupId(){ return groupId; }

    public String getGroupName(){
        return groupName;
    }
}

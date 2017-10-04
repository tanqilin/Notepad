package com.admin.notepad.dbService;

import com.admin.notepad.db.City;
import com.admin.notepad.db.LogGroup;
import com.admin.notepad.db.Province;
import com.admin.notepad.util.DateUtil;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

/**
 * Created by Admin on 2017/10/4.
 */

public class LogGroupService {

    // 插入分组
    public static void InsertLogGroup(String name,String pass){
        LogGroup group = new LogGroup();
        group.setGroupName(name);

        // 如果设置了密码
        if(!pass.equals("")) {
            group.setSafe(true);
            group.setPassword(pass);
        }
        group.setSave(true);
        group.setDelete(false);
        group.setCreateTime(DateUtil.getDateTime());
        group.setUpdateTime(DateUtil.getDateTime());
        group.save();
    }

    // 获取所有分组
    public static List<LogGroup> GetAllLogGroup(){
        return DataSupport.findAll(LogGroup.class);
    }

    // 通过名字获取分组
    public static LogGroup GetLogGroupByName(String name){
        return DataSupport.where("groupName = ?",String.valueOf(name)).findFirst(LogGroup.class);
    }

    // 删除所有分组
    public static void DeleteAll(){
        DataSupport.deleteAll(LogGroup.class);
    }
}

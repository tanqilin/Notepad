package com.admin.notepad.dbService;

import android.text.TextUtils;

import com.admin.notepad.db.LogGroup;
import com.admin.notepad.db.UserLog;
import com.admin.notepad.util.DateUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Admin on 2017/10/4.
 */

public class LogService {

    // 插入日志（标题，内容，背景图，分组）
    public static void InsertUserLog(String title,String content,String image,LogGroup group){
        UserLog log = new UserLog();

        if(group != null)
            log.setGroupId(group.getId());

        // 设置日志内容
        if(TextUtils.isEmpty(title))
            log.setTitle("默认标题");
        else
            log.setTitle(title);

        log.setContent(content);
        log.setIsSave(true);
        log.setIsDelete(false);
        log.setImage(image);
        log.setCreateTime(DateUtil.getDateTime());
        log.setUpdateTime(DateUtil.getDateTime());
        log.save();
    }

    // 根据ID查询日志
    public static UserLog GetLogById(int id){
        return DataSupport.where("id = ?",String.valueOf(id)).findFirst(UserLog.class);
    }

    // 查询所有数据
    public static List<UserLog> GetAllUserLog(){
        return DataSupport.order("id desc").find(UserLog.class);
    }
}

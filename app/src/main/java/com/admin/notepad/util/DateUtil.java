package com.admin.notepad.util;

/**
 * Created by Admin on 2017/10/3.
 */

public class DateUtil {

    //获取系统时间的10位的时间戳
    public static String getTimeString(){
        long time = System.currentTimeMillis()/1000;
        String  str = String.valueOf(time);
        return str;
    }
}

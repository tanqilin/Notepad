package com.admin.notepad.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    // 获取日期时间字符串
    public static String getDateTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm E");
        return sdf.format(date);
    }
}

package com.admin.notepad.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by CINDY on 2017/10/1.
 * 文件和文件夹操作
 */

public class FileUtil {
    // 项目文件根目录
    public static final String FILEDIR = "/TanNotepad";

    // 判断SD卡是否存在
    public static boolean sdCardExist(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    // 创建应用文件根目录
    public static void createAppDirectory(){
        if(sdCardExist()) {
            File sdCard = Environment.getExternalStorageDirectory();
            File directory_pictures = new File(sdCard, "TanNotepad");
            if(!directory_pictures.exists())
                directory_pictures.mkdirs();
        }
    }

    // 获取应用文件根目录
    public static String getLocalPath(){
        return Environment.getExternalStorageDirectory()+ FILEDIR;
    }

    // 在应用文件根目录下创建文件
    public static  void  createFileDir(String fileDir){
        String path = getLocalPath() +fileDir;
        File createPath = new File(path);
        if(!createPath.exists())
            createPath.mkdirs();
    }
}

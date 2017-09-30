package com.admin.notepad.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by Admin on 2017/9/30.
 * 网络请求工具类
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        // 发送请求并放入回调
        client.newCall(request).enqueue(callback);
    }
}

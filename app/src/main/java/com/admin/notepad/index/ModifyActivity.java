package com.admin.notepad.index;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.notepad.R;
import com.admin.notepad.util.FileUtil;

import java.io.File;

/*
* 创建于：2017,10,01 11:45 在公司电脑上
* 修改个人资料
* */
public class ModifyActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView appTitle;
    private ImageView comeBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_modify);
        comeBack = (ImageView) findViewById(R.id.come_back);
        appTitle = (TextView) findViewById(R.id.app_title);
        appTitle.setText("修改资料");
        comeBack.setOnClickListener(this);

        // 创建一个文件夹用来存放设置图片
        FileUtil.createFileDir("/image");
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.come_back:
                finish();
                break;
            default:break;
        }
    }

    public static void actionStart(Context context){
        Intent intent = new Intent(context,ModifyActivity.class);
        context.startActivity(intent);
    }
}

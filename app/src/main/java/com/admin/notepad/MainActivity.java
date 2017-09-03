package com.admin.notepad;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.admin.notepad.index.IndexActivity;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(true){
            setContentView(R.layout.activity_main);
            new Handler().postDelayed(r, 5000);
        }else{
            setContentView(R.layout.app_start_page);
        }
    }

    // 定时跳转页面
    Runnable r = new Runnable() {
        @Override
        public void run() {
            IndexActivity.actionStart(MainActivity.this);
            finish();
        }
    };

    @Override
    public void onClick(View v){

    }
}

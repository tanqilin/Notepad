package com.admin.notepad.index;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.notepad.R;

//
public class PrivacyActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView appTitle;
    private ImageView comeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        appTitle = (TextView) findViewById(R.id.app_title);
        comeBack = (ImageView) findViewById(R.id.come_back);
        appTitle.setText("秘密中的秘密");
        comeBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.come_back:finish();break;
            default:break;
        }
    }

    // 启动活动
    public static void actionStart(Context context){
        Intent intent = new Intent(context,PrivacyActivity.class);
        context.startActivity(intent);
    }
}

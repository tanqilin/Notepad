package com.admin.notepad;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.admin.notepad.index.IndexActivity;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText pass1,pass2,pass3,pass4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        if(false){
            setContentView(R.layout.activity_main);
            new Handler().postDelayed(r, 3000);
        }else{
            setContentView(R.layout.app_start_page);
            pass1 = (EditText)findViewById(R.id.password1);
            pass2 = (EditText)findViewById(R.id.password2);
            pass3 = (EditText)findViewById(R.id.password3);
            pass4 = (EditText)findViewById(R.id.password4);

            pass1.setOnFocusChangeListener(new android.view.View.
                    OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        validatorInputPassword();
                    } else {
                        // 此处为失去焦点时的处理内容
                    }
                }
            });
            pass2.setOnFocusChangeListener(new android.view.View.
                    OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        validatorInputPassword();
                    } else {
                        // 此处为失去焦点时的处理内容
                    }
                }
            });
            pass3.setOnFocusChangeListener(new android.view.View.
                    OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        validatorInputPassword();
                    } else {
                        // 此处为失去焦点时的处理内容
                    }
                }
            });
            pass4.setOnFocusChangeListener(new android.view.View.
                    OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        validatorInputPassword();
                    } else {
                        // 此处为失去焦点时的处理内容
                    }
                }
            });
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

    // 密码必须按从左到右的顺序输入
    private void validatorInputPassword(){
        if(TextUtils.isEmpty(pass1.getText())){
            setEditTextFocusable(pass1);
            return;
        }
        if(TextUtils.isEmpty(pass2.getText())){
            setEditTextFocusable(pass2);
            return;
        }
        if(TextUtils.isEmpty(pass3.getText())){
            setEditTextFocusable(pass3);
            return;
        }
        if(TextUtils.isEmpty(pass4.getText())){
            setEditTextFocusable(pass4);
            return;
        }
    }

    // 设置输入框焦点
    private void setEditTextFocusable(EditText editText){
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
    @Override
    public void onClick(View v){

    }
}

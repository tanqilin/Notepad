package com.admin.notepad;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.admin.notepad.index.IndexActivity;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener{

    private EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏,状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SharedPreferences pref = getSharedPreferences("Setting",MODE_PRIVATE);
        boolean safe = pref.getBoolean("safe", false);
        if(!safe){
            setContentView(R.layout.activity_main);
            new Handler().postDelayed(r, 5000);
        }else{
            setContentView(R.layout.app_start_page);
            password = (EditText)findViewById(R.id.password1);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            password.addTextChangedListener(watcher);
            setEditTextFocusable(password);
        }
    }
    // 监听文本框输入变化
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

        @Override
        public void afterTextChanged(Editable s) {
            // 验证登录密码
            SharedPreferences pref = getSharedPreferences("Setting",MODE_PRIVATE);
            String password = pref.getString("password", "000000");
            if(s.toString().equals(password)){
                IndexActivity.actionStart(MainActivity.this);
                finish();
            }
            if(s.toString().length() > 5 && !(s.toString().equals(password)))
                Toast.makeText(MainActivity.this,"密码不对噢！",Toast.LENGTH_LONG).show();
        }
    };

    // 定时跳转页面
    Runnable r = new Runnable() {
        @Override
        public void run() {
            IndexActivity.actionStart(MainActivity.this);
            finish();
        }
    };


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

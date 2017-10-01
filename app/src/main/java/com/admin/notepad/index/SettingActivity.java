package com.admin.notepad.index;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.notepad.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView comeBack;
    private Switch setSave;
    private TextView appTitle;
    private LinearLayout setPassword;
    private LinearLayout changeCity;
    private LinearLayout modifyData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏,状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        comeBack = (ImageView) findViewById(R.id.come_back);
        setSave = (Switch) findViewById(R.id.switch_save);
        appTitle = (TextView) findViewById(R.id.app_title);
        setPassword = (LinearLayout) findViewById(R.id.set_password);
        changeCity = (LinearLayout) findViewById(R.id.change_city);
        modifyData = (LinearLayout) findViewById(R.id.modify_userdata);

        setPassword.setOnClickListener(this);
        changeCity.setOnClickListener(this);
        modifyData.setOnClickListener(this);
        comeBack.setOnClickListener(this);
        appTitle.setText("设置中心");

        // 根据缓存判断是否开启安全保护
        SharedPreferences pref = getSharedPreferences("Setting",MODE_PRIVATE);
        boolean safe = pref.getBoolean("safe", false);
        setSave.setChecked(safe);

        setSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                SharedPreferences.Editor editor = getSharedPreferences("Setting",MODE_PRIVATE).edit();
                if (isChecked) {
                    editor.putBoolean("safe", true);
                    Toast.makeText(SettingActivity.this,"已开启密码保护",Toast.LENGTH_SHORT).show();
                } else {
                    editor.putBoolean("safe", false);
                    Toast.makeText(SettingActivity.this,"已关闭密码保护",Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.come_back:finish();break;
            case R.id.set_password:
                showPopupWindow(SettingActivity.this,SettingActivity.this.findViewById(R.id.set_password));
                break;
            case R.id.change_city:
                ChangeCityActivity.actionStart(SettingActivity.this); break;
            case R.id.modify_userdata:
                ModifyActivity.actionStart(SettingActivity.this); break;
            default:break;
        }
    }

    // 启动活动
    public static void actionStart(Context context){
        Intent intent = new Intent(context,SettingActivity.class);
        context.startActivity(intent);
    }

    // 修改密码模态框
    public void showPopupWindow(Context context,View parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopupWindow=inflater.inflate(R.layout.popupwindow, null, false);
        final PopupWindow pw= new PopupWindow(vPopupWindow, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);

        //OK按钮及其处理事件
        Button btnOK = (Button) vPopupWindow.findViewById(R.id.dialog_ok);
        final EditText oldPassword = (EditText) vPopupWindow.findViewById(R.id.old_password);
        final EditText newPassword = (EditText) vPopupWindow.findViewById(R.id.new_password);
        oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        btnOK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String old = oldPassword.getText().toString();
                String newPass = newPassword.getText().toString();

                // 先验证原始密码
                SharedPreferences pref = getSharedPreferences("Setting",MODE_PRIVATE);
                String password = pref.getString("password", "000000");
                if(newPass.length() != 6){
                    Toast.makeText(SettingActivity.this, "密码必须是6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!old.equals(password)) {
                    Toast.makeText(SettingActivity.this, "原始密码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = getSharedPreferences("Setting",MODE_PRIVATE).edit();
                editor.putString("password", newPass);
                editor.commit();
                Toast.makeText(SettingActivity.this,"小朋友，修改成功啦",Toast.LENGTH_SHORT).show();
                pw.dismiss();//关闭
            }
        });

        //Cancel按钮及其处理事件
        Button btnCancel=(Button)vPopupWindow.findViewById(R.id.dialog_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pw.dismiss();//关闭
            }
        });
        //显示popupWindow对话框
        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
}

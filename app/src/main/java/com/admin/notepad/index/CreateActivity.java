package com.admin.notepad.index;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.notepad.R;
import com.admin.notepad.db.LogGroup;
import com.admin.notepad.dbService.LogGroupService;
import com.admin.notepad.dbService.LogService;
import com.admin.notepad.util.DateUtil;
import com.admin.notepad.util.FileUtil;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SET_BACK = 1; // 设置背景
    public static final int CROP_PHOTO = 2; // 相册

    private TextView appTitle;
    private ImageView comeBack;
    private ImageView logImage;
    private ImageButton uploadImg;
    private EditText createLogTitle;
    private EditText createLogContent;

    // 下拉列表
    private Spinner spinner;
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    // 上传图片
    private Uri imageUri;

    // 用户输入
    private String savePath;
    private String logContent;
    private String logTitle;
    private LogGroup logGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        appTitle = (TextView) findViewById(R.id.app_title);
        comeBack = (ImageView) findViewById(R.id.come_back);
        logImage = (ImageView) findViewById(R.id.log_image);
        uploadImg = (ImageButton) findViewById(R.id.upload_log_image);
        createLogTitle = (EditText) findViewById(R.id.create_log_title);
        createLogContent = (EditText) findViewById(R.id.create_log_content);

        appTitle.setText("记录每一刻");
        comeBack.setOnClickListener(this);
        uploadImg.setOnClickListener(this);

        // 下拉列表
        spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
                // 获取用户选择的分组
                logGroup =  LogGroupService.GetLogGroupByName(data_list.get(pos));

                if(data_list.get(pos) == "新建分组")
                    showPopupWindow(CreateActivity.this,CreateActivity.this.findViewById(R.id.create_log_layout));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        // 初始化下拉选项
        initSpinnerDownData();
    }

    // 初始化Spinner下拉菜单数据
    public void initSpinnerDownData(){
        List<LogGroup> groups = LogGroupService.GetAllLogGroup();

        // 绑定数据
        data_list = new ArrayList<String>();
        data_list.add("分组");
        for(LogGroup groupName : groups){
            data_list.add(groupName.getGroupName());
        }
        data_list.add("新建分组");

        //适配器
        arr_adapter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list);
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arr_adapter);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.come_back:finish();break;
            case R.id.upload_log_image:openCamera(SET_BACK);break;
            default:break;
        }
    }

    // 打开相册
    public void openCamera(int type){
        // 打开手机相册
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, type);
    }

    // 处理选择的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没选择图片就退出了
        if(data == null) return;
        Uri uri = data.getData();

        switch (requestCode) {
            case SET_BACK:  // 设置背景
                if (resultCode == RESULT_OK) {
                    saveImageToCach(uri);
                    // 从缓存中取出文件
                    File outputImage = new File(FileUtil.getLocalPath()+"/cache","cache.jpg");
                    imageUri = Uri.fromFile(outputImage);
                    // 裁剪图片
                    startCropImage(3,2);
                }
                break;
            case CROP_PHOTO:    // 保存并显示图片
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        savePath = saveImageToImage(bitmap,"log_"+ DateUtil.getTimeString());
                        // 显示
                        Glide.with(this).load(savePath).into(logImage);
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                }
                break;
            default:break;
        }
    }

    // 把选择的文件先保存到缓存中，在从缓存中调到剪切程序中
    public void saveImageToCach(Uri uri){
        try{
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

            String fileName = "cache" + ".jpg";
            File file = new File(FileUtil.getLocalPath()+"/cache", fileName);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Exception", e.getMessage(), e);
        }
    }

    // 裁剪图片(传入宽高比)
    private void startCropImage(int width,int height){
        // 启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", width);
        intent.putExtra("aspectY", height);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CROP_PHOTO);
    }

    // 保存裁剪后的文件到文件夹(日志插图)
    public String saveImageToImage(Bitmap bitmap,String pictureType){
        // 文件存在则删除
        File file = new File(FileUtil.getLocalPath()+"/image", pictureType + ".jpg");
        try{
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        // 保存文件
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.toString();
    }

    // 新建分组模态框
    public void showPopupWindow(Context context,View parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopupWindow=inflater.inflate(R.layout.activity_group_create_model, null, false);
        final PopupWindow pw= new PopupWindow(vPopupWindow, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

        //OK按钮及其处理事件
        Button btnOK = (Button) vPopupWindow.findViewById(R.id.dialog_ok);
        final EditText groupName = (EditText) vPopupWindow.findViewById(R.id.group_name);
        final EditText groupPassword = (EditText) vPopupWindow.findViewById(R.id.group_password);
        groupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        btnOK.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = groupName.getText().toString();
                String pass = groupPassword.getText().toString();

                if (TextUtils.isEmpty(name.replace(" ",""))){
                    Toast.makeText(CreateActivity.this,"分组名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }

                // 存入数据库
                LogGroupService.InsertLogGroup(name,pass);
                Toast.makeText(CreateActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                initSpinnerDownData();
                pw.dismiss();
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

    // 启动活动
    public static void actionStart(Context context){
        Intent intent = new Intent(context,CreateActivity.class);
        context.startActivity(intent);
    }

    // 保存日志
    private void saveLog(){
        logTitle = createLogTitle.getText().toString();
        logContent = createLogContent.getText().toString();

        if(TextUtils.isEmpty(logTitle.replace(" ","")) && TextUtils.isEmpty(logContent.replace(" ",""))) {
            return;
        }

        // 存入数据库
        LogService.InsertUserLog(logTitle,logContent,savePath,logGroup);
        Toast.makeText(CreateActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        saveLog();
        super.onDestroy();
    }
}

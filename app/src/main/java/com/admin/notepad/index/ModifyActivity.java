package com.admin.notepad.index;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.notepad.R;
import com.admin.notepad.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

/*
* 创建于：2017,10,01 11:45 在公司电脑上 CircleImageView
* 修改个人资料
* */
public class ModifyActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int SET_HEAD = 0; // 设置头像
    public static final int SET_BACK = 1; // 设置背景
    public static final int CROP_PHOTO = 2; // 相册

    private TextView appTitle;
    private ImageView comeBack;
    private ImageView backgroundImage;
    private CircleImageView headPicture;

    private Uri imageUri;
    private int activityType = 0; // 正在设置的类别
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        comeBack = (ImageView) findViewById(R.id.come_back);
        backgroundImage = (ImageView) findViewById(R.id.background_image);
        appTitle = (TextView) findViewById(R.id.app_title);
        headPicture = (CircleImageView) findViewById(R.id.head_picture);
        appTitle.setText("修改资料");

        backgroundImage.setOnClickListener(this);
        headPicture.setOnClickListener(this);
        comeBack.setOnClickListener(this);
        headPicture.bringToFront();

        initUserSetting();
    }

    // 加载用户默认设置
    private void initUserSetting(){
        // 从缓存中加载用户设置好的图片
        SharedPreferences pref = getSharedPreferences("Setting",MODE_PRIVATE);
        String background = pref.getString("background", null);
        String head = pref.getString("head", null);
        if(background != null)
            backgroundImage.setImageURI(Uri.parse(background));
        if(head != null)
            headPicture.setImageURI(Uri.parse(head));
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.come_back:finish();break;
            case R.id.background_image:
                activityType = 1;
                openCamera(SET_BACK);break;
            case R.id.head_picture:
                activityType = 0;
                openCamera(SET_HEAD);break;
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

    /*
     * 图片裁剪和显示图片
     * 先把选择的图片存入缓存
     * 再从缓存中吧图片取到裁剪程序中
     * 最后把裁剪后的图片存入文件夹
     */
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

                    startCropImage(3,2);
                }
            break;
            case SET_HEAD:  // 设置头像
                if (resultCode == RESULT_OK) {
                    saveImageToCach(uri);
                    // 从缓存中取出文件
                    File outputImage = new File(FileUtil.getLocalPath()+"/cache","cache.jpg");
                    imageUri = Uri.fromFile(outputImage);

                    startCropImage(1,1);
                }
                break;
            case CROP_PHOTO:    // 保存并显示图片
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        if(activityType == 0){
                            saveImageToImage(bitmap,"head");
                            headPicture.setImageBitmap(bitmap);
                        }
                        else if (activityType == 1){
                            saveImageToImage(bitmap,"background");
                            backgroundImage.setImageBitmap(bitmap);
                        }
                    } catch (FileNotFoundException e) {
                        Log.e("Exception", e.getMessage(), e);
                    }
                }
            break;
            default:break;
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

    // 保存裁剪后的文件到文件夹(文件，文件类型(背景图/头像))
    public void saveImageToImage(Bitmap bitmap,String pictureType){
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

        // 把保存后的图片路径存入缓存
        SharedPreferences.Editor editor = getSharedPreferences("Setting",MODE_PRIVATE).edit();
        editor.putString( pictureType , file.toString());
        editor.commit();
    }


    public static void actionStart(Context context){
        Intent intent = new Intent(context,ModifyActivity.class);
        context.startActivity(intent);
    }
}

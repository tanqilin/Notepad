package com.admin.notepad.index;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.notepad.R;
import com.admin.notepad.db.City;
import com.admin.notepad.db.County;
import com.admin.notepad.db.Province;
import com.admin.notepad.gson.Bing;
import com.admin.notepad.util.HttpUtil;
import com.admin.notepad.util.Utility;
import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChangeCityActivity extends AppCompatActivity implements View.OnClickListener{
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private TextView appTitle;
    private ImageView comeBack;

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private ImageView bingImage;

    // 省列表
    private List<Province> provinceList;
    // 市区列表
    private List<City> cityList;
    // 县列表
    private List<County> countyList;

    // 选中省，市，区
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_city);
        appTitle = (TextView) findViewById(R.id.app_title);
        comeBack = (ImageView) findViewById(R.id.come_back);
        listView  = (ListView) findViewById(R.id.list_view);
        comeBack.setOnClickListener(this);

        // 创建适配器，渲染 ListView
        adapter = new ArrayAdapter<>(ChangeCityActivity.this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        // 给listView 设置监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View v,int position,long id){
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCitiese();
                }else if( currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounty();
                }else if(currentLevel == LEVEL_COUNTY ){
                    String weatherId = countyList.get(position).getWeatherId();
                    // 判断fragment是在哪个当前窗口中打开
                    Intent intent = new Intent(ChangeCityActivity.this,WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    finish(); // 释放依附的Activity
                }
            }
        });

        queryProvince();    // 打开页面时加载全国省份
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.come_back:
                // 根据当前显示内容返回上一级
                if(currentLevel == LEVEL_COUNTY)
                    queryCitiese();
                else if(currentLevel == LEVEL_CITY)
                    queryProvince();
                else
                    finish();
                break;
            default:break;
        }
    }

    /*
    *  显示必应每日一图
    * */
    public void showBingImage(){
        // 从缓存中获取
        SharedPreferences prefer = PreferenceManager.getDefaultSharedPreferences(ChangeCityActivity.this);
        if(prefer.getString("bing_pic",null) != null){
            String bingImg = prefer.getString("bing_pic",null);
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ChangeCityActivity.this).edit();
            editor.putString("bing_pic",bingImg);
            editor.apply();
            Glide.with(ChangeCityActivity.this).load(bingImg).into(bingImage);
        }else{
            loadBingPic();
        }
    }

    /*
   * 查询全国的省份
   * */
    private void queryProvince(){
        appTitle.setText("换个城市吧");
        provinceList = DataSupport.findAll(Province.class);
        // 如果数据存在，则现实数据库中信息，否则请求网络获取
        if(provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china/";
            queryFromServer(address,"province");
        }
    }

    /*
    *  查询选中省内的所有市
    * */
    public void queryCitiese(){
        appTitle.setText(selectedProvince.getProvinceName());
        cityList = DataSupport.where("provinceId = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        // 如果数据存在
        if(cityList.size() > 0){
            dataList.clear();
            for (City city : cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryFromServer(address,"city");
        }
    }

    /*
    *  查询选中市内的所有县
    * */
    private void queryCounty(){
        appTitle.setText(selectedCity.getCityName());
        countyList = DataSupport.where("cityId = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size() > 0){
            dataList.clear();
            for (County county : countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryFromServer(address,"county");
        }
    }

    /*
   *  发送网络请求并处理返回结果
   * */
    private void queryFromServer(String address,final String type){
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                // 把请求结果存入数据库
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if("city".equals(type)){
                    result = Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }

                // 存储成功后更新界面
                if(result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if("province".equals(type)){
                                queryProvince();
                            }else if("city".equals(type)){
                                queryCitiese();
                            }else if("county".equals(type)){
                                queryCounty();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChangeCityActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    // 加载必应每日一图
    public void loadBingPic(){
        String requestBingPic = "http://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                // 把加载的必应图片路径存入缓存
                Bing bing = Utility.handleBingResponse(responseText);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ChangeCityActivity.this).edit();
                editor.putString("bing_pic","http://cn.bing.com" + bing.url);
                editor.apply();
                showBingImage();    // 加载完成后显示图片
            }
        });
    }

    // 启动活动
    public static void actionStart(Context context){
        Intent intent = new Intent(context,ChangeCityActivity.class);
        context.startActivity(intent);
    }
}

package com.admin.notepad.util;

import android.text.TextUtils;

import com.admin.notepad.db.City;
import com.admin.notepad.db.County;
import com.admin.notepad.db.Province;
import com.admin.notepad.gson.Bing;
import com.admin.notepad.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Admin on 2017/9/30.
 *  解析请求后的json数据
 */

public class Utility {
    /*
   * 解析和处理服务器返回的省级数据
   * */
    public static boolean handleProvinceResponse(String response){
        if(!TextUtils.isEmpty(response)){
            try{
                // 解析json数据
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++){
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    // 把解析结果存入数据库
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    *  解析和处理返回的市级数据
    * */
    public static boolean handleCityResponse(String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            try{
                // 解析json数据
                JSONArray allCiry = new JSONArray(response);
                for (int i = 0; i < allCiry.length(); i++){
                    JSONObject cityObject = allCiry.getJSONObject(i);
                    // 把解析结果存入数据库
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    * 解析和处理返回的县级JSON数据
    * */
    public static boolean handleCountyResponse(String response,int cityId){
        if(!TextUtils.isEmpty(response)){
            try{
                // 解析json数据
                JSONArray allCounty = new JSONArray(response);
                for (int i = 0; i < allCounty.length(); i++){
                    JSONObject countyObject = allCounty.getJSONObject(i);
                    // 把解析结果存入数据库
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

    /*
    *  解析和风天气返回的JSON信息
    * */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /*
    *  解析必应每日一图返回的JSON
    * */
    public static Bing handleBingResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("images");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Bing.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

package com.example.lifebl.weather_forecast.tools;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.lifebl.weather_forecast.db.DBmanage;
import com.example.lifebl.weather_forecast.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Json_utility {

    //读取本地json文件
    public static String getLocalJson(Context context){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream input= context.getAssets().open("city.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line=bufferedReader.readLine()) != null){
                stringBuilder.append(line);
            }
            input.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    //解析Json字符串
    public static boolean handleCitiesResponse(DBmanage dbmanage, String response) {
        if(!TextUtils.isEmpty(response)){
        try{
            JSONArray provinces=new JSONArray(response);
            for(int i=0;i<provinces.length();i++){
                JSONObject cityObject=provinces.getJSONObject(i);
                dbmanage.saveCity(cityObject);
            }
            return true;
        }catch(JSONException e){
            e.printStackTrace();
            Log.d("lifebl","解析失败");
        }
        }
        return false;
    }
    //解析天气，将返回的额数据解析成Weather实体类
    public static Weather handleWeatherResponse(String resoponse){
        try{
            JSONObject jsonObject=new JSONObject(resoponse);
            String weatherContent=jsonObject.toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}

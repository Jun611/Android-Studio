package com.example.lifebl.weather_forecast.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.exceptions.DataSupportException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class DBmanage {

    //保存城市信息
    public void saveCity(JSONObject cityobject) {
        try {
            City city = new City();
            city.setCity_name(cityobject.getString("city_name"));
            city.setCity_code(cityobject.getString("city_code"));
            city.setUnique_id(cityobject.getString("id"));
            city.setPid(cityobject.getString("pid"));
            city.save();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    //返回所有省份信息
    public List<City> getAllProvince() {
        List<City> provinceList=LitePal.where("pid=?","0").find(City.class);
        return provinceList;
    }

    //返回指定省份下的所有城市
    public List<City> getAllCity(String provinceId) {
        List<City> cityList = LitePal.where("pid=?",provinceId).find(City.class);
        return cityList;
    }

    //增加
    public static  void addCity(String cityid,String cityname,String citydegree,String citydate ){
        CityList cityList=new CityList();
        //cityList.setCityDate(citydate);
        //cityList.setCityDegree(citydegree);
        cityList.setCityId(cityid);
        cityList.setCityName(cityname);
        cityList.save();
    }
    //删
    public static void deleteCity(int id){
        LitePal.delete(CityList.class, id);
    }
    //删by cityId
    public static void deleteCityByCityId(String cityid){
        LitePal.deleteAll(CityList.class,"cityId = ?",cityid);
    }
    //修改
    public static void updateNote(int id,String degree,String citydate){
        CityList cityList=new CityList();
        cityList.setCityDegree(degree);
        cityList.setCityDate(citydate);
        cityList.update(id);
    }
    //查
    public List<CityList> getAllCity(){
        List<CityList> array =LitePal.findAll(CityList.class);
        List<CityList> tempArray = new ArrayList<CityList>();
        for (int i = array.size(); i >0; i--) {
            tempArray.add(array.get(i-1));
        }
        return tempArray;
    }
    //查城市By 城市名字
    public List<City> getCityByName(String name){
        List<City> cityList = LitePal.where("city_name like ?",name+"%").find(City.class);
        return cityList;
    }
    //查城市By 城市ID
    public City getCityById(String id){
        List<City> cityList = LitePal.where("city_code = ?",id).find(City.class);
        City city=cityList.get(0);
        return city;
    }
}


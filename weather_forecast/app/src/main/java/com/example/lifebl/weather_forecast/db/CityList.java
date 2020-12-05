package com.example.lifebl.weather_forecast.db;

import org.litepal.crud.LitePalSupport;

public class CityList extends LitePalSupport {
    private int id;
    private String cityId;
    private String cityName;
    private String cityDegree;
    private String cityDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityDegree() {
        return cityDegree;
    }

    public void setCityDegree(String cityDegree) {
        this.cityDegree = cityDegree;
    }

    public String getCityDate() {
        return cityDate;
    }

    public void setCityDate(String cityDate) {
        this.cityDate = cityDate;
    }
}

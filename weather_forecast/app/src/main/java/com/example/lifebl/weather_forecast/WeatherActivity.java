package com.example.lifebl.weather_forecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lifebl.weather_forecast.db.DBmanage;
import com.example.lifebl.weather_forecast.gson.Forecast;
import com.example.lifebl.weather_forecast.gson.Weather;
import com.example.lifebl.weather_forecast.tools.Json_utility;
import com.example.lifebl.weather_forecast.tools.OkHttp;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//import android.support.v4.widget.SwipeRefreshLayout;

//import android.support.v7.app.AppCompatActivity;

public class WeatherActivity extends AppCompatActivity {
    //滑动查看
    private ScrollView weatherLayout;
    //城市名称
    private TextView titleCity;
    //收藏按钮
    private Button favorite;
    private Button favorite2;
    //今天的时间
    private TextView todayDateText;
    //数据的更新时间
    private TextView titleUpdateTime;
    //现在的天气温度
    private TextView degreeText;
    //今日最低最高温
    private TextView mix_max_degree;
    //现在的天气情况，晴、阴、雨、多云...
    private TextView weatherInfoText;
    //未来天气
    private LinearLayout forecastLayout;
    //空气指数
    private TextView aqiText;
    //pm2.5
    private TextView pm25Text;
    //空气等级
    private TextView qualityLevelText;
    //湿度
    private TextView shiduText;
    //风向
    private TextView fengxiangText;
    //风力
    private TextView fengliText;
    //notice
    private TextView noticeText;
    //Mainactivity传过来的城市ID
    private String CityID;
    //解析出来的Weather对象
    private Button back_favorite;
    private Weather weather;
    private String responseText;
    private DBmanage dBmanage;
    private SwipeRefreshLayout push_refresh;
    //缓存计数器("3",null)
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    //天气的描述图片
    private ImageView sunny;
    private ImageView cloud;
    private ImageView yin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        // 初始化各控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        favorite = (Button) findViewById(R.id.favorite);
        favorite2 = (Button) findViewById(R.id.favorite2);
        todayDateText = (TextView) findViewById(R.id.todat_date);
        titleUpdateTime = (TextView) findViewById(R.id.title_update_time);
        degreeText = (TextView) findViewById(R.id.degree_text);
        mix_max_degree = (TextView) findViewById(R.id.mix_max_degree);
        weatherInfoText = (TextView) findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_layout);
        aqiText = (TextView) findViewById(R.id.aqi_text);
        pm25Text = (TextView) findViewById(R.id.pm25_text);
        qualityLevelText = (TextView) findViewById(R.id.quality_level);
        shiduText = (TextView) findViewById(R.id.shidu_text);
        fengxiangText = (TextView) findViewById(R.id.fengxiang_text);
        fengliText = (TextView) findViewById(R.id.fengli_text);
        noticeText = (TextView) findViewById(R.id.notice_text);
        back_favorite = (Button) findViewById(R.id.back_favorite);
        sunny=(ImageView)findViewById(R.id.sunny);
        cloud=(ImageView)findViewById(R.id.cloud);
        yin=(ImageView)findViewById(R.id.yin);
        yin.setVisibility(View.INVISIBLE);
        sunny.setVisibility(View.INVISIBLE);cloud.setVisibility(View.INVISIBLE);
        push_refresh = (SwipeRefreshLayout) findViewById(R.id.push_refresh);
        push_refresh.setColorSchemeResources(R.color.colorPrimary);
        dBmanage = new DBmanage();
        CityID = getIntent().getStringExtra("city_code");
        //FirstActivityID=getIntent().getStringExtra("firstActivityId");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
        final String weatherString = prefs.getString(CityID, null);

        //缓存计数器
//        COUNT = prefs.getString(6+"", null);
//        Log.d("lifeblll",COUNT);
        if (prefs.getString(3+"", null)==null) {
            editor.putString("3", 0+"");
            editor.apply();
        }

        //读取最近3次的天气数据和城市ID缓存
        String cache[] = new String[3];
        for (int i = 0; i < 3; i++) {
            cache[i] = prefs.getString(String.valueOf(i), null);
        }
        String _cache[] = new String[3];
        for (int i = 0; i < 3; i++) {
            _cache[i] = prefs.getString("_"+String.valueOf(i), null);
        }

        //判断cityID对应的sharedPreferences里是否有无缓存
        if (CityID.equals(_cache[0])|| CityID.equals(_cache[1])|| CityID.equals(_cache[2])) {
            if (CityID.equals(_cache[0])) {
                weather = Json_utility.handleWeatherResponse(cache[0]);
            } else if (CityID.equals(_cache[1])) {
                weather = Json_utility.handleWeatherResponse(cache[1]);
            } else if (CityID.equals(_cache[2])) {
                weather = Json_utility.handleWeatherResponse(cache[2]);
            }
            if(weatherString!=null){
                favorite.setVisibility(View.INVISIBLE);
                favorite2.setVisibility(View.VISIBLE);
            }else{
                favorite.setVisibility(View.VISIBLE);
                favorite2.setVisibility(View.INVISIBLE);
            }
            showWeatherInfo(weather);
            Toast.makeText(WeatherActivity.this, "最近读取", Toast.LENGTH_LONG).show();
        } else if (weatherString != null) {
            // 有缓存时直接解析天气数据
            favorite.setVisibility(View.INVISIBLE);
            favorite2.setVisibility(View.VISIBLE);
            weather = Json_utility.handleWeatherResponse(weatherString);
            CityID = weather.cityInfo.cityId;
            showWeatherInfo(weather);
            Toast.makeText(WeatherActivity.this, "本地读取", Toast.LENGTH_LONG).show();
        } else {
            favorite.setVisibility(View.VISIBLE);
            favorite2.setVisibility(View.INVISIBLE);
            // 无缓存时去服务器查询天气
            CityID = getIntent().getStringExtra("city_code");
            //weatherLayout.setVisibility(View.INVISIBLE);
            ///Log.d("lifeblll",CityID);
            requestWeather(CityID);
        }

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite2.setVisibility(View.VISIBLE);
                favorite.setVisibility(View.INVISIBLE);
                editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString(CityID, responseText);
                editor.apply();
                //添加数据库

                String cityid = weather.cityInfo.cityId;
                String cityname = weather.cityInfo.cityname;
                String citydegree = weather.data.wendu + "℃";
                String citydate = weather.data.forecast.get(0).ymd;
                dBmanage.addCity(cityid, cityname, citydegree, citydate);
                Toast.makeText(WeatherActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
            }
        });
        favorite2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite.setVisibility(View.VISIBLE);
                favorite2.setVisibility(View.INVISIBLE);
                dBmanage.deleteCityByCityId(CityID);
                editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.remove(CityID);
                editor.apply();
                Toast.makeText(WeatherActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
            }
        });
        back_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, First_activity.class);
                startActivity(intent);
            }
        });
        push_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(CityID);
            }
        });
    }

    /**
     * 根据天气id请求城市天气信息。
     */

    public void requestWeather(final String weatherId) {
       // String weatherUrl = "http://t.weather.sojson.com/api/weather/city/"+ weatherId ;
        String weatherUrl = "http://t.weather.itboy.net/api/weather/city/"+ weatherId ;   //新的网址
        Log.d("lifeblll", weatherUrl);
        OkHttp.sendHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseText = response.body().string();
                weather = Json_utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && weather.status.equals("200")) {
                            CityID = weather.cityInfo.cityId;
                            showWeatherInfo(weather);
                        } else {
                            Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        if (responseText.length()<100){
                            Toast.makeText(WeatherActivity.this, "该城市代码不存在", Toast.LENGTH_LONG).show();
                        }else{
                        Toast.makeText(WeatherActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();}
                        push_refresh.setRefreshing(false);
                        //最近三次数据缓存
                        String COUNT=prefs.getString("3",null);
                        editor.putString(Integer.parseInt(COUNT) % 3 + "", responseText);
                        editor.putString("_"+String.valueOf(Integer.parseInt(COUNT)% 3), CityID);
                        int i = Integer.parseInt(COUNT) + 1;
                        if (i == 2000) {
                            i = 2;
                        }
                        editor.putString("3", i + "");
                        editor.apply();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        Toast.makeText(WeatherActivity.this, "刷新失败", Toast.LENGTH_SHORT).show();
                        push_refresh.setRefreshing(false);
                        //swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.cityInfo.parent + "-" + weather.cityInfo.cityname;
        String todayDate = weather.data.forecast.get(0).ymd + "  " + weather.data.forecast.get(0).week;
        String updateTime = "更新于" + weather.cityInfo.updateTime;
        String degree = weather.data.wendu + "℃";
        String low = weather.data.forecast.get(0).low;
        String max = weather.data.forecast.get(0).high;
        String mixToMaxDegree = low.substring(2, low.length()) + "~" + max.substring(2, max.length());
        String weatherInfo = weather.data.forecast.get(0).type;

        //六个天气参数
        String aqi = weather.data.forecast.get(0).aqi;
        String pm25 = weather.data.pm25;
        String quality_level = weather.data.quality;
        String shidu = weather.data.shidu;
        String fengxiang = weather.data.forecast.get(0).fx;
        String fengli = weather.data.forecast.get(0).fl;

        titleCity.setText(cityName);
        todayDateText.setText(todayDate);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        mix_max_degree.setText(mixToMaxDegree);
        weatherInfoText.setText(weatherInfo);

        //设置天气的描述图片
        switch (weatherInfo){
            case "晴":
                sunny.setVisibility(View.VISIBLE);
                break;
            case "多云":
                cloud.setVisibility(View.VISIBLE);
                break;
            case "阴":
                yin.setVisibility(View.VISIBLE);
                break;
        }
        //设置六种参数
        aqiText.setText(aqi);
        pm25Text.setText(pm25);
        qualityLevelText.setText(quality_level);
        shiduText.setText(shidu);
        fengxiangText.setText(fengxiang);
        fengliText.setText(fengli);
        //添加forecastLayout的子项
        forecastLayout.removeAllViews();
        List<Forecast> forecastList = weather.data.forecast;
        for (int i = 0; i < weather.data.forecast.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecastList.get(i).ymd);
            infoText.setText(forecastList.get(i).type);
            maxText.setText(forecastList.get(i).high);
            minText.setText(forecastList.get(i).low);
            forecastLayout.addView(view);
        }

        //设置天气描述notice
        String notice = weather.data.forecast.get(0).notice;
        noticeText.setText(notice);
        weatherLayout.setVisibility(View.VISIBLE);
//        Intent intent = new Intent(this, WeatherActivity.class);
//        startActivity(intent);
    }

}
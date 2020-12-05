package com.example.lifebl.weather_forecast;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifebl.weather_forecast.db.City;
import com.example.lifebl.weather_forecast.db.DBmanage;
import com.example.lifebl.weather_forecast.tools.Json_utility;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

//package com.example.lifebl.weather_forecast;

public class MainActivity extends AppCompatActivity {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private DBmanage dbmanage;
    private Button back;
    private List<String> dataList = new ArrayList<String>();
    //省列表
    private List<City> provinceList;
    //市列表
    private List<City> cityList;
    //选中的省份
    private City selectedProvince;
    // 选中的城市
    private City selectedCity;
    // 当前选中的级别
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();
        dbmanage = new DBmanage();
        back = (Button) findViewById(R.id.back_button);
        titleText = (TextView) findViewById(R.id.title);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    String chooseCity_Code = cityList.get(position).getCity_code();
                    Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                    intent.putExtra("city_code", chooseCity_Code);
                    startActivity(intent);
                    //finish();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_CITY) {
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }

    //查询全国所有的省
    private void queryProvinces() {
        titleText.setText("中国");
        back.setVisibility(View.INVISIBLE);
        provinceList = dbmanage.getAllProvince();
        if (provinceList.size() > 0) {
            dataList.clear();
            for (City province : provinceList) {
                dataList.add(province.getCity_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            queryFromLocalJson();
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities() {
        if (selectedProvince.getUnique_id().equals("33") || selectedProvince.getUnique_id().equals("32")) {
        } else {
            back.setVisibility(View.VISIBLE);
        }
        if (selectedProvince.getCity_name().equals("香港")) {
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            intent.putExtra("city_code", "101320101");
            startActivity(intent);
        } else if(selectedProvince.getCity_name().equals("澳门")){
            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            intent.putExtra("city_code", "101330101");
            startActivity(intent);
        } else {
            titleText.setText(selectedProvince.getCity_name());
            cityList = dbmanage.getAllCity(selectedProvince.getUnique_id());
            if (cityList.size() > 0) {
                dataList.clear();
                for (City city : cityList) {
                    dataList.add(city.getCity_name());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                currentLevel = LEVEL_CITY;
            }
        }
    }

    /**
     * 从服务器上查询全国所有城市数据
     */
    private void queryFromLocalJson() {
        String str = Json_utility.getLocalJson(MainActivity.this);
        Json_utility.handleCitiesResponse(dbmanage, str);
        Log.d("lifebl", "解析本地json完毕");
        queryProvinces();
//        String address = "http://cdn.sojson.com/_city.json";
//        OkHttp.sendHttpRequest(address, new Callback(){
//
//            @Override
//            public void onResponse(Call call, Responjsonse response) throws IOException {
//                String responseText = response.body().string();
//                boolean result = false;
//                result = Json_utility.handleCitiesResponse(dbmanage, responseText);
//                if (result) {
//                    // 通过runOnUiThread()方法回到主线程处理逻辑
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            queryProvinces();
//                        }
//                    });
//                }
//
//            }
//
//            public void onFailure(Call call,IOException e) {
//                // 通过runOnUiThread()方法回到主线程处理逻辑
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
    }

}
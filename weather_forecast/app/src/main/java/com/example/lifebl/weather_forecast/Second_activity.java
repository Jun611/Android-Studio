package com.example.lifebl.weather_forecast;

import android.content.Intent;
import android.os.Bundle;
//import android.support.annotation.Nullable;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifebl.weather_forecast.db.City;
import com.example.lifebl.weather_forecast.db.DBmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

//import android.support.v7.app.AppCompatActivity;


public class Second_activity extends AppCompatActivity {

    private ListView listViewSecond;
    private List<City> cityList;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private DBmanage dBmanage;
    private Button search_back;
    private Button search;
    private Button delete_content;
    private EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        search_back=(Button)findViewById(R.id.search_back);
        search=(Button)findViewById(R.id.search);
        delete_content=(Button)findViewById(R.id.delete_content);
        editText=(EditText)findViewById(R.id.editText);
        listViewSecond=(ListView)findViewById(R.id.listViewSecond);
        cityList=new ArrayList<City>();
        dBmanage=new DBmanage();
        dataList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(Second_activity.this, android.R.layout.simple_list_item_1, dataList);
        listViewSecond.setAdapter(adapter);
        final String content=editText.getText().toString();
        //设置点击搜索按键后的操作
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
                if (isNumeric(content)) {
                    if(content.length()!=9){
                        Toast.makeText(Second_activity.this,"城市ID为9位哦~",Toast.LENGTH_SHORT).show();
                    }else{
                        City choosecity=dBmanage.getCityById(content);
                        cityList.clear();
                        cityList.add(choosecity);
                        dataList.clear();
                        dataList.add(choosecity.getCity_name());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    try {
                        cityList.clear();
                        cityList = dBmanage.getCityByName(content);
                        if (cityList.get(0).getPid().equals("0")) {
                            cityList = dBmanage.getAllCity(cityList.get(0).getUnique_id());
                            dataList.clear();
                            for (City city : cityList) {
                                dataList.add(city.getCity_name());
                            }
                            adapter.notifyDataSetChanged();
//                            adapter = new ArrayAdapter<String>(Second_activity.this, android.R.layout.simple_list_item_1, dataList);
//                            listViewSecond.setAdapter(adapter);
//                            listViewSecond.setSelection(0);
                        } else {
                            dataList.clear();
                            for (City city : cityList) {
                                dataList.add(city.getCity_name());
                            }
                            adapter.notifyDataSetChanged();
//                            adapter = new ArrayAdapter<String>(Second_activity.this, android.R.layout.simple_list_item_1, dataList);
//                            listViewSecond.setAdapter(adapter);
//                            listViewSecond.setSelection(0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Second_activity.this, "没找到哦~", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //设置点击返回按键后的操作
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置清空内容按钮的点击事件
        delete_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        //listviewSecond的点击事件
        listViewSecond.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String City_Code = cityList.get(position).getCity_code();
                Intent intent = new Intent(Second_activity.this, WeatherActivity.class);
                intent.putExtra("city_code", City_Code);
                startActivity(intent);
            }
        });
    }
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}

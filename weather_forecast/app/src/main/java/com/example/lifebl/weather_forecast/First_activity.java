package com.example.lifebl.weather_forecast;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lifebl.weather_forecast.db.CityList;
import com.example.lifebl.weather_forecast.db.DBmanage;

import org.litepal.LitePal;

import java.util.List;

//import android.support.v7.app.AppCompatActivity;

public class First_activity extends AppCompatActivity {
    private ListView listview;
    private DBmanage dBmanage;
    private MyAdapter myAdapter;
    /**列表的数据源*/
    private List<CityList> cityLists;
    private Button home;
    private Button add;
    private Button search1;
    private EditText idcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        home=(Button)findViewById(R.id.home);
        add=(Button)findViewById(R.id.add);
        search1 = findViewById(R.id.search1);
        idcode = findViewById(R.id.idCode);
        dBmanage=new DBmanage();
        LitePal.getDatabase();
        listview = (ListView) findViewById(R.id.listview); //listview
        cityLists=dBmanage.getAllCity(); //返回所有数据项
        myAdapter = new MyAdapter(getApplicationContext(),cityLists);//适配数据项
        listview.setAdapter(myAdapter); //显示数据项
        //监听listview的长按事件
        //监听listview的长按事件
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                new AlertDialog.Builder(First_activity.this)
                        .setTitle("提醒")
                        .setMessage("你确定要删除么？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        // TODO Auto-generated method stub
                                        dBmanage.deleteCity(cityLists.get(position).getId());
                                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(First_activity.this).edit();
                                        editor.remove(cityLists.get(position).getCityId());
                                        editor.apply();
                                        cityLists=dBmanage.getAllCity();
//                                        Intent intent=new Intent(First_activity.this,First_activity.class);
//                                        startActivity(intent);
//                                        First_activity.this.finish();
                                        myAdapter = new MyAdapter(getApplicationContext(),cityLists);//适配数据项
                                        listview.setAdapter(myAdapter);
                                    }
                                }).show();

                return true;
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { //打开旧的编辑
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(First_activity.this,WeatherActivity.class);
                intent.putExtra("city_code",cityLists.get(position).getCityId());
                startActivity(intent);
                //First_activity.this.finish();
            }
        });

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String City_Code = String.valueOf(idcode.getText());
                if(City_Code.length() > 9 || City_Code.length() < 9){
                    Toast.makeText(First_activity.this, "城市代码长短错误", Toast.LENGTH_LONG).show();
                    //城市代码正常，把城市代码通过intent传到weatherActivity.class
                }
                else{
                    Intent intent = new Intent(First_activity.this,WeatherActivity.class);
                    intent.putExtra("city_code",City_Code);
                    startActivity(intent);
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //新建
                Intent intent =new Intent(First_activity.this,MainActivity.class);
                startActivity(intent);
                //First_activity.this.finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(First_activity.this,Second_activity.class);
                startActivity(intent);
            }
        });
    }
    //重写Resume方法，在回到此页面时保证刷新
        protected void onResume() {
        super.onResume();
        // 刷新
        cityLists=dBmanage.getAllCity();
        myAdapter = new MyAdapter(getApplicationContext(),cityLists);
        listview.setAdapter(myAdapter);
    }
    /**自定义listview的适配器*/
    class MyAdapter extends BaseAdapter{
        List<CityList> cityLists;
        LayoutInflater inflater;
        public MyAdapter(Context context,List<CityList> cityLists){
            this.cityLists=cityLists;
            inflater=LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return cityLists.size();
        }
        @Override
        public Object getItem(int arg0) {
            return cityLists.get(arg0);
        } //得到数据项
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }//得到数据项下标
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null){
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                viewHolder.cityName = (TextView) convertView.findViewById(R.id.cityName);
                viewHolder.cityDegree=(TextView) convertView.findViewById(R.id.cityDegree);
                viewHolder.cityDate = (TextView) convertView.findViewById(R.id.cityDate);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.cityName.setText(cityLists.get(position).getCityName());
            Log.d("likunpeng",cityLists.get(position).getCityName());
            viewHolder.cityDegree.setText(cityLists.get(position).getCityDegree());//得到时间
            viewHolder.cityDate.setText(cityLists.get(position).getCityDate());
            return convertView;
        }
        class ViewHolder{
            TextView cityName;
            TextView cityDegree;
            TextView cityDate;
        }
    }
}


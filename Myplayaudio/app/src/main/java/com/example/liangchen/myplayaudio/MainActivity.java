package com.example.liangchen.myplayaudio;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
//import android.support.v7.app.ActionBar;
import androidx.appcompat.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private LinkedList<Music> musicList = new LinkedList<>();
    Thread thread;
    boolean exit;
    int size;
    int num;
    int mode;
    int temp;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    Button play;
    Button stop;
    TextView mark;
    Button Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mode=1;
        temp=0;
        play = findViewById(R.id.play);
        mark = findViewById(R.id.mark);
        seekBar = findViewById(R.id.seekBar);
        Back = findViewById(R.id.Back);
        seekBar.setOnSeekBarChangeListener(this);
        mediaPlayer = new MediaPlayer();
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            ((ActionBar) actionbar).hide();
        }
        searchMusic();
        try {
            mediaPlayer.setDataSource("/sdcard/Download/" + musicList.get(0).getName());
            mediaPlayer.prepare();
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(0);
            exit = false;
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        while (exit) {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            if (mediaPlayer.getCurrentPosition() >= mediaPlayer.getDuration()-500) {
                                Log.d("MainActivity",(++temp)+"");
                                playOther(mode);
                            }
                        }
                    }
                }
            });
            thread.start();
        } catch (Exception e) {
            Log.d("MainActivity", e.toString());
        }
        MusicAdapter adapter = new MusicAdapter(MainActivity.this, R.layout.music_item, musicList);
        ListView listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mediaPlayer.seekTo(seekBar.getProgress());
        exit = true;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        exit = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("MainActivity", "onItemClick" + i);
        try {
            exit = false;
            mediaPlayer.stop();
            mediaPlayer.release();
            num = i;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource("/sdcard/Download/" + musicList.get(i).getName());
            mediaPlayer.prepare();
            mediaPlayer.start();
            play.setText("暂停");
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(0);
            mark.setText("/sdcard/Download/" + musicList.get(i).getName());
            exit = true;
        } catch (Exception e) {
            Log.d("MainActivity", "asdasd"+e.toString());
        }
    }

    private void playOther(int temp) {
        try {
            exit = false;
            mediaPlayer.stop();
            mediaPlayer.release();
            num += size;
            if (temp < 0) {
                num--;
            } else if(temp>0){
                num++;
            }
            num %= size;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource("/sdcard/Download/" + musicList.get(num).getName());
            mediaPlayer.prepare();
            mediaPlayer.start();
            play.setText("暂停");
            mark.setText("/sdcard/Download/" + musicList.get(num).getName());
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(0);
            exit = true;
        } catch (Exception e) {
            Log.d("MainActivity","|"+ e.toString()+"|");    }
    }

    private void searchMusic() {
        musicList = new LinkedList<>();
        String result = "";
        File[] files = new File("/sdcard/Download").listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(".mp3")) {
                    Log.d("MainActivity", "|" + file.getPath() + "|");
                    result = file.getPath().split("/")[file.getPath().split("/").length - 1];
                    musicList.addLast(new Music(result));
                }
            }
            size = musicList.size();
        }
    }
    public void initMediaPlayer(){
        try{
            exit=false;
            num = 0;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource("/sdcard/Download/" + musicList.get(0).getName());
            mediaPlayer.prepare();
            play.setText("播放");
            seekBar.setMax(mediaPlayer.getDuration());
            seekBar.setProgress(0);
        }catch (Exception e){

        }
    }
    public void onClick(View v) {
        Log.d("MainActivity", "view:" + v);
        switch (v.getId()) {
            case R.id.Back:
                finish();
                break;
            case R.id.play:
                if ("播放".equals(play.getText()) && !mediaPlayer.isPlaying()) {
                    exit = true;
                    mediaPlayer.start();
                    mark.setText("/sdcard/Download/" + musicList.get(num).getName());
                    play.setText("暂停");
                } else if ("暂停".equals(play.getText()) && mediaPlayer.isPlaying()) {
                    exit = false;
                    mediaPlayer.pause();
                    mark.setText("/sdcard/Download/" + musicList.get(num).getName());
                    play.setText("播放");
                }
                break;
            case R.id.UpMusic:
                playOther(-1);
                break;
            case R.id.DownMusic:
                playOther(1);
                break;
            case R.id.stop:
                exit = false;
                mark.setText("");
                mediaPlayer.stop();
                mediaPlayer.release();
              initMediaPlayer();
                break;
            case R.id.Mode:
                if(mode==1){
                    mode=0;
                    Toast.makeText(this,"单曲循环", Toast.LENGTH_SHORT).show();
                }else  if(mode==0){
                    mode=1;
                    Toast.makeText(this,"列表循环", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}

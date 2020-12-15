package com.example.liangchen.myplayaudio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class MusicAdapter extends ArrayAdapter<Music> {
    private  int resourceId;

    public  MusicAdapter(Context context, int textViewResourceId, List<Music> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        Music music =getItem(position);
        View view =LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView Name=(TextView) view.findViewById(R.id.Name);
        Name.setText(music.getName());
        return view;
    }
}

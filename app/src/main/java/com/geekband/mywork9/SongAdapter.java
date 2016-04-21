package com.geekband.mywork9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Hyper on 2016/4/1.
 */
public class SongAdapter extends ArrayAdapter<SongInfo> {
    public SongAdapter(Context context, int textViewResourceId, List<SongInfo> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public SongInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SongInfo songInfo=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view=LayoutInflater.from(getContext()).inflate(R.layout.song_item,null);
            viewHolder=new ViewHolder();
            viewHolder.songtitle=(TextView) view.findViewById(R.id.title_item_text_view);
            viewHolder.songsinger=(TextView) view.findViewById(R.id.singer_item_text_view);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.songtitle.setText(songInfo.getTitle());
        viewHolder.songsinger.setText(songInfo.getSinger());
        return view;
    }

    class ViewHolder {
        TextView songtitle;
        TextView songsinger;
    }
}

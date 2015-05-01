package com.courseworktracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TerryS on 3/10/15.
 */
public class CustomAdapter extends BaseAdapter {

    List<String> title, data;

    private LayoutInflater inflater = null;

    public CustomAdapter(Context context){
        title = null;
        data = null;
    }

    public CustomAdapter(Context context, List<String> title, List<String> data){
        this.inflater = LayoutInflater.from(context);
        this.title = title;
        this.data = data;
    }

    @Override
    public int getCount() {
        if (title == null)
            return 0;
        else
            return title.size();
    }

    @Override
    public Object getItem(int i) {
        return this.title.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row;
        row = inflater.inflate(R.layout.list_row_2cols, viewGroup, false);
        TextView text_title = (TextView)row.findViewById(R.id.name);
        TextView text_data = (TextView)row.findViewById(R.id.record);
        text_title.setText(title.get(i));
        text_data.setText(data.get(i));

        return row;
    }
}
package com.courseworktracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by TerryS on 3/10/15.
 */
public class CustomAdapter extends BaseAdapter {

    // TODO implement methods
    ArrayList<String> title, data;

    public CustomAdapter(){
        title = null;
        data = null;
    }

    public CustomAdapter(ArrayList<String> title, ArrayList<String> data){
        this.title = title;
        this.data = data;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row;
        row = inflater.inflate(R.layout.list_row_2cols, viewGroup, false);
        TextView text_title = (TextView)row.findViewById(R.id.name);
        TextView text_data = (TextView)row.findViewById(R.id.record);
        text_data.setText(title.get(i));
        text_data.setText(data.get(i));

        return row;
    }
}

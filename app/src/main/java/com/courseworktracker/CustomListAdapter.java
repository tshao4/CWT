package com.courseworktracker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jasonzhong on 2/20/15.
 */
public class CustomListAdapter extends BaseAdapter {
    private ArrayList<OverviewItem> listData;
    private LayoutInflater layoutInflater;

    public CustomListAdapter(Context aContext, ArrayList<OverviewItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.overview_row_layout, null);
            holder = new ViewHolder();
            holder.category = (TextView) convertView.findViewById(R.id.category);
            holder.description = (TextView) convertView.findViewById(R.id.description);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(convertView.getTag() == null){
            Log.i("12", "12");

        }
        holder.category.setText(listData.get(position).getMainItem());
        holder.description.setText(listData.get(position).getSubItem());
        holder.date.setText(listData.get(position).getDate());
        return convertView;
    }

    static class ViewHolder {
        TextView category;
        TextView description;
        TextView date;
    }
}

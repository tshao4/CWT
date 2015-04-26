package com.courseworktracker;

import android.app.Activity;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;


/**
 * Created by jasonzhong on 4/25/15.
 */
public class OverviewFragment extends Fragment {
    private ListView overview;
    private ArrayAdapter adapter;

    public OverviewFragment(){

    }

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        overview = (ListView) getActivity().findViewById(R.id.listView_overview);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();


        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        ArrayList items = getListData();
        CustomListAdapter arrayAdapter;
        arrayAdapter = new CustomListAdapter(getActivity().getApplicationContext(), items);
        overview = (ListView)v.findViewById(R.id.listView_overview);
        overview.setAdapter(arrayAdapter);

        return v;
    }


    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
    */

    private ArrayList getListData(){
        ArrayList<OverviewItem> results = new ArrayList<OverviewItem>();
        String[] cats = getResources().getStringArray(R.array.overview);
        for(int i = 0; i < 4; i++){
            OverviewItem newItem = new OverviewItem(cats[i],"2","3");
            results.add(newItem);
        }
        return results;
    }
}


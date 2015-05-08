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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by jasonzhong on 4/25/15.
 */

public class OverviewFragment extends Fragment {
    private ListView overview;
    private ArrayAdapter adapter;


    private DBManager dbm;

    public OverviewFragment(){

    }

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dbm = new DBManager(activity);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();


        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        dbm.open();
        List<CourseWork> assignments = dbm.getAllAssingments();
        dbm.close();
        int count = assignments.size();

        List<List> lists = new ArrayList<List>(2);
        lists.add(new ArrayList<String>());
        lists.add(new ArrayList<Integer>());

        for (int i = 0; i < count; i++) {
            lists.get(0).add(assignments.get(i).getAname());
            lists.get(1).add(assignments.get(i).getDuedate());
        }

        List<List<String>> newList = CourseWork.getFormDate(lists);

        CustomAdapter adapter = new CustomAdapter(v.getContext(), newList.get(0), newList.get(1));
        ListView assignList = (ListView) v.findViewById(R.id.listView_upcoming);
        assignList.setAdapter(adapter);

        dbm.open();
        List info = dbm.OverviewInfo();
        dbm.close();
        TextView gpa_txt = (TextView) v.findViewById(R.id.textview_overview_gpa_disp);
        double[] credits = (double[]) info.get(0);
        Double gpa = credits[1]/credits[0];
        String Gpa = gpa.toString();
        if(Gpa.length()>5) {
            Gpa = Gpa.substring(0, 5);
        }
        gpa_txt.setText(Gpa);

        TextView credit_txt = (TextView) v.findViewById(R.id.textview_overview_credits_disp);
        credit_txt.setText("" + (int)credits[2]);

        List<String> gen_ed = new ArrayList<String>();
        boolean[] gen = (boolean[]) info.get(1);
        List<String> gen_ed_condi = new ArrayList<String>();

        String[] items = getResources().getStringArray(R.array.gen_ed);
        for(int i = 2; i<6; i++){
            gen_ed.add(items[i]);
            if(gen[i-2]){
                gen_ed_condi.add("Completed");
            } else{
                gen_ed_condi.add("Incomplete");
            }
        }
        adapter = new CustomAdapter(v.getContext(),gen_ed,gen_ed_condi);
        ListView gen_lv = (ListView) v.findViewById(R.id.listView_general);
        gen_lv.setAdapter(adapter);


        items = getResources().getStringArray(R.array.breadth);
        int[] breadth = (int[]) info.get(2);
        List<String> breadth_name = new ArrayList<String>();
        List<String> breadth_cre = new ArrayList<String>();
        for(int i = 1; i< 8; i++){
            breadth_name.add(items[i]);
            breadth_cre.add("" + breadth[i-1]);
        }
        adapter = new CustomAdapter(v.getContext(),breadth_name,breadth_cre);
        ListView breadth_lv = (ListView) v.findViewById(R.id.listView_breadth);
        breadth_lv.setAdapter(adapter);

        return v;
    }


    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
    */


}


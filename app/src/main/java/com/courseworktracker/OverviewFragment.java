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
        CourseWork ex = new CourseWork("faf","fsaf", 20150512);
        assignments.add(ex);
        Calendar currentDate = Calendar.getInstance();
        Calendar dueDate = Calendar.getInstance();
        ArrayList<String> coursework = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();

        int count = assignments.size();
        while(count > 0){
            if(assignments.get(count-1) != null){
                int due = assignments.get(count-1).getDuedate();
                int day = due%100;
                int month = (due - day)%10000/100;
                int year = (due - month*100 - day)/10000;
                dueDate.set(Calendar.YEAR, year);
                dueDate.set(Calendar.MONTH, month-1);
                dueDate.set(Calendar.DAY_OF_MONTH, day);

                long diff = dueDate.getTimeInMillis() - currentDate.getTimeInMillis();

                int days = (int) diff / (24 * 60 * 60 * 1000);
                String dueIn = days + "D";
                coursework.add(assignments.get(count-1).getAname());
                date.add(dueIn);
            }
            count--;
        }
        CustomAdapter adapter = new CustomAdapter(v.getContext(), coursework, date);
        ListView assignList = (ListView) v.findViewById(R.id.listView_upcoming);
        assignList.setAdapter(adapter);

        dbm.open();
        double[] credits = dbm.Credits();
        dbm.close();
        TextView gpa_txt = (TextView) v.findViewById(R.id.textview_overview_gpa_disp);
        Double gpa = credits[1]/credits[0];
        String Gpa = gpa.toString();
        if(Gpa.length()>5) {
            Gpa = Gpa.substring(0, 5);
        }
        gpa_txt.setText(Gpa);

        TextView credit_txt = (TextView) v.findViewById(R.id.textview_overview_credits_disp);
        credit_txt.setText("" + (int)credits[0]);

        List<String> gen_ed = new ArrayList<String>();

        String[] items = getResources().getStringArray(R.array.gen_ed);
        for(int i = 2; i<=6; i++){
            gen_ed.add(items[i]);
        }




        return v;
    }


    /*
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    }
    */


}


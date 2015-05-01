package com.courseworktracker;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by TerryS on 3/9/15.
 */
public class CourseWork {
    private String category;
    private String cname;
    private String aname;
    private String note;
    private double maxScore;
    private double gotScore; // -1 if in progress
    private int duedate;

    public CourseWork(String cname, String aname, int duedate){
    //  this.category = category;
        this.cname = cname;
        this.aname = aname;
    //  this.note = note;
    //  this.maxScore = maxScore;
    //  this.gotScore = gotScore;
        this.duedate = duedate;
    }

    public static List<List<String>> getFormDate(List<List> list){

        List<List<String>> newList = new ArrayList<List<String>>();

        Calendar cal = Calendar.getInstance();
        Calendar calDue = Calendar.getInstance();

        newList.add(list.get(0));
        newList.add(new ArrayList<String>());

        int counter = 0;

        int curDate = cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < list.get(0).size(); i++){
            int dueDate = ((Integer)list.get(1).get(i)).intValue();

            if (dueDate < curDate) {
                counter++;
            }
            else{
                int day = dueDate%100;
                int month = (dueDate%10000 - day)/100;
                int year = (dueDate - month * 100 - day)/10000;
                calDue.set(Calendar.YEAR, year);
                calDue.set(Calendar.MONTH, month - 1);
                calDue.set(Calendar.DAY_OF_MONTH, day);

                long diff = calDue.getTimeInMillis() - cal.getTimeInMillis();

                long daydiff = diff / (24*60*60*1000);

                newList.get(1).add(daydiff + "D");
            }
        }

        for (;counter > 0; counter--) {
            newList.get(1).add("PassDue");
        }

        return newList;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public void setGotScore(double gotScore) {
        this.gotScore = gotScore;
    }

    public void setDuedate(int duedate) {
        this.duedate = duedate;
    }

    public String getCategory() {
        return category;
    }

    public String getCname() {
        return cname;
    }

    public String getAname() {
        return aname;
    }

    public String getNote() {
        return note;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public double getGotScore() {
        return gotScore;
    }

    public int getDuedate() {
        return duedate;
    }
}
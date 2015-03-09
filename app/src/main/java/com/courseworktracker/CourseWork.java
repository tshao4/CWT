package com.courseworktracker;

/**
 * Created by TerryS on 3/9/15.
 */
public class CourseWork {
    private String category;
    private String cwname;
    private String note;
    private double maxScore;
    private double gotScore; // -1 if in progress
    private long dDate;

    public CourseWork(String category, String cwname, String note,
                      double maxScore, double gotScore, long dDate){
        this.category = category;
        this.cwname = cwname;
        this.note = note;
        this.maxScore = maxScore;
        this.gotScore = gotScore;
        this.dDate = dDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCwname(String cwname) {
        this.cwname = cwname;
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

    public void setdDate(long dDate) {
        this.dDate = dDate;
    }

    public String getCategory() {
        return category;
    }

    public String getCwname() {
        return cwname;
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

    public long getdDate() {
        return dDate;
    }
}
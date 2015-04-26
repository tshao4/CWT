package com.courseworktracker;

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
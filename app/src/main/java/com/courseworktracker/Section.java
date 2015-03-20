package com.courseworktracker;

/**
 * Created by TerryS on 3/9/15.
 */
public class Section {
    private String sname;
    private String schedule;
    private String location;

    public Section(String sname, String schedule, String location){
        this.sname = sname;
        this.schedule = schedule;
        this.location = location;
    }

    public void setSname(String sname){
        this.sname = sname;
    }

    public void setSchedule(String schedule){
        this.schedule = schedule;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getSname(){
        return sname;
    }

    public String getSchedule(){
        return schedule;
    }

    public String getLocation(){
        return location;
    }
}

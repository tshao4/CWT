package com.courseworktracker;

/**
 * Created by jasonzhong on 2/20/15.
 */
public class OverviewItem {
    private String mainItem;
    private String subItem;
    private String date;

    public OverviewItem(String i, String x, String z){
        this.mainItem = i;
        this.subItem = x;
        this.date = z;
    }

    public String getMainItem(){
        return mainItem;
    }

    public String getSubItem(){
        return subItem;
    }

    public String getDate(){
        return date;
    }
    public void setMainItem(String MainItem){
        this.mainItem = MainItem;
    }

    public void setSubItem(String SubItem){
        this.subItem = SubItem;
    }

    public void setDate(String Date){
        this.date = Date;
    }
}

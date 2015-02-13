package com.courseworktracker;

/**
 * Created by TerryS on 2/13/15.
 */
public class Course {
    private String cname;
    private int credit;
    private String grade;
    private String breadth;
    private String gen_ed;

    public Course(String cname, int credit, String grade, String breadth, String gen_ed){
        this.cname = cname;
        this.credit = credit;
        this.grade = grade;
        this.breadth = breadth;
        this.gen_ed = gen_ed;
    }

    public Course(){}

    public void setCname(String cname){
        this.cname = cname;
    }

    public void setCredit(int credit){
        this.credit = credit;
    }

    public void setGrade(String grade){
        this.grade = grade;
    }

    public void setBreadth(String breadth){
        this.breadth = breadth;
    }

    public void setGen_ed(String gen_ed){
        this.gen_ed = gen_ed;
    }

    public String getCname(){
        return cname;
    }

    public int getCredit(){
        return credit;
    }

    public String getGrade(){
        return grade;
    }

    public String getBreadth(){
        return breadth;
    }

    public String getGen_ed(){
        return gen_ed;
    }
}

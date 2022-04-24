package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems26 {
    public String id,date,name,intime,outtime;
    ArrayList<ClassListItems26> arraylist;

    public ClassListItems26(String id, String date, String name, String intime, String outtime)
    {
        this.id=id;
        this.date=date;
        this.name = name;
        this.intime=intime;
        this.outtime=outtime;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getIntime() {
        return intime;
    }

    public String getOuttime() {
        return outtime;
    }

}

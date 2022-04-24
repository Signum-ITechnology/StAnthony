package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems9 {
    public String id,roll,name,status,srno,date;
    ArrayList<ClassListItems9> arraylist;

    public ClassListItems9(String id, String srno, String roll, String name, String status, String date)
    {
        this.id = id;
        this.srno = srno;
        this.roll = roll;
        this.name = name;
        this.status = status;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getSrno() {
        return srno;
    }

    public String getDate() {
        return date;
    }

}

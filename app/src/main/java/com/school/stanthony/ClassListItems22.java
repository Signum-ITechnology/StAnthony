package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems22 {
    public String id,date,time,std,subject,desc,code,pass,active;
    ArrayList<ClassListItems9> arraylist;

    public ClassListItems22(String id, String date, String time, String std, String subject, String desc, String code, String pass, String active)
    {
        this.id = id;
        this.date = date;
        this.time = time;
        this.std=std;
        this.subject=subject;
        this.desc = desc;
        this.code = code;
        this.pass=pass;
        this.active=active;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStd() {
        return std;
    }

    public String getSubject() {
        return subject;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }

    public String getPass() {
        return pass;
    }

    public String getActive() {
        return active;
    }


}

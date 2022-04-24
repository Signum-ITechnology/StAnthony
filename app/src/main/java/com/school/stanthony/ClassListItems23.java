package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems23 {
    public String date,time,subject,desc,code,pass,active;
    ArrayList<ClassListItems9> arraylist;

    public ClassListItems23(String date, String time, String subject, String desc, String code, String pass, String active)
    {
        this.date = date;
        this.time = time;
        this.subject=subject;
        this.desc = desc;
        this.code = code;
        this.pass=pass;
        this.active=active;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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

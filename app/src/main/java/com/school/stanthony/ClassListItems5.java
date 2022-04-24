package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems5 {
    public String id,active,link,date,name,section,std,subject;
    ArrayList<ClassListItems5> arraylist;

    public ClassListItems5(String id,String active,String link,String date, String name, String section, String std, String subject)
    {
        this.id = id;
        this.active = active;
        this.link = link;
        this.date = date;
        this.name = name;
        this.section = section;
        this.std = std;
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public String getActive() {
        return active;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getSection() {
        return section;
    }

    public String getStd() {
        return std;
    }

    public String getSubject() {
        return subject;
    }

}

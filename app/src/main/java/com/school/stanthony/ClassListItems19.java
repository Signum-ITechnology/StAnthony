package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems19 {
    public String subject,date,time,endtime,questions,marks,starttime,checkdate;
    ArrayList<ClassListItems9> arraylist;

    public ClassListItems19(String subject, String date, String time, String questions, String endtime, String marks, String starttime, String checkdate)
    {
        this.subject = subject;
        this.date = date;
        this.time=time;
        this.endtime=endtime;
        this.questions=questions;
        this.marks=marks;
        this.starttime=starttime;
        this.checkdate=checkdate;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getEndtime() {
        return endtime;
    }

    public String getQuestions() {
        return questions;
    }

    public String getMarks() {
        return marks;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getCheckdate() {
        return checkdate;
    }

}

package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems20 {
    public String subject,date,time,questions,marks;
    ArrayList<ClassListItems9> arraylist;

    public ClassListItems20(String subject, String date, String time, String questions, String marks)
    {
        this.subject = subject;
        this.date = date;
        this.time=time;
        this.questions=questions;
        this.marks=marks;
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

    public String getQuestions() {
        return questions;
    }

    public String getMarks() {
        return marks;
    }

}

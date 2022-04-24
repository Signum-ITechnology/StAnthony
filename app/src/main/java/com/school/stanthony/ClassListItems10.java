package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems10 {
    public String date,day,subject,homework,topic,chapter,subdate;
    ArrayList<ClassListItems10> arraylist;

    public ClassListItems10(String date, String day, String subject, String homework, String topic, String chapter, String subdate)
    {
        this.date = date;
        this.day = day;
        this.subject = subject;
        this.homework = homework;
        this.topic = topic;
        this.chapter = chapter;
        this.subdate = subdate;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getSubject() {
        return subject;
    }

    public String getHomework() {
        return homework;
    }

    public String getTopic() {
        return topic;
    }

    public String getChapter() {
        return chapter;
    }

    public String getSubdate() {
        return subdate;
    }

}

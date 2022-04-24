package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems6 {
    public String id,date,day,section,std,div,subject,homework,subdate,topic,chapter,category,pdflink,videolink;
    ArrayList<ClassListItems6> arraylist;

    public ClassListItems6(String id, String date,String day,String section, String std, String div, String subject,String topic,String chapter, String homework ,String subdate ,String category,String pdflink,String videolink)
    {
        this.id=id;
        this.date=date;
        this.day=day;
        this.section = section;
        this.std = std;
        this.div = div;
        this.subject = subject;
        this.topic=topic;
        this.chapter=chapter;
        this.homework = homework;
        this.subdate = subdate;
        this.category = category;
        this.pdflink = pdflink;
        this.videolink = videolink;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getSection() {
        return section;
    }

    public String getStd() {
        return std;
    }

    public String getDiv() {
        return div;
    }

    public String getSubject() {
        return subject;
    }

    public String getTopic() {
        return topic;
    }

    public String getChapter() {
        return chapter;
    }

    public String getHomework() {
        return homework;
    }

    public String getSubdate() {
        return subdate;
    }

    public String getCategory() {
        return category;
    }

    public String getPdflink() {
        return pdflink;
    }

    public String getVideolink() {
        return videolink;
    }

}

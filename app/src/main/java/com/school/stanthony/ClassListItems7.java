package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems7 {
    public String date,day,topic,chapter,subdate,link,actuallink,upload,hw;
    ArrayList<ClassListItems7> arraylist;

    public ClassListItems7(String date, String day, String topic, String chapter, String subdate, String link,String actuallink,String upload,String hw)
    {
        this.date = date;
        this.day = day;
        this.topic=topic;
        this.chapter=chapter;
        this.subdate=subdate;
        this.link=link;
        this.actuallink=actuallink;
        this.upload=upload;
        this.hw=hw;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
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

    public String getLink() {
        return link;
    }

    public String getActuallink() {
        return actuallink;
    }

    public String getUpload() {
        return upload;
    }

    public String getHw() {
        return hw;
    }

}

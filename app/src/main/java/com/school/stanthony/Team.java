package com.school.stanthony;

public class Team {
    private  String title;
    private String desc;
    private String desc2;
    private  int image;

    public Team(String title, String desc, String desc2 , int image) {
        this.title = title;
        this.desc = desc;
        this.desc2 = desc2;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getDesc2() {
        return desc2;
    }

    public int getImage() {
        return image;
    }
}
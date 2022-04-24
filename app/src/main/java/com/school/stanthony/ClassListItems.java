package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems {
    public String img;
    public String name,roll,std,code,contact;
    ArrayList<ClassListItems> arraylist;

    public ClassListItems(String name, String img, String roll, String std, String code, String contact)
    {
        this.img = img;
        this.name = name;
        this.std=std;
        this.roll=roll;
        this.code=code;
        this.contact=contact;
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getStd() {
        return std;
    }

    public String getRoll() {
        return roll;
    }

    public String getCode() {
        return code;
    }

    public String getContact() {
        return contact;
    }

}

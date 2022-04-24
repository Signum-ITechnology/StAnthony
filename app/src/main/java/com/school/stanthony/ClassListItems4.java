package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems4 {
    public String url,name,department,designation,contact;
    ArrayList<ClassListItems> arraylist;

    public ClassListItems4(String url, String name, String department, String designation,String contact)
    {
        this.url = url;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.contact = contact;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public String getContact() {
        return contact;
    }

}

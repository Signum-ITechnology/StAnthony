package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems24 {
    public String id,date,hodstatus,pristatus,type,leavedate;
    ArrayList<ClassListItems24> arraylist;

    public ClassListItems24(String id, String date, String hodstatus, String pristatus, String type, String leavedate)
    {
        this.id=id;
        this.date = date;
        this.hodstatus = hodstatus;
        this.pristatus = pristatus;
        this.type = type;
        this.leavedate = leavedate;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getHodstatus() {
        return hodstatus;
    }

    public String getPristatus() {
        return pristatus;
    }

    public String getType() {
        return type;
    }

    public String getLeavedate() {
        return leavedate;
    }

}

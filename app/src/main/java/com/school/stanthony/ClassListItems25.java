package com.school.stanthony;

import java.util.ArrayList;

public class ClassListItems25 {
    public String date,status,type,leavedate,days,reason;
    ArrayList<ClassListItems25> arraylist;

    public ClassListItems25(String date, String status, String type, String leavedate, String days, String reason)
    {
        this.date = date;
        this.status = status;
        this.type = type;
        this.leavedate = leavedate;
        this.days = days;
        this.reason = reason;
    }


    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getLeavedate() {
        return leavedate;
    }

    public String getDays() {
        return days;
    }

    public String getReason() {
        return reason;
    }

}

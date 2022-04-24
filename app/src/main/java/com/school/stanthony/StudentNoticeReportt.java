package com.school.stanthony;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentNoticeReportt extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "",count,section,std,div;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;

    final Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            StudentNoticeReportt mydata = new StudentNoticeReportt();
            MyData = mydata.replacetoast();
            String[] fromwhere = {"issuedate","section","class","name","subject","notice","created_by"};
            int[] viewswhere = {R.id.date,R.id.section,R.id.std,R.id.stuname,R.id.subject,R.id.notice,R.id.name};
            adapter = new SimpleAdapter(StudentNoticeReportt.this, MyData, R.layout.noticelist, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }
    };

    final Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            StudentNoticeReportt mydata = new StudentNoticeReportt();
            MyData = mydata.replacetoast1(section,std,div);
            String[] fromwhere = {"issuedate","section","class","name","subject","notice","created_by"};
            int[] viewswhere = {R.id.date,R.id.section,R.id.std,R.id.stuname,R.id.subject,R.id.notice,R.id.name};
            adapter = new SimpleAdapter(StudentNoticeReportt.this, MyData, R.layout.noticelist, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView=findViewById(R.id.notice);
        section=getIntent().getExtras().getString("section");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");

        if(section.equals("YYY")){
            mainHandler.post(myRunnable);
        }else{
            mainHandler1.post(myRunnable1);
        }
    }

    public List<Map<String,String>> replacetoast() {
        List<Map<String, String>> data ;
        data = new ArrayList<>();
        try
        {
            ConnectionHelper conStr=new ConnectionHelper();
            conn =conStr.connectionclasss();
            if (conn == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                String query = "select CONVERT(varchar(10),issuedate,103)issuedate,section,std+'/'+div 'class',\n" +
                        "Student_Code 'Name',subject,notice,issuedate dd,created_by from tblstudentnotice where Student_Code='ALL' and \n" +
                        "acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster) \n" +
                        "union all\n" +
                        "select CONVERT(varchar(10),issuedate,103)issuedate,section,std+'/'+div 'class',\n" +
                        "b.Name+' '+b.SurName 'Name',subject,notice,issuedate dd,A.created_by  \n" +
                        "from tblstudentnotice a,tbladmissionfeemaster b where Student_Code!='ALL'\n" +
                        " and a.acadmic_year=b.acadmic_year and a.Student_Code=b.applicant_type and b.applicant_type!='NEW' and\n" +
                        " a.acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster)  order by dd desc";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<>();
                    datanum.put("issuedate",rs.getString("issuedate"));
                    datanum.put("section",rs.getString("section"));
                    datanum.put("class",rs.getString("class"));
                    datanum.put("subject",rs.getString("subject"));
                    datanum.put("notice",rs.getString("notice"));
                    datanum.put("name",rs.getString("name"));
                    datanum.put("created_by",rs.getString("created_by"));
                    data.add(datanum);
                }
                ConnectionResult = "Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (Exception ex)
        {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return  data;
    }

    public List<Map<String,String>> replacetoast1(String section,String std,String div) {
        List<Map<String, String>> data ;
        data = new ArrayList<>();
        try
        {
            ConnectionHelper conStr=new ConnectionHelper();
            conn =conStr.connectionclasss();
            if (conn == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                String query = "select CONVERT(varchar(10),issuedate,103)issuedate,section,std+'/'+div 'class',\n" +
                        "Student_Code 'Name',subject,notice,issuedate dd,created_by from tblstudentnotice where Student_Code='ALL' and \n" +
                        "acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster) \n" +
                        "and section='"+section+"' and std='"+std+"' and div='"+div+"' \n" +
                        "union all\n" +
                        "select CONVERT(varchar(10),issuedate,103)issuedate,section,std+'/'+div 'class',\n" +
                        "b.Name+' '+b.SurName 'Name',subject,notice,issuedate dd,A.created_by  \n" +
                        "from tblstudentnotice a,tbladmissionfeemaster b where \n" +
                        "  a.acadmic_year=b.acadmic_year and a.Student_Code=b.applicant_type and b.applicant_type!='NEW' and\n" +
                        " a.acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster) \n" +
                        " and a.section='"+section+"' and a.std='"+std+"' and a.div='"+div+"'  order by dd desc";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<>();
                    datanum.put("issuedate",rs.getString("issuedate"));
                    datanum.put("section",rs.getString("section"));
                    datanum.put("class",rs.getString("class"));
                    datanum.put("subject",rs.getString("subject"));
                    datanum.put("notice",rs.getString("notice"));
                    datanum.put("name",rs.getString("name"));
                    datanum.put("created_by",rs.getString("created_by"));
                    data.add(datanum);
                }
                ConnectionResult = "Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (Exception ex)
        {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return  data;
    }

}
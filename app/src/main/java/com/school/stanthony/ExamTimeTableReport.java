package com.school.stanthony;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ExamTimeTableReport  extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "",Form1;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    String section,std,semester;
    SharedPreferences sharedPreferences;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            ExamTimeTableReport mydata = new ExamTimeTableReport();
            MyData = mydata.replacetoast(section,std,semester,Form1);
            String[] fromwhere = {"Row","Title","semester","ExamDate","ExamStartTime","ExamEndTime"};
            int[] viewswhere = {R.id.Row,R.id.title,R.id.semester,R.id.date,R.id.stime,R.id.etime};
            adapter = new SimpleAdapter(ExamTimeTableReport.this, MyData, R.layout.examtimetable, fromwhere, viewswhere);
            //       mainHandler.postDelayed(myRunnable,1000);
            listView.setAdapter(adapter);

            if(adapter.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ExamTimeTableReport.this);
                builder1.setTitle("No Record Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_time_tbl);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sharedPreferences = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        listView=findViewById(R.id.exam);
        semester=getIntent().getExtras().getString("sem");
        section=getIntent().getExtras().getString("section");
        std=getIntent().getExtras().getString("std");
        if(semester.equals("1st Unit")){
            semester="3";
        }else if(semester.equals("1st Semester")){
            semester="1";
        }else if(semester.equals("2nd Unit")){
            semester="4";
        }else if(semester.equals("2nd Semester")){
            semester="2";
        }

        mainHandler.post(myRunnable);
    }

    public List<Map<String,String>> replacetoast(String section,String std,String semester,String Form1 ) {
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
                String query= "select ROW_NUMBER() OVER (ORDER BY ExamDate)  AS Row, case when semester=1 then 'Sem 1' when semester=2 then 'Sem 2' when semester=3 then 'Unit 1' when semester=4 then 'Unit 2'end as semester,\n" +
                        "Title,CONVERT(varchar(10),ExamDate,103)ExamDate,LTRIM(RIGHT(CONVERT(VARCHAR(20), ExamStartTime, 100), 7))as ExamStartTime,\n" +
                        "LTRIM(RIGHT(CONVERT(VARCHAR(20), ExamEndTime, 100), 7))as ExamEndTime from tblExamtimetablemaster\n" +
                        " where batchcode='"+section+"'and class_name='"+std+"'  and semester ='"+semester+"'\n" +
                        " and acadmic_year=(select TOP(1) selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<String,String>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("Title",rs.getString("Title"));
                    datanum.put("semester",rs.getString("semester"));
                    datanum.put("ExamDate",rs.getString("ExamDate"));
                    datanum.put("ExamStartTime",rs.getString("ExamStartTime"));
                    datanum.put("ExamEndTime",rs.getString("ExamEndTime"));
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
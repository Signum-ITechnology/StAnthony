package com.school.stanthony;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BirthdayRegisterReport extends AppCompatActivity {

    ListView listView;
    SimpleAdapter adapter;
    Connection conn;
    String ConnectionResult = "",Form1;
    Boolean isSuccess = false;
    String decide,month,date,batch,std,div;
    SharedPreferences sharedPreferences;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            BirthdayRegisterReport mydata = new BirthdayRegisterReport();
            MyData = mydata.replacetoast(month,date,Form1);
            String[] fromwhere = {"sr","name","rollno","std","dob","con",};
            int[] viewswhere = {R.id.no, R.id.name, R.id.rollno, R.id.std, R.id.dob, R.id.contact};
            adapter = new SimpleAdapter(BirthdayRegisterReport.this, MyData, R.layout.birthlayout, fromwhere, viewswhere);
            listView.setAdapter(adapter);

            if (adapter.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(BirthdayRegisterReport.this);
                builder1.setTitle("Record Not Found");
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

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData;
            BirthdayRegisterReport mydata1 = new BirthdayRegisterReport();
            MyData = mydata1.replacetoast1(month,date,batch,std,div,Form1);
            String[] fromwhere1 = {"sr","name","rollno","std","dob","con",};
            int[] viewswhere1 = {R.id.no, R.id.name, R.id.rollno, R.id.std, R.id.dob, R.id.contact};
            adapter = new SimpleAdapter(BirthdayRegisterReport.this, MyData, R.layout.birthlayout, fromwhere1, viewswhere1);
            listView.setAdapter(adapter);

            if (adapter.getCount()==0){
                Toast.makeText(BirthdayRegisterReport.this, "No Record Found", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_register_report);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sharedPreferences = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        listView=findViewById(R.id.list);
        decide=getIntent().getExtras().getString("sec");
        batch=getIntent().getExtras().getString("sec");
        date=getIntent().getExtras().getString("date");
        month=getIntent().getExtras().getString("month");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");

        if(decide.equals("YYY")){
            mainHandler.post(myRunnable);
        }else {
            mainHandler1.post(myRunnable1);
        }
    }

    public List<Map<String, String>> replacetoast(String month,String date,String Form1) {
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select ROW_NUMBER() over( order by a.birth_Date) as Sr, convert(varchar,a.Student_Code) as code,a.Roll_Number as [RollNo],\n" +
                        "a.gr_number,a.SurName+' '+a.Name+' '+a.Father_Name as Name,d.batch_for,c.class_Name,d.batchcode,roman_keyword+'/'+b.division as Standard,\n" +
                        "b.division,convert(varchar(10),birth_Date, 103) 'Birth Date', CONVERT(varchar,a.Self_Moblie_Number) as ContactNo\n" +
                        "FROM tblstudentmaster a,tbladmissionfeemaster b,tblclassmaster c,tblbatchmaster d where lower(b.applicant_type)!= 'new'\n" +
                        " and a.student_code=b.applicant_type and \n" +
                        "b.Acadmic_Year=c.Acadmic_Year and b.class_name=d.batch_for and b.class_name=c.class_name and isnull(b.iscancelled,0)=0 and\n" +
                        "c.Acadmic_year=d.Acadmic_Year and b.Division=c.division\n" +
                        "and month(birth_date)='"+month+"' and b.acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and day(birth_date)='"+date+"'\n" +
                        "order by birth_Date";
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("sr", rs.getString("sr"));
                    datanum.put("name", rs.getString("name"));
                    datanum.put("rollno", rs.getString("RollNo"));
                    datanum.put("std", rs.getString("Standard"));
                    datanum.put("dob", rs.getString("Birth Date"));
                    datanum.put("con", rs.getString("ContactNo"));
                    data.add(datanum);
                }
                ConnectionResult = " successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }

    public List<Map<String, String>> replacetoast1(String month,String date,String batch,String std,String div,String Form1) {
        List<Map<String, String>> data = null;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select ROW_NUMBER() over( order by a.birth_Date) as Sr, convert(varchar,a.Student_Code) as code,a.Roll_Number as [RollNo],\n" +
                        "a.gr_number,a.SurName+' '+a.Name+' '+a.Father_Name as Name,d.batch_for,c.class_Name,d.batchcode,roman_keyword+'/'+b.division as Standard,\n" +
                        "b.division,convert(varchar(10),birth_Date, 103) 'Birth Date', CONVERT(varchar,a.Self_Moblie_Number) as ContactNo\n" +
                        "FROM tblstudentmaster a,tbladmissionfeemaster b,tblclassmaster c,tblbatchmaster d where lower(b.applicant_type)!= 'new'\n" +
                        " and a.student_code=b.applicant_type and \n" +
                        "b.Acadmic_Year=c.Acadmic_Year and b.class_name=d.batch_for and b.class_name=c.class_name and isnull(b.iscancelled,0)=0 and\n" +
                        "c.Acadmic_year=d.Acadmic_Year and b.Division=c.division\n" +
                        "and month(birth_date)='"+month+"' and b.acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')  and day(birth_date)='"+date+"'\n" +
                        "and b.batch_code='"+batch+"' and b.class_name='"+std+"' and b.division='"+div+"'\n" +
                        "order by birth_Date";
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("sr", rs.getString("sr"));
                    datanum.put("name", rs.getString("name"));
                    datanum.put("rollno", rs.getString("RollNo"));
                    datanum.put("std", rs.getString("Standard"));
                    datanum.put("dob", rs.getString("Birth Date"));
                    datanum.put("con", rs.getString("ContactNo"));
                    data.add(datanum);
                }
                ConnectionResult = " successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return data;
    }
}

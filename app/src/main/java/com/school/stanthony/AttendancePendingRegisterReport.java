package com.school.stanthony;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AttendancePendingRegisterReport extends AppCompatActivity {

    ListView listView,listView2;
    SimpleAdapter adapter;
    Connection conn;
    String ConnectionResult;
    Boolean isSuccess;
    Bundle bundle;
    String section,std,div,contact,no,Form1;
    Spinner s1;
    PreparedStatement stmt;
    ResultSet rs;
    Button btn;
    SharedPreferences sharedPref;
    private static final int Request=1;

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            List<Map<String, String>> MyData = null;
            AttendancePendingRegisterReport mydata = new AttendancePendingRegisterReport();
            MyData = mydata.replacetoast1(section, std, div,Form1);
            String[] fromwhere = {"no", "class_Name", "Division", "AttendenceDate", "CurDate", "contactno", "name"};
            int[] viewswhere = {R.id.section, R.id.std, R.id.div, R.id.pending, R.id.till, R.id.contact, R.id.teacher};
            adapter = new SimpleAdapter(AttendancePendingRegisterReport.this, MyData, R.layout.pendingattendance, fromwhere, viewswhere);
            listView.setAdapter(adapter);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_pending_register_report);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        listView = findViewById(R.id.list);
        listView2 = findViewById(R.id.list2);
        s1=findViewById(R.id.s1);

        bundle = getIntent().getExtras();
        section = bundle.getString("section");
        std = bundle.getString("std");
        div = bundle.getString("div");
        btn=findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contact.equals("No Contact Available")){
                    Toast.makeText(AttendancePendingRegisterReport.this, "No Contact Number Found", Toast.LENGTH_SHORT).show();
                }
                else {
                    no=contact;
                    makephonecall();
                }
            }
        });

        ///////////////////spinner
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select mobile_no from tblteachermaster where teacher_Name=\n" +
                        "(select AssigneTeacher from tblClassMaster where \n" +
                        "BatchCode='"+section+"' and class_Name='"+std+"' and Division='"+div+"'" +
                        "and Acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"'))";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    contact = rs.getString("mobile_no");
                }

                if(contact.equals("")) {
                    contact = "No Contact Available";
                }

                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        btn.setText("CALL : "+contact);
        mainHandler1.post(myRunnable1);

    }

    public List<Map<String,String>> replacetoast1(String section,String std,String div,String Form1) {
        List<Map<String, String>> data = null;
        data = new ArrayList<>();

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();        // Connect to database
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {

                String query = "select Batch_Code,class_Name,Division,\n" +
                        "convert (varchar,(select max(AttendenceDate+1)),103)AttendenceDate,\n" +
                        "convert (varchar,SYSDATETIME(),103)CurDate,\n" +
                        "isnull((select mobile_no from tblteachermaster where teacher_Name=\n" +
                        "(select AssigneTeacher from tblClassMaster where \n" +
                        "BatchCode='"+section+"' and class_Name='"+std+"' and Division='"+div+"' and\n" +
                        "Acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"'))),'-')contactno,\n" +
                        "((select AssigneTeacher from tblClassMaster \n" +
                        "where BatchCode='"+section+"' and class_Name='"+std+"' and Division='"+div+"'and\n" +
                        "Acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') ))name\n" +
                        "from tblstudentAttendenceDetails \n" +
                        "where  Batch_Code='"+section+"' and class_Name='"+std+"' and Division='"+div+"' and\n" +
                        "Acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "group by  Batch_Code, class_Name, Division";
                Statement statement = conn.createStatement();
                ResultSet rt = statement.executeQuery(query);

                while (rt.next()) {
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("no", rt.getString("Batch_Code"));
                    datanum.put("class_Name", rt.getString("class_Name"));
                    datanum.put("Division", rt.getString("Division"));
                    datanum.put("AttendenceDate", rt.getString("AttendenceDate"));
                    datanum.put("CurDate", rt.getString("CurDate"));
                    datanum.put("contactno", rt.getString("contactno"));
                    datanum.put("name", rt.getString("name"));
                    data.add(datanum);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (Exception ex) {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }

        return  data;
    }

    private void makephonecall() {
        if (ContextCompat.checkSelfPermission(AttendancePendingRegisterReport.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(AttendancePendingRegisterReport.this, new String[]{Manifest.permission.CALL_PHONE}, Request);
        }
        else{
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + no)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Request){
            if(grantResults.length >=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makephonecall();
            }
        }
    }

}

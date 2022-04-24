package com.school.stanthony;

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

import androidx.appcompat.app.AppCompatActivity;

public class AttendancePendingRegisterYYY extends AppCompatActivity {

    ListView listView1;
    SimpleAdapter adapter,adapter1;
    Connection conn;
    String ConnectionResult,Form1;
    Boolean isSuccess;
    SharedPreferences sharedPref;
    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            AttendancePendingRegisterYYY mydata = new AttendancePendingRegisterYYY();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"Row","batch", "std", "div", "date","assigneteacher"};
            int[] viewswhere = {R.id.no,R.id.batch, R.id.std, R.id.div, R.id.date,R.id.dob};
            adapter = new SimpleAdapter(AttendancePendingRegisterYYY.this, MyData, R.layout.registeryyy, fromwhere, viewswhere);
            listView1.setAdapter(adapter);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_pending_register_yyy);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        listView1 = findViewById(R.id.list);

        mainHandler.post(myRunnable);

    }

    public List<Map<String,String>> replacetoast(String Form1) {
        List<Map<String, String>> data = null;
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
                String query= "select distinct(batch_code), ROW_NUMBER() OVER (ORDER BY class_id)  AS Row,a.class_name,a.division ,convert (varchar,(select max(AttendenceDate)),103)+' To '+\n" +
                        "convert (varchar,SYSDATETIME(),103)CurDate,assigneteacher,class_id from  tblstudentAttendenceDetails a,tblclassmaster b\n" +
                        "where a.Acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "and a.class_name!='select'\n" +
                        "and batch_code=batchcode and a.acadmic_year=b.acadmic_year \n" +
                        "and a.batch_code=b.batchcode\n" +
                        "and a.class_name=b.class_name and a.Division=b.Division\n" +
                        "group by  Batch_Code,a.class_Name, a.Division,assigneteacher,class_id \n" +
                        "having convert (varchar,(select max(AttendenceDate)),103)!=\n" +
                        "convert (varchar,SYSDATETIME(),103) ";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<String,String>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("batch",rs.getString("batch_code"));
                    datanum.put("std",rs.getString("class_name"));
                    datanum.put("div",rs.getString("division"));
                    datanum.put("date",rs.getString("CurDate"));
                    datanum.put("assigneteacher",rs.getString("assigneteacher"));
                    data.add(datanum);

                }
                ConnectionResult = " Successful";
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
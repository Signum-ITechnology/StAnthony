package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class StudentFeeReport extends AppCompatActivity {
    ListView listView;
    SimpleAdapter adapter;
    Connection conn;
    String ConnectionResult,Form1;
    Boolean isSuccess;
    Bundle bundle;
    String section,std,div,code;
    SharedPreferences sharedPref;
    Spinner s1;

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData;
            StudentFeeReport mydata = new StudentFeeReport();
            MyData = mydata.replacetoast1(section,std,div,code,Form1);
            String[] fromwhere = {"Roll_Number","fullname","std","Applicant_type","Contact_no","PendingFee"};
            int[] viewswhere = {R.id.rollno,R.id.name,R.id.std,R.id.code,R.id.contact,R.id.pending};
            adapter = new SimpleAdapter(StudentFeeReport.this, MyData, R.layout.studentfeereport, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fee_report);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        listView = findViewById(R.id.list);
        s1 = findViewById(R.id.s1);
        bundle = getIntent().getExtras();
        section = bundle.getString("section");
        std = bundle.getString("std");
        div = bundle.getString("div");
        code = bundle.getString("code");

        mainHandler1.post(myRunnable1);
    }

    public List<Map<String,String>> replacetoast1(String section,String std,String div,String code,String Form1) {
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
                String query = "select ROW_NUMBER() OVER (ORDER BY Applicant_type)  AS Row,Applicant_type,Roll_Number,\n" +
                        "Surname+' '+name as fullname,Class_Name+'/'+Division std,Contact_no,\n" +
                        "(select((select (total_fee-discount_fee)as Total_Fee from  tbladmissionfeemaster \n" +
                        "where Applicant_type='"+code+"' and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"'))-(Select isnull(SUM(Receipt_Amount),0) from tblfeemaster\n" +
                        "where Fee_Type in(3,9) and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_no='"+code+"')-(Select Fee_Paid from tbladmissionfeemaster where Applicant_type='"+code+"' and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') )+(Select isnull(SUM(latefee),0) from tblfeemaster\n" +
                        "where Fee_Type in(3,9) and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_no='"+code+"')) Balance\n" +
                        "from tbladmissionfeemaster\n" +
                        "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and Applicant_type='"+code+"')PendingFee\n" +
                        " from tbladmissionfeemaster where Batch_code ='"+section+"' and Class_Name='"+std+"'\n" +
                        "and Division='"+div+"' and Applicant_Type='"+code+"'";
                Statement statement=conn.createStatement();
                ResultSet rt=statement.executeQuery(query);

                while (rt.next()){
                    Map<String,String> datanum=new HashMap<>();
                    datanum.put("no",rt.getString("Row"));
                    datanum.put("Roll_Number",rt.getString("Roll_Number"));
                    datanum.put("fullname",rt.getString("fullname"));
                    datanum.put("std",rt.getString("std"));
                    datanum.put("Applicant_type",rt.getString("Applicant_type"));
                    datanum.put("Contact_no",rt.getString("Contact_no"));
                    datanum.put("PendingFee",rt.getString("PendingFee"));
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
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

public class StudentListDetails extends AppCompatActivity {

    Connection conn;
    Boolean isSuccess = false;
    SharedPreferences sharedPref;
    ListView LV_Country;
    SimpleAdapter ADAhere;
    String ConnectionResult,Form1,adminaca;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData;
            StudentListDetails mydata = new StudentListDetails();
            MyData = mydata.replacetoast(adminaca,Form1);
            String[] fromwhere = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
            int[] viewswhere = {R.id.name, R.id.fname, R.id.mname, R.id.code, R.id.rollnumber, R.id.grno, R.id.dob, R.id.admission, R.id.address, R.id.gender, R.id.number, R.id.religion, R.id.caste, R.id.category, R.id.uid, R.id.adhar};
            ADAhere = new SimpleAdapter(StudentListDetails.this, MyData, R.layout.listtemplate2, fromwhere, viewswhere);
            LV_Country.setAdapter(ADAhere);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_details);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        adminaca = sharedPref.getString("Admincode", null);

        LV_Country = findViewById(R.id.text);

        try{
            Form1=getIntent().getExtras().getString("code");
        }catch (Exception e){}

        mainHandler.post(myRunnable);
    }
    

    public List<Map<String, String>> replacetoast(String aca,String code) {
        List<Map<String, String>> data ;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select a.surname+' '+a.name+' '+a.father_name 'name',a.father_name,a.mother_name,Student_Code,Per_Address,CONVERT(varchar(10),Birth_Date,103)Birth_Date,\n" +
                        "Self_Moblie_Number,Gender,Religion,Caste,Category,AadharCard,Uid,b.roll_number,b.gr_number,CONVERT(varchar(10),Admission_date,103)Admission_date\n" +
                        "from tblstudentmaster a,tbladmissionfeemaster b where Student_Code=Applicant_type and Applicant_type!='new' \n" +
                        "and b.Acadmic_Year=(select selectedaca from tblusermaster where username='"+aca+"')and Student_Code='"+code+"'";
                Statement statement = conn.createStatement();
                ResultSet  rs = statement.executeQuery(query);
                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("1",rs.getString("name"));
                    datanum.put("2",rs.getString("father_name"));
                    datanum.put("3", rs.getString("mother_name"));
                    datanum.put("4", rs.getString("Student_Code"));
                    datanum.put("5", rs.getString("roll_number"));
                    datanum.put("6", rs.getString("gr_number"));
                    datanum.put("7", rs.getString("Birth_Date"));
                    datanum.put("8", rs.getString("Admission_date"));
                    datanum.put("9", rs.getString("Per_Address"));
                    datanum.put("10", rs.getString("Gender"));
                    datanum.put("11", rs.getString("Self_Moblie_Number"));
                    datanum.put("12", rs.getString("Religion"));
                    datanum.put("13", rs.getString("Caste"));
                    datanum.put("14", rs.getString("Category"));
                    datanum.put("15", rs.getString("Uid"));
                    datanum.put("16", rs.getString("AadharCard"));
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
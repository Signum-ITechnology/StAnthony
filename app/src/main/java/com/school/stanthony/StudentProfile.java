package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class StudentProfile extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    TextView year;
    Snackbar snackbar;
    PreparedStatement stmt;
    ListView LV_Country;
    SimpleAdapter ADAhere;
    String Form1,url;
    SharedPreferences sharedPref;
    ImageView imageView;
    ResultSet rs;
    ScrollView scrollView;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData;
            StudentProfile mydata = new StudentProfile();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"StudentId", "RollNumber", "Surname", "Name", "FatherName", "MotherName", "Class_Name", "Division", "Address", "BirthDate", "MobileNumber", "FatherMobileNumber", "Gender", "Religion", "Caste","AadharCard","Uid"};
            int[] viewswhere = {R.id.code, R.id.roll, R.id.address, R.id.name, R.id.fname, R.id.sname, R.id.standard, R.id.div, R.id.birth, R.id.mobile, R.id.fmobile, R.id.year, R.id.gender, R.id.religion, R.id.caste,R.id.adhar,R.id.uid};
            ADAhere = new SimpleAdapter(StudentProfile.this, MyData, R.layout.listtemplate, fromwhere, viewswhere);
            LV_Country.setAdapter(ADAhere);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LV_Country = findViewById(R.id.text);
        imageView=findViewById(R.id.image);
        scrollView=findViewById(R.id.scroll);
        year=findViewById(R.id.year);
        year.setText(getIntent().getExtras().getString("aca"));

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        final ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        // Connect to database

        if (conn == null) {
            scrollView.setVisibility(View.INVISIBLE);
            snackbar = Snackbar.make(findViewById(R.id.id), "No Internet Connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            startActivity(getIntent());
                            mainHandler.post(myRunnable);
                        }
                    });
            snackbar.show();
        }

        ///////  For Student Image
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "Select 'http://stanthony.edusofterp.co.in/'+replace(replace(imagepath,' ','%20'),'..','') photo from tbladmissionfeemaster\n" +
                        "where applicant_type='"+Form1+"' and Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    url = rs.getString("photo");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        loadimagefromurl(url);
        mainHandler.post(myRunnable);
    }

    private void loadimagefromurl(String url) {
        Glide.with(this).load(url).into(imageView);

    }

    public List<Map<String, String>> replacetoast(String code) {
        List<Map<String, String>> data ;
        data = new ArrayList<>();
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "Check Your Internet Access!";
            } else {
                String query = "select Student_Code,b.Roll_Number,b.Class_Name,b.Division,a.Name,a.Father_Name,a.Mother_Name,a.SurName,Per_Address,Res_Address,CONVERT(varchar(10),\n" +
                        "Birth_Date,103)Birth_Date,Self_Moblie_Number,Father_Mobile_number,Gender,Religion,Caste,AadharCard,Uid from tblstudentmaster a,tbladmissionfeemaster b where Student_Code=Applicant_type and Applicant_type!='new' \n" +
                        "and b.Acadmic_Year= ((select isselected from tblstudentmaster where student_code='"+code+"')) and Student_Code='"+code+"'";
                Statement statement = conn.createStatement();
                ResultSet  rs = statement.executeQuery(query);
                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<>();
                    datanum.put("StudentId",rs.getString("Student_Code"));
                    datanum.put("RollNumber",rs.getString("Roll_Number"));
                    datanum.put("Name",rs.getString("Name"));
                    datanum.put("FatherName",rs.getString("Father_Name"));
                    datanum.put("MotherName",rs.getString("Mother_Name"));
                    datanum.put("Surname",rs.getString("SurName"));
                    datanum.put("Class_Name",rs.getString("Class_Name"));
                    datanum.put("Division",rs.getString("Division"));
                    datanum.put("Address",rs.getString("Per_Address"));
                    datanum.put("ResAddress",rs.getString("Res_Address"));
                    datanum.put("BirthDate",rs.getString("Birth_date"));
                    datanum.put("MobileNumber", rs.getString("Self_Moblie_Number"));
                    datanum.put("FatherMobileNumber", rs.getString("Father_Mobile_number"));
                    datanum.put("Gender", rs.getString("Gender"));
                    datanum.put("Religion", rs.getString("Religion"));
                    datanum.put("Caste", rs.getString("Caste"));
                    datanum.put("AadharCard", rs.getString("AadharCard"));
                    datanum.put("Uid", rs.getString("Uid"));
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
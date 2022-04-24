package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TakeAttendaceHoliday extends AppCompatActivity {

    EditText code,roll,name,present;
    List<EditText> allEd = new ArrayList<EditText>();
    List<EditText> allEds = new ArrayList<EditText>();
    List<EditText> allEds1 = new ArrayList<EditText>();
    List<EditText> allEds2 = new ArrayList<EditText>();
    LinearLayout linear,linear1,linear2,linear3;
    Button button;
    ArrayList<String> getname;
    ArrayList<String> getroll;
    ArrayList<String> getcode;
    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;
    Boolean isSuccess;
    String ConnectionResult,Form1;
    SharedPreferences sharedPreferences;
    int id;
    Bundle bundle;
    String section,section1,section2,section3,section4,section5,createdby;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TakeAttendaceHoliday.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    progress.dismiss();
                }
            }, 5000);
        }};

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TakeAttendaceHoliday.this);
            progress.setTitle("Taking Attendance");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    takeattendaceholiday();
                    progress.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(TakeAttendaceHoliday.this);
                    builder.setMessage("Attendance Successfull");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 500);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance1);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        linear=findViewById(R.id.id);
        linear1=findViewById(R.id.id1);
        linear2=findViewById(R.id.id2);
        linear3=findViewById(R.id.id3);
        button=findViewById(R.id.btn);

        getroll=new ArrayList<>();
        getname=new ArrayList<>();
        getcode=new ArrayList<>();

        bundle=getIntent().getExtras();
        section=bundle.getString("section");
        section1=bundle.getString("section1");
        section2=bundle.getString("section2");
        section3=bundle.getString("section3");
        section4=bundle.getString("section4");
        createdby=bundle.getString("section6");

        mainHandler.post(myRunnable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHandler1.post(myRunnable1);
            }
        });
    }

    public void loaddata(){

        ///get acadmic
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select TOP(1) selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"'";

                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    section5 = rs.getString("selectedaca");
                }
            } }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }
        ////get rollno and name

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select applicant_type,roll_number,surname+' '+name name  from tbladmissionfeemaster where acadmic_year='"+section5+"' \n" +
                        "and class_name='"+section1+"' and division='"+section2+"' and iscancelled='0' and applicant_type!='new' order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String cod = rs.getString("applicant_type");
                    getcode.add(cod);

                    String fullname = rs.getString("roll_number");
                    getroll.add(fullname);

                    String roll = rs.getString("name");
                    getname.add(roll);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        ////

        for (int j = 0; j < getroll.size(); j++) {

            roll = new EditText(TakeAttendaceHoliday.this);
            allEds.add(roll);
            roll.setId(id);
            roll.setText(getroll.get(j));
            roll.setFocusable(false);
            roll.setTextSize(14);
            roll.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            roll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear1.addView(roll);
        }

        for (int j = 0; j < getroll.size(); j++) {

            name = new EditText(TakeAttendaceHoliday.this);
            allEds1.add(name);
            name.setId(id);
            name.setText(getname.get(j));
            name.setFocusable(false);
            name.setTextSize(14);
            name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(name);
        }

        for (int j = 0; j < getroll.size(); j++) {

            present = new EditText(TakeAttendaceHoliday.this);
            allEds2.add(present);
            present.setId(id);
            present.setText("H");
            present.setAllCaps(true);
            present.setTextSize(14);
            present.setTextColor(Color.BLUE);
            present.setGravity(Gravity.CENTER);
            present.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            present.setFocusable(false);
            present.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(present);
        }
    }

    public void takeattendaceholiday(){

        for (int j = 0; j < getroll.size(); j++){

            String code=getcode.get(j);
            String roll=getroll.get(j);

            String msg = "unknown";
            ///get acadmic
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into tblstudentattendencedetails (StudentCode,Roll_Number,Batch_code,Class_name,Division,Acadmic_year,Present,AttendenceDate,AttendenceMonth,created_on,created_by,Day_status)\n" +
                            "values('" + code + "','"+roll+"','" + section + "','" + section1 + "','" + section2 + "','"+section5+"','H','" + section3 + "','" + section4 + "',SYSDATETIME(),'" + createdby + "','2')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }
}
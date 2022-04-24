package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.appcompat.app.AppCompatActivity;

public class TeacherAttendenceStatus extends AppCompatActivity {

    SharedPreferences sharedPref;
    Button jun,jul,sep,oct,nov,dec,jan,feb,mar,apr,aug,may;
    String Form1,January="1",
            February="2",
            March="3",
            April="4",
            May="5",
            June="6",
            July="7",
            August="8",
            September="9",
            October="10",
            November="11",
            December="12";
    String section,std,div,ConnectionResult,getyear;
    PreparedStatement stmt;
    ResultSet rs;
    Connection conn;
    Boolean isSuccess;

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TeacherAttendenceStatus.this);
            progress.setTitle("Loading Details");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ///////    get current year

                    try {
                        ConnectionHelper conStr1 = new ConnectionHelper();
                        conn = conStr1.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select top 1 a.acadmicyear from tblcompanymaster a,tbl_hrstaffnew b\n" +
                                    "where a.companycode=b.selectedaca and b.staffuser='"+Form1+"'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            while (rs.next()) {
                                getyear = rs.getString("acadmicyear");
                            }
                            ConnectionResult = "Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    } catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    } catch (java.sql.SQLException e) {
                        e.printStackTrace();
                    }
                    progress.cancel();
                }
            }, 1000);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendence_status);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Teachercode", null);

        section=getIntent().getExtras().getString("section");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");

        jun=findViewById(R.id.jun);
        jul=findViewById(R.id.jul);
        aug=findViewById(R.id.aug);
        sep=findViewById(R.id.sep);
        oct=findViewById(R.id.oct);
        nov=findViewById(R.id.nov);
        dec=findViewById(R.id.dec);
        jan=findViewById(R.id.jan);
        feb=findViewById(R.id.feb);
        mar=findViewById(R.id.mar);
        apr=findViewById(R.id.apr);
        may=findViewById(R.id.may);

        mainHandler2.post(myRunnable2);

        jun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(0,4);
                intent.putExtra("month",June);
                intent.putExtra("monthname","June");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        jul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(0,4);
                intent.putExtra("month",July);
                intent.putExtra("monthname","July");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        aug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(0,4);
                intent.putExtra("month",August);
                intent.putExtra("monthname","August");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        sep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(0,4);
                intent.putExtra("month",September);
                intent.putExtra("monthname","September");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        oct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(0,4);
                intent.putExtra("month",October);
                intent.putExtra("monthname","October");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        nov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(0,4);
                intent.putExtra("month",November);
                intent.putExtra("monthname","November");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(0,4);
                intent.putExtra("month",December);
                intent.putExtra("monthname","December");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        jan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(5,9);
                intent.putExtra("month",January);
                intent.putExtra("monthname","January");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        feb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(5,9);
                intent.putExtra("month",February);
                intent.putExtra("monthname","February");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        mar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(5,9);
                intent.putExtra("month",March);
                intent.putExtra("monthname","March");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        apr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(5,9);
                intent.putExtra("month",April);
                intent.putExtra("monthname","April");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

        may.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), TeacherAttendenceStatusDetails.class);
                String newyear=getyear.substring(5,9);
                intent.putExtra("month",May);
                intent.putExtra("monthname","May");
                intent.putExtra("year",newyear);
                intent.putExtra("section",section);
                intent.putExtra("std",std);
                intent.putExtra("div",div);
                startActivity(intent);
            }
        });

    }
}
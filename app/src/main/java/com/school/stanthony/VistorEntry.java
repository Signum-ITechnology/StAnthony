package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class VistorEntry extends AppCompatActivity {
    EditText text1,text2,text3,text5,text6,text7,text4,text8,text9;
    String s1,s2,s3,s4,s5,s6,s7,s8,s9,getdate,gettime,ConnectionResult;
    String acadmic,createdby,Form1;
    SharedPreferences sharedPreferences;
    Button button;
    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;
    Boolean isSuccess;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(VistorEntry.this);
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
            }, 1000);
        }};

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(VistorEntry.this);
            progress.setTitle("Entering Visitor Details");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    String msg = "unknown";
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String commands = "insert into tblVisitorEntry values('"+s1+"','"+s8+"','"+s2+"','"+s3+"','"+s4+"','"+s5+"','"+s6+"','"+s7+"','"+s9+"','',getdate(),'"+createdby+"','','','"+acadmic+"','')";
                            PreparedStatement preStmt = conn.prepareStatement(commands);
                            preStmt.executeUpdate();
                        }
                    }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }

                    button.setVisibility(View.INVISIBLE);
                    progress.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(VistorEntry.this);
                    builder.setMessage("Visitor Details Added");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 2000);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistor_entry);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("otherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Othercode", null);

        text1= findViewById(R.id.text1);
        text2= findViewById(R.id.text2);
        text3= findViewById(R.id.text3);
        text4= findViewById(R.id.text4);
        text5= findViewById(R.id.text5);
        text6= findViewById(R.id.text6);
        text7= findViewById(R.id.text7);
        text8= findViewById(R.id.text8);
        text9= findViewById(R.id.text9);
        button= findViewById(R.id.login);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setFocusableInTouchMode(true);
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setFocusableInTouchMode(true);
            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text3.setFocusableInTouchMode(true);
            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text4.setFocusableInTouchMode(true);
            }
        });

        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text5.setFocusableInTouchMode(true);
            }
        });

        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text6.setFocusableInTouchMode(true);
            }
        });

        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text7.setFocusableInTouchMode(true);
            }
        });

        text8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text8.setFocusableInTouchMode(true);
            }
        });

        mainHandler.post(myRunnable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(text1.getText())) {
                    text1.setError("Please Enter Your Name");
                    Toast.makeText(VistorEntry.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
                } else if (text2.getText().toString().trim().length()<10 || text2.getText().toString().trim().length()>10) {
                    text2.setError("Please Enter 10 Digits Contact Number");
                    Toast.makeText(VistorEntry.this, "Please Enter 10 Digits Number", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(text5.getText())) {
                    text5.setError("Please Enter Regarding Details");
                    Toast.makeText(VistorEntry.this, "Please Enter Regarding Details", Toast.LENGTH_LONG).show();
                }  else if (TextUtils.isEmpty(text6.getText())) {
                    text6.setError("Please Enter Card Number");
                    Toast.makeText(VistorEntry.this, "Please Enter Card Number", Toast.LENGTH_LONG).show();
                }  else{
                    s1=text1.getText().toString();
                    s2=text2.getText().toString();
                    s3=text3.getText().toString();
                    s4=text4.getText().toString();
                    s5=text5.getText().toString();
                    s6=text6.getText().toString();
                    s7=text7.getText().toString();
                    s8=text8.getText().toString();
                    s9=text9.getText().toString();

                    mainHandler1.post(myRunnable1);

                }
            }
        });

    }

    private void loaddata(){

        ///get current date and time
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select CONVERT(varchar(10),getdate(),103) date,CONVERT(varchar(15),CAST(getdate()AS TIME),100) as Time";

                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getdate = rs.getString("date");
                    gettime = rs.getString("Time");

                    text8.setText(getdate);
                    text9.setText(gettime);
                }
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        ///get staffid and acadmic year
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select Staff_ID,acadmic_year from tbl_HRStaffnew where StaffUser='"+Form1+"'\n" +
                        "and acadmic_year=(select max(acadmic_year) from tbl_hrstaffnew where staffuser='"+Form1+"')";

                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    createdby= rs.getString("Staff_ID");
                    acadmic= rs.getString("acadmic_year");

                    text8.setText(getdate);
                    text9.setText(gettime);
                }
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

    }

}
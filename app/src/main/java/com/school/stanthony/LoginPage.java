package com.school.stanthony;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class LoginPage extends AppCompatActivity {
    EditText nm,pwd;
    Button login;
    Connection conn;
    Boolean isSuccess = false;
    String ConnectionResult = "";
    ProgressBar progressBar;
    Integer code;
    ImageView imageView;
    String uname,password;
    SharedPreferences sharedPref2,sharedPref1,sharedPref3,sharedPref4,sharedPref5;
    DatabaseHelper myDb;
    ResultSet rs;
    PreparedStatement stmt;
    CheckBox checkBox;
    String post,upperpost,checkt,checko,checkp,checks,checka;
    ArrayList<String> data1;
    ArrayList<String>data2;
    ArrayList<String>data3;
    ArrayList<String>data4;
    ArrayList<String>data5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPref1 = getSharedPreferences("loginref", MODE_PRIVATE);
        sharedPref2 = getSharedPreferences("teacherref", MODE_PRIVATE);
        sharedPref3 = getSharedPreferences("adminref", MODE_PRIVATE);
        sharedPref4 = getSharedPreferences("otherref", MODE_PRIVATE);
        sharedPref5 = getSharedPreferences("prinref", MODE_PRIVATE);

        nm = findViewById(R.id.uname);
        pwd = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress);
        imageView = findViewById(R.id.image);
        progressBar.setVisibility(View.GONE);
        login = findViewById(R.id.login);
        myDb = new DatabaseHelper(this);
        checkBox = findViewById(R.id.check);

        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwd.setTypeface(pwd.getTypeface(), Typeface.BOLD);

        data1=new ArrayList<>();
        data2=new ArrayList<>();
        data3=new ArrayList<>();
        data4=new ArrayList<>();
        data5=new ArrayList<>();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(pwd.getText().toString().trim().equals("")){
                    checkBox.setChecked(false);
                    Toast.makeText(LoginPage.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }
                else if (isChecked == true) {
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd.setTypeface(pwd.getTypeface(), Typeface.BOLD);
                } else {
                    pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pwd.setTypeface(pwd.getTypeface(), Typeface.BOLD);

                }
            }
        });

        nm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nm.setFocusableInTouchMode(true);
            }
        });

        pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwd.setFocusableInTouchMode(true);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nm.getText().toString().trim().equals("")) {
                    nm.setHint("Please Enter User Id");
                    nm.setHintTextColor(Color.RED);
                } else if (pwd.getText().toString().trim().equals("")) {
                    pwd.setHint("Please Enter Password");
                    pwd.setHintTextColor(Color.RED);
                }
                else  if (!nm.getText().toString().trim().equals("") && !pwd.getText().toString().trim().equals("")) {

                    final ProgressDialog progress = new ProgressDialog(LoginPage.this);
                    progress.setTitle("Verifying");
                    progress.setMessage("Please Wait a Moment ...");
                    progress.setCancelable(false);
                    progress.show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uname = nm.getText().toString();
                            password = pwd.getText().toString();

                            ////for student

                            try {
                                ConnectionHelper conStr2 = new ConnectionHelper();
                                conn = conStr2.connectionclasss();

                                if (conn == null) {
                                    ConnectionResult = "NO INTERNET";
                                } else {
                                    String query2 = "select student_code from tblstudentmaster where student_code='" + uname + "' and logincode='" + password + "'";
                                    stmt = conn.prepareStatement(query2);
                                    rs = stmt.executeQuery();
                                    while (rs.next()) {
                                        String user = rs.getString("student_code");
                                        data1.add(user);
                                        if (data1.size() != 0) {
                                            checks="syes";

                                            code = Integer.parseInt(nm.getText().toString());
                                            SharedPreferences.Editor edit = sharedPref1.edit();
                                            edit.putBoolean("Registered", true);
                                            edit.putString("code", uname);
                                            edit.putString("pass", password);
                                            edit.apply();
                                            {
                                                myDb.insertData(uname,password);
                                                click();
                                                progress.cancel();
                                                Intent i = new Intent(LoginPage.this, HomePage.class);
                                                startActivity(i);
                                                conn.close();
                                            }
                                        }
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

                            ////for admin

                            try {
                                ConnectionHelper conStr2 = new ConnectionHelper();
                                conn = conStr2.connectionclasss();

                                if (conn == null) {
                                    ConnectionResult = "NO INTERNET";
                                } else {
                                    String query2 = "select a.username from tblusermaster a , tblUserDetail b\n" +
                                            "where b.groupid=1 and a.userid=b.userid and username='" + uname + "' and androidpassword='" + password + "'";
                                    stmt = conn.prepareStatement(query2);
                                    rs = stmt.executeQuery();
                                    while (rs.next()) {
                                        String user2 = rs.getString("username");
                                        data3.add(user2);
                                        if (data3.size() != 0) {
                                            checka="ayes";

                                            SharedPreferences.Editor edit = sharedPref3.edit();
                                            edit.putBoolean("Registered2", true);
                                            edit.putString("Admincode", uname);
                                            edit.apply();
                                            {
                                                click();
                                                progress.cancel();
                                                Intent i = new Intent(getApplicationContext(), HomePage3.class);
                                                startActivity(i);
                                                conn.close();
                                            }
                                        }
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

                            ////for teacher

                            //check teacher post
                            try {
                                ConnectionHelper conStr1 = new ConnectionHelper();
                                conn = conStr1.connectionclasss();

                                if (conn == null) {
                                    ConnectionResult = "NO INTERNET";
                                } else {
                                    uname = nm.getText().toString();
                                    password = pwd.getText().toString();
                                    String query = "select Designation from tbl_hrstaffnew where staffuser='" + uname + "' and \n" +
                                            "acadmic_year=(select Max(acadmic_year) from tbl_hrstaffnew where staffuser='" + uname + "')";
                                    stmt = conn.prepareStatement(query);
                                    rs = stmt.executeQuery();
                                    while (rs.next()) {
                                        post = rs.getString("Designation");
                                        upperpost = post.toUpperCase();
                                        if (upperpost.equals("AST TEACHER")) {
                                            ///check teacher login

                                            try {
                                                ConnectionHelper conStr2 = new ConnectionHelper();
                                                conn = conStr2.connectionclasss();

                                                if (conn == null) {
                                                    ConnectionResult = "NO INTERNET";
                                                } else {
                                                    String query2 = "select StaffUser from tbl_HRStaffnew where StaffUser='" + uname + "' and StaffPassword='" + password + "'\n";
                                                    stmt = conn.prepareStatement(query2);
                                                    rs = stmt.executeQuery();
                                                    while (rs.next()) {
                                                        String user2 = rs.getString("StaffUser");
                                                        data2.add(user2);
                                                        if (data2.size() != 0) {
                                                            checkt="tyes";
//                                                    teachercode=data4.get(0);
                                                            SharedPreferences.Editor edit = sharedPref2.edit();
                                                            edit.putBoolean("Registered1", true);
                                                            edit.putString("Teachercode", uname);
                                                            edit.putString("TeacherPass", password);
                                                            edit.apply();
                                                            {
                                                                click();
                                                                progress.cancel();
                                                                Intent i = new Intent(getApplicationContext(), HomePage2.class);
                                                                startActivity(i);
                                                                conn.close();
                                                            }
                                                        }
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
                                        } else if (upperpost.equals("PRINCIPAL") || upperpost.equals("SUPERVISOR")) {
                                            ///check principal login

                                            try {
                                                ConnectionHelper conStr2 = new ConnectionHelper();
                                                conn = conStr2.connectionclasss();

                                                if (conn == null) {
                                                    ConnectionResult = "NO INTERNET";
                                                } else {
                                                    String query2 = "select StaffUser from tbl_HRStaffnew where StaffUser='" + uname + "' and StaffPassword='" + password + "'\n";
                                                    stmt = conn.prepareStatement(query2);
                                                    rs = stmt.executeQuery();
                                                    while (rs.next()) {
                                                        String user2 = rs.getString("StaffUser");
                                                        data5.add(user2);
                                                        if (data5.size() != 0) {
                                                            checkp="tyes";
//                                                    teachercode=data4.get(0);
                                                            SharedPreferences.Editor edit = sharedPref5.edit();
                                                            edit.putBoolean("Registered4", true);
                                                            edit.putString("Principalcode", uname);
                                                            edit.putString("PrincipalPass", password);
                                                            edit.apply();
                                                            {
                                                                click();
                                                                progress.cancel();
                                                                Intent i = new Intent(getApplicationContext(), HomePage5.class);
                                                                startActivity(i);
                                                                conn.close();
                                                            }
                                                        }
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
                                        } else {
                                            ///check teacher login

                                            try {
                                                ConnectionHelper conStr2 = new ConnectionHelper();
                                                conn = conStr2.connectionclasss();

                                                if (conn == null) {
                                                    ConnectionResult = "NO INTERNET";
                                                } else {
                                                    String query2 = "select StaffUser from tbl_HRStaffnew where StaffUser='" + uname + "' and StaffPassword='" + password + "'\n";
                                                    stmt = conn.prepareStatement(query2);
                                                    rs = stmt.executeQuery();
                                                    while (rs.next()) {
                                                        String user2 = rs.getString("StaffUser");
                                                        data4.add(user2);
                                                        if (data4.size() != 0) {
                                                            checko="oyes";
//
                                                            SharedPreferences.Editor edit = sharedPref4.edit();
                                                            edit.putBoolean("Registered3", true);
                                                            edit.putString("Othercode", uname);
                                                            edit.putString("OtherPass", password);
                                                            edit.apply();
                                                            {
                                                                click();
                                                                progress.cancel();
                                                                Intent i = new Intent(getApplicationContext(), HomePage4.class);
                                                                startActivity(i);
                                                                conn.close();
                                                            }
                                                        }
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
                                        }
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

                            if((data1.size()==0) && (data2.size()==0) && (data3.size()==0) && (data4.size()==0) && (data5.size()==0)){
                                progress.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                                builder.setMessage("Invalid User");
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

                        }
                    }, 5000);
                }
            }
        });
    }

    // For Notification Purpose
        private void click() {
            {
                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("St Anthony High School")
                        .setContentText("Powered By - EdusoftErp");
                mBuilder.setSound(defaultSoundUri);
                NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                noti.notify(0, mBuilder.build());
            }
        }

    }
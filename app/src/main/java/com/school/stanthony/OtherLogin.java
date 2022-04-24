package com.school.stanthony;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class OtherLogin extends AppCompatActivity {
    EditText nm, pwd, form;
    Button login;
    Connection conn;
    Boolean isSuccess = false;
    String ConnectionResult = "";
    ProgressBar progressBar;
    String Teachercode;
    String uname, password,post,upperpost;
    SharedPreferences sharedPreferences;
    ProgressDialog progress;
    Spinner spinner;
    ResultSet rs;
    PreparedStatement stmt;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPreferences = getSharedPreferences("otherref",MODE_PRIVATE);

        nm = findViewById(R.id.uname);
        pwd = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        login = findViewById(R.id.login);
        spinner=findViewById(R.id.spinner);
        checkBox = findViewById(R.id.check);

        pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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
        ////

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(pwd.getText().toString().trim().equals("")){
                    checkBox.setChecked(false);
                    Toast.makeText(OtherLogin.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
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


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        uname = nm.getText().toString();
                        password = pwd.getText().toString();
                        String query = "select designation from tbl_hrstaffnew where\n" +
                                "staff_id=(select staff_id from tbl_Hrstaffnew where staffuser='"+uname+"' \n" +
                                "and acadmic_year=(select max(acadmic_year) from tbl_hrstaffnew where staffuser='"+uname+"'))";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            post = rs.getString("designation");
                        }
                        try {
                            upperpost=post.toUpperCase();
                            ////////////////
                            if(!upperpost.equals("TEACHER")){
                                CheckLogin checkLogin = new CheckLogin();
                                checkLogin.execute();
                            }
                            else {
                                Toast.makeText(OtherLogin.this, "Invalid User", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(OtherLogin.this, "Please Enter Staff ID & Password", Toast.LENGTH_LONG).show();
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

            }
        });
    }

    // For Notification Purpose
    private void click() {
        {
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Shree Sanatan Dharam Vidyalaya & Jr College")
                    .setContentText("Powered By - EdusoftErp");
            mBuilder.setSound(defaultSoundUri);

            NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            noti.notify(0, mBuilder.build());
        }
    }

////////////////////

    public class CheckLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        protected void onPreExecute() {
            super.onPreExecute();
            uname = nm.getText().toString();
            password = pwd.getText().toString();
            if (uname.trim().equals("") || password.trim().equals("")) {
                z = "Please Enter Staff Id & Password";
            }
            else {
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        z="Check Your Internet Access!";
                    } else {
                        {
                            Toast.makeText(OtherLogin.this, "Verifying please wait...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){

                }
            }}

        @Override
        protected String doInBackground(String... params) {
            uname = nm.getText().toString();
            password = pwd.getText().toString();
            if (uname.trim().equals("") || password.trim().equals("")) {
                z = "Please Enter Staff ID & Password";
            } else {

                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        z="Check Your Internet Access!";
                    } else {
                        String query = "select StaffUser,StaffPassword from tbl_HRStaffnew where StaffUser=? and StaffPassword=?";
                        PreparedStatement stmt = conn.prepareStatement(query);
                        stmt.setString(1, uname);
                        stmt.setString(2, password);
                        ResultSet resultSet = stmt.executeQuery();
                        if (resultSet.next()) {
                            z = "Login Successful";
                            isSuccess = true;
                            click();
                            Teachercode = nm.getText().toString();
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putBoolean("Registered3", true);
                            edit.putString("Othercode", uname);
                            edit.apply();
                            {
                                Intent i = new Intent(getApplicationContext(), HomePage4.class);
                                startActivity(i);
                                conn.close();
                            }
                        } else {
                            z = "Please Enter Valid Staff Id & Password";
                            isSuccess = false;
                        }
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    z = e.getMessage();
                }
            }
            return z;
        }
        protected void onPostExecute(String r) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(OtherLogin.this, r, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
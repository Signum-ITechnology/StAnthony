package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {
    Button b1,b2,b3,b4,b5,b6,b7;
    String ConnectionResult,website;
    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;
    Boolean isSuccess;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(MainPage.this);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        b1= findViewById(R.id.btn1);
        b2=findViewById(R.id.btn2);
        b3= findViewById(R.id.btn3);
        b4= findViewById(R.id.btn4);
        b5= findViewById(R.id.btn5);
        b6= findViewById(R.id.btn6);
        b7= findViewById(R.id.btn7);

        Animation animation1= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide);
        b1.startAnimation(animation1);
        b2.startAnimation(animation1);
        b3.startAnimation(animation1);
        b4.startAnimation(animation1);
        b5.startAnimation(animation1);
        b6.startAnimation(animation1);
        b7.startAnimation(animation1);

        mainHandler.post(myRunnable);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AboutUs.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),NewsPage.class);
                startActivity(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AdmissionEnquiry.class);
                startActivity(i);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),PrincipalMessage.class);
                startActivity(i);
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Website.class);
                i.putExtra("url",website);
                startActivity(i);
            }
        });

        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),YouTube.class);
                startActivity(i);
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),LoginPage.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void loaddata() {

        ///get about us details
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select link from tblorgmaster";

                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    website = rs.getString("link");
                }
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

    }

}
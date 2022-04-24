package com.school.stanthony;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage3 extends AppCompatActivity {

    LinearLayout l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,l13,l14;
    Spinner spinner;
    PreparedStatement stmt;
    Connection conn;
    String ConnectionResult="",getacademic;
    ResultSet rs;
    boolean isSuccess;
    String Form1,checkacadmic;
    TextView allcount,academic;
    SharedPreferences sharedPref;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page3);
        l1=findViewById(R.id.linear1);
        l2=findViewById(R.id.linear2);
        l3=findViewById(R.id.linear3);
        l4=findViewById(R.id.linear4);
        l5=findViewById(R.id.linear5);
        l6=findViewById(R.id.linear6);
        l7=findViewById(R.id.linear7);
        l8=findViewById(R.id.linear8);
        l9=findViewById(R.id.linear9);
        l10=findViewById(R.id.linear10);
        l11=findViewById(R.id.linear11);
        l12=findViewById(R.id.linear12);
        l13=findViewById(R.id.linear13);
        l14=findViewById(R.id.linear14);
        spinner=findViewById(R.id.spinner);
        academic=findViewById(R.id.academic);
        bottomNavigationView=findViewById(R.id.bottomNavigationView);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);
        ///////    FOr Academic Year

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select b.acadmicyear,b.companycode from tblusermaster a,tblcompanymaster b\n" +
                        "where username='"+Form1+"' and a.selectedaca=b.companycode";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getacademic = rs.getString("acadmicyear");
                    academic.setText("Acadmic Year : "+getacademic);

                    checkacadmic = rs.getString("companycode");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }



        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AdmissionRegister.class);
                startActivity(i);
            }
        });

        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StudentList.class);
                startActivity(i);
            }
        });

        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StudentsPendingFeeReport.class);
                startActivity(i);
            }
        });

        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),DailyCollectionChoice.class);
                startActivity(i);
            }
        });

        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),NewPendingFeeReport.class);
                startActivity(i);
            }
        });

        l6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StaffBirthdayRegister.class);
                startActivity(i);
            }
        });

        l7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AttendancePendingRegister.class);
                startActivity(i);
            }
        });

        l8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ExpenditureRegister.class);
                startActivity(i);
            }
        });

        l9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ChequeRegister.class);
                startActivity(i);
            }
        });

        l10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),NewsPage.class);
                startActivity(i);
            }
        });

        l11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AdminStudentNoticeEntry.class);
                startActivity(i);
            }
        });

        l12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AdminTeacherNoticeEntry.class);
                startActivity(i);
            }
        });

        l13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StudentNoticeReport.class);
                startActivity(i);
            }
        });

        l14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),StaffList.class);
                startActivity(i);
            }
        });

        bottomNavigationView.getMenu().getItem(0).setCheckable(false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.acadamic:
                        Intent iii = new Intent(HomePage3.this,SelectAcadmicAdmin.class);
                        iii.putExtra("aca",checkacadmic);
                        startActivity(iii);
                        break;
                    case R.id.refresh:
                        finish();
                        startActivity(getIntent());
                        break;
                    case R.id.action_settings:
                        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.apply();
                        Intent i = new Intent(HomePage3.this,MainPage.class);
                        startActivity(i);
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomePage3.this);
        builder.setTitle("EXIT");
        builder.setMessage("Are You Sure You Want To Exit");
        builder.setCancelable(true);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
            }
        });

        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
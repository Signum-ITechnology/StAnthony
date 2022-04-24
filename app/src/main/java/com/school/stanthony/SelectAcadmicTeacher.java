package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SelectAcadmicTeacher extends AppCompatActivity {

    ListView listView;
    SharedPreferences sharedPref;
    String Form1,ConnectionResult;
    PreparedStatement stmt;
    ResultSet rs;
    Connection conn;
    Boolean isSuccess;
    String getyear,getacademic,checkaca;
    ProgressDialog progress;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            progress = new ProgressDialog(SelectAcadmicTeacher.this);
            progress.setTitle("Changing Acadmic");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                }
            }, 2000);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_acadmic);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        checkaca=getIntent().getExtras().getString("aca");
        sharedPref = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPref.getString("Teachercode", null);

        listView=findViewById(R.id.listview1);

        /////////////////// get student all acadmic_year
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select b.acadmicyear from tbl_hrstaffnew a,tblcompanymaster b\n" +
                        "where staffuser='"+Form1+"' and a.acadmic_year=b.companycode";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                while (rs.next())
                {
                    String fullname = rs.getString("acadmicyear");
                    data2.add(fullname);
                }
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(SelectAcadmicTeacher.this,R.layout.spinner11, data2);
                listView.setAdapter(NoCoreAdapter2);
                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getyear=listView.getItemAtPosition(i).toString();
                mainHandler.post(myRunnable);

            }
        });

    }

    private void loaddata() {

        ////For Academic

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select companycode from tblcompanymaster where acadmicyear='"+getyear+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getacademic = rs.getString("companycode");
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

        if(checkaca.equals(getacademic)){
            progress.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(SelectAcadmicTeacher.this);
            builder.setMessage(getyear+" Already Selected");
            builder.setCancelable(false);
            builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }else {

            ///// update acadmic year
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrstaffnew set selectedaca='" + getacademic + "' where staffuser='" + Form1 + "'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            } catch (java.sql.SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }


            startActivity(new Intent(SelectAcadmicTeacher.this,HomePage2.class));
            progress.dismiss();

        }

    }

}


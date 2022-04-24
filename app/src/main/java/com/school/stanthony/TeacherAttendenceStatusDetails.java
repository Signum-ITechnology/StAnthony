package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherAttendenceStatusDetails extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "",section,std,div,Form1;
    Boolean isSuccess;
    PreparedStatement stmt;
    ResultSet rs;
    SharedPreferences sharedPref;
    TableLayout tl;
    TableRow tr;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> data2;
    String total,year,month,showdate,monthname,check;
    TextView date,status;

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TeacherAttendenceStatusDetails.this);
            progress.setTitle("Loading Details");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    ///////////////get all date

                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();
                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "Declare @year int = '"+year+"', @month int = '"+month+"';\n" +
                                    "WITH numbers\n" +
                                    "as\n" +
                                    "(\n" +
                                    "    Select 1 as value\n" +
                                    "    UNion ALL\n" +
                                    "    Select value + 1 from numbers\n" +
                                    "    where value + 1 <= Day(EOMONTH(datefromparts(@year,@month,1)))\n" +
                                    ")\n" +
                                    "SELECT datefromparts(@year,@month,numbers.value) Datum FROM numbers";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();

                            while (rs.next()) {
                                total = rs.getString("Datum");
                                data.add(total);
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

                    addHeaders();
                    addData();
                    progress.cancel();
                }
            }, 1000);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_alllivelecturedetails);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        monthname = getIntent().getExtras().getString("monthname");
        month = getIntent().getExtras().getString("month");
        year = getIntent().getExtras().getString("year");
        section=getIntent().getExtras().getString("section");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");

        sharedPref = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Teachercode", null);

        tl =findViewById(R.id.maintable);

        mainHandler2.post(myRunnable2);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addHeaders() {
        /** Create a TableRow dynamically **/

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("purple"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(10f);

        tr = new TableRow(TeacherAttendenceStatusDetails.this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setBackground(sd);

        /** Creating a TextView to add to the row **/

        date = new TextView(TeacherAttendenceStatusDetails.this);
        date.setText("Date");
        date.setTextColor(Color.BLUE);
        date.setTextSize(18);
        date.setHeight(90);
        date.setGravity(Gravity.START);
        date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        date.setPadding(40,5,5,5);
        tr.addView(date);

        status = new TextView(TeacherAttendenceStatusDetails.this);
        status.setText("Status");
        status.setTextColor(Color.BLUE);
        status.setTextSize(18);
        status.setHeight(90);
        status.setGravity(Gravity.CENTER_VERTICAL);
        status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        status.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        status.setPadding(0,5,5,0);
        tr.addView(status);

        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    private void addData(){


        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("purple"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(2f);


        for(int i=0;i<data.size();i++){

            String convertdate=data.get(i);

            // convert date
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select convert(varchar,cast('" + convertdate + "' as datetime),103)showdate";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        showdate = rs.getString("showdate");
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


            //// check if attendence is they
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select distinct day_status from tblstudentattendencedetails \n" +
                            "where batch_code='"+section+"' and class_name='"+std+"' and division='"+div+"' and attendencemonth='"+monthname+"' \n" +
                            "and acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                            "and CONVERT(varchar(10),attendencedate,103)='"+showdate+"'";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();
                    data2=new ArrayList<>();
                    while (rs.next()) {
                        check = rs.getString("day_status");
                        data2.add(check);
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

            tr = new TableRow(TeacherAttendenceStatusDetails.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackground(sd);

                date = new TextView(TeacherAttendenceStatusDetails.this);
                date.setText(showdate);
                date.setTextColor(Color.BLACK);
                date.setTextSize(15);
                date.setGravity(Gravity.START);
                date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                date.setPadding(20,5,5,5);
                tr.addView(date);

                if(data2.size()==0){

                    status = new TextView(TeacherAttendenceStatusDetails.this);
                    status.setText("--");
                    status.setTextColor(Color.BLACK);
                    status.setTextSize(15);
                    status.setGravity(Gravity.START);
                    status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    status.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                    status.setPadding(50,5,5,0);
                    tr.addView(status);

                }else {

                    String daystatus=data2.get(0);
                    if(daystatus.equals("1")){
                        String day="Working";

                        status = new TextView(TeacherAttendenceStatusDetails.this);
                        status.setText(day);
                        status.setTextColor(Color.GREEN);
                        status.setTextSize(15);
                        status.setGravity(Gravity.START);
                        status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        status.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        status.setPadding(0,5,5,0);
                        tr.addView(status);

                    }else if(daystatus.equals("2")){
                        String day="Holiday";

                        status = new TextView(TeacherAttendenceStatusDetails.this);
                        status.setText(day);
                        status.setTextColor(Color.BLUE);
                        status.setTextSize(15);
                        status.setGravity(Gravity.START);
                        status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        status.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        status.setPadding(0,5,5,0);
                        tr.addView(status);

                    }else if(daystatus.equals("3")){
                        String day="Un-Instructional";

                        status = new TextView(TeacherAttendenceStatusDetails.this);
                        status.setText(day);
                        status.setTextColor(Color.MAGENTA);
                        status.setTextSize(15);
                        status.setGravity(Gravity.START);
                        status.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                        status.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        status.setPadding(0,5,5,0);
                        tr.addView(status);

                    }

                }

            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        }
    }
}
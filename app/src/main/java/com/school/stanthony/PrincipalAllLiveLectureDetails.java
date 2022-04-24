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

import androidx.appcompat.app.AppCompatActivity;

public class PrincipalAllLiveLectureDetails extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "",month,Form1;
    Boolean isSuccess;
    PreparedStatement stmt;
    ResultSet rs;
    SharedPreferences sharedPref;
    TableLayout tl;
    TableRow tr;
    ArrayList<String> totaldate,totaltime,totalsubject,totalstd;
    String getdate,gettime,getsubject,getstd;
    TextView date,subject,time,std;

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(PrincipalAllLiveLectureDetails.this);
            progress.setTitle("Loading Details");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    ///get live lecture all details
                    try {
                        ConnectionHelper conStr1 = new ConnectionHelper();
                        conn = conStr1.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select Convert(varchar,date,103) date,time,section+'/'+std+'/'+div 'std',subjectname from livelecture\n" +
                                    "where academic=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and month='"+month+"'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            totaldate=new ArrayList<>();
                            totaltime=new ArrayList<>();
                            totalsubject=new ArrayList<>();
                            totalstd=new ArrayList<>();

                            while (rs.next()) {
                                getdate = rs.getString("date");
                                totaldate.add(getdate);

                                gettime = rs.getString("time");
                                totaltime.add(gettime);

                                getsubject = rs.getString("subjectname");
                                totalsubject.add(getsubject);

                                getstd = rs.getString("std");
                                totalstd.add(getstd);
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
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        month = getIntent().getExtras().getString("month");
        sharedPref = getSharedPreferences("prinref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Principalcode", null);
        tl =findViewById(R.id.maintable);

        mainHandler2.post(myRunnable2);

    }

    public void addHeaders() {
        /** Create a TableRow dynamically **/

        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("purple"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(10f);

        tr = new TableRow(PrincipalAllLiveLectureDetails.this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setBackground(sd);

        /** Creating a TextView to add to the row **/

        date = new TextView(PrincipalAllLiveLectureDetails.this);
        date.setText("Date");
        date.setTextColor(Color.BLUE);
        date.setTextSize(18);
        date.setHeight(90);
        date.setGravity(Gravity.CENTER);
        date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        date.setPadding(5,5,5,5);
        tr.addView(date);  // Adding textView to tablerow.

        time = new TextView(PrincipalAllLiveLectureDetails.this);
        time.setText("Time");
        time.setTextColor(Color.BLUE);
        time.setTextSize(18);
        time.setHeight(90);
        time.setGravity(Gravity.START);
        time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        time.setPadding(5,5,5,5);
        tr.addView(time);  // Adding textView to tablerow.

        std = new TextView(PrincipalAllLiveLectureDetails.this);
        std.setText("Std");
        std.setTextColor(Color.BLUE);
        std.setTextSize(18);
        std.setHeight(90);
        std.setGravity(Gravity.START);
        std.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        std.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        std.setPadding(5,5,5,5);
        tr.addView(std);  // Adding textView to tablerow.

        subject = new TextView(PrincipalAllLiveLectureDetails.this);
        subject.setText("Subject");
        subject.setTextColor(Color.BLUE);
        subject.setTextSize(18);
        subject.setHeight(90);
        subject.setGravity(Gravity.START);
        subject.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        subject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        subject.setPadding(5,5,5,5);
        tr.addView(subject);  // Adding textView to tablerow.

        tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
    }

    private void addData(){


        ShapeDrawable sd=new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.parseColor("purple"));
        sd.getPaint().setStyle(Paint.Style.STROKE);
        sd.getPaint().setStrokeWidth(2f);


        for(int i=0;i<totaldate.size();i++){

            tr = new TableRow(PrincipalAllLiveLectureDetails.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackground(sd);

            if(i==0){
                date = new TextView(PrincipalAllLiveLectureDetails.this);
                date.setText(totaldate.get(0));
                date.setTextColor(Color.BLACK);
                date.setTextSize(15);
                date.setGravity(Gravity.CENTER);
                date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                date.setPadding(5,5,5,5);
                tr.addView(date);

                time = new TextView(PrincipalAllLiveLectureDetails.this);
                time.setText(totaltime.get(0));
                time.setTextColor(Color.BLACK);
                time.setTextSize(15);
                time.setGravity(Gravity.START);
                time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                time.setPadding(20,5,5,5);
                tr.addView(time);

                std = new TextView(PrincipalAllLiveLectureDetails.this);
                std.setText(totalstd.get(0));
                std.setTextColor(Color.BLACK);
                std.setTextSize(15);
                std.setGravity(Gravity.START);
                std.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                std.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                std.setPadding(20,5,5,5);
                tr.addView(std);

                subject = new TextView(PrincipalAllLiveLectureDetails.this);
                subject.setText(totalsubject.get(0));
                subject.setTextColor(Color.BLACK);
                subject.setTextSize(15);
                subject.setGravity(Gravity.START);
                subject.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                subject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                subject.setPadding(20,5,20,5);
                tr.addView(subject);
            }
            else if(totaldate.get(i-1).equals(totaldate.get(i))) {

                date = new TextView(PrincipalAllLiveLectureDetails.this);
                date.setText("--");
                date.setTextColor(Color.BLACK);
                date.setTextSize(15);
                date.setGravity(Gravity.CENTER);
                date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                date.setPadding(5, 5, 5, 5);
                tr.addView(date);  // Adding textView to tablerow.

                time = new TextView(PrincipalAllLiveLectureDetails.this);
                time.setText(totaltime.get(i));
                time.setTextColor(Color.BLACK);
                time.setTextSize(15);
                time.setGravity(Gravity.START);
                time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                time.setPadding(20,5,5,5);
                tr.addView(time);

                std = new TextView(PrincipalAllLiveLectureDetails.this);
                std.setText(totalstd.get(i));
                std.setTextColor(Color.BLACK);
                std.setTextSize(15);
                std.setGravity(Gravity.START);
                std.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                std.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                std.setPadding(20,5,5,5);
                tr.addView(std);

                subject = new TextView(PrincipalAllLiveLectureDetails.this);
                subject.setText(totalsubject.get(i));
                subject.setTextColor(Color.BLACK);
                subject.setTextSize(15);
                subject.setGravity(Gravity.START);
                subject.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                subject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                subject.setPadding(20,5,20,5);
                tr.addView(subject);

            }else {
                date = new TextView(PrincipalAllLiveLectureDetails.this);
                date.setText(totaldate.get(i));
                date.setTextColor(Color.BLACK);
                date.setTextSize(15);
                date.setGravity(Gravity.CENTER);
                date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                date.setPadding(5,5,5,5);
                tr.addView(date);  // Adding textView to tablerow.


                time = new TextView(PrincipalAllLiveLectureDetails.this);
                time.setText(totaltime.get(i));
                time.setTextColor(Color.BLACK);
                time.setTextSize(15);
                time.setGravity(Gravity.START);
                time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                time.setPadding(20,5,5,5);
                tr.addView(time);

                std = new TextView(PrincipalAllLiveLectureDetails.this);
                std.setText(totalstd.get(i));
                std.setTextColor(Color.BLACK);
                std.setTextSize(15);
                std.setGravity(Gravity.START);
                std.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                std.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                std.setPadding(20,5,5,5);
                tr.addView(std);

                subject = new TextView(PrincipalAllLiveLectureDetails.this);
                subject.setText(totalsubject.get(i));
                subject.setTextColor(Color.BLACK);
                subject.setTextSize(15);
                subject.setGravity(Gravity.START);
                subject.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                subject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                subject.setPadding(20,5,20,5);
                tr.addView(subject);
            }


            tl.addView(tr, new TableLayout.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        }

    }

}


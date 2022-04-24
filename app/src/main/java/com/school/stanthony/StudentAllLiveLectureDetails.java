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

public class StudentAllLiveLectureDetails extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "",month,Form1;
    Boolean isSuccess;
    PreparedStatement stmt;
    ResultSet rs;
    SharedPreferences sharedPref;
    TableLayout tl;
    TableRow tr;
    ArrayList<String> totaldate,totaltime,totalsubject;
    String getdate,gettime,getsubject;
    TextView date,subject,time;

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(StudentAllLiveLectureDetails.this);
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
                            String query = "select Convert(varchar,date,103) date,time,subjectname from livelecture\n" +
                                    "where academic=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                                    "and section=(select Batch_code from tbladmissionfeemaster where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                                    "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                                    "and std=(select Class_Name from tbladmissionfeemaster where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                                    "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                                    "and div=(select Division from tbladmissionfeemaster where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                                    "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')and month='"+month+"'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            totaldate=new ArrayList<>();
                            totaltime=new ArrayList<>();
                            totalsubject=new ArrayList<>();

                            while (rs.next()) {
                                getdate = rs.getString("date");
                                totaldate.add(getdate);

                                gettime = rs.getString("time");
                                totaltime.add(gettime);

                                getsubject = rs.getString("subjectname");
                                totalsubject.add(getsubject);
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
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
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

        tr = new TableRow(StudentAllLiveLectureDetails.this);
        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tr.setBackground(sd);

        /** Creating a TextView to add to the row **/

        date = new TextView(StudentAllLiveLectureDetails.this);
        date.setText("Date");
        date.setTextColor(Color.BLUE);
        date.setTextSize(18);
        date.setHeight(90);
        date.setGravity(Gravity.CENTER);
        date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        date.setPadding(5,5,5,5);
        tr.addView(date);  // Adding textView to tablerow.

        time = new TextView(StudentAllLiveLectureDetails.this);
        time.setText("Time");
        time.setTextColor(Color.BLUE);
        time.setTextSize(18);
        time.setHeight(90);
        time.setGravity(Gravity.START);
        time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        time.setPadding(5,5,5,5);
        tr.addView(time);  // Adding textView to tablerow.

        subject = new TextView(StudentAllLiveLectureDetails.this);
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

            tr = new TableRow(StudentAllLiveLectureDetails.this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackground(sd);

            if(i==0){
                date = new TextView(StudentAllLiveLectureDetails.this);
                date.setText(totaldate.get(0));
                date.setTextColor(Color.BLACK);
                date.setTextSize(15);
                date.setGravity(Gravity.CENTER);
                date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                date.setPadding(5,5,5,5);
                tr.addView(date);

                time = new TextView(StudentAllLiveLectureDetails.this);
                time.setText(totaltime.get(0));
                time.setTextColor(Color.BLACK);
                time.setTextSize(15);
                time.setGravity(Gravity.START);
                time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                time.setPadding(20,5,5,5);
                tr.addView(time);

                subject = new TextView(StudentAllLiveLectureDetails.this);
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

                date = new TextView(StudentAllLiveLectureDetails.this);
                date.setText("--");
                date.setTextColor(Color.BLACK);
                date.setTextSize(15);
                date.setGravity(Gravity.CENTER);
                date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                date.setPadding(5, 5, 5, 5);
                tr.addView(date);  // Adding textView to tablerow.

                time = new TextView(StudentAllLiveLectureDetails.this);
                time.setText(totaltime.get(i));
                time.setTextColor(Color.BLACK);
                time.setTextSize(15);
                time.setGravity(Gravity.START);
                time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                time.setPadding(20,5,5,5);
                tr.addView(time);

                subject = new TextView(StudentAllLiveLectureDetails.this);
                subject.setText(totalsubject.get(i));
                subject.setTextColor(Color.BLACK);
                subject.setTextSize(15);
                subject.setGravity(Gravity.START);
                subject.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                subject.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                subject.setPadding(20,5,20,5);
                tr.addView(subject);

            }else {
                date = new TextView(StudentAllLiveLectureDetails.this);
                date.setText(totaldate.get(i));
                date.setTextColor(Color.BLACK);
                date.setTextSize(15);
                date.setGravity(Gravity.CENTER);
                date.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                date.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                date.setPadding(5,5,5,5);
                tr.addView(date);  // Adding textView to tablerow.


                time = new TextView(StudentAllLiveLectureDetails.this);
                time.setText(totaltime.get(i));
                time.setTextColor(Color.BLACK);
                time.setTextSize(15);
                time.setGravity(Gravity.START);
                time.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                time.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                time.setPadding(20,5,5,5);
                tr.addView(time);

                subject = new TextView(StudentAllLiveLectureDetails.this);
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


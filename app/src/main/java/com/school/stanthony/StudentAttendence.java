package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StudentAttendence extends AppCompatActivity {

   CompactCalendarView calenderView;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    TextView show,showpresent,showtotal,showabsent,showper;
    ArrayList<String> date=new ArrayList<>();
    ArrayList<String> present=new ArrayList<>();
    Connection conn;
    String ConnectionResult = "",dateStrings1,getpresent,getacadmic,Form1,getmonths,getyear;
    Boolean isSuccess;
    PreparedStatement stmt;
    ResultSet rt,rs;
    ImageView next,previous;
    String getmonth,getnewmonth,getp,geta,gett,getper,getfirstaca,getsecondaca;
    SharedPreferences sharedPref;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendence);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        calenderView=findViewById(R.id.compactcalendar_view);
        calenderView.setUseThreeLetterAbbreviation(true);
        calenderView.shouldScrollMonth(false);
        show=findViewById(R.id.month);
        previous=findViewById(R.id.previous);
        next=findViewById(R.id.next);
        showtotal=findViewById(R.id.showtotal);
        showpresent=findViewById(R.id.showpresent);
        showabsent=findViewById(R.id.showabsent);
        showper=findViewById(R.id.showper);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getdate = show.getText().toString();
                getfirstaca = getacadmic.substring(0, 4);

                if (getdate.equals("June- " + getfirstaca)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(StudentAttendence.this);
                    builder1.setMessage("Reached The Start Of Acadmic Year "+getacadmic);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                } else {
                    final ProgressDialog progress = new ProgressDialog(StudentAttendence.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Please Wait a Moment");
                    progress.setCancelable(false);
                    progress.show();

                    Runnable progressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            calenderView.showPreviousMonth();
                            getmonth = show.getText().toString();
                            getnewmonth = getmonth.substring(0, getmonth.length() - 6);

                            ///////    For acadmic

                            try {
                                ConnectionHelper conStr1 = new ConnectionHelper();
                                conn = conStr1.connectionclasss();

                                if (conn == null) {
                                    ConnectionResult = "NO INTERNET";
                                } else {
                                    String query = "Select COUNT(Present)WorkingDays,\n" +
                                            "(Select COUNT(Present) from tblstudentattendencedetails \n" +
                                            "where StudentCode='"+Form1+"' and Present in('P') and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getnewmonth + "')Present,\n" +
                                            "(Select COUNT(Present) from tblstudentattendencedetails \n" +
                                            "where StudentCode='"+Form1+"' and Present='A' and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"')\n" +
                                            " and AttendenceMonth='" + getnewmonth + "')Absent,\n" +
                                            " CAST(round((ISNULL(((Select COUNT(Present) from tblstudentattendencedetails where StudentCode='"+Form1+"' \n" +
                                            "and Present in('P') and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getnewmonth + "')*100.0)/NULLIF((Select COUNT(Present) \n" +
                                            "from tblstudentattendencedetails where \n" +
                                            "StudentCode ='"+Form1+"'  and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getnewmonth + "' and Present in('P','A')),0),0)),0)as INT) as percentage, + '%' as per\n" +
                                            "from tblstudentattendencedetails where StudentCode='"+Form1+"'  and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getnewmonth + "' and Present in('P','a')";
                                    stmt = conn.prepareStatement(query);
                                    rs = stmt.executeQuery();
                                    while (rs.next()) {
                                        gett = rs.getString("WorkingDays");
                                        getp = rs.getString("Present");
                                        geta = rs.getString("Absent");
                                        getper = rs.getString("percentage");

                                        showtotal.setText(gett);
                                        showpresent.setText(getp);
                                        showabsent.setText(geta);
                                        showper.setText(getper + "%");
                                    }
                                    ConnectionResult = "Successful";
                                    isSuccess = true;
                                    conn.close();
                                }
                            } catch (android.database.SQLException e) {
                                isSuccess = false;
                                ConnectionResult = e.getMessage();
                            } catch (java.sql.SQLException e) {
                                e.printStackTrace();
                            }
                            progress.cancel();
                            calenderView.showCalendarWithAnimation();
                        }
                    };
                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable,1000);

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getdate = show.getText().toString();
                getsecondaca = getacadmic.substring(5, 9);

                if (getdate.equals("May- " + getsecondaca)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(StudentAttendence.this);
                    builder1.setMessage("Reached The End Of Acadmic Year "+getacadmic);
                    builder1.setCancelable(false);
                    builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog1 = builder1.create();
                    alertDialog1.show();
                } else {

                    final ProgressDialog progress = new ProgressDialog(StudentAttendence.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Please Wait a Moment");
                    progress.setCancelable(false);
                    progress.show();

                    Runnable progressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            calenderView.showNextMonth();
                            getmonth = show.getText().toString();
                            getnewmonth = getmonth.substring(0, getmonth.length() - 6);

                            ///////    For acadmic

                            try {
                                ConnectionHelper conStr1 = new ConnectionHelper();
                                conn = conStr1.connectionclasss();

                                if (conn == null) {
                                    ConnectionResult = "NO INTERNET";
                                } else {
                                    String query = "Select COUNT(Present)WorkingDays,\n" +
                                            "(Select COUNT(Present) from tblstudentattendencedetails \n" +
                                            "where StudentCode='"+Form1+"' and Present in('P') and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getnewmonth + "')Present,\n" +
                                            "(Select COUNT(Present) from tblstudentattendencedetails \n" +
                                            "where StudentCode='"+Form1+"' and Present='A' and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"')\n" +
                                            " and AttendenceMonth='" + getnewmonth + "')Absent,\n" +
                                            " CAST(round((ISNULL(((Select COUNT(Present) from tblstudentattendencedetails where StudentCode='"+Form1+"' \n" +
                                            "and Present in('P') and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getnewmonth + "')*100.0)/NULLIF((Select COUNT(Present) \n" +
                                            "from tblstudentattendencedetails where \n" +
                                            "StudentCode ='"+Form1+"'  and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getnewmonth + "' and Present in('P','A')),0),0)),0)as INT) as percentage, + '%' as per\n" +
                                            "from tblstudentattendencedetails where StudentCode='"+Form1+"'  and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getnewmonth + "' and Present in('P','a')";
                                    stmt = conn.prepareStatement(query);
                                    rs = stmt.executeQuery();
                                    while (rs.next()) {
                                        gett = rs.getString("WorkingDays");
                                        getp = rs.getString("Present");
                                        geta = rs.getString("Absent");
                                        getper = rs.getString("percentage");

                                        showtotal.setText(gett);
                                        showpresent.setText(getp);
                                        showabsent.setText(geta);
                                        showper.setText(getper + "%");
                                    }
                                    ConnectionResult = "Successful";
                                    isSuccess = true;
                                    conn.close();
                                }
                            } catch (android.database.SQLException e) {
                                isSuccess = false;
                                ConnectionResult = e.getMessage();
                            } catch (java.sql.SQLException e) {
                                e.printStackTrace();
                            }
                            progress.cancel();
                            calenderView.showCalendarWithAnimation();
                        }
                    };
                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable,1000);

                }
            }

        });

        final ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                ///////    For current month and year

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "SELECT DATENAME(MONTH,Getdate())MONTH,DATEPART(YEAR,GETDATE())YEAR";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            getmonths = rs.getString("MONTH");
                            getyear = rs.getString("YEAR");
                            show.setText(getmonths+"- "+getyear);
                        }
                        ConnectionResult = "Successful";
                        isSuccess = true;
                        conn.close();
                    }
                } catch (android.database.SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

                ///////    For acadmic

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select acadmicyear from tblcompanymaster where companycode=(\n" +
                                "Select MAX(Acadmic_Year)Acadmic_Year from tbladmissionfeemaster where Applicant_type='"+Form1+"')";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            getacadmic = rs.getString("acadmicyear");
                        }
                        ConnectionResult = "Successful";
                        isSuccess = true;
                        conn.close();
                    }
                } catch (android.database.SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

                ///////    For counting presenty

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "Select COUNT(Present)WorkingDays,\n" +
                                "(Select COUNT(Present) from tblstudentattendencedetails \n" +
                                "where StudentCode='"+Form1+"' and Present in('P') and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getmonths + "')Present,\n" +
                                "(Select COUNT(Present) from tblstudentattendencedetails \n" +
                                "where StudentCode='"+Form1+"' and Present='A' and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"')\n" +
                                " and AttendenceMonth='" + getmonths + "')Absent,\n" +
                                " CAST(round((ISNULL(((Select COUNT(Present) from tblstudentattendencedetails where StudentCode='"+Form1+"' \n" +
                                "and Present in('P') and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getmonths + "')*100.0)/NULLIF((Select COUNT(Present) \n" +
                                "from tblstudentattendencedetails where \n" +
                                "StudentCode ='"+Form1+"'  and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getmonths + "' and Present in('P','A')),0),0)),0)as INT) as percentage, + '%' as per\n" +
                                "from tblstudentattendencedetails where StudentCode='"+Form1+"'  and Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Form1+"') and AttendenceMonth='" + getmonths + "' and Present in('P','a')";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            gett = rs.getString("WorkingDays");
                            getp = rs.getString("Present");
                            geta = rs.getString("Absent");
                            getper = rs.getString("percentage");

                            showtotal.setText(gett);
                            showpresent.setText(getp);
                            showabsent.setText(geta);
                            showper.setText(getper + "%");
                        }
                        ConnectionResult = "Successful";
                        isSuccess = true;
                        conn.close();
                    }
                } catch (android.database.SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

                ///Gor all present and date
                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();
                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query1 = "SELECT convert(varchar, attendencedate, 23)attendencedate,present  from tblstudentattendencedetails\n" +
                                " where acadmic_year=(Select MAX(Acadmic_Year)Acadmic_Year from tbladmissionfeemaster where Applicant_type='"+Form1+"') and studentcode='"+Form1+"'";
                        stmt = conn.prepareStatement(query1);
                        rt = stmt.executeQuery();

                        while (rt.next()) {
                            String Applicant_type = rt.getString("attendencedate");
                            date.add(Applicant_type);

                            String type = rt.getString("present");
                            present.add(type);
                        }
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

                for(int i=0;i<date.size();i++){
                    dateStrings1=date.get(i);
                    getpresent=present.get(i);

                    String dateString=dateStrings1+"T10:00:23+01:00";
                    //   String dateString=dateStrings1+"T10:36:23+01:00";
                    if (dateString != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        Date testDate = null;
                        try {
                            testDate = sdf.parse(dateString);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if(getpresent.equals("P") || getpresent.equals("p")){
                            Event event=new Event(Color.GREEN,testDate.getTime());
                            calenderView.addEvent(event);
                        }else if(getpresent.equals("A") || getpresent.equals("a")){
                            Event event=new Event(Color.RED,testDate.getTime());
                            calenderView.addEvent(event);
                        }else if(getpresent.equals("H") || getpresent.equals("h")){
                            Event event=new Event(Color.BLUE,testDate.getTime());
                            calenderView.addEvent(event);
                        }else if(getpresent.equals("U") || getpresent.equals("u")){
                            Event event=new Event(Color.MAGENTA,testDate.getTime());
                            calenderView.addEvent(event);
                        }
                    }
                }
                progress.cancel();
                calenderView.showCalendarWithAnimation();
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable,4000);


      calenderView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
          @Override
          public void onDayClick(Date dateClicked) {

          }

          @Override
          public void onMonthScroll(Date firstDayOfNewMonth) {
           show.setText(simpleDateFormat.format(firstDayOfNewMonth));
          }
      });

    }
}

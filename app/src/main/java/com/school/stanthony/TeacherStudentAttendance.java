package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherStudentAttendance extends AppCompatActivity {
    Spinner section,std,div,day;
    TextView showstartdate;
    EditText start;
    Button btn;
    DatePickerDialog dpd;
    int mMonth, getmoonth,getdayy;
    Calendar c;
    PreparedStatement stmt;
    ResultSet rs,rt;
    Boolean isSuccess;
    Connection conn;
    SharedPreferences sharedPreferences;
    ProgressDialog progress;
    String Form1,ConnectionResult,name,createdby,getrange,checksubmitted,first,getmaxdate,showmaxxdate;
    AlertDialog alertDialog;
    String firstdate,secondate,getday,getsection,getstd,getdiv,getmaxxdate,checkpending,monthselected;
    ArrayList<String> data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_student_attendance);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        section=findViewById(R.id.section);
        std=findViewById(R.id.std);
        start=findViewById(R.id.start);
        div=findViewById(R.id.div);
        day=findViewById(R.id.day);
        btn=findViewById(R.id.btn);
        showstartdate =findViewById(R.id.showstartdate);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setFocusableInTouchMode(true);
            }
        });

        String[] days = {"Select Day","Working", "Holiday", "Un-Instructional" };
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.spinner11, days);
        day.setAdapter(adapter1);

        /////////////////////////////// start time

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(TeacherStudentAttendance.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                //       showstartdate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                                //       start.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                getmoonth = monthOfYear + 1;
                                //    getmoonth = dayOfMonth;
                                if(String.valueOf(getmoonth).length()==1){
                                    start.setText(dayOfMonth + "/0" + (monthOfYear + 1) + "/" + year);
                                    showstartdate.setText("0"+(monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                                }else{
                                    start.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    showstartdate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                                }

//                                String input_date = start.getText().toString();
//                                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
//                                Date dt1 = null;
//                                try {
//                                    dt1 = format1.parse(input_date);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                DateFormat format2 = new SimpleDateFormat("EEEE");
//                                String finalDay = format2.format(dt1);
//                                showday.setText(finalDay);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        progress = new ProgressDialog(TeacherStudentAttendance.this);
        progress.setTitle("Loading");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                /////////  Bydefault StartDate

                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select CONVERT(varchar(10),getdate(),101) sysdate ,CONVERT(varchar(10),getdate(),103) showdate";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();

                        while (rs.next()) {
                            String fullname = rs.getString("showdate");
                            start.setText(fullname);

                            String fullnam = rs.getString("sysdate");
                            showstartdate.setText(fullnam);
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

                ///////for tecaher name

                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select name from tbl_HRStaffnew where staffuser='"+Form1+"' and \n" +
                                "acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"') ";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        while (rs.next()) {
                            name = rs.getString("name");
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

                /////////////////// For Section
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null)
                    {
                        ConnectionResult="NO INTERNET";
                    }
                    else
                    {
                        String query = "select batchcode,class_name,division from tblclassmaster where acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"') \n" +
                                "and assigneteacher='"+name+"'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data = new ArrayList<String>();
                        ArrayList<String> data2 = new ArrayList<String>();
                        ArrayList<String> data3 = new ArrayList<String>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("batchcode");
                            data.add(fullname);

                            String class1 = rs.getString("class_name");
                            data2.add(class1);

                            String div = rs.getString("division");
                            data3.add(div);

                        }
                        String[] array = data.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(TeacherStudentAttendance.this,R.layout.spinner11, data);
                        section.setAdapter(NoCoreAdapter);

                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(TeacherStudentAttendance.this,R.layout.spinner11, data2);
                        std.setAdapter(NoCoreAdapter2);

                        String[] array3 = data3.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter3 = new ArrayAdapter(TeacherStudentAttendance.this,R.layout.spinner11, data3);
                        div.setAdapter(NoCoreAdapter3);

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

                progress.cancel();
            }
        }, 2000);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(day.getSelectedItem().toString().trim().equalsIgnoreCase("Select Day")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(TeacherStudentAttendance.this);
                    builder.setMessage("Please Select Day");
                    builder.setCancelable(true);
                    alertDialog = builder.create();
                    alertDialog.show();
                }else {
                    progress = new ProgressDialog(TeacherStudentAttendance.this);
                    progress.setTitle("Loading");
                    progress.setMessage("Please Wait a Moment");
                    progress.setCancelable(false);
                    progress.show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            firstdate = start.getText().toString();
                            secondate = showstartdate.getText().toString();
                            getday=day.getSelectedItem().toString();
                            getsection=section.getSelectedItem().toString();
                            getstd=std.getSelectedItem().toString();
                            getdiv=div.getSelectedItem().toString();

                            ////For last attendance date
                            try {
                                ConnectionHelper conStr1 = new ConnectionHelper();
                                conn = conStr1.connectionclasss();

                                if (conn == null) {
                                    ConnectionResult = "NO INTERNET";
                                } else {
                                    String query = "select isnull(convert (varchar,( max(AttendenceDate)),110),0) AttendenceDate from tblstudentattendencedetails where\n" +
                                            "acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"')and \n" +
                                            "batch_code='"+getsection+"' and class_name='"+getstd+"' and division='"+getdiv+"'";
                                    stmt = conn.prepareStatement(query);
                                    rs = stmt.executeQuery();

                                    while (rs.next()) {
                                        getmaxdate = rs.getString("AttendenceDate");
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

                            ////////////////
                            try {
                                ConnectionHelper conStr = new ConnectionHelper();
                                conn = conStr.connectionclasss();

                                if (conn == null)
                                {
                                    ConnectionResult="NO INTERNET";
                                }
                                else
                                {
                                    String query = "select Staff_ID from tbl_HRStaffnew where StaffUser='"+Form1+"' and \n" +
                                            "acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"')";
                                    stmt = conn.prepareStatement(query);
                                    rs = stmt.executeQuery();
                                    while (rs.next())
                                    {
                                        createdby = rs.getString("Staff_ID");

                                    }
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

                            ////check date range
                            try {
                                ConnectionHelper conStr1 = new ConnectionHelper();
                                conn = conStr1.connectionclasss();

                                if (conn == null) {
                                    ConnectionResult = "NO INTERNET";
                                } else {
                                    String query = "Select 100 'checkrange' where DATEDIFF(D,CONVERT(varchar(10),'"+secondate+"',101),(Select Start_school from tblclassmaster \n" +
                                            "where batchcode='"+getsection+"' and class_name='"+getstd+"' and division='"+getdiv+"'\n" +
                                            "and acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"')))<=0\n" +
                                            "and DATEDIFF(D,CONVERT(varchar(10),'"+secondate+"',101),\n" +
                                            "(Select End_school from tblclassmaster where batchcode='"+getsection+"' and class_name='"+getstd+"' and division='"+getdiv+"'\n" +
                                            "and acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"')))>=0";
                                    stmt = conn.prepareStatement(query);
                                    rs = stmt.executeQuery();
                                    data2 = new ArrayList<String>();
                                    while (rs.next()) {
                                        getrange = rs.getString("checkrange");
                                        data2.add(getrange);
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

                            if(data2.size()==0){
                                progress.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherStudentAttendance.this);
                                builder.setMessage("Attendance Date Not In Range");
                                builder.setCancelable(true);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                alertDialog = builder.create();
                                alertDialog.show();
                            }
                            else if(getmaxdate.equals("0")){

                                ///////////////////First Attendace date

                                try {
                                    ConnectionHelper conStr = new ConnectionHelper();
                                    conn = conStr.connectionclasss();

                                    if (conn == null)
                                    {
                                        ConnectionResult="NO INTERNET";
                                    }
                                    else
                                    {
                                        String query = "select convert (varchar,(start_school),103) start_school from tblclassmaster \n" +
                                                "where batchcode='"+getsection+"' and class_name='"+getstd+"' and division='"+getdiv+"' and\n" +
                                                "acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"')";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();
                                        while (rs.next())
                                        {
                                            first = rs.getString("start_school");
                                        }
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

                                progress.cancel();
                                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherStudentAttendance.this);
                                builder.setMessage("Please Take Attendance Of Date "+first+" From Web Application And Then You Can Take Further Date Attendances");
                                builder.setCancelable(true);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });

                                alertDialog = builder.create();
                                alertDialog.show();
                            }
                            else {

                                String newfirstdate=firstdate.substring(1,2);
                                if(newfirstdate.equals("/")){
                                    firstdate="0"+firstdate;
                                }
                                ////For submitted date check
                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "if not exists (Select * from tblstudentattendencedetails where Convert(varchar(10),AttendenceDate,103)=Convert(varchar(10),'"+firstdate+"',103)\n" +
                                                "and batch_code='"+getsection+"' and class_name='"+getstd+"' and division='"+getdiv+"' ) begin select 200 'check' end else begin select -200 'check' end";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();

                                        while (rs.next()) {
                                            checksubmitted = rs.getString("check");
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
                                if(checksubmitted.equals("-200")){
                                    progress.cancel();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(TeacherStudentAttendance.this);
                                    builder.setMessage("It seems you have already submitted attendance of date "+firstdate);
                                    builder.setTitle("Submitted");
                                    builder.setCancelable(true);

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    alertDialog = builder.create();
                                    alertDialog.show();
                                }else {
                                    ////For last attendance date+1
                                    try {
                                        ConnectionHelper conStr1 = new ConnectionHelper();
                                        conn = conStr1.connectionclasss();

                                        if (conn == null) {
                                            ConnectionResult = "NO INTERNET";
                                        } else {
                                            String query = "select isnull(convert (varchar,( max(AttendenceDate+1)),110),0) AttendenceDate,\n" +
                                                    "isnull(convert (varchar,( max(AttendenceDate+1)),103),0) ShowAttendenceDate from tblstudentattendencedetails where\n" +
                                                    "acadmic_year=(select top 1 selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"')and \n" +
                                                    "batch_code='"+getsection+"' and class_name='"+getstd+"' and division='"+getdiv+"'";
                                            stmt = conn.prepareStatement(query);
                                            rs = stmt.executeQuery();

                                            while (rs.next()) {
                                                getmaxxdate = rs.getString("AttendenceDate");
                                                showmaxxdate = rs.getString("ShowAttendenceDate");
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

                                    ////For pendind date
                                    try {
                                        ConnectionHelper conStr1 = new ConnectionHelper();
                                        conn = conStr1.connectionclasss();

                                        if (conn == null) {
                                            ConnectionResult = "NO INTERNET";
                                        } else {
                                            String query = "Select DateDiff(Day,'"+getmaxdate+"','"+secondate+"' )'check'";
                                            stmt = conn.prepareStatement(query);
                                            rs = stmt.executeQuery();

                                            while (rs.next()) {
                                                checkpending = rs.getString("check");
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

                                    if(!checkpending.equals("1")){
                                        progress.cancel();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherStudentAttendance.this);
                                        builder.setMessage("Please enter pending attendance from date "+showmaxxdate);
                                        builder.setTitle("Pending");
                                        builder.setCancelable(true);

                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                                proceed();
                                            }
                                        });

                                        alertDialog = builder.create();
                                        alertDialog.show();
                                    }else {
                                        proceed();
                                    }
                                }

                            }
                        }
                    }, 1000);

                }
            }
        });

    }

    public void proceed(){

        if (getmoonth == 1) {
            monthselected = "January";
        } else if (getmoonth == 2) {
            monthselected = "February";
        } else if (getmoonth == 3) {
            monthselected = "March";
        } else if (getmoonth == 4) {
            monthselected = "April";
        } else if (getmoonth == 5) {
            monthselected = "May";
        } else if (getmoonth == 6) {
            monthselected = "June";
        } else if (getmoonth == 7) {
            monthselected = "July";
        } else if (getmoonth == 8) {
            monthselected = "August";
        } else if (getmoonth == 9) {
            monthselected = "September";
        } else if (getmoonth == 10) {
            monthselected = "October";
        } else if (getmoonth == 11) {
            monthselected = "November";
        } else if (getmoonth == 12) {
            monthselected = "December";
        }

        if(getday.equals("Holiday")){
            progress.cancel();
            Intent i = new Intent(getApplicationContext(), TakeAttendaceHoliday.class);
            i.putExtra("section", getsection);
            i.putExtra("section1", getstd);
            i.putExtra("section2", getdiv);
            i.putExtra("section3", secondate);
            i.putExtra("section4", monthselected);
            i.putExtra("section6", createdby);
            startActivity(i);
        }else if(getday.equals("Un-Instructional")){
            progress.cancel();
            Intent i = new Intent(getApplicationContext(), TakeAttendanceUnconditional.class);
            i.putExtra("section", getsection);
            i.putExtra("section1", getstd);
            i.putExtra("section2", getdiv);
            i.putExtra("section3", secondate);
            i.putExtra("section4", monthselected);
            i.putExtra("section6", createdby);
            startActivity(i);
        }else {
            progress.cancel();
            Intent i = new Intent(getApplicationContext(), TakeAttendance.class);
            i.putExtra("section", getsection);
            i.putExtra("section1", getstd);
            i.putExtra("section2", getdiv);
            i.putExtra("section3", secondate);
            i.putExtra("section4", monthselected);
            i.putExtra("section6", createdby);
            startActivity(i);

        }

    }

}
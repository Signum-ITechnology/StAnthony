package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherUploadVideo extends AppCompatActivity {

    String ConnectionResult,code,msg,section, std, div,code1,section1,startdate,getnotice,getacademic,staffid,getday,getsubject,nstd;
    Boolean isSuccess;
    EditText start,notice,subdate,chapter;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs,rt;
    Spinner s1, s2, s3,subject,topic;
    Button btn;
    TextView note;
    String ConnectionURL,row,name,Form1,getnot,gettopic,gettop,getchapter,getchap,submissiondate,toppic;
    SharedPreferences sharedPreferences;
    ArrayAdapter<String> adapter1, adapter2, adapter3;
    ArrayList<String> data2 = new ArrayList<>();
    ProgressDialog progress;
    TextView show,starttext,endtext;
    AlertDialog alertDialog;
    TextView showstartdate,showday,showsubdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_upload_video);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        btn = findViewById(R.id.btn);
        subject = findViewById(R.id.subject);
        start = findViewById(R.id.start);
        notice = findViewById(R.id.notice);
        showstartdate = findViewById(R.id.showstartdate);
        showday = findViewById(R.id.showday);
        showsubdate=findViewById(R.id.showsubdate);
        subdate=findViewById(R.id.subdate);
        topic=findViewById(R.id.topic);
        chapter=findViewById(R.id.chapter);

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setFocusableInTouchMode(true);
            }
        });

        subdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subdate.setFocusableInTouchMode(true);
            }
        });


        chapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chapter.setFocusableInTouchMode(true);
            }
        });


        /////////////////////////////// start time

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(TeacherUploadVideo.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                showstartdate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                                start.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                getmoonth = monthOfYear + 1;

                                String input_date = start.getText().toString();
                                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                                Date dt1 = null;
                                try {
                                    dt1 = format1.parse(input_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                DateFormat format2 = new SimpleDateFormat("EEEE");
                                String finalDay = format2.format(dt1);
                                showday.setText(finalDay);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        /////////////////////////////// sub date
        subdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(TeacherUploadVideo.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                showsubdate.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                                subdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        /////////  Bydefault StartDate

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select CONVERT(varchar(10),getdate(),110) sysdate ,CONVERT(varchar(10),getdate(),103) showdate,datename(dw,getdate())day";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data = new ArrayList<>();
                ArrayList<String> data2 = new ArrayList<>();

                while (rs.next()) {
                    String fullname = rs.getString("showdate");
                    data.add(fullname);
                    start.setText(data.get(0));

                    String fullnam = rs.getString("sysdate");
                    data2.add(fullnam);
                    showstartdate.setText(data2.get(0));

                    String day = rs.getString("day");
                    showday.setText(day);
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
        ////For Academic

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select selectedaca,name,staff_id from tbl_hrstaffnew where staffuser='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getacademic = rs.getString("selectedaca");
                    name = rs.getString("name");
                    staffid = rs.getString("staff_id");
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

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select batchCode from tblbatchcodemaster where \n" +
                        "acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next()) {
                    String fullname = rs.getString("batchcode");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                s1.setAdapter(NoCoreAdapter2);
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

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section = s1.getSelectedItem().toString();
                s2.setVisibility(View.VISIBLE);
                /////////////////// For Class
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select distinct(class_name) from tblclassmaster where \n" +
                                "Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and \n" +
                                "batchcode='" + section + "'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next()) {
                            String fullname = rs.getString("class_name");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                        s2.setAdapter(NoCoreAdapter2);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section1 = s2.getSelectedItem().toString();
                s3.setVisibility(View.VISIBLE);

                /////////////////// For Div
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select distinct(division) from tblclassmaster where \n" +
                                "Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and \n" +
                                "class_name='" + section1 + "'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next()) {
                            String fullname = rs.getString("division");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                        s3.setAdapter(NoCoreAdapter2);
                        // NoCoreAdapter2.add("YYY");
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section = s1.getSelectedItem().toString();
                section1 = s2.getSelectedItem().toString();

                if (section.equals("JR.COLLEGE")) {
                    /////////////////// For Div
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select distinct title,subjectcode from  tbljrcoursemaster where acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                                    "and section='" + section + "' and batchcode='" + section1 + "' order by subjectcode";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<>();
                            while (rs.next()) {
                                String fullname = rs.getString("title");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                            subject.setAdapter(NoCoreAdapter2);
                            // NoCoreAdapter2.add("YYY");
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
                } else {
                    /////////////////// For Div
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select distinct title,subjectcode from  tblcoursemaster where acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') \n" +
                                    "and batchcode='" + section + "' and class_name='" + section1 + "' order by subjectcode";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<>();
                            while (rs.next()) {
                                String fullname = rs.getString("title");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                            subject.setAdapter(NoCoreAdapter2);
                            // NoCoreAdapter2.add("YYY");
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                section = s1.getSelectedItem().toString();
                section1 = s2.getSelectedItem().toString();
                getsubject=subject.getSelectedItem().toString();

                /////////////////////For Batch
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select textassignment from tbl_topic where section='"+section+"' and standard='"+section1+"' \n" +
                                "and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                                "and subjectname='"+getsubject+"'";
                        stmt = conn.prepareStatement(query);
                        rt = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<String>();
                        while (rt.next()) {
                            String fullname = rt.getString("textassignment");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner33, data2);
                        topic.setAdapter(NoCoreAdapter2);
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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    toppic=topic.getSelectedItem().toString();
                }catch (Exception e){
                    toppic="0";
                }
                if (toppic.equals("0")) {
                    Toast.makeText(getApplicationContext(), "Please Select Topic", Toast.LENGTH_LONG).show();
                }
                else if (chapter.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Add Chapter", Toast.LENGTH_LONG).show();
                }
                else if (notice.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Add Some Link", Toast.LENGTH_LONG).show();
                }
                else if (subdate.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Select Submission Date", Toast.LENGTH_LONG).show();
                } else {

                    final ProgressDialog progress = new ProgressDialog(TeacherUploadVideo.this);
                    progress.setTitle("Adding Vide Link");
                    progress.setMessage("Please Wait a Moment");
                    progress.setCancelable(false);
                    progress.show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                section = s1.getSelectedItem().toString();
                                std = s2.getSelectedItem().toString();
                                div = s3.getSelectedItem().toString();
                                getday = showday.getText().toString();
                                startdate = start.getText().toString();
                                getnotice = notice.getText().toString();
                                submissiondate=showsubdate.getText().toString();
                                getnot = getnotice.replace("'", "''");
                                getsubject = subject.getSelectedItem().toString();
                                gettopic = topic.getSelectedItem().toString();
                                gettop = gettopic.replace("'", "''");
                                getchapter = chapter.getText().toString();
                                getchap = getchapter.replace("'", "''");
                            } catch (Exception e) {
                            }

                            String msg = "unknown";
                            try {
                                ConnectionHelper conStr1 = new ConnectionHelper();
                                conn = conStr1.connectionclasss();

                                if (conn == null) {
                                    msg = "Check Your Internet Access";
                                } else {
                                    String commands = "insert into tblhomeworkentry (date,day,batch_code,class_id,division,acadmic_year,created_on,\n" +
                                            "created_by,subject,topic,teachar_name,chapter_name,submission_date,link,approval,category)\n" +
                                            "values('"+startdate+"','"+getday+"','"+section+"','"+section1+"','"+div+"','"+getacademic+"',getdate(),'"+staffid+"','"+getsubject+"','"+gettop+"','"+name+"','"+getchap+"','"+submissiondate+"','"+getnot+"','0','3')";
                                    PreparedStatement preStmt = conn.prepareStatement(commands);
                                    preStmt.executeUpdate();
                                }
                                conn.close();
                            } catch (android.database.SQLException ex) {
                                msg = ex.getMessage().toString();
                                Log.d("Error no 1:", msg);
                            } catch (IOError ex) {
                                msg = ex.getMessage().toString();
                                Log.d("Error no 2:", msg);
                            } catch (AndroidRuntimeException ex) {
                                msg = ex.getMessage().toString();
                                Log.d("Error no 3:", msg);
                            } catch (NullPointerException ex) {
                                msg = ex.getMessage().toString();
                                Log.d("Error no 4:", msg);
                            } catch (Exception ex) {
                                msg = ex.getMessage().toString();
                                Log.d("Error no 5:", msg);
                            }

                            progress.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(TeacherUploadVideo.this);
                            builder.setMessage("Link Added");
                            builder.setCancelable(false);
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    notice.setText("");
                                    chapter.setText("");

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }, 800);
                }
            }
        });
    }

}

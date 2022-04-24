package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import androidx.fragment.app.Fragment;

public class TeacherAddHomework extends Fragment {

    String ConnectionResult,section, std, div,startdate,getnotice,getacademic,staffid,getday,getsubject;
    Boolean isSuccess;
    EditText start,notice,subdate,chapter,link;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs,rt;
    RadioGroup radioGroup;
    RadioButton radiotext;
    Spinner s1, s2, s3,subject,topic;
    Button btn;
    String name,Form1,getnot,gettopic,gettop,getchapter,getchap,submissiondate,toppic,getlink,getlk;
    SharedPreferences sharedPreferences;
    TextView showstartdate,showday,showsubdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_teacher_add_homework, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        s1 = view.findViewById(R.id.s1);
        s2 = view.findViewById(R.id.s2);
        s3 = view.findViewById(R.id.s3);
        btn = view.findViewById(R.id.btn);
        subject = view.findViewById(R.id.subject);
        start = view.findViewById(R.id.start);
        notice = view.findViewById(R.id.notice);
        showstartdate = view.findViewById(R.id.showstartdate);
        showday = view.findViewById(R.id.showday);
        showsubdate=view.findViewById(R.id.showsubdate);
        subdate=view.findViewById(R.id.subdate);
        topic=view.findViewById(R.id.topic);
        chapter=view.findViewById(R.id.chapter);
        link=view.findViewById(R.id.link);
        radioGroup=view.findViewById(R.id.group1);

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

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                link.setFocusableInTouchMode(true);
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

                dpd = new DatePickerDialog(getContext(),
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

                dpd = new DatePickerDialog(getContext(),
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
                String query = "select CONVERT(varchar(10),getdate(),103) showdate,datename(dw,getdate())day";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String date = rs.getString("showdate");
                    start.setText(date);

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
        /////////////////// For Teacher Name
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select name,staff_id,acadmic_year from tbl_HRStaffnew where staffuser='"+Form1+"' and\n" +
                        "acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    name = rs.getString("name");
                    staffid = rs.getString("staff_id");
                    getacademic = rs.getString("acadmic_year");
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
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(), R.layout.spinner11, data2);
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
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(), R.layout.spinner11, data2);
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

                std = s2.getSelectedItem().toString();
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
                                "class_name='" + std + "'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next()) {
                            String fullname = rs.getString("division");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(), R.layout.spinner11, data2);
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
                std = s2.getSelectedItem().toString();

                if (section.equals("JR.COLLEGE")) {
                    /////////////////// For Div
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select distinct title,subjectcode from  tbljrcoursemaster where acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                                    "and section='" + section + "' and batchcode='" + std + "' order by subjectcode";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<>();
                            while (rs.next()) {
                                String fullname = rs.getString("title");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(), R.layout.spinner11, data2);
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
                                    "and batchcode='" + section + "' and class_name='" + std + "' order by subjectcode";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<>();
                            while (rs.next()) {
                                String fullname = rs.getString("title");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(), R.layout.spinner11, data2);
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
                std = s2.getSelectedItem().toString();
                getsubject=subject.getSelectedItem().toString();

                /////////////////////For Batch
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select textassignment from tbl_topic where section='"+section+"' and standard='"+std+"' \n" +
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
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(), R.layout.spinner33, data2);
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
                    Toast.makeText(getContext(), "Please Select Topic", Toast.LENGTH_LONG).show();
                }
                else if (chapter.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Add Chapter", Toast.LENGTH_LONG).show();
                }
                else if (notice.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Add Some Homework", Toast.LENGTH_LONG).show();
                }
                else if (subdate.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Select Submission Date", Toast.LENGTH_LONG).show();
                } else {

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
                        getlink = link.getText().toString();
                        getlk = getlink.replace("'", "''");
                    } catch (Exception e) {
                    }

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radiotext = view.findViewById(selectedId);
                    String text = radiotext.getText().toString();

                    if(text.equals("Text")){
                        addtext();
                    }else if(text.equals("Pdf Link")){
                        if (link.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Please Add Some Pdf Link", Toast.LENGTH_LONG).show();
                        }else {
                            addpdf();
                        }
                    }else if(text.equals("Video Link")){
                        if (link.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Please Add Some Video Link", Toast.LENGTH_LONG).show();
                        }else {
                            addvideo();
                        }
                    }

                }
            }
        });
        return view;
    }


    public void addtext(){

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Adding Homework");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String msg = "unknown";
                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        msg = "Check Your Internet Access";
                    } else {
                        String commands = "insert into tblhomeworkentry (date,day,batch_code,class_id,division,homeworkdesciption,acadmic_year,created_on,\n" +
                                "created_by,subject,teachar_name,topic,chapter_name,submission_date,approval,category)\n" +
                                "values('"+startdate+"','"+getday+"','"+section+"','"+std+"','"+div+"','"+getnot+"','"+getacademic+"',getdate(),\n" +
                                "'"+staffid+"','"+getsubject+"','"+name+"','"+gettop+"','"+getchap+"','"+submissiondate+"','0','1')";
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("HomeWork Added");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        notice.setText("");
                        start.setText("");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }, 800);

    }

    public void addpdf(){

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Adding Pdf Link");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String msg = "unknown";
                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        msg = "Check Your Internet Access";
                    } else {
                        String commands = "insert into tblhomeworkentry (date,day,batch_code,class_id,division,homeworkdesciption,acadmic_year,created_on,\n" +
                                "created_by,subject,teachar_name,topic,chapter_name,submission_date,filename,filepath,approval,category)\n" +
                                "values('"+startdate+"','"+getday+"','"+section+"','"+std+"','"+div+"','"+getnot+"','"+getacademic+"',getdate(),\n" +
                                "'"+staffid+"','"+getsubject+"','"+name+"','"+gettop+"','"+getchap+"','"+submissiondate+"','Mobile','"+getlk+"','0','2')";
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Pdf Link Added");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        notice.setText("");
                        start.setText("");
                        link.setText("");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }, 800);

    }

    public void addvideo(){

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Adding Video Link");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String msg = "unknown";
                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        msg = "Check Your Internet Access";
                    } else {
                        String commands = "insert into tblhomeworkentry (date,day,batch_code,class_id,division,homeworkdesciption,acadmic_year,created_on,\n" +
                                "created_by,subject,teachar_name,topic,chapter_name,submission_date,link,approval,category)\n" +
                                "values('"+startdate+"','"+getday+"','"+section+"','"+std+"','"+div+"','"+getnot+"','"+getacademic+"',getdate(),\n" +
                                "'"+staffid+"','"+getsubject+"','"+name+"','"+gettop+"','"+getchap+"','"+submissiondate+"','"+getlk+"','0','3')";
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Video Link Added");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        notice.setText("");
                        start.setText("");
                        link.setText("");
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }, 800);

    }

}
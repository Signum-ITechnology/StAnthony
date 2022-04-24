package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class TeacherLectureEntry extends Fragment {

    String ConnectionResult,section,std, div,startdate,getnotice,getacademic,staffid,getsubject,getsub;
    Boolean isSuccess;
    EditText start,description,joincode,joinpassword;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1, s2, s3,subject,time;
    Button btn;
    String Form1,name,getnot,getjoincode,getjoinpassword,getmonth,gettime;
    SharedPreferences sharedPreferences;
    AlertDialog alertDialog;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(getContext());
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    createid();
                    createpassword();
                    progress.dismiss();
                }
            }, 2000);
        }};

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(getContext());
            progress.setTitle("Loading Time Slots");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadnewtimeslot();
                    progress.dismiss();
                    Toast.makeText(getContext(), ""+start.getText().toString(), Toast.LENGTH_LONG).show();
                }
            }, 2000);
        }};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_lecture_entry, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        s1 = view.findViewById(R.id.s1);
        s2 = view.findViewById(R.id.s2);
        s3 = view.findViewById(R.id.s3);
        btn = view.findViewById(R.id.btn);
        start=view.findViewById(R.id.start);
        time=view.findViewById(R.id.time);
        description=view.findViewById(R.id.notice);
        subject=view.findViewById(R.id.subject);
        joincode=view.findViewById(R.id.create);
        joinpassword=view.findViewById(R.id.password);

        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description.setFocusableInTouchMode(true);
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
                                getmoonth = monthOfYear + 1;
                                int daylength=String.valueOf(dayOfMonth).length();
                                int monthlength=String.valueOf(getmoonth).length();

                                if(daylength==1 && monthlength==1){
                                    start.setText( "0"+dayOfMonth + "/0"+(monthOfYear + 1)+ "/" + year);
                                }else if(daylength==1){
                                    start.setText( "0"+dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                                }else if(monthlength==1){
                                    start.setText( dayOfMonth + "/0"+(monthOfYear + 1)+ "/" + year);
                                }else {
                                    start.setText( dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                                }
                                mainHandler2.post(myRunnable2);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        mainHandler.post(myRunnable);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    section = s1.getSelectedItem().toString();
                    std = s2.getSelectedItem().toString();
                    div = s3.getSelectedItem().toString();
                    startdate = start.getText().toString();
                }catch (Exception e){}
                try{
                    getsubject = subject.getSelectedItem().toString();
                }catch (Exception e){
                    getsubject="0";
                }
                try{
                    gettime = time.getSelectedItem().toString();
                }catch (Exception e){
                    gettime="0";
                }

                try{
                    getnotice = description.getText().toString();
                }catch (Exception e){
                    getnotice="No Details";
                }
                if (gettime.equals("0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Select Time Slot");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                } else if (getsubject.equals("0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Select Book Name");
                    builder.setCancelable(false);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                         dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    startdate = start.getText().toString();
                    getsubject = subject.getSelectedItem().toString();
                    getjoincode=joincode.getText().toString();
                    getjoinpassword=joinpassword.getText().toString();
                    getsub=getsubject.replace("'","''");
                    getnotice = description.getText().toString();
                    getnot=getnotice.replace("'","''");
                    section = s1.getSelectedItem().toString();
                    std = s2.getSelectedItem().toString();
                    div = s3.getSelectedItem().toString();

                    final ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setTitle("Creating Live Lecture");
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
                                    String commands = "insert into LiveLecture values('"+startdate+"','"+gettime+"','"+section+"','"+std+"','"+div+"','"+getsub+"','"+getjoincode+"','"+getjoinpassword+"','"+getnot+"','0','"+staffid+"',getdate(),'"+getacademic+"','"+getmoonth+"')";
                                    PreparedStatement preStmt = conn.prepareStatement(commands);
                                    preStmt.executeUpdate();
                                }
                                conn.close();
                            } catch (SQLException ex) {
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
                            builder.setMessage("Live Lecture Created");
                            builder.setCancelable(false);
                            builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    btn.setVisibility(View.INVISIBLE);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }, 2000);
                }
            }
        });

        return view;
    }


    private void loaddata() {

        /////////  Bydefault StartDate

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select CONVERT(varchar(10),getdate(),103) showdate,MONTH(GETDATE()) month";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data = new ArrayList<>();

                while (rs.next())
                {
                    String fullname = rs.getString("showdate");
                    data.add(fullname);
                    start.setText(data.get(0));

                    getmonth = rs.getString("month");
                    getmoonth=Integer.parseInt(getmonth);
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
        } catch (SQLException e) {
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
                String query = "select distinct batchcode from tblclassmaster where acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                while (rs.next())
                {
                    String fullname = rs.getString("batchcode");
                    data2.add(fullname);
                }
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(),R.layout.spinner11, data2);
                s1.setAdapter(NoCoreAdapter2);
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


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section=s1.getSelectedItem().toString();
                s2.setVisibility(View.VISIBLE);
                /////////////////// For Class
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null)
                    {
                        ConnectionResult="NO INTERNET";
                    }
                    else
                    {
                        String query = "select batch_for from tblbatchmaster where \n" +
                                "Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and \n" +
                                "batchcode='" + section + "'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<String>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("batch_for");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(),R.layout.spinner11, data2);
                        s2.setAdapter(NoCoreAdapter2);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                std=s2.getSelectedItem().toString();
                s3.setVisibility(View.VISIBLE);

                /////////////////// For Div
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null)
                    {
                        ConnectionResult="NO INTERNET";
                    }
                    else
                    {
                        String query = "select distinct(division) from tblclassmaster where \n" +
                                "Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and \n" +
                                "class_name='"+std+"'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<String>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("division");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(),R.layout.spinner11, data2);
                        s3.setAdapter(NoCoreAdapter2);
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

                /////////////////// For Div
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select bookname from tblstudentbookmaster where acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                                "and batch_code='"+section+"' and class='"+std+"' order by bookid";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next()) {
                            String fullname = rs.getString("bookname");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(), R.layout.spinner11, data2);
                        subject.setAdapter(NoCoreAdapter2);
                        loadtimeslot();
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void createid() {

        Random rnd = new Random();
        int n = 10000000 + rnd.nextInt(90000000);
        String code=String.valueOf(n);
        joincode.setText(code);

    }

    private void createpassword() {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("mmss");
        String time = dateFormat.format(new Date());

        char[] chars1 = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random random1 = new Random();
        for (int i = 0; i < 4; i++)
        {
            char c1 = chars1[random1.nextInt(chars1.length)];
            sb1.append(c1);
        }
        String random_string = sb1.toString();
        joinpassword.setText(random_string+time);

    }

    private void loadtimeslot(){

        section = s1.getSelectedItem().toString();
        std = s2.getSelectedItem().toString();
        div = s3.getSelectedItem().toString();
        startdate = start.getText().toString();

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select distinct timeslot from tblLiveLectureTimeSlotEntry a\n" +
                        "where a.acadmicyear=(select Max(acadmic_year) from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and a.batch='"+section+"' and a.classname='"+std+"' and a.division='"+div+"'\n" +
                        "and timeslot not in(select time from livelecture where academic=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and section='"+section+"' and std='"+std+"' and div='"+div+"' and date='"+startdate+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                while (rs.next())
                {
                    String fullname = rs.getString("timeslot");
                    data2.add(fullname);
                }
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(),R.layout.spinner11, data2);
                time.setAdapter(NoCoreAdapter2);
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

    }

    private void loadnewtimeslot(){

        section = s1.getSelectedItem().toString();
        std = s2.getSelectedItem().toString();
        div = s3.getSelectedItem().toString();
        startdate = start.getText().toString();

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select distinct timeslot from tblLiveLectureTimeSlotEntry a,\n" +
                        "where a.acadmicyear=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and a.batch='"+section+"' and a.classname='"+std+"' and a.division='"+div+"'\n" +
                        "and timeslot not in(select time from livelecture where academic=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and section='"+section+"' and std='"+std+"' and div='"+div+"' and date='"+startdate+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                while (rs.next())
                {
                    String fullname = rs.getString("timeslot");
                    data2.add(fullname);
                }
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(),R.layout.spinner11, data2);
                time.setAdapter(NoCoreAdapter2);
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

    }

}
package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AssignmentStatusEntry extends AppCompatActivity {

    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess,success;
    Connection conn;
    ProgressDialog progress;
    ListView listView;
    AlertDialog alertDialog;
    ArrayList<String> rollno = new ArrayList<>();
    ArrayList<String> stucode = new ArrayList<>();
    String ConnectionResult,getsection,gettopic,getchapter,getstd,getdiv,getsubject;
    String getcountt,gethw,getstaffid,nameget,getdate,idget,Form1,getacadmic;
    String getcat,pdflink,videolink;
    ArrayList<ClassListItems9> arraylist;
    ArrayList<ClassListItems9> itemArrayList;
    ConnectionHelper connectionClass;
    MyAppAdapter myAppAdapter;
    MyAppAdapter2 myAppAdapter2;
    MyAppAdapter3 myAppAdapter3;
    SharedPreferences sharedPreferences;
    Dialog myDialog;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_status_entry);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        try {
            getsection = getIntent().getExtras().getString("section");
            getstd = getIntent().getExtras().getString("std");
            getdiv = getIntent().getExtras().getString("div");
            getsubject = getIntent().getExtras().getString("subject");
            getchapter = getIntent().getExtras().getString("chapter");
            gettopic = getIntent().getExtras().getString("topic");
            gethw = getIntent().getExtras().getString("hw");
            getstaffid = getIntent().getExtras().getString("sid");
            getcat = getIntent().getExtras().getString("cat");
            pdflink = getIntent().getExtras().getString("pdf");
            videolink = getIntent().getExtras().getString("video");
        }catch (Exception e){}

        listView=findViewById(R.id.list);
        myDialog = new Dialog(this);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems9>();

        mainHandler.post(myRunnable);

    }

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(AssignmentStatusEntry.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(getcat.equals("1")){
                        loaddata();
                    }else if(getcat.equals("2")){
                        loaddata2();
                    }else if(getcat.equals("3")){
                        loaddata3();
                    }

                    progress.dismiss();
                }
            }, 2000);
        }};

    private void loaddata() {

        /////////  Bydefault StartDate

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select CONVERT(varchar(10),getdate(),103) showdate";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getdate = rs.getString("showdate");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /////////  get acadmic_year

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = " select selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"' ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getacadmic = rs.getString("selectedaca");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ////to check if record is inserted

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select count(s_code)count from tbl_StudentAssignmnet where section='"+getsection+"' and class='"+getstd+"' and division='"+getdiv+"'\n" +
                        "and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and textassignment='"+gethw+"' and subjectname='"+getsubject+"' and topic='"+gettopic+"' and chapter_name='"+getchapter+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getcountt = rs.getString("count");
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        if(getcountt.equals("0")){


            ////get rollno and studentcode

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select b.Roll_Number,b.applicant_type from tblhomeworkentry a,tbladmissionfeemaster b \n" +
                            "where b.batch_code='"+getsection+"' and b.class_name='"+getstd+"' and b.division='"+getdiv+"' and ISNULL(IsCancelled,0)=0 \n" +
                            "and b.acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') \n" +
                            "and a.batch_code=b.batch_code and a.class_id=b.class_name and a.division=b.division\n" +
                            "and a.subject='"+getsubject+"' and a.homeworkdesciption='"+gethw+"' and a.topic='"+gettopic+"' and a.chapter_name='"+getchapter+"' order by b.roll_number";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        String roll = rs.getString("Roll_Number");
                        rollno.add(roll);

                        String code = rs.getString("applicant_type");
                        stucode.add(code);
                    }

                    ConnectionResult = "Successful";
                    isSuccess = true;
                    conn.close();
                }
            } catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }

            for (int i=0;i<rollno.size();i++){

                String code=stucode.get(i);
                String roll=rollno.get(i);

                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String commands = "insert into tbl_StudentAssignmnet values('"+getsection+"','"+getdiv+"','"+getsubject+"','1',NULL,NULL,getdate(),'"+getstaffid+"',NULL,NULL,NULL,NULL,'"+getstd+"',NULL,\n" +
                                "'"+gethw+"','"+gettopic+"','"+getacadmic+"',NULL,'"+getchapter+"',NULL,NULL,NULL,'"+roll+"',NULL,'Pending',NULL,'"+code+"',NULL)";
                        PreparedStatement preStmt = conn.prepareStatement(commands);
                        preStmt.executeUpdate();
                    } }
                catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                }
            }

            SyncData orderData = new SyncData();
            orderData.execute("");

        }else{
            SyncData orderData = new SyncData();
            orderData.execute("");
        }
    }

    private void loaddata2() {

        /////////  Bydefault StartDate

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select CONVERT(varchar(10),getdate(),103) showdate";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getdate = rs.getString("showdate");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /////////  get acadmic_year

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = " select selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"' ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getacadmic = rs.getString("selectedaca");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ////to check if record is inserted

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select count(s_code)count from tbl_StudentAssignmnet where section='"+getsection+"' and class='"+getstd+"' and division='"+getdiv+"'\n" +
                        "and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and subjectname='"+getsubject+"' and textassignment='"+gethw+"' and filepath='"+pdflink+"' and topic='"+gettopic+"' and chapter_name='"+getchapter+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getcountt = rs.getString("count");
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        if(getcountt.equals("0")){

            ////get rollno and studentcode

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select b.Roll_Number,b.applicant_type from tblhomeworkentry a,tbladmissionfeemaster b \n" +
                            "where b.batch_code='"+getsection+"' and b.class_name='"+getstd+"' and b.division='"+getdiv+"' and ISNULL(IsCancelled,0)=0 \n" +
                            "and b.acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') \n" +
                            "and a.batch_code=b.batch_code and a.class_id=b.class_name and a.division=b.division\n" +
                            "and a.subject='"+getsubject+"' and a.homeworkdesciption='"+gethw+"' and a.filepath='"+pdflink+"' and a.topic='"+gettopic+"' and a.chapter_name='"+getchapter+"' order by b.roll_number";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        String roll = rs.getString("Roll_Number");
                        rollno.add(roll);

                        String code = rs.getString("applicant_type");
                        stucode.add(code);
                    }

                    ConnectionResult = "Successful";
                    isSuccess = true;
                    conn.close();
                }
            } catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }

            for (int i=0;i<rollno.size();i++){

                String code=stucode.get(i);
                String roll=rollno.get(i);

                String msg = "unknown";
                ///insert all students
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String commands = "insert into tbl_StudentAssignmnet values('"+getsection+"','"+getdiv+"','"+getsubject+"','2','"+pdflink+"',NULL,getdate(),'"+getstaffid+"',NULL,NULL,NULL,NULL,'"+getstd+"',NULL,\n" +
                                "'"+gethw+"','"+gettopic+"','"+getacadmic+"',NULL,'"+getchapter+"',NULL,NULL,NULL,'"+roll+"',NULL,'Pending',NULL,'"+code+"',NULL)";
                        PreparedStatement preStmt = conn.prepareStatement(commands);
                        preStmt.executeUpdate();
                    } }
                catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                }
            }

            SyncData2 orderData = new SyncData2();
            orderData.execute("");

        }else{
            SyncData2 orderData = new SyncData2();
            orderData.execute("");
        }
    }

    private void loaddata3() {

        /////////  Bydefault StartDate

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select CONVERT(varchar(10),getdate(),103) showdate";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getdate = rs.getString("showdate");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        /////////  get acadmic_year

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = " select selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"' ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getacadmic = rs.getString("selectedaca");
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ////to check if record is inserted

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select count(s_code)count from tbl_StudentAssignmnet where section='"+getsection+"' and class='"+getstd+"' and division='"+getdiv+"'\n" +
                        "and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and subjectname='"+getsubject+"' and textassignment='"+gethw+"' and link='"+videolink+"' and topic='"+gettopic+"' and chapter_name='"+getchapter+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getcountt = rs.getString("count");
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        if(getcountt.equals("0")){

            ////get rollno and studentcode

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select b.Roll_Number,b.applicant_type from tblhomeworkentry a,tbladmissionfeemaster b \n" +
                            "where b.batch_code='"+getsection+"' and b.class_name='"+getstd+"' and b.division='"+getdiv+"' and ISNULL(IsCancelled,0)=0 \n" +
                            "and b.acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') \n" +
                            "and a.batch_code=b.batch_code and a.class_id=b.class_name and a.division=b.division\n" +
                            "and a.subject='"+getsubject+"' and a.homeworkdesciption='"+gethw+"' and a.link='"+videolink+"' and a.topic='"+gettopic+"' and a.chapter_name='"+getchapter+"' order by b.roll_number";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        String roll = rs.getString("Roll_Number");
                        rollno.add(roll);

                        String code = rs.getString("applicant_type");
                        stucode.add(code);
                    }

                    ConnectionResult = "Successful";
                    isSuccess = true;
                    conn.close();
                }
            } catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }

            for (int i=0;i<rollno.size();i++){

                String code=stucode.get(i);
                String roll=rollno.get(i);

                String msg = "unknown";
                ///insert all students
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String commands = "insert into tbl_StudentAssignmnet values('"+getsection+"','"+getdiv+"','"+getsubject+"','3',NULL,NULL,getdate(),'"+getstaffid+"',NULL,NULL,NULL,NULL,'"+getstd+"',NULL,\n" +
                                "'"+gethw+"','"+gettopic+"','"+getacadmic+"',NULL,'"+getchapter+"',NULL,'"+videolink+"',NULL,'"+roll+"',NULL,'Pending',NULL,'"+code+"',NULL)";
                        PreparedStatement preStmt = conn.prepareStatement(commands);
                        preStmt.executeUpdate();
                    } }
                catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                }
            }

            SyncData3 orderData = new SyncData3();
            orderData.execute("");

        }else{
            SyncData3 orderData = new SyncData3();
            orderData.execute("");
        }
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(AssignmentStatusEntry.this, "Loading Details",
                    "Please Wait...", true);

        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                Connection conn = connectionClass.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {
                    String query = "select a.id,ROW_NUMBER()OVER (ORDER BY b.Roll_Number) AS Row,b.Roll_Number,b.Surname+' '+b.Name Name,a.assignmentstatus,\n" +
                            "ISNULL(a.submitted_date,0)submitted_date from tbl_StudentAssignmnet a,tbladmissionfeemaster b \n" +
                            "where b.batch_code='"+getsection+"' and b.class_name='"+getstd+"' and b.division='"+getdiv+"' and ISNULL(IsCancelled,0)=0 \n" +
                            "and b.acadmic_year='"+getacadmic+"' and a.section=b.batch_code and a.class=b.class_name and a.division=b.division\n" +
                            " and a.subjectname='"+getsubject+"' and a.topic='"+gettopic+"' and a.chapter_name='"+getchapter+"'\n" +
                            "and a.s_code=b.applicant_type and b.applicant_type!='new'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems9(rs.getString("id"),rs.getString("Row"),rs.getString("Roll_Number"),rs.getString("Name"),rs.getString("assignmentstatus"),rs.getString("submitted_date")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {
            progress.dismiss();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, AssignmentStatusEntry.this);
                    if(myAppAdapter.getCount()!=0) {
                        listView.setAdapter(myAppAdapter);
                        myAppAdapter.notifyDataSetChanged();
                    }
                } catch (Exception ex)
                {

                }

            }
        }
    }

    private class SyncData2 extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(AssignmentStatusEntry.this, "Loading Details",
                    "Please Wait...", true);

        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                Connection conn = connectionClass.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {
                    String query = "select a.id,ROW_NUMBER()OVER (ORDER BY b.Roll_Number) AS Row,b.Roll_Number,b.Surname+' '+b.Name Name,a.assignmentstatus,\n" +
                            "ISNULL(a.submitted_date,0)submitted_date from tbl_StudentAssignmnet a,tbladmissionfeemaster b \n" +
                            "where b.batch_code='"+getsection+"' and b.class_name='"+getstd+"' and b.division='"+getdiv+"' and ISNULL(IsCancelled,0)=0 \n" +
                            "and b.acadmic_year='"+getacadmic+"' and a.section=b.batch_code and a.class=b.class_name and a.division=b.division\n" +
                            " and a.subjectname='"+getsubject+"' and a.textassignment='"+gethw+"' and a.filepath='"+pdflink+"' and a.topic='"+gettopic+"' and a.chapter_name='"+getchapter+"'\n" +
                            "and a.s_code=b.applicant_type and b.applicant_type!='new'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems9(rs.getString("id"),rs.getString("Row"),rs.getString("Roll_Number"),rs.getString("Name"),rs.getString("assignmentstatus"),rs.getString("submitted_date")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {
            progress.dismiss();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter2 = new MyAppAdapter2(itemArrayList, AssignmentStatusEntry.this);
                    if(myAppAdapter2.getCount()!=0) {
                        listView.setAdapter(myAppAdapter2);
                        myAppAdapter2.notifyDataSetChanged();
                    }
                } catch (Exception ex)
                {

                }

            }
        }
    }

    private class SyncData3 extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(AssignmentStatusEntry.this, "Loading Details",
                    "Please Wait...", true);

        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                Connection conn = connectionClass.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {
                    String query = "select a.id,ROW_NUMBER()OVER (ORDER BY b.Roll_Number) AS Row,b.Roll_Number,b.Surname+' '+b.Name Name,a.assignmentstatus,\n" +
                            "ISNULL(a.submitted_date,0)submitted_date from tbl_StudentAssignmnet a,tbladmissionfeemaster b \n" +
                            "where b.batch_code='"+getsection+"' and b.class_name='"+getstd+"' and b.division='"+getdiv+"' and ISNULL(IsCancelled,0)=0 \n" +
                            "and b.acadmic_year='"+getacadmic+"' and a.section=b.batch_code and a.class=b.class_name and a.division=b.division\n" +
                            " and a.subjectname='"+getsubject+"' and a.textassignment='"+gethw+"' and a.link='"+videolink+"' and a.topic='"+gettopic+"' and a.chapter_name='"+getchapter+"'\n" +
                            "and a.s_code=b.applicant_type and b.applicant_type!='new'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems9(rs.getString("id"),rs.getString("Row"),rs.getString("Roll_Number"),rs.getString("Name"),rs.getString("assignmentstatus"),rs.getString("submitted_date")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {
            progress.dismiss();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter3 = new MyAppAdapter3(itemArrayList, AssignmentStatusEntry.this);
                    if(myAppAdapter3.getCount()!=0) {
                        listView.setAdapter(myAppAdapter3);
                        myAppAdapter3.notifyDataSetChanged();
                    }
                } catch (Exception ex)
                {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter {
        public class ViewHolder
        {
            TextView id,rollno,name,status,edit,srno,date;
        }

        public List<ClassListItems9> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems9> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems9>();
            arraylist.addAll(parkingList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View rowView = convertView;
            MyAppAdapter.ViewHolder viewHolder= null;

            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.assignmentstatus, parent, false);
                viewHolder = new MyAppAdapter.ViewHolder();
                viewHolder.id = rowView.findViewById(R.id.id);
                viewHolder.srno = rowView.findViewById(R.id.srno);
                viewHolder.rollno = rowView.findViewById(R.id.roll);
                viewHolder.edit = rowView.findViewById(R.id.edit);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.name = rowView.findViewById(R.id.name);
                viewHolder.status = rowView.findViewById(R.id.status);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.id.setText(parkingList.get(position).getId()+"");
            viewHolder.srno.setText(parkingList.get(position).getSrno()+"");
            viewHolder.name.setText(parkingList.get(position).getName()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.rollno.setText(parkingList.get(position).getRoll()+"");
            viewHolder.status.setText(parkingList.get(position).getStatus()+"");

            if(parkingList.get(position).getStatus().equals("Pending")){
                viewHolder.status.setTextColor(Color.MAGENTA);
            }else if(parkingList.get(position).getStatus().equals("Done")){
                viewHolder.status.setTextColor(Color.GREEN);
            }else if(parkingList.get(position).getStatus().equals("Not Done")){
                viewHolder.status.setTextColor(Color.RED);
            }

            if(parkingList.get(position).getDate().equals("0")){
                viewHolder.date.setText("-");
            }

            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nameget=itemArrayList.get(position).getName();
                    idget=itemArrayList.get(position).getId();
                    ShowPopup();
                }
            });

            return rowView;
        }
    }

    public class MyAppAdapter2 extends BaseAdapter {
        public class ViewHolder
        {
            TextView id,rollno,name,status,edit,srno,date;
        }

        public List<ClassListItems9> parkingList;
        public Context context;


        private MyAppAdapter2(List<ClassListItems9> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems9>();
            arraylist.addAll(parkingList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View rowView = convertView;
            MyAppAdapter2.ViewHolder viewHolder= null;

            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.assignmentstatus, parent, false);
                viewHolder = new MyAppAdapter2.ViewHolder();
                viewHolder.id = rowView.findViewById(R.id.id);
                viewHolder.srno = rowView.findViewById(R.id.srno);
                viewHolder.rollno = rowView.findViewById(R.id.roll);
                viewHolder.edit = rowView.findViewById(R.id.edit);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.name = rowView.findViewById(R.id.name);
                viewHolder.status = rowView.findViewById(R.id.status);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (MyAppAdapter2.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.id.setText(parkingList.get(position).getId()+"");
            viewHolder.srno.setText(parkingList.get(position).getSrno()+"");
            viewHolder.name.setText(parkingList.get(position).getName()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.rollno.setText(parkingList.get(position).getRoll()+"");
            viewHolder.status.setText(parkingList.get(position).getStatus()+"");

            if(parkingList.get(position).getStatus().equals("Pending")){
                viewHolder.status.setTextColor(Color.MAGENTA);
            }else if(parkingList.get(position).getStatus().equals("Done")){
                viewHolder.status.setTextColor(Color.GREEN);
            }else if(parkingList.get(position).getStatus().equals("Not Done")){
                viewHolder.status.setTextColor(Color.RED);
            }

            if(parkingList.get(position).getDate().equals("0")){
                viewHolder.date.setText("-");
            }

            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nameget=itemArrayList.get(position).getName();
                    idget=itemArrayList.get(position).getId();
                    ShowPopup2();
                }
            });

            return rowView;
        }
    }

    public class MyAppAdapter3 extends BaseAdapter {
        public class ViewHolder
        {
            TextView id,rollno,name,status,edit,srno,date;
        }

        public List<ClassListItems9> parkingList;
        public Context context;


        private MyAppAdapter3(List<ClassListItems9> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems9>();
            arraylist.addAll(parkingList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View rowView = convertView;
            MyAppAdapter3.ViewHolder viewHolder= null;

            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.assignmentstatus, parent, false);
                viewHolder = new MyAppAdapter3.ViewHolder();
                viewHolder.id = rowView.findViewById(R.id.id);
                viewHolder.srno = rowView.findViewById(R.id.srno);
                viewHolder.rollno = rowView.findViewById(R.id.roll);
                viewHolder.edit = rowView.findViewById(R.id.edit);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.name = rowView.findViewById(R.id.name);
                viewHolder.status = rowView.findViewById(R.id.status);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (MyAppAdapter3.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.id.setText(parkingList.get(position).getId()+"");
            viewHolder.srno.setText(parkingList.get(position).getSrno()+"");
            viewHolder.name.setText(parkingList.get(position).getName()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.rollno.setText(parkingList.get(position).getRoll()+"");
            viewHolder.status.setText(parkingList.get(position).getStatus()+"");

            if(parkingList.get(position).getStatus().equals("Pending")){
                viewHolder.status.setTextColor(Color.MAGENTA);
            }else if(parkingList.get(position).getStatus().equals("Done")){
                viewHolder.status.setTextColor(Color.GREEN);
            }else if(parkingList.get(position).getStatus().equals("Not Done")){
                viewHolder.status.setTextColor(Color.RED);
            }

            if(parkingList.get(position).getDate().equals("0")){
                viewHolder.date.setText("-");
            }

            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nameget=itemArrayList.get(position).getName();
                    idget=itemArrayList.get(position).getId();
                    ShowPopup3();
                }
            });

            return rowView;
        }
    }

    public void ShowPopup() {
        final TextView id,name,date,submit,cancel;
        final Spinner status ;

        myDialog.setContentView(R.layout.updatestatusentry);
        myDialog.setCancelable(false);

        id = myDialog.findViewById(R.id.id);
        status = myDialog.findViewById(R.id.status);
        date = myDialog.findViewById(R.id.date);
        name = myDialog.findViewById(R.id.name);
        submit =myDialog.findViewById(R.id.submit);
        cancel = myDialog.findViewById(R.id.cancel);

        String[] Std = {"Pending", "Done","Not Done"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner11, Std);
        status.setAdapter(adapter);

        name.setText(nameget);
        date.setText(getdate);
        id.setText(idget);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(AssignmentStatusEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(AssignmentStatusEntry.this);
                progress.setTitle("Submitting");
                progress.setMessage("Please Wait a Moment");
                progress.setCancelable(false);
                progress.show();

                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String getstatus=status.getSelectedItem().toString();
                        String getdate=date.getText().toString();
                        String getid=id.getText().toString();
                        String msg = "unknown";

                        try {
                            ConnectionHelper conStr = new ConnectionHelper();
                            conn = conStr.connectionclasss();

                            if (conn == null) {
                                ConnectionResult = "NO INTERNET";
                            } else {
                                String commands = "update tbl_StudentAssignmnet set assignmentstatus='"+getstatus+"',submitted_date='"+getdate+"' where id='"+getid+"'";
                                PreparedStatement preStmt = conn.prepareStatement(commands);
                                preStmt.executeUpdate();
                            } }
                        catch (SQLException e) {
                            isSuccess = false;
                            ConnectionResult = e.getMessage();
                        }
                        progress.cancel();
                        myDialog.cancel();

                        itemArrayList.clear();
                        myAppAdapter.notifyDataSetChanged();
                        SyncData orderData = new SyncData();
                        orderData.execute("");
                    }
                };
                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable,1000);

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowPopup2() {
        final TextView id,name,date,submit,cancel;
        final Spinner status ;

        myDialog.setContentView(R.layout.updatestatusentry);
        myDialog.setCancelable(false);

        id = myDialog.findViewById(R.id.id);
        status = myDialog.findViewById(R.id.status);
        date = myDialog.findViewById(R.id.date);
        name = myDialog.findViewById(R.id.name);
        submit =myDialog.findViewById(R.id.submit);
        cancel = myDialog.findViewById(R.id.cancel);

        String[] Std = {"Pending", "Done","Not Done"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner11, Std);
        status.setAdapter(adapter);

        name.setText(nameget);
        date.setText(getdate);
        id.setText(idget);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(AssignmentStatusEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(AssignmentStatusEntry.this);
                progress.setTitle("Submitting");
                progress.setMessage("Please Wait a Moment");
                progress.setCancelable(false);
                progress.show();

                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String getstatus=status.getSelectedItem().toString();
                        String getdate=date.getText().toString();
                        String getid=id.getText().toString();
                        String msg = "unknown";

                        try {
                            ConnectionHelper conStr = new ConnectionHelper();
                            conn = conStr.connectionclasss();

                            if (conn == null) {
                                ConnectionResult = "NO INTERNET";
                            } else {
                                String commands = "update tbl_StudentAssignmnet set assignmentstatus='"+getstatus+"',submitted_date='"+getdate+"' where id='"+getid+"'";
                                PreparedStatement preStmt = conn.prepareStatement(commands);
                                preStmt.executeUpdate();
                            } }
                        catch (SQLException e) {
                            isSuccess = false;
                            ConnectionResult = e.getMessage();
                        }
                        progress.cancel();
                        myDialog.cancel();

                        itemArrayList.clear();
                        myAppAdapter2.notifyDataSetChanged();
                        SyncData2 orderData = new SyncData2();
                        orderData.execute("");
                    }
                };
                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable,1000);

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    public void ShowPopup3() {
        final TextView id,name,date,submit,cancel;
        final Spinner status ;

        myDialog.setContentView(R.layout.updatestatusentry);
        myDialog.setCancelable(false);

        id = myDialog.findViewById(R.id.id);
        status = myDialog.findViewById(R.id.status);
        date = myDialog.findViewById(R.id.date);
        name = myDialog.findViewById(R.id.name);
        submit =myDialog.findViewById(R.id.submit);
        cancel = myDialog.findViewById(R.id.cancel);

        String[] Std = {"Pending", "Done","Not Done"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner11, Std);
        status.setAdapter(adapter);

        name.setText(nameget);
        date.setText(getdate);
        id.setText(idget);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(AssignmentStatusEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.cancel();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progress = new ProgressDialog(AssignmentStatusEntry.this);
                progress.setTitle("Submitting");
                progress.setMessage("Please Wait a Moment");
                progress.setCancelable(false);
                progress.show();

                Runnable progressRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String getstatus=status.getSelectedItem().toString();
                        String getdate=date.getText().toString();
                        String getid=id.getText().toString();
                        String msg = "unknown";

                        try {
                            ConnectionHelper conStr = new ConnectionHelper();
                            conn = conStr.connectionclasss();

                            if (conn == null) {
                                ConnectionResult = "NO INTERNET";
                            } else {
                                String commands = "update tbl_StudentAssignmnet set assignmentstatus='"+getstatus+"',submitted_date='"+getdate+"' where id='"+getid+"'";
                                PreparedStatement preStmt = conn.prepareStatement(commands);
                                preStmt.executeUpdate();
                            } }
                        catch (SQLException e) {
                            isSuccess = false;
                            ConnectionResult = e.getMessage();
                        }
                        progress.cancel();
                        myDialog.cancel();

                        itemArrayList.clear();
                        myAppAdapter3.notifyDataSetChanged();
                        SyncData3 orderData = new SyncData3();
                        orderData.execute("");
                    }
                };
                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable,1000);

            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

}

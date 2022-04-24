package com.school.stanthony;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PrincipalLeaveStatus extends AppCompatActivity {

    String gettext1,gettext2,gettext3,gettext4,gettext5,gettext6,gettext7,getdays,getstaffid;
    TextView text1,text2,text3,text4,text5,text6,text7,text9,text10,approveddate;
    TextView grant,revoke;
    EditText edit;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess,success;
    Connection conn;
    String ConnectionResult,getid,getapproveddate,getapproveddays,Form1,getdate1,getdate2;
    String getballeave,getbalgranted;
    String getbalel,getbalcl,getbalml,getbalsl,getbaldl,getbalol,checksup;
    SharedPreferences sharedPreferences;
    Calendar c;
    DatePickerDialog dpd;
    int mMonth;
    AlertDialog alertDialog;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(PrincipalLeaveStatus.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    progress.dismiss();
                }
            }, 2000);
        }};

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(PrincipalLeaveStatus.this);
            progress.setTitle("Approving Leave");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    loaddata2();
                    if(checksup.equals("0")){
                        updatedata();
                    }
                    progress.dismiss();
                    grant.setVisibility(View.INVISIBLE);
                    revoke.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalLeaveStatus.this);
                    builder.setMessage("Leave Approved");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 1000);
        }};

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(PrincipalLeaveStatus.this);
            progress.setTitle("Rejecting Leave");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(checksup.equals("1")){
                        updatedata2();
                    }
                    ///update principal details
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String commands = "update tbl_hrappliedleave set messageprincipal='"+edit.getText().toString()+"',principalapprovoed='2' where id='"+getid+"' ";
                            PreparedStatement preStmt = conn.prepareStatement(commands);
                            preStmt.executeUpdate();
                        }
                    }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }

                    progress.dismiss();
                    grant.setVisibility(View.INVISIBLE);
                    revoke.setVisibility(View.INVISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalLeaveStatus.this);
                    builder.setMessage("Leave Rejected");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 1000);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_leave_status);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("prinref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Principalcode", null);

        getid=getIntent().getExtras().getString("id");
        getstaffid=getIntent().getExtras().getString("sid");

        text1=findViewById(R.id.text1);
        text2=findViewById(R.id.text2);
        text3=findViewById(R.id.text3);
        text4=findViewById(R.id.text4);
        text5=findViewById(R.id.text5);
        text6=findViewById(R.id.text6);
        text7=findViewById(R.id.text7);
        text9=findViewById(R.id.text9);
        text10=findViewById(R.id.text10);
        grant=findViewById(R.id.grant);
        edit=findViewById(R.id.edit);
        revoke=findViewById(R.id.revoke);
        approveddate=findViewById(R.id.approveddate);

        mainHandler.post(myRunnable);

        ///////////////////////////// first date picker

        text10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    final int mDay = c.get(Calendar.DAY_OF_MONTH);

                    dpd = new DatePickerDialog(PrincipalLeaveStatus.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                            text10.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            if (!text9.getText().toString().equals("")) {
                                calulateday();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dpd.show();

            }
        });

        ///////////////////////////// second date picker

        text9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    dpd = new DatePickerDialog(PrincipalLeaveStatus.this, new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            text9.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            calulateday();
                        }
                    }, mYear, mMonth, mDay);
                    dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    dpd.show();
            }
        });

        grant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getdays.startsWith("-")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalLeaveStatus.this);
                    builder.setMessage("Ohh it seems you are granting invalid leave days.");
                    builder.setCancelable(true);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();

                }else if(Integer.parseInt(getdays)>Integer.parseInt(getapproveddays)){

                        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalLeaveStatus.this);
                        builder.setMessage("Ohh it seems you have extended the applied leave days.");
                        builder.setCancelable(true);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();

                    }else if(edit.getText().toString().trim().equals("")){

                        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalLeaveStatus.this);
                        builder.setMessage("Please Enter Some Message.");
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
                         mainHandler1.post(myRunnable1);
                    }
            }
        });

        revoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit.getText().toString().trim().equals("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalLeaveStatus.this);
                    builder.setMessage("Please Enter Some Message.");
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
                    mainHandler2.post(myRunnable2);
                }
            }
        });

    }

    private void loaddata() {

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select convert(varchar(10),createdon,103)'date',leavetype,leavefrom+' - '+leavetill 'leavedate',message,\n" +
                        "approvedfrom+' - '+approvedtill 'approveddate',hodapprovoed,messagehod,adjustmentid,days, \n" +
                        "leavefrom,leavetill from tbl_hrappliedleave where id='"+getid+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    gettext1 = rs.getString("date");
                    gettext2 = rs.getString("leavetype");
                    gettext3 = rs.getString("leavedate");
                    gettext4 = rs.getString("message");
                    gettext5 = rs.getString("adjustmentid");
                    try {
                        getapproveddate = rs.getString("approveddate");
                    }catch (Exception e){
                        getapproveddate="Pending";
                    }

                    try {
                        getapproveddays = rs.getString("days");
                        getdays=getapproveddays;
                    }catch (Exception e){
                        getapproveddays="0";
                        getdays=getapproveddays;
                    }

                    try {
                        gettext6 = rs.getString("hodapprovoed");
                    }catch (Exception e){
                        gettext6="";
                    }

                    try {
                        gettext7 = rs.getString("messagehod");
                    }catch (Exception e){
                        gettext7="";
                    }

                    try {
                        getdate1 = rs.getString("leavefrom");
                    }catch (Exception e){
                        getdate1=" Select Date";
                    }

                    try {
                        getdate2 = rs.getString("leavetill");
                    }catch (Exception e){
                        getdate2=" Select Date";
                    }

                    if(gettext6.equals("1")){
                        gettext6="Pending";
                        checksup="0";
                    }else if(gettext6.equals("2")){
                        gettext6="Rejected";
                        checksup="0";
                    } else {
                        gettext6="Approved";
                        checksup="1";
                    }

                    if(getapproveddate==null){
                        getapproveddate="Pending";
                    }

                    if(gettext7==null){
                        gettext7=" -- ";
                    }

                    text1.setText(gettext1);
                    text2.setText(gettext2);
                    text3.setText(gettext3);
                    text4.setText(gettext4);
                    approveddate.setText(getapproveddate);
                    text6.setText(gettext6);
                    text7.setText(gettext7);
                    text10.setText(getdate1);
                    text9.setText(getdate2);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        ////For name of staff
        String staffid=gettext5;
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select name from tbl_hrstaffnew where staff_id='"+staffid+"'\n" +
                        "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String name = rs.getString("name");
                    text5.setText(name);
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void loaddata2() {

        ///update principal details
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String commands = "update tbl_hrappliedleave set approvedfrom='"+text10.getText().toString()+"',approvedtill='"+text9.getText().toString()+"',approveddays='"+getdays+"',\n" +
                        "messageprincipal='"+edit.getText().toString()+"',principalapprovoed='0' where id='"+getid+"' ";
                PreparedStatement preStmt = conn.prepareStatement(commands);
                preStmt.executeUpdate();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }


        ////For staff leave type
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select balel,balcl,balml,balsl,baldl,balol,baltotalleave, \n" +
                        "totalgranted,baltotalgranted from tbl_hrleave where staffid='"+getstaffid+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getbalel = rs.getString("balel");
                    getbalcl = rs.getString("balcl");
                    getbalml = rs.getString("balml");
                    getbalsl = rs.getString("balsl");
                    getbaldl = rs.getString("baldl");
                    getbalol = rs.getString("balol");
                    getballeave = rs.getString("baltotalleave");
                    getbalgranted = rs.getString("baltotalgranted");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void updatedata() {

        ///// update leave

        if(gettext2.equals("CL")){
            int updatebalcl=(Integer.parseInt(getbalcl))-(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))-(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balcl='"+updatebalcl+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("EL")){
            int updatebalel=(Integer.parseInt(getbalel))-(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))-(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balel='"+updatebalel+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("ML")){
            int updatebalml=(Integer.parseInt(getbalml))-(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))-(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balml='"+updatebalml+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("SL")){
            int updatebalsl=(Integer.parseInt(getbalsl))-(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))-(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balsl='"+updatebalsl+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("DL")){
            int updatebaldl=(Integer.parseInt(getbaldl))-(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))-(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set baldl='"+updatebaldl+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("OL")){
            int updatebalol=(Integer.parseInt(getbalol))-(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))-(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balol='"+updatebalol+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }

    }

    private void updatedata2() {

        ////For staff leave type
        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select balel,balcl,balml,balsl,baldl,balol,baltotalleave, \n" +
                        "totalgranted,baltotalgranted from tbl_hrleave where staffid='"+getstaffid+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getbalel = rs.getString("balel");
                    getbalcl = rs.getString("balcl");
                    getbalml = rs.getString("balml");
                    getbalsl = rs.getString("balsl");
                    getbaldl = rs.getString("baldl");
                    getbalol = rs.getString("balol");
                    getballeave = rs.getString("baltotalleave");
                    getbalgranted = rs.getString("baltotalgranted");
                }
                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ///// update leave

        if(gettext2.equals("CL")){
            int updatebalcl=(Integer.parseInt(getbalcl))+(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))+(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balcl='"+updatebalcl+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("EL")){
            int updatebalel=(Integer.parseInt(getbalel))+(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))+(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balel='"+updatebalel+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("ML")){
            int updatebalml=(Integer.parseInt(getbalml))+(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))+(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balml='"+updatebalml+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("SL")){
            int updatebalsl=(Integer.parseInt(getbalsl))+(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))+(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balsl='"+updatebalsl+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("DL")){
            int updatebaldl=(Integer.parseInt(getbaldl))+(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))+(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set baldl='"+updatebaldl+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }else  if(gettext2.equals("OL")){
            int updatebalol=(Integer.parseInt(getbalol))+(Integer.parseInt(getdays));
            int updatetotal=(Integer.parseInt(getballeave))+(Integer.parseInt(getdays));

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tbl_hrleave set balol='"+updatebalol+"',baltotalleave='"+updatetotal+"' where staffid='"+getstaffid+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                }
            }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void calulateday() {
        String ffirstdate=text10.getText().toString();
        String sseconddate=text9.getText().toString();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d/M/u");

        LocalDate startDateValue = LocalDate.parse(ffirstdate, dateFormatter);
        LocalDate endDateValue = LocalDate.parse(sseconddate, dateFormatter);
        long days = ChronoUnit.DAYS.between(startDateValue, endDateValue) + 1;
        getdays=(String.valueOf(days));

    }
}
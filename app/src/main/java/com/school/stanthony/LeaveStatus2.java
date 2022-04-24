package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import androidx.appcompat.app.AppCompatActivity;

public class LeaveStatus2 extends AppCompatActivity {

    TextView date,type,applieddate,reason,approveddate,approveddays;
    TextView hodstatus,hodmsg,prinstatus,prinmsg,adjstatus;
    String getdate,gettype,getapplieddate,getreason,getapproveddate,getapproveddays;
    String gethodstatus,gethodmsg,getprinstatus,getprinmsg,getadjstatus,getid;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess,success;
    Connection conn;
    String ConnectionResult;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(LeaveStatus2.this);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_status2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getid=getIntent().getExtras().getString("id");

        date=findViewById(R.id.date);
        type=findViewById(R.id.type);
        applieddate=findViewById(R.id.applieddate);
        reason=findViewById(R.id.reason);
        approveddate=findViewById(R.id.approveddate);
        approveddays=findViewById(R.id.approveddays);
        hodstatus=findViewById(R.id.hodstatus);
        hodmsg=findViewById(R.id.hodmsg);
        prinstatus=findViewById(R.id.pdstatus);
        prinmsg=findViewById(R.id.pdmsg);
        adjstatus=findViewById(R.id.adjstatus);

        mainHandler.post(myRunnable);

    }


    private void loaddata() {

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select convert(varchar(10),createdon,103)'date',leavetype,leavefrom+' - '+leavetill 'leavedate',message,\n" +
                        "approvedfrom+' - '+approvedtill 'approveddate',approveddays,hodapprovoed,messagehod,principalapprovoed,messageprincipal,\n" +
                        "adjustmentstatus from tbl_hrappliedleave where id='"+getid+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getdate = rs.getString("date");
                    gettype = rs.getString("leavetype");
                    getapplieddate = rs.getString("leavedate");
                    getreason = rs.getString("message");
                    try {
                        getapproveddate = rs.getString("approveddate");
                    }catch (Exception e){
                        getapproveddate="Pending";
                    }

                    try {
                        getapproveddays = rs.getString("approveddays");
                    }catch (Exception e){
                        getapproveddays="0";
                    }

                    try {
                        gethodstatus = rs.getString("hodapprovoed");
                    }catch (Exception e){
                        gethodstatus="";
                    }

                    try {
                        gethodmsg = rs.getString("messagehod");
                    }catch (Exception e){
                        gethodmsg="";
                    }

                    try {
                        getprinstatus = rs.getString("principalapprovoed");
                    }catch (Exception e){
                        getprinstatus="";
                    }

                    try {
                        getprinmsg = rs.getString("messageprincipal");
                    }catch (Exception e){
                        getprinmsg="";
                    }

                    getadjstatus = rs.getString("adjustmentstatus");

                    if(getapproveddays.equals("0")){
                        getapproveddays="Pending";
                    }else if(getapproveddays.equals("1")){
                        getapproveddays=getapproveddays+" Day";
                    }else {
                        getapproveddays=getapproveddays+" Days";
                    }

                    if(gethodstatus.equals("1")){
                        gethodstatus="Pending";
                    }else if(gethodstatus.equals("2")){
                        gethodstatus="Rejected";
                    } else {
                        gethodstatus="Approved";
                    }

                    if(getprinstatus.equals("1")){
                        getprinstatus="Pending";
                    }else if(getprinstatus.equals("2")){
                        getprinstatus="Rejected";
                    } else {
                        getprinstatus="Approved";
                    }

                    if(getadjstatus.equals("1")){
                        getadjstatus="Pending";
                    }else if(getadjstatus.equals("2")){
                        getadjstatus="Rejected";
                    }else {
                        getadjstatus="Approved";
                    }

                    if(getapproveddate==null){
                        getapproveddate="Pending";
                    }

                    if(gethodmsg==null){
                        gethodmsg=" -- ";
                    }

                    if(getprinmsg==null){
                        getprinmsg=" -- ";
                    }

                    date.setText(getdate);
                    type.setText(gettype);
                    applieddate.setText(getapplieddate);
                    reason.setText(getreason);
                    approveddate.setText(getapproveddate);
                    approveddays.setText(getapproveddays);
                    hodstatus.setText(gethodstatus);
                    hodmsg.setText(gethodmsg);
                    prinstatus.setText(getprinstatus);
                    prinmsg.setText(getprinmsg);
                    adjstatus.setText(getadjstatus);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

    }

}

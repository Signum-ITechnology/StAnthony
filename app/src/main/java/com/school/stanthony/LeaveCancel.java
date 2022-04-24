package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LeaveCancel extends AppCompatActivity {

    String getfirstdate,getseconddate,getdays,gettype,getreason,getid,getstaffid;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess,success;
    Connection conn;
    String ConnectionResult,getnewreason;
    EditText reason;
    Button btn;
    AlertDialog alertDialog;
    ProgressDialog progress;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(LeaveCancel.this);
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
            }, 1000);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_cancel);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        reason=findViewById(R.id.reason);
        btn=findViewById(R.id.btn);

        try{
            getid=getIntent().getExtras().getString("id");
        }catch (Exception e){}

        reason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reason.setFocusableInTouchMode(true);
            }
        });

        mainHandler.post(myRunnable);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reason.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LeaveCancel.this);
                    builder.setMessage("Please Enter Your Cancel Leave Reason");
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
                    getreason=reason.getText().toString();
                    getnewreason=getreason.replace("'","''");
                    insertdata();
                }
            }
        });

    }


    private void loaddata() {

        ////get all details

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select staffid,leavefrom,leavetill,leavetype,days from tbl_hrappliedleave where id='"+getid+"' ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getfirstdate = rs.getString("leavefrom");
                    getseconddate = rs.getString("leavetill");
                    getdays = rs.getString("days");
                    gettype = rs.getString("leavetype");
                    getstaffid = rs.getString("staffid");
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

    }

    private void insertdata() {

        progress = new ProgressDialog(LeaveCancel.this);
        progress.setTitle("Applying For Leave Cancellation");
        progress.setMessage("Please Wait a Moment");
        progress.setCancelable(false);
        progress.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /////////

                String msg = "unknown";
                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        msg = "Check Your Internet Access";
                    } else {
                        String commands = "insert into tbl_hrcancelrequest (requestfrom,requesttill,days,leavetype,isactive,reason,createdon,createdby)\n" +
                                "values('"+getfirstdate+"','"+getseconddate+"','"+getdays+"','"+gettype+"','1','"+getnewreason+"',getdate(),'"+getstaffid+"')";
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
                AlertDialog.Builder builder = new AlertDialog.Builder(LeaveCancel.this);
                builder.setMessage("Request For Cancellation Added");
                builder.setCancelable(false);
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }, 2000);

    }
}

package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

public class AdminTeacherNoticeEntry extends AppCompatActivity {

    EditText start,subject,notice;
    Button btn;
    AutoCompleteTextView staffname;
    String startdate,getsubject,getnotice,code,getstaffid;
    String ConnectionResult;
    Boolean isSuccess;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs;
    String Form1,getdes,getnot,getsub;
    AlertDialog alertDialog;
    Spinner dept;
    SharedPreferences sharedPref;
    TextView showstartdate,showday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_teacher_notice_entry);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        start=findViewById(R.id.start);
        subject=findViewById(R.id.subject);
        notice=findViewById(R.id.notice);
        staffname=findViewById(R.id.code);
        btn=findViewById(R.id.btn);
        dept=findViewById(R.id.dept);
        showstartdate=findViewById(R.id.showstartdate);
        showday=findViewById(R.id.showday);

        staffname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staffname.setFocusableInTouchMode(true);
            }
        });

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select distinct('YYY') postnew from tbl_hrstaffnew where \n" +
                        "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "union all\n" +
                        "select distinct postnew from tbl_hrstaffnew where\n" +
                        "acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    String fullname = rs.getString("postnew");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdminTeacherNoticeEntry.this,R.layout.spinner11, data2);
                dept.setAdapter(NoCoreAdapter2);
                ConnectionResult = "Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        dept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //////////For Staffname
                getdes=dept.getSelectedItem().toString();
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();
                    if (conn == null)
                    {
                        ConnectionResult="NO INTERNET";
                    }
                    else {

                        String query = "select name from tbl_hrstaffnew where\n" +
                                "acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') and postnew='"+getdes+"'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data = new ArrayList<>();
                        while (rs.next()) {
                            String cid = rs.getString("name");
                            data.add(cid);
                        }
                        String[] array = data.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(AdminTeacherNoticeEntry.this,android.R.layout.simple_list_item_1, data);
                        staffname.setAdapter(NoCoreAdapter);
                        ConnectionResult = " Successful";
                        isSuccess = true;
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

        staffname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                code=(String)adapterView.getItemAtPosition(i);
            }
        });

        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.setFocusableInTouchMode(true);
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setFocusableInTouchMode(true);
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

                dpd = new DatePickerDialog(AdminTeacherNoticeEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                showstartdate.setText( (monthOfYear + 1)+ "-" + dayOfMonth + "-" + year);
                                start.setText( dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                                getmoonth = monthOfYear + 1;

                                String input_date=start.getText().toString();
                                SimpleDateFormat format1=new SimpleDateFormat("dd/MM/yyyy");
                                Date dt1= null;
                                try {
                                    dt1 = format1.parse(input_date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                DateFormat format2=new SimpleDateFormat("EEEE");
                                String finalDay=format2.format(dt1);
                                showday.setText(finalDay);
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

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select CONVERT(varchar(10),getdate(),110) sysdate ,CONVERT(varchar(10),getdate(),103) showdate,datename(dw,getdate())day";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data = new ArrayList<>();
                ArrayList<String> data2 = new ArrayList<>();

                while (rs.next())
                {
                    String fullname = rs.getString("showdate");
                    data.add(fullname);
                    start.setText(data.get(0));

                    String name = rs.getString("sysdate");
                    data2.add(name);
                    showstartdate.setText(data2.get(0));

                    String day=rs.getString("day");
                    showday.setText(day);
                }
                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code=staffname.getText().toString();
                getdes=dept.getSelectedItem().toString();
                //  Toast.makeText(AdminTeacherNoticeEntry.this, ""+getdes, Toast.LENGTH_SHORT).show();
                if(subject.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminTeacherNoticeEntry.this);
                    builder.setMessage("Please Add Subject");
                    builder.setCancelable(true);
                    alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(notice.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminTeacherNoticeEntry.this);
                    builder.setMessage("Please Add Some Notice");
                    builder.setCancelable(true);
                    alertDialog = builder.create();
                    alertDialog.show();
                }
                else if(code.equals("YYY")){
                    startdate = showstartdate.getText().toString();
                    getsubject=subject.getText().toString();
                    getnotice=notice.getText().toString();
                    getnot=getnotice.replace("'","''");
                    getsub=getsubject.replace("'","''");

                    final ProgressDialog progress = new ProgressDialog(AdminTeacherNoticeEntry.this);
                    progress.setTitle("Adding Notice");
                    progress.setMessage("Please Wait a Moment");
                    progress.setCancelable(false);
                    progress.show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String msg = "unknown";
                            try {
                                ConnectionHelper conStr = new ConnectionHelper();
                                conn = conStr.connectionclasss();

                                if (conn == null) {
                                    msg = "Check Your Internet Access";
                                } else {
                                    String commands = "insert into tblTeacherNotification values('"+getsub+"','"+getnot+"','1','"+startdate+"','1','','','"+getdes+"','YYY')";
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminTeacherNoticeEntry.this);
                            builder.setMessage("Notice Added");
                            builder.setCancelable(false);
                            builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(getIntent());
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }, 800);
                }
                else if(!code.equals("YYY")){
                    startdate = showstartdate.getText().toString();
                    getsubject=subject.getText().toString();
                    getnotice=notice.getText().toString();
                    getnot=getnotice.replace("'","''");
                    getsub=getsubject.replace("'","''");
                    ////For StaffId

                    try {
                        ConnectionHelper conStr1 = new ConnectionHelper();
                        conn = conStr1.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select staff_id from tbl_hrstaffnew where name='"+code+"'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();

                            while (rs.next()) {
                                getstaffid = rs.getString("staff_id");
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

                    final ProgressDialog progress = new ProgressDialog(AdminTeacherNoticeEntry.this);
                    progress.setTitle("Adding Notice");
                    progress.setMessage("Please Wait a Moment");
                    progress.setCancelable(false);
                    progress.show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            String msg = "unknown";
                            try {
                                ConnectionHelper conStr = new ConnectionHelper();
                                conn = conStr.connectionclasss();

                                if (conn == null) {
                                    msg = "Check Your Internet Access";
                                } else {
                                    String commands = "insert into tblTeacherNotification values('"+getsub+"','"+getnot+"','1','"+startdate+"','1','','','"+getdes+"','"+getstaffid+"')";
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminTeacherNoticeEntry.this);
                            builder.setMessage("Notice Added");
                            builder.setCancelable(false);
                            builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    startActivity(getIntent());
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
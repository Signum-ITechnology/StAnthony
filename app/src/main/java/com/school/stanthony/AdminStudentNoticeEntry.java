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

public class AdminStudentNoticeEntry extends AppCompatActivity {

    String ConnectionResult,code,msg,section, std, div,code1,section1,startdate,getnotice,getacademic,getsubject,getsub,nstd;
    Boolean isSuccess;
    EditText start,notice,subject;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs,rt;
    Spinner s1, s2, s3;
    AutoCompleteTextView studcode;
    Button btn;
    SharedPreferences sharedPref;
    String Form1,getnot;
    AlertDialog alertDialog;
    TextView showstartdate,showname,showday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_student_notice_entry);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //  requestQueue= Volley.newRequestQueue(getApplicationContext());

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        studcode = findViewById(R.id.code);
        btn = findViewById(R.id.btn);
        start=findViewById(R.id.start);
        notice=findViewById(R.id.notice);
        showstartdate=findViewById(R.id.showstartdate);
        showname=findViewById(R.id.name);
        subject=findViewById(R.id.subject);
        showday=findViewById(R.id.showday);

        studcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studcode.setFocusableInTouchMode(true);
            }
        });

        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setFocusableInTouchMode(true);
            }
        });

        subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.setFocusableInTouchMode(true);
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

                dpd = new DatePickerDialog(AdminStudentNoticeEntry.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
//                                start.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);

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

//        /////////  Bydefault StartDate
//
//        try {
//            ConnectionHelper conStr = new ConnectionHelper();
//            conn = conStr.connectionclasss();
//
//            if (conn == null)
//            {
//                ConnectionResult="NO INTERNET";
//            }
//            else
//            {
//                String query = "select CONVERT(varchar(10),getdate(),110) sysdate ";
//                stmt = conn.prepareStatement(query);
//                rs = stmt.executeQuery();
//          //      ArrayList<String> data2 = new ArrayList<>();
//                while (rs.next())
//                {
//                    startdate = rs.getString("sysdate");
////                    data2.add(fullname);
////                    startdate=data2.get(0);
//                    start.setText(startdate);
//                }
//                ConnectionResult = " Successful";
//                isSuccess=true;
//                conn.close();
//            }
//        }
//        catch (android.database.SQLException e) {
//            isSuccess = false;
//            ConnectionResult = e.getMessage();
//        } catch (java.sql.SQLException e) {
//            e.printStackTrace();
//        }

        ////For Academic

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select selectedaca from tblusermaster where username='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                //        ArrayList<String> data2 = new ArrayList<>();
                while (rs.next()) {
                    getacademic= rs.getString("selectedaca");
//                    data2.add(fullname);
//                    getacademic=data2.get(0);
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
                String query = "select distinct('YYY') batchcode from tblclassmaster where \n" +
                        "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "union all             \n" +
                        "select batchCode from tblbatchcodemaster where \n" +
                        "acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                while (rs.next())
                {
                    String fullname = rs.getString("batchcode");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdminStudentNoticeEntry.this,R.layout.spinner11, data2);
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

                section = s1.getSelectedItem().toString();
                if (section.equals("YYY")) {
                    s2.setVisibility(View.INVISIBLE);
                    s3.setVisibility(View.INVISIBLE);
                    studcode.setVisibility(View.INVISIBLE);
                    //    stdtext.setVisibility(View.INVISIBLE);
                    //     divtext.setVisibility(View.INVISIBLE);
                } else {
                    s2.setVisibility(View.VISIBLE);
                    // stdtext.setVisibility(View.VISIBLE);
                    /////////////////// For Class
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select batch_for from tblbatchmaster where \n" +
                                    "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and \n" +
                                    "batchcode='" + section + "'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<>();
                            while (rs.next()) {
                                String fullname = rs.getString("batch_for");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdminStudentNoticeEntry.this, R.layout.spinner11, data2);
                            s2.setAdapter(NoCoreAdapter2);
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
                studcode.setVisibility(View.VISIBLE);

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
                                "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and \n" +
                                "class_name='"+std+"'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("division");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdminStudentNoticeEntry.this,R.layout.spinner11, data2);
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
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    std=s2.getSelectedItem().toString();
                    div=s3.getSelectedItem().toString();
                }catch (Exception e){}
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();
                    if (conn == null)
                    {
                        ConnectionResult="NO INTERNET";
                    }
                    else {
                        String query = "select Applicant_type from tbladmissionfeemaster where Batch_Code ='"+section+"' and Class_Name='"+std+"'\n" +
                                "and Division='"+div+"' and Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data = new ArrayList<>();
                        while (rs.next()) {
                            String cid = rs.getString("Applicant_type");
                            data.add(cid);
                        }
                        String[] array = data.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(AdminStudentNoticeEntry.this,android.R.layout.simple_list_item_1, data);
                        studcode.setAdapter(NoCoreAdapter);
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

                studcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        code = (String) adapterView.getItemAtPosition(i);
                        if (code.equals("")) {
                            showname.setText("");
                        } else {
                            try {
                                ConnectionHelper conStr = new ConnectionHelper();
                                conn = conStr.connectionclasss();
                                if (conn == null) {
                                    ConnectionResult = "NO INTERNET";
                                } else {
                                    String query = "select name+' '+father_name+' '+surname as 'name' from tbladmissionfeemaster \n" +
                                            "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and applicant_type='" + code + "'";
                                    stmt = conn.prepareStatement(query);
                                    rs = stmt.executeQuery();
                                    while (rs.next()) {
                                        String name = rs.getString("name");
                                        showname.setText(name);
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
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    std = s2.getSelectedItem().toString();
                    div = s3.getSelectedItem().toString();
                    startdate = showstartdate.getText().toString();
                    getnotice = notice.getText().toString();
                    getsubject = subject.getText().toString();
                }catch (Exception e){}

                if(subject.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminStudentNoticeEntry.this);
                    builder.setMessage("Please Add Some subject");
                    builder.setCancelable(true);
                    alertDialog = builder.create();
                    alertDialog.show();
                }else if(notice.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminStudentNoticeEntry.this);
                    builder.setMessage("Please Add Some Notice");
                    builder.setCancelable(true);
                    alertDialog = builder.create();
                    alertDialog.show();
                }
                else  if(section.equals("YYY")){
                    startdate = showstartdate.getText().toString();
                    getnotice = notice.getText().toString();
                    getnot=getnotice.replace("'","''");
                    getsubject = subject.getText().toString();
                    getsub=getsubject.replace("'","''");

                    final ProgressDialog progress = new ProgressDialog(AdminStudentNoticeEntry.this);
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
                                    String commands = "insert into tblstudentnotice values('All','All','All','All','"+getnot+"','"+startdate+"','1','"+getacademic+"','Admin','','"+getsub+"')";
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
                            //  sendnotificationyyy();
                            progress.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminStudentNoticeEntry.this);
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
                else  if(!section.equals("YYY")){
                    startdate = showstartdate.getText().toString();
                    getnotice = notice.getText().toString();
                    getnot=getnotice.replace("'","''");
                    getsubject = subject.getText().toString();
                    getsub=getsubject.replace("'","''");
                    section = s1.getSelectedItem().toString();
                    std = s2.getSelectedItem().toString();
                    div = s3.getSelectedItem().toString();
                    code=studcode.getText().toString();
                    if(std.equals("JR KG") || std.equals("SR KG")){
                        nstd=std.replace(" ","");
                    }

                    final ProgressDialog progress = new ProgressDialog(AdminStudentNoticeEntry.this);
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
                                    String commands = "insert into tblstudentnotice values('"+section+"','"+std+"','"+div+"','"+code+"','"+getnot+"','"+startdate+"','1','"+getacademic+"','Admin','','"+getsub+"')";
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminStudentNoticeEntry.this);
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
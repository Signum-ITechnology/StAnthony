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
import androidx.fragment.app.Fragment;

public class TeacherAddNotice extends Fragment {

    String ConnectionResult,code,section, std, div,startdate,getnotice,getacademic,staffid,getsubject,getsub,nstd;
    Boolean isSuccess;
    EditText start,notice,subject;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1, s2, s3;
    AutoCompleteTextView studcode;
    Button btn;
    String Form1,name,getnot;
    SharedPreferences sharedPreferences;
    AlertDialog alertDialog;
    TextView showstartdate,showname,showday;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_teacher_add_notice, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        s1 = view.findViewById(R.id.s1);
        s2 = view.findViewById(R.id.s2);
        s3 = view.findViewById(R.id.s3);
        studcode = view.findViewById(R.id.code);
        btn = view.findViewById(R.id.btn);
        start=view.findViewById(R.id.start);
        notice=view.findViewById(R.id.notice);
        showstartdate=view.findViewById(R.id.showstartdate);
        showname=view.findViewById(R.id.name);
        subject=view.findViewById(R.id.subject);
        showday=view.findViewById(R.id.showday);

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

                dpd = new DatePickerDialog(getContext(),
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

                    String fullnam = rs.getString("sysdate");
                    data2.add(fullnam);
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
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getContext(),R.layout.spinner11, data2);
                s1.setAdapter(NoCoreAdapter2);
                //   section=s1.getSelectedItem().toString();
                //   Toast.makeText(this, ""+section, Toast.LENGTH_SHORT).show();
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
                        String query = "select Applicant_type from tbladmissionfeemaster where Class_Name='"+std+"'and Division='"+div+"'\n" +
                                "and applicant_type!='new' and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data = new ArrayList<>();
                        while (rs.next()) {
                            String cid = rs.getString("Applicant_type");
                            data.add(cid);
                        }
                        String[] array = data.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, data);
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
                                            "where Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and applicant_type='" + code + "'";
                                    stmt = conn.prepareStatement(query);
                                    rs = stmt.executeQuery();
                                    //   ArrayList<String> data = new ArrayList<>();
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
                    section = s1.getSelectedItem().toString();
                    std = s2.getSelectedItem().toString();
                    div = s3.getSelectedItem().toString();
                    startdate = showstartdate.getText().toString();
                    getnotice = notice.getText().toString();
                    getsubject = subject.getText().toString();
                    //     getnot=getnotice.replace("'","''");
                    //    Toast.makeText(getContext(), ""+getnot, Toast.LENGTH_SHORT).show();
                }catch (Exception e){}

                if(subject.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Add Some subject");
                    builder.setCancelable(true);
                    alertDialog = builder.create();
                    alertDialog.show();
                }else if(notice.getText().toString().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Please Add Some Notice");
                    builder.setCancelable(true);
                    alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    startdate = showstartdate.getText().toString();
                    getsubject = subject.getText().toString();
                    getsub=getsubject.replace("'","''");
                    getnotice = notice.getText().toString();
                    getnot=getnotice.replace("'","''");
                    section = s1.getSelectedItem().toString();
                    std = s2.getSelectedItem().toString();
                    div = s3.getSelectedItem().toString();
                    code=studcode.getText().toString();
//                    if(code.equals("YYY")){
//                        code="ALL";
//                    }
                    if(std.equals("JR KG") || std.equals("SR KG")){
                        nstd=std.replace(" ","");
                    }

                    final ProgressDialog progress = new ProgressDialog(getContext());
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
                                ConnectionHelper conStr1 = new ConnectionHelper();
                                conn = conStr1.connectionclasss();

                                if (conn == null) {
                                    msg = "Check Your Internet Access";
                                } else {
                                    String commands = "insert into tblstudentnotice values('"+section+"','"+std+"','"+div+"','"+code+"','"+getnot+"','"+startdate+"','1','"+getacademic+"','"+name+"','','"+getsub+"')";
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
//                            if(code.equals("ALL")){
//                                   sendnotification();
//                            }else{
//                                sendnotification1();
//                            }
                            progress.cancel();
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Notice Added");
                            builder.setCancelable(false);
                            builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    subject.setText("");
                                    notice.setText("");
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }, 800);
                }
            }
        });

        return view;
    }
}
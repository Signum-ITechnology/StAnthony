package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherMarksEntry extends AppCompatActivity {

    String ConnectionResult,section,std,Form1,div;
    String code,sem,subjectcode,checkentry,getsem,getsubject;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess;
    Connection conn;
    RadioGroup radioGroup;
    RadioButton radiotext;
    Spinner s1, s2, s3,subject,semester;
    AutoCompleteTextView studcode;
    SharedPreferences sharedPreferences;
    Button btn;
    TextView showname;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(TeacherMarksEntry.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    loaddata2();
                    progress.dismiss();
                }
            }, 1000);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_marks_entry);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        btn = findViewById(R.id.btn);
        subject = findViewById(R.id.subject);
        semester = findViewById(R.id.semester);
        studcode = findViewById(R.id.code);
        showname=findViewById(R.id.name);
        radioGroup=findViewById(R.id.group1);

        studcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studcode.setFocusableInTouchMode(true);
            }
        });

        String[] blood = { "1st Unit","1st Term","2nd Unit","2nd Term"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TeacherMarksEntry.this,R.layout.spinner11, blood);
        semester.setAdapter(adapter);

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
        } catch (SQLException e) {
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
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                        s3.setAdapter(NoCoreAdapter2);
                        // NoCoreAdapter2.add("YYY");
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

        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section = s1.getSelectedItem().toString();
                std = s2.getSelectedItem().toString();
                div=s3.getSelectedItem().toString();

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
                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1, data);
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
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                            subject.setAdapter(NoCoreAdapter2);

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
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                            subject.setAdapter(NoCoreAdapter2);
                            // NoCoreAdapter2.add("YYY");
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    std = s2.getSelectedItem().toString();
                    div=s3.getSelectedItem().toString();
                    getsem = semester.getSelectedItem().toString();
                    getsubject = subject.getSelectedItem().toString();

                    if (getsem.equals("1st Unit")) {
                        sem = "3";
                    } else if (getsem.equals("1st Term")) {
                        sem = "1";
                    } else if (getsem.equals("2nd Unit")) {
                        sem = "4";
                    } else if (getsem.equals("2nd Term")) {
                        sem = "2";
                    }

                    mainHandler.post(myRunnable);

               }catch (Exception e){

                }
            }
        });

    }

    private String loaddata(){

        ////For subject code

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select distinct subjectcode from tblcoursemaster where class_name='"+std+"' and title='"+getsubject+"'\n" +
                        "and semester='"+sem+"' and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "and maxmarks!='naa'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    subjectcode = rs.getString("subjectcode");
                }
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


        ////For marks entry check

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select count(marks)count from tblstudentMarksDetails where class_id='"+std+"' and division='"+div+"' and subjectcode='"+subjectcode+"'\n" +
                        "and semester='"+sem+"' and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    checkentry = rs.getString("count");
                }
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

        return checkentry;
    }

    private void loaddata2(){

            int selectedId = radioGroup.getCheckedRadioButtonId();
            radiotext = findViewById(selectedId);
            String text = radiotext.getText().toString();

            if(text.equals("Addition")){

                if(checkentry.equals("0")) {

                    String stucode = studcode.getText().toString();

                    if (stucode.equals("YYY")) {
                        Intent i = new Intent(getApplicationContext(), TeacherAddMarks.class);
                        i.putExtra("sec", section);
                        i.putExtra("std", std);
                        i.putExtra("div", div);
                        i.putExtra("sem", sem);
                        i.putExtra("sub", getsubject);
                        startActivity(i);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherMarksEntry.this);
                        builder.setMessage("You Cannot Enter Marks Of Single Student It Must Be YYY.");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }

                } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherMarksEntry.this);
                        builder.setMessage("It seems you have already submitted marks of selected semester.");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }

            }else{
                if(!checkentry.equals("0")) {

                    String stucode = studcode.getText().toString();

                    if (!stucode.equals("YYY")) {
                        Intent i = new Intent(getApplicationContext(), TeacherUpdateMarks.class);
                        i.putExtra("sem", sem);
                        i.putExtra("std", std);
                        i.putExtra("sub", getsubject);
                        i.putExtra("code", stucode);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(getApplicationContext(), TeacherUpdateAllMarks.class);
                        i.putExtra("sec", section);
                        i.putExtra("std", std);
                        i.putExtra("div", div);
                        i.putExtra("sem", sem);
                        i.putExtra("sub", getsubject);
                        startActivity(i);
                    }
                }else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(TeacherMarksEntry.this);
                    builder.setMessage("It seems you have not entered marks kindly add first then you can update marks.");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }
    }
}
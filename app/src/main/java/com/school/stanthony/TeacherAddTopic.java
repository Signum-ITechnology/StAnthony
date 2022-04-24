package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TeacherAddTopic extends Fragment {

    Spinner s1, s2,s3,subject;
    EditText topic;
    Button btn;
    SharedPreferences sharedPreferences;
    Boolean isSuccess;
    PreparedStatement stmt;
    ResultSet rs;
    Connection conn;
    String ConnectionResult,Form1,section,getstd,getdiv,getacademic;
    String  name,staffid,getsubject,gettopic,gettop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_teacher_add_topic, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        s1 = view.findViewById(R.id.s1);
        s2 = view.findViewById(R.id.s2);
        s3 = view.findViewById(R.id.s3);
        btn = view.findViewById(R.id.btn);
        subject = view.findViewById(R.id.subject);
        topic = view.findViewById(R.id.topic);

        topic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topic.setFocusableInTouchMode(true);
            }
        });

        ////For Academic

        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select max(acadmic_year)acadmic_year from tbl_HRStaffnew where staffuser='"+Form1+"'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
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

        /////////////////// For Teacher Name
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select name,staff_id from tbl_HRStaffnew where staffuser='" + Form1 + "'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    name = rs.getString("name");
                    staffid = rs.getString("staff_id");
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
                        "acadmic_year=(select max(acadmic_year) from tbl_HRStaffnew where staffuser='"+Form1+"') ";
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
                                "Acadmic_Year=(select max(acadmic_year) from tbl_HRStaffnew where staffuser='"+Form1+"') and \n" +
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

                getstd = s2.getSelectedItem().toString();
                s3.setVisibility(View.VISIBLE);

                /////////////////// For Div
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select distinct(division) from tblclassmaster where \n" +
                                "Acadmic_Year=(select max(acadmic_year) from tbl_HRStaffnew where staffuser='"+Form1+"') and \n" +
                                "class_name='" + getstd + "'";
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
                getstd = s2.getSelectedItem().toString();

                if (section.equals("JR.COLLEGE")) {
                    /////////////////// For Div
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select distinct title,subjectcode from  tbljrcoursemaster where acadmic_year=(select max(acadmic_year)acadmic_year from tbl_HRStaffnew where staffuser='"+Form1+"')\n" +
                                    "and section='" + section + "' and batchcode='" + getstd + "' order by subjectcode";
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
                            String query = "select distinct title,subjectcode from  tblcoursemaster where acadmic_year=(select max(acadmic_year) from tbl_HRStaffnew where staffuser='"+Form1+"') \n" +
                                    "and batchcode='" + section + "' and class_name='" + getstd + "' order by subjectcode";
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    section=s1.getSelectedItem().toString();
                    getstd=s2.getSelectedItem().toString();
                    getdiv=s3.getSelectedItem().toString();
                    getsubject=subject.getSelectedItem().toString();
                    gettopic = topic.getText().toString();
                    gettop = gettopic.replace("'", "''");
                }catch (Exception e){}

                if (section==null) {
                    Toast.makeText(getContext(), "Please Select Section", Toast.LENGTH_LONG).show();
                }else  if (getstd==null) {
                    Toast.makeText(getContext(), "Please Select Std", Toast.LENGTH_LONG).show();
                }else  if (getdiv==null) {
                    Toast.makeText(getContext(), "Please Select Div", Toast.LENGTH_LONG).show();
                }else  if (getsubject==null) {
                    Toast.makeText(getContext(), "Please Select Subject", Toast.LENGTH_LONG).show();
                } else if (topic.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please Add Topic", Toast.LENGTH_LONG).show();
                }
                else {

                    final ProgressDialog progress = new ProgressDialog(getContext());
                    progress.setTitle("Adding Topic");
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
                                    String commands = "insert into tbl_topic(subjectname,section,standard,createdon,createdby,textassignment,acadmic_year)\n" +
                                            "values('"+getsubject+"','"+section+"','"+getstd+"',getdate(),'"+staffid+"','"+gettop+"','"+getacademic+"')";
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
                            builder.setMessage("Topic Added");
                            builder.setCancelable(false);
                            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    topic.setText("");
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    }, 1000);

                }
            }
        });

        return view;
    }
}

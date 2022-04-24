package com.school.stanthony;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class AdmissionRegister extends AppCompatActivity {
    String ConnectionResult,code,startdate,enddate,section,std,div,section1,Form1;
    Boolean isSuccess;
    EditText start, end;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1, s2, s3,s4,s5;
    AutoCompleteTextView studcode;
    Button btn;
    SharedPreferences sharedPref;
    TextView showstartdate,showenddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admission_register);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        start = findViewById(R.id.date);
        end = findViewById(R.id.date1);
        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        s4 = findViewById(R.id.s4);
        s5 = findViewById(R.id.s5);
        studcode = findViewById(R.id.code);
        btn = findViewById(R.id.btn);
        showstartdate=findViewById(R.id.showstartdate);
        showenddate=findViewById(R.id.showenddate);

        studcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studcode.setFocusableInTouchMode(true);
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

                dpd = new DatePickerDialog(AdmissionRegister.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                showstartdate.setText( +(monthOfYear + 1)+ "-" + dayOfMonth + "-" + year);
                                start.setText( dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        /////////////////////////// End time

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(AdmissionRegister.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
//                                end.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
//                                getmoonth = monthOfYear + 1;

                                showenddate.setText( (monthOfYear + 1)+ "-" + dayOfMonth + "-" + year);
                                end.setText( dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                                getmoonth=monthOfYear+1;
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
                String query = "select CONVERT(varchar(10),getdate(),110) sysdate ,CONVERT(varchar(10),getdate(),103) showdate";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    String fullname = rs.getString("showdate");
                    start.setText(fullname);
                    end.setText(fullname);

                    String show = rs.getString("sysdate");
                    showstartdate.setText(show);
                    showenddate.setText(show);
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
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdmissionRegister.this,R.layout.spinner11, data2);
                s1.setAdapter(NoCoreAdapter2);
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


//        ArrayList<String> data = new ArrayList<String>();
//        String stdd = "BBB";
//        data.add(stdd);
//
//        ArrayAdapter NoCoreAdapter = new ArrayAdapter(AdmissionRegister.this, R.layout.spinner11, data);
//        s2.setAdapter(NoCoreAdapter);
//
//        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdmissionRegister.this, R.layout.spinner11, data);
//        s3.setAdapter(NoCoreAdapter2);

        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section=s1.getSelectedItem().toString();
                if(section.equals("YYY")){
//                    ArrayList<String> data = new ArrayList<String>();
//                    String std = "YYY";
//                    data.add(std);
//
//                    ArrayAdapter NoCoreAdapter = new ArrayAdapter(AdmissionRegister.this, R.layout.spinner11, data);
//                    s2.setAdapter(NoCoreAdapter);
//
//                    ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdmissionRegister.this, R.layout.spinner11, data);
//                    s3.setAdapter(NoCoreAdapter2);

                    s2.setVisibility(View.INVISIBLE);
                    s3.setVisibility(View.INVISIBLE);
                }
                else {
                    s2.setVisibility(View.VISIBLE);
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
                            ArrayList<String> data2 = new ArrayList<String>();
                            while (rs.next()) {
                                String fullname = rs.getString("batch_for");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdmissionRegister.this, R.layout.spinner11, data2);
                            s2.setAdapter(NoCoreAdapter2);
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section1=s2.getSelectedItem().toString();
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
                                "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and \n" +
                                "class_name='"+section1+"'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<String>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("division");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(AdmissionRegister.this,R.layout.spinner11, data2);
                        s3.setAdapter(NoCoreAdapter2);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//////////////////// student code

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
                                "and Division='"+div+"' ";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data = new ArrayList<String>();
                        while (rs.next()) {
                            String cid = rs.getString("Applicant_type");
                            data.add(cid);
                        }
                        String[] array = data.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(AdmissionRegister.this,R.layout.spinner11, data);
                        studcode.setAdapter(NoCoreAdapter);
                        ConnectionResult = " Successful";
                        isSuccess = true;
                        conn.close();
                    }
                }
                catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                }

                studcode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        code=(String)adapterView.getItemAtPosition(i);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        /////////////////// Button
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                section=s1.getSelectedItem().toString();
                startdate=showstartdate.getText().toString();
                enddate=showenddate.getText().toString();
                studcode.setThreshold(0);
                code=studcode.getText().toString();

                // Toast.makeText(AdmissionRegister.this, ""+startdate+" "+enddate+" "+section+" "+std+" "+div+" "+code, Toast.LENGTH_LONG).show();

                if(startdate.equals("")){
                    Toast.makeText(AdmissionRegister.this, "Please Enter Start Date", Toast.LENGTH_LONG).show();
                }else  if(enddate.equals("")){
                    Toast.makeText(AdmissionRegister.this, "Please Enter End Date", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent i = new Intent(getApplicationContext(), AdmissionStudentsRegister.class);
                    i.putExtra("start", startdate);
                    i.putExtra("end", enddate);
                    i.putExtra("section", section);
                    i.putExtra("std", std);
                    i.putExtra("div", div);
                    i.putExtra("code", code);
                    startActivity(i);
                }
            }
        });
    }
}
package com.school.stanthony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class StudentsPendingFeeReport extends AppCompatActivity {
    String ConnectionResult,code,msg,section, std, div,Form1,section1;
    Boolean isSuccess;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1, s2, s3;
    AutoCompleteTextView studcode;
    Button btn;
    RadioGroup radioGroup;
    RadioButton radiotext;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitysummaryreport);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        studcode = findViewById(R.id.code);
        btn = findViewById(R.id.btn);
        radioGroup=findViewById(R.id.group1);

        studcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                studcode.setFocusableInTouchMode(true);
            }
        });

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
                String query = "select batchCode from tblbatchcodemaster where \n" +
                        "acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                while (rs.next())
                {
                    String fullname = rs.getString("batchcode");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentsPendingFeeReport.this,R.layout.spinner11, data2);
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
                                "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and \n" +
                                "batchcode='"+section+"'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<String>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("batch_for");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentsPendingFeeReport.this,R.layout.spinner11, data2);
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
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentsPendingFeeReport.this,R.layout.spinner11, data2);
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
                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(StudentsPendingFeeReport.this,android.R.layout.simple_list_item_1, data);
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
                        code=(String)adapterView.getItemAtPosition(i);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s1.getSelectedItem().toString().trim().equalsIgnoreCase("Select Section")) {
                    Toast.makeText(StudentsPendingFeeReport.this, "Select Section", Toast.LENGTH_LONG).show();
                } else   if (s2.getSelectedItem().toString().trim().equalsIgnoreCase("Select Class")) {
                    Toast.makeText(StudentsPendingFeeReport.this, "Select Class", Toast.LENGTH_LONG).show();
                } else   if (s3.getSelectedItem().toString().trim().equalsIgnoreCase("Select Div")) {
                    Toast.makeText(StudentsPendingFeeReport.this, "Select Div", Toast.LENGTH_LONG).show();
                }

                else if(studcode.getText().toString().equals("YYY")){
                    studcode.setThreshold(0);
                    std=s2.getSelectedItem().toString();
                    div=s3.getSelectedItem().toString();

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radiotext = findViewById(selectedId);
                    String text = radiotext.getText().toString();

                    Intent i=new Intent(getApplicationContext(),StudentFeeReportYYY.class);
                    i.putExtra("section",section);
                    i.putExtra("std",std);
                    i.putExtra("div",div);
                    i.putExtra("text",text);
                    startActivity(i);
                }
                else if(!studcode.getText().toString().equals("YYY")){
                    String code=studcode.getText().toString();
                    section=s1.getSelectedItem().toString();
                    std=s2.getSelectedItem().toString();
                    div=s3.getSelectedItem().toString();
                    Intent i=new Intent(getApplicationContext(),SingleStudentSummaryReport.class);
                    i.putExtra("section",section);
                    i.putExtra("std",std);
                    i.putExtra("div",div);
                    i.putExtra("code",code);
                    startActivity(i);
                }
            }
        });
    }
}
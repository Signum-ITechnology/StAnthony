package com.school.stanthony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class  StudentList extends AppCompatActivity {
    String ConnectionResult, Form1,section, std, div,section1;
    Boolean isSuccess;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1, s2, s3, s4;
    Button btn;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitystudentlist);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        btn = findViewById(R.id.btn);

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
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    String fullname = rs.getString("batchcode");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentList.this,R.layout.spinner11, data2);
                s1.setAdapter(NoCoreAdapter2);
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
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("batch_for");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentList.this,R.layout.spinner11, data2);
                        s2.setAdapter(NoCoreAdapter2);
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
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("division");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentList.this,R.layout.spinner11, data2);
                        s3.setAdapter(NoCoreAdapter2);
                        // NoCoreAdapter2.add("YYY");
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (s1.getSelectedItem().toString().trim().equalsIgnoreCase("Select Section")) {
                    Toast.makeText(StudentList.this, "Select Section", Toast.LENGTH_LONG).show();
                } else   if (s2.getSelectedItem().toString().trim().equalsIgnoreCase("Select Class")) {
                    Toast.makeText(StudentList.this, "Select Class", Toast.LENGTH_LONG).show();
                } else   if (s3.getSelectedItem().toString().trim().equalsIgnoreCase("Select Div")) {
                    Toast.makeText(StudentList.this, "Select Div", Toast.LENGTH_LONG).show();
                }else {
                    std = s2.getSelectedItem().toString();
                    div = s3.getSelectedItem().toString();

                    Intent i = new Intent(getApplicationContext(), StudentListReport.class);
                    i.putExtra("section", section);
                    i.putExtra("std", std);
                    i.putExtra("div", div);
                    startActivity(i);
                }
            }
        });
    }
}

package com.school.stanthony;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class  StudentNotice extends AppCompatActivity {
    String ConnectionResult, code,section, std, div,section1;
    Boolean isSuccess;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1, s2, s3;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitystudentlist);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        btn = findViewById(R.id.btn);

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
                        "Acadmic_Year=(select CompanyCode from tblcompanymaster where Isselected=1)\n" +
                        "union all             \n" +
                        "select batchCode from tblbatchcodemaster where \n" +
                        "acadmic_year=(select CompanyCode from tblcompanymaster where Isselected=1)";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<String>();
                while (rs.next())
                {
                    String fullname = rs.getString("batchcode");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentNotice.this,R.layout.spinner11, data2);
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
                } else {
                    s2.setVisibility(View.VISIBLE);
                    /////////////////// For Class
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select batch_for from tblbatchmaster where \n" +
                                    "Acadmic_Year=(select CompanyCode from tblcompanymaster where Isselected=1) and \n" +
                                    "batchcode='" + section + "'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<>();
                            while (rs.next()) {
                                String fullname = rs.getString("batch_for");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentNotice.this, R.layout.spinner11, data2);
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
                                "Acadmic_Year=(select CompanyCode from tblcompanymaster where Isselected=1) and \n" +
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
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(StudentNotice.this,R.layout.spinner11, data2);
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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    section=s1.getSelectedItem().toString();
                    std = s2.getSelectedItem().toString();
                    div = s3.getSelectedItem().toString();
                }catch (Exception e){}

                    Intent i = new Intent(getApplicationContext(), StudentNoticeReportt.class);
                    i.putExtra("section", section);
                    i.putExtra("std", std);
                    i.putExtra("div", div);
                    startActivity(i);
            }
        });
    }
}
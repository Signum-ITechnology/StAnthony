package com.school.stanthony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class BirthdayRegister extends AppCompatActivity {

    String ConnectionResult,getmonth,getdate,section,std,div,newmonth,Form1;
    Boolean isSuccess;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1,s2,s3,s4,s5;
    SharedPreferences sharedPreferences;
    Button submit;
    String[] date = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String[] month = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_register);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("teacherref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        s4 = findViewById(R.id.s4);
        s5 = findViewById(R.id.s5);
        submit = findViewById(R.id.btn);
    //    classname = findViewById(R.id.classname);
    //    divname = findViewById(R.id.div);

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner11, date);
        s1.setAdapter(adapter);

        ArrayAdapter<String> adapter2= new ArrayAdapter<>(this, R.layout.spinner11, month);
        s2.setAdapter(adapter2);

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
                        "Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "union all             \n" +
                        "select batchCode from tblbatchcodemaster where \n" +
                        "acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    String fullname = rs.getString("batchcode");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(BirthdayRegister.this,R.layout.spinner11, data2);
                s3.setAdapter(NoCoreAdapter2);
             //   NoCoreAdapter2.add("YYY");
                   section=s3.getSelectedItem().toString();
                //   Toast.makeText(this, ""+section, Toast.LENGTH_SHORT).show();
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


        s3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section=s3.getSelectedItem().toString();
                if(section.equals("YYY")){
                    s4.setVisibility(View.INVISIBLE);
                    s5.setVisibility(View.INVISIBLE);
                 //   classname.setVisibility(View.INVISIBLE);
                //    divname.setVisibility(View.INVISIBLE);
//                    s4.setFocusable(false);
//                    s5.setFocusable(false);
                }else {
                 //   s2.setVisibility(View.VISIBLE);
                    s4.setVisibility(View.VISIBLE);
                    s5.setVisibility(View.VISIBLE);
                }
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
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(BirthdayRegister.this,R.layout.spinner11, data2);
                        s4.setAdapter(NoCoreAdapter2);
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

        s4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                std=s4.getSelectedItem().toString();
                if(std.equals("")) {
                    s5.setVisibility(View.INVISIBLE);
                }
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
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("division");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(BirthdayRegister.this,R.layout.spinner11, data2);
                        s5.setAdapter(NoCoreAdapter2);
                        div=s5.getSelectedItem().toString();
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

        if(section.equals("YYY")){
            s4.setVisibility(View.INVISIBLE);
            s5.setVisibility(View.INVISIBLE);
//          s4.setFocusable(false);
//                    s5.setFocusable(false);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate=s1.getSelectedItem().toString();
                getmonth=s2.getSelectedItem().toString();
                if(getmonth.equals("January")){
                    newmonth="01";
                }else if(getmonth.equals("February")){
                    newmonth="02";
                }else if(getmonth.equals("March")){
                    newmonth="03";
                }else if(getmonth.equals("April")){
                    newmonth="04";
                }else if(getmonth.equals("May")){
                    newmonth="05";
                }else if(getmonth.equals("June")){
                    newmonth="06";
                }else if(getmonth.equals("July")){
                    newmonth="07";
                }else if(getmonth.equals("August")){
                    newmonth="08";
                }else if(getmonth.equals("September")){
                    newmonth="09";
                }else if(getmonth.equals("October")){
                    newmonth="10";
                }else if(getmonth.equals("November")){
                    newmonth="11";
                }else if(getmonth.equals("December")){
                    newmonth="12";
                }
          //     submit.setText(getdate+" "+newmonth+" "+section+" "+std+" "+div);
                Intent i=new Intent(getApplicationContext(),BirthdayRegisterReport.class);
                i.putExtra("date",getdate);
                i.putExtra("month",newmonth);
                i.putExtra("sec",section);
                i.putExtra("std",std);
                i.putExtra("div",div);
                startActivity(i);
            }
        });
    }
}

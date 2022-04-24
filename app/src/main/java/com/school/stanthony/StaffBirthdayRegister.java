package com.school.stanthony;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class StaffBirthdayRegister extends AppCompatActivity {

    String ConnectionResult,getmonth,getdate,getpost,newmonth,Form1;
    Boolean isSuccess;
    Connection conn;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1,s2,s3;
    Button submit;
    String[] date = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
    String[] month = {"January","February","March","April","May","June","July","August","September","October","November","December"};
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_birthday_register);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        submit = findViewById(R.id.btn);

        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, R.layout.spinner11, date);
        s1.setAdapter(adapter);

        ArrayAdapter<String> adapter2= new ArrayAdapter<>(this, R.layout.spinner11, month);
        s2.setAdapter(adapter2);

        /////////////////// For Post
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select distinct 'YYY' postnew from tbl_hrstaffnew where \n" +
                        "acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "union all\n" +
                        "select distinct postnew from tbl_hrstaffnew where \n" +
                        "acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"') and postnew!='NULL'";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    String fullname = rs.getString("postnew");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(this,R.layout.spinner11, data2);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdate=s1.getSelectedItem().toString();
                getmonth=s2.getSelectedItem().toString();
                getpost=s3.getSelectedItem().toString();
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

                Intent i=new Intent(getApplicationContext(),StaffBirthdayRegisterReport.class);
                i.putExtra("date",getdate);
                i.putExtra("month",newmonth);
                i.putExtra("post",getpost);
                startActivity(i);
            }
        });
    }
}

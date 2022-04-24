package com.school.stanthony;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.sql.Connection;
import java.util.Calendar;

public class WorkingDay extends AppCompatActivity {

    Button b1,b2,b3;
    Connection conn;
    Calendar c;
    int mMonth;
    DatePickerDialog dpd;
    TextView date;
    String start,section,std,div;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_day);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        section=getIntent().getExtras().getString("section");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");

        b1=findViewById(R.id.add);
        b2=findViewById(R.id.update);
        b3=findViewById(R.id.status);
        date=findViewById(R.id.date);

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        if (conn == null) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.id), "No Internet Coonection", Snackbar.LENGTH_INDEFINITE);
            snackbar.show();
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(WorkingDay.this,TeacherStudentAttendance.class);
                startActivity(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(WorkingDay.this,TeacherAttendenceStatus.class);
                i.putExtra("section",section);
                i.putExtra("std",std);
                i.putExtra("div",div);
                startActivity(i);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(WorkingDay.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                //    date.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                                String month=String.valueOf(monthOfYear+1);

                                if(month.length()==1){
                                    date.setText( dayOfMonth + "/0"+(monthOfYear + 1)+ "/" + year);
                                    start=date.getText().toString();
                                }
                                else {
                                    date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    start=date.getText().toString();
                                }

                                String first=start.substring(2,3);

                                if(!first.equals("/")){
                                    start="0"+start;
                                    Intent i=new Intent(getApplicationContext(),ChangeAttendence.class);
                                    i.putExtra("date",start);
                                    startActivity(i);
                                }
                                else {
                                    Intent i = new Intent(getApplicationContext(), ChangeAttendence.class);
                                    i.putExtra("date", start);
                                    startActivity(i);
                                }

                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();

            }
        });
    }

}
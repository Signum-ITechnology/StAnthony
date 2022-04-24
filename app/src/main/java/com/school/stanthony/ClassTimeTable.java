package com.school.stanthony;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ClassTimeTable extends AppCompatActivity {

    Button b1,b2,b3,b4,b5,b6;
    String monday="1";
    String tuesday="2";
    String wednesday="3";
    String thrusday="4";
    String friday="5";
    String saturday="6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_time_table);

        b1=findViewById(R.id.btn1);
        b2=findViewById(R.id.btn2);
        b3=findViewById(R.id.btn3);
        b4=findViewById(R.id.btn4);
        b5=findViewById(R.id.btn5);
        b6=findViewById(R.id.btn6);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassTimeTable.this, "Loading Please Wait...", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),MondayTimeTable.class);
                i.putExtra("Day",monday);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassTimeTable.this, "Loading Please Wait...", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),MondayTimeTable.class);
                i.putExtra("Day",tuesday);
                startActivity(i);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassTimeTable.this, "Loading Please Wait...", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),MondayTimeTable.class);
                i.putExtra("Day",wednesday);
                startActivity(i);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassTimeTable.this, "Loading Please Wait...", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),MondayTimeTable.class);
                i.putExtra("Day",thrusday);
                startActivity(i);
            }
        });

        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassTimeTable.this, "Loading Please Wait...", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),MondayTimeTable.class);
                i.putExtra("Day",friday);
                startActivity(i);
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ClassTimeTable.this, "Loading Please Wait...", Toast.LENGTH_LONG).show();
                Intent i=new Intent(getApplicationContext(),MondayTimeTable.class);
                i.putExtra("Day",saturday);
                startActivity(i);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

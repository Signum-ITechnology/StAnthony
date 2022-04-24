package com.school.stanthony;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class RegisteredUsers extends AppCompatActivity {

    DatabaseHelper myDatab = new DatabaseHelper(this);
    SharedPreferences sharedPref;
    ListView lv;
    String uname[];
    String contact[];

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_users);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);

        lv= findViewById(R.id.listview1);

        StringBuffer buffer=new StringBuffer();
        StringBuffer buffers=new StringBuffer();
        Cursor res=myDatab.getAllData();
        Cursor res1=myDatab.getAllData();

        while (res.moveToNext())
        {
            buffer.append(res.getString(1)+"\n");
        }
        uname=buffer.toString().split("\\n");

        while (res1.moveToNext())
        {
            buffers.append(res1.getString(2)+"\n");
        }
        contact=buffers.toString().split("\\n");

        Cursor cursor=myDatab.getAllData();

        Custom_Adapter ca=new Custom_Adapter(getApplicationContext(),uname,contact);
        lv.setAdapter(ca);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv = (TextView) view.findViewById(R.id.tvname);

                String data = tv.getText().toString();
                SharedPreferences.Editor edit = sharedPref.edit();
                edit.remove("code");
                edit.apply();

                edit.putBoolean("Registered", true);
                edit.putString("code",data);
                edit.apply();

                Intent i=new Intent(getApplicationContext(),HomePage.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}


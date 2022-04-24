package com.school.stanthony;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class CircularPage extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "",Code;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    SharedPreferences sharedPref;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData;
            CircularPage mydata = new CircularPage();
            MyData = mydata.replacetoast(Code);
            String[] fromwhere = {"TodayDate","Timing","Subject","Description"};
            int[] viewswhere = {R.id.date,R.id.time,R.id.subject,R.id.desc};
            adapter = new SimpleAdapter(CircularPage.this, MyData, R.layout.circularlist, fromwhere, viewswhere);
            listView.setAdapter(adapter);

            if (adapter.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CircularPage.this);
                builder1.setTitle("No Record Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getApplicationContext(),HomePage.class);
                        startActivity(i);
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView=findViewById(R.id.circular);
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Code=sharedPref.getString("code",null);

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        if (conn == null) {
            Snackbar snackbar=Snackbar.make(findViewById(R.id.id),"No Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(getIntent());
                    mainHandler.post(myRunnable);
                }
            });
            snackbar.show();
        }
        mainHandler.post(myRunnable);
    }

    public List<Map<String,String>> replacetoast(String Code) {
        List<Map<String, String>> data ;
        data = new ArrayList<>();
        try
        {
            ConnectionHelper conStr=new ConnectionHelper();
            conn =conStr.connectionclasss();
            if (conn == null)
            {
                ConnectionResult = "Check Your Internet Access!";
            }
            else
            {
                String query = "select CONVERT(varchar(10),TodayDate,103)TodayDate,Timing,Subject,Description from" +
                        " dbo.tbl_MasterNews where Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster where Applicant_type='"+Code+"') and Active=1";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<String,String>();
                    datanum.put("TodayDate",rs.getString("TodayDate"));
                    datanum.put("Timing",rs.getString("Timing"));
                    datanum.put("Subject",rs.getString("Subject"));
                    datanum.put("Description",rs.getString("Description"));
                    data.add(datanum);
                }
                ConnectionResult = "Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (Exception ex)
        {
            isSuccess = false;
            ConnectionResult = ex.getMessage();
        }
        return  data;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
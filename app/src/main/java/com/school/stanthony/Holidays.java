package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.snackbar.Snackbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Holidays extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "",count,Code;
    SharedPreferences sharedPref;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    SwipeRefreshLayout pullToRefresh;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData;
            Holidays mydata = new Holidays();
            MyData = mydata.replacetoast(Code);
            String[] fromwhere = {"no","subject","today","date"};
            int[] viewswhere = {R.id.no,R.id.subject,R.id.day,R.id.date};
            adapter = new SimpleAdapter(Holidays.this, MyData, R.layout.holidaylistlayout, fromwhere, viewswhere);
     //       mainHandler.postDelayed(myRunnable,1000);
            listView.setAdapter(adapter);

            if (adapter.getCount()==0){
                Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holidays);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        listView=findViewById(R.id.list);
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Code=sharedPref.getString("code",null);
        pullToRefresh=findViewById(R.id.id1);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mainHandler.post(myRunnable);
                pullToRefresh.setRefreshing(false);
            }
        });

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        if (conn == null) {
            Snackbar snackbar=Snackbar.make(findViewById(R.id.id),"No Internet Connection",Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
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
                String query =("select ROW_NUMBER() OVER (ORDER BY date)  AS Row,subject,today,\n" +
                        "CONVERT(varchar(10),date,103)date from dbo.tblholidaylist \n" +
                        "where Acadmic_year=(Select MAX(Acadmic_Year) from tbladmissionfeemaster)") ;
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum= new HashMap<>();
                    datanum.put("no",rs.getString("Row"));
                    datanum.put("subject",rs.getString("subject"));
                    datanum.put("today",rs.getString("today"));
                    datanum.put("date",rs.getString("date"));
                    data.add(datanum);
                }
                ConnectionResult = " Successful";
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

}
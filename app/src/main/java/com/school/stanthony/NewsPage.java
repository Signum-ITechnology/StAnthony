package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsPage extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "",Code;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    SharedPreferences sharedPref;
    ImageView imageView;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            NewsPage mydata = new NewsPage();
            MyData = mydata.replacetoast();
            String[] fromwhere = {"TodayDate","Timing","Subject","Description"};
            int[] viewswhere = {R.id.date,R.id.time,R.id.subject,R.id.desc};
            adapter = new SimpleAdapter(NewsPage.this, MyData, R.layout.circularlist, fromwhere, viewswhere);
        //    mainHandler.postDelayed(myRunnable,1000);
            listView.setAdapter(adapter);
         //   Toast.makeText(getApplicationContext(), ""+adapter.getCount(), Toast.LENGTH_SHORT).show();
            if (adapter.getCount()==0){
                imageView.setImageResource(R.drawable.norecord);
            }
        } // This is your code
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circular_page);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView=findViewById(R.id.circular);
        imageView=findViewById(R.id.image);

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

    public List<Map<String,String>> replacetoast() {
        List<Map<String, String>> data;
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
                String query = "select CONVERT(varchar(10),TodayDate,103)TodayDate,Timing,Subject,Description from dbo.tbl_MasterNews\n" +
                        " where Acadmic_year=(select companycode from tblcompanymaster where isselected='1') order by TodayDate desc";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<>();
                    datanum.put("TodayDate",rs.getString("TodayDate"));
                    datanum.put("Timing",rs.getString("Timing"));
                    datanum.put("Subject",rs.getString("Subject"));
                    datanum.put("Description",rs.getString("Description"));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
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

public class PtaMeeting extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "",Code;
    SharedPreferences sharedPref;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    ImageView imageView;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            PtaMeeting mydata = new PtaMeeting();
            MyData = mydata.replacetoast(Code);
            String[] fromwhere = {"TodayDate","Timing","Subject","Description"};
            int[] viewswhere = {R.id.date,R.id.time,R.id.subject,R.id.desc};
            adapter = new SimpleAdapter(PtaMeeting.this, MyData, R.layout.ptameetinglayout, fromwhere, viewswhere);

            if(adapter.getCount()==0){
                imageView.setImageResource(R.drawable.norecord);
            }
      //      mainHandler.postDelayed(myRunnable,1000);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pta_meeting);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView=findViewById(R.id.pta);
        imageView=findViewById(R.id.image);

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

    public List<Map<String,String>> replacetoast(String Form1) {
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
                String query = "select CONVERT(varchar(10),TodayDate,103)TodayDate,LTRIM(RIGHT(CONVERT(VARCHAR(20),Timing, 100),7))as Timing,Subject,Description,todaydate dd from \n" +
                        "tblPTA where Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') order by dd desc";
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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

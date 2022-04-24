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

public class MyMessagePage extends AppCompatActivity {
 Connection conn;
 String ConnectionResult = "";
 Boolean isSuccess;
 ListView listView;
 SimpleAdapter adapter;
 String Form1;
 SharedPreferences sharedPref;
 ImageView imageView;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            MyMessagePage mydata = new MyMessagePage();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"Row","issuedate","subject","notice"};
            int[] viewswhere = {R.id.Row, R.id.date,R.id.subject, R.id.notice};
            adapter = new SimpleAdapter(MyMessagePage.this, MyData, R.layout.mymsg, fromwhere, viewswhere);

            if(adapter.getCount()==0){
                imageView.setImageResource(R.drawable.norecord);
            }
         //   mainHandler.postDelayed(myRunnable,1000);
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymsg);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("loginref",MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        listView=findViewById(R.id.msg);
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

    public List<Map<String,String>> replacetoast(String Code ) {
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
                String query= "select ROW_NUMBER() OVER (ORDER BY issuedate desc)  AS Row,CONVERT(varchar(10),issuedate,103)issuedate,issuedate dd ,subject,notice\n" +
                        "from tblstudentnotice where student_code='"+Code+"' and \n" +
                        "Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Code+"')\n" +
                        "order by dd desc";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("issuedate",rs.getString("issuedate"));
                    datanum.put("subject",rs.getString("subject"));
                    datanum.put("notice",rs.getString("notice"));
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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

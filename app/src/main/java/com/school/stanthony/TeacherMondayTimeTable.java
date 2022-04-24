package com.school.stanthony;

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

import androidx.appcompat.app.AppCompatActivity;

public class TeacherMondayTimeTable extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    String Form1,day;
    SharedPreferences sharedPreferences;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            TeacherMondayTimeTable mydata = new TeacherMondayTimeTable();
            MyData = mydata.replacetoast(Form1,day);
            String[] fromwhere = {"Row", "Row1", "Row2", "Row3", "Row4","Row5"};
            int[] viewswhere = {R.id.section, R.id.classwhich, R.id.div, R.id.subject, R.id.stime,R.id.etime};
            adapter = new SimpleAdapter(TeacherMondayTimeTable.this, MyData, R.layout.classtimetable1, fromwhere, viewswhere);
            listView.setAdapter(adapter);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_monday_time_table);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);
        day=getIntent().getExtras().getString("Day");

        listView=findViewById(R.id.list);

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

    public List<Map<String,String>> replacetoast(String Code,String day ) {
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
                String query= "select batchcode,classname,division,Subject_Title,LTRIM(RIGHT(CONVERT(VARCHAR(20), StartTime, 100), 7))as \n" +
                        "StartTime,LTRIM(RIGHT(CONVERT(VARCHAR(20), EndTime, 100), 7))as EndTime,Day,StartTime DD\n" +
                        "from tbltimetabledetails where Professor_Id=\n" +
                        "(select Staff_id from tbl_HRstaffnew where StaffUser='"+Code+"') and day='"+day+"'\n" +
                        "and acadmic_year=(select TOP(1) selectedaca from tbl_hrstaffnew where staffuser='"+Code+"')order by DD";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<String,String>();
                    datanum.put("Row",rs.getString("batchcode"));
                    datanum.put("Row1",rs.getString("classname"));
                    datanum.put("Row2",rs.getString("division"));
                    datanum.put("Row3",rs.getString("Subject_Title"));
                    datanum.put("Row4",rs.getString("StartTime"));
                    datanum.put("Row5",rs.getString("EndTime"));
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

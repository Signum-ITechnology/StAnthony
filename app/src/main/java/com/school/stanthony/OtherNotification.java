package com.school.stanthony;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherNotification extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    SharedPreferences sharedPreferences;
    String Form1;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData;
            OtherNotification mydata = new OtherNotification();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"Row","date","subject","msg"};
            int[] viewswhere = {R.id.srno,R.id.date,R.id.subject,R.id.notice};
            adapter = new SimpleAdapter(OtherNotification.this, MyData, R.layout.teachernotification, fromwhere, viewswhere);

            if(adapter.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(OtherNotification.this);
                builder1.setTitle("No Record Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i=new Intent(getApplicationContext(),HomePage4.class);
                        startActivity(i);
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_notification);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("otherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Othercode", null);

        listView= findViewById(R.id.list);

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

    public List<Map<String,String>> replacetoast(String code) {
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
                String query = "select ROW_NUMBER() OVER (ORDER BY createdon)  AS Row, Convert(varchar(20),CreatedOn,103)CreatedOn,Subject,\n" +
                        "Messg from tblTeacherNotification\n" +
                        "where ForStaff=(SELECT Staff_ID from tbl_HRStaffnew where StaffUser='"+code+"' )\n" +
                        "and ForStaff!='YYY' and IsActive=1";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("date",rs.getString("CreatedOn"));
                    datanum.put("subject",rs.getString("Subject"));
                    datanum.put("msg",rs.getString("Messg"));
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

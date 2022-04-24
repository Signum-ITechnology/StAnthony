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

public class MondayTimeTable extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    String Form1,day;
    SharedPreferences sharedPref;
    ImageView imageView;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            MondayTimeTable mydata = new MondayTimeTable();
            MyData = mydata.replacetoast(Form1,day);
            String[] fromwhere = {"Row", "StartTime", "EndTime", "Subject_Title", "Professor_Name"};
            int[] viewswhere = {R.id.id, R.id.stime, R.id.etime, R.id.subject, R.id.mam};
            adapter = new SimpleAdapter(MondayTimeTable.this, MyData, R.layout.classtimetable, fromwhere, viewswhere);
       //     mainHandler.postDelayed(myRunnable,1000);
            if(adapter.getCount()==0){
                imageView.setImageResource(R.drawable.norecord);
            }

            //      mainHandler.postDelayed(myRunnable,1000);
            listView.setAdapter(adapter);

        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monday_time_table);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        day=getIntent().getExtras().getString("Day");
   //     Toast.makeText(this, ""+day, Toast.LENGTH_SHORT).show();

        listView=findViewById(R.id.list);
        imageView=findViewById(R.id.image);

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
                String query= "\n" +
                        "select ROW_NUMBER() \n" +
                        "                OVER (ORDER BY StartTime)  AS Row, LTRIM(RIGHT(CONVERT(VARCHAR(20), StartTime, 100), 7))as StartTime, LTRIM(RIGHT(CONVERT(VARCHAR(20), EndTime, 100), 7))as EndTime,Subject_Title,Professor_Name from  tbltimetabledetails \n" +
                        "where classname=(select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"')and Applicant_type='"+Code+"') and\n" +
                        "division=(select Division from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"')and Applicant_type='"+Code+"') and\n" +
                        "Day='"+day+"' and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"')\n" +
                        "and Subject_Title!='NULL' and Professor_Name!='NULL'";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<String,String>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("StartTime",rs.getString("StartTime"));
                    datanum.put("EndTime",rs.getString("EndTime"));
                    datanum.put("Subject_Title",rs.getString("Subject_Title"));
                    datanum.put("Professor_Name",rs.getString("Professor_Name"));
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

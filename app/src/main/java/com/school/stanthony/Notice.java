package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
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

public class Notice extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "",count;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    String Form1;
    SharedPreferences sharedPref;
    ImageView imageView;

   final Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            Notice mydata = new Notice();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"Row","issuedate","subject","notice"};
            int[] viewswhere = {R.id.Row,R.id.date,R.id.subject,R.id.notice};
            adapter = new SimpleAdapter(Notice.this, MyData, R.layout.mymessege, fromwhere, viewswhere);
            String total= String.valueOf(adapter.getCount());

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
        setContentView(R.layout.activity_notice);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        listView=findViewById(R.id.notice);
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
                String query = "select ROW_NUMBER() OVER (ORDER BY issuedate desc )  AS Row,subject,notice,CONVERT(varchar(10),issuedate,103)issuedate,issuedate dd from tblstudentnotice where \n" +
                        "Section in((select Batch_Code from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"')and Applicant_type='"+Code+"'),'All' )and\n" +
                        "std in((select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"')and Applicant_type='"+Code+"'),'All') and\n" +
                        "div in((select Division from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"')and Applicant_type='"+Code+"'),'All') and \n" +
                        "student_code='ALL' and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"') order by dd desc ";
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
}
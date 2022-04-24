package com.school.stanthony;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class PrincipalStudentNoticeReport extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "",count;
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    String Form1;
    SharedPreferences sharedPref;

    final Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData ;
            PrincipalStudentNoticeReport mydata = new PrincipalStudentNoticeReport();
            MyData = mydata.replacetoast(Form1);
            String[] fromwhere = {"issuedate","section","class","name","notice","created_by"};
            int[] viewswhere = {R.id.date,R.id.section,R.id.std,R.id.stuname,R.id.notice,R.id.name};
            adapter = new SimpleAdapter(PrincipalStudentNoticeReport.this, MyData, R.layout.noticelist, fromwhere, viewswhere);
            String total= String.valueOf(adapter.getCount());
            listView.setAdapter(adapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("prinref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Principalcode", null);

        listView=findViewById(R.id.notice);

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        if (conn == null) {
            Snackbar snackbar= Snackbar.make(findViewById(R.id.id),"No Internet Connection", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
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

    public List<Map<String, String>> replacetoast(String Code) {
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
                String query = "select  ROW_NUMBER() OVER (ORDER BY issuedate desc )  AS Row,CONVERT(varchar(10),issuedate,103)issuedate,section,std+'/'+div 'class',\n" +
                        "Student_Code 'Name',notice,issuedate dd,created_by from tblstudentnotice where Student_Code='ALL'\n" +
                        "and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Code+"')\n" +
                        "union all\n" +
                        "select  ROW_NUMBER() OVER (ORDER BY issuedate desc )  AS Row,CONVERT(varchar(10),issuedate,103)issuedate,section,std+'/'+div 'class',\n" +
                        "b.Name+' '+b.SurName 'Name',notice,issuedate dd,A.created_by  \n" +
                        "from tblstudentnotice a,tbladmissionfeemaster b where Student_Code!='ALL'\n" +
                        "and a.acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Code+"')\n" +
                        "and a.acadmic_year=b.acadmic_year and a.Student_Code=b.applicant_type and b.applicant_type!='NEW' order by dd desc";
                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String, String> datanum=new HashMap<>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("issuedate",rs.getString("issuedate"));
                    datanum.put("section",rs.getString("section"));
                    datanum.put("class",rs.getString("class"));
                    datanum.put("notice",rs.getString("notice"));
                    datanum.put("name",rs.getString("name"));
                    datanum.put("created_by",rs.getString("created_by"));
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

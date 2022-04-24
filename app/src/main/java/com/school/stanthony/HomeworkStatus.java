package com.school.stanthony;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeworkStatus extends AppCompatActivity {

    TextView homework,subdate,subtdate,status;
    String gethw,getsubdate,getsubtdate,getstatus,getsubject;
    String ConnectionResult,Form1,getchapter,gettopic;
    PreparedStatement stmt;
    ResultSet rs;
    Boolean isSuccess,success;
    Connection conn;
    ProgressDialog progress;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(HomeworkStatus.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    progress.dismiss();
                }
            }, 2000);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_status);

        homework=findViewById(R.id.hw);
        subdate=findViewById(R.id.subdate);
        subtdate=findViewById(R.id.subtdate);
        status=findViewById(R.id.status);

        gethw=getIntent().getExtras().getString("hw");
        getsubdate=getIntent().getExtras().getString("sb");
        gettopic=getIntent().getExtras().getString("topic");
        getchapter=getIntent().getExtras().getString("chapter");
        getsubject=getIntent().getExtras().getString("subject");
        Form1=getIntent().getExtras().getString("Form1");
        homework.setText(gethw);
        subdate.setText(getsubdate);

        mainHandler.post(myRunnable);

    }

    private void loaddata() {

        ////to check if record is inserted

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select assignmentstatus,CONVERT(varchar(10),submitted_date,103)submitted_date from tbl_StudentAssignmnet\n" +
                        "where s_code='"+Form1+"' and SubjectName='"+getsubject+"' and textassignment='"+gethw+"' and topic='"+gettopic+"'\n" +
                        "and Chapter_Name='"+getchapter+"' and  acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"') ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    getstatus = rs.getString("assignmentstatus");
                    getsubtdate = rs.getString("submitted_date");

                        status.setText(getstatus);
                        subtdate.setText(getsubtdate);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }
    }
}
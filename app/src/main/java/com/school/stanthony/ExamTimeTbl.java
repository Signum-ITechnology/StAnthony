package com.school.stanthony;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamTimeTbl extends AppCompatActivity {
    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess;
    ListView listView;
    SimpleAdapter adapter;
    String Form1,StudentCode,semester;
    SharedPreferences sharedPref;
    static ListView modeList;

    Handler mainHandler = new Handler(Looper.getMainLooper());

    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            List<Map<String, String>> MyData = null;
            ExamTimeTbl mydata = new ExamTimeTbl();
            MyData = mydata.replacetoast(Form1,semester);
            String[] fromwhere = {"Row","Title","semester","ExamDate","ExamStartTime","ExamEndTime"};
            int[] viewswhere = {R.id.Row,R.id.title,R.id.semester,R.id.date,R.id.stime,R.id.etime};
            adapter = new SimpleAdapter(ExamTimeTbl.this, MyData, R.layout.examtimetable, fromwhere, viewswhere);
     //       mainHandler.postDelayed(myRunnable,1000);
            listView.setAdapter(adapter);

            if(adapter.getCount()==0){
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ExamTimeTbl.this);
                builder1.setTitle("No Record Found");
                builder1.setIcon(R.drawable.nointernet);
                builder1.setCancelable(false);
                builder1.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
            }
        } // This is your code
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_time_tbl);
       this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        listView=findViewById(R.id.exam);

        final AlertDialog.Builder builder = new AlertDialog.Builder(ExamTimeTbl.this);
        builder.setTitle("Select Semester");
        modeList = new ListView(ExamTimeTbl.this);

        String[] blood = { "1st Unit","1st Semester","2nd Unit","2nd Semester"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExamTimeTbl.this,android.R.layout.simple_list_item_single_choice, blood);
        modeList.setAdapter(adapter);
        modeList.setChoiceMode(modeList.CHOICE_MODE_SINGLE);
        builder.setView(modeList);
        final Dialog dialog = builder.create();
        dialog.show();

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String checked= String.valueOf(modeList.getItemAtPosition(position));
               if(checked.equals("1st Unit")){
                   semester="3";
                   mainHandler.post(myRunnable);
                   dialog.dismiss();
               }else if(checked.equals("1st Semester")){
                   semester="1";
                   mainHandler.post(myRunnable);
                   dialog.dismiss();
               }else if(checked.equals("2nd Unit")){
                   semester="4";
                   mainHandler.post(myRunnable);
                   dialog.dismiss();
               }else if(checked.equals("2nd Semester")){
                    semester="2";
                    mainHandler.post(myRunnable);
                    dialog.dismiss();
                }
            }
        });
    }

    public List<Map<String,String>> replacetoast(String Code,String semester ) {
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
                String query= "select ROW_NUMBER() OVER (ORDER BY ExamDate)  AS Row, case when semester=1 then 'Sem 1' when semester=2 then 'Sem 2' when semester=3 then 'Unit 1' when semester=4 then 'Unit 2'end as semester,\n" +
                        "Title,CONVERT(varchar(10),ExamDate,103)ExamDate,LTRIM(RIGHT(CONVERT(VARCHAR(20), ExamStartTime, 100), 7))as ExamStartTime,\n" +
                        "LTRIM(RIGHT(CONVERT(VARCHAR(20), ExamEndTime, 100), 7))as ExamEndTime from tblExamtimetablemaster \n" +
                        "where Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"')\n" +
                        "and Class_Name=(Select Class_Name from tbladmissionfeemaster where \n" +
                        "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Code+"') \n" +
                        "and Applicant_type='"+Code+"') and semester ='"+semester+"'";

                Statement statement=conn.createStatement();
                ResultSet rs=statement.executeQuery(query);

                while (rs.next()){
                    Map<String,String> datanum=new HashMap<String,String>();
                    datanum.put("Row",rs.getString("Row"));
                    datanum.put("Title",rs.getString("Title"));
                    datanum.put("semester",rs.getString("semester"));
                    datanum.put("ExamDate",rs.getString("ExamDate"));
                    datanum.put("ExamStartTime",rs.getString("ExamStartTime"));
                    datanum.put("ExamEndTime",rs.getString("ExamEndTime"));
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

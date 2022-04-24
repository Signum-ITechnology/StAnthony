package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class McqSubjects extends AppCompatActivity {

    String Form1,ConnectionResult,getdate,gettime,getsem,checkdiff,checkstatus,getminute;
    SharedPreferences sharedPref;
    ImageView imageView;
    ListView listView;
    ArrayList<ClassListItems19> arraylist;
    ArrayList<ClassListItems19> itemArrayList;
    ConnectionHelper connectionClass;
    Boolean success,isSuccess;
    MyAppAdapter myAppAdapter;
    PreparedStatement stmt;
    Connection conn;
    ResultSet rs;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_subjects);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getsem=getIntent().getExtras().getString("sem");
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems19>();

        listView=findViewById(R.id.notice);
        imageView=findViewById(R.id.image);

        SyncData orderData = new SyncData();
        orderData.execute("");

   }


    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(McqSubjects.this, "Loading Details",
                    "Please Wait...", true);

            /////////  Bydefault StartDate

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null)
                {
                    ConnectionResult="NO INTERNET";
                }
                else
                {
                    String query = "select LTRIM(RIGHT(CONVERT(VARCHAR(20),GETDATE(),100),7)) time ,CONVERT(varchar(10),getdate(),23) showdate";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next())
                    {
                        getdate = rs.getString("showdate");
                        gettime = rs.getString("time");
                    }
                    ConnectionResult = " Successful";
                    isSuccess=true;
                    conn.close();
                }
            }
            catch (android.database.SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                Connection conn = connectionClass.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {
                    String query = "select subjectname,convert(varchar(10),shed_date,103)date,convert(varchar(10),shed_date,23)checkdate,start_time+' '+am +' - '+ end_time+' '+pm time,totalmarks,end_time+pm end_time,start_time+pm start_time,totalmarks \n" +
                            "from tblExamScheduleMaster where Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                            "and Class_name COLLATE SQL_Latin1_General_CP1_CI_AS=(select Class_Name from tbladmissionfeemaster where\n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                            "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and semester='"+getsem+"'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems19(rs.getString("subjectname"),rs.getString("date"),rs.getString("time"),rs.getString("totalmarks"),rs.getString("end_time"),rs.getString("totalmarks"),rs.getString("start_time"),rs.getString("checkdate")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data found!";
                        success = false;
                    }
                }
            } catch (Exception e)
            {
                e.printStackTrace();
                Writer writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                msg = writer.toString();
                success = false;
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {
            progress.dismiss();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, McqSubjects.this);
                    if(myAppAdapter.getCount()!=0) {
                        listView.setAdapter(myAppAdapter);
                        myAppAdapter.notifyDataSetChanged();
                    }else{
                        imageView.setImageResource(R.drawable.norecord);
                    }
                } catch (Exception ex)
                {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter {
        public class ViewHolder
        {
            TextView subject,date,time,endtime,questions,marks,status,starttime,checkdate,active;
            LinearLayout linearLayout;
        }

        public List<ClassListItems19> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems19> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems19>();
            arraylist.addAll(parkingList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View rowView = convertView;
            ViewHolder viewHolder= null;

            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.mcqsubject, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.subject = rowView.findViewById(R.id.subject);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.time = rowView.findViewById(R.id.time);
                viewHolder.questions = rowView.findViewById(R.id.questions);
                viewHolder.marks = rowView.findViewById(R.id.marks);
                viewHolder.endtime = rowView.findViewById(R.id.endtime);
                viewHolder.status = rowView.findViewById(R.id.status);
                viewHolder.starttime = rowView.findViewById(R.id.starttime);
                viewHolder.checkdate = rowView.findViewById(R.id.checkdate);
                viewHolder.active = rowView.findViewById(R.id.active);
                viewHolder.linearLayout = rowView.findViewById(R.id.linear);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.subject.setText(parkingList.get(position).getSubject()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.time.setText(parkingList.get(position).getTime()+"");
            viewHolder.questions.setText(parkingList.get(position).getQuestions()+"");
            viewHolder.marks.setText(parkingList.get(position).getMarks()+"");
            viewHolder.endtime.setText(parkingList.get(position).getEndtime()+"");
            viewHolder.starttime.setText(parkingList.get(position).getStarttime()+"");
            viewHolder.checkdate.setText(parkingList.get(position).getCheckdate()+"");

            //// check date
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();
                String secondate=viewHolder.checkdate.getText().toString();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "Select DateDiff(Day,'"+getdate+"','"+secondate+"' )'check'";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        checkdiff = rs.getString("check");
                    }
                    ConnectionResult = " Successful";
                    isSuccess = true;
                    conn.close();
                }
            } catch (android.database.SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

            /////////  get time in minutes
            String getetime=viewHolder.endtime.getText().toString();
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null)
                {
                    ConnectionResult="NO INTERNET";
                }
                else
                {
                    String query = "select datediff(minute,LTRIM(RIGHT(CONVERT(VARCHAR(20),GETDATE(),100),7)),'"+getetime+"')minutes";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next())
                    {
                        getminute = rs.getString("minutes");
                    }
                    ConnectionResult = " Successful";
                    isSuccess=true;
                    conn.close();
                }
            }
            catch (android.database.SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

            /////////////

            if(Integer.parseInt(checkdiff)>0){
                checkstatus="0";
                viewHolder.active.setText(checkstatus);
                viewHolder.status.setText("Inactive");
                viewHolder.status.setTextColor(Color.RED);
            }else if(Integer.parseInt(checkdiff)==0){
                if(Integer.parseInt(getminute)>0){
                    checkstatus="1";
                    viewHolder.active.setText(checkstatus);
                    viewHolder.status.setText("Active");
                    viewHolder.status.setTextColor(Color.GREEN);
                }else {
                    checkstatus = "0";
                    viewHolder.active.setText(checkstatus);
                    viewHolder.status.setText("Inactive");
                    viewHolder.status.setTextColor(Color.RED);
                }
            } else  {
                checkstatus="0";
                viewHolder.active.setText(checkstatus);
                viewHolder.status.setText("Completed");
                viewHolder.status.setTextColor(Color.RED);
            }


            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String isactive= finalViewHolder.active.getText().toString();
                    if(isactive.equals("1")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(McqSubjects.this);
                        LayoutInflater inflater = (McqSubjects.this).getLayoutInflater();
                        builder.setView(inflater.inflate(R.layout.mcqrules, null));
                        builder.setCancelable(false);

                        builder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String subject = itemArrayList.get(position).getSubject();
                                String date = itemArrayList.get(position).getDate();
                                String time = itemArrayList.get(position).getTime();
                                String total = itemArrayList.get(position).getMarks();
                                String stime = itemArrayList.get(position).getStarttime();
                                String etime = itemArrayList.get(position).getEndtime();
                                String noofquestions = itemArrayList.get(position).getQuestions();
                                Intent i = new Intent(getApplicationContext(), McqQuestions.class);
                                i.putExtra("subject", subject);
                                i.putExtra("date", date);
                                i.putExtra("time", time);
                                i.putExtra("total", total);
                                i.putExtra("stime", stime);
                                i.putExtra("etime", etime);
                                i.putExtra("noq", noofquestions);
                                i.putExtra("sem", getsem);
                                startActivity(i);

                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        alertDialog = builder.create();
                        alertDialog.show();
                        }
                }
            });

            return rowView;
        }
    }
}
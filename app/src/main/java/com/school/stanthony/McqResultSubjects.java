package com.school.stanthony;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import androidx.appcompat.app.AppCompatActivity;

public class McqResultSubjects extends AppCompatActivity {

    String Form1,ConnectionResult,getsem,getdate,gettime,getsubject;
    SharedPreferences sharedPref;
    ImageView imageView;
    ListView listView;
    ArrayList<ClassListItems20> arraylist;
    ArrayList<ClassListItems20> itemArrayList;
    ConnectionHelper connectionClass;
    Boolean success,isSuccess;
    MyAppAdapter myAppAdapter;
    PreparedStatement stmt;
    Connection conn;
    ResultSet rs;
    Dialog myDialog;
    ArrayList<String> totalquestion;
    ArrayList<String> totalanswer;
    ArrayList<String> totalcorrect;
    int a,b,c;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(McqResultSubjects.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ShowPopup();
                    progress.dismiss();
                }
            }, 2000);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_result_subjects);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getsem=getIntent().getExtras().getString("sem");
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        myDialog = new Dialog(McqResultSubjects.this);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems20>();

        listView=findViewById(R.id.notice);
        imageView=findViewById(R.id.image);

        SyncData orderData = new SyncData();
        orderData.execute("");

    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(McqResultSubjects.this, "Loading Details",
                    "Please Wait...", true);

        }

        @Override
        protected String doInBackground(String... strings) {
            try
            {
                Connection conn = connectionClass.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {
                    String query = "select distinct subject,date,time,totalmarks,noofquestions from mcqstudentanswers where semester='"+getsem+"' \n" +
                            "and student_code='"+Form1+"' and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems20(rs.getString("subject"),rs.getString("date"),rs.getString("time"),rs.getString("totalmarks"),rs.getString("noofquestions")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, McqResultSubjects.this);
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
            TextView subject,date,time,questions,marks,view;
        }

        public List<ClassListItems20> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems20> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems20>();
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
                rowView = inflater.inflate(R.layout.mcqresultsubject, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.subject = rowView.findViewById(R.id.subject);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.time = rowView.findViewById(R.id.time);
                viewHolder.questions = rowView.findViewById(R.id.questions);
                viewHolder.marks = rowView.findViewById(R.id.marks);
                viewHolder.view = rowView.findViewById(R.id.view);
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

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getdate=finalViewHolder.date.getText().toString();
                    gettime=finalViewHolder.time.getText().toString();
                    getsubject=finalViewHolder.subject.getText().toString();
                    mainHandler.post(myRunnable);
                }
            });


            return rowView;
        }
    }

    private void ShowPopup() {

        TextView total,correct,wrong,na,view;

        myDialog.setContentView(R.layout.mcqresultlayout);
        total=myDialog.findViewById(R.id.total);
        correct=myDialog.findViewById(R.id.correct);
        wrong=myDialog.findViewById(R.id.wrong);
        na=myDialog.findViewById(R.id.na);
        view=myDialog.findViewById(R.id.view);

        totalquestion=new ArrayList<>();
        totalanswer=new ArrayList<>();
        totalcorrect=new ArrayList<>();

        a=0;b=0;c=0;

        /////////////Total No of questions
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();
            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select noofquestions,selectedanswer,correctanswer from mcqstudentanswers where semester='"+getsem+"' and  date='"+getdate+"' and time='"+gettime+"' and subject='"+getsubject+"'\n" +
                        "and student_code='"+Form1+"' and acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String question = rs.getString("noofquestions");
                    totalquestion.add(question);
                    
                    String answer = rs.getString("selectedanswer");
                    totalanswer.add(answer);

                    String correctt = rs.getString("correctanswer");
                    totalcorrect.add(correctt);
                }
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


         for (int i=0;i< totalquestion.size();i++){
            String selectedanswer=totalanswer.get(i);
            String correctanswer=totalcorrect.get(i);

            if(selectedanswer.equals("notatm")){
                a++;
            }else if(selectedanswer.equals(correctanswer)){
                b++;
            }else if(!selectedanswer.equals(correctanswer)){
                c++;
            }
         }

        String totall= String.valueOf(totalquestion.size());
        total.setText(totall);
        correct.setText(""+b);
        wrong.setText(""+c);
        na.setText(""+a);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(McqResultSubjects.this,McqViewResult.class);
                i.putExtra("date",getdate);
                i.putExtra("time",gettime);
                i.putExtra("subject",getsubject);
                i.putExtra("sem",getsem);
                startActivity(i);
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(true);
    }

}

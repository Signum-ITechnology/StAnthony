package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class McqQuestions extends AppCompatActivity {

    ResultSet rs;
    PreparedStatement stmt;
    Connection conn;
    String ConnectionResult,academic,count,getsubject,getsem,getnoq;
    String getdate,gettime,gettotal,getstime,getetime,getminute,getcount;
    TextView date,time,total,subject,timeremaining;
    boolean isSuccess;
    private ArrayList<ClassListItems8> itemArrayList;
    private MyAppAdapter myAppAdapter;
    private ListView listView;
    private boolean success = false;
    private ConnectionHelper connectionClass;
    String Form1;
    Button btn;
    private long timee;
    Object[] array;
    SharedPreferences sharedPref;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog dialog=new ProgressDialog(McqQuestions.this);
            dialog.setTitle("Saving Answers");
            dialog.setMessage("Please wait a moment ...");
            dialog.setCancelable(false);
            dialog.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    ///////  Academic year

                    try {
                        ConnectionHelper conStr1 = new ConnectionHelper();
                        conn = conStr1.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select Acadmic_Year from tbladmissionfeemaster where applicant_type='"+Form1+"'\n" +
                                    "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            while (rs.next()) {
                                academic = rs.getString("Acadmic_Year");
                            }
                            ConnectionResult = " Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    } catch (android.database.SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    insertdata();
                    dialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(McqQuestions.this);
                    builder.setMessage("Answer Saved Successfully");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(McqQuestions.this, HomePage.class);
                            startActivity(i);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }, 5000);
        }};


    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog dialog=new ProgressDialog(McqQuestions.this);
            dialog.setTitle("Time Up");
            dialog.setMessage("Saving all your answers please wait a moment ...");
            dialog.setCancelable(false);
            dialog.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    insertdata();
                    dialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(McqQuestions.this);
                    builder.setMessage("Answer Saved Successfully");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(McqQuestions.this, HomePage.class);
                            startActivity(i);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                }
            }, 5000);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_questions);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getsubject=getIntent().getExtras().getString("subject");
        getdate=getIntent().getExtras().getString("date");
        gettime=getIntent().getExtras().getString("time");
        gettotal=getIntent().getExtras().getString("total");
        getstime=getIntent().getExtras().getString("stime");
        getetime=getIntent().getExtras().getString("etime");
        getnoq=getIntent().getExtras().getString("noq");
        getsem=getIntent().getExtras().getString("sem");
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);

        listView=findViewById(R.id.list);
        btn=findViewById(R.id.btn);
        date=findViewById(R.id.date);
        time=findViewById(R.id.time);
        subject=findViewById(R.id.subject);
        total=findViewById(R.id.total);
        timeremaining=findViewById(R.id.remainingtime);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems8>();

        date.setText(getdate);
        time.setText(gettime);
        total.setText(gettotal);
        subject.setText(getsubject);

        loaddata();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainHandler.post(myRunnable);

            }
        });

    }

    private void loaddata() {

        /////////  get count
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select count(isattempted)count from mcqstudentanswers\n" +
                        "where date='"+getdate+"' and time='"+gettime+"' and subject='"+getsubject+"' and student_code='"+Form1+"' and semester='"+getsem+"'\n" +
                        "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    getcount = rs.getString("count");
                    if(getcount.equals("0")){
                        SyncData orderData = new SyncData();
                        orderData.execute("");
                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(McqQuestions.this);
                        builder.setTitle("Already Attempted");
                        builder.setMessage("It seems you have already attempted these mcq module .");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                    finish();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                }
                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void insertdata() {

        List<Object> list= Arrays.asList(array);

        for (int i=0;i<=list.size()-1;i++) {
            String answer= String.valueOf(list.get(i));
            String correct=itemArrayList.get(i).correct.toString();
            String question=itemArrayList.get(i).question.toString();
            if(answer.equals("null")){
                answer="notatm";
            }

            ///// insert answers

            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "insert into mcqstudentanswers values('"+getdate+"','"+gettime+"','"+Form1+"','"+academic+"','"+getsem+"','"+getsubject+"','"+getnoq+"','"+gettotal+"','"+answer+"','"+correct+"',getdate(),'"+question+"','1')";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }

        }

    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(McqQuestions.this, "Loading Questions",
                    "Please Wait...", true);

            /////////  get time in minutes

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
                        long min= Long.parseLong(getminute);
                        timee=min*60*1000;

                        new CountDownTimer(timee, 1000){
                            public void onTick(long l){
                                timee=l;
                                updatetimer();
                            }
                            public  void onFinish(){
                                timeremaining.setText("Finish ..!");
                                mainHandler1.post(myRunnable1);
                            }
                        }.start();
                    }
                    ConnectionResult = " Successful";
                    isSuccess=true;
                    conn.close();
                }
            }
            catch (android.database.SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Connection conn = connectionClass.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {
                    String query = "select ROW_NUMBER() OVER (ORDER BY question )AS Row,question,option_a,option_b,option_c,option_d,answer from tblmcqquestionmster where \n" +
                            "batch_code=(select batch_code from tbladmissionfeemaster where\n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"') \n" +
                            "and Class_name=(select Class_Name from tbladmissionfeemaster where\n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and Applicant_type='"+Form1+"')\n" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') and semester='"+getsem+"' \n" +
                            "and subjectname='"+getsubject+"'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems8(rs.getString("Row"),rs.getString("question"),rs.getString("option_a"),rs.getString("option_b"),rs.getString("option_c"),rs.getString("option_d"),rs.getString("answer")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "";
                        success = true;
                    } else {
                        msg = "";
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
            //    Toast.makeText(this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, McqQuestions.this);
                    listView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(myAppAdapter);
                    Helper.getListViewSize(listView);
                    myAppAdapter.notifyDataSetChanged();
                    array=new Object[itemArrayList.size()];
                } catch (Exception ex)
                {

                }
            }
        }
    }

    public class MyAppAdapter extends BaseAdapter {
        public class ViewHolder
        {
            TextView srno,question,answer;
            RadioButton opta,optb,optc,optd,text;
            RadioGroup radioGroup;
        }

        public List<ClassListItems8> parkingList;

        public Context context;
        ArrayList<ClassListItems8> arraylist;

        private MyAppAdapter(List<ClassListItems8> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems8>();
            arraylist.addAll(parkingList);
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            View rowView = convertView;
            ViewHolder viewHolder= null;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.mcqquestions, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.question = rowView.findViewById(R.id.question);
                viewHolder.srno = rowView.findViewById(R.id.srno);
                viewHolder.opta = rowView.findViewById(R.id.op1);
                viewHolder.optb = rowView.findViewById(R.id.op2);
                viewHolder.optc = rowView.findViewById(R.id.op3);
                viewHolder.optd = rowView.findViewById(R.id.op4);
                viewHolder.answer = rowView.findViewById(R.id.correct);
                viewHolder.radioGroup=rowView.findViewById(R.id.group1);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.question.setText(" "+parkingList.get(position).getQuestion()+"");
            viewHolder.srno.setText(parkingList.get(position).getSrno()+"");
            viewHolder.opta.setText(parkingList.get(position).getOpta()+"");
            viewHolder.optb.setText(parkingList.get(position).getOptb()+"");
            viewHolder.optc.setText(parkingList.get(position).getOptc()+"");
            viewHolder.optd.setText(parkingList.get(position).getOptd()+"");
            viewHolder.answer.setText(parkingList.get(position).getCorrect()+"");

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup group, int i) {
                   switch (i){
                       case R.id.op1:
                           int row1= Integer.parseInt(finalViewHolder.srno.getText().toString());
                           int row11=row1-1;
                           array[row11]=finalViewHolder.opta.getText().toString();
                           break;
                       case R.id.op2:
                           int row2= Integer.parseInt(finalViewHolder.srno.getText().toString());
                           int row22=row2-1;
                           array[row22]=finalViewHolder.optb.getText().toString();
                           break;
                       case R.id.op3:
                           int row3= Integer.parseInt(finalViewHolder.srno.getText().toString());
                           int row33=row3-1;
                           array[row33]=finalViewHolder.optc.getText().toString();
                           break;
                       case R.id.op4:
                           int row4= Integer.parseInt(finalViewHolder.srno.getText().toString());
                           int row44=row4-1;
                           array[row44]=finalViewHolder.optd.getText().toString();
                           break;
                   }
               }
           });

            return rowView;
        }
    }

    private void updatetimer() {
        int min=(int)  timee/60000;
        int sec=(int)  timee %60000 /1000;

        String timeleft;

        //timeleft="0" +min;
        timeleft=""+min;
        timeleft += ":";


        if(sec < 10) timeleft+="0";
        timeleft +=sec;
//
//        if(timee<30000){
//            timeremaining.setTextColor(Color.RED);
//            timeremaining.startAnimation(anim);
//        }

        timeremaining.setText(timeleft);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        startActivity(new Intent(McqQuestions.this,HomePage.class));
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(McqQuestions.this,HomePage.class));
//    }
}
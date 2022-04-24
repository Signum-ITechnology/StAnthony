package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
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

public class McqViewResult extends AppCompatActivity {

    String Form1,ConnectionResult,getsem,getdate,gettime,getsubject;
    SharedPreferences sharedPref;
    ImageView imageView;
    ListView listView;
    ArrayList<ClassListItems21> arraylist;
    ArrayList<ClassListItems21> itemArrayList;
    ConnectionHelper connectionClass;
    Boolean success,isSuccess;
    MyAppAdapter myAppAdapter;
    PreparedStatement stmt;
    Connection conn;
    ResultSet rs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mcq_view_result);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getdate=getIntent().getExtras().getString("date");
        gettime=getIntent().getExtras().getString("time");
        getsubject=getIntent().getExtras().getString("subject");
        getsem=getIntent().getExtras().getString("sem");
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems21>();

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
            progress = ProgressDialog.show(McqViewResult.this, "Loading Details",
                    "Please Wait...", true);
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
                    String query = "select ROW_NUMBER() OVER (ORDER BY question )AS Row,question,selectedanswer,correctanswer from mcqstudentanswers\n" +
                            "where date='"+getdate+"' and time='"+gettime+"' and subject='"+getsubject+"' and student_code='"+Form1+"' and semester='"+getsem+"'\n" +
                            "and Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems21(rs.getString("Row"),rs.getString("question"),rs.getString("selectedanswer"),rs.getString("correctanswer")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, McqViewResult.this);
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
            TextView srno,question,answer,correct;
        }

        public List<ClassListItems21> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems21> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems21>();
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
                rowView = inflater.inflate(R.layout.mcqresultlayout2, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.srno = rowView.findViewById(R.id.srno);
                viewHolder.question = rowView.findViewById(R.id.question);
                viewHolder.answer = rowView.findViewById(R.id.answer);
                viewHolder.correct = rowView.findViewById(R.id.correct);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.srno.setText(parkingList.get(position).getSrno()+" ");
            viewHolder.question.setText(parkingList.get(position).getQuestion()+"");
            viewHolder.answer.setText(parkingList.get(position).getAnswer()+"");
            viewHolder.correct.setText(parkingList.get(position).getCorrect()+"");

            /////////////

            String selectedanswer=viewHolder.answer.getText().toString();
            String correctanswer=viewHolder.correct.getText().toString();

            if(selectedanswer.equals("notatm")){
                viewHolder.answer.setTextColor(Color.MAGENTA);
                viewHolder.answer.setText("Not Attempted");
            }else if(selectedanswer.equals(correctanswer)){
                viewHolder.answer.setTextColor(Color.GREEN);
            }else {
                viewHolder.answer.setTextColor(Color.RED);
            }


            return rowView;
        }
    }
    
}

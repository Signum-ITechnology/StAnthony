package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HomeWork extends AppCompatActivity {

    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess,success;
    ListView listView;
    SimpleAdapter adapter;
    String Form1,subject;
    ImageView imageView;
    SharedPreferences sharedPref;
    ArrayList<ClassListItems10> arraylist;
    ArrayList<ClassListItems10> itemArrayList;
    ConnectionHelper connectionClass;
    MyAppAdapter myAppAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listView=findViewById(R.id.work);
        imageView=findViewById(R.id.image);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems10>();

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        subject=getIntent().getExtras().getString("hw");

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        if (conn == null) {
            Snackbar snackbar=Snackbar.make(findViewById(R.id.id),"No Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(getIntent());

                    SyncData orderData = new SyncData();
                    orderData.execute("");
                }
            });
            snackbar.show();
        }

        SyncData orderData = new SyncData();
        orderData.execute("");
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(HomeWork.this, "Loading Details",
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
                    String query = "select date,day,subject,homeworkdesciption,topic,chapter_name,CONVERT(varchar(10),submission_date,103)submission_date,created_on dd from tblhomeworkentry\n" +
                            "where Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                            "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                            "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                            "and Division=(select Division from tbladmissionfeemaster where \n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                            "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and subject='"+subject+"' and category='1' and approval='1' order by dd desc";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems10(rs.getString("date"),rs.getString("day"),rs.getString("subject"),rs.getString("homeworkdesciption"),rs.getString("topic"),rs.getString("chapter_name"),rs.getString("submission_date")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, HomeWork.this);
                    if(myAppAdapter.getCount()!=0) {
                        listView.setAdapter(myAppAdapter);
                        myAppAdapter.notifyDataSetChanged();
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
            TextView date,day,subject,homework,topic,chapter,subdate,view;
        }

        public List<ClassListItems10> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems10> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems10>();
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
            MyAppAdapter.ViewHolder viewHolder= null;

            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.homeworklayout, parent, false);
                viewHolder = new MyAppAdapter.ViewHolder();
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.day = rowView.findViewById(R.id.day);
                viewHolder.subject = rowView.findViewById(R.id.subject);
                viewHolder.homework = rowView.findViewById(R.id.homework);
                viewHolder.topic = rowView.findViewById(R.id.topic);
                viewHolder.chapter = rowView.findViewById(R.id.chapter);
                viewHolder.subdate = rowView.findViewById(R.id.subdate);
                viewHolder.view = rowView.findViewById(R.id.view);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.day.setText(parkingList.get(position).getDay()+"");
            viewHolder.subject.setText(parkingList.get(position).getSubject()+"");
            viewHolder.homework.setText(parkingList.get(position).getHomework()+"");
            viewHolder.topic.setText(parkingList.get(position).getTopic()+"");
            viewHolder.chapter.setText(parkingList.get(position).getChapter()+"");
            viewHolder.subdate.setText(parkingList.get(position).getSubdate()+"");

            if(parkingList.get(position).getSubdate().equals("01/01/1900")){
                viewHolder.subdate.setText("Not Mentioned");
            }

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getApplicationContext(),HomeworkStatus.class);
                    i.putExtra("hw", finalViewHolder.homework.getText().toString());
                    i.putExtra("sb", finalViewHolder.subdate.getText().toString());
                    i.putExtra("topic", finalViewHolder.topic.getText().toString());
                    i.putExtra("chapter", finalViewHolder.chapter.getText().toString());
                    i.putExtra("subject", finalViewHolder.subject.getText().toString());
                    i.putExtra("Form1", Form1);
                    startActivity(i);
                }
            });

            return rowView;
        }
    }


}

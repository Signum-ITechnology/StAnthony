package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class VideoList extends AppCompatActivity {

    String subject,Form1;
    private ArrayList<ClassListItems7> itemArrayList;
    private MyAppAdapter myAppAdapter;
    private ListView gridView;
    private boolean success = false;
    private ConnectionHelper connectionClass;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        gridView = findViewById(R.id.gridView);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems7>();
        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        subject=getIntent().getExtras().getString("subject");

        SyncData orderData = new SyncData();
        orderData.execute("");


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String link = itemArrayList.get(position). getLink();
                Intent i=new Intent(getApplicationContext(),PlayVideo.class);
                i.putExtra("video",link);
                startActivity(i);
            }
        });
    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(VideoList.this, "Loading Videos",
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
                    String query = "select date,day,link,topic,chapter_name,CONVERT(varchar(10),submission_date,103)submission_date,created_on dd,homeworkdesciption from tblhomeworkentry\n" +
                            "where Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                            "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                            "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                            "and Division=(select Division from tbladmissionfeemaster where \n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                            "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and subject='"+subject+"' and category='3'  order by dd desc";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems7(rs.getString("date"),rs.getString("day"),rs.getString("topic"),rs.getString("chapter_name"),rs.getString("submission_date"),rs.getString("link"),rs.getString("link"),rs.getString("link"),rs.getString("homeworkdesciption")));
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
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, VideoList.this);
                    gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                    gridView.setAdapter(myAppAdapter);
                    myAppAdapter.notifyDataSetChanged();
                } catch (Exception ex)
                {

                }
            }
        }
    }

    public class MyAppAdapter extends BaseAdapter {
        public class ViewHolder
        {
            TextView date,day,topic,chapter,subdate,link,view,hw,videolink;
        }

        public List<ClassListItems7> parkingList;

        public Context context;
        ArrayList<ClassListItems7> arraylist;

        private MyAppAdapter(List<ClassListItems7> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems7>();
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
            MyAppAdapter.ViewHolder viewHolder= null;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.videolist2, parent, false);
                viewHolder = new MyAppAdapter.ViewHolder();
                viewHolder.day = rowView.findViewById(R.id.day);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.topic = rowView.findViewById(R.id.topic);
                viewHolder.chapter = rowView.findViewById(R.id.chapter);
                viewHolder.subdate = rowView.findViewById(R.id.subdate);
                viewHolder.link = rowView.findViewById(R.id.link);
                viewHolder.view = rowView.findViewById(R.id.view);
                viewHolder.videolink = rowView.findViewById(R.id.videolink);
                viewHolder.hw = rowView.findViewById(R.id.hw);
                //    viewHolder.imageView1 = rowView.findViewById(R.id.imageView);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.day.setText(parkingList.get(position).getDay()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.topic.setText(parkingList.get(position).getTopic()+"");
            viewHolder.chapter.setText(parkingList.get(position).getChapter()+"");
            viewHolder.subdate.setText(parkingList.get(position).getSubdate()+"");
            viewHolder.link.setText(parkingList.get(position).getLink()+"");
            viewHolder.hw.setText(parkingList.get(position).getHw()+"");

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.videolink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link=finalViewHolder.link.getText().toString();
                    Intent i=new Intent(getApplicationContext(),PlayVideo.class);
                    i.putExtra("video",link);
                    startActivity(i);
                }
            });


            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String link=finalViewHolder.link.getText().toString();
                    Intent i=new Intent(getApplicationContext(),HomeworkStatus.class);
                    i.putExtra("hw", finalViewHolder.hw.getText().toString());
                    i.putExtra("sb", finalViewHolder.subdate.getText().toString());
                    i.putExtra("topic", finalViewHolder.topic.getText().toString());
                    i.putExtra("chapter", finalViewHolder.chapter.getText().toString());
                    i.putExtra("subject", subject);
                    i.putExtra("Form1", Form1);
                    i.putExtra("cat", "3");
                    i.putExtra("video", link);
                    startActivity(i);
                }
            });

            return rowView;
        }
    }

}

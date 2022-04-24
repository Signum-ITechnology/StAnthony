package com.school.stanthony;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
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

import androidx.appcompat.app.AppCompatActivity;

public class NotesShowPdf extends AppCompatActivity {

    ResultSet rs;
    PreparedStatement stmt;
    Connection conn;
    String ConnectionResult,fullname,link,subject;
    boolean isSuccess;
    private ArrayList<ClassListItems3> itemArrayList;
    private MyAppAdapter myAppAdapter;
    private ListView listView;
    private boolean success = false;
    private ConnectionHelper connectionClass;
    ImageView imageView;
    String Form1;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("loginref", MODE_PRIVATE);
        Form1 = sharedPref.getString("code", null);
        subject=getIntent().getExtras().getString("pdf");
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems3>();

        listView=findViewById(R.id.notice);
        imageView=findViewById(R.id.image);

        SyncData orderData = new SyncData();
        orderData.execute("");

    }

    // Async Task has three overrided methods,
    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(NotesShowPdf.this, "Loading Pdf's",
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
                    String query = "select CONVERT(varchar(10),date,103)date,day,'http://stanthony.edusofterp.co.in/'+replace(replace(filepath,' ','%20'),'..','')url,topic,chapter_name,\n" +
                            "filepath,filename,CONVERT(varchar(10),submission_date,103)submission_date,created_on dd,homeworkdesciption from tbl_notes\n" +
                            "where Acadmic_year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                            "and Class_ID=(select Class_Name from tbladmissionfeemaster where \n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"') \n" +
                            "and Applicant_type='"+Form1+"' and Applicant_type!='NEW')\n" +
                            "and Division=(select Division from tbladmissionfeemaster where \n" +
                            "Acadmic_Year=(select isselected from tblstudentmaster where student_code='"+Form1+"')\n" +
                            "and Applicant_type='"+Form1+"' and Applicant_type!='NEW') and subject='"+subject+"' and category='2' order by dd desc";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems3(rs.getString("date"),rs.getString("day"),rs.getString("topic"),rs.getString("chapter_name"),rs.getString("submission_date"),rs.getString("url"),rs.getString("filepath"),rs.getString("filename"),rs.getString("homeworkdesciption")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, NotesShowPdf.this);
                    listView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(myAppAdapter);
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
            TextView date,day,topic,chapter,subdate,link,view,actuallink,upload,pdflink,hw,subdatetitle;
        }

        public List<ClassListItems3> parkingList;

        public Context context;
        ArrayList<ClassListItems3> arraylist;

        private MyAppAdapter(List<ClassListItems3> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems3>();
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
                rowView = inflater.inflate(R.layout.fileoption, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.day = rowView.findViewById(R.id.day);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.topic = rowView.findViewById(R.id.topic);
                viewHolder.chapter = rowView.findViewById(R.id.chapter);
                viewHolder.subdate = rowView.findViewById(R.id.subdate);
                viewHolder.link = rowView.findViewById(R.id.link);
                viewHolder.actuallink = rowView.findViewById(R.id.actuallink);
                viewHolder.upload = rowView.findViewById(R.id.upload);
                viewHolder.view = rowView.findViewById(R.id.view);
                viewHolder.pdflink = rowView.findViewById(R.id.pdflink);
                viewHolder.hw = rowView.findViewById(R.id.hw);
                viewHolder.subdatetitle = rowView.findViewById(R.id.subdatetitle);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.day.setText(parkingList.get(position).getDay()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.topic.setText(parkingList.get(position).getTopic()+"");
            viewHolder.chapter.setText(parkingList.get(position).getChapter()+"");
            viewHolder.subdate.setText(parkingList.get(position).getSubdate()+"");
            viewHolder.link.setText(parkingList.get(position).getLink()+"");
            viewHolder.actuallink.setText(parkingList.get(position).getActuallink()+"");
            viewHolder.upload.setText(parkingList.get(position).getUpload()+"");
            viewHolder.hw.setText(parkingList.get(position).getHw()+"");
            viewHolder.subdate.setVisibility(View.INVISIBLE);
            viewHolder.subdatetitle.setVisibility(View.INVISIBLE);

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String upload=finalViewHolder.upload.getText().toString();
                    if(upload.equals("Mobile")){
                        link=finalViewHolder.actuallink.getText().toString();
                    }else {
                        link=finalViewHolder.link.getText().toString();
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(NotesShowPdf.this);
                    builder.setMessage(finalViewHolder.hw.getText().toString());
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            viewHolder.pdflink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String upload=finalViewHolder.upload.getText().toString();
                    if(upload.equals("Mobile")){
                        String link=finalViewHolder.actuallink.getText().toString();
                        Intent i=new Intent(getApplicationContext(),ViewAssignmentPdf.class);
                        i.putExtra("pdf",link);
                        i.putExtra("check","mob");
                        startActivity(i);
                    }else {
                        String link=finalViewHolder.link.getText().toString();
                        Intent i=new Intent(getApplicationContext(),ViewAssignmentPdf.class);
                        i.putExtra("pdf",link);
                        i.putExtra("check","erp");
                        startActivity(i);
                    }

                }
            });

            return rowView;
        }
    }
}

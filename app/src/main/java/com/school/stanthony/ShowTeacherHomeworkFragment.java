package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;

public class ShowTeacherHomeworkFragment  extends Fragment {

    ArrayList<ClassListItems6> arraylist;
    ArrayList<ClassListItems6> itemArrayList;
    ConnectionHelper connectionClass;
    Boolean success,isSuccess;
    MyAppAdapter myAppAdapter;
    ListView listView;
    ImageView imageView;
    SharedPreferences sharedPref;
    String Form1,ConnectionResult,getstaffid;
    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.show_teacher_fragment, container, false);

        listView =view.findViewById(R.id.listview);
        imageView = view.findViewById(R.id.image);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems6>();

        sharedPref = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPref.getString("Teachercode", null);

        SyncData orderData = new SyncData();
        orderData.execute("");

        return view;
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(getContext(), "Loading Details",
                    "Please Wait...", true);

            ////For Staffid

            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select staff_id from tbl_hrstaffnew where staffuser='"+Form1+"' and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        getstaffid = rs.getString("staff_id");
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
                    String query = "select id,date,day,batch_code,class_id,division,subject,topic,chapter_name,homeworkdesciption,\n" +
                            "convert(varchar(10),submission_date,103)'subdate',category,filepath,link from tblhomeworkentry\n" +
                            "where created_by=(SELECT Staff_ID from tbl_HRStaffnew where StaffUser='"+Form1+"' \n" +
                            "and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"'))\n" +
                            "and acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') order by date desc";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems6(rs.getString("id"),rs.getString("date"),rs.getString("day"),rs.getString("batch_code"),rs.getString("class_id"),rs.getString("division"),rs.getString("subject")
                                        ,rs.getString("topic"),rs.getString("chapter_name"),rs.getString("homeworkdesciption"),rs.getString("subdate"),rs.getString("category"),rs.getString("filepath"),rs.getString("link")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, getContext());
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
            TextView id,date,day,section,std,div,subject,homework,subdate,topic,chapter;
            TextView edit,update,category,pdflink,videolink;
        }

        public List<ClassListItems6> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems6> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems6>();
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
                rowView = inflater.inflate(R.layout.homeworklist, parent, false);
                viewHolder = new MyAppAdapter.ViewHolder();
                viewHolder.id = rowView.findViewById(R.id.id);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.day = rowView.findViewById(R.id.day);
                viewHolder.section = rowView.findViewById(R.id.section);
                viewHolder.std = rowView.findViewById(R.id.std);
                viewHolder.div = rowView.findViewById(R.id.div);
                viewHolder.subject = rowView.findViewById(R.id.subject);
                viewHolder.topic = rowView.findViewById(R.id.topic);
                viewHolder.chapter = rowView.findViewById(R.id.chapter);
                viewHolder.homework = rowView.findViewById(R.id.homework);
                viewHolder.subdate = rowView.findViewById(R.id.subdate);
                viewHolder.edit = rowView.findViewById(R.id.edit);
                viewHolder.update = rowView.findViewById(R.id.update);
                viewHolder.category = rowView.findViewById(R.id.cat);
                viewHolder.pdflink = rowView.findViewById(R.id.pdf);
                viewHolder.videolink = rowView.findViewById(R.id.video);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.id.setText(parkingList.get(position).getId()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.day.setText(parkingList.get(position).getDay()+"");
            viewHolder.section.setText(parkingList.get(position).getSection()+"");
            viewHolder.std.setText(parkingList.get(position).getStd()+"");
            viewHolder.div.setText(parkingList.get(position).getDiv()+"");
            viewHolder.subject.setText(parkingList.get(position).getSubject()+"");
            viewHolder.topic.setText(parkingList.get(position).getTopic()+"");
            viewHolder.chapter.setText(parkingList.get(position).getChapter()+"");
            viewHolder.homework.setText(parkingList.get(position).getHomework()+"");
            viewHolder.subdate.setText(parkingList.get(position).getSubdate()+"");

            if(parkingList.get(position).getCategory().equals("1")){
                viewHolder.edit.setText("  View / Edit Homework");
            }else if(parkingList.get(position).getCategory().equals("2")){
                viewHolder.edit.setText("  View / Edit Pdf Hw");
            }else if(parkingList.get(position).getCategory().equals("3")){
                viewHolder.edit.setText("  View / Edit Video Hw");
            }


            if(parkingList.get(position).getSubdate().equals("01/01/1900")){
                viewHolder.subdate.setText("Not Mentioned");
            }

            viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String category=parkingList.get(position).getCategory();
                    String pdflink=parkingList.get(position).getPdflink();
                    String videolink=parkingList.get(position).getVideolink();
                    String id=parkingList.get(position).getId();
                    String text=parkingList.get(position).getHomework();

                    if(category.equals("1")) {
                        Intent i = new Intent(getContext(), EditHomework.class);
                        i.putExtra("id", id);
                        i.putExtra("text", text);
                        i.putExtra("show", "Edit HomeWork");
                        i.putExtra("cat", "1");
                        startActivity(i);
                    }else if(category.equals("2")) {
                        Intent i = new Intent(getContext(), EditHomework.class);
                        i.putExtra("id", id);
                        i.putExtra("text", text);
                        i.putExtra("show", "Edit Pdf HomeWork");
                        i.putExtra("cat", "2");
                        i.putExtra("pdf",pdflink);
                        startActivity(i);
                    }else if(category.equals("3")) {
                        Intent i = new Intent(getContext(), EditHomework.class);
                        i.putExtra("id", id);
                        i.putExtra("text", text);
                        i.putExtra("show", "Edit Video HomeWork");
                        i.putExtra("cat", "3");
                        i.putExtra("video",videolink);
                        startActivity(i);
                    }
                }
            });

            viewHolder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String section=parkingList.get(position).getSection();
                    String std=parkingList.get(position).getStd();
                    String div=parkingList.get(position).getDiv();
                    String subject=parkingList.get(position).getSubject();
                    String topic=parkingList.get(position).getTopic();
                    String chapter=parkingList.get(position).getChapter();
                    String hw=parkingList.get(position).getHomework();
                    String category=parkingList.get(position).getCategory();
                    String pdflink=parkingList.get(position).getPdflink();
                    String videolink=parkingList.get(position).getVideolink();
                    Intent i=new Intent(getContext(),AssignmentStatusEntry.class);
                    i.putExtra("section",section);
                    i.putExtra("std",std);
                    i.putExtra("div",div);
                    i.putExtra("subject",subject);
                    i.putExtra("topic",topic);
                    i.putExtra("chapter",chapter);
                    i.putExtra("hw",hw);
                    i.putExtra("sid",getstaffid);
                    i.putExtra("cat", category);
                    i.putExtra("video",videolink);
                    i.putExtra("pdf",pdflink);
                    startActivity(i);

                }
            });

            return rowView;
        }
    }
}
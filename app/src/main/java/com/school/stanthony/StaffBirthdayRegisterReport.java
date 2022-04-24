package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class StaffBirthdayRegisterReport extends AppCompatActivity {

    String Form1,post,date,month;
    ArrayList<ClassListItems4> arraylist;
    ArrayList<ClassListItems4> itemArrayList;
    ConnectionHelper ConnectionHelper;
    ListView listView;
    MyAppAdapter myAppAdapter;
    boolean success = false;
    ImageView imageView;
    SharedPreferences sharedPref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_birthday_register_report);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        listView = findViewById(R.id.listview);
        imageView = findViewById(R.id.image);
        ConnectionHelper = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems4>();

        date=getIntent().getExtras().getString("date");
        month=getIntent().getExtras().getString("month");
        post=getIntent().getExtras().getString("post");

        if(post.equals("YYY")){
            SyncData orderData = new SyncData();
            orderData.execute("");
        }else{
            SyncData1 orderData1 = new SyncData1();
            orderData1.execute("");
        }
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(StaffBirthdayRegisterReport.this, "Staff Birthday List",
                    "Loading Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                Connection conn = ConnectionHelper.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {
                    // Change below query according to your own database.
                    String query = "select 'http://stanthony.edusofterp.co.in'+replace(replace(imagepath,' ','%20'),'..','') url,\n" +
                            "name,convert(varchar(11),dob, 113) 'Birth Date',department,designation from tbl_hrstaffnew\n" +
                            "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            "and month(dob)='"+month+"' and day(dob)='"+date+"' order by dob";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems4(rs.getString("url"),rs.getString("name"),rs.getString("department"),rs.getString("designation"),rs.getString("Birth Date")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data Found";
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, getApplicationContext());
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

    private class SyncData1 extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(StaffBirthdayRegisterReport.this, "Staff Birthday List",
                    "Loading Please Wait...", true);
        }

        @Override
        protected String doInBackground(String... strings)
        {
            try
            {
                Connection conn = ConnectionHelper.connectionclasss();
                if (conn == null)
                {
                    success = false;
                }
                else {
                    // Change below query according to your own database.
                    String query = "select 'http://stanthony.edusofterp.co.in'+replace(replace(imagepath,' ','%20'),'..','') url,\n" +
                            "name,convert(varchar(11),dob, 113) 'Birth Date',department,designation from tbl_hrstaffnew\n" +
                            "where Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                            "and month(dob)='"+month+"' and day(dob)='"+date+"' and postnew='"+post+"' order by dob";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems4(rs.getString("url"),rs.getString("name"),rs.getString("department"),rs.getString("designation"),rs.getString("Birth Date")));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        msg = "Found";
                        success = true;
                    } else {
                        msg = "No Data Found";
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, getApplicationContext());
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
            TextView name,department,designation,contact;
            ImageView imageView;
        }

        public List<ClassListItems4> parkingList;

        public Context context;


        private MyAppAdapter(List<ClassListItems4> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems4>();
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

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            View rowView = convertView;
            MyAppAdapter.ViewHolder viewHolder;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.stafflist, parent, false);
                viewHolder = new MyAppAdapter.ViewHolder();
                viewHolder.name = rowView.findViewById(R.id.textName);
                viewHolder.imageView = rowView.findViewById(R.id.imageView);
                viewHolder.department=rowView.findViewById(R.id.department);
                viewHolder.designation=rowView.findViewById(R.id.designation);
                viewHolder.contact=rowView.findViewById(R.id.contact);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (MyAppAdapter.ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.name.setText(parkingList.get(position).getName()+"");
            viewHolder.department.setText(parkingList.get(position).getDepartment()+"  ");
            viewHolder.designation.setText(parkingList.get(position).getDesignation()+"  ");
            viewHolder.contact.setText(parkingList.get(position).getContact()+"");
            Glide.with(context).load(parkingList.get(position).getUrl()).error(R.drawable.logo).into(viewHolder.imageView);

            return rowView;
        }
    }
}

package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class VistorList extends AppCompatActivity {

    Connection conn;
    Boolean success;
    ListView listView;
    String Form1;
    ImageView imageView;
    SharedPreferences sharedPref;
    ArrayList<ClassListItems26> arraylist;
    ArrayList<ClassListItems26> itemArrayList;
    ConnectionHelper connectionClass;
    MyAppAdapter myAppAdapter;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vistor_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("otherref",MODE_PRIVATE);
        Form1 = sharedPref.getString("Othercode", null);

        floatingActionButton=findViewById(R.id.floating);
        listView=findViewById(R.id.list);
        imageView=findViewById(R.id.image);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems26>();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),VistorEntry.class));
            }
        });

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        if (conn == null) {
            Snackbar snackbar= Snackbar.make(findViewById(R.id.id),"No Internet Connection", Snackbar.LENGTH_INDEFINITE).setAction("RETRY", new View.OnClickListener() {
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
            progress = ProgressDialog.show(VistorList.this, "Loading Details",
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
                    String query = "select visitor_id,convert(varchar(10),date1,103)date,CONVERT(VARCHAR(20),visitor_in_time, 100)as intime,\n" +
                            "CONVERT(VARCHAR(20),visitor_out_time, 100)as outtime,name from tblVisitorEntry\n" +
                            "where academic_year=(select max(acadmic_year) from tbl_hrstaffnew where staffuser='"+Form1+"')";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems26(rs.getString("visitor_id"),rs.getString("date"),rs.getString("name"),rs.getString("intime"),rs.getString("outtime")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, VistorList.this);
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
            TextView id,date,name,intime,outtime;
        }

        public List<ClassListItems26> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems26> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems26>();
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
                rowView = inflater.inflate(R.layout.vistorlist, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.id = rowView.findViewById(R.id.id);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.name = rowView.findViewById(R.id.name);
                viewHolder.intime = rowView.findViewById(R.id.intime);
                viewHolder.outtime = rowView.findViewById(R.id.outtime);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.id.setText(parkingList.get(position).getId()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.name.setText(parkingList.get(position).getName()+"");
            viewHolder.intime.setText(parkingList.get(position).getIntime()+"");
            viewHolder.outtime.setText(parkingList.get(position).getOuttime()+"");

            if(parkingList.get(position).getOuttime().equals("")){
                viewHolder.outtime.setText("-- ( Update ) ");
                viewHolder.outtime.setTextColor(Color.RED);
            }else {
                viewHolder.outtime.setText(parkingList.get(position).getOuttime()+"");
                viewHolder.outtime.setTextColor(Color.BLACK);
            }

            viewHolder.outtime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            return rowView;
        }
    }
}
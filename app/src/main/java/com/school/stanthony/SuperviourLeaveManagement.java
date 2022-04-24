package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Color;
import android.os.AsyncTask;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SuperviourLeaveManagement extends AppCompatActivity {

    PreparedStatement stmt;
    ResultSet rs;
    Connection conn;
    Boolean isSuccess;
    Boolean success;
    ListView listView;
    String Form1,ConnectionResult,name,designation;
    ImageView imageView;
    ArrayList<ClassListItems24> arraylist;
    ArrayList<ClassListItems24> itemArrayList;
    ConnectionHelper connectionClass;
    MyAppAdapter myAppAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_leave_status);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("prinref", MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Principalcode", null);

        listView=findViewById(R.id.list);
        imageView=findViewById(R.id.image);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems24>();

        SyncData orderData = new SyncData();
        orderData.execute("");

    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "No Internet Connection";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(SuperviourLeaveManagement.this, "Loading Details",
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
                    String query = "select id,convert(varchar(10),createdon,103)'date',hodapprovoed,principalapprovoed+staffid'staffid',leavetype,leavefrom+' - '+leavetill 'leavedate' \n" +
                            "from tbl_hrappliedleave where Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') order by createdon desc";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems24(rs.getString("id"),rs.getString("date"),rs.getString("hodapprovoed"),rs.getString("staffid"),rs.getString("leavetype"),rs.getString("leavedate")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, SuperviourLeaveManagement.this);
                    if(myAppAdapter.getCount()!=0) {
                        listView.setAdapter(myAppAdapter);
                        myAppAdapter.notifyDataSetChanged();
                    }else {
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
            TextView id,date,status,hodstatus,pristatus,type,leavedate,designation,name,view;
        }

        public List<ClassListItems24> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems24> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems24>();
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
            ViewHolder viewHolder= null;

            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.leavestatus2, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.id = rowView.findViewById(R.id.id);
                viewHolder.hodstatus = rowView.findViewById(R.id.hodstatus);
                viewHolder.pristatus = rowView.findViewById(R.id.pristatus);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.status = rowView.findViewById(R.id.status);
                viewHolder.type = rowView.findViewById(R.id.type);
                viewHolder.leavedate = rowView.findViewById(R.id.leavedate);
                viewHolder.name = rowView.findViewById(R.id.name);
                viewHolder.designation = rowView.findViewById(R.id.designation);
                viewHolder.view = rowView.findViewById(R.id.view);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.id.setText(parkingList.get(position).getId()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.hodstatus.setText(parkingList.get(position).getHodstatus()+"");
            viewHolder.pristatus.setText(parkingList.get(position).getPristatus()+"");
            viewHolder.type.setText(parkingList.get(position).getType()+"");
            viewHolder.leavedate.setText(parkingList.get(position).getLeavedate()+"");

            if(parkingList.get(position).getPristatus().startsWith("0")){
                viewHolder.status.setText("Principal Approved");
                viewHolder.name.setEnabled(false);
            }else if(parkingList.get(position).getPristatus().startsWith("2")){
                viewHolder.status.setText("Principal Rejected");
                viewHolder.name.setEnabled(false);
                viewHolder.status.setTextColor(Color.RED);
            }else if(parkingList.get(position).getHodstatus().equals("1")){
                viewHolder.status.setText("Pending");
            }else if(parkingList.get(position).getHodstatus().equals("0")){
                viewHolder.status.setText("Superviour Approved");
                viewHolder.name.setEnabled(false);
            }else if(parkingList.get(position).getHodstatus().equals("2")){
                viewHolder.status.setText("Superviour Rejected");
                viewHolder.name.setEnabled(false);
                viewHolder.status.setTextColor(Color.RED);
            }

            ////For name of staff
            String staff=viewHolder.pristatus.getText().toString();
            String staffid=staff.substring(1,2);
            try {
                ConnectionHelper conStr1 = new ConnectionHelper();
                conn = conStr1.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String query = "select name,designation from tbl_hrstaffnew where staff_id='"+staffid+"'\n" +
                            "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        name = rs.getString("name");
                        designation = rs.getString("designation");
                        viewHolder.name.setText(name);
                        viewHolder.designation.setText(designation);
                    }
                    ConnectionResult = "Successful";
                    isSuccess = true;
                    conn.close();
                }
            } catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }

//            else if(parkingList.get(position).getPristatus().equals("0")){
//                viewHolder.status.setText("Principal Approved");
//            }


            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalViewHolder.hodstatus.getText().toString().equals("0")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SuperviourLeaveManagement.this);
                        builder.setMessage("Leave Already Approved");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else if(finalViewHolder.pristatus.getText().toString().equals("2")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(SuperviourLeaveManagement.this);
                        builder.setMessage("Leave Already Rejected");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }else {
                        String id = finalViewHolder.id.getText().toString();
                        String staff = finalViewHolder.pristatus.getText().toString();
                        String staffid = staff.substring(1, 2);
                        Intent i = new Intent(SuperviourLeaveManagement.this, SuperviourLeaveStatus.class);
                        i.putExtra("id", id);
                        i.putExtra("sid", staffid);
                        startActivity(i);
                    }
                }
            });

            return rowView;
        }
    } 
    
}

package com.school.stanthony;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class LeaveCancelled extends Fragment {
    Connection conn;
    String ConnectionResult = "";
    Boolean isSuccess,success;
    ListView listView;
    ResultSet rs;
    PreparedStatement stmt;
    String Form1,getstaffid;
    ImageView imageView;
    ArrayList<ClassListItems25> arraylist;
    ArrayList<ClassListItems25> itemArrayList;
    ConnectionHelper connectionClass;
    MyAppAdapter myAppAdapter;
    SharedPreferences sharedPreferences;

    @Override
       public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_leave_cancelled, container, false);
        sharedPreferences = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);

        listView=view.findViewById(R.id.list);
        imageView=view.findViewById(R.id.image);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems25>();

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
                    String query = "select staff_id from tbl_hrstaffnew where staffuser='"+Form1+"'";
                    stmt = conn.prepareStatement(query);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
                        getstaffid = rs.getString("staff_id");
                    }
                    ConnectionResult = "Successful";
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
                    String query = "select requestfrom+' - '+requesttill 'leavedate',isactive,leavetype,convert(varchar(10),createdon,103)'date',days,reason \n" +
                            "from tbl_hrcancelrequest where createdby='"+getstaffid+"'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems25(rs.getString("date"),rs.getString("isactive"),rs.getString("leavetype"),rs.getString("leavedate"),rs.getString("days"),rs.getString("reason")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList,getContext());
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
            TextView date,status,type,leavedate,days,reason,view;
        }

        public List<ClassListItems25> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems25> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems25>();
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
                rowView = inflater.inflate(R.layout.leavecancel, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.reason = rowView.findViewById(R.id.reason);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.days = rowView.findViewById(R.id.days);
                viewHolder.status = rowView.findViewById(R.id.status);
                viewHolder.type = rowView.findViewById(R.id.type);
                viewHolder.leavedate = rowView.findViewById(R.id.leavedate);
                viewHolder.view = rowView.findViewById(R.id.view);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.days.setText(parkingList.get(position).getDays()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.status.setText(parkingList.get(position).getStatus()+"");
            viewHolder.reason.setText(parkingList.get(position).getReason()+"");
            viewHolder.type.setText(parkingList.get(position).getType()+"");
            viewHolder.leavedate.setText(parkingList.get(position).getLeavedate()+"");

                String day=viewHolder.days.getText().toString();
                if(day.equals("1")){
                    viewHolder.days.setText(day+" Day");
                }else {
                    viewHolder.days.setText(day+" Days");
                }

                if(viewHolder.status.getText().toString().equals("1")){
                    viewHolder.status.setText("Pending");
                }else {
                    viewHolder.status.setText("Approved");
                }


            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage(finalViewHolder.reason.getText().toString());
                    builder.setCancelable(true);
                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
            });

            return rowView;
        }
    }

}
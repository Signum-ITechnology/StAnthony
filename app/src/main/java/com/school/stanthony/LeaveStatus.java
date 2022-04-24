package com.school.stanthony;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class LeaveStatus extends Fragment {

    Boolean success;
    ListView listView;
    String Form1;
    ImageView imageView;
    ArrayList<ClassListItems24> arraylist;
    ArrayList<ClassListItems24> itemArrayList;
    ConnectionHelper connectionClass;
    MyAppAdapter myAppAdapter;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_leave_status, container, false);
        
        sharedPreferences = this.getActivity().getSharedPreferences("teacherref", Context.MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);
        
        listView=view.findViewById(R.id.list);
        imageView=view.findViewById(R.id.image);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems24>();

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
                    String query = "select id,convert(varchar(10),createdon,103)'date',hodapprovoed,principalapprovoed,leavetype,leavefrom+' - '+leavetill 'leavedate' \n" +
                            "from tbl_hrappliedleave where staffid=(select top 1 staff_id from tbl_hrstaffnew where staffuser='"+Form1+"' \n" +
                            "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"'))\n" +
                            "and Acadmic_Year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems24(rs.getString("id"),rs.getString("date"),rs.getString("hodapprovoed"),rs.getString("principalapprovoed"),rs.getString("leavetype"),rs.getString("leavedate")));
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
            TextView id,date,status,hodstatus,pristatus,type,leavedate,cancel,view;
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

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View rowView = convertView;
            ViewHolder viewHolder= null;

            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.leavestatus, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.id = rowView.findViewById(R.id.id);
                viewHolder.hodstatus = rowView.findViewById(R.id.hodstatus);
                viewHolder.pristatus = rowView.findViewById(R.id.pristatus);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.status = rowView.findViewById(R.id.status);
                viewHolder.type = rowView.findViewById(R.id.type);
                viewHolder.leavedate = rowView.findViewById(R.id.leavedate);
                viewHolder.cancel = rowView.findViewById(R.id.cancel);
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

            if(parkingList.get(position).getPristatus().equals("0")){
                viewHolder.status.setText("Principal Approved");
                viewHolder.cancel.setEnabled(false);
                viewHolder.cancel.setAlpha(0.5f);
            }else if(parkingList.get(position).getPristatus().equals("2")){
                viewHolder.status.setText("Principal Rejected");
                viewHolder.cancel.setEnabled(false);
                viewHolder.cancel.setAlpha(0.5f);
                viewHolder.status.setTextColor(Color.RED);
            }else if(parkingList.get(position).getHodstatus().equals("1")){
                viewHolder.status.setText("Pending");
            }else if(parkingList.get(position).getHodstatus().equals("0")){
                viewHolder.status.setText("Superviour Approved");
                viewHolder.cancel.setEnabled(false);
                viewHolder.cancel.setAlpha(0.5f);
            }else if(parkingList.get(position).getHodstatus().equals("2")){
                viewHolder.status.setText("Superviour Rejected");
                viewHolder.cancel.setEnabled(false);
                viewHolder.cancel.setAlpha(0.5f);
                viewHolder.status.setTextColor(Color.RED);
            }


            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id=finalViewHolder.id.getText().toString();
                    Intent i=new Intent(getContext(),LeaveStatus2.class);
                    i.putExtra("id",id);
                    startActivity(i);
                }
            });

            viewHolder.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                    builder.setMessage("Are You Sure You Want To Request For Cancellation");
                    builder.setCancelable(true);
                    builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                               Intent ii=new Intent(getContext(),LeaveCancel.class);
                               String id=finalViewHolder.id.getText().toString();
                               ii.putExtra("id",id);
                               startActivity(ii);
                        }
                    });

                    builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
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
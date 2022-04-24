package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ShowLectureEntry extends Fragment {
    
    ArrayList<ClassListItems22> arraylist;
    ArrayList<ClassListItems22> itemArrayList;
    ConnectionHelper connectionClass;
    Boolean success,isSuccess;
    MyAppAdapter myAppAdapter;
    ListView listView;
    ImageView imageView;
    SharedPreferences sharedPref;
    String Form1,ConnectionResult,getid,checkstatus;
    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(getContext());
            progress.setTitle("Activating Meeting");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String commands = "update LiveLecture set isactive='1' where lectid='"+getid+"'";
                            PreparedStatement preStmt = conn.prepareStatement(commands);
                            preStmt.executeUpdate();
                        } }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }
                    progress.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Meeting Activated");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 1000);
        }};


    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(getContext());
            progress.setTitle("Deactivating Meeting");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String commands = "update LiveLecture set isactive='0' where lectid='"+getid+"'";
                            PreparedStatement preStmt = conn.prepareStatement(commands);
                            preStmt.executeUpdate();
                        } }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }
                    progress.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Meeting Deactivated");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 1000);
        }};
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_updatevideo, container, false);
        listView =view.findViewById(R.id.listview);
        imageView = view.findViewById(R.id.image);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems22>();

        URL serverUrl;

        try{
            serverUrl = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverUrl)
                            .setWelcomePageEnabled(false)
                            .setFeatureFlag("invite.enabled",false)
                         //   .setFeatureFlag("live-streaming.enabled",false)
                            .setFeatureFlag("overflow-menu.enabled",false)
                        //   .setFeatureFlag("chat.enabled",false)
                        //    .setFeatureFlag("video-share.enabled",false)
                            .setFeatureFlag("pip.enabled",false)
                       //     .setFeatureFlag("raise-hand.enabled",false)
                            .setFeatureFlag("server-url-change.enabled",false)
                            .setFeatureFlag("disableRemoteMute",true)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

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
                    String query = "select lectid,convert(varchar(10),date,103)'date',time,section+'/'+std+'/'+div 'std',subjectname,details,joincode,joinpassword,isactive\n" +
                            "from LiveLecture where academic=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and\n" +
                            "createdby=(SELECT Staff_ID from tbl_HRStaffnew where StaffUser='"+Form1+"'\n" +
                            "and academic=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')) and Convert(varchar,date,103)=Convert(varchar,getDate(),103)";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems22(rs.getString("lectid"),rs.getString("date"),rs.getString("time"),rs.getString("std"),rs.getString("subjectname"),rs.getString("details"),rs.getString("joincode"),rs.getString("joinpassword")
                                        ,rs.getString("isactive")));
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
            TextView id,date,time,std,subject,desc,code,pass,active,join;
            Switch status;
        }

        public List<ClassListItems22> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems22> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems22>();
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
                rowView = inflater.inflate(R.layout.livelecture, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.id = rowView.findViewById(R.id.id);
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.time = rowView.findViewById(R.id.time);
                viewHolder.std = rowView.findViewById(R.id.std);
                viewHolder.subject = rowView.findViewById(R.id.subject);
                viewHolder.desc = rowView.findViewById(R.id.desc);
                viewHolder.code = rowView.findViewById(R.id.code);
                viewHolder.pass = rowView.findViewById(R.id.pass);
                viewHolder.active = rowView.findViewById(R.id.active);
                viewHolder.status = rowView.findViewById(R.id.status);
                viewHolder.join = rowView.findViewById(R.id.join);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.id.setText(parkingList.get(position).getId()+"");
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.time.setText(parkingList.get(position).getTime()+"");
            viewHolder.std.setText(parkingList.get(position).getStd()+"");
            viewHolder.subject.setText(parkingList.get(position).getSubject()+"");
            viewHolder.desc.setText(parkingList.get(position).getDesc()+"");
            viewHolder.code.setText(parkingList.get(position).getCode()+"");
            viewHolder.pass.setText(parkingList.get(position).getPass()+"");
            viewHolder.active.setText(parkingList.get(position).getActive()+"");

            if(parkingList.get(position).getDesc().equals("")){
                viewHolder.desc.setVisibility(View.GONE);
            }else{
                viewHolder.desc.setVisibility(View.VISIBLE);
            }

            if(parkingList.get(position).getActive().equals("1")){
                viewHolder.status.setChecked(true);
                viewHolder.active.setText("Active");
                viewHolder.active.setTextColor(Color.GREEN);
            }else{
                viewHolder.status.setChecked(false);
                viewHolder.active.setText("Inactive");
                viewHolder.active.setTextColor(Color.RED);
            }

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getid=parkingList.get(position).getId();
                    if(finalViewHolder.status.isChecked()){
                        mainHandler1.post(myRunnable1);
                        finalViewHolder.active.setText("Active");
                        finalViewHolder.active.setTextColor(Color.GREEN);
                    }else {
                        mainHandler2.post(myRunnable2);
                        finalViewHolder.active.setText("Inactive");
                        finalViewHolder.active.setTextColor(Color.RED);
                    }
                }
            });

            viewHolder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id=parkingList.get(position).getId();

                    try {
                        ConnectionHelper conStr1 = new ConnectionHelper();
                        conn = conStr1.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select isactive from livelecture where lectid='"+id+"'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            while (rs.next()) {
                                checkstatus = rs.getString("isactive");
                            }
                            ConnectionResult = "Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    } catch (android.database.SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    if(checkstatus.equals("1")){
                        String code=parkingList.get(position).getCode();

                        JitsiMeetConferenceOptions defaultOptions =
                                new JitsiMeetConferenceOptions.Builder()
                                        .setRoom(code)
                                        .setWelcomePageEnabled(false)
                                        .build();
                        JitsiMeetActivity.launch(getContext(), defaultOptions);

                    }else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Please Activate The Lecture First.");
                        builder.setCancelable(false);
                        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
            });

            return rowView;
        }
    }
    
}

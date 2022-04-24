package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class PrincipalShowLecture extends Fragment {

    ArrayList<ClassListItems23> arraylist;
    ArrayList<ClassListItems23> itemArrayList;
    ConnectionHelper connectionClass;
    Boolean success,isSuccess;
    MyAppAdapter myAppAdapter;
    ListView listView;
    ImageView imageView;
    SharedPreferences sharedPref;
    String Form1,ConnectionResult,getid;
    Connection conn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.studentshowalllectures, container, false);
        listView =view.findViewById(R.id.listview);
        imageView = view.findViewById(R.id.image);
        connectionClass = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems23>();

        sharedPref = this.getActivity().getSharedPreferences("prinref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Principalcode", null);

        URL serverUrl;

        try{
            serverUrl = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions=
                    new JitsiMeetConferenceOptions.Builder()
                            .setServerURL(serverUrl)
                            .setWelcomePageEnabled(false)
                            .setFeatureFlag("meeting-password.enabled",false)
                            .setFeatureFlag("invite.enabled",false)
                            .setFeatureFlag("live-streaming.enabled",false)
                            .setFeatureFlag("overflow-menu.enabled",false)
                        //    .setFeatureFlag("chat.enabled",false)
                            .setFeatureFlag("video-share.enabled",false)
                        //    .setFeatureFlag("video-mute.enabled",false)
                            .setFeatureFlag("pip.enabled",false)
                            .setFeatureFlag("raise-hand.enabled",false)
                       //     .setFeatureFlag("audio-mute.enabled",true)
                            .setFeatureFlag("server-url-change.enabled",false)
                            .build();
            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

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
                    String query = "select convert(varchar(10),date,103)'date',time,subjectname,details,joincode,joinpassword,isactive\n" +
                            "from LiveLecture where academic=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                            "and Convert(varchar,date,103)=Convert(varchar,getDate(),103)";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems23(rs.getString("date"),rs.getString("time"),rs.getString("subjectname"),rs.getString("details"),rs.getString("joincode"),rs.getString("joinpassword"),rs.getString("isactive")));
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
            TextView date,time,subject,desc,code,pass,active,join;
        }

        public List<ClassListItems23> parkingList;
        public Context context;


        private MyAppAdapter(List<ClassListItems23> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems23>();
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
                rowView = inflater.inflate(R.layout.livelecture2, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.date = rowView.findViewById(R.id.date);
                viewHolder.time = rowView.findViewById(R.id.time);
                viewHolder.subject = rowView.findViewById(R.id.subject);
                viewHolder.desc = rowView.findViewById(R.id.desc);
                viewHolder.code = rowView.findViewById(R.id.code);
                viewHolder.pass = rowView.findViewById(R.id.pass);
                viewHolder.active = rowView.findViewById(R.id.active);
                viewHolder.join = rowView.findViewById(R.id.join);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.date.setText(parkingList.get(position).getDate()+"");
            viewHolder.time.setText(parkingList.get(position).getTime()+"");
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
                viewHolder.active.setText("Active");
                viewHolder.active.setTextColor(Color.GREEN);
            }else{
                viewHolder.active.setText("Inactive");
                viewHolder.active.setTextColor(Color.RED);
            }

            final ViewHolder finalViewHolder = viewHolder;
            finalViewHolder.join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String status=parkingList.get(position).getActive();
                    if(status.equals("1")){

                        String code=parkingList.get(position).getCode();

                        JitsiMeetConferenceOptions defaultOptions =
                                new JitsiMeetConferenceOptions.Builder()
                                        .setRoom(code)
                                        .setWelcomePageEnabled(false)
                                        .build();
                        JitsiMeetActivity.launch(getContext(), defaultOptions);
                    }else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Lecture Not Started Yet.");
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
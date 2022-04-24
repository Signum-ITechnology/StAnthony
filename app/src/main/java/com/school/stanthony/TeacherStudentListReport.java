package com.school.stanthony;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TeacherStudentListReport extends AppCompatActivity {

    ArrayList<ClassListItems> itemArrayList;
    MyAppAdapter myAppAdapter;
    ListView listView;
    boolean success = false;
    ConnectionHelper ConnectionHelper;
    String Form1,nameget,numberget,section,std,div;
    ArrayList<ClassListItems> arraylist;
    TextView textView;
    Dialog myDialog;
    private static final int Request = 1;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list_report);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPref.getString("Teachercode", null);
        myDialog = new Dialog(this);

        listView = findViewById(R.id.listView);
        textView=findViewById(R.id.show);
        ConnectionHelper = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems>();
        section=getIntent().getExtras().getString("section");
        std=getIntent().getExtras().getString("std");
        div=getIntent().getExtras().getString("div");

        textView.setText(section+" "+std+" "+div+" STUDENT LIST");
        // Calling Async Task
        SyncData orderData = new SyncData();
        orderData.execute("");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                numberget = itemArrayList.get(position). getContact();
                nameget = itemArrayList.get(position). getName();
                ShowPopup();
            }
        });

    }

    public void ShowPopup() {
        myDialog.setCancelable(false);
        TextView name,number,call,cancel;
        myDialog.setContentView(R.layout.custompopup1);
        name = myDialog.findViewById(R.id.name);
        number = myDialog.findViewById(R.id.number);
        call = myDialog.findViewById(R.id.call);
        cancel = myDialog.findViewById(R.id.cancel);

        name.setText(nameget);
        number.setText(numberget);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makephonecall();
            }
        });

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void makephonecall() {
        if (ContextCompat.checkSelfPermission(TeacherStudentListReport.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(TeacherStudentListReport.this, new String[]{Manifest.permission.CALL_PHONE}, Request);
        }
        else{
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numberget)));
        }
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(TeacherStudentListReport.this, "Student List",
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
                    String query = "select ROW_NUMBER() OVER (ORDER BY division)  AS Row, a.Class_Name+'/'+a.Division 'class',\n" +
                            "a.Roll_Number,a.Surname+' '+a.name+' '+a.father_name as fullname,a.Applicant_Type,\n" +
                            "case when Self_Moblie_Number!=0 then Self_Moblie_Number\n" +
                            "when Self_Moblie_Number=0 and Father_Mobile_Number=0 then Mother_Mobile_Number\n" +
                            "when Self_Moblie_Number=0 then Father_Mobile_Number \n" +
                            "when Father_Mobile_Number=0 then Mother_Mobile_Number end as Contact_no,\n" +
                            "'http://stanthony.edusofterp.co.in/'+replace(replace(a.imagepath,' ','%20'),'..','') url\n" +
                            "from tbladmissionfeemaster a,tblstudentmaster b where a.Batch_Code ='"+section+"' and a.Class_Name='"+std+"' and a.division='"+div+"'\n" +
                            "and a.iscancelled !='1' and a.acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and \n" +
                            "a.Applicant_type=b.student_code and applicant_type!='new' order by a.Roll_Number";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems(rs.getString("fullname"),rs.getString("url"),rs.getString("class"),rs.getString("roll_number"),rs.getString("applicant_type"),rs.getString("contact_no")));
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
            Toast.makeText(TeacherStudentListReport.this, msg + "", Toast.LENGTH_LONG).show();
            if (success == false)
            {
            }
            else {
                try {
                    myAppAdapter = new MyAppAdapter(itemArrayList, TeacherStudentListReport.this);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(myAppAdapter);
                } catch (Exception ex)
                {

                }

            }
        }
    }

    public class MyAppAdapter extends BaseAdapter {
        public class ViewHolder
        {
            TextView textName,std,roll,code,contact;
            ImageView imageView;
        }

        public List<ClassListItems> parkingList;

        public Context context;


        private MyAppAdapter(List<ClassListItems> apps, Context context)
        {
            this.parkingList = apps;
            this.context = context;
            arraylist = new ArrayList<ClassListItems>();
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
            ViewHolder viewHolder;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.list_content, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textName = rowView.findViewById(R.id.textName);
                viewHolder.imageView = rowView.findViewById(R.id.imageView);
                viewHolder.std=rowView.findViewById(R.id.std);
                viewHolder.roll=rowView.findViewById(R.id.roll);
                viewHolder.code=rowView.findViewById(R.id.code);
                viewHolder.contact=rowView.findViewById(R.id.contact);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.textName.setText(parkingList.get(position).getName()+"");
            viewHolder.std.setText(parkingList.get(position).getStd()+"  ");
            viewHolder.roll.setText(parkingList.get(position).getRoll()+"  ");
            viewHolder.code.setText(parkingList.get(position).getCode()+"");
            viewHolder.contact.setText(parkingList.get(position).getContact()+"");
            Picasso.with(context).load(parkingList.get(position).getImg()).into(viewHolder.imageView);

            return rowView;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Request){
            if(grantResults.length >=0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                makephonecall();
            }
        }
    }

}

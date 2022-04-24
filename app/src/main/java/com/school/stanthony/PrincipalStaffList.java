package com.school.stanthony;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PrincipalStaffList extends AppCompatActivity {

    Spinner s1;
    Button btn;
    Connection conn;
    String Form1,post,ConnectionResult,numberget,nameget;
    ResultSet rs,rt;
    PreparedStatement stmt;
    Boolean isSuccess;
    ArrayList<ClassListItems4> arraylist;
    ArrayList<ClassListItems4> itemArrayList;
    ConnectionHelper ConnectionHelper;
    ListView listView;
   MyAppAdapter myAppAdapter;
    boolean success = false;
    Dialog myDialog;
    private static final int Request = 1;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myDialog = new Dialog(this);

        sharedPref = getSharedPreferences("prinref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Principalcode", null);

        s1 = findViewById(R.id.s1);
        btn = findViewById(R.id.btn);
        listView = findViewById(R.id.list);
        ConnectionHelper = new ConnectionHelper();
        itemArrayList = new ArrayList<ClassListItems4>();

        /////////////////// For Post
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select distinct 'YYY' department from tbl_hrstaffnew where \n" +
                        "acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n" +
                        "union all\n" +
                        "select distinct department from tbl_hrstaffnew where \n" +
                        "acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    String fullname = rs.getString("department");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(this,R.layout.spinner11, data2);
                s1.setAdapter(NoCoreAdapter2);
                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post=s1.getSelectedItem().toString();
                if(post.equals("YYY")){
                    itemArrayList.clear();
                    SyncData orderData = new SyncData();
                    orderData.execute("");
                }else{
                    itemArrayList.clear();
                    SyncData1 orderData1 = new SyncData1();
                    orderData1.execute("");
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                numberget = itemArrayList.get(position). getContact();
                nameget = itemArrayList.get(position). getName();
                ShowPopup();
            }
        });
    }

    private class SyncData extends AsyncTask<String, String, String> {
        String msg = "Internet/DB_Credentials/Windows_FireWall_TurnOn Error, See Android Monitor in the bottom For details!";
        ProgressDialog progress;

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(PrincipalStaffList.this, "Staff List",
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
                    String query = "select name,'http://stanthony.edusofterp.co.in'+replace(replace(imagepath,' ','%20'),'..','') url,department,designation,contactno\n" +
                            "from tbl_hrstaffnew where acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems4(rs.getString("url"),rs.getString("name"),rs.getString("department"),rs.getString("designation"),rs.getString("contactno")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, PrincipalStaffList.this);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    listView.setAdapter(myAppAdapter);
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
            progress = ProgressDialog.show(PrincipalStaffList.this, "Staff List",
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
                    String query = "select name,'http://stanthony.edusofterp.co.in'+replace(replace(imagepath,' ','%20'),'..','') url,department,designation,contactno\n" +
                            "from tbl_hrstaffnew where acadmic_year=(select top 1 selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') and department='"+post+"'";
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs != null)
                    {
                        while (rs.next())
                        {
                            try {
                                itemArrayList.add(new ClassListItems4(rs.getString("url"),rs.getString("name"),rs.getString("department"),rs.getString("designation"),rs.getString("contactno")));
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
                    myAppAdapter = new MyAppAdapter(itemArrayList, PrincipalStaffList.this);
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
            ViewHolder viewHolder;
            if (rowView == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.stafflist, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.name = rowView.findViewById(R.id.textName);
                viewHolder.imageView = rowView.findViewById(R.id.imageView);
                viewHolder.department=rowView.findViewById(R.id.department);
                viewHolder.designation=rowView.findViewById(R.id.designation);
                viewHolder.contact=rowView.findViewById(R.id.contact);
                rowView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // here setting up names and images
            viewHolder.name.setText(parkingList.get(position).getName()+"");
            viewHolder.department.setText(parkingList.get(position).getDepartment()+"  ");
            viewHolder.designation.setText(parkingList.get(position).getDesignation()+"  ");
            viewHolder.contact.setText(parkingList.get(position).getContact()+"");
            Picasso.with(context).load(parkingList.get(position).getUrl()).error(R.drawable.logo).into(viewHolder.imageView);

            return rowView;
        }
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
        if (ContextCompat.checkSelfPermission(PrincipalStaffList.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(PrincipalStaffList.this, new String[]{Manifest.permission.CALL_PHONE}, Request);
        }
        else{
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numberget)));
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
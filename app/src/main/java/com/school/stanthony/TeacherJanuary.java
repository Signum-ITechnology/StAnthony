package com.school.stanthony;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TeacherJanuary extends AppCompatActivity {

    Connection conn;
    String ConnectionResult ="";
    Boolean isSuccess;
    ListView listView,listView1;
    SimpleAdapter adapter;
    SharedPreferences sharedPreferences;
    String Form1,contentdata,month;
    ResultSet rs;
    PreparedStatement stmt;
    View clickSource;
    View touchSource;
    int offset = 0;
    TextView present,absent,holiday,all;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_january);

        listView= findViewById(R.id.list1);
        listView1=findViewById(R.id.list2);
        present=findViewById(R.id.present);
        absent=findViewById(R.id.absent);
        holiday=findViewById(R.id.holiday);
        all=findViewById(R.id.all);
        LinearLayout linearLayout=findViewById(R.id.linear);

        sharedPreferences = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Teachercode", null);
        month=getIntent().getExtras().getString("month");


        try {
            ConnectionHelper conStr1 = new ConnectionHelper();
            conn = conStr1.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select CONVERT(varchar(10),AttendanceDate,103)AttendanceDate,\n" +
                        "case when Present='p' then 'Present'when Present='a' then 'Absent' when Present='u' then 'Uninstructional'\n" +
                        "when Present='h' then 'Holiday' end as Present from tbl_HRAttendance \n" +
                        "where AcadmicYear=\n" +
                        "(select CompanyCode from tblcompanymaster where isselected=1) \n" +
                        "and StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+Form1+"') and month='"+month+"' order by AttendanceDate";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<>();
                ArrayList<String> data22 = new ArrayList<>();
                while (rs.next()) {
                    String fullname = rs.getString("AttendanceDate");
                    data2.add(fullname);

                    String present = rs.getString("Present");
                    data22.add(present);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(TeacherJanuary.this,R.layout.spinnerdate,data2);

                String[] array22 = data22.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter22 = new ArrayAdapter(TeacherJanuary.this,R.layout.spinner,data22)
                {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent)
                    {
                        View itemView = super.getView(position, convertView, parent);
                        TextView textView = itemView.findViewById(R.id.text);
                        contentdata=textView.getText().toString();
                        if (contentdata.equals("Present"))
                        {
                            textView.setTextColor(Color.BLACK);
                        }

                        if (contentdata.equals("Absent"))
                        {
                            textView.setTextColor(Color.WHITE);
                        }

                        if (contentdata.equals("Holiday"))
                        {
                            textView.setTextColor(Color.WHITE);
                        }

                        if (contentdata.equals("Uninstructional"))
                        {
                            textView.setTextColor(Color.BLACK);
                        }


                        if (getItem(position).equals("Absent"))
                            itemView.setBackgroundColor(Color.RED);

                        if (getItem(position).equals("Present"))
                            itemView.setBackgroundColor(Color.GREEN);

                        if (getItem(position).equals("Holiday"))
                            itemView.setBackgroundColor(Color.BLUE);

                        if (getItem(position).equals("Uninstructional"))
                            itemView.setBackgroundColor(Color.YELLOW);

                        return itemView;
                    }
                };

                if(NoCoreAdapter2.getCount()==0){
                    Toast.makeText(TeacherJanuary.this, "Sorry No Attendance To Show", Toast.LENGTH_LONG).show();
                    linearLayout.setVisibility(View.INVISIBLE);
                }

                listView.setAdapter(NoCoreAdapter2);
                listView1.setAdapter(NoCoreAdapter22);
                ConnectionResult = " Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        ////
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    listView1.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });

        ///
        listView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(touchSource == null)
                    touchSource = v;

                if(v == touchSource) {
                    listView.dispatchTouchEvent(event);
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        clickSource = v;
                        touchSource = null;
                    }
                }
                return false;
            }
        });

        //
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent == clickSource) {
                    // Do something with the ListView was clicked
                }
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent == clickSource) {
                    // Do something with the ListView was clicked

                }
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                    listView1.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        listView1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(view == clickSource)
                    listView.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop() + offset);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}
        });

        ConnectionHelper conStr = new ConnectionHelper();
        conn = conStr.connectionclasss();
        if (conn == null) {
            Snackbar snackbar=Snackbar.make(findViewById(R.id.id),"No Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("RETRY",new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(getIntent());
                    //   mainHandler.post(myRunnable);
                }
            });
            snackbar.show();
        }
        //  mainHandler.post(myRunnable);

        present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select CONVERT(varchar(10),AttendanceDate,103)AttendanceDate,\n" +
                                "case when Present='p' then 'Present' end as Present from tbl_HRAttendance where AcadmicYear=\n" +
                                "(select CompanyCode from tblcompanymaster where isselected=1) \n" +
                                "and StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+ Form1+"') \n" +
                                "and month='"+month+"' and Present='p'order by AttendanceDate";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        ArrayList<String> data22 = new ArrayList<>();
                        while (rs.next()) {
                            String fullname = rs.getString("AttendanceDate");
                            data2.add(fullname);

                            String present = rs.getString("Present");
                            data22.add(present);
                        }
                        if(data2.size()==0){
                            Toast.makeText(getApplicationContext(), "Sorry No Present Dates To Show", Toast.LENGTH_LONG).show();
                        }
                        else {

                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(TeacherJanuary.this, R.layout.spinnerdate, data2);

                            String[] array22 = data22.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter22 = new ArrayAdapter(TeacherJanuary.this, R.layout.spinner, data22) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View itemView = super.getView(position, convertView, parent);
                                    TextView textView = itemView.findViewById(R.id.text);
                                    contentdata = textView.getText().toString();
                                    if (contentdata.equals("Present")) {
                                        textView.setTextColor(Color.BLACK);
                                    }

                                    if (getItem(position).equals("Present"))
                                        itemView.setBackgroundColor(Color.GREEN);

                                    return itemView;
                                }
                            };

                            listView.setAdapter(NoCoreAdapter2);
                            listView1.setAdapter(NoCoreAdapter22);
                            ConnectionResult = " Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        ////// For Absent

        absent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select CONVERT(varchar(10),AttendanceDate,103)AttendanceDate,\n" +
                                "case when Present='a' then 'Absent' end as Present from tbl_HRAttendance where AcadmicYear=\n" +
                                "(select CompanyCode from tblcompanymaster where isselected=1) \n" +
                                "and StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+Form1+"') \n" +
                                "and month='"+month+"' and Present='a'order by AttendanceDate";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        ArrayList<String> data22 = new ArrayList<>();
                        while (rs.next()) {
                            String fullname = rs.getString("AttendanceDate");
                            data2.add(fullname);

                            String present = rs.getString("Present");
                            data22.add(present);
                        }

                        if(data2.size()==0){
                            Toast.makeText(getApplicationContext(), "Sorry No Absent Dates To Show", Toast.LENGTH_LONG).show();
                        }
                        else {

                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(TeacherJanuary.this, R.layout.spinnerdate, data2);

                            String[] array22 = data22.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter22 = new ArrayAdapter(TeacherJanuary.this, R.layout.spinner, data22) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View itemView = super.getView(position, convertView, parent);

                                    if (getItem(position).equals("Absent"))
                                        itemView.setBackgroundColor(Color.RED);

                                    return itemView;
                                }
                            };

                            listView.setAdapter(NoCoreAdapter2);
                            listView1.setAdapter(NoCoreAdapter22);
                            ConnectionResult = " Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        /////// For Holiday

        holiday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select CONVERT(varchar(10),AttendanceDate,103)AttendanceDate,\n" +
                                "case when Present='h' then 'Holiday' end as Present from tbl_HRAttendance where AcadmicYear=\n" +
                                "(select CompanyCode from tblcompanymaster where isselected=1) \n" +
                                "and StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+Form1+"') \n" +
                                "and month='"+month+"' and Present='h'order by AttendanceDate";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        ArrayList<String> data22 = new ArrayList<>();
                        while (rs.next()) {
                            String fullname = rs.getString("AttendanceDate");
                            data2.add(fullname);

                            String present = rs.getString("Present");
                            data22.add(present);
                        }

                        if(data2.size()==0){
                            Toast.makeText(getApplicationContext(), "Sorry No Holidays Dates To Show", Toast.LENGTH_LONG).show();
                        }
                        else {

                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(TeacherJanuary.this, R.layout.spinnerdate, data2);

                            String[] array22 = data22.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter22 = new ArrayAdapter(TeacherJanuary.this, R.layout.spinner, data22) {
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    View itemView = super.getView(position, convertView, parent);

                                    if (getItem(position).equals("Holiday"))
                                        itemView.setBackgroundColor(Color.BLUE);

                                    return itemView;
                                }
                            };

                            listView.setAdapter(NoCoreAdapter2);
                            listView1.setAdapter(NoCoreAdapter22);
                            ConnectionResult = " Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    ConnectionHelper conStr1 = new ConnectionHelper();
                    conn = conStr1.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String query = "select CONVERT(varchar(10),AttendanceDate,103)AttendanceDate,\n" +
                                "case when Present='p' then 'Present'when Present='a' then 'Absent' when Present='u' then 'Uninstructional'\n" +
                                "when Present='h' then 'Holiday' end as Present from tbl_HRAttendance \n" +
                                "where AcadmicYear=\n" +
                                "(select CompanyCode from tblcompanymaster where isselected=1) \n" +
                                "and StaffAttendanceId=(SELECT Staff_ID from tbl_HRStaff where StaffUser='"+Form1+"') and month='"+month+"' order by AttendanceDate";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        ArrayList<String> data22 = new ArrayList<>();
                        while (rs.next()) {
                            String fullname = rs.getString("AttendanceDate");
                            data2.add(fullname);

                            String present = rs.getString("Present");
                            data22.add(present);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(TeacherJanuary.this,R.layout.spinnerdate,data2);

                        String[] array22 = data22.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter22 = new ArrayAdapter(TeacherJanuary.this,R.layout.spinner,data22)
                        {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent)
                            {
                                View itemView = super.getView(position, convertView, parent);
                                TextView textView = itemView.findViewById(R.id.text);
                                contentdata=textView.getText().toString();
                                if (contentdata.equals("Present"))
                                {
                                    textView.setTextColor(Color.BLACK);
                                }

                                if (contentdata.equals("Absent"))
                                {
                                    textView.setTextColor(Color.WHITE);
                                }

                                if (contentdata.equals("Holiday"))
                                {
                                    textView.setTextColor(Color.WHITE);
                                }

                                if (contentdata.equals("Uninstructional"))
                                {
                                    textView.setTextColor(Color.BLACK);
                                }

                                if (getItem(position).equals("Absent"))
                                    itemView.setBackgroundColor(Color.RED);

                                if (getItem(position).equals("Present"))
                                    itemView.setBackgroundColor(Color.GREEN);

                                if (getItem(position).equals("Holiday"))
                                    itemView.setBackgroundColor(Color.BLUE);

                                if (getItem(position).equals("Uninstructional"))
                                    itemView.setBackgroundColor(Color.YELLOW);

                                return itemView;
                            }
                        };

                        listView.setAdapter(NoCoreAdapter2);
                        listView1.setAdapter(NoCoreAdapter22);
                        ConnectionResult = " Successful";
                        isSuccess = true;
                        conn.close();
                    }
                } catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
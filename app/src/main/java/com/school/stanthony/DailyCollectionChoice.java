package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class DailyCollectionChoice extends AppCompatActivity {

    String ConnectionResult,Form1,startdate,enddate,section,std,div,daily,section1,cash,cheque,online;
    Boolean isSuccess;
    EditText start, end;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner s1, s2, s3;
    Button btn,btn2;
    TextView textView,stdtext,divtext;
    LinearLayout linearLayout;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_collection_choice);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        start = findViewById(R.id.date);
        end = findViewById(R.id.date1);
        s1 = findViewById(R.id.s1);
        s2 = findViewById(R.id.s2);
        s3 = findViewById(R.id.s3);
        textView = findViewById(R.id.cost);
        btn = findViewById(R.id.btn);
        btn2 = findViewById(R.id.btn2);
        linearLayout=findViewById(R.id.linear);
        stdtext=findViewById(R.id.std);
        divtext=findViewById(R.id.div);
        /////////////////////////////// start time

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(DailyCollectionChoice.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                start.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                                getmoonth = monthOfYear + 1;
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        /////////////////////////// End time

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(DailyCollectionChoice.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                end.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
                                getmoonth = monthOfYear + 1;
                            }

                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select distinct('YYY') batchcode from tblclassmaster where \n" +
                        "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"')\n" +
                        "union all             \n" +
                        "select batchCode from tblbatchcodemaster where \n" +
                        "acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
                    String fullname = rs.getString("batchcode");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(DailyCollectionChoice.this,R.layout.spinner11, data2);
                s1.setAdapter(NoCoreAdapter2);
                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        /////////  Bydefault StartDate

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null)
            {
                ConnectionResult="NO INTERNET";
            }
            else
            {
                String query = "select CONVERT(varchar(10),getdate(),110) sysdate ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    startdate = rs.getString("sysdate");

                    start.setText(startdate);
                    end.setText(startdate);
                }
                ConnectionResult = " Successful";
                isSuccess=true;
                conn.close();
            }
        }
        catch (android.database.SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section=s1.getSelectedItem().toString();
                if(section.equals("YYY")){
                    s2.setVisibility(View.INVISIBLE);
                    s3.setVisibility(View.INVISIBLE);
                }
                else {
                    s2.setVisibility(View.VISIBLE);
                    //       stdtext.setVisibility(View.VISIBLE);
                    /////////////////// For Class
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select distinct(class_name) from tblclassmaster where \n" +
                                    "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and \n" +
                                    "batchcode='" + section + "'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<String>();
                            while (rs.next()) {
                                String fullname = rs.getString("class_name");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(DailyCollectionChoice.this, R.layout.spinner11, data2);
                            s2.setAdapter(NoCoreAdapter2);
                            ConnectionResult = " Successful";
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section1=s2.getSelectedItem().toString();
                s3.setVisibility(View.VISIBLE);
                //       divtext.setVisibility(View.VISIBLE);

                /////////////////// For Div
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null)
                    {
                        ConnectionResult="NO INTERNET";
                    }
                    else
                    {
                        String query = "select distinct(division) from tblclassmaster where \n" +
                                "Acadmic_Year=(select selectedaca from tblusermaster where username='"+Form1+"') and \n" +
                                "class_name='"+section1+"'";
                        stmt = conn.prepareStatement(query);
                        rs = stmt.executeQuery();
                        ArrayList<String> data2 = new ArrayList<>();
                        while (rs.next())
                        {
                            String fullname = rs.getString("division");
                            data2.add(fullname);
                        }
                        String[] array2 = data2.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(DailyCollectionChoice.this,R.layout.spinner11, data2);
                        s3.setAdapter(NoCoreAdapter2);
                        //       NoCoreAdapter2.add("YYY");
                        ConnectionResult = " Successful";
                        isSuccess=true;
                        conn.close();
                    }
                }
                catch (android.database.SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (daily.equals("")) {
                    Toast.makeText(DailyCollectionChoice.this, "No Record Found", Toast.LENGTH_LONG).show();
                }
                else {

                    final ProgressDialog progress = new ProgressDialog(DailyCollectionChoice.this);
                    progress.setTitle("Calculating Total");
                    progress.setMessage("Please Wait a Moment");
                    progress.setCancelable(false);
                    progress.show();

                    Runnable progressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            progress.cancel();
                        }
                    };
                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable,5000);

                    startdate = start.getText().toString();
                    enddate = end.getText().toString();
                    try {
                        std = s2.getSelectedItem().toString();
                        div = s3.getSelectedItem().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent i = new Intent(getApplicationContext(), DailyCollection.class);
                    i.putExtra("start", startdate);
                    i.putExtra("end", enddate);
                    i.putExtra("section", section);
                    i.putExtra("std", std);
                    i.putExtra("div", div);
                    i.putExtra("cash", cash);
                    i.putExtra("cheque", cheque);
                    i.putExtra("online", online);
                    startActivity(i);
                }
            }
        });

        /////

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(start.getText())) {
                    Toast.makeText(DailyCollectionChoice.this, "Please Select Start Date", Toast.LENGTH_LONG).show();
                } else  if (TextUtils.isEmpty(end.getText())) {
                    Toast.makeText(DailyCollectionChoice.this, "Please Select End Date", Toast.LENGTH_LONG).show();
                }else if (s1.getSelectedItem().toString().trim().equalsIgnoreCase("Select Section")) {
                    Toast.makeText(DailyCollectionChoice.this, "Select Section", Toast.LENGTH_LONG).show();
                }else {
                    final ProgressDialog progress = new ProgressDialog(DailyCollectionChoice.this);
                    progress.setTitle("Calculating Total");
                    progress.setMessage("Please Wait a Moment");
                    progress.setCancelable(false);
                    progress.show();

                    Runnable progressRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (s1.getSelectedItem().toString().equals("YYY")) {
                                section=s1.getSelectedItem().toString();
                                startdate = start.getText().toString();
                                enddate = end.getText().toString();


                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "select \n" +
                                                "            distinct(select isnull((SUM(Fee_paid)),0) \n" +
                                                "            from tbladmissionfeemaster where \n" +
                                                "             DATEDIFF(d,convert(varchar,created_on,101),'"+startdate+"')<=0 \n" +
                                                "            and DATEDIFF(d,convert(varchar,created_on,101), '"+enddate+"')>=0) \n" +
                                                "            + \n" +
                                                "            (select isnull((select SUM(receipt_amount) from tblfeemaster where \n" +
                                                "              DATEDIFF(d,convert(varchar,created_on,101),'"+startdate+"')<=0 \n" +
                                                "            and DATEDIFF(d,convert(varchar,created_on,101), '"+enddate+"')>=0 ),0)) daily";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();

                                        while (rs.next()) {
                                            daily = rs.getString("daily");
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

                                /////////////For Cash Cal

                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "select \n" +
                                                "distinct isnull(((select isnull((SUM(Fee_paid)),0) \n" +
                                                "from tbladmissionfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'"+startdate+"')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '"+enddate+"')>=0 \n" +
                                                "and paymentmode=1) \n" +
                                                "+ \n" +
                                                "(select isnull((SUM(receipt_amount)),0) from tblfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'"+startdate+"')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '"+enddate+"')>=0 \n" +
                                                "and paymentmode=1)),0) as daily \n" +
                                                "from tbladmissionfeemaster ";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();

                                        while (rs.next()) {
                                            cash = rs.getString("daily");
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

                                /////////////For Cheque Cal

                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "select \n" +
                                                "distinct isnull(((select isnull((SUM(Fee_paid)),0) \n" +
                                                "from tbladmissionfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'"+startdate+"')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '"+enddate+"')>=0 \n" +
                                                "and paymentmode=2) \n" +
                                                "+ \n" +
                                                "(select isnull((SUM(receipt_amount)),0) from tblfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'"+startdate+"')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '"+enddate+"')>=0 \n" +
                                                "and paymentmode=2)),0) as daily \n" +
                                                "from tbladmissionfeemaster";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();

                                        while (rs.next()) {
                                            cheque = rs.getString("daily");
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

                                /////////////For online Cal

                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "select \n" +
                                                "distinct isnull(((select isnull((SUM(Fee_paid)),0) \n" +
                                                "from tbladmissionfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'"+startdate+"')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '"+enddate+"')>=0 \n" +
                                                "and paymentmode=3) \n" +
                                                "+ \n" +
                                                "(select isnull((SUM(receipt_amount)),0) from tblfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'"+startdate+"')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '"+enddate+"')>=0 \n" +
                                                "and paymentmode=3)),0) as daily \n" +
                                                "from tbladmissionfeemaster ";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();

                                        while (rs.next()) {
                                            online = rs.getString("daily");
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
                                /////

                                if(daily.equals("")){
                                    textView.setText("0");
                                }
                                else {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    textView.setText(daily);
                                    btn2.setVisibility(View.VISIBLE);
                                }
                            }
                            else if (s2.getSelectedItem().toString().trim().equalsIgnoreCase("Select Class")) {
                                Toast.makeText(DailyCollectionChoice.this, "Select Class", Toast.LENGTH_LONG).show();
                            }

                            else if (s3.getSelectedItem().toString().trim().equalsIgnoreCase("Select Div")) {
                                Toast.makeText(DailyCollectionChoice.this, "Select Div", Toast.LENGTH_LONG).show();
                            }
                            else {
                                startdate = start.getText().toString();
                                enddate = end.getText().toString();
                                try {
                                    section = s1.getSelectedItem().toString();
                                    std = s2.getSelectedItem().toString();
                                    div = s3.getSelectedItem().toString();
                                } catch (Exception e) {
                                }

                                /////////////For Cash Cal

                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "select\n" +
                                                "distinct isnull(((select isnull((SUM(Fee_paid)),0) \n" +
                                                "from tbladmissionfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'" + startdate + "')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '" + enddate + "')>=0 \n" +
                                                "and paymentmode=1 and class_name='" + std + "' and division='" + div + "') \n" +
                                                "+ \n" +
                                                "(select isnull((SUM(receipt_amount)),0) from tblfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'" + startdate + "')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '" + enddate + "')>=0 \n" +
                                                "and paymentmode=1 and class_id='" + std + "' and division='" + div + "')),0 ) as daily \n" +
                                                "from tbladmissionfeemaster";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();

                                        while (rs.next()) {
                                            cash = rs.getString("daily");
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

                                /////////////For cheque Cal

                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "select\n" +
                                                "distinct isnull(((select isnull((SUM(Fee_paid)),0)\n" +
                                                "from tbladmissionfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'" + startdate + "')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '" + enddate + "')>=0 \n" +
                                                "and paymentmode=2 and class_name='" + std + "' and division='" + div + "') \n" +
                                                "+\n" +
                                                "(select isnull((SUM(receipt_amount)),0) from tblfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'" + startdate + "')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '" + enddate + "')>=0 \n" +
                                                "and paymentmode=2 and class_id='" + std + "' and division='" + div + "')),0 ) as daily \n" +
                                                "from tbladmissionfeemaster";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();
                                        while (rs.next()) {
                                            cheque = rs.getString("daily");
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

                                /////////////For online Cal

                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "select\n" +
                                                "distinct isnull(((select isnull((SUM(Fee_paid)),0)\n" +
                                                "from tbladmissionfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'" + startdate + "')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '" + enddate + "')>=0 \n" +
                                                "and paymentmode=3 and class_name='" + std + "' and division='" + div + "') \n" +
                                                "+\n" +
                                                "(select isnull((SUM(receipt_amount)),0) from tblfeemaster where \n" +
                                                "DATEDIFF(d,convert(varchar,created_on,101),'" + startdate + "')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '" + enddate + "')>=0 \n" +
                                                "and paymentmode=3 and class_id='" + std + "' and division='" + div + "')),0 ) as daily \n" +
                                                "from tbladmissionfeemaster";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();

                                        while (rs.next()) {
                                            online = rs.getString("daily");
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

                                //////////////////
                                try {
                                    ConnectionHelper conStr1 = new ConnectionHelper();
                                    conn = conStr1.connectionclasss();

                                    if (conn == null) {
                                        ConnectionResult = "NO INTERNET";
                                    } else {
                                        String query = "select\n" +
                                                "distinct(select isnull((SUM(Fee_paid)),0) \n" +
                                                "from tbladmissionfeemaster where \n" +
                                                " DATEDIFF(d,convert(varchar,created_on,101),'" + startdate + "')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '" + enddate + "')>=0 \n" +
                                                " and batch_code='" + section + "' and class_name='" + std + "' and division='" + div + "') \n" +
                                                "+\n" +
                                                "(select isnull((select SUM(receipt_amount) from tblfeemaster where \n" +
                                                "  DATEDIFF(d,convert(varchar,created_on,101),'" + startdate + "')<=0 \n" +
                                                "and DATEDIFF(d,convert(varchar,created_on,101), '" + enddate + "')>=0 \n" +
                                                "and batch_code='" + section + "' and class_id='" + std + "' and division='" + div + "'),0)) daily  ";
                                        stmt = conn.prepareStatement(query);
                                        rs = stmt.executeQuery();
                                        while (rs.next()) {
                                            daily = rs.getString("daily");
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

                                if (daily.equals("0")) {
                                    textView.setText("0");
                                } else {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    textView.setText(daily);
                                    btn2.setVisibility(View.VISIBLE);
                                }
                            }

                            progress.cancel();
                        }
                    };
                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable,5000);

                }
            }
        });
    }
}
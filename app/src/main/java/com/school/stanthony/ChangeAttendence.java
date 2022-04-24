package com.school.stanthony;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangeAttendence extends AppCompatActivity {

    EditText roll,name,present;
    List<EditText> allEds = new ArrayList<EditText>();
    List<EditText> allEds1 = new ArrayList<EditText>();
    List<EditText> allEds2 = new ArrayList<EditText>();
    List<EditText> allEds3 = new ArrayList<EditText>();
    //    List<EditText> allEds2 = new ArrayList<EditText>();
//    List<EditText> allEds2 = new ArrayList<EditText>();
    LinearLayout linear1,linear2,linear3,linear4,linear5,linear6;
    int id;
    Button button;
    ArrayList<String> getname;
    ArrayList<String> getroll;
    ArrayList<String> getpresent;
    ArrayList<String> getcode;
    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;
    Boolean isSuccess;
    String ConnectionResult,date,daystatus,acadmic,tname,Form1,section1,section2;
    RadioGroup radioGroup;
    RadioButton radiotext;
    TextView showdate;
    SharedPreferences sharedPref;
    RadioButton work,holi,un;
    String checkpresent="0";

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(ChangeAttendence.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    /////////////////// For Teacher Name
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select name,selectedaca from tbl_HRStaffnew where staffuser='"+Form1+"'\n" +
                                    "and acadmic_year=(select TOP(1) selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"')\n";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            while (rs.next()) {
                                tname= rs.getString("name");
                                acadmic = rs.getString("selectedaca");
                            }
                            ConnectionResult = " Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    } catch (android.database.SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    /////////////////// For Section
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select class_name,division from tblclassmaster where acadmic_year=(select TOP(1) selectedaca from tbl_hrstaffnew where staffuser='"+Form1+"') \n" +
                                    "and assigneteacher='" + tname + "'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            while (rs.next()) {

                                section1 = rs.getString("class_name");
                                section2 = rs.getString("division");
                            }
                            ConnectionResult = " Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    } catch (android.database.SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    ////////////  get day status

                    try {
                        ConnectionHelper conStr1 = new ConnectionHelper();
                        conn = conStr1.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {

                            String query = "select distinct day_status from tblstudentattendencedetails where acadmic_year='"+acadmic+"' and class_name='"+section1+"' and division='"+section2+"'\n" +
                                    "and CONVERT(varchar(10),AttendenceDate,103)='"+date+"'";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();

                            while (rs.next()) {
                                daystatus= rs.getString("day_status");
                                if(daystatus.equals("1")){
                                    radioGroup.check(R.id.work);
                                    work.setAlpha(0.3f);
                                    work.setEnabled(false);
                                    loaddata();
                                }else if(daystatus.equals("2")){
                                    radioGroup.check(R.id.holi);
                                    holi.setAlpha(0.3f);
                                    holi.setEnabled(false);
                                    loaddata();
                                }else{
                                    radioGroup.check(R.id.un);
                                    un.setAlpha(0.3f);
                                    un.setEnabled(false);
                                    loaddata();
                                }
                            }
                            ConnectionResult = " Successful";
                            isSuccess = true;
                            conn.close();
                        }
                    } catch (android.database.SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    progress.dismiss();
                }
            }, 500);
        }};

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(ChangeAttendence.this);
            progress.setTitle("Updating Attendance");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    takeattendance();
                    progress.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAttendence.this);
                    builder.setMessage("Attendance Successfull");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(ChangeAttendence.this, WorkingDay.class);
                            startActivity(i);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 500);
        }};

    Handler mainHandler2 = new Handler(Looper.getMainLooper());
    Runnable myRunnable2 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(ChangeAttendence.this);
            progress.setTitle("Updating Attendance");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    takeattendance2();
                    progress.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAttendence.this);
                    builder.setMessage("Attendance Successfull");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(ChangeAttendence.this, WorkingDay.class);
                            startActivity(i);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 500);
        }};

    Handler mainHandler3 = new Handler(Looper.getMainLooper());
    Runnable myRunnable3 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(ChangeAttendence.this);
            progress.setTitle("Updating Attendance");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    takeattendance3();
                    progress.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAttendence.this);
                    builder.setMessage("Attendance Successfull");
                    builder.setCancelable(false);
                    builder.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(ChangeAttendence.this, WorkingDay.class);
                            startActivity(i);
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }, 500);
        }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_attendence);

        sharedPref = getSharedPreferences("teacherref",MODE_PRIVATE);
        Form1 = sharedPref.getString("Teachercode", null);

        linear1=findViewById(R.id.id1);
        linear2=findViewById(R.id.id2);
        linear3=findViewById(R.id.id3);
        linear4=findViewById(R.id.id4);
        linear5=findViewById(R.id.id5);
        linear6=findViewById(R.id.id6);
        button=findViewById(R.id.btn);
        radioGroup=findViewById(R.id.group1);
        showdate=findViewById(R.id.showdate);
        work=findViewById(R.id.work);
        holi=findViewById(R.id.holi);
        un=findViewById(R.id.un);

        getroll=new ArrayList<>();
        getname=new ArrayList<>();
        getpresent=new ArrayList<>();
        getcode=new ArrayList<>();

        date=getIntent().getExtras().getString("date");
        showdate.setText(date);
        mainHandler.post(myRunnable);

        work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpresent="1";
                holi.setEnabled(false);
                holi.setAlpha(0.3f);
                un.setEnabled(false);
                un.setAlpha(0.3f);

                for (int j = 0; j < getroll.size(); j++) {
                    linear3.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT));
                    //   linear4.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linear5.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT));
                    present = new EditText(ChangeAttendence.this);
                    allEds3.add(present);
                    present.setId(id);
                    present.setText("P");
                    present.setGravity(Gravity.CENTER);
                    present.setTextColor(Color.GREEN);
                    present.setTextSize(18);
                    present.setAllCaps(true);
                    present.addTextChangedListener(new CustomTextWatcher(present));
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(1);
                    present.setFilters(filterArray);
                    present.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                    present.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    present.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    linear4.addView(present);
                }
            }
        });


        holi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                work.setEnabled(false);
                work.setAlpha(0.3f);
                un.setEnabled(false);
                un.setAlpha(0.3f);

                for (int j = 0; j < getroll.size(); j++) {

                    linear3.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT));
                    present = new EditText(ChangeAttendence.this);
                    allEds2.add(present);
                    present.setId(id);
                    present.setText("H");
                    present.setGravity(Gravity.CENTER);
                    present.setTextColor(Color.BLUE);
                    present.setFocusable(false);
                    present.setTextSize(18);
                    present.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    present.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    linear4.addView(present);
                }
            }
        });

        un.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                work.setEnabled(false);
                work.setAlpha(0.3f);
                holi.setEnabled(false);
                holi.setAlpha(0.3f);

                for (int j = 0; j < getroll.size(); j++) {

                    linear3.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT));
                    linear4.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT));
                    present = new EditText(ChangeAttendence.this);
                    allEds2.add(present);
                    present.setId(id);
                    present.setText("U");
                    present.setGravity(Gravity.CENTER);
                    present.setTextColor(Color.MAGENTA);
                    present.setFocusable(false);
                    present.setTextSize(18);
                    present.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    present.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    linear5.addView(present);
                }
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radiotext = findViewById(selectedId);
                String text = radiotext.getText().toString();

                if(text.equals("Working")){
                    mainHandler1.post(myRunnable1);
                }else  if(text.equals("Holiday")){
                    mainHandler2.post(myRunnable2);
                }else  if(text.equals("Uninstructional")){
                    mainHandler3.post(myRunnable3);
                }

            }
        });
    }

    public void loaddata(){

        ////get rollno and name

        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select applicant_type,roll_number,surname+' '+name name  from tbladmissionfeemaster where acadmic_year='"+acadmic+"' \n" +
                        "and class_name='"+section1+"' and division='"+section2+"' and iscancelled='0' and applicant_type!='new' order by roll_number";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String cod = rs.getString("applicant_type");
                    getcode.add(cod);

                    String fullname = rs.getString("roll_number");
                    getroll.add(fullname);

                    String roll = rs.getString("name");
                    getname.add(roll);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        ////get present
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select Present from tblstudentattendencedetails where acadmic_year='"+acadmic+"' and class_name='"+section1+"' and division='"+section2+"'\n" +
                        "and CONVERT(varchar(10),AttendenceDate,103)='"+date+"' ";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next()) {
                    String fullname = rs.getString("Present");
                    getpresent.add(fullname);
                }

                ConnectionResult = "Successful";
                isSuccess = true;
                conn.close();
            }
        } catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }


        ////
        for (int j = 0; j < getroll.size(); j++) {

            roll = new EditText(ChangeAttendence.this);
            allEds.add(roll);
            roll.setId(id);
            roll.setText(getroll.get(j));
            roll.setFocusable(false);
            roll.setTextSize(18);
            roll.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            roll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear1.addView(roll);
        }

        for (int j = 0; j < getroll.size(); j++) {

            name = new EditText(ChangeAttendence.this);
            allEds1.add(name);
            name.setId(id);
            name.setText(getname.get(j));
            name.setFocusable(false);
            name.setTextSize(18);
            name.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            name.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear2.addView(name);
        }

        for (int j = 0; j < getroll.size(); j++) {

            present = new EditText(ChangeAttendence.this);
            allEds2.add(present);
            present.setId(id);
            present.setText(getpresent.get(j));
            present.setAllCaps(true);
            int maxLength = 1;
            present.setTextSize(18);
            present.setGravity(Gravity.CENTER);
            present.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(1);
            present.setFilters(filterArray);
            present.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            present.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            linear3.addView(present);
            present.addTextChangedListener(new CustomTextWatcher(present));

            if(present.getText().toString().equals("P")){
                present.setTextColor(Color.GREEN);
            }
            else if(present.getText().toString().equals("H")){
                present.setTextColor(Color.BLUE);
                present.setFocusable(false);
            }
            else if(present.getText().toString().equals("U")){
                present.setTextColor(Color.MAGENTA);
                present.setFocusable(false);
            } else {
                present.setTextColor(Color.RED);
            }
        }
    }

    public void takeattendance(){

        for (int j = 0; j < getroll.size(); j++){

            if(checkpresent.equals("0")){
                String code=getcode.get(j);
                String roll=getroll.get(j);
                String present=allEds2.get(j).getText().toString();

                if(present.equals("")){
                    present="P";
                }

                if(!present.equals("P")){
                    present="A";
                }

                String msg = "unknown";
                ///get acadmic
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String commands = "update tblstudentattendencedetails set present='"+present+"' ,day_status='1' where acadmic_year='"+acadmic+"' and studentcode='"+code+"'\n" +
                                "and CONVERT(varchar(10),AttendenceDate,103)='"+date+"' ";
                        PreparedStatement preStmt = conn.prepareStatement(commands);
                        preStmt.executeUpdate();
                    } }
                catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                }
            }
            else {
                String code = getcode.get(j);
                String roll = getroll.get(j);
                String present = allEds3.get(j).getText().toString();


                if(present.equals("")){
                    present="P";
                }

                if(!present.equals("P")){
                    present="A";
                }

                String msg = "unknown";
                ///get acadmic
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();

                    if (conn == null) {
                        ConnectionResult = "NO INTERNET";
                    } else {
                        String commands = "update tblstudentattendencedetails set present='"+present+"' ,day_status='1' where acadmic_year='"+acadmic+"' and studentcode='"+code+"'\n" +
                                "and CONVERT(varchar(10),AttendenceDate,103)='"+date+"' ";
                        PreparedStatement preStmt = conn.prepareStatement(commands);
                        preStmt.executeUpdate();
                    } }
                catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                }
            }

        }
    }

    public void takeattendance2(){

        for (int j = 0; j < getroll.size(); j++){

            String msg = "unknown";

            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tblstudentattendencedetails set present='H',day_status='2' where acadmic_year='"+acadmic+"' and CONVERT(varchar(10),AttendenceDate,103)='"+date+"'\n" +
                            "and class_name='"+section1+"' and division='"+section2+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }

    public void takeattendance3(){

        for (int j = 0; j < getroll.size(); j++){

            String msg = "unknown";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                conn = conStr.connectionclasss();

                if (conn == null) {
                    ConnectionResult = "NO INTERNET";
                } else {
                    String commands = "update tblstudentattendencedetails set present='U',day_status='3' where acadmic_year='"+acadmic+"' and CONVERT(varchar(10),AttendenceDate,103)='"+date+"'\n" +
                            "and class_name='"+section1+"' and division='"+section2+"'";
                    PreparedStatement preStmt = conn.prepareStatement(commands);
                    preStmt.executeUpdate();
                } }
            catch (SQLException e) {
                isSuccess = false;
                ConnectionResult = e.getMessage();
            }
        }
    }
}

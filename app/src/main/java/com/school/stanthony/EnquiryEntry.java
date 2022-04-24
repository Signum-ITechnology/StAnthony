package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EnquiryEntry extends AppCompatActivity {

    EditText text1,text2,text5,text6,text7,text4,text9,text10,text11;
    String s1,s2,s3,s4,s5,s6,s7,s9,s10,s11,ConnectionResult,gender,dob;
    Button button;
    Spinner text3;
    SharedPreferences sharedPreferences;
    String getdate,createdby,acadmic,Form1;
    Connection conn;
    ResultSet rs;
    PreparedStatement stmt;
    Boolean isSuccess;
    RadioGroup radioGroup;
    RadioButton radiotext;

    Handler mainHandler = new Handler(Looper.getMainLooper());
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(EnquiryEntry.this);
            progress.setTitle("Loading");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loaddata();
                    progress.dismiss();
                }
            }, 1000);
        }};

    Handler mainHandler1 = new Handler(Looper.getMainLooper());
    Runnable myRunnable1 = new Runnable() {
        @Override
        public void run() {

            final ProgressDialog progress = new ProgressDialog(EnquiryEntry.this);
            progress.setTitle("Entering Enquiry Details");
            progress.setMessage("Please Wait a Moment");
            progress.setCancelable(false);
            progress.show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String convertdate=text2.getText().toString();
                    // convert date
                    try {
                        ConnectionHelper conStr1 = new ConnectionHelper();
                        conn = conStr1.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select convert(varchar,cast('" + convertdate + "' as datetime),103)showdate";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            while (rs.next()) {
                                dob = rs.getString("showdate");
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


                    String msg = "unknown";
                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        conn = conStr.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String commands = "insert into EnquiryRegistration values('"+getdate+"','"+s1+"','"+gender+"','"+s3+"','"+dob+"','"+s5+"','"+s4+"','"+s6+"','"+s7+"','"+s9+"','"+s10+"','','"+acadmic+"','"+createdby+"','','',getdate(),'','','"+s11+"','','')";
                            PreparedStatement preStmt = conn.prepareStatement(commands);
                            preStmt.executeUpdate();
                        }
                    }
                    catch (SQLException e) {
                        isSuccess = false;
                        ConnectionResult = e.getMessage();
                    }

                    button.setVisibility(View.INVISIBLE);
                    progress.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(EnquiryEntry.this);
                    builder.setMessage("Enquiry Details Added");
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
            }, 2000);
        }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry_entry);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPreferences = getSharedPreferences("otherref",MODE_PRIVATE);
        Form1 = sharedPreferences.getString("Othercode", null);

        text1= findViewById(R.id.text1);
        text2= findViewById(R.id.text2);
        text3= findViewById(R.id.text3);
        text4= findViewById(R.id.text4);
        text5= findViewById(R.id.text5);
        text6= findViewById(R.id.text6);
        text7= findViewById(R.id.text7);
        text9= findViewById(R.id.text9);
        text10= findViewById(R.id.text10);
        text11= findViewById(R.id.text11);
        button= findViewById(R.id.login);
        radioGroup=findViewById(R.id.group1);

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setFocusableInTouchMode(true);
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setFocusableInTouchMode(true);
            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text4.setFocusableInTouchMode(true);
            }
        });

        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text5.setFocusableInTouchMode(true);
            }
        });

        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text6.setFocusableInTouchMode(true);
            }
        });

        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text7.setFocusableInTouchMode(true);
            }
        });

        text9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text9.setFocusableInTouchMode(true);
            }
        });

        text10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text10.setFocusableInTouchMode(true);
            }
        });

        text11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text11.setFocusableInTouchMode(true);
            }
        });

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dateDialog = new DatePickerDialog(view.getContext(), datePickerListener, mYear, mMonth, mDay);
                dateDialog.getDatePicker().setMaxDate(new Date().getTime());
                dateDialog.show();
            }
        });

        mainHandler.post(myRunnable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(text1.getText())) {
                    text1.setError("Please Enter Your Name");
                    Toast.makeText(EnquiryEntry.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(text2.getText())) {
                    text2.setError("Please Select Your DOB");
                    Toast.makeText(EnquiryEntry.this, "Please Select Your DOB", Toast.LENGTH_LONG).show();
                } else  if (text3.getSelectedItem().toString().trim().equalsIgnoreCase("Please Select Your Standard")) {
                    Toast.makeText(EnquiryEntry.this, "Please Select Your Standard", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(text6.getText())) {
                    text6.setError("Please Enter Your Parent Name");
                    Toast.makeText(EnquiryEntry.this, "Please Enter Parent Name", Toast.LENGTH_LONG).show();
                }  else if (text7.getText().toString().trim().length()<10 || text7.getText().toString().trim().length()>10) {
                    text7.setError("Please Enter 10 Digits Contact Number");
                    Toast.makeText(EnquiryEntry.this, "Please Enter 10 Digits Number", Toast.LENGTH_LONG).show();
                }  else{
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radiotext = findViewById(selectedId);
                    gender = radiotext.getText().toString();

                    s1=text1.getText().toString();
                    s2=text2.getText().toString();
                    s3=text3.getSelectedItem().toString();
                    s4=text4.getText().toString();
                    s5=text5.getText().toString();
                    s6=text6.getText().toString();
                    s7=text7.getText().toString();
                    s9=text9.getText().toString();
                    s10=text10.getText().toString();
                    s11=text11.getText().toString();

                    mainHandler1.post(myRunnable1);

                }

            }
        });
    }

    private void loaddata(){

        ///get current date and time
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select CONVERT(varchar(10),getdate(),110) date";

                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    getdate = rs.getString("date");
                }
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        ///get staffid and acadmic year
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select Staff_ID,acadmic_year from tbl_HRStaffnew where StaffUser='"+Form1+"'\n" +
                        "and acadmic_year=(select max(acadmic_year) from tbl_hrstaffnew where staffuser='"+Form1+"')";

                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    createdby= rs.getString("Staff_ID");
                    acadmic= rs.getString("acadmic_year");
                }
            }
        }
        catch (SQLException e) {
            isSuccess = false;
            ConnectionResult = e.getMessage();
        }

        /////////////////// For Div
        try {
            ConnectionHelper conStr = new ConnectionHelper();
            conn = conStr.connectionclasss();

            if (conn == null) {
                ConnectionResult = "NO INTERNET";
            } else {
                String query = "select class_name from Enquiryclass where acadmic_year=(select max(acadmic_year) from tbl_hrstaffnew where staffuser='"+Form1+"')";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next()) {
                    String fullname = rs.getString("class_name");
                    data2.add(fullname);
                }
                String[] array2 = data2.toArray(new String[0]);
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.spinner11, data2);
                text3.setAdapter(NoCoreAdapter2);
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

    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);
            String format = new SimpleDateFormat("dd MMM yyyy").format(c.getTime());
            text2.setText(format);
            text4.setText(Integer.toString(calculateAge(c.getTimeInMillis())));
        }
    };

    int calculateAge(long date){
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(date);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH)){
            age--;
        }
        return age;
    }
}
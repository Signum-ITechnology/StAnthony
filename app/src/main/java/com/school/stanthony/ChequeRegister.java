package com.school.stanthony;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class ChequeRegister extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radiotext;
    String studcode;
    EditText date1,date2;
    Spinner s1;
    Button btn;
    String cid,section,startdate,enddate;
    AutoCompleteTextView code;
    Connection conn;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    SharedPreferences sharedPref;
    String ConnectionResult,Form1;
    PreparedStatement stmt;
    ResultSet rt,rs;
    Boolean isSuccess;
    TextView showstartdate,showenddate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_register);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sharedPref = getSharedPreferences("adminref", MODE_PRIVATE);
        Form1 = sharedPref.getString("Admincode", null);

        radioGroup=findViewById(R.id.group1);
        date1=findViewById(R.id.date1);
        date2=findViewById(R.id.date2);
        s1=findViewById(R.id.s1);
        code=findViewById(R.id.code);
        btn=findViewById(R.id.btn);
        showstartdate=findViewById(R.id.showstartdate);
        showenddate=findViewById(R.id.showenddate);

        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                code.setFocusableInTouchMode(true);
            }
        });

/////////////////////////////// start time

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(ChequeRegister.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                date1.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
//                                getmoonth = monthOfYear + 1;

                                showstartdate.setText( (monthOfYear + 1)+ "-" + dayOfMonth + "-" + year);
                                date1.setText( dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                            }
                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

/////////////////////////// End time

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(ChequeRegister.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                date2.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
////                                getmoonth = monthOfYear + 1;

                                showenddate.setText( (monthOfYear + 1)+ "-" + dayOfMonth + "-" + year);
                                date2.setText( dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                                getmoonth=monthOfYear+1;
                            }

                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

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
                String query = "select CONVERT(varchar(10),getdate(),110) sysdate ,CONVERT(varchar(10),getdate(),103) showdate";
                stmt = conn.prepareStatement(query);
                rs = stmt.executeQuery();

                while (rs.next())
                {
                    String fullname = rs.getString("showdate");
                    date1.setText(fullname);
                    date2.setText(fullname);

                    String show = rs.getString("sysdate");
                    showstartdate.setText(show);
                    showenddate.setText(show);
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

        /////////////////// For Section

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
                        "union all \n" +
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
                ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(ChequeRegister.this,R.layout.spinner11, data2);
                s1.setAdapter(NoCoreAdapter2);
                ConnectionResult = "Successful";
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

//////////////////////////////////code
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    section=s1.getSelectedItem().toString();
                }catch (Exception e){}
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    conn = conStr.connectionclasss();
                    if (conn == null)
                    {
                        ConnectionResult="NO INTERNET";
                    }
                    else {
                        String query = "select Applicant_Type from tbladmissionfeemaster where Batch_Code='"+section+"'\n" +
                                "       and Acadmic_year=(select selectedaca from tblusermaster where username='"+Form1+"')";
                        stmt = conn.prepareStatement(query);
                        rt = stmt.executeQuery();
                        ArrayList<String> data = new ArrayList<>();
                        while (rt.next()) {
                            String cid = rt.getString("Applicant_type");
                            data.add(cid);
                        }
                        String[] array = data.toArray(new String[0]);
                        ArrayAdapter NoCoreAdapter = new ArrayAdapter(ChequeRegister.this,android.R.layout.simple_list_item_1, data);
                        code.setAdapter(NoCoreAdapter);
                        code.setThreshold(0);
                        ConnectionResult = "Successful";
                        isSuccess = true;
                        conn.close();
                    }
                }
                catch (SQLException e) {
                    isSuccess = false;
                    ConnectionResult = e.getMessage();
                } catch (java.sql.SQLException e) {
                    e.printStackTrace();
                }

                code.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        studcode=(String)adapterView.getItemAtPosition(i);
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ////////////////////
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(date1.getText())) {
                    Toast.makeText(ChequeRegister.this, "Please Select Start Date", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(date2.getText())) {
                    Toast.makeText(ChequeRegister.this, "Please Select End Date", Toast.LENGTH_LONG).show();
                } else if (s1.getSelectedItem().toString().trim().equalsIgnoreCase("Select Section")) {
                    Toast.makeText(ChequeRegister.this, "Please Select Section", Toast.LENGTH_LONG).show();
                } else {
                    section = s1.getSelectedItem().toString();
                    startdate = showstartdate.getText().toString();
                    enddate = showenddate.getText().toString();
                    studcode = code.getText().toString();

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    radiotext = findViewById(selectedId);
                    String text = radiotext.getText().toString();
                    if (text.equals("ALL")) {
                        if (studcode.equals("YYY")) {
                            Intent intent = new Intent(getApplicationContext(), ChequeRegisterYYY.class);
                            intent.putExtra("date1", startdate);
                            intent.putExtra("date2", enddate);
                            intent.putExtra("section", section);
                            intent.putExtra("radiotext", text);
                            startActivity(intent);
                        } else if (!studcode.equals("YYY")) {
                            Intent intent = new Intent(getApplicationContext(), ChequeRegisterStudent.class);
                            intent.putExtra("date1", startdate);
                            intent.putExtra("date2", enddate);
                            intent.putExtra("section", section);
                            intent.putExtra("radiotext", text);
                            intent.putExtra("studcode", studcode);
                            startActivity(intent);
                        }
                    } else if (text.equals("CLEAR")) {
                        if (studcode.equals("YYY")) {
                            Intent intent = new Intent(getApplicationContext(), ChequeRegisterYYY.class);
                            intent.putExtra("date1", startdate);
                            intent.putExtra("date2", enddate);
                            intent.putExtra("section", section);
                            intent.putExtra("radiotext", text);
                            startActivity(intent);
                        } else if (!studcode.equals("YYY")) {
                            Intent intent = new Intent(getApplicationContext(), ChequeRegisterStudent.class);
                            intent.putExtra("date1", startdate);
                            intent.putExtra("date2", enddate);
                            intent.putExtra("section", section);
                            intent.putExtra("radiotext", text);
                            intent.putExtra("studcode", studcode);
                            startActivity(intent);
                        }
                    } else if (text.equals("BOUNCE")) {
                        if (studcode.equals("YYY")) {
                            Intent intent = new Intent(getApplicationContext(), ChequeRegisterYYY.class);
                            intent.putExtra("date1", startdate);
                            intent.putExtra("date2", enddate);
                            intent.putExtra("section", section);
                            intent.putExtra("radiotext", text);
                            startActivity(intent);
                        } else if (!studcode.equals("YYY")) {
                            Intent intent = new Intent(getApplicationContext(), ChequeRegisterStudent.class);
                            intent.putExtra("date1", startdate);
                            intent.putExtra("date2", enddate);
                            intent.putExtra("section", section);
                            intent.putExtra("radiotext", text);
                            intent.putExtra("studcode", studcode);
                            startActivity(intent);
                        }
                    } else if (text.equals("NOT ENTERED")) {
                        if (studcode.equals("YYY")) {
                            Intent intent = new Intent(getApplicationContext(), ChequeRegisterYYY.class);
                            intent.putExtra("date1", startdate);
                            intent.putExtra("date2", enddate);
                            intent.putExtra("section", section);
                            intent.putExtra("radiotext", text);
                            startActivity(intent);
                        } else if (!studcode.equals("YYY")) {
                            Intent intent = new Intent(getApplicationContext(), ChequeRegisterStudent.class);
                            intent.putExtra("date1", startdate);
                            intent.putExtra("date2", enddate);
                            intent.putExtra("section", section);
                            intent.putExtra("radiotext", text);
                            intent.putExtra("studcode", studcode);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}
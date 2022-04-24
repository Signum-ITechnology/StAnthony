package com.school.stanthony;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
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

public class ExpenditureRegister extends AppCompatActivity {

    EditText start, end;
    Button btn,btn2;
    TextView textView;
    LinearLayout linearLayout;
    Calendar c;
    int mMonth, getmoonth;
    DatePickerDialog dpd;
    PreparedStatement stmt;
    ResultSet rs;
    Spinner spinner;
    Connection conn;
    String ConnectionResult,total,startdate,enddate;
    Boolean isSuccess;
    TextView showstartdate,showenddate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenditure_register);

        start = findViewById(R.id.date);
        end = findViewById(R.id.date1);
        textView = findViewById(R.id.cost);
        btn = findViewById(R.id.btn);
        btn2 = findViewById(R.id.btn2);
        linearLayout=findViewById(R.id.linear);
        spinner=findViewById(R.id.spinner);
        showstartdate=findViewById(R.id.showstartdate);
        showenddate=findViewById(R.id.showenddate);

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
                ArrayList<String> data = new ArrayList<>();
                ArrayList<String> data2 = new ArrayList<>();
                while (rs.next())
                {
//                    String fullname = rs.getString("sysdate");
//                    data2.add(fullname);
//                    startdate=data2.get(0);
//                    enddate=data2.get(0);
//                    start.setText(startdate);
//                    end.setText(enddate);

                    String fullname = rs.getString("showdate");
                    data2.add(fullname);
                    start.setText(data2.get(0));
                    end.setText(data2.get(0));

                    String show = rs.getString("sysdate");
                    data.add(show);
                    showstartdate.setText(data.get(0));
                    showenddate.setText(data.get(0));
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


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                dpd = new DatePickerDialog(ExpenditureRegister.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
//                                start.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
//                                getmoonth = monthOfYear + 1;

                                showstartdate.setText( (monthOfYear + 1)+ "-" + dayOfMonth + "-" + year);
                                start.setText( dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                            }
                        }, mYear, mMonth, mDay
                );

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

                dpd = new DatePickerDialog(ExpenditureRegister.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
//                                end.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
//                                getmoonth = monthOfYear + 1;
                                showenddate.setText( (monthOfYear + 1)+ "-" + dayOfMonth + "-" + year);
                                end.setText( dayOfMonth + "/"+(monthOfYear + 1)+ "/" + year);
                                getmoonth=monthOfYear+1;
                            }

                        }, mYear, mMonth, mDay
                );
                dpd.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(start.getText())) {
                    Toast.makeText(ExpenditureRegister.this, "Please Select Start Date", Toast.LENGTH_LONG).show();
                } else  if (TextUtils.isEmpty(end.getText())) {
                    Toast.makeText(ExpenditureRegister.this, "Please Select End Date", Toast.LENGTH_LONG).show();
                }else {

                    startdate = showstartdate.getText().toString();
                    enddate = showenddate.getText().toString();

                    final ProgressDialog progress = new ProgressDialog(ExpenditureRegister.this);
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

                    try {
                        ConnectionHelper conStr1 = new ConnectionHelper();
                        conn = conStr1.connectionclasss();

                        if (conn == null) {
                            ConnectionResult = "NO INTERNET";
                        } else {
                            String query = "select isnull((SUM(amount)),0) as total from tblExpenditure_Entry  where Acadmic_year=(select companycode from tblcompanymaster where isselected=1)\n" +
                                    " and  DATEDIFF(d,convert(varchar,Expenditure_date,101),'"+startdate+"')<=0 and \n" +
                                    "DATEDIFF(d,convert(varchar,Expenditure_date,101),'"+enddate+"')>=0 ";
                            stmt = conn.prepareStatement(query);
                            rs = stmt.executeQuery();
                            ArrayList<String> data2 = new ArrayList<>();
                            while (rs.next()) {
                                String fullname = rs.getString("total");
                                data2.add(fullname);
                            }
                            String[] array2 = data2.toArray(new String[0]);
                            ArrayAdapter NoCoreAdapter2 = new ArrayAdapter(ExpenditureRegister.this,android.R.layout.simple_list_item_1,data2);
                            spinner.setAdapter(NoCoreAdapter2);
                            try {
//                                if(NoCoreAdapter2.getCount()==0) {
//                                    total = "0";
//                                }
//                                else {
                                    total = spinner.getSelectedItem().toString();
                               // }
                            } catch (Exception e) {
                                //
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

                 //   progress.cancel();

                    if(total.equals("0.00")){
                        textView.setText("0");
                        linearLayout.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                    }
                    else {
                        textView.setText(total);
                        linearLayout.setVisibility(View.VISIBLE);
                        btn2.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(total.equals("0.00")){
                    Toast.makeText(ExpenditureRegister.this, "No Records To Show", Toast.LENGTH_LONG).show();
                }
                else if(total.equals("")){
                    Toast.makeText(ExpenditureRegister.this, "No Records To Show", Toast.LENGTH_LONG).show();
                }
                else {
                    startdate = showstartdate.getText().toString();
                    enddate = showenddate.getText().toString();
                    Intent i=new Intent(getApplicationContext(),ExpenditureRegisterDetails.class);
                    i.putExtra("start",startdate);
                    i.putExtra("end",enddate);
                    startActivity(i);
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